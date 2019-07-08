package com.example.mobileapp_praktikum;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.util.Log;

import com.google.android.gms.location.LocationResult;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.Response;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

import static com.example.mobileapp_praktikum.LocationUpdatesService.PREFERENCE_FILE_KEY;
import static com.example.mobileapp_praktikum.LocationUpdatesService.getLocalDB;


public class LocationDeliveryBroadcastReciever extends BroadcastReceiver {

    public static final String TAG = LocationDeliveryBroadcastReciever.class.getSimpleName();
    public static final String ACTION_LOCATION_DELIVERY = "com.example.mobileapp_praktikum.action.locdelivery";




    @Override
    public void onReceive(Context context, Intent intent) {

        LocationResult locationResult = LocationResult.extractResult(intent);
        if (locationResult != null) {
            Log.w(TAG, "onRecieve not null");
            List<Location> locations = locationResult.getLocations();

            deliverUnsentLocationsToUser(context, locations);
            /*
            for (Location location : locations) {
                //processing if velocity is 0 in repeated locations dont send repeated
                //unprocessed atm
                }


            JsonArray locationsJson = new JsonArray();

            for (Location loc : locations) {
                JsonObject locJson = new JsonObject();
                locJson.addProperty("longitude", loc.getLongitude());
                locJson.addProperty("latitude", loc.getLatitude());
                locJson.addProperty("speed", loc.getSpeed());
                locJson.addProperty("timestamp", loc.getTime());
                locJson.addProperty("mode", "walk");
                locationsJson.add(locJson);
            }

            JsonObject datapoints = new JsonObject();
            datapoints.add("data_points", locationsJson);

            Log.w(TAG, datapoints.toString());
            Log.w(TAG, "will send to user: ");
            Log.w(TAG, "will send with token: ");
            //add user and token from context protected field or from Intent
            Ion.with(context).load("https://treibhaus.informatik.rwth-aachen.de/praktikum-ss19/api/users" + "/22" + "/tracking-data")
                    .setHeader("Authorization", "Bearer " + "25d0be6a-d6be-4432-acaa-3509579f8c93")
                    .setJsonObjectBody(datapoints)
                    .asJsonObject()
                    .withResponse()
                    .setCallback(new FutureCallback<Response<JsonObject>>() {
                        @Override
                        public void onCompleted(Exception e, Response<JsonObject> result) {
                            if (e != null) {
                                Log.w(TAG, "Error while sending GPS data :" + e);
                                return;
                            }

                            Log.w(TAG, result.getHeaders().message());
                        }

                    });

            */
        } else Log.w(TAG, "onRecieve is null");

    }

    void deliverUnsentLocationsToUser(final Context context, final List<Location> newlocations){

        Usermanagement usermanagement = Usermanagement.getInstance();


        final JsonArray newLocationsJson = new JsonArray();
        JsonArray toSendLocations;

        /*
        if(!databaseEmpty()) {
            //toSendLocations = getJsonLocationsFromDB();
        }
        else
        */
            toSendLocations = new JsonArray();
            //or pointer to newLocJson





        for (Location loc : newlocations) {
            JsonObject locJson = new JsonObject();
            locJson.addProperty("longitude", loc.getLongitude());
            locJson.addProperty("latitude", loc.getLatitude());
            locJson.addProperty("accuracy", loc.getAccuracy());
            float speed = loc.getSpeed() * 3.6f;
            locJson.addProperty("speed", speed);
            locJson.addProperty("timestamp", loc.getTime());
            if(speed < 8f)
                locJson.addProperty("mode", "walk");
            else if(speed < 35f)
                    locJson.addProperty("mode", "bike");
                    else
                    locJson.addProperty("mode", "unknown_vehicle");

            newLocationsJson.add(locJson);
        }


        toSendLocations.addAll(newLocationsJson);
        JsonObject datapoints = new JsonObject();
        datapoints.add("data_points", toSendLocations);

        Log.w(TAG, datapoints.toString());
        Log.w(TAG, "will send to user: " + LocationUpdatesService.getUserId());

        Ion.with(context).load("https://treibhaus.informatik.rwth-aachen.de/praktikum-ss19/api/users/" + LocationUpdatesService.getUserId() + "/tracking-data")
                .setHeader("Authorization", "Bearer " + LocationUpdatesService.getSecurityToken())
                .setJsonObjectBody(datapoints)
                .asJsonObject()
                .withResponse()
                .setCallback(new FutureCallback<Response<JsonObject>>() {
                    @Override
                    public void onCompleted(Exception e, Response<JsonObject> result) {
                        if (e != null) {
                            Log.w(TAG, "Error while sending GPS data :" + e);
                            //storeJsonLocations(newLocationsJson);
                            return;
                        }

                        Log.w(TAG, result.getHeaders().message());
                        if (result.getHeaders().code() == 200) {
                            //if (!databaseEmpty())
                              //clearStoredJsonLocations();
                        }
                        else;
                            //storeJsonLocations(newLocationsJson);
                    }

                });

    }

