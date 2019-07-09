package com.imfpmo.app;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

public class LocationUpdatesService extends Service {

    private static final String TAG = LocationUpdatesService.class.getSimpleName();

    private static final int NOTIFICATION_ID = 223;
    public static final String CHANNEL_ID = "channel_00"; //private
    static final String PREFERENCE_FILE_KEY = "com.example.mobileapp_praktikum.PREFERENCE_FILE_KEY";
    static final String DB_STATUS_EMPTY = "com.example.mobileapp_praktikum.cache_status";
    static final String DB_CREATED = "com.example.mobileapp_praktikum.cache_created_status";
    static final String DB_NAME = "local_json_locations_database";


    private static String userId;
    private static String securityToken;

    private FusedLocationProviderClient fusedLocationClient;
    private LocationRequest mLocationRequest;
    private Handler mServiceHandler;
    private NotificationManager mNotificationManager;
    private static ToSendDatabase mlocalDatabase;


    //trackingtester@rwth.de and trackingtester2@rwth.de
    //1234

    public LocationUpdatesService() {
    }

    //furt
    @Override
    public void onCreate() {
        Log.w(TAG, "onCreate");
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        createLocationRequest();


        //why?
        HandlerThread handlerThread = new HandlerThread(TAG);
        handlerThread.start();
        mServiceHandler = new Handler(handlerThread.getLooper());
        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        Usermanagement instance = Usermanagement.getInstance();
        userId = instance.getUserID();
        securityToken = instance.getSecurityToken();

        if (mlocalDatabase == null)
            //createLocalDB();

        // Android O requires a Notification Channel.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.app_name);
            // Create the channel for the notification
            // only do once. check through static var or through a get method
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

        //stopSendingLocUpdates():
        //needs work. doesnt send stored locations
        fusedLocationClient.flushLocations();
        fusedLocationClient.removeLocationUpdates(pendingIntentCreator());

        super.onDestroy();
        mServiceHandler.removeCallbacksAndMessages(null);

        //global onKillIntent if it came from button or from system nu neaparat onBind. poate chiar direct stopService in main
        //checkif an intent came from onBind or from system kill if onBind dontsend restart else do next:
        //sendBroadcast(new Intent(this, RestartServiceBroadcast.class));
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
        //you bind only when u want to shutServiceDown (impl prin intent action) also check if service is running or not
    }


    //helpers

    private void createLocationRequest() {
        Log.w(TAG, "createLocationRequest");
        final long UPDATE_INTERVAL = 20 * 1000;

        final long FASTEST_UPDATE_INTERVAL = UPDATE_INTERVAL;

        final long MAX_WAIT_TIME = UPDATE_INTERVAL * 3;// * 120 ;

        //real values are 30k 28k 5h

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
        //Helpers.setRequestingLocationUpdates(this, true);
        //startService(new Intent(getApplicationContext(), LocationUpdatesService.class));
        try {
            fusedLocationClient.requestLocationUpdates(mLocationRequest,
                    pendingIntentCreator());
        } catch (SecurityException unlikely) {
            //Helpers.setRequestingLocationUpdates(this, false);
            Log.e(TAG, "Lost location permission. Could not request updates. " + unlikely);
        }
    }

    //furt
    private Notification getNotification() {

        CharSequence text = "Currently tracking location";

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

    private PendingIntent pendingIntentCreator() {
        Log.w(TAG, "pendingIntentCreator");
        Intent intent = new Intent(this, LocationDeliveryBroadcastReciever.class);
        intent.setAction(LocationDeliveryBroadcastReciever.ACTION_LOCATION_DELIVERY);
        //this is just a bcast rec start intent and doesnt coincide with the intent delivered to the bcast rec.
        //intent.putExtra(EXTRA_STRING_USERID, userId);
        //intent.putExtra(EXTRA_STRING_USERTOKEN, userToken);

        //solution:
        //Location Client request location updates with parcelable extras in PendingIntent bookm
        //use bundle instead of extra


        return PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }


/*
    private void createLocalDB() {

        mlocalDatabase = Room.databaseBuilder(getApplicationContext(),
                ToSendDatabase.class, DB_NAME).allowMainThreadQueries().build();


        //optional as null works as well
        /*
        SharedPreferences pref = this.getSharedPreferences(PREFERENCE_FILE_KEY, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean(DB_CREATED, true);
        editor.commit();
        */

    //}

    static ToSendDatabase getLocalDB(){
        return mlocalDatabase;
    }


    static String getUserId(){
        return userId;
    }

    static String getSecurityToken(){
        return securityToken;
    }

}
