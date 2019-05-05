package com.example.myapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RelativeLayout;

public class MypageActivity extends AppCompatActivity {

    private RelativeLayout registeraccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mypage);

        registeraccount = findViewById(R.id.registerAccount);
        registeraccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startSetting();
            }
        });
    }

    private void startSetting() {
        startActivity(new Intent(this, SettingDialogActivity.class));
    }
}
