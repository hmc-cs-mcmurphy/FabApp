package com.example.android.fabapp;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface FabricDao {

    @Insert
    void insert(Fabric fabric);

    @Query("DELETE FROM fabric_table")
    void deleteAll();

    @Query("SELECT * from fabric_table ORDER BY fabric_uri ASC")
    LiveData<List<Fabric>> getAllFabrics();
}
