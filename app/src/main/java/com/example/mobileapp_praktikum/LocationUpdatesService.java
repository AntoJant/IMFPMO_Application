package com.example.mobileapp_praktikum;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.SystemClock;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.Response;

public class LocationUpdatesService extends Service {

    private static final String TAG = LocationUpdatesService.class.getSimpleName();

    private static final int NOTIFICATION_ID = 223;
    public static final String CHANNEL_ID = "channel_00"; //private

    private FusedLocationProviderClient fusedLocationClient;
    private LocationRequest mLocationRequest;
    private LocationCallback locationCallback;
    private Handler mServiceHandler;
    private NotificationManager mNotificationManager;


    //maybe not needed bcause these are passed in intent (cant be passed by context)
    private static String userToken = "1";
    private static String userId = "1";
    //trackingtester@rwth.de and trackingtester2@rwth.de
    //1234

    public LocationUpdatesService() {
    }

    //furt
    @Override
    public void onCreate() {
        Log.w(TAG, "onCreate");
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        //creates sample user and token
        /*
        JsonObject sampleUserObject = new JsonObject();
        sampleUserObject.addProperty("email", "trackingtester2@rwth.de");
        sampleUserObject.addProperty("password", "1234");
        Ion.with(this)
                .load("http://fjobilabs.de:8383/api/users")
                .setJsonObjectBody(sampleUserObject)
                .asJsonObject()
                .withResponse()
                .setCallback(new FutureCallback<Response<JsonObject>>() {
                    @Override
                    public void onCompleted(Exception e, Response<JsonObject> result) {
                        Log.w(TAG, result.getResult().get("id").getAsString());
                    }
                });
        */







        createLocationRequest();


        //why?
        HandlerThread handlerThread = new HandlerThread(TAG);
        handlerThread.start();
        mServiceHandler = new Handler(handlerThread.getLooper());
        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        // Android O requires a Notification Channel.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.app_name);
            // Create the channel for the notification
            NotificationChannel mChannel =
                    new NotificationChannel(CHANNEL_ID, name, NotificationManager.IMPORTANCE_DEFAULT);

            // Set the Notification Channel for the Notification Manager.
            mNotificationManager.createNotificationChannel(mChannel);
        }
    }


    //possibly in onCreate as these are called each time startService is called
    //actually start service is called only in onCreate so we good
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.w(TAG, "onStartComm");
        startForeground(NOTIFICATION_ID, getNotification());
        requestLocationUpdates();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        //is not necessary?
        Log.w(TAG, "onDestroy service terminated by os/ ended by itself");
        super.onDestroy();
        mServiceHandler.removeCallbacksAndMessages(null);

        //global onKillIntent if it came from button or from system nu neaparat onBind. poate chiar direct stopService in main
        //checkif an intent came from onBind or from system kill if onBind dontsend restart else do next:
        sendBroadcast(new Intent(this, RestartServiceBroadcast.class));
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
        //you bind only when u want to shutServiceDown (impl prin intent action) also check if service is running or not
    }




    //helpers

    private void createLocationRequest() {
        Log.w(TAG, "createLocationRequest");
        final long UPDATE_INTERVAL = 4 * 1000;

        final long FASTEST_UPDATE_INTERVAL = UPDATE_INTERVAL / 2;

        final long MAX_WAIT_TIME = UPDATE_INTERVAL * 1;

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL);
        mLocationRequest.setMaxWaitTime(MAX_WAIT_TIME);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

    }

    //method called by the main to start the tracking
    //not usable without binding
    //atm bypassed through startService in activity and outcommenting startService here
    //also this is called in onStartcommand instead of main.

    //furt
    public void requestLocationUpdates() {
        Log.i(TAG, "requestLocationUpdates");
        Helpers.setRequestingLocationUpdates(this, true);
        //startService(new Intent(getApplicationContext(), LocationUpdatesService.class));
        try {
            fusedLocationClient.requestLocationUpdates(mLocationRequest,
                    pendingIntentCreator());
        } catch (SecurityException unlikely) {
            Helpers.setRequestingLocationUpdates(this, false);
            Log.e(TAG, "Lost location permission. Could not request updates. " + unlikely);
        }
    }

    //furt
    private Notification getNotification() {
        Intent intent = new Intent(this, LocationUpdatesService.class);

        CharSequence text = "service active";

        // Extra to help us figure out if we arrived in onStartCommand via the notification or not.

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentText(text)
                .setContentTitle(text)
                .setOngoing(true)
                .setPriority(Notification.PRIORITY_HIGH)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setTicker(text)
                .setWhen(System.currentTimeMillis());

        // Set the Channel ID for Android O.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder.setChannelId(CHANNEL_ID); // Channel ID
        }

        return builder.build();
    }
    @TargetApi(29)
    private PendingIntent pendingIntentCreator() {
        Log.w(TAG, "pendingIntentCreator");
        Intent intent = new Intent(this, LocationDeliveryBroadcastReciever.class);
        intent.setAction(LocationDeliveryBroadcastReciever.ACTION_LOCATION_DELIVERY);
        //fucks shit up cuz of handleintent in bdcast rec
        //intent.putExtra("userId", userId);
        //intent.putExtra("userToken", userToken);
        //startService
        return PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

}
