package com.example.android.fabapp;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import java.util.List;

public class ProjectRepository {

    private ProjectDao mProjectDao;
    private LiveData<List<Project>> mAllProjects;

    ProjectRepository(Application application) {
        FabAppRoomDatabase db = FabAppRoomDatabase.getDatabase(application);
        mProjectDao = db.projectDao();
        mAllProjects = mProjectDao.getAllProjects();
    }

    LiveData<List<Project>> getAllProjects() {
        return mAllProjects;
    }


    public void insert (Project project) {
        new insertAsyncTask(mProjectDao).execute(project);
    }

    private static class insertAsyncTask extends AsyncTask<Project, Void, Void> {

        private ProjectDao mAsyncTaskDao;

        insertAsyncTask(ProjectDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Project... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }
}