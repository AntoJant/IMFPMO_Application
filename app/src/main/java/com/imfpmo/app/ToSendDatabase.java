package com.imfpmo.app;


import android.content.Context;
import android.content.SharedPreferences;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {JsonLocation.class}, version = 3,exportSchema = false)
public abstract class ToSendDatabase extends RoomDatabase {

    private static final String DB_NAME = "local_locations_database";

    public abstract JsonLocationDao jsonLocationDao();

    private static volatile ToSendDatabase mlocalDatabase;

    static synchronized ToSendDatabase getInstance(Context context) {
        if (mlocalDatabase == null) {
            mlocalDatabase = createLocalDB(context);
        }
        return mlocalDatabase;
    }

    private static ToSendDatabase createLocalDB(final Context context) {

        return Room.databaseBuilder(context,
                    ToSendDatabase.class, DB_NAME)
                .addMigrations(new Migration(2, 3){
                    public void migrate(SupportSQLiteDatabase database) {
                    }
                }).allowMainThreadQueries().build();
    }



}
