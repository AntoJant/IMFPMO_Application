package com.example.mobileapp_praktikum;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
class JsonLocation {

    @ColumnInfo(name = "longitude")
    public double longitude;

    @ColumnInfo(name = "latitude")
    public double latitude;

    @ColumnInfo(name = "accuracy")
    public float accuracy;

    @ColumnInfo(name = "speed")
    public float speed;

    @PrimaryKey
    public long timestamp;

    @ColumnInfo(name = "mode")
    public String mode;
}
