package com.imfpmo.app;

import android.content.Context;
import android.preference.PreferenceManager;
import android.util.Log;


class Helpers {

    private static String TAG = Helpers.class.getSimpleName();

    static final String KEY_REQUESTING_LOCATION_UPDATES = "requesting_locaction_updates";
    private static final String KEY_SENDING_WORKER_SHEDULED = "scheduling_location_deliveries";
    private static final String KEY_IS_DATA_AVAILABLE = "is_data_in_local_db_waiting_to_be_sent";



    static boolean requestingLocationUpdates(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getBoolean(KEY_REQUESTING_LOCATION_UPDATES, false);
    }


    static void setRequestingLocationUpdates(Context context, boolean requestingLocationUpdates) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putBoolean(KEY_REQUESTING_LOCATION_UPDATES, requestingLocationUpdates)
                .apply();
    }

    static boolean isSendingWorkerScheduled(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getBoolean(KEY_SENDING_WORKER_SHEDULED, false);
    }

    static void setSendingWorkerScheduled(Context context, boolean scheduled) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putBoolean(KEY_SENDING_WORKER_SHEDULED, scheduled)
                .apply();
    }

    static boolean isDataAvailable(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getBoolean(KEY_IS_DATA_AVAILABLE, false);
    }


    static void setIsDataAvailable(Context context, boolean dataAvailable) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putBoolean(KEY_IS_DATA_AVAILABLE, dataAvailable)
                .apply();
    }

    static boolean validateInputUserAuthData(String userID, String securityToken) {
        if (userID != null && securityToken != null)
            if (!userID.equals("") && !securityToken.equals(""))
                return true;
            else {
                Log.e(TAG, "userId or securityToken are not set (empty String) at runtime of sending worker");
                return false;
            }

        Log.w(TAG, "userId or securityToken are null at runtime of sending worker");
        return false;

    }
}
