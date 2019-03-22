package com.example.android.fabapp;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface ProjectDao {

    @Insert
    void insert(Project project);

    @Query("DELETE FROM project_table")
    void deleteAll();

    @Delete
    void deleteProject(Project project);

    @Query("SELECT * from project_table ORDER BY project ASC")
    LiveData<List<Project>> getAllProjects();
}