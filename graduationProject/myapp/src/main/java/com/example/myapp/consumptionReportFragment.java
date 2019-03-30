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
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.util.ArrayList;
import java.util.List;

public class consumptionReportFragment extends Fragment {
    public consumptionReportFragment() {
    }


    private LineChart lineChart;

    private TextView cstv;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_consumption_report, container, false);



        lineChart = (LineChart)view.findViewById(R.id.chart);
        cstv = view.findViewById(R.id.consumptionSumTv);

        final List<Entry> entries = new ArrayList<>();
        entries.add(new Entry(1, 109230));
        entries.add(new Entry(2, 115889));
        entries.add(new Entry(3, 123456));
//        entries.add(new Entry(4, 4));
//        entries.add(new Entry(5, 3));

        LineDataSet lineDataSet = new LineDataSet(entries, "주별 지출");
        lineDataSet.setLineWidth(2);
        lineDataSet.setCircleRadius(6);
        lineDataSet.setValueTextSize(12);



        lineDataSet.setCircleColor(Color.parseColor("#FFA1B4DC")); // LineChart에서 Line Circle Color 설정
        //lineDataSet.setCircleColorHole(Color.BLUE); // LineChart에서 Line Hole Circle Color 설정
        lineDataSet.setColor(Color.parseColor("#FFA1B4DC")); // LineChart에서 Line Color 설정
        lineDataSet.setDrawCircleHole(true);
        lineDataSet.setDrawCircles(true);
        lineDataSet.setDrawHorizontalHighlightIndicator(false);
        lineDataSet.setDrawHighlightIndicators(false);
        lineDataSet.setDrawValues(true);

        LineData lineData = new LineData(lineDataSet);


        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM); // x축 표시에 대한 위치 설정으로 아래쪽에 위치시킨다.
        xAxis.setTextColor(Color.BLACK); // x축 텍스트 컬러 설정

        xAxis.setTextSize(11);

        xAxis.setLabelCount(3, true);
//        xAxis.setAxisMinimum(1);

        YAxis yLAxis = lineChart.getAxisLeft();
        yLAxis.setTextColor(Color.BLACK);
        yLAxis.setDrawLabels(true);
        yLAxis.setDrawAxisLine(false);
        yLAxis.setDrawGridLines(false);
        yLAxis.setLabelCount(3);

        // y축 오른쪽 비활성화
        YAxis yRAxis = lineChart.getAxisRight();
        yRAxis.setDrawLabels(true);
        yRAxis.setDrawAxisLine(false);
        yRAxis.setDrawGridLines(false);
        yRAxis.setLabelCount(3);


        Description description = new Description();
        description.setText("");

        lineChart.setDoubleTapToZoomEnabled(false);
        lineChart.setDrawGridBackground(false);
        lineChart.setDescription(description);
        lineChart.animateY(2000, Easing.EaseInCubic);
        lineChart.setData(lineData);
        lineChart.invalidate();


        int temp = 0;

        for (int i=0; i<entries.size(); i++) {
            temp += entries.get(i).getY();
        }

        temp /= 3;

        cstv.setText("이번 주 평균 지출은 : " + Integer.toString(temp) + "원 입니다");

        return view;
    }
}

