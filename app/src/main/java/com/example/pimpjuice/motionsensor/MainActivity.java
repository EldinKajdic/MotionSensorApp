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
    private boolean on;
    MediaPlayer lazerSound;
    MediaPlayer beatBox;
    MediaPlayer gunshot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Här skapas sensormanagern
        sensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);

        // Här skapas sensorn
        motionSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        // Skapar sensor lyssnaren
        sensorManager.registerListener(this, motionSensor, SensorManager.SENSOR_DELAY_FASTEST);

        // Hämtar värden
        xText = (TextView)findViewById(R.id.bokstavX);
        zText = (TextView)findViewById(R.id.bokstavZ);
        yText = (TextView)findViewById(R.id.bokstavY);

        originalColor =  xText.getTextColors();
        toggleButton = (ToggleButton) findViewById(R.id.toggleButton);

        lazerSound = MediaPlayer.create(this, R.raw.lasershot);
        beatBox = MediaPlayer.create(this, R.raw.beatbox);
        gunshot = MediaPlayer.create(this, R.raw.gunshot);
        on = false;
    }

    public void onToggleClicked(View view) {
            // Is the toggle on?
            if(toggleButton.isChecked()) {
                on = true;
            }
            else {
                on = false;
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            on = true;
            beatBox.reset();
            gunshot.reset();

        }

        if (on == true) {

        xText.setText("X: " + event.values[0]);
        zText.setText("Z: " + event.values[1]);
        yText.setText("Y: " + event.values[2]);

            if (event.values[0] > event.values[1] && event.values[0] > event.values[2]) {

                lazerSound.start();
                xText.setTextColor(Color.RED);
                zText.setTextColor(originalColor);
                yText.setTextColor(originalColor);


            } else if (event.values[1] > event.values[0] && event.values[1] > event.values[2]) {

                beatBox.start();
                xText.setTextColor(originalColor);
                zText.setTextColor(Color.GREEN);
                yText.setTextColor(originalColor);


            } else if (event.values[2] > event.values[0] && event.values[2] > event.values[1]) {

                gunshot.start();
                xText.setTextColor(originalColor);
                zText.setTextColor(originalColor);
                yText.setTextColor(Color.BLUE);

            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    // Används inte
    }
}
