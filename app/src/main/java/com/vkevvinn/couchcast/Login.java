package com.vkevvinn.couchcast;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.vkevvinn.couchcast.backend.BaseActivity;
import com.vkevvinn.couchcast.backend.FirestoreWrapper;
import com.vkevvinn.couchcast.backend.PasswordWrapper;

public class Login extends BaseActivity {

    FirestoreWrapper firestoreWrapper = new FirestoreWrapper();
    TextView userName;
    TextView passwordNoHash;

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

        Button loginButton = findViewById(R.id.loginbutton);
        loginButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                userName = findViewById(R.id.editTextTextUsername);
                passwordNoHash = findViewById(R.id.editTextTextPassword);

                if( !TextUtils.isEmpty(userName.getText()) && !TextUtils.isEmpty(passwordNoHash.getText()) ) {
                    attemptLogin();
                }
                else if (!TextUtils.isEmpty(userName.getText()) && TextUtils.isEmpty(passwordNoHash.getText())){
                    passwordNoHash.requestFocus();
                    passwordNoHash.setError(" Please Enter a Valid Password. ");
                }
                else if (TextUtils.isEmpty(userName.getText()) && !TextUtils.isEmpty(passwordNoHash.getText())){
                    userName.requestFocus();
                    userName.setError(" Please Enter a Valid Username. ");
                }
                else {
                    userName.requestFocus();
                    userName.setError(" Please Enter a Valid Username. ");
                    passwordNoHash.requestFocus();
                    passwordNoHash.setError(" Please Enter a Valid Password. ");
                    //passwordNoHash.setText("");
                }
            }
        });
    }

    private void attemptLogin() {
        firestoreWrapper.getUserInfo(userName.getText().toString())
            .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        if( task.getResult().exists() ) {
                            DocumentSnapshot docSnapshot = task.getResult();
                            if( docSnapshot.get("userName").toString().equals(userName.getText().toString())
                                    && PasswordWrapper.isValid(passwordNoHash.getText().toString(),
                                    docSnapshot.get("createTime").toString(),
                                    docSnapshot.get("hashedPassword").toString())) {
                                setUserName(docSnapshot.get("userName").toString());
                                Intent downloadIntent = new Intent(getApplicationContext(), BotNavActivity.class);
                                startService(downloadIntent);
                                startActivity(new Intent(Login.this, BotNavActivity.class));
//                                userName.setText("Successfully logged in!");
//                                passwordNoHash.setText("");
                            }
                        }

                        else {
                            userName.setText("Invalid login credentials!");
                            passwordNoHash.setText("");
                        }

                    } else {
                        userName.setText("Please try again!");
                        passwordNoHash.setText("");
                    }
                }
            });
    }
}