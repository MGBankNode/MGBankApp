package com.example.myapp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class menu1_fragment_tab0 extends Fragment {

    String userID;

    public menu1_fragment_tab0() {    }

    @Nullable
    @Override //child fragment 구현
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.menu1_fragment_tab0,
                container, false);

        if(getArguments() != null){
            userID = getArguments().getString("ID");
            Log.i("nkw","menu1_tab0_userID="+userID);
        }
        return layout;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
}
