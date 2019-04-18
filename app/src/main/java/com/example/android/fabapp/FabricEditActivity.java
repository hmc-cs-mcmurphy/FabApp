package com.example.android.fabapp;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FabricEditActivity extends AppCompatActivity {
    private static final String TAG = "FabricEditActivity";

    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_TAKE_PHOTO = 1;

    private EditText mEditFabricNameView;
    private EditText mEditFabricLine;
    private EditText mEditFabricMaker;
    private EditText mEditFabricYardage;
    private EditText mEditFabricPurchaseLocation;
    private ImageView mEditImageView;
    private String currentPhotoPath;
    private Fabric originalFabric;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fabric_edit);
        mEditFabricNameView = findViewById(R.id.ef_edit_fabric);
        mEditImageView = findViewById(R.id.ef_image_view_fabric);
        mEditFabricLine = findViewById(R.id.ef_fabric_line);
        mEditFabricMaker = findViewById(R.id.ef_fabric_maker);
        mEditFabricYardage = findViewById(R.id.ef_fabric_yardage);
        mEditFabricPurchaseLocation = findViewById(R.id.ef_fabric_purchase_location);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("Edit fabric");

        Fabric fabric = getIncomingIntent();
        originalFabric = fabric;

        Bitmap bmImg = BitmapFactory.decodeFile(fabric.getFabricUri());
        mEditImageView.setImageBitmap(bmImg);

        mEditFabricNameView.setText(fabric.getFabricName());
        mEditFabricLine.setText(fabric.getFabricLine());
        mEditFabricMaker.setText(fabric.getFabricMaker());
        mEditFabricYardage.setText(fabric.getFabricYardage());
        mEditFabricPurchaseLocation.setText(fabric.getFabricPurchaseLocation());

        currentPhotoPath = fabric.getFabricUri();

        final Button newPictureButton = findViewById(R.id.button_new_picture);
        newPictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
            }
        });

        final Button button = findViewById(R.id.button_save);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent replyIntent = new Intent();
                if (TextUtils.isEmpty(mEditFabricNameView.getText())) {
                    setResult(RESULT_CANCELED, replyIntent);
                } else {
                    Bundle extras = new Bundle();
                    String fabricName = mEditFabricNameView.getText().toString();
                    String fabricLine = mEditFabricLine.getText().toString();
                    String fabricMaker = mEditFabricMaker.getText().toString();
                    String fabricYardage = mEditFabricYardage.getText().toString();
                    String fabricPurchaseLocation = mEditFabricPurchaseLocation.getText().toString();
                    // ADD THE URI FOR THE PHOTO
                    Log.d(TAG, "onClick: Fabric name is " + fabricName);
                    Log.d(TAG, "onClick: Fabric URI is " + currentPhotoPath);
                    extras.putString("FABRIC_NAME", fabricName);
                    extras.putString("FABRIC_URI", currentPhotoPath);
                    extras.putString("FABRIC_LINE", fabricLine);
                    extras.putString("FABRIC_MAKER", fabricMaker);
                    extras.putString("FABRIC_YARDAGE", fabricYardage);
                    extras.putString("FABRIC_PURCHASE_LOCATION", fabricPurchaseLocation);
                    replyIntent.putExtras(extras);
                    setResult(RESULT_OK, replyIntent);
                }
                finish();
            }
        });
    }

    private Fabric getIncomingIntent() {
        Log.d(TAG, "getIncomingIntent: Checking for incoming intent...");
        Fabric fabric = null;

        if (getIntent().hasExtra("FABRIC_NAME") &&
                getIntent().hasExtra("FABRIC_URI")) {

            Log.d(TAG, "getIncomingIntent: found intent extras.");

            String fabricName = getIntent().getStringExtra("FABRIC_NAME");
            String fabricUri = getIntent().getStringExtra("FABRIC_URI");
            String fabricLine = null;
            String fabricMaker = null;
            String fabricYardage = null;
            String fabricPurchaseLocation = null;

            if (getIntent().hasExtra("FABRIC_LINE"))
                fabricLine = getIntent()
                        .getStringExtra("FABRIC_LINE");

            if (getIntent().hasExtra("FABRIC_MAKER"))
                fabricMaker = getIntent()
                        .getStringExtra("FABRIC_MAKER");

            if (getIntent().hasExtra("FABRIC_YARDAGE"))
                fabricYardage = getIntent()
                        .getStringExtra("FABRIC_YARDAGE");

            if (getIntent().hasExtra("FABRIC_PURCHASE_LOCATION"))
                fabricPurchaseLocation = getIntent()
                        .getStringExtra("FABRIC_PURCHASE_LOCATION");
            fabric = new Fabric(fabricName,
                    fabricUri,
                    fabricLine,
                    fabricMaker,
                    fabricYardage,
                    fabricPurchaseLocation
            );
        }
        return fabric;
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException e){
                System.err.println("File not created.");
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Log.d(TAG, "dispatchTakePictureIntent: picture taken");
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.android.fileprovider",
                        photoFile);
                Log.d(TAG, "dispatchTakePictureIntent: Photo URI is " + photoURI);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                setResult(RESULT_OK, takePictureIntent);
                Log.d(TAG, "dispatchTakePictureIntent: Intent is: " + takePictureIntent);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
                galleryAddPic();
//                finish();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "onActivityResult: in activity result");
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
//            Bundle extras = data.getExtras();
//            Bitmap imageBitmap = (Bitmap) extras.get("data");
//            imageView.setImageBitmap(imageBitmap);
            loadImageFromFile();
        }
    }


    private File createImageFile() throws IOException {
        // Create an image file name
        @SuppressLint("SimpleDateFormat") String timeStamp = new SimpleDateFormat(getString(R.string.DATE_FORMAT)).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image;
        try {
            image = File.createTempFile(
                    imageFileName,  /* prefix */
                    ".jpg",         /* suffix */
                    storageDir      /* directory */
            );
        } catch (IOException e) {
            throw new IOException(e);
        }
        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Log.d(TAG, "galleryAddPic: currentPhotoPath " + currentPhotoPath);
        File f = new File(currentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }

    public void loadImageFromFile(){

        mEditImageView.setVisibility(View.VISIBLE);


        int targetW = mEditImageView.getWidth();
        int targetH = mEditImageView.getHeight();

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(currentPhotoPath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(currentPhotoPath, bmOptions);
        mEditImageView.setImageBitmap(bitmap);

    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            Log.d(TAG, "onKeyDown: back buttons pressed!");
            AlertDialog alertDialog = new AlertDialog.Builder(FabricEditActivity.this).create();
            alertDialog.setTitle("Alert");
            alertDialog.setMessage("Going back will lose your current changes.\nAre you sure you'd like to leave?");
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Yes, I'm sure",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            Intent replyIntent = new Intent();
                            Bundle extras = new Bundle();
                            String fabricName = originalFabric.getFabricName();
                            String fabricLine = originalFabric.getFabricLine();
                            String fabricMaker = originalFabric.getFabricMaker();
                            String fabricYardage = originalFabric.getFabricYardage();
                            String fabricPurchaseLocation = originalFabric.getFabricPurchaseLocation();
                            // ADD THE URI FOR THE PHOTO
                            extras.putString("FABRIC_NAME", fabricName);
                            extras.putString("FABRIC_URI", currentPhotoPath);
                            extras.putString("FABRIC_LINE", fabricLine);
                            extras.putString("FABRIC_MAKER", fabricMaker);
                            extras.putString("FABRIC_YARDAGE", fabricYardage);
                            extras.putString("FABRIC_PURCHASE_LOCATION", fabricPurchaseLocation);
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
