package com.example.android.fabapp;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import java.util.List;

public class FabricRepository {

    private FabricDao mFabricDao;
    private LiveData<List<Fabric>> mAllFabrics;

    FabricRepository(Application application) {
        FabAppRoomDatabase db = FabAppRoomDatabase.getDatabase(application);
        mFabricDao = db.fabricDao();
        mAllFabrics = mFabricDao.getAllFabrics();
    }

    LiveData<List<Fabric>> getAllFabrics() {
        return mAllFabrics;
    }


    public void insert (Fabric fabric) {
        new FabricRepository.insertAsyncTask(mFabricDao).execute(fabric);
    }

    private static class insertAsyncTask extends AsyncTask<Fabric, Void, Void> {

        private FabricDao mAsyncTaskDao;

        insertAsyncTask(FabricDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Fabric... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }
}
