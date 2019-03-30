package com.example.myapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;

import com.example.myapp.R;

public class loginFragment extends Fragment {

    Button loginCancleBtn;
    ScrollView loginFrag;
    EditText etLogin;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        etLogin = view.findViewById(R.id.etLogin);
        etLogin.requestFocus();

        loginFrag = view.findViewById(R.id.loginFrag);
        loginCancleBtn = view.findViewById(R.id.logInCancleBtn);
        loginCancleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginFrag.setVisibility(View.GONE);
            }
        });

        return view;
    }
}
