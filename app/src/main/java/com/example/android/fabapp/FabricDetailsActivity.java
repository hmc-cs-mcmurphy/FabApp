package com.example.android.fabapp;

import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class FabricDetailsActivity extends AppCompatActivity {
    private static final String TAG = "FabricDetailsActivity";


    public static final int EDIT_FABRIC_ACTIVITY_REQUEST_CODE = 1;

    private FabricViewModel mFabricViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("Fabric Details");

        mFabricViewModel = ViewModelProviders.of(this).get(FabricViewModel.class);

        setContentView(R.layout.activity_fabric_details);
        Log.d(TAG, "onCreate: started");

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
        if (id == R.id.action_delete_fabric) {
            deleteFabricDialog();
            return true;
        }

        if (id == R.id.action_edit_fabric) {
            Fabric fabric = getFabric();
            goToEditFabric(fabric);
            mFabricViewModel.delete(fabric);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_fabric_details, menu);
        return true;
    }

    private void getIncomingIntent() {
        Log.d(TAG, "getIncomingIntent: Checking for incoming intent...");

        if (getIntent().hasExtra("fabric_name") &&
                getIntent().hasExtra("fabric_uri")) {

            Log.d(TAG, "getIncomingIntent: found intent extras.");

            String fabricName = getIntent().getStringExtra("fabric_name");
            String fabricUri = getIntent().getStringExtra("fabric_uri");
            String fabricLine = null;
            String fabricMaker = null;
            String fabricYardage = null;
            String fabricPurchaseLocation = null;

            if (getIntent().hasExtra("fabric_line"))
                fabricLine = getIntent()
                        .getStringExtra("fabric_line");

            if (getIntent().hasExtra("fabric_maker"))
                fabricMaker = getIntent()
                        .getStringExtra("fabric_maker");

            if (getIntent().hasExtra("fabric_yardage"))
                fabricYardage = getIntent()
                        .getStringExtra("fabric_yardage");

            if (getIntent().hasExtra("fabric_purchase_location"))
                fabricPurchaseLocation = getIntent()
                        .getStringExtra("fabric_purchase_location");

            setFabric(fabricName,
                    fabricUri,
                    fabricLine,
                    fabricMaker,
                    fabricYardage,
                    fabricPurchaseLocation);
        }
    }

    private Fabric getFabric() {
        Fabric fabric = null;

        if (getIntent().hasExtra("fabric_name") &&
                getIntent().hasExtra("fabric_uri")) {

            Log.d(TAG, "getIncomingIntent: found intent extras.");

            String fabricName = getIntent().getStringExtra("fabric_name");
            String fabricUri = getIntent().getStringExtra("fabric_uri");
            String fabricLine = null;
            String fabricMaker = null;
            String fabricYardage = null;
            String fabricPurchaseLocation = null;

            if (getIntent().hasExtra("fabric_line"))
                fabricLine = getIntent()
                        .getStringExtra("fabric_line");

            if (getIntent().hasExtra("fabric_maker"))
                fabricMaker = getIntent()
                        .getStringExtra("fabric_maker");

            if (getIntent().hasExtra("fabric_yardage"))
                fabricYardage = getIntent()
                        .getStringExtra("fabric_yardage");

            if (getIntent().hasExtra("fabric_purchase_location"))
                fabricPurchaseLocation = getIntent()
                        .getStringExtra("fabric_purchase_location");

            fabric = new Fabric(fabricName,
                    fabricUri,
                    fabricLine,
                    fabricMaker,
                    fabricYardage,
                    fabricPurchaseLocation);
        }
        return fabric;
    }

    private void setFabric(String fabricName,
                           String fabricUri,
                           String fabricLine,
                           String fabricMaker,
                           String fabricYardage,
                           String fabricPurchaseLocation) {
        Log.d(TAG, "setFabric: setting fabric.");
        Log.d(TAG, "setFabric: fabricName is " + fabricName);
        Log.d(TAG, "setFabric: fabricUri is " + fabricUri);
        Log.d(TAG, "setFabric: fabricLine is " + fabricLine);
        Log.d(TAG, "setFabric: fabricMaker is " + fabricMaker);
        Log.d(TAG, "setFabric: fabricYardage is " + fabricYardage);
        Log.d(TAG, "setFabric: fabricPurchaseLocation is " + fabricPurchaseLocation);

        TextView name = findViewById(R.id.fabric_name);
        name.setText(fabricName);


        if (fabricLine != null){
            TextView line = findViewById(R.id.details_fabric_line);
            line.setText(fabricLine);
        }

        if (fabricMaker != null) {
            TextView maker = findViewById(R.id.details_fabric_maker);
            maker.setText(fabricMaker);
        }

        if (fabricYardage != null) {
            TextView yardage = findViewById(R.id.details_fabric_yardage);
            yardage.setText(fabricYardage);
        }

        if (fabricPurchaseLocation != null) {
            TextView purchase_location = findViewById(R.id.details_fabric_purchase_location);
            purchase_location.setText(fabricPurchaseLocation);
        }

        Bitmap bmImg = BitmapFactory.decodeFile(fabricUri);
        ImageView picture = findViewById(R.id.fabric_picture);
        picture.setImageBitmap(bmImg);
    }

    private void deleteFabric(Fabric fabric) {
        mFabricViewModel.delete(fabric);
        Intent upIntent = NavUtils.getParentActivityIntent(this);
        NavUtils.navigateUpTo(this, upIntent);
    }

    private void deleteFabricDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Confirm");
        builder.setMessage("Are you sure you'd like to delete this fabric? It cannot be undone.");

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                // Do nothing but close the dialog
                Log.d(TAG, "onOptionsItemSelected: fabric delete confirmed!!");
                Fabric fabric = getFabric();
                deleteFabric(fabric);
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.d(TAG, "onOptionsItemSelected: fabric delete denied!!");
                // Do nothing
                dialog.dismiss();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }

    private void goToEditFabric(Fabric fabric) {
//        Fabric fabric = getFabric();
        Log.d(TAG, "goToEditFabric: Fabric name is " + fabric.getFabricName());
        Intent intent = new Intent(FabricDetailsActivity.this, FabricEditActivity.class);

        Bundle extras = new Bundle();
        String fabricName = fabric.getFabricName();
        String currentPhotoPath = fabric.getFabricUri();
        String fabricLine = fabric.getFabricLine();
        String fabricMaker = fabric.getFabricMaker();
        String fabricYardage = fabric.getFabricYardage();
        String fabricPurchaseLocation = fabric.getFabricPurchaseLocation();
        // ADD THE URI FOR THE PHOTO
        Log.d(TAG, "onClick: Fabric name is " + fabricName);
        Log.d(TAG, "onClick: Fabric URI is " + currentPhotoPath);
        extras.putString("FABRIC_NAME", fabricName);
        extras.putString("FABRIC_URI", currentPhotoPath);
        extras.putString("FABRIC_LINE", fabricLine);
        extras.putString("FABRIC_MAKER", fabricMaker);
        extras.putString("FABRIC_YARDAGE", fabricYardage);
        extras.putString("FABRIC_PURCHASE_LOCATION", fabricPurchaseLocation);
        intent.putExtras(extras);

        startActivityForResult(intent, EDIT_FABRIC_ACTIVITY_REQUEST_CODE);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult: called");

        if (requestCode == EDIT_FABRIC_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            Log.d(TAG, "onActivityResult: GOOD");
            Fabric fabric = new Fabric(
                    data.getStringExtra("FABRIC_NAME"),
                    data.getStringExtra("FABRIC_URI"),
                    data.getStringExtra("FABRIC_LINE"),
                    data.getStringExtra("FABRIC_MAKER"),
                    data.getStringExtra("FABRIC_YARDAGE"),
                    data.getStringExtra("FABRIC_PURCHASE_LOCATION")
            );
            mFabricViewModel.insert(fabric);
            setFabric(fabric.getFabricName(),
                    fabric.getFabricUri(),
                    fabric.getFabricLine(),
                    fabric.getFabricMaker(),
                    fabric.getFabricYardage(),
                    fabric.getFabricPurchaseLocation());
        } else {

            Log.d(TAG, "onActivityResult: BAD");
            Toast.makeText(
                    getApplicationContext(),
                    R.string.empty_not_saved,
                    Toast.LENGTH_LONG).show();
        }
    }
}
