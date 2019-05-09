package com.example.myapp;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class backPressCloseHandler {
    private long backKeyPressedTime = 0;
    private Toast toast;
    private Activity activity;

    public backPressCloseHandler(Activity context) {

        this.activity = context;

    }

    public void onBackPressed() {

        if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
            backKeyPressedTime = System.currentTimeMillis();
            showToast();
            return;
        }
        if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
            activity.finish(); toast.cancel();
        }
    }

    public void showToast() {
        toast = Toast.makeText(activity, "\'뒤로\' 버튼을 한번 더 누르시면 종료됩니다." , Toast.LENGTH_SHORT); toast.show();
    }

}
