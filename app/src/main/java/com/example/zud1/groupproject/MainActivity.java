package com.example.zud1.groupproject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.FeatureInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static android.R.attr.angle;
import static android.content.res.Resources.getSystem;
import static java.lang.Math.cos;
import static java.lang.Math.sin;

public class MainActivity extends AppCompatActivity
        implements GestureDetector.OnGestureListener
{

    //Initialising global variables to be used in all the classes
    SensorManager sMgr;
    Sensor accel;
    public float PaddleLength = getScreenWidth() / 4;
    public float sWidthL = getScreenWidth() / 2 - PaddleLength / 2;
    public float sWidthR = getScreenWidth() / 2 + PaddleLength / 2;
    public float sHeight = getScreenHeight();
    public float xx;
    public float yy;
    public float blackX = getScreenWidth()/2;
    public float blackY = getScreenHeight()/10 * 4.2f;

    double sinAngle = 0;
    double cosAngle = 0;

    public float targetSize = getScreenWidth() / 24;
    public float ballSize = getScreenWidth() / 30;
    boolean started;
    float centre_x = (sWidthL + sWidthR) / 2, centre_y = sHeight - sHeight/15 - ballSize, radius = ballSize/2;

    GraphicsView gView;
    GestureDetectorCompat gdet;

    float velocity_x;
    float velocity_y;

    public int lives = 3;
    public int score = 0;
    Intent h;

    @Override
    public boolean onDown(MotionEvent motionEvent) {
        return false;
    }
    @Override
    public void onShowPress(MotionEvent motionEvent) {
    }
    @Override
    public boolean onSingleTapUp(MotionEvent motionEvent) {
        return false;
    }
    @Override
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        Log.d("204", "OnScroll event received");
        return false;
    }
    @Override
    public void onLongPress(MotionEvent motionEvent) {
    }
    //If the fling method is called on the ball
    @Override
    public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        if(centre_x == (sWidthL + sWidthR) / 2 && centre_y == sHeight - sHeight/15 - ballSize){
            Log.d("204", "Fling event");
            centre_x = centre_x + (v / 1000);
            centre_y = centre_y + (v1 / 1000);
            velocity_x = v / 8;
            velocity_y = v1 / 8;
            gView.invalidate();
        }
            return false;

    }
    //Listener for the accelerometer to get how much the phone has been tilted by
    SensorEventListener acclistener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent sensorEvent)
        {
            xx = sensorEvent.values[0];
            yy = sensorEvent.values[1];
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i)
        {

        }
    };
    //2 methods that return the screen width and height
    public static int getScreenWidth() {
        return getSystem().getDisplayMetrics().widthPixels;
    }

    public static int getScreenHeight() {
        return getSystem().getDisplayMetrics().heightPixels;
    }

    //When the program is opened it goes to this method
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        PackageManager pMgr = getPackageManager();
        for(FeatureInfo fi : pMgr.getSystemAvailableFeatures())
        {
            Log.d("204", fi.toString());
        }

        sMgr = (SensorManager)getSystemService(SENSOR_SERVICE);
        accel = sMgr.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        gView = new GraphicsView(this);
        setContentView(gView);

        gdet = new GestureDetectorCompat(this, this);


        h = new Intent(this, HighScoreActivity.class);
    }

    //If the game has been paused
    @Override
    protected void onPause()
    {
        super.onPause();
        sMgr.unregisterListener(acclistener, accel);
    }

    //Resuming the game
    @Override
    protected void onResume()
    {
        super.onResume();
        sMgr.registerListener(acclistener, accel, SensorManager.SENSOR_DELAY_NORMAL);
    }

    //Class for drawing everything
    class GraphicsView extends View {
        List listBall  = new ArrayList();
        public Canvas c;
        public GraphicsView(Context c) {
            super(c);
        }
        @Override
        protected void onDraw(Canvas canvas) {
            c = canvas;
            super.onDraw(canvas);
            Paint p = new Paint();
            p.setStyle(Paint.Style.FILL_AND_STROKE);
            p.setColor(Color.BLUE);
            //Drawing the paddle
            canvas.drawRect(sWidthL, sHeight - sHeight / 15, sWidthR, sHeight - sHeight / 20, p);

            //Paddle speed and stopping it from going off the screen  all these methods also contain the
            //Ball speed and stopping it from going off the screen
            //int = -int is for changing the direction of the velocity
            sWidthL = sWidthL - xx * 5;
            if (sWidthL <= 0) {
                sWidthL = 0;
                sWidthR = PaddleLength;
            }
            sWidthR = sWidthR - xx * 5;
            if (sWidthR >= canvas.getWidth()) {
                sWidthR = canvas.getWidth();
                sWidthL = canvas.getWidth() - PaddleLength;
            }
            if (centre_y == sHeight - sHeight / 15 - ballSize) {
                centre_x = (sWidthL + sWidthR) / 2;
                centre_y = sHeight - sHeight / 15 - ballSize;
            } else {

                centre_x = centre_x + velocity_x / 70;
                centre_y = centre_y + velocity_y / 70;
                if (centre_x <= ballSize) {
                    centre_x = ballSize;
                    velocity_x = -velocity_x;
                }
                if (centre_x >= canvas.getWidth() - ballSize) {
                    centre_x = canvas.getWidth() - ballSize;
                    velocity_x = -velocity_x;
                }
                if (centre_y <= ballSize) {
                    centre_y = ballSize;
                    velocity_y = -velocity_y;
                }
                if (centre_y >= canvas.getHeight() - ballSize) {
                    centre_y = canvas.getHeight() - ballSize;
                    velocity_x = 0;
                    centre_x = (sWidthL + sWidthR) / 2;
                    centre_y = sHeight - sHeight / 15 - ballSize;
                    lives = lives - 3;
                    if(lives < 0)
                    {
                        lives = 0;
                    }
                    //If you run out of lives or finished the game then move to the highscore screen
                    if(lives<=0 || score == 60){
                        lives = 0;
                        gameOver();
                        startActivity(h);
                    }
                }
                //If the ball hits the paddle put the ball back in the middle of the paddle
                if (centre_x >= sWidthL && centre_x <= sWidthR && centre_y >= sHeight - sHeight / 15 - ballSize && centre_y <= sHeight - sHeight / 20) {
                    centre_x = (sWidthL + sWidthR) / 2;
                    centre_y = sHeight - sHeight / 15 - ballSize;
                    velocity_x = 0;
                    velocity_y = 0;
                }
            }
            //Create the list of targets
            if (listBall.size() == 0) {
                started = true;
                int temp = 0;
                for (int j = 0; j < 5; j++) {
                    for (int i = 0; i < 12; i++) {
                        Ball b = new Ball(targetSize + (targetSize * i * 2), targetSize + targetSize * j * 2);
                        listBall.add(temp, b);
                        temp++;
                    }
                }
            }//If the list has already been created then draw the remaining targets
            else {
                for (int i = 0; i < listBall.size(); i++) {
                    Ball ball = (Ball) listBall.get(i);
                    Paint pa = new Paint();
                    pa.setStyle(Paint.Style.FILL_AND_STROKE);
                    pa.setColor(Color.GREEN);
                    ball.draw(pa);
                }
            }
            //If the main ball collides with the target, remove the target and make the ball bounce back
            for (int i = 0; i < listBall.size(); i++) {
                Ball ball = (Ball) listBall.get(i);
                boolean collide = ball.collide(centre_x, centre_y);
                if (collide) {
                    listBall.remove(i);
                    //velocity_x = -velocity_x ;
                    velocity_y = -velocity_y;
                    score = score + 1;

                }
            }

            //Draws the main circle
            Paint pai = new Paint();
            pai.setStyle(Paint.Style.FILL_AND_STROKE);
            pai.setColor(Color.RED);
            canvas.drawCircle(centre_x, centre_y, ballSize, pai);

            //Calculations for the angle of the black ball to move in  circles
            blackX = blackX + (float) cos(sinAngle) * (canvas.getWidth() / 140);
            blackY = blackY + (float) sin(cosAngle) * (canvas.getWidth() / 140);
            sinAngle += 0.0225;
            cosAngle += 0.0225;

            //Drawing the ball and checking for collision, if collision occurs minus 1 life and put the main ball back in the middle of the paddle
            Ball ballBlack = new Ball(blackX, blackY, canvas.getWidth() / 13);
            Paint pain = new Paint();
            pain.setStyle(Paint.Style.FILL_AND_STROKE);
            pain.setColor(Color.BLACK);
            ballBlack.draw(pain);
            boolean collide = ballBlack.collide(centre_x, centre_y);
            if (collide) {
                centre_x = (sWidthL + sWidthR) / 2;
                centre_y = sHeight - sHeight / 15 - ballSize;
                lives = lives - 1;
                //If you lose or finish the game then go to high schore screen
                if(lives <= 0 || score == 60){
                    lives = 0;
                    gameOver();
                    startActivity(h);
                }
            }

            //Draws the rectangle at the bottom of the screen
            Paint paint = new Paint();
            paint.setStyle(Paint.Style.FILL_AND_STROKE);
            paint.setColor(Color.BLACK);
            canvas.drawRect(0, canvas.getHeight() / 30 * 29, canvas.getWidth(), canvas.getHeight(), paint);

            //Writing lives and scor at the bottom of the screen
            Paint writePaint = new Paint();
            writePaint.setColor(Color.WHITE );
            writePaint.setTextSize(canvas.getHeight() / 30);
            pai.setTextSize(canvas.getHeight() / 30);
            canvas.drawText("Lives " + lives, 0, canvas.getHeight(), writePaint);

            canvas.drawText("Score " + score, canvas.getWidth() / 10 * 7, canvas.getHeight(), writePaint);

            invalidate();
        }

        //Opens and updates the highscores
        public void gameOver()
        {
            SharedPreferences set = getSharedPreferences("SCORES", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = set.edit();
            int[] arrInt = new int[6];
            for(int i = 0; i<arrInt.length-1; i++){
                int value = set.getInt(Integer.toString(i+1), 0);
                arrInt[i] = value;
            }
            arrInt[5] = score;
            Arrays.sort(arrInt);
            int count = 1;
            for(int i = 5; i > 0; i--){
                editor.putInt(Integer.toString(count), arrInt[i]);
                editor.commit();
                count++;
            }
            editor.putInt("your", score);
            editor.commit();
        }
        @Override
        public boolean onTouchEvent(MotionEvent event) {
            gdet.onTouchEvent(event);

            return true;
        }
        //Ball class for the target balls and the black ball
        public class Ball {
            float cx;
            float cy;
            float radius;
            boolean hit = false;

            public Ball(float x, float y, float r)
            {
                cx = x;
                cy = y;
                radius = r;
                Paint pai = new Paint();
                pai.setStyle(Paint.Style.FILL_AND_STROKE);
                pai.setColor(Color.GREEN);
                draw(pai);

            }

            public Ball(float x, float y) {
                cx = x;
                cy = y;
                radius = targetSize;
                Paint pa = new Paint();
                pa.setStyle(Paint.Style.FILL_AND_STROKE);
                pa.setColor(Color.GREEN);
                draw(pa);
            }

            //Collision method to see if the main ball collides with targets
            public boolean collide(float bx, float by) {
                if (hit == false) {
                    double xDif = bx - cx;
                    double yDif = by - cy;
                    double distanceSquared = xDif * xDif + yDif * yDif;
                    boolean collision = distanceSquared < (radius + ballSize) * (radius + ballSize);
                    if (collision == true) {
                        hit = true;
                    }
                    return collision;
                }
                return false;
            }

            private void draw(Paint p) {

                c.drawCircle(cx, cy, radius, p);
            }
        }
    }
}
