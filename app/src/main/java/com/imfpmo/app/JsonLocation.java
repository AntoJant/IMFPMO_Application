package com.imfpmo.app;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

@Entity(tableName = "locations")
class JsonLocation {

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public float getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(float accuracy) {
        this.accuracy = accuracy;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public long getKey() {
        return key;
    }

    @PrimaryKey(autoGenerate = true)
    public long key;

    @ColumnInfo(name = "longitude")
    public double longitude;

    @ColumnInfo(name = "latitude")
    public double latitude;

    @ColumnInfo(name = "accuracy")
    public float accuracy;

    @ColumnInfo(name = "speed")
    public float speed;

    @ColumnInfo(name = "timestamp")
    public long timestamp;

    @ColumnInfo(name = "mode")
    public String mode;


    JsonObject toJsonObject(){
        JsonObject locJson = new JsonObject();
        locJson.addProperty("longitude", this.getLongitude());
        locJson.addProperty("latitude", this.getLatitude());
        locJson.addProperty("accuracy", this.getAccuracy());
        locJson.addProperty("speed", this.getSpeed());
        locJson.addProperty("timestamp", this.getTimestamp());
        locJson.addProperty("mode", this.getMode());

        return locJson;
    }
}