    boolean databaseEmpty(){
        return true;//LocationUpdatesService.getLocalDB().jsonLocationDao().isEmpty() == 0;

    }
    /*
    JsonArray getJsonLocationsFromDB(){
        ToSendDatabase db = LocationUpdatesService.getLocalDB();
        return db.jsonLocationDao().getAll();
    }

    void clearStoredJsonLocations(){
        LocationUpdatesService.getLocalDB().jsonLocationDao().deleteAll();
    }

    void storeJsonLocations(final JsonArray newLocationsJson){
        ToSendDatabase db = LocationUpdatesService.getLocalDB();
        db.jsonLocationDao().insertAll(newLocationsJson); //probably doesnt work

        for (JsonElement obj: newLocationsJson){
           // db.jsonLocationDao().insertAll(obj); //tbd
        }
    */














    /*
    //not elegant
    //working with paths and no file reference
    //try local db maybe
    JsonArray getJsonLocationsFromCache(Context context) {
        try {
            Log.w(TAG, "getJsonLocationsFromCache");
            String path = context.getCacheDir().getAbsolutePath() + "/" + LocationUpdatesService.CACHE_NAME + ".tmp";
            FileInputStream cache = new FileInputStream(path);
            ObjectInputStream stream = new ObjectInputStream(cache);
            Object obj = stream.readObject();
            stream.close();
            return (JsonArray)obj;

        }catch(Exception e){
            Log.w(TAG, "file was not created or path is bad" + e);
        }
        return new JsonArray();
    }

    void cacheLocations(Context context, JsonArray unsentJsonLocations) {
        try {
            Log.w(TAG, "cacheLocations");
            String path = context.getCacheDir().getAbsolutePath() + "/" + LocationUpdatesService.CACHE_NAME + ".tmp";
            FileOutputStream cache = new FileOutputStream(path);
            ObjectOutputStream stream = new ObjectOutputStream(cache);

            stream.writeObject(unsentJsonLocations);
            stream.close();

            SharedPreferences pref = context.getSharedPreferences(PREFERENCE_FILE_KEY, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = pref.edit();
            editor.putBoolean(LocationUpdatesService.CACHE_STATUS_EMPTY, false);
            editor.commit();

        } catch (Exception e) {
            Log.w(TAG, "file was not created or path is bad" + e);
        }
    }

    void clearCachedLocations(Context context) {
        try {
            Log.w(TAG, "clearCachedLocations");
            String path = context.getCacheDir().getAbsolutePath() + "/" + LocationUpdatesService.CACHE_NAME + ".tmp";
            new FileOutputStream(path).close();

            SharedPreferences pref = context.getSharedPreferences(PREFERENCE_FILE_KEY, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = pref.edit();
            editor.putBoolean(LocationUpdatesService.CACHE_STATUS_EMPTY, true);
            editor.commit();

        } catch (IOException e) {
            Log.w(TAG, "cant delete contents of cache" + e);
        }
    }


    boolean isCacheEmpty(Context context) {
        Log.w(TAG, "isCacheEmpty");
        return context.getSharedPreferences(PREFERENCE_FILE_KEY, Context.MODE_PRIVATE).getBoolean(LocationUpdatesService.CACHE_STATUS_EMPTY, true);
    }
    */
}
