package com.imfpmo.app;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.Response;

import java.util.List;
import java.util.Set;


/**
 * Class that sends the data stored in the local database to the backend.
 * It first checks for duplicates then converts the database location objects to Json objects,
 * stores them on a JsonArray and sends them.
 */
public class LocationDeliveryWorker extends Worker {

    private String TAG = LocationDeliveryWorker.class.getSimpleName();

    private static final String KEY_USER_ID = "usermanagement_user_id";
    private static final String KEY_SECURITY_TOKEN = "usermanagement_security_token";
    private static final String WORKER_TYPE_REGULAR = "periodic_worker";
    private static final String WORKER_TYPE_ONCE = "logout_worker";

    public LocationDeliveryWorker(@NonNull Context context, @NonNull WorkerParameters params) {
        super(context, params);
    }


    @NonNull
    @Override
    public Result doWork(){

        Log.w(TAG, "Sending process started");

        if(!Helpers.isDataAvailable(getApplicationContext()))
            return Result.success();

        Set<String> tags = getTags();

        // if sending fails depending on worker type retries or lets data be sent at a later time
        if(!sendDataToServer()) {
            Log.w(TAG, "doWork: something happened trying later");
            if(tags.contains(WORKER_TYPE_REGULAR))
                return Result.failure();
            else if(tags.contains(WORKER_TYPE_ONCE))
                    return Result.retry();
                else {
                    Log.e(TAG, "error when indentifying by tags");
                    return Result.failure();
                }
        }

        Helpers.setIsDataAvailable(getApplicationContext(), false);
        return Result.success();
    }


    /**
     * Queries the data from the local database, performs conversion to JsonObjects
     * and sends it to the server with Ion. If succesfull, deletes the locations from the database.
     * @return - returns the status of the Ion HTTP request. If true then data was received by the server.
     */
    private boolean sendDataToServer(){
        Data inputData = getInputData();
        String userId = inputData.getString(KEY_USER_ID);
        String securityToken = inputData.getString(KEY_SECURITY_TOKEN);

        //Method called as to avoid null pointer exceptions
        if(!Helpers.validateInputUserAuthData(userId, securityToken)) {
            Log.e(TAG, "Data will never be sent as auth data is faulty");
            return false;
        }

        List<JsonLocation> locationsToSend = ToSendDatabase.getInstance(getApplicationContext()).jsonLocationDao().getAll();
        JsonArray locationsToSendJson = new JsonArray();

        long checkDuplicateTime = 0;

        for(JsonLocation loc : locationsToSend) {
            if(loc.getTimestamp() != checkDuplicateTime) {
                JsonObject locJson = loc.toJsonObject();
                locationsToSendJson.add(locJson);
                Log.w(TAG, locJson.toString());
            }
            checkDuplicateTime = loc.getTimestamp();
        }

        JsonObject datapoints = new JsonObject();
        datapoints.add("data_points", locationsToSendJson);

        Log.w(TAG, "Check contiguous block for datapoints: " + datapoints);
        Log.w(TAG, "will send to user: " + userId);


        try {
            Response<JsonObject> result = Ion.with(getApplicationContext())
                    .load("https://treibhaus.informatik.rwth-aachen.de/praktikum-ss19/api/users/" + userId + "/tracking-data")
                    .setHeader("Authorization", "Bearer " + securityToken)
                    .setJsonObjectBody(datapoints)
                    .asJsonObject()
                    .withResponse().get();

            if(result != null && result.getHeaders().code() == 200){
                Log.w(TAG, "sending succesful. Locations in DB before delete: "
                            + ToSendDatabase.getInstance(getApplicationContext()).jsonLocationDao().isEmpty());
                ToSendDatabase.getInstance(getApplicationContext()).jsonLocationDao().deleteAll();
                Log.w(TAG, "Locations in DB after delete: "
                        + ToSendDatabase.getInstance(getApplicationContext()).jsonLocationDao().isEmpty());
                return true;
            }

            return false;

        }catch(Exception e){
            Log.w(TAG, "Error while sending GPS data :" + e);
            return false;
        }

    }
}
