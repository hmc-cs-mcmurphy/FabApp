package com.example.android.fabapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

public class FabricDetailsActivity extends AppCompatActivity {
    private static final String TAG = "FabricDetailsActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fabric_details);
        Log.d(TAG, "onCreate: started");

        getIncomingIntent();
    }

    private void getIncomingIntent() {
        Log.d(TAG, "getIncomingIntent: Checking for incoming intent...");
        if (getIntent().hasExtra("fabric_name") && getIntent().hasExtra("fabric_uri")) {
            Log.d(TAG, "getIncomingIntent: found intent extras.");
            String fabricName = getIntent().getStringExtra("fabric_name");
            String fabricUri = getIntent().getStringExtra("fabric_uri");

            setFabric(fabricName, fabricUri);
        }
    }

    private void setFabric(String fabricName, String fabricUri) {
        Log.d(TAG, "setFabric: setting fabric.");
        Log.d(TAG, "setFabric: fabricName is " + fabricName);
        Log.d(TAG, "setFabric: fabricUri is " + fabricUri);

        TextView name = findViewById(R.id.fabric_name);
        name.setText(fabricName);
//
//        TextView dimension = findViewById(R.id.fabric_uri);
//        dimension.setText(fabricUri);

        ImageView picture = findViewById(R.id.fabric_picture);
        Bitmap bmImg = BitmapFactory.decodeFile(fabricUri);
        picture.setImageBitmap(bmImg);
    }
}
