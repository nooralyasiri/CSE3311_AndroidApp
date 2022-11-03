package com.vkevvinn.couchcast;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.vkevvinn.couchcast.backend.FirestoreWrapper;
import com.vkevvinn.couchcast.backend.PasswordWrapper;

import java.util.HashMap;
import java.util.Map;

public class Registration extends AppCompatActivity {

    FirestoreWrapper firestoreWrapper = new FirestoreWrapper();
    TextView email;
    TextView emailConfirm;
    TextView password;
    TextView passConfirm;
    TextView userName;
    TextView firstName;
    TextView lastName;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        TextView Login_Back;


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
                email = findViewById(R.id.email);
                emailConfirm = findViewById(R.id.emailConfirm);
                if( !TextUtils.isEmpty(email.getText().toString()) && email.getText().toString().equals(emailConfirm.getText().toString()) )
                {
                    password = findViewById(R.id.passNew);
                    passConfirm = findViewById(R.id.passConfirm);
                    if( !TextUtils.isEmpty(password.getText().toString()) && password.getText().toString().equals(passConfirm.getText().toString()) ) {

                        userName = findViewById(R.id.usernameNew);
                        getUserInfo();
                    }

                    else {
                        password.setText("Password must not be empty and match!");
                        passConfirm.setText("");
                    }
                }

                else {
                    email.setText("E-Mails must not be empty and match!");
                    emailConfirm.setText("");
                }
            }
        });
    }

    private void addNewUser() {
        Map<String, Object> userInfoMap = new HashMap<>();

        firstName = findViewById(R.id.firstName);
        lastName = findViewById(R.id.lastName);
        userInfoMap.put("userName", userName.getText().toString());
        userInfoMap.put("firstName", firstName.getText().toString());
        userInfoMap.put("lastName", lastName.getText().toString());
        userInfoMap.put("email", email.getText().toString());
        userInfoMap.put("createTime", String.valueOf(System.currentTimeMillis()));
        userInfoMap.put("hashedPassword", PasswordWrapper.hashPassword(password.getText().toString(), String.valueOf(userInfoMap.get("createTime"))));

        firestoreWrapper.addNewUser(userInfoMap)
            .addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Log.d(TAG, "DocumentSnapshot successfully written!");
                    Intent loginScreen = new Intent(Registration.this, Login.class);
                    startActivity(loginScreen);
                }
            })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.w(TAG, "Error writing document", e);
                    userName.setText(e.toString());
                }
            });
    }

    private void getUserInfo() {
        firestoreWrapper.getUserInfo(userName.getText().toString())
            .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        if( task.getResult().exists() ) {
                            userName.setText("Username already in use!");
                        }

                        else {
                            addNewUser();
                        }

                    } else {
                        userName.setText("Please try again!");
                    }
                }
            });
    }
}