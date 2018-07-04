package com.example.zud1.groupproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_home);

        //If button is pressed then go to game
        final Intent s = new Intent(this, MainActivity.class);
        Button start = (Button)findViewById(R.id.button);
        start.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivity(s);
            }
        });

        //If button is pressed then got to Highscore page
        final Intent h = new Intent(this, HighScoreActivity.class);
        Button score = (Button)findViewById(R.id.button2);
        score.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivity(h);
            }
        });

    }

    //If the back button is pressed then close the application
    @Override
    public void onBackPressed() {
        super.onBackPressed();
         finishAffinity();
    }
}
