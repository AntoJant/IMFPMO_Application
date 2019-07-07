package com.example.mobileapp_praktikum;


import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {JsonLocation.class}, version = 1)
public abstract class ToSendDatabase extends RoomDatabase {

    public abstract JsonLocationDao jsonLocationDao();
}
