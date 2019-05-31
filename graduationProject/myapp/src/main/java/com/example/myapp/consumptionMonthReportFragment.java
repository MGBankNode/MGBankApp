package com.example.myapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import java.util.ArrayList;

public class consumptionMonthReportFragment extends Fragment {

    String userID;
    BarChart monthChart;
    ArrayList<String> dateList;
    BroadcastReceiver receiver = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_consumption_month_report, container, false);

        dateList = getArguments().getStringArrayList("dateList");
        Log.d(">>>size", String.valueOf(dateList.size()));
        monthChart = view.findViewById(R.id.chartMonth);
        ArrayList<BarEntry> entries = new ArrayList<>();
        ArrayList<Integer> resultSumList = new ArrayList<>();

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("SEND_USERID");
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {


                userID = intent.getStringExtra("userID");


                }

            }
        };

     //   Log.d(">>>sumlist", String.valueOf(resultSumList));
        getContext().registerReceiver(receiver, intentFilter);
        Intent intent = new Intent();
        intent.setAction("GET_USERID");
        getActivity().sendBroadcast(intent);




//        // 그래프에 데이터 삽입
//        for(int i=1; i<=4; i++)
//            entries.add(new BarEntry(i, i*10000));

        String[] labels = new String[] {"", "1", "2", "3", "4"};

        YAxis leftAxis = monthChart.getAxisLeft();
        YAxis rightAxis = monthChart.getAxisRight();
        XAxis xAxis = monthChart.getXAxis();

        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextSize(11f);
        xAxis.setTextColor(Color.rgb(155,155,155));

        xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));
        xAxis.setLabelCount(4);
        xAxis.setDrawAxisLine(true);
        xAxis.setDrawGridLines(false);

        leftAxis.setDrawLabels(true);
        leftAxis.setAxisMinimum(0);
        //leftAxis.setGranularity(7000);
        leftAxis.setTextColor(Color.rgb(155,155,155));
        leftAxis.setDrawAxisLine(true);
        leftAxis.setDrawGridLines(false);

        rightAxis.setDrawAxisLine(false);
        rightAxis.setDrawGridLines(false);
        rightAxis.setDrawLabels(false);

        BarDataSet set = new BarDataSet(entries, "");

        set.setValueTextColor(Color.rgb(90,90,90));
        set.setValueTextSize(9);

        BarData data = new BarData(set);

        data.setBarWidth(0.6f); // set custom bar width

        setChartData(monthChart, data);


      return view;
    }

    public void setChartData(BarChart chart,BarData data) {
        Description desc;
        Legend L;

        desc = chart.getDescription();
        desc.setText(""); // this is the weirdest way to clear something!!

        L = chart.getLegend();
        L.setEnabled(false);

        chart.setData(data);
        chart.setFitBars(true); // make the x-axis fit exactly all bars
        chart.invalidate(); // refresh
        chart.setScaleEnabled(false);
        chart.setDoubleTapToZoomEnabled(false);
        chart.setBackgroundColor(Color.rgb(255, 255, 255));
        chart.animateXY(2000, 2000);
        chart.setDrawBorders(false);
        chart.setDescription(desc);
        chart.setDrawValueAboveBar(true);
    }

}
