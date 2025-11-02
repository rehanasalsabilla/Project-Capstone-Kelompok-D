package com.example.capstone;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log; // Tambahkan import log
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SensorActivity extends AppCompatActivity {
    private static final String TAG = "SensorActivity"; // Tag log
    private Switch switchSensor;
    private TextView tvSensorStatus;
    private static final String PREF_NAME = "SensorPrefs";
    private static final String KEY_SENSOR_STATUS = "sensor_status";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor);
        Log.d(TAG, "onCreate: SensorActivity started");

        switchSensor = findViewById(R.id.switchSensor);
        tvSensorStatus = findViewById(R.id.tvSensorStatus);

        SharedPreferences prefs = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        boolean isSensorOn = prefs.getBoolean(KEY_SENSOR_STATUS, false);
        Log.d(TAG, "Loaded sensor status from SharedPreferences: " + isSensorOn);

        switchSensor.setChecked(isSensorOn);
        tvSensorStatus.setText(isSensorOn ? "Sensor On" : "Sensor Off");

        if (isSensorOn) {
            Log.d(TAG, "Sensor is ON, starting MusicService");
            Intent serviceIntent = new Intent(SensorActivity.this, MusicService.class);
            startService(serviceIntent);
        }

        switchSensor.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.d(TAG, "Switch toggled. New value: " + isChecked);

                SharedPreferences.Editor editor = getSharedPreferences(PREF_NAME, MODE_PRIVATE).edit();
                editor.putBoolean(KEY_SENSOR_STATUS, isChecked);
                editor.commit();
                Log.d(TAG, "Sensor status saved to SharedPreferences: " + isChecked);

                if (isChecked) {
                    tvSensorStatus.setText("Sensor On");
                    Log.d(TAG, "Switch ON: Starting MusicService");
                    Intent serviceIntent = new Intent(SensorActivity.this, MusicService.class);
                    startService(serviceIntent);
                } else {
                    tvSensorStatus.setText("Sensor Off");
                    Log.d(TAG, "Switch OFF: Stopping MusicService");
                    Intent serviceIntent = new Intent(SensorActivity.this, MusicService.class);
                    stopService(serviceIntent);

                    Log.d(TAG, "Restarting application...");
                    Intent restartIntent = getBaseContext().getPackageManager()
                            .getLaunchIntentForPackage(getBaseContext().getPackageName());
                    if (restartIntent != null) {
                        restartIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(restartIntent);
                        finish();
                        Log.d(TAG, "SensorActivity finished, app restarting.");
                    } else {
                        Log.w(TAG, "RestartIntent is null. Could not restart app.");
                    }
                }
            }
        });
    }
}
