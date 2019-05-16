package com.example.myapp;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
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
    public consumptionReportFragment() {}

    final static int minColor = Color.rgb(43, 176, 221);
    final static int normColor = Color.GRAY;
    final static int maxColor = Color.RED;

    final static int WeekNum = 4;
    final static int DayNum = 7;

    public String lDay;
    public int cYear;
    public int cMonth;
    public int cDay;
    public String userID;

    public String[] labelArray;

    public BarChart weekChart;
    public BarChart dayChart;

    public TextView weekSum;
    public TextView daySum;

    View view;

    AnalysisInfo analysisWeekData;
    ImageButton imgbutton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_consumption_report, container, false);
//        userID = getArguments().getString("userID");
        userID= new MainActivity().userID;
        TextView mainDate = view.findViewById(R.id.reportMainDate);

        Bundle bundle = getArguments();
        analysisWeekData = (AnalysisInfo) bundle.getSerializable("dateData");
        lDay =  bundle.getString("LastDay");

        int reportMainMonth = bundle.getInt("Month");
        int reportMainWeek = bundle.getInt("Week");

        String mainDateStr = String.valueOf(reportMainMonth) + "월 " + String.valueOf(reportMainWeek) + "주차";
        mainDate.setText(mainDateStr);

       // String result = lDay;




        DrawWeekChart(view, lDay);
        DrawDayChart(view, analysisWeekData);

        return view;
    }

    //


    public void DrawDayChart(final View view, AnalysisInfo Info) {
        Context context = getContext();
        //Info.get
        // 이런 정보가 담겨져 있는 객체를 받아왔다고 가정
        AnalysisInfo analysisWeekInfo = Info;

        //요청 정보 입력!!!!!!!test
        AnalysisRequest test = new AnalysisRequest(
                userID,                               //현재 로그인 아이디
                analysisWeekInfo.getsDate(),                      //시작 날짜
                analysisWeekInfo.getlDate(),                       //마지막 날짜
                RequestInfo.RequestType.ANALYSIS_DAILY,    //고정
                context);                                 //고정


        //Request 함수 호출해서 정보 accountHistoryInfo 객체와 dailyHistoryInfo 객체에서 받아와서 사용
        test.DailyRequestHandler(new AnalysisRequest.VolleyCallback() {
            @Override
            public void onSuccess(AnalysisInfo[] info) {

                int sum = 0;

                dayChart =  view.findViewById(R.id.chartDay);
                daySum = view.findViewById(R.id.daySum);

                ArrayList<BarEntry> entries = new ArrayList<>();

                int[] sumArray = new int[8];

                int ci = 1;
                int cj = 0;
                while (true) {
                    if(ci>7)
                        break;

                    AnalysisInfo tempInfo = info[cj];

                    if(ci==Integer.parseInt(tempInfo.getDaily())) {
                        sumArray[ci] = Integer.parseInt(tempInfo.getDailySum());
                        ci++;
                        cj++;
                    }
                    else {
                        sumArray[ci] = 0;
                        ci++;
                    }
                }

                for(int i=1; i<sumArray.length; i++)
                    entries.add(new BarEntry(i, sumArray[i]));

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
        });
    }


    public void DrawWeekChart(View view, String str) {

        final String[] labelArray = new String[5];

        String result = str;

        weekChart =  view.findViewById(R.id.chartWeek);
        weekSum = view.findViewById(R.id.weekSum);

        String[] lDate = result.split("-");

        cYear = Integer.parseInt(lDate[0]);
        cMonth = Integer.parseInt(lDate[1]);
        cDay = Integer.parseInt(lDate[2]);

        labelArray[labelArray.length-1] = "~" + cMonth + "." + cDay;

        for(int i=0; i<4; i++) {
            cDay -= 7;

            result += ",";

            if(cDay < 1) {
                cMonth--;
                if(cMonth < 1) {
                    cMonth = 12;
                    cYear--;
                }

                if (cMonth == 2) {
                    cDay += 28;
                } else if (cMonth % 2 == 1 || cMonth == 8) {
                    cDay += 31;
                } else if (cMonth % 2 == 0) {
                    cDay += 30;
                }
            }
            String tempcMonth = String.valueOf(cMonth);
            String tempcDay = String.valueOf(cDay);

            if(cMonth < 10)
                tempcMonth = "0" + cMonth;
            if(cDay < 10)
                tempcDay = "0" + cDay;

            labelArray[labelArray.length-i-2] = "~" + tempcMonth + "." + tempcDay;
            result = result + (cYear + "-" + tempcMonth + "-" + tempcDay);

        }
        labelArray[0] = "";

        // 끝 날짜 잡고 감소 시키면서 저장함 (내림차순)
        // 서버 호출 포멧은 오름차순이므로, 데이터 뒤집어줌
        String[] results = result.split(",");
        String[] reversed = new String[results.length];
        for(int i=0; i<results.length; i++)
            reversed[i] = results[results.length-i-1];


        result = reversed[0];

        for(int i=1; i<reversed.length; i++)
            result += ","+ reversed[i];

        Log.d(">>>result", result);


        AnalysisRequest test = new AnalysisRequest(
                "b",                               //현재 로그인 아이디
                result,            //날짜들 list
                RequestInfo.RequestType.ANALYSIS_WEEK,    //고정
                getContext());                                 //고정

        //Request 함수 호출해서 정보 accountHistoryInfo 객체와 dailyHistoryInfo 객체에서 받아와서 사용
        test.WeekRequestHandler(new AnalysisRequest.VolleyCallback() {
            @Override
            public void onSuccess(AnalysisInfo[] info) {
                int sum = 0;

                ArrayList<BarEntry> entries = new ArrayList<>();
                for(int i=0; i<info.length; i++)
                    entries.add(new BarEntry(i+1, Integer.parseInt(info[i].getWeekSum())));

                YAxis leftAxis = weekChart.getAxisLeft();
                YAxis rightAxis = weekChart.getAxisRight();
                XAxis xAxis = weekChart.getXAxis();

                xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                xAxis.setTextSize(11f);
                xAxis.setTextColor(Color.rgb(155,155,155));

                xAxis.setValueFormatter(new IndexAxisValueFormatter(labelArray));
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

        });



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

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        imgbutton = (ImageButton)getActivity().findViewById(R.id.close_fr_btn);

        //뒤로가기 버튼 누르면 프래그먼트 안보이게 하기
        imgbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getView().setVisibility(View.GONE);
            }
        });
        super.onActivityCreated(savedInstanceState);
    }
}

