package com.imfpmo.app;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
interface JsonLocationDao {

    @Query("SELECT * FROM locations")
    List<JsonLocation> getAll();

    @Query("SELECT count(*) FROM locations")
    int isEmpty();

    @Insert
    void insertAll(List<JsonLocation> jsonLocations); //maybe collection is needed

    @Query("DELETE FROM locations")
    void deleteAll();


}
