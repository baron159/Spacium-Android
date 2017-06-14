package io.spoton.spacium;

import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;

public class Main extends AppCompatActivity implements SensorEventListener {

    private double rValue = 0;
    private double gValue = 255;
    private double bValue = 0;

    private boolean rIncreaseFlag = true;
    private boolean gIncreaseFlag = false;
    private boolean bIncreaseFlag = true;

    private ConstraintLayout layoutMain;
    private SensorManager mSensorManager;
    private Sensor mSensor;

    public Main() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        layoutMain = (ConstraintLayout) findViewById(R.id.mainView);

        mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);

        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_UI);


    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float[] eValues = event.values;
        float sensorValue = Math.abs(eValues[0]);

        if(sensorValue > 0.5){
            float rateChange = sensorValue;
            // This is the red value change and test block
            if (!this.rIncreaseFlag) {
                this.rValue -= rateChange;
                if (this.rValue < 0) {
                    this.rValue = 0;
                    this.rIncreaseFlag = true;
                }
            }else{
                this.rValue += rateChange;
                if (this.rValue > 255) {
                    this.rValue = 255;
                    this.rIncreaseFlag = false;
                }
            }

            // This is the green value change and test block
            if (!this.gIncreaseFlag) {
                this.gValue -= rateChange;
                if (this.gValue < 0) {
                    this.gValue = 0;
                    this.gIncreaseFlag = true;
                }
            }else{
                this.gValue += rateChange;
                if (this.gValue > 255) {
                    this.gValue = 255;
                    this.gIncreaseFlag = false;
                }
            }

            // This is the blue value change and test block
            if (!this.bIncreaseFlag) {
                this.bValue -= rateChange;
                if (this.bValue < 0) {
                    this.bValue = 0;
                    this.bIncreaseFlag = true;
                }
            }else{
                this.bValue += rateChange;
                if (this.bValue > 255) {
                    this.bValue = 255;
                    this.bIncreaseFlag = false;
                }
            }

            // Now we create the color for the background to use;
            layoutMain.setBackgroundColor(Color.argb(255, (int)this.rValue, (int)this.gValue, (int)this.bValue));
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        Log.i("Accuracy Change: ", Integer.toString(accuracy));
    }
}
