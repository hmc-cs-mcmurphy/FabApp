package com.example.android.fabapp;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.List;

public class FabricActivity extends AppCompatActivity implements FabricListAdapter.OnFabricListener {

    private static final String TAG = "FabricActivity";
    public static final int NEW_FABRIC_ACTIVITY_REQUEST_CODE = 1;

    private FabricViewModel mFabricViewModel;
    private List<Fabric> mFabrics;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fabric);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("Fabric Library");

        mFabricViewModel = ViewModelProviders.of(this).get(FabricViewModel.class);

        FloatingActionButton viewFabricButton = findViewById(R.id.fabric_fab);

        RecyclerView recyclerView = findViewById(R.id.fabric_recycler_view);
        final FabricListAdapter adapter = new FabricListAdapter(this,this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));

        mFabricViewModel.getAllFabrics().observe(this, new Observer<List<Fabric>>() {
            @Override
            public void onChanged(@Nullable final List<Fabric> fabrics) {
                // Update the cached copy of the projects in the adapter.
                adapter.setFabrics(fabrics);
                mFabrics = fabrics;
            }
        });

        viewFabricButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FabricActivity.this, NewFabricActivity.class);
                startActivityForResult(intent, NEW_FABRIC_ACTIVITY_REQUEST_CODE);
            }
        });
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == NEW_FABRIC_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            Log.d(TAG, "onActivityResult: GOOD");
            Fabric fabric = new Fabric(data.getStringExtra("FABRIC_NAME"),
                    data.getStringExtra("FABRIC_URI"), null, null, null, null);
            mFabricViewModel.insert(fabric);
        } else {

            Log.d(TAG, "onActivityResult: BAD");
            Toast.makeText(
                    getApplicationContext(),
                    R.string.empty_not_saved,
                    Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onFabricClick(int position) {
        Log.d(TAG, "onFabricClick: clicked");
        Fabric fabric = mFabrics.get(position);
        Intent intent = new Intent(this, FabricDetailsActivity.class);
        intent.putExtra("fabric_name", fabric.getFabricName());
        intent.putExtra("fabric_uri", fabric.getFabricUri());
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }
}
