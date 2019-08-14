package com.imfpmo.app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.util.Log;

import com.google.android.gms.location.ActivityTransitionEvent;
import com.google.android.gms.location.ActivityTransitionResult;

import java.util.List;

public class ActivityRecognitionBroadcastReceiver extends BroadcastReceiver {

    private String TAG = ActivityRecognitionBroadcastReceiver.class.getSimpleName();

    /**
     * Method updates Activity Transitions array each time a transition is received so that the mode
     * of the received locations in LocationDeliveryBroadcastReceiver can be determined.
     * @param context - Received context
     * @param intent - Received intent
     */
    @Override
    public void onReceive(Context context, Intent intent) {

        if (ActivityTransitionResult.hasResult(intent)) {
            Log.w(TAG, "onReceive hasResult");
            ActivityTransitionResult result = ActivityTransitionResult.extractResult(intent);
            if(result != null) {
                Log.w(TAG, "Printing received transitions: ");
                List<ActivityTransitionEvent> list = result.getTransitionEvents();
                for (ActivityTransitionEvent event : list) {
                    Log.w(TAG, "event happened after " + (SystemClock.elapsedRealtimeNanos() - event.getElapsedRealTimeNanos())/1000/1000/1000 + " s");
                    Log.w(TAG, event.toString());
                }

                if(LocationUpdatesService.currentTransitions != null)
                    LocationUpdatesService.currentTransitions.addAll(list);
                else {
                    Log.w(TAG, "Service is dead and is being stopped");
                    LocationUpdatesService.stopLocationUpdates(context);
                }
            } else
                Log.w(TAG, "result is null");

        }
    }
}
