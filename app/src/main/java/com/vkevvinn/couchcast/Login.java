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

        //***testing day/night mode. temporary forgotpw->settings
        TextView settings = findViewById(R.id.textView2);
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent downloadIntent = new Intent(getApplicationContext(), Settings.class);
                startService(downloadIntent);
                startActivity(new Intent(Login.this, Settings.class));
            }
        });
        //***

    }
}
