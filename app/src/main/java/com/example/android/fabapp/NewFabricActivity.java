package com.example.android.fabapp;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class NewFabricActivity extends AppCompatActivity {
    private static final String TAG = "NewFabricActivity";

    private EditText mEditFabricNameView;
    private EditText mEditFabricLine;
    private EditText mEditFabricMaker;
    private EditText mEditFabricYardage;
    private EditText mEditFabricPurchaseLocation;
    private ImageView mEditImageView;

    String currentPhotoPath;

    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_TAKE_PHOTO = 1;

    Context context;


    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.activity_new_fabric);
        mEditFabricNameView = findViewById(R.id.edit_fabric);
        mEditImageView = findViewById(R.id.image_view_fabric);
        mEditFabricLine = findViewById(R.id.fabric_line);
        mEditFabricMaker = findViewById(R.id.fabric_maker);
        mEditFabricYardage = findViewById(R.id.fabric_yardage);
        mEditFabricPurchaseLocation = findViewById(R.id.fabric_purchase_location);

        final Button pictureButton = findViewById(R.id.button_take_picture);
        pictureButton.setOnClickListener(new View.OnClickListener() {
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
                    replyIntent.putExtras(extras);
                    setResult(RESULT_OK, replyIntent);
                }
                finish();
            }
        });
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
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = null;
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

//        ImageView view = (ImageView)this.findViewById(R.id.image_view_fabric);
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
}
