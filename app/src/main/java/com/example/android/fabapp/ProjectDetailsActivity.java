package com.example.android.fabapp;

import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

public class ProjectDetailsActivity extends AppCompatActivity {
    private static final String TAG = "ProjectDetailsActivity";

    public static final int EDIT_PROJECT_ACTIVITY_REQUEST_CODE = 1;

    private ProjectViewModel mProjectViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_details);
        Log.d(TAG, "onCreate: started");

        mProjectViewModel = ViewModelProviders.of(this).get(ProjectViewModel.class);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("Project Details");

        getIncomingIntent();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_delete_project) {
            deleteProjectDialog();
            return true;
        }

        if (id == R.id.action_edit_project) {
            Project project = getProject();
            goToProjectEdit(project);
            mProjectViewModel.delete(project);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_project_details, menu);
        return true;
    }

    private void getIncomingIntent() {
        Log.d(TAG, "getIncomingIntent: Checking for incoming intent...");
        if (getIntent().hasExtra("project_name") && getIntent().hasExtra("project_dimension")) {
            Log.d(TAG, "getIncomingIntent: found intent extras.");
            String projectName = getIntent().getStringExtra("project_name");
            String projectDimension = getIntent().getStringExtra("project_dimension");
            String projectStartDate = getIntent().getStringExtra("project_start_date");

            Project project = new Project(projectName, projectDimension, projectStartDate);
            setProjectView(project);
        }
    }

    /**
     * getProject
     *
     * @return Project: returns the Project object held in the incoming Intent.
     */
    private Project getProject() {
        Project project = null;

        if (getIntent().hasExtra("project_name")) {

            Log.d(TAG, "getIncomingIntent: found intent extras.");

            String projectName = getIntent().getStringExtra("project_name");
            String projectStartDate = null;
            String projectDimensions = null;

            if (getIntent().hasExtra("project_start_date"))
                projectStartDate = getIntent()
                        .getStringExtra("project_start_date");

            if (getIntent().hasExtra("project_dimension"))
                projectDimensions = getIntent()
                        .getStringExtra("project_dimension");

            project = new Project(projectName,
                    projectDimensions,
                    projectStartDate
                    );
        }
        return project;
    }

    /**
     * setProjectView(Project project):
     *
     * Given a Fabric object, extract its features
     * and set them in the View objects for the page.
     *
     */
    private void setProjectView(Project project) {
        Log.d(TAG, "setProject: setting project.");
        Log.d(TAG, "setProject: projectName is " + project.getProject());
        Log.d(TAG, "setProject: projectDimension is " + project.getDimension());
        Log.d(TAG, "setProject: projectStartDate is " + project.getStartDate());
        String projectName = project.getProject();
        String projectDimensions = project.getDimension();
        String projectStartDate = project.getStartDate();

        TextView name = findViewById(R.id.project_name);
        name.setText(projectName);


        if (projectDimensions != null){
            TextView dimension = findViewById(R.id.project_dimension);
            dimension.setText(projectDimensions);
        }

        if (projectStartDate != null) {
            TextView startDate = findViewById(R.id.project_start_date);
            startDate.setText(projectStartDate);
        }
    }

    /**
     * deleteProject
     * @param project: project object to be deleted from the database.
     */
    private void deleteProject(Project project) {
        mProjectViewModel.delete(project);
        Intent upIntent = NavUtils.getParentActivityIntent(this);
        NavUtils.navigateUpTo(this, upIntent);
    }

    /**
     * deleteProjectDialog
     *
     * Displays a confirmation dialog for delete the project.
     */
    private void deleteProjectDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Confirm");
        builder.setMessage("Are you sure you want to delete this project? It cannot be undone.");

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                // Delete the project
                Log.d(TAG, "onOptionsItemSelected: Project delete confirmed.");
                Project project = getProject();
                deleteProject(project);
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.d(TAG, "onOptionsItemSelected: Project delete denied.");
                // Do nothing
                dialog.dismiss();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }

    /**
     * goToProjectEdit
     *
     * @param project: Project object passed the the ProjectEditActivity to be displayed and edited.
     */
    private void goToProjectEdit(Project project) {
        Log.d(TAG, "goToFabricEdit: Project name is " + project.getProject());
        Intent intent = new Intent(ProjectDetailsActivity.this, ProjectEditActivity.class);

        Bundle extras = new Bundle();
        String projectName = project.getProject();
        String projectDimension = project.getDimension();
        String projectStartDate = project.getStartDate();
        extras.putString("PROJECT_NAME", projectName);
        extras.putString("PROJECT_DIMENSION", projectDimension);
        extras.putString("PROJECT_START_DATE", projectStartDate);
        intent.putExtras(extras);

        startActivityForResult(intent, EDIT_PROJECT_ACTIVITY_REQUEST_CODE);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult: called");

        if (requestCode == EDIT_PROJECT_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            Log.d(TAG, "onActivityResult: GOOD");
            Project project = new Project(
                    data.getStringExtra("PROJECT_NAME"),
                    data.getStringExtra("PROJECT_DIMENSION"),
                    data.getStringExtra("PROJECT_START_DATE")
            );
            mProjectViewModel.insert(project);
            setProjectView(project);
        } else {

            Log.d(TAG, "onActivityResult: BAD");
            Toast.makeText(
                    getApplicationContext(),
                    R.string.empty_not_saved,
                    Toast.LENGTH_LONG).show();
        }
    }
}
