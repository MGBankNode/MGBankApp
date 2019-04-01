package com.example.myapp;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.Timer;
import java.util.TimerTask;


public class MainScreen extends AppCompatActivity {


    public MainScreen() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainscreen);

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                finish();
            }
        }, 3000);
    }


}


    //    @Override
//    public void onAttach(Activity activity) {
//        myContext = (FragmentActivity) activity;
//
//        super.onAttach(activity);
//    }

//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.mainscreen, container, false);
//
//        maincontainer = view.findViewById(R.id.mainScreenContainer);

//        fragmanager = myContext.getSupportFragmentManager();

//        Timer timer = new Timer();
//        timer.schedule(new TimerTask() {
//
//            @Override
//            public void run() {
//
//            }
//        }, 3000);
//
//        //maincontainer.setVisibility(View.GONE);
//        try {
//            Thread.sleep(3000);
//        } catch (InterruptedException e) {
//
//        }

//        Fragment fr;
//        fr = new loginActivity();
//        FragmentManager fm = fragmanager;
//        FragmentTransaction fragmentTransaction = fm.beginTransaction();
//        fragmentTransaction.replace(R.id.dynamic_mainFragment, fr);
//        fragmentTransaction.commit();



//        return view;


