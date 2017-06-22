package io.spoton.spacium;

import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import io.spoton.spacium.views.KScope;
import io.spoton.spacium.views.SpacView;

public class Main extends AppCompatActivity implements SensorEventListener {

    private SensorManager mSensorManager;
    private Sensor mSensor;

    private SpacView view;

    public Main() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_UI);

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        this.view = new KScope(this, metrics);
        setContentView((View) this.view);

    }

    @Override
    protected void onResume() {
        super.onResume();
        this.view.activateView();
    }

    @Override
    protected void onPause() {
        super.onPause();
        this.view.pauseView();
    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        float[] eValues = event.values;
        float sensorValue = Math.abs(eValues[0]);

        if(sensorValue > 0.5){
            this.view.setRotation(sensorValue);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
//        Log.i("Accuracy Change: ", Integer.toString(accuracy));
    }
}
