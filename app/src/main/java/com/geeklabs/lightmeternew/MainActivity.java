package com.geeklabs.lightmeternew;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private TextView lightValue;
    private TextView resultText;
    private ImageView imageView;
    private float maxLux;
    private final String TAG = "LightMeter";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_light_meter);

        lightValue = findViewById(R.id.lightValue);
        resultText = findViewById(R.id.resultText);
        imageView = findViewById(R.id.imageView);

        SensorManager mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        Sensor mLight = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);

        maxLux = mLight.getMaximumRange();
        mSensorManager.registerListener(this, mLight, SensorManager.SENSOR_DELAY_NORMAL);

    }

    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        if (sensor.getType() == Sensor.TYPE_LIGHT) {
            // Handle onAccuracyChanged here
            Log.i(TAG, "Sensor type is light");
        }
    }

    public void onSensorChanged(SensorEvent event) {

        if (event.sensor.getType() == Sensor.TYPE_LIGHT) {

            float currentLux = event.values[0];

            NumberFormat formatter = new DecimalFormat("#0");
            lightValue.setText(formatter.format(currentLux));

            double maxAlpha = 1;
            double alpha = ((currentLux * maxAlpha) / maxLux) * 100;
            imageView.setAlpha((float) alpha);

            resultText.setText(getResultText(currentLux));

            /* Logs */
            Log.e(TAG, "currentLux = " + formatter.format(currentLux));
            Log.e(TAG, "Alpha: " + alpha);

        }

    }

    private String getResultText(double luxValue) {
        String result = "Minimum for ";

        if (luxValue < 50) {
            result = "Lighting conditions far below the minimum.";
        } else if (luxValue >= 50 && luxValue < 100) {
            result += "garage and general light of bedroom or living room.";
        } else if (luxValue >= 100 && luxValue < 150) {
            result += "staircase and general bathroom light.";
        } else if (luxValue >= 150 && luxValue < 200) {
            result += "hall and circulation areas.";
        } else if (luxValue >= 200 && luxValue < 300) {
            result += "illuminate bathroom mirror.";
        } else if (luxValue >= 300 && luxValue < 400) {
            result += "desk, to read, study or sew.";
        } else if (luxValue >= 400 && luxValue < 600) {
            result += "work environments such as the kitchen.";
        } else {
            result = "Excessive lighting environments.";
        }
        Log.e(TAG, "resultText: " + result);
        return result;
    }

}