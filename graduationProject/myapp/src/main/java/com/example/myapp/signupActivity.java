package com.example.myapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

public class signupActivity extends AppCompatActivity {


    Button signupConfirmBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);

        Spinner spinner = findViewById(R.id.spinner);
        String[] str = getResources().getStringArray(R.array.bankarray);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.bankarray, R.layout.spinneritem);
//        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    Toast.makeText(getApplicationContext(), (String) parent.getItemAtPosition(position) + "을 선택", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        signupConfirmBtn = findViewById(R.id.signupConfirmBtn);
        signupConfirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "로그인 성공", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

    }


}
