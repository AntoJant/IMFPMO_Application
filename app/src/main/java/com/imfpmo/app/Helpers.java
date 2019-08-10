package com.imfpmo.app;

import android.content.Context;
import android.preference.PreferenceManager;
import android.util.Log;


class Helpers {

    private static String TAG = Helpers.class.getSimpleName();

    static final String KEY_REQUESTING_LOCATION_UPDATES = "requesting_locaction_updates";
    private static final String KEY_SENDING_WORKER_SHEDULED = "scheduling_location_deliveries";
    private static final String KEY_IS_DATA_AVAILABLE = "is_data_in_local_db_waiting_to_be_sent";
    private static final String KEY_SERVICE_STARTED_SUCCESFULLY = "was_service_started_as_intended";


    /**
     * Static method used for checking if service is active. Used to change button state in main activity
     * and for detecting if Service is already started or not.
     * @param context - The Activity or Service from where the method is called.
     * @return - true if service is currently active
     */
    static boolean requestingLocationUpdates(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getBoolean(KEY_REQUESTING_LOCATION_UPDATES, false);
    }


    /**
     * Used to set the Service status through for the above method.
     * @param context -
     * @param requestingLocationUpdates - The boolean to be stored in shared preferences
     */
    static void setRequestingLocationUpdates(Context context, boolean requestingLocationUpdates) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putBoolean(KEY_REQUESTING_LOCATION_UPDATES, requestingLocationUpdates)
                .apply();
    }


    /**
     * Used at startup of a service to stop the Location button listener from stopping the service.
     * Value will be changed after locations are turned on.
     * @param context -
     * @param startedSuccesfully - The boolean to be stored in shared preferences
     */
    @SuppressWarnings("ApplySharedPref")
    static void setServiceStartedSuccesfully(Context context, boolean startedSuccesfully) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putBoolean(KEY_SERVICE_STARTED_SUCCESFULLY, startedSuccesfully)
                .commit();
    }

    /**
     * The status check set in the above method.
     * @param context -
     * @return true if  Locations are turned on.
     */
    static boolean getServiceStartedSuccesfully(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getBoolean(KEY_SERVICE_STARTED_SUCCESFULLY, false);
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

    /**
     * Checks if there are locations to be sent.
     * @param context -
     * @return - true if database is not empty
     */
    static boolean isDataAvailable(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getBoolean(KEY_IS_DATA_AVAILABLE, false);
    }


    /**
     * Sets the flag for checking status in the above method.
     * @param context -
     * @param dataAvailable -
     */
    static void setIsDataAvailable(Context context, boolean dataAvailable) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putBoolean(KEY_IS_DATA_AVAILABLE, dataAvailable)
                .apply();
    }


    /**
     * Method checks if the user authentication data is valid. Method is used at start of
     * delivery worker.
     * @param userID - The user id stored at login
     * @param securityToken - The security token stored at login
     * @return - true if both parameters are not null and valid
     */
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
