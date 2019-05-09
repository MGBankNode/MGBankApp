package com.example.myapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RelativeLayout;

public class MypageActivity extends AppCompatActivity {

    private RelativeLayout registeraccount;
    private DeviceInfo myDeviceInfo;
    private String deviceCheckResult;
    private int userAccountCheck;
    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mypage);

        Intent intent = getIntent();
        myDeviceInfo = (DeviceInfo) intent.getSerializableExtra("DeviceInfoObject");
        deviceCheckResult = intent.getStringExtra("DeviceCheckResult");
        userAccountCheck = Integer.parseInt(intent.getStringExtra("UserAccountCheck"));
        userID = intent.getStringExtra("userID");

        registeraccount = findViewById(R.id.registerAccount);
        registeraccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MypageActivity.this, SettingDialogActivity.class);
                intent.putExtra("DeviceInfoObject", myDeviceInfo);
                intent.putExtra("DeviceCheckResult", deviceCheckResult);
                intent.putExtra("UserAccountCheck", String.valueOf(userAccountCheck));
                intent.putExtra("UserID", userID);
                startActivityForResult(intent, 1);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 1){
            if(resultCode == RESULT_OK){
                deviceCheckResult = data.getStringExtra("DeviceCheckResult");
                userAccountCheck = Integer.parseInt(data.getStringExtra("UserAccountCheck"));

                Intent intent = new Intent();
                intent.putExtra("DeviceCheckResult", deviceCheckResult);
                intent.putExtra("UserAccountCheck", String.valueOf(userAccountCheck));
                setResult(RESULT_OK, intent);

            }else if(resultCode == RESULT_CANCELED){
                deviceCheckResult = data.getStringExtra("DeviceCheckResult");
                userAccountCheck = Integer.parseInt(data.getStringExtra("UserAccountCheck"));

                Intent intent = new Intent();
                intent.putExtra("DeviceCheckResult", deviceCheckResult);
                intent.putExtra("UserAccountCheck", String.valueOf(userAccountCheck));
                setResult(RESULT_CANCELED, intent);
                finish();
            }
        }
    }

}
