package com.example.myapp;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.DecimalFormat;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeTextFragment extends Fragment {


    public HomeTextFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home_text, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        //텍스트 셋팅
        setMainTextView();

        super.onActivityCreated(savedInstanceState);
    }

    //텍스트 셋팅 메소드
    public void setMainTextView(){
        TextView tv = (TextView)getView().findViewById(R.id.mainFragment_textView);
        DecimalFormat formater = new DecimalFormat("###,###");
        String moneyString = formater.format(123456789);
        tv.setText(moneyString + "원");

        //밑줄을 추가한다.
        /*SpannableString content = new SpannableString("99999999원");
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        tv.setText(content);*/
    }
}
