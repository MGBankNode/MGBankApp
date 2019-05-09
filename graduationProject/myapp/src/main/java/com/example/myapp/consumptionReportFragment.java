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

    final static int minColor = Color.rgb(43, 176, 221);
    final static int normColor = Color.GRAY;
    final static int maxColor = Color.RED;

    final static int WeekNum = 4;
    final static int DayNum = 7;

    private BarChart weekChart;
    private BarChart dayChart;

    private TextView weekSum;
    private TextView daySum;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_consumption_report, container, false);

        DrawWeekChart(view);
        DrawDayChart(view);

        return view;
    }

    public void DrawDayChart(View view) {
        int sum = 0;

        dayChart =  view.findViewById(R.id.chartDay);
        daySum = view.findViewById(R.id.daySum);

        ArrayList<BarEntry> entries = new ArrayList<>();

        entries.add(new BarEntry(1f, 19230));
        entries.add(new BarEntry(2f, 25889));
        entries.add(new BarEntry(3f, 21940));
        entries.add(new BarEntry(4f, 31321));
        entries.add(new BarEntry(5f, 24529));
        entries.add(new BarEntry(6f, 43291));
        entries.add(new BarEntry(7f, 14291));

        String[] labels = new String[] {"", "월", "화", "수", "목", "금", "토", "일"};

        YAxis leftAxis = dayChart.getAxisLeft();
        YAxis rightAxis = dayChart.getAxisRight();
        XAxis xAxis = dayChart.getXAxis();

        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextSize(11f);
        xAxis.setTextColor(Color.rgb(155,155,155));

        xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));
        xAxis.setLabelCount(7);
        xAxis.setDrawAxisLine(true);
        xAxis.setDrawGridLines(false);

        leftAxis.setDrawLabels(true);
        leftAxis.setAxisMinimum(9000);
        leftAxis.setGranularity(6000);
        leftAxis.setYOffset(-30f);
        leftAxis.setTextColor(Color.rgb(155,155,155));
        leftAxis.setDrawAxisLine(true);
        leftAxis.setDrawGridLines(false);

        rightAxis.setDrawAxisLine(false);
        rightAxis.setDrawGridLines(false);
        rightAxis.setDrawLabels(false);

        BarDataSet set = new BarDataSet(entries, "");
        set.setColors(decisionColor(entries, DayNum));
        set.setValueTextColor(Color.rgb(90,90,90));
        set.setValueTextSize(9);

        BarData data = new BarData(set);

        data.setBarWidth(0.6f); // set custom bar width

        setChartData(dayChart, data);

        for(int i=0; i<entries.size(); i++)
            sum += (int) entries.get(i).getY();

        sum /= DayNum;

        String temp = sum + "원 입니다!";

        daySum.append(temp);


    }


    public void DrawWeekChart(View view) {
        int sum = 0;

        weekChart =  view.findViewById(R.id.chartWeek);
        weekSum = view.findViewById(R.id.weekSum);

        ArrayList<BarEntry> entries = new ArrayList<>();

        entries.add(new BarEntry(1f, 139230));
        entries.add(new BarEntry(2f, 155889));
        entries.add(new BarEntry(3f, 149403));
        entries.add(new BarEntry(4f, 173291));

        String[] labels = new String[] {"", "1주차", "2주차", "3주차", "4주차"};

        int tempmonth = 3;
        int tempdate = 9;

        for(int i=1; i< labels.length; i++) {
            String tempstr = "~" + tempmonth + "." + tempdate;
            labels[i] = tempstr;
            tempdate += 7;
        }


        YAxis leftAxis = weekChart.getAxisLeft();
        YAxis rightAxis = weekChart.getAxisRight();
        XAxis xAxis = weekChart.getXAxis();

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
        set.setColors(decisionColor(entries, WeekNum));
        set.setValueTextColor(Color.rgb(90,90,90));
        set.setValueTextSize(9);

        BarData data = new BarData(set);

        data.setBarWidth(0.6f); // set custom bar width

        setChartData(weekChart, data);

        for(int i=0; i<entries.size() - 1; i++)
            sum += (int) entries.get(i).getY();

        sum /= (WeekNum-1);

        String temp = sum + "원 입니다!";

        weekSum.append(temp);
    }

    public int[] decisionColor(ArrayList<BarEntry> entries, int size) {
        ArrayList<Integer> entryValues = new ArrayList<>();

        int[] result = new int[size];

        for (int i=0; i < entries.size(); i++) {
            entryValues.add((int) entries.get(i).getY());
        }

        Collections.sort(entryValues);

        for(int i=0 ; i< entryValues.size(); i++) {

            if(entries.get(i).getY() == entryValues.get(0)) { // set min value color
                result[i] = minColor;
            }
            else if (entries.get(i).getY() == entryValues.get(entryValues.size()-1)) { // set max value color
                result[i] = maxColor;
            }
            else {
                result[i] = normColor;
            }
        }

        return result;
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

