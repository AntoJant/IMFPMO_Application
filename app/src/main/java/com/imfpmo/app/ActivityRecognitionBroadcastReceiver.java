package com.imfpmo.app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.util.Log;

import com.google.android.gms.location.ActivityRecognition;
import com.google.android.gms.location.ActivityRecognitionResult;
import com.google.android.gms.location.ActivityTransitionEvent;
import com.google.android.gms.location.ActivityTransitionResult;

import java.util.List;

public class ActivityRecognitionBroadcastReceiver extends BroadcastReceiver {

    private String TAG = ActivityRecognitionBroadcastReceiver.class.getSimpleName();

    @Override
    // TODO: 7/19/19 see what you get and find maxconf more are delivered at once & test with periodic activity updates
    public void onReceive(Context context, Intent intent) {

        //if(ActivityRecognitionResult.hasResult(intent)){
        //ActivityRecognitionResult result = ActivityRecognitionResult.extractResult(intent);
        //Log.w(TAG, "received Activity: " + result.getMostProbableActivity().toString());}

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

                LocationUpdatesService.currentTransitions.addAll(list);
            } else
                Log.w(TAG, "result is null");

        }
    }
}
