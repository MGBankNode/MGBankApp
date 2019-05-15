package com.example.myapp;


import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListPopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Stack;


/**
 * A simple {@link Fragment} subclass.
 */
public class CustomDialog extends DialogFragment {
    private Stat stat;
    private String result;
    private CustomDialogResult dialogResult;
    private Fragment fragment;
    private String selectedAccountName;

    public CustomDialog() {
        // Required empty public constructor
    }
    public interface CustomDialogResult{
        void finish(String result);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getDialog().getWindow().setWindowAnimations(R.style.DialogAnimation);
        getDialog().getWindow().setGravity(Gravity.CENTER_HORIZONTAL|Gravity.BOTTOM);

        fragment = getActivity().getSupportFragmentManager().findFragmentByTag("OpenDialog");

        stat = (Stat)getArguments().get("STAT");
        selectedAccountName = getArguments().getString("PAY_ACCOUNT");
        return inflater.inflate(R.layout.fragment_custom_dialog, container, false);
    }

    @Override
    public void onStart() {
        getDialog().getWindow()
                .setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                        WindowManager.LayoutParams.WRAP_CONTENT);
        super.onStart();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        TextView tv = (TextView) getView().findViewById(R.id.pay_account_name);
        tv.setText(selectedAccountName);
        tv = (TextView) getView().findViewById(R.id.pay_account_stat_name);
        tv.setText(stat.getName());

        //스피너 설정
        final Spinner spinner = (Spinner) getView().findViewById(R.id.pay_account_stat_spinner);
        ArrayList<String> item = new ArrayList<>(Arrays.asList(Stat.statNames));
        Stack<String> st = new Stack<>();
        String currentstat = getArguments().getString("PAY_ACCOUNT_STAT");

        for(String str : item) {
            if(str != currentstat)
                st.push(str);
        }
        st.push(currentstat);

        item.clear();

        while(true) {
            if(st.empty())
                break;

            item.add(st.pop());
        }


       //ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity().getApplicationContext(),
             //   R.layout.spin_dropdown, item);

        SpinnerAdapter adapter = new SpinnerAdapter(getContext(), item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) { }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) { }

        });

        final TextView textView = (TextView) getView().findViewById(R.id.pay_account_stat_change_button);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PayInfomation p = stat.getPayInfomation(selectedAccountName);
                result = spinner.getSelectedItem().toString();

                // /////////////////////카테고리 변경 요청////////////////////////////
                ///////////////////////////////////////////////////////////////////
                CategoryRequest categoryRequest = new CategoryRequest(
                        stat.getUserId(),                    //사용자 아이디
                        p.gethId(),       //사용처 이름
                        getCategoryId(stat.getName()),                  //기존 카테고리 번호
                        getCategoryId(result),                  //바꿀 카테고리 번호
                        getContext(),                   //context 고정
                        RequestInfo.RequestType.UPDATE_CATEGORY);   //고정

                categoryRequest.UpdateCategoryHandler(new CategoryRequest.VolleyCallback() {
                    @Override
                    public void onSuccess() {
                        result = spinner.getSelectedItem().toString();
                        dialogResult.finish(result);
                        DialogFragment dialogFragment = (DialogFragment)fragment;
                        dialogFragment.dismiss();

                    }

                    @Override
                    public void onFail() {

                    }

                    @Override
                    public void onError() {

                    }
                });
                ///////////////////////////////////////////////////////////////////
                ///////////////////////////////////////////////////////////////////
                dialogResult.finish(result);
            }
        });
        final TextView canceltextView = (TextView) getView().findViewById(R.id.pay_account_stat_cancel_button);
        canceltextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                result = "cancel";
                dialogResult.finish(result);
                DialogFragment dialogFragment = (DialogFragment)fragment;
                dialogFragment.dismiss();
            }
        });

        super.onActivityCreated(savedInstanceState);


    }
    public void setDialogResult(CustomDialogResult cdr){
        dialogResult = cdr;
    }
    public int getCategoryId(String s){
        switch(s){
            case Stat.DRINK: return 1;
            case Stat.LIFE: return 2;
            case Stat.TRAFFIC: return 3;
            case Stat.DWELLING: return 4;
            case Stat.HOSPITAL: return 5;
            case Stat.FINANCE: return 6;
            case Stat.CULTURE: return 7;
            case Stat.TRAVEL: return 8;
            case Stat.FOOD: return 9;
            case Stat.COFFEE: return 10;
            case Stat.NONE: return 11;
            case Stat.ACCOUNTSLOSS: return 13;
            default: return 0;
        }
    }

}
