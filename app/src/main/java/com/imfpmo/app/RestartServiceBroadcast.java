package com.imfpmo.app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class RestartServiceBroadcast extends BroadcastReceiver {

    public static String TAG = RestartServiceBroadcast.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.w(TAG, "onRecieve");
        context.startService(new Intent(context,LocationUpdatesService.class));
    }
}
