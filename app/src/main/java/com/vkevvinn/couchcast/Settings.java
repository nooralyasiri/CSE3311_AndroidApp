package com.vkevvinn.couchcast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Switch;

import java.util.Objects;

public class Settings extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        //initializing variable
        Switch switchtheme = findViewById(R.id.switchtheme);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Dark Mode");
        //switch theme per user input
        switchtheme.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isOn) {
                if (isOn) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    compoundButton.setText("Night Mode");
                }
                else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    compoundButton.setText("Day Mode");
                }
            }
        });
        //set theme when app starts
        boolean isNightModeOn = AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES;
        switchtheme.setChecked(isNightModeOn);
        if (isNightModeOn) {
            switchtheme.setText("Night Mode");
        }
        else {
            switchtheme.setText("Day Mode");
        }


    }
}