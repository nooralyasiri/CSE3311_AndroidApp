package com.vkevvinn.couchcast;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Registration extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        TextView Login_Back;
        Button registerbutton;


        Login_Back = findViewById(R.id.loginReturn2);
        Login_Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent downloadIntent = new Intent(getApplicationContext(), Login.class);
                startService(downloadIntent);
                startActivity(new Intent(Registration.this, Login.class));
            }
        });
        registerbutton = findViewById(R.id.registerbutton);
        registerbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent downloadIntent = new Intent(getApplicationContext(), Login.class);
                startService(downloadIntent);
                startActivity(new Intent(Registration.this, Login.class));
            }
        });




    }
}