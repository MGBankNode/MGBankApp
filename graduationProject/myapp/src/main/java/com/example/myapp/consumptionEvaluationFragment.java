package com.example.myapp;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class consumptionEvaluationFragment extends Fragment {

    Fragment fr;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_consumption_evaluation, container, false);

        ListView reportListView = view.findViewById(R.id.reportList);
        Button detailBtn = view.findViewById(R.id.detailBtn);


        ArrayList<reportElement> reportElements = new ArrayList<>();
        reportElements.add(new reportElement("[3월 3주차 주간 리포트]", 149403));
        reportElements.add(new reportElement());
        reportElements.add(new reportElement("[3월 2주차 주간 리포트]", 155889));
        reportElements.add(new reportElement());
        reportElements.add(new reportElement("[3월 1주차 주간 리포트]", 139230));


        reportListviewAdapter reportListviewAdapter = new reportListviewAdapter(getContext(), R.layout.report_list_item, reportElements);
        reportListView.setAdapter(reportListviewAdapter);

        return view;
    }
}
