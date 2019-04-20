package com.example.myapp;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class consumptionReportFragment extends Fragment {
    public consumptionReportFragment() {
    }


    protected BarChart chart;

    private TextView cstv;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_consumption_report, container, false);

        chart = (BarChart) view.findViewById(R.id.chartWeek);


        final int minColor = Color.rgb(43, 176, 221);
        final int normColor = Color.GRAY;
        final int maxColor = Color.RED;

        ArrayList<BarEntry> entries = new ArrayList<>();
        int[] colors = new int[]{Color.RED, Color.BLACK, Color.BLUE, Color.YELLOW};
        entries.add(new BarEntry(1f, 109230));
        entries.add(new BarEntry(2f, 125889));
        entries.add(new BarEntry(3f, 119403));
        entries.add(new BarEntry(4f, 143291));

        ArrayList<Integer> entryValues = new ArrayList<>();

        for (int i=0; i < entries.size(); i++) {
            entryValues.add((int) entries.get(i).getY());
        }

        Collections.sort(entryValues);

        for(int i=0 ; i< entryValues.size(); i++) {

            if(entries.get(i).getY() == entryValues.get(0)) { // set min value color
                colors[i] = minColor;
            }
            else if (entries.get(i).getY() == entryValues.get(entryValues.size()-1)) { // set max value color
                colors[i] = maxColor;
            }
            else {
                colors[i] = normColor;
            }

        }


        String[] labels = new String[] {"", "1주차", "2주차", "3주차", "4주차"};

        int tempmonth = 3;
        int tempdate = 9;

        for(int i=1; i< labels.length; i++) {
            String tempstr = "~" + String.valueOf(tempmonth) + "." + String.valueOf(tempdate);
            labels[i] = tempstr;
            tempdate += 7;
        }

        Description desc ;
        Legend L;

        L = chart.getLegend();
        desc = chart.getDescription();
        desc.setText(""); // this is the weirdest way to clear something!!
        L.setEnabled(false);


        YAxis leftAxis = chart.getAxisLeft();
        YAxis rightAxis = chart.getAxisRight();
        XAxis xAxis = chart.getXAxis();

        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextSize(11f);
        xAxis.setTextColor(Color.rgb(155,155,155));

        xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));
        xAxis.setLabelCount(4);
        xAxis.setDrawAxisLine(true);
        xAxis.setDrawGridLines(false);

        leftAxis.setDrawLabels(true);
        leftAxis.setAxisMinimum(90000);
        leftAxis.setGranularity(30000);
        leftAxis.setYOffset(-30f);
        leftAxis.setTextColor(Color.rgb(155,155,155));
        leftAxis.setDrawAxisLine(true);
        leftAxis.setDrawGridLines(false);

        rightAxis.setDrawAxisLine(false);
        rightAxis.setDrawGridLines(false);
        rightAxis.setDrawLabels(false);

        BarDataSet set = new BarDataSet(entries, "");
        set.setColors(colors);
        set.setValueTextColor(Color.rgb(90,90,90));
        set.setValueTextSize(9);

        BarData data = new BarData(set);


        data.setBarWidth(0.6f); // set custom bar width

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


        return view;
    }
}

