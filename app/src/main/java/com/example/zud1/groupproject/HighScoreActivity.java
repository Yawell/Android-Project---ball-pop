package com.example.zud1.groupproject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import java.util.Arrays;

public class HighScoreActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_high_score);

        //If button is pressed then go to home activity
        final Intent s = new Intent(this, HomeActivity.class);
        Button home = (Button)findViewById(R.id.button3);
        home.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivity(s);
            }
        });

        //If button is pressed then go to game
        final Intent h = new Intent(this, MainActivity.class);
        Button game = (Button)findViewById(R.id.button4);
        game.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivity(h);
            }
        });

        //Create shared preference file and set variables
        SharedPreferences set = getSharedPreferences("SCORES", Context.MODE_PRIVATE);
        int data1 = set.getInt("1", 0);// 1
        int data2 = set.getInt("2", 0);
        int data3 = set.getInt("3", 0);
        int data4 = set.getInt("4", 0);
        int data5 = set.getInt("5", 0);
        int data6 = set.getInt("your", 0);

        //Get text views
        TextView t1 = (TextView) findViewById(R.id.textViewNumber1);
        TextView t2 = (TextView) findViewById(R.id.textViewNumber2);
        TextView t3 = (TextView) findViewById(R.id.textViewNumber3);
        TextView t4 = (TextView) findViewById(R.id.textViewNumber4);
        TextView t5 = (TextView) findViewById(R.id.textViewNumber5);
        TextView your = (TextView) findViewById(R.id.textViewYour);

        //Display the high scores
        t1.setText(Integer.toString(data1));
        t2.setText(Integer.toString(data2));
        t3.setText(Integer.toString(data3));
        t4.setText(Integer.toString(data4));
        t5.setText(Integer.toString(data5));
        your.setText(Integer.toString(data6));

    }

    //If back button is pressed then go on the home screen
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        final Intent inte = new Intent(this, HomeActivity.class);
        startActivity(inte);
    }
}
