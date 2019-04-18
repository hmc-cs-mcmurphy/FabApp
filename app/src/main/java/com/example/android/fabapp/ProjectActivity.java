package com.example.android.fabapp;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.List;

public class ProjectActivity extends AppCompatActivity implements ProjectListAdapter.OnProjectListener {
    private static final String TAG = "ProjectActivity";
    public static final int NEW_PROJECT_ACTIVITY_REQUEST_CODE = 1;

    private ProjectViewModel mProjectViewModel;
    private List<Project> mProjects;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

//        getActionBar().setDisplayHomeAsUpEnabled(true);



        mProjectViewModel = ViewModelProviders.of(this).get(ProjectViewModel.class);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("Project Library");

        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        final ProjectListAdapter adapter = new ProjectListAdapter(this,this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProjectActivity.this, NewProjectActivity.class);
                startActivityForResult(intent, NEW_PROJECT_ACTIVITY_REQUEST_CODE);
            }
        });

        mProjectViewModel.getAllProjects().observe(this, new Observer<List<Project>>() {
            @Override
            public void onChanged(@Nullable final List<Project> projects) {
                // Update the cached copy of the projects in the adapter.
                adapter.setProjects(projects);
                mProjects = projects;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
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
//                NavUtils.navigateUpFromSameTask(this);
//                return true;
//            // handle arrow click here
//            if (item.getItemId() == android.R.id.home) {
                finish(); // close this activity and return to preview activity (if there is any)
//            }
        }

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == NEW_PROJECT_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
//            System.out.println(data.getStringExtra(NewProjectActivity.EXTRA_REPLY));
//            Project project = new Project(data.getStringExtra(NewProjectActivity.EXTRA_REPLY),
//                    data.getStringExtra(NewProjectActivity.EXTRA_REPLY_1));
            Project project = new Project(data.getStringExtra("PROJECT_NAME"),
                    data.getStringExtra("PROJECT_DIMENSIONS"), data.getStringExtra("PROJECT_START_DATE"));
            mProjectViewModel.insert(project);
        } else {
            Toast.makeText(
                    getApplicationContext(),
                    R.string.empty_not_saved,
                    Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onProjectClick(int position) {
        Log.d(TAG, "onProjectClick: clicked");
        Project proj = mProjects.get(position);
        Intent intent = new Intent(this, ProjectDetailsActivity.class);
        intent.putExtra("project_name", proj.getProject());
        intent.putExtra("project_dimension", proj.getDimension());
        intent.putExtra("project_start_date", proj.getStartDate());
        startActivity(intent);
    }
}
