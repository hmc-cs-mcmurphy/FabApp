package com.example.android.fabapp;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

public class ProjectEditActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    private static final String TAG = "ProjectEditActivity";

    private EditText mEditProjectName;
    private EditText mEditProjectDimension;
    private TextView mEditProjectStartDate;
    Context context;

    private Project originalProject;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: in edit project activity");

        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.activity_project_edit);
        mEditProjectName = findViewById(R.id.ep_project_name);
        mEditProjectDimension = findViewById(R.id.ep_project_dimension);
        mEditProjectStartDate = findViewById(R.id.ep_project_sd);

        final Project project = getIncomingIntent();
        Log.d(TAG, "onCreate: dimensions are " + project.getDimension());
        originalProject = project;

        mEditProjectName.setText(project.getProject());
        mEditProjectDimension.setText(project.getDimension());
        mEditProjectStartDate.setText(project.getStartDate());

        // months are zero indexed
        final DatePickerDialog datePickerDialog = new DatePickerDialog(
                context, ProjectEditActivity.this, 2019, 8, 22);
        final Button pickDate = findViewById(R.id.button_pick_date);

        pickDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog.show();
            }
        });


        final Button button = findViewById(R.id.button_save);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent replyIntent = new Intent();
                if (TextUtils.isEmpty(mEditProjectName.getText())) {
                    setResult(RESULT_CANCELED, replyIntent);
                } else {
                    Bundle extras = new Bundle();
                    String projectName = mEditProjectName.getText().toString();
                    String projectDimen = mEditProjectDimension.getText().toString();
                    String projectStartDate = mEditProjectStartDate.getText().toString();
                    extras.putString("PROJECT_NAME", projectName);
                    extras.putString("PROJECT_DIMENSION", projectDimen);
                    extras.putString("PROJECT_START_DATE", projectStartDate);
                    replyIntent.putExtras(extras);
                    setResult(RESULT_OK, replyIntent);
                }
                finish();
            }
        });
    }

    private Project getIncomingIntent() {
        Log.d(TAG, "getIncomingIntent: Checking for incoming intent...");
        Project project = null;

        if (getIntent().hasExtra("PROJECT_NAME")) {

            Log.d(TAG, "getIncomingIntent: found intent extras.");

            String projectName = getIntent().getStringExtra("PROJECT_NAME");
            String projectDimension = null;
            String projectStartDate = null;

            if (getIntent().hasExtra("PROJECT_DIMENSION"))
                projectDimension = getIntent()
                        .getStringExtra("PROJECT_DIMENSION");

            if (getIntent().hasExtra("PROJECT_START_DATE"))
                projectStartDate = getIntent()
                        .getStringExtra("PROJECT_START_DATE");
            project = new Project(projectName,
                    projectDimension,
                    projectStartDate
            );
        }
        return project;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Log.d(TAG, "onDateSet: Year is " + year);
        Log.d(TAG, "onDateSet: Month is " + month);
        Log.d(TAG, "onDateSet: Day is " + dayOfMonth);
        String day_x = "" + dayOfMonth, month_x = "" + (month + 1);
        if (dayOfMonth < 9) {
            day_x = "0" + day_x;
        }
        if (month < 9) {
            month_x = "0" + month_x;
        }
        String setText = month_x + "/" + day_x + "/" + year;
        ((TextView) findViewById(R.id.ep_project_sd)).setText(setText);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            Log.d(TAG, "onKeyDown: back buttons pressed!");
            AlertDialog alertDialog = new AlertDialog.Builder(ProjectEditActivity.this).create();
            alertDialog.setTitle("Alert");
            alertDialog.setMessage("Going back will lose your current changes.\nAre you sure you'd like to leave?");
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Yes, I'm sure",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            Intent replyIntent = new Intent();
                            Bundle extras = new Bundle();
                            String projectName = originalProject.getProject();
                            String projectDimen = originalProject.getDimension();
                            String projectStartDate = originalProject.getStartDate();
                            extras.putString("PROJECT_NAME", projectName);
                            extras.putString("PROJECT_DIMENSION", projectDimen);
                            extras.putString("PROJECT_START_DATE", projectStartDate);
                            replyIntent.putExtras(extras);
                            setResult(RESULT_OK, replyIntent);
                            finish();
                        }
                    });
            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "No, stay here",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();
        }
        return super.onKeyDown(keyCode, event);
    }

}
