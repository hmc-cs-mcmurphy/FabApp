package com.example.android.fabapp;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class FrontPageActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_front_page);

        TextView welcomeText = findViewById(R.id.welcome_text);
        welcomeText.setText(getString(R.string.text_welcome));

        ImageView projectIcon = findViewById(R.id.projectIcon);
        int projectResource = getResources().getIdentifier("@drawable/projects_picture", null, this.getPackageName());
        projectIcon.setImageResource(projectResource);

        ImageView fabricIcon = findViewById(R.id.fabricIcon);
        int fabricResource = getResources().getIdentifier("@drawable/fabric_picture", null, this.getPackageName());
        fabricIcon.setImageResource(fabricResource);


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
