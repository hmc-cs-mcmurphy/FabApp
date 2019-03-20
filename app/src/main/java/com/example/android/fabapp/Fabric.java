package com.example.android.fabapp;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "fabric_table")
public class Fabric {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "fabric_name")
    private String mFabricName;

    @ColumnInfo(name = "fabric_uri")
    @NonNull
    private String mFabricUri;

    @ColumnInfo(name = "fabric_line")
    private String mFabricLine;

    @ColumnInfo(name = "fabric_yardage")
    private String mFabricYardage;

    @ColumnInfo(name = "fabric_maker")
    private String mFabricMaker;

    @ColumnInfo(name = "fabric_purchase_location")
    private String mFabricPurchaseLocation;

    public Fabric(@NonNull String mFabricName,
                  @NonNull String mFabricUri,
                  String mFabricLine,
                  String mFabricYardage,
                  String mFabricMaker,
                  String mFabricPurchaseLocation) {
        this.mFabricName = mFabricName;
        this.mFabricUri = mFabricUri;
        this.mFabricLine = mFabricLine;
        this.mFabricMaker = mFabricMaker;
        this.mFabricYardage = mFabricYardage;
        this.mFabricPurchaseLocation = mFabricPurchaseLocation;
    }

    String getFabricUri(){return this.mFabricUri;}

    String getFabricName() {return this.mFabricName;}

    String getFabricLine() {return  this.mFabricLine;}

    String getFabricYardage() {return this.mFabricYardage;}

    String getFabricMaker() {return this.mFabricMaker;}

    String getFabricPurchaseLocation() {return this.mFabricPurchaseLocation;}
}
