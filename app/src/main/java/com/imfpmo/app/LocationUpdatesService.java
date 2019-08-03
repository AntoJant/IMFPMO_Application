package com.imfpmo.app;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.room.Room;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.work.BackoffPolicy;
import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.ExistingWorkPolicy;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.ActivityRecognition;
import com.google.android.gms.location.ActivityRecognitionClient;
import com.google.android.gms.location.ActivityTransition;
import com.google.android.gms.location.ActivityTransitionEvent;
import com.google.android.gms.location.ActivityTransitionRequest;
import com.google.android.gms.location.DetectedActivity;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class LocationUpdatesService extends Service {

    private static final String TAG = LocationUpdatesService.class.getSimpleName();

    private static final int NOTIFICATION_ID = 223;
    public static final String CHANNEL_ID = "channel_00"; //private
    private static final String KEY_USER_ID = "usermanagement_user_id";
    private static final String KEY_SECURITY_TOKEN = "usermanagement_security_token";
    private static final String WORKER_TYPE_REGULAR = "periodic_worker";
    private static final String WORKER_TYPE_ONCE = "logout_worker";
    private static final String KEY_STOP_SERVICE_FROM_NOTIFICATION = "user_stopped_service_from_notification";

    private static final long UPDATE_INTERVAL = 20 * 1000;

    private static final long FASTEST_UPDATE_INTERVAL = UPDATE_INTERVAL;

    private static final long MAX_WAIT_TIME = UPDATE_INTERVAL * 3;// * 120 ;

    static List<ActivityTransitionEvent> currentTransitions;

    private BroadcastReceiver disableFromGPSButtonReceiver;

    //real values are 30k 28k 5h


    private FusedLocationProviderClient fusedLocationClient;
    private ActivityRecognitionClient activityRecognitionClient;
    private static LocationRequest mLocationRequest;
    private NotificationManager mNotificationManager;
    private ActivityTransitionRequest mTransitionRequest;

    public LocationUpdatesService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onCreate() {
        Log.w(TAG, "onCreate");

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        activityRecognitionClient = ActivityRecognition.getClient(this);

        //is necessary to reinit in case of OS killing service
        createLocationRequest();

        initActivityRecognition();

        //initLocationDisabledListener();


        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

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

        if(!Helpers.isSendingWorkerScheduled(this))
            scheduleSendingWorker(this, WORKER_TYPE_REGULAR);


    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.w(TAG, "onStartComm");
        if(intent == null) {
            Log.w(TAG, "service restarted by OS");

            startForeground(NOTIFICATION_ID, getNotification());

            Helpers.setRequestingLocationUpdates(this, true);
            requestActivityRecognitionUpdates();
            requestLocationUpdates();
            return START_STICKY;
        }
        else
            if(intent.getBooleanExtra(KEY_STOP_SERVICE_FROM_NOTIFICATION, false)) {
                Log.w(TAG, "service stopped from notification");
                stopSelf();
                return START_NOT_STICKY;
            }
            else {
                Log.w(TAG, "service started normally");
                startForeground(NOTIFICATION_ID, getNotification());

                Helpers.setRequestingLocationUpdates(this, true);
                requestActivityRecognitionUpdates();
                requestLocationUpdates();
                return START_STICKY;
            }
    }

    // TODO: 7/21/19 take out one or the other
    @Override
    public void onDestroy() {
        //
        Log.w(TAG, "onDestroy service terminated by os/ ended by itself");

        activityRecognitionClient
                .removeActivityTransitionUpdates(pendingIntentCreator("START-RECOGNITION")).addOnSuccessListener(
                        result -> pendingIntentCreator("START-RECOGNITION").cancel());

        //activityRecognitionClient.removeActivityUpdates(pendingIntentCreator("START-RECOGNITION"));

        fusedLocationClient.flushLocations().addOnCompleteListener(
                task -> fusedLocationClient.removeLocationUpdates(pendingIntentCreator("START-UPDATES")));


        Helpers.setRequestingLocationUpdates(this, false);

        super.onDestroy();

    }
    //helpers

    private static void createLocationRequest() {
        Log.w(TAG, "createLocationRequest");

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL);
        mLocationRequest.setMaxWaitTime(MAX_WAIT_TIME);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

    }

    private static void setCheckAndResolveSettings(Activity activity) {

        createLocationRequest();

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequest);
        SettingsClient client = LocationServices.getSettingsClient(activity);
        Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());

        task.addOnFailureListener(e -> {
            if (e instanceof ResolvableApiException) {

                try {
                    ResolvableApiException resolvable = (ResolvableApiException) e;
                    resolvable.startResolutionForResult(activity, 0x1);
                } catch (IntentSender.SendIntentException sendEx) {
                    // Ignore the error.
                }
            }
        });
    }


    private void initActivityRecognition(){
        currentTransitions = new ArrayList<>();
        currentTransitions.add(
                new ActivityTransitionEvent(
                        DetectedActivity.STILL, ActivityTransition.ACTIVITY_TRANSITION_ENTER, SystemClock.elapsedRealtimeNanos()
                )
        );

        Log.w(TAG, "START PADDING AT INIT: " + currentTransitions.get(0));
        List<ActivityTransition> transitions = new ArrayList<>();

        transitions.add(
                new ActivityTransition.Builder()
                    .setActivityType(DetectedActivity.IN_VEHICLE)
                    .setActivityTransition(ActivityTransition.ACTIVITY_TRANSITION_ENTER)
                    .build());

        transitions.add(
                new ActivityTransition.Builder()
                        .setActivityType(DetectedActivity.ON_BICYCLE)
                        .setActivityTransition(ActivityTransition.ACTIVITY_TRANSITION_ENTER)
                        .build());

        transitions.add(
                new ActivityTransition.Builder()
                        .setActivityType(DetectedActivity.STILL)
                        .setActivityTransition(ActivityTransition.ACTIVITY_TRANSITION_ENTER)
                        .build());

        //from here only for testing
        transitions.add(
                new ActivityTransition.Builder()
                        .setActivityType(DetectedActivity.RUNNING)
                        .setActivityTransition(ActivityTransition.ACTIVITY_TRANSITION_ENTER)
                        .build());

        transitions.add(
                new ActivityTransition.Builder()
                        .setActivityType(DetectedActivity.WALKING)
                        .setActivityTransition(ActivityTransition.ACTIVITY_TRANSITION_ENTER)
                        .build());


        mTransitionRequest = new ActivityTransitionRequest(transitions);

    }

    // TODO: 7/21/19 take out activityUpdates
    private void requestActivityRecognitionUpdates(){
        Task<Void> task = activityRecognitionClient
                .requestActivityTransitionUpdates(mTransitionRequest, pendingIntentCreator("START-RECOGNITION"));

        task.addOnSuccessListener(result -> Log.w(TAG, "ActivityRecognition initialization succesful"));
        task.addOnFailureListener(e -> Log.w(TAG, "ActivityRecognition initialization failure: " + e));

        //activityRecognitionClient.requestActivityUpdates(10000, pendingIntentCreator("START-RECOGNITION"));

    }

    private void requestLocationUpdates() {
        Log.i(TAG, "requestLocationUpdates");

        try {
            fusedLocationClient.requestLocationUpdates(mLocationRequest,
                    pendingIntentCreator("START-UPDATES"));
        } catch (SecurityException unlikely) {
            //Helpers.setRequestingLocationUpdates(this, false);
            Log.e(TAG, "Lost location permission. Could not request updates. " + unlikely);
            //stopSelf();
        }
    }

    //furt
    private Notification getNotification() {

        CharSequence text = "Currently Tracking Location";

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentIntent(pendingIntentCreator("STOP"))
                .setContentText("Tap to stop tracking")
                .setContentTitle(text)
                .setOngoing(true)
                .setPriority(Notification.PRIORITY_MIN)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setTicker(text)
                .setWhen(System.currentTimeMillis());

        // Set the Channel ID for Android O.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder.setChannelId(CHANNEL_ID); // Channel ID
        }

        return builder.build();
    }


    private PendingIntent pendingIntentCreator(String motive) {
        Log.w(TAG, "pendingIntentCreator type:" + motive);
        if(motive.equals("START-UPDATES")) {

            Intent intent = new Intent(this, LocationDeliveryBroadcastReceiver.class);
            intent.setAction(LocationDeliveryBroadcastReceiver.ACTION_LOCATION_DELIVERY);

            return PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        }else if(motive.equals("STOP")){

            Intent intent = new Intent(this, LocationUpdatesService.class);
            intent.putExtra(KEY_STOP_SERVICE_FROM_NOTIFICATION,true);

            return PendingIntent.getService(this, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        }else {

            Intent intent = new Intent(this, ActivityRecognitionBroadcastReceiver.class);

            return PendingIntent.getBroadcast(this, 2, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        }
    }



//non testing values: init delay 2 h, repeater 12h(so if fail it sends back in 10h. and !charging


    private static void scheduleSendingWorker(Context context, String type){

        Data userAuthData = new Data.Builder()
                .putString(KEY_USER_ID, Usermanagement.getInstance().getUserID())
                .putString(KEY_SECURITY_TOKEN, Usermanagement.getInstance().getSecurityToken())
                .build();


        if(type.equals(WORKER_TYPE_REGULAR)) {
            Constraints constraints = new Constraints.Builder()
                    .setRequiresCharging(true)
                    .setRequiredNetworkType(NetworkType.UNMETERED)
                    //.setRequiresDeviceIdle(true)
                    .build();

            ExistingPeriodicWorkPolicy existingPolicy = ExistingPeriodicWorkPolicy.KEEP;

            PeriodicWorkRequest saveRequest =
                    new PeriodicWorkRequest.Builder(LocationDeliveryWorker.class, 10, TimeUnit.HOURS)
                            .setConstraints(constraints)
                            .addTag(WORKER_TYPE_REGULAR)
                            .setInputData(userAuthData)
                            .setInitialDelay(1, TimeUnit.MINUTES)
                            .build();


            WorkManager.getInstance(context).enqueueUniquePeriodicWork(WORKER_TYPE_REGULAR, existingPolicy, saveRequest);
            //WorkManager.getInstance(context).enqueue(saveRequest);

            Log.w(TAG, "Sending task scheduled");


            Helpers.setSendingWorkerScheduled(context, true);

        }
        else {
            Constraints constraints = new Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build();

            ExistingWorkPolicy existingPolicy = ExistingWorkPolicy.KEEP;

            OneTimeWorkRequest saveRequest =
                    new OneTimeWorkRequest.Builder(LocationDeliveryWorker.class)
                        .setConstraints(constraints)
                        .addTag(WORKER_TYPE_ONCE)
                        .setInputData(userAuthData)
                        .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, OneTimeWorkRequest.MIN_BACKOFF_MILLIS, TimeUnit.MILLISECONDS)
                        .build();



            WorkManager.getInstance(context).enqueueUniqueWork(WORKER_TYPE_ONCE, existingPolicy, saveRequest);
            //WorkManager.getInstance(context).enqueue(saveRequest);

            Log.w(TAG, "sending remaining data worker scheduled");

        }
    }

    static void sendLastDataAndCancelWorker(Context context){
        Helpers.setSendingWorkerScheduled(context, false);

        WorkManager.getInstance(context).cancelAllWorkByTag(WORKER_TYPE_REGULAR);
        Log.w(TAG, "workers cancelled, one-time-only send-remaining-data worker scheduled");

        scheduleSendingWorker(context, WORKER_TYPE_ONCE);
    }

    static void requestLocationUpdates(Context context){
        if(!Helpers.requestingLocationUpdates(context)) {
            setCheckAndResolveSettings((Activity) context);
            context.startService(new Intent(context, LocationUpdatesService.class));
        }
    }


    static void stopLocationUpdates(Context context){
        if(Helpers.requestingLocationUpdates(context))
            context.stopService(new Intent(context, LocationUpdatesService.class));
    }
}
