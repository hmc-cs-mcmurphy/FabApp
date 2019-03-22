package com.example.android.fabapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class FrontPageActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_front_page);


        final Button projectsButton = findViewById(R.id.button_view_projects);
        projectsButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(FrontPageActivity.this, ProjectActivity.class);
                startActivity(intent);
            }
        });

        final Button fabricButton = findViewById(R.id.button_view_fabric);
        fabricButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(FrontPageActivity.this, FabricActivity.class);
                startActivity(intent);
            }
        });
    }


}
