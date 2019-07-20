package com.imfpmo.app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.util.Log;

import com.google.android.gms.location.ActivityTransitionEvent;
import com.google.android.gms.location.ActivityTransitionResult;

public class ActivityRecognitionBroadcastReceiver extends BroadcastReceiver {

    private String TAG = ActivityRecognitionBroadcastReceiver.class.getSimpleName();

    @Override
    // TODO: 7/19/19 see what you get and find maxconf more are delivered at once 
    public void onReceive(Context context, Intent intent) {

        if (ActivityTransitionResult.hasResult(intent)) {
            Log.w(TAG, "onReceive hasResult");
            ActivityTransitionResult result = ActivityTransitionResult.extractResult(intent);
            if(result != null) {
                Log.w(TAG, "Printing received transitions: ");
                for (ActivityTransitionEvent event : result.getTransitionEvents()) {
                    Log.w(TAG, "event happened after " + (SystemClock.elapsedRealtimeNanos() - event.getElapsedRealTimeNanos())/1000/1000/1000 + " s");
                    Log.w(TAG, event.toString());
                }

                LocationUpdatesService.currentTransitions.addAll(result.getTransitionEvents());
            } else
                Log.w(TAG, "result is null");

        }
    }
}
