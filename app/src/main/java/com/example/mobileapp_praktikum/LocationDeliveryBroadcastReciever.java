package com.example.mobileapp_praktikum;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.util.Log;

import com.google.android.gms.location.LocationResult;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.Response;

import java.util.List;

public class LocationDeliveryBroadcastReciever extends BroadcastReceiver {

    public static final String TAG = LocationDeliveryBroadcastReciever.class.getSimpleName();
    public static final String ACTION_LOCATION_DELIVERY = "com.example.mobileapp_praktikum.action.locdelivery";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.w(TAG, "onRecieve");
        LocationResult locationResult = LocationResult.extractResult(intent);
        if (locationResult != null) {

            List<Location> locations = locationResult.getLocations();

            for (Location location : locations) {
                //processing if velocity is 0 in repeated locations dont send repeated
                //unprocessed atm
                Log.w(TAG, location.toString());

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
            Ion.with(context).load("http://fjobilabs.de:8383/api/users/" + "1" + "/tracking-data")
                    .setHeader("Authorization: ", "1")
                    .setJsonObjectBody(datapoints)
                    .asString()
                    .withResponse()
                    .setCallback(new FutureCallback<Response<String>>() {
                        @Override
                        public void onCompleted(Exception e, Response<String> response) {
                            if(response != null)
                                Log.w(TAG, String.valueOf(response.getHeaders().code()));

                        }
                    });
        }

    }
}
