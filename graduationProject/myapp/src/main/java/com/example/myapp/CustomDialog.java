package com.example.myapp;


import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
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


/**
 * A simple {@link Fragment} subclass.
 */
public class CustomDialog extends DialogFragment {
    private Stat stat;
    private String result;
    private CustomDialogResult dialogResult;
    private Fragment fragment;
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
        tv.setText(getArguments().getString("PAY_ACCOUNT"));
        tv = (TextView) getView().findViewById(R.id.pay_account_stat_name);
        tv.setText(getArguments().getString("PAY_ACCOUNT_STAT"));

        //스피너 설정
        final Spinner spinner = (Spinner) getView().findViewById(R.id.pay_account_stat_spinner);
        String[] item = Stat.statNames;

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity().getApplicationContext(),
                R.layout.spin_dropdown, item);

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
                result = spinner.getSelectedItem().toString();
                dialogResult.finish(result);
                DialogFragment dialogFragment = (DialogFragment)fragment;
                dialogFragment.dismiss();
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

}
