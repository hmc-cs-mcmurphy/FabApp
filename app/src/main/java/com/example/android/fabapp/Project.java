package com.example.android.fabapp;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "project_table")
public class Project {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "project")
    private String mProject;

    @ColumnInfo(name = "dimensions")
    private String mDimension;

    @ColumnInfo(name = "start_date")
    private String mStartDate;

    public Project(String project, String dimension, String startDate) {
        this.mProject = project;
        this.mDimension = dimension;
        this.mStartDate = startDate;
    }

    public void setDimension(String dimension) {
        this.mDimension = dimension;
    }

    public String getProject(){return this.mProject;}

    public String getDimension(){return this.mDimension;}

    public String getStartDate(){return this.mStartDate;}
}