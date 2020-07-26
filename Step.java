package com.example.user.stepcounterfitness;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Step extends AppCompatActivity implements StepListener,SensorEventListener{
    private TextView textView,text;
    private  ImageButton BtnStop;
    private  Button TvSteps,dist,calories;
    private StepDetector simpleStepDetector;
    private SensorManager sensorManager;
    private Sensor accel;
    private static final String TEXT_NUM_STEPS = "Number of Steps: ";
    private int numSteps;
    private DatabaseReference dbRef;
    private FirebaseDatabase fire;
    String name,weight,height,Age,gender;
    Chronometer cmTimer;
    Button  btnReset,BtnStart;
    Boolean resume = false;
    float distance;
    double cal;
    long elapsedTime,minutes,seconds;
    String TAG = "TAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step);
        Bundle extras = getIntent().getExtras();
         name = extras.getString("Name");
         weight = (extras.getString("Weight"));
         height = extras.getString("Height");
         Age = (extras.getString("Age"));
         gender=(extras.getString("Gender"));
         Log.d("Age","print"+Age);
        Log.d("Weight","print"+weight);
        Log.d("Height","print"+height);

        fire=FirebaseDatabase.getInstance();
        dbRef=fire.getReference();

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accel = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        simpleStepDetector = new StepDetector();
        simpleStepDetector.registerListener(this);

        TvSteps = ( Button) findViewById(R.id.tv_steps);
        BtnStart = (Button) findViewById(R.id.btn_start);
        BtnStop = (ImageButton) findViewById(R.id.btn_stop);
        dist=(Button) findViewById(R.id.start);
        calories=(Button) findViewById(R.id.calorie);
        cmTimer = (Chronometer) findViewById(R.id.cmTimer);
        text=(TextView) findViewById(R.id.welcome);
        text.setText("Welcome "+name);

        btnReset = (Button) findViewById(R.id.btnReset);

        cmTimer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            public void onChronometerTick(Chronometer arg0) {
                if (!resume) {
                     minutes = ((SystemClock.elapsedRealtime() - cmTimer.getBase())/1000) / 60;
                    seconds = ((SystemClock.elapsedRealtime() - cmTimer.getBase())/1000) % 60;
                    elapsedTime = SystemClock.elapsedRealtime();
                    Log.d(TAG, "onChronometerTick: " + minutes + " : " + seconds);
                } else {
                    minutes = ((elapsedTime - cmTimer.getBase())/1000) / 60;
                    seconds = ((elapsedTime - cmTimer.getBase())/1000) % 60;
                    elapsedTime = elapsedTime + 1000;
                    Log.d(TAG, "onChronometerTick: " + minutes + " : " + seconds);
                }
            }
        });

        BtnStart.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                BtnStart.setEnabled(false);
                BtnStop.setEnabled(true);


                numSteps = 0;
                sensorManager.registerListener(Step.this,accel, SensorManager.SENSOR_DELAY_FASTEST);

                if (!resume) {
                    cmTimer.setBase(SystemClock.elapsedRealtime());
                    cmTimer.start();
                } else
                    {
                    cmTimer.start();
                }



            }
        });

        BtnStop.setOnClickListener(new View.OnClickListener() {


            public void onClick(View arg0) {

                sensorManager.unregisterListener(Step.this);
                BtnStart.setEnabled(true);
                BtnStop.setEnabled(false);
                cmTimer.stop();
                BtnStart.setText("Start");
                resume = false;
                SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy");
                Calendar c = Calendar.getInstance();
                String date = sdf.format(c.getTime());

                int step=numSteps;

                dbRef.child("Nive1820").child(name).child(date).child("steps").setValue(step);
                dbRef.child("Nive1820").child(name).child(date).child("distance").setValue(distance);
                dbRef.child("Nive1820").child(name).child(date).child("calories").setValue(cal);

            }
        });
        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cmTimer.stop();
                cmTimer.setText("00:00");
                resume = true;
                BtnStop.setEnabled(false);
                BtnStart.setText("Start");
            }
        });









}




    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }


    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            simpleStepDetector.updateAccel(
                    event.timestamp, event.values[0], event.values[1], event.values[2]);
        }
    }

    @Override
    public void step(long timeNs) {
        numSteps++;
        TvSteps.setText(TEXT_NUM_STEPS + numSteps);
        try {
            getDistanceRun(numSteps);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void getDistanceRun(long steps) throws Exception
    {

        distance = (float)(steps*78)/(float)100000;
        dist.setText("Distance Covered:"+distance+"Km");

        Calorie(distance,minutes,seconds);
    }
    public void Calorie(float distance,long minutes,long seconds) throws  Exception
    {
        double cal;
        long time=(minutes)+(seconds/60);
        try {
            if(gender.equalsIgnoreCase("female"))
            {
                Log.d("Calories","Female");
                cal = ((((Integer.parseInt(Age) * 0.2017) - (Integer.parseInt(weight) * 0.09036 * 2.2046) + (79.5* 0.6309) - 55.0969) * time / 4.184)/1000);
                calories.setText("Calories Burned:" + cal + "Kcal");
            }
            else {
                cal = ((((Integer.parseInt(Age) * 0.2017) - (Integer.parseInt(weight) * 0.09036 * 2.2046) + (71* 0.6309) - 55.0969) * time / 4.184)/1000);
                calories.setText("Calories Burned:" + cal + "Kcal");

            }

        }
        catch (Exception e)
        {
            Log.d("Exception","Inside Calorie");
        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }






}
