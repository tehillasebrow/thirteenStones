package com.example.thirteenstones.activities.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.preference.PreferenceManager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import com.example.thirteenstones.R;
import com.example.thirteenstones.activities.lib.Utils;



public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       /* AppCompatDelegate.setDefaultNightMode(Build.VERSION.SDK_INT < 28 ?
                AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY :
                AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);*/
        PreferenceManager.setDefaultValues(this, R.xml.root_preferences, false);
        Utils.setNightModeOnOffFromPreferenceValue(getApplicationContext(), getString(R.string.night_mode_key));


        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        finish();
    }
}