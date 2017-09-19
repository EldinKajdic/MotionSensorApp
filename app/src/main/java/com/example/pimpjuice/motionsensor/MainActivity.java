package com.example.pimpjuice.motionsensor;

import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.view.View;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.io.IOException;

public class MainActivity extends AppCompatActivity implements SensorEventListener {


    private TextView xText, yText, zText;
    private Sensor motionSensor;
    private SensorManager sensorManager;
    private ColorStateList originalColor;
    private ToggleButton toggleButton;

    // Used to start and stop the program
    private boolean on;

    // The different soundplayers used in the program
    MediaPlayer lazerSound, beatBox, gunshot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Creates the SensorManager used in the program
        sensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);

        // Sets the Sensor motionSensor to type Accelerometer
        motionSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        // Sets the speed of the SensorManager to FASTEST
        sensorManager.registerListener(this, motionSensor, SensorManager.SENSOR_DELAY_FASTEST);

        // Collects the values from the mainpage
        xText = (TextView)findViewById(R.id.bokstavX);
        zText = (TextView)findViewById(R.id.bokstavZ);
        yText = (TextView)findViewById(R.id.bokstavY);

        // Used to reset the texts back to default after coloring further down
        originalColor =  xText.getTextColors();

        // Togglebutton used to start and stop the program
        toggleButton = (ToggleButton) findViewById(R.id.toggleButton);

        // Imports the different soundfiles from the raw directory and assigns them
        lazerSound = MediaPlayer.create(this, R.raw.lasershot);
        beatBox = MediaPlayer.create(this, R.raw.beatbox);
        gunshot = MediaPlayer.create(this, R.raw.gunshot);

        // Sets the bool on to false/not clicked as a default
        on = false;
    }

    public void onToggleClicked(View view) {

            if(toggleButton.isChecked()) {
                on = true;
            }
            else {
                on = false;
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        // If the app is in landscape mode, automaticly start the program (cheap fix of a bug)
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            on = true;
            beatBox.reset();
            gunshot.reset();
        }

        // If the toggle button has been clicked
        if (on == true) {

            // Set the text to the current values of the accelerometer
        xText.setText("X: " + event.values[0]);
        zText.setText("Z: " + event.values[1]);
        yText.setText("Y: " + event.values[2]);

            // If phone is leaned towards the X value
            if (event.values[0] > event.values[1] && event.values[0] > event.values[2]) {
                lazerSound.start();
                xText.setTextColor(Color.RED);
                zText.setTextColor(originalColor);
                yText.setTextColor(originalColor);
            }
            // If phone is leaned towards the Z value
            else if (event.values[1] > event.values[0] && event.values[1] > event.values[2]) {
                beatBox.start();
                xText.setTextColor(originalColor);
                zText.setTextColor(Color.GREEN);
                yText.setTextColor(originalColor);
            }
            // If phone is leaned towards the Y value
            else if (event.values[2] > event.values[0] && event.values[2] > event.values[1]) {
                gunshot.start();
                xText.setTextColor(originalColor);
                zText.setTextColor(originalColor);
                yText.setTextColor(Color.BLUE);
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    // Is needed but not in use
    }
}
