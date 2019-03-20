package com.example.android.fabapp;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

public class NewProjectActivity  extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private static final String TAG = "NewProjectActivity";

    private EditText mEditProjectView;
    private EditText mEditDimensionView;
    private TextView mEditStartDateView;
    Context context;



    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.activity_new_project);
        mEditProjectView = findViewById(R.id.edit_project);
        mEditDimensionView = findViewById(R.id.edit_dimension);
        mEditStartDateView = findViewById(R.id.view_start_date);

        // months are zero indexed
        final DatePickerDialog datePickerDialog = new DatePickerDialog(
                context, NewProjectActivity.this, 2019, 8, 22);
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
                if (TextUtils.isEmpty(mEditProjectView.getText())) {
                    setResult(RESULT_CANCELED, replyIntent);
                } else {
                    Bundle extras = new Bundle();
                    String project = mEditProjectView.getText().toString();
                    String dimen = mEditDimensionView.getText().toString();
                    String startDate = mEditStartDateView.getText().toString();
                    System.out.println("Project is: " + project);
                    extras.putString("PROJECT_NAME", project);
                    extras.putString("PROJECT_DIMENSIONS", dimen);
                    extras.putString("PROJECT_START_DATE", startDate);
                    replyIntent.putExtras(extras);
                    setResult(RESULT_OK, replyIntent);
                }
                finish();
            }
        });
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
        ((TextView) findViewById(R.id.view_start_date)).setText(setText);
    }
}