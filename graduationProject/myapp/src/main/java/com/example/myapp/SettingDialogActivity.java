package com.example.myapp;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

/*
    SettingDialogActivity
 */

public class SettingDialogActivity extends Activity {

    Button switchFinishBtn;
    Switch pushSwitch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_dialog);

        switchFinishBtn = findViewById(R.id.switchFinishBtn);
        switchFinishBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        pushSwitch = findViewById(R.id.pushSwitch);
        pushSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked == true){
                    Toast.makeText(getApplicationContext(), "스위치-ON", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(getApplicationContext(), "스위치-OFF", Toast.LENGTH_SHORT).show();
                }
            }

        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //바깥레이어 클릭시 안닫히게
        if(event.getAction() == MotionEvent.ACTION_OUTSIDE){
            return false;
        }

        return true;
    }
}
