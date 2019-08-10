package com.imfpmo.app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.SystemClock;
import android.util.Log;

import com.google.android.gms.location.ActivityTransition;
import com.google.android.gms.location.ActivityTransitionEvent;
import com.google.android.gms.location.DetectedActivity;
import com.google.android.gms.location.LocationResult;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;


/**
 * Broadcast Receiver created through the Fused Location Provider Client pending intent.
 * It receives the locations in the onReceive method (and checks if the Location button is still on).
 * Then it handles storing them in the local database while also determining their mode through
 * the stored Activity updates.
 */
public class LocationDeliveryBroadcastReceiver extends BroadcastReceiver {

    public static final String TAG = LocationDeliveryBroadcastReceiver.class.getSimpleName();
    public static final String ACTION_LOCATION_DELIVERY = "com.example.mobileapp_praktikum.action.locdelivery";
    private static final int DEFAULT_UNKNOWN_VEHICLE_VALUE = 100;




    @Override
    public void onReceive(Context context, Intent intent) {



        LocationResult locationResult = LocationResult.extractResult(intent);
        if (locationResult != null) {
            List<Location> locations = locationResult.getLocations();
            deliverUnsentLocationsToLocalDB(context, locations);

            Helpers.setIsDataAvailable(context, true);

        }else{
            Log.w(TAG, "onReceive is null");

            LocationManager manager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

            if (manager != null && Helpers.getServiceStartedSuccesfully(context))
                if(!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    Log.w(TAG, "Service is being stopped");
                    LocationUpdatesService.stopLocationUpdates(context);
                }
        }

    }


    /**
     * Handles the padding so that the received locations can be positioned between activities,
     * determines the mode, stores them and the mode into the database and clears all activities outside
     * of the last one.
     * @param context - The package context delivered in the onReceive method.
     * @param newlocations - List of Locations that are delivered in the onReceive method from the
     *                     Fused Location Provider Client
     */
    void deliverUnsentLocationsToLocalDB(Context context, final List<Location> newlocations){


        final List<JsonLocation> newLocationsJson = new LinkedList<>();


        final List<ActivityTransitionEvent> transitions = LocationUpdatesService.currentTransitions;
        if(transitions != null) {
            int padding = transitions.get(transitions.size() - 1).getActivityType();
            transitions.add(
                    new ActivityTransitionEvent(
                            padding, ActivityTransition.ACTIVITY_TRANSITION_EXIT, SystemClock.elapsedRealtimeNanos() + 1000
                    )
            );

        }
        else {
            Log.w(TAG, "ActivityRecognition initialization error!");
            return;
        }


        Iterator it = transitions.iterator();
        ActivityTransitionEvent activityProbe1 = (ActivityTransitionEvent)it.next();
        int modeToTest1 = activityProbe1.getActivityType();
        long time1 = activityProbe1.getElapsedRealTimeNanos();

        ActivityTransitionEvent activityProbe2 = (ActivityTransitionEvent)it.next();
        int modeToTest2 = activityProbe2.getActivityType();
        long time2 = activityProbe2.getElapsedRealTimeNanos();

        Log.w(TAG, "Transitions at runtime of Delivery Receiver: " + transitions);

        for (Location loc : newlocations) {

            long currLocationTime = loc.getElapsedRealtimeNanos();

            if(currLocationTime < time1)
                modeToTest1 = DetectedActivity.STILL;
            else
                while(currLocationTime > time2) {

                    time1 = time2;
                    modeToTest1 = modeToTest2;

                    if(it.hasNext()) {
                        activityProbe2 = (ActivityTransitionEvent)it.next();
                        time2 = activityProbe2.getElapsedRealTimeNanos();
                        modeToTest2 = activityProbe2.getActivityType();
                    }
                    else {
                        Log.w(TAG, "iterator logic error");
                        modeToTest1 = DEFAULT_UNKNOWN_VEHICLE_VALUE;
                        break;
                    }
                }


            Log.w(TAG, "observed mode: " + getVehicle(modeToTest1));

            Log.w(TAG, "INTERVAL START: " + time1);
            Log.w(TAG, "" + loc.getElapsedRealtimeNanos());
            Log.w(TAG, "INTERVAL END: " + time2);

            Log.w(TAG, "" + loc.getLongitude() + " " + loc.getLatitude() + " Time milis: " + loc.getTime() + " Boot time: " + loc.getElapsedRealtimeNanos());

            JsonLocation locJson = new JsonLocation();
            locJson.setLongitude(loc.getLongitude());
            locJson.setLatitude(loc.getLatitude());
            locJson.setAccuracy(loc.getAccuracy());
            locJson.setSpeed(loc.getSpeed() * 3.6f);
            locJson.setTimestamp(loc.getTime());
            locJson.setMode(getVehicle(modeToTest1));

            newLocationsJson.add(locJson);
        }

        clearTransitionObjects();

        ToSendDatabase.getInstance(context.getApplicationContext()).jsonLocationDao().insertAll(newLocationsJson);
        Log.w(TAG, "DB cardinality: " + ToSendDatabase.getInstance(context.getApplicationContext()).jsonLocationDao().isEmpty());
    }


    /**
     * @param mode - integer received from the Activity Recognition Client
     * @return - The mode to be stored into the database
     */
    private String getVehicle(int mode){

        switch(mode) {
            case DetectedActivity.IN_VEHICLE:
                return "car";

                case DetectedActivity.ON_BICYCLE:
                    return "bike";

                    case DetectedActivity.ON_FOOT:
                        return "walk";

                        case DetectedActivity.STILL:
                            return "walk";

                            case DetectedActivity.WALKING:
                                return "walk";

                                case DetectedActivity.RUNNING:
                                    return "walk";

                                    case DetectedActivity.UNKNOWN:
                                        return "unknown_vehicle";

                                        default:
                                            return "unknown_vehicle";

        }

    }


    /**
     * clears the Activities recognized by the Activity Recognition Client
     */
    private void clearTransitionObjects(){
        ActivityTransitionEvent lastEvent = LocationUpdatesService.currentTransitions.get(
                LocationUpdatesService.currentTransitions.size() - 2);

        LocationUpdatesService.currentTransitions.clear();
        LocationUpdatesService.currentTransitions.add(lastEvent);
    }
}

