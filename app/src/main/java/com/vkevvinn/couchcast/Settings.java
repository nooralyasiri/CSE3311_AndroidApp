package com.vkevvinn.couchcast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Switch;

public class Settings extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Switch switch1;
        boolean nightMODE;
        SharedPreferences sharedPreferences;
        final SharedPreferences.Editor[] editor = new SharedPreferences.Editor[1];

        switch1 = findViewById(R.id.switch1);
        sharedPreferences = getSharedPreferences( "MODE", Context.MODE_PRIVATE);
        nightMODE = sharedPreferences.getBoolean("night", false);

        if(nightMODE){
            switch1.setChecked(true);
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }
        switch1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                if(nightMODE){
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    editor[0] = sharedPreferences.edit();
                    editor[0].putBoolean("night", false);
                }
                else{
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    editor[0] = sharedPreferences.edit();
                    editor[0].putBoolean("night", true);
                }
                editor[0].apply();
            }
        });


        //not working correctly
        /*ImageButton back = findViewById(R.id.backbtn);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent downloadIntent = new Intent(getApplicationContext(), ProfileFragment.class);
                startService(downloadIntent);
                startActivity(new Intent(Settings.this, ProfileFragment.class));
            }
        });*/

    }
}