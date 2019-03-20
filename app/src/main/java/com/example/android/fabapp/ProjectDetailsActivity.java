package com.example.android.fabapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

public class ProjectDetailsActivity extends AppCompatActivity {
    private static final String TAG = "ProjectDetailsActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_details);
        Log.d(TAG, "onCreate: started");

        getIncomingIntent();
    }

    private void getIncomingIntent() {
        Log.d(TAG, "getIncomingIntent: Checking for incoming intent...");
        if (getIntent().hasExtra("project_name") && getIntent().hasExtra("project_dimension")) {
            Log.d(TAG, "getIncomingIntent: found intent extras.");
            String projectName = getIntent().getStringExtra("project_name");
            String projectDimension = getIntent().getStringExtra("project_dimension");
            String projectStartDate = getIntent().getStringExtra("project_start_date");

            setProject(projectName, projectDimension, projectStartDate);
        }
    }

    private void setProject(String projectName, String projectDimension, String projectStartDate) {
        Log.d(TAG, "setProject: setting project.");
        Log.d(TAG, "setProject: projectName is " + projectName);
        Log.d(TAG, "setProject: projectDimension is " + projectDimension);
        Log.d(TAG, "setProject: projectStartDate is " + projectStartDate);

        TextView name = findViewById(R.id.project_name);
        name.setText(projectName);

        TextView dimension = findViewById(R.id.project_dimension);
        dimension.setText(projectDimension);

        TextView startDate = findViewById(R.id.project_start_date);
        startDate.setText(projectStartDate);
    }
}
