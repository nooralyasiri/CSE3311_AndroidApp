package com.vkevvinn.couchcast;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.vkevvinn.couchcast.backend.FirestoreWrapper;
import com.vkevvinn.couchcast.backend.PasswordWrapper;

import java.util.HashMap;
import java.util.Map;

public class Registration extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        TextView Login_Back;
        FirestoreWrapper firestoreWrapper = new FirestoreWrapper();


        Login_Back = findViewById(R.id.loginReturn2);
        Login_Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent downloadIntent = new Intent(getApplicationContext(), Login.class);
                startService(downloadIntent);
                startActivity(new Intent(Registration.this, Login.class));
            }
        });

        Button registerButton = findViewById(R.id.registerbutton);
        registerButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                TextView email = findViewById(R.id.email);
                TextView emailConfirm = findViewById(R.id.emailConfirm);
                if( email.getText().toString().equals(emailConfirm.getText().toString()) )
                {
                    Map<String, Object> userInfo = new HashMap<>();
                    userInfo.put("userName", findViewById(R.id.usernameNew).toString());
                    userInfo.put("firstName", findViewById(R.id.firstName).toString());
                    userInfo.put("lastName", findViewById(R.id.lastName).toString());
                    userInfo.put("email", findViewById(R.id.email).toString());
                    userInfo.put("createTime", String.valueOf(System.currentTimeMillis()));
                    userInfo.put("hashedPassword", PasswordWrapper.hashPassword(findViewById(R.id.passNew).toString(), String.valueOf(userInfo.get("createTime"))));

                    firestoreWrapper.addNewUser(userInfo);
                }
            }
        });
    }
}