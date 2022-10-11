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
                    TextView userName = findViewById(R.id.usernameNew);
                    TextView firstName = findViewById(R.id.firstName);
                    TextView lastName = findViewById(R.id.lastName);
                    TextView passsword = findViewById(R.id.passNew);
                    Map<String, Object> userInfo = new HashMap<>();
                    userInfo.put("userName", userName.getText().toString());
                    userInfo.put("firstName", firstName.getText().toString());
                    userInfo.put("lastName", lastName.getText().toString());
                    userInfo.put("email", email.getText().toString());
                    userInfo.put("createTime", String.valueOf(System.currentTimeMillis()));
                    userInfo.put("hashedPassword", PasswordWrapper.hashPassword(passsword.getText().toString(), String.valueOf(userInfo.get("createTime"))));

                    firestoreWrapper.addNewUser(userInfo);
                }
            }
        });
    }
}