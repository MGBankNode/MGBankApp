package com.example.myapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Toast;


public class loginFragment extends AppCompatActivity {

    Button loginCancleBtn;
    Button loginConfirmBtn;
    EditText etLogin;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_login);

        etLogin = findViewById(R.id.etLogin);
        etLogin.requestFocus();


        loginCancleBtn = findViewById(R.id.logInCancleBtn);
        loginCancleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "로그인 취소", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
        loginConfirmBtn = findViewById(R.id.logInConfirmBtn);
        loginConfirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "로그인 성공", Toast.LENGTH_SHORT).show();
                finish();
            }
        });


    }
}