package com.example.myapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class loginActivity extends AppCompatActivity {

    Button loginCancleBtn;
    Button loginConfirmBtn;
    Button moveToSignupBtn;
    EditText etLogin;
    String loginflag = " ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        Intent intent = new Intent(loginActivity.this, MainScreen.class);
        startActivity(intent);

        etLogin = findViewById(R.id.etLogin);
        etLogin.requestFocus();

        loginConfirmBtn = findViewById(R.id.logInConfirmBtn);
        loginConfirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // mainActivity start
                Intent mintent = new Intent(loginActivity.this, MainActivity.class);
                mintent.putExtra("loginUser", String.valueOf(etLogin.getText()));
                mintent.putExtra("loginflag", "start");
                startActivity(mintent);
            }
        });

        loginCancleBtn = findViewById(R.id.logInCancleBtn);
        loginCancleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exitApp();
            }
        });

        moveToSignupBtn = findViewById(R.id.moveToSignupBtn);
        moveToSignupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentSignup = new Intent(loginActivity.this, signupActivity.class);
                startActivity(intentSignup);
            }
        });
    }

    public void exitApp() {
        ActivityCompat.finishAffinity(this);
    }

}
