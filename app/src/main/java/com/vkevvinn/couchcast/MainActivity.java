package com.vkevvinn.couchcast;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    Button LOGIN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //creating variables
        TextView forgotpw, createuser;
        Button enterButton;

        LOGIN = findViewById(R.id.enterButton);
        LOGIN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent downloadIntent = new Intent(getApplicationContext(), Login.class);
                startService(downloadIntent);
                startActivity(new Intent(MainActivity.this, Login.class));
            }
        });
    }

}
