package com.vkevvinn.couchcast;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        TextView NEW_ACCOUNT;


        NEW_ACCOUNT = findViewById(R.id.textView5);
        NEW_ACCOUNT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent downloadIntent = new Intent(getApplicationContext(), Registration.class);
                startService(downloadIntent);
                startActivity(new Intent(Login.this, Registration.class));
            }
        });

        Button LOGIN_BUTTON = findViewById(R.id.loginbutton);
        LOGIN_BUTTON.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent downloadIntent = new Intent(getApplicationContext(), BotNavActivity.class);
                startService(downloadIntent);
                startActivity(new Intent(Login.this, BotNavActivity.class));
            }
        });
    }
}
