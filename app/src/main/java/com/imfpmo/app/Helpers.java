package com.imfpmo.app;

import android.content.Context;
import android.preference.PreferenceManager;

class Helpers {

    static final String KEY_REQUESTING_LOCATION_UPDATES = "requesting_locaction_updates";

    //deprecated
    static boolean requestingLocationUpdates(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getBoolean(KEY_REQUESTING_LOCATION_UPDATES, false);
    }

    //deprecated
    static void setRequestingLocationUpdates(Context context, boolean requestingLocationUpdates) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putBoolean(KEY_REQUESTING_LOCATION_UPDATES, requestingLocationUpdates)
                .apply();
    }


}
