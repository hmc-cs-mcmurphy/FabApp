package com.example.android.fabapp;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

@Database(entities = {Project.class, Fabric.class}, version = 1)
public abstract class FabAppRoomDatabase extends RoomDatabase {
    public abstract ProjectDao projectDao();
    public abstract FabricDao fabricDao();

    private static volatile FabAppRoomDatabase INSTANCE;

    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {

        private final ProjectDao mDao;
        private final FabricDao mFabricDao;

        PopulateDbAsync(FabAppRoomDatabase db) {
            mDao = db.projectDao();
            mFabricDao = db.fabricDao();
        }

        @Override
        protected Void doInBackground(final Void... params) {
            mDao.deleteAll();
            mFabricDao.deleteAll();
            return null;
        }
    }

    private static RoomDatabase.Callback sRoomDatabaseCallback =
            new RoomDatabase.Callback(){

                @Override
                public void onOpen (@NonNull SupportSQLiteDatabase db){
                    super.onOpen(db);
                    // comment out to refresh db
//                    new PopulateDbAsync(INSTANCE).execute();
                }
            };

    static FabAppRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (FabAppRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            FabAppRoomDatabase.class, "fabapp_database")
                            .addCallback(sRoomDatabaseCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
