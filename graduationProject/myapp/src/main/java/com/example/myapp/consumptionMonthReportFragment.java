package com.example.myapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

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

    final static int minColor = Color.rgb(43, 176, 221);
    final static int normColor = Color.rgb(210, 210, 210);
    int curYear;
    int curMonth;
    BarChart monthChart;
    BroadcastReceiver receiver = null;
    TextView monthReportMainTv;
    TextView monthChartDetail;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_consumption_month_report, container, false);



        curYear = getArguments().getInt("Year");
        curMonth = getArguments().getInt("Month");
        monthChart = view.findViewById(R.id.chartMonth);
        monthReportMainTv = view.findViewById(R.id.monthReportMainTv);
        monthReportMainTv.setText(curMonth + "월 월간 리포트");

        monthChartDetail = view.findViewById(R.id.monthChartDetail);

        // 현재 달이 4월 달이면, 5~4, 4~3 .. 이런 식의 정보가 필요
        // curMonth 값을 증가 시켜주어 계산 용이하게 함
        curMonth++;


        ArrayList<BarEntry> entries = new ArrayList<>();

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("SEND_USERID");
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String[] labels = new String[5];
                String userID = intent.getStringExtra("userID");
                String requestDates = "";

                for(int i=0; i<5; i++) {
                    int tempCurMonth = curMonth - i;
                    int tempCurYear = curYear;

                    if (tempCurMonth < 1) {
                        tempCurMonth += 12;
                        tempCurYear--;
                    }

                    // 한 자리 수의 숫자에 0을 붙여주기 위한 변수 (9 -> 09)
                    String tempCurMonthStr = "";

                    if(String.valueOf(tempCurMonth).length()==1) {
                        tempCurMonthStr = "0" + (tempCurMonth);
                    }
                    else {
                        tempCurMonthStr = String.valueOf(tempCurMonth);
                    }

                    labels[i] = tempCurYear + "-" + tempCurMonthStr;
                    requestDates += tempCurYear + "-" + tempCurMonthStr + "-" + "01";

                    if(i<4)
                        requestDates += ",";

                }
                // 내림차순으로 되어있는 requestDates를 오름차순으로 변경
                String[] requestDatesArray = requestDates.split(",");

                for(int i=0; i<requestDatesArray.length/2 ; i++) {
                    String temp = requestDatesArray[i];
                    requestDatesArray[i] = requestDatesArray[requestDatesArray.length-1-i];
                    requestDatesArray[requestDatesArray.length-1-i] = temp;
                }
                requestDates = "";

                for(int i=0; i<requestDatesArray.length; i++) {
                    requestDates += requestDatesArray[i];

                    if(i<requestDatesArray.length - 1)
                        requestDates += ",";
                }
                labels[0] = "";
                // 내림차순으로 되어있는 labels를 오름차순으로 변경
                for(int i=1; i<labels.length/2+1; i++) {
                    String temp = labels[i];
                    labels[i] = labels[labels.length-i];
                    labels[labels.length-i] = temp;
                }
                ////////////////////////////////////////////////////////////////////////////////
                AnalysisRequest test1 = new AnalysisRequest(
                        userID,              //현재 로그인 아이디
                        requestDates,              //날짜들 list
                        RequestInfo.RequestType.ANALYSIS_MONTH,    //고정
                        context);                                 //고정

                test1.MonthRequestHandler(new AnalysisRequest.VolleyCallback() {
                    @Override
                    public void onSuccess(AnalysisInfo[] Info) {
                        int lastThreeMonthAvg = 0;
                        int differenceFromLastThreeMonth = 0;


                        for(int i=0; i<Info.length; i++)
                            entries.add(new BarEntry(i+1, Integer.parseInt(Info[i].getMonthSum())));

                        // 지난 세 달 간 지출 합
                        for(int i=0; i<Info.length-1; i++)
                            lastThreeMonthAvg += Integer.parseInt(Info[i].getMonthSum());

                        lastThreeMonthAvg /= 3;

                        // differenceFromLastThreeMonth 값이 양수이면 많이 지출, 음수이면 적게 지출
                        differenceFromLastThreeMonth = Integer.parseInt(Info[Info.length-1].getMonthSum()) - lastThreeMonthAvg;

                        if(differenceFromLastThreeMonth >= 0) {
                            monthChartDetail.append(Html.fromHtml("<u>" + (lastThreeMonthAvg) + "</u>"));
                            monthChartDetail.append("원 입니다\n\n이번 달은 지난 세 달간 평균 보다 ");
                            monthChartDetail.append(Html.fromHtml("<u>" + differenceFromLastThreeMonth + "</u>"));
                            monthChartDetail.append("원 더 사용하셨네요");
                        }
                        else {
                            monthChartDetail.append(Html.fromHtml("<u>" + (lastThreeMonthAvg) + "</u>"));
                            monthChartDetail.append("원 입니다\n\n이번 달은 지난 세 달간 평균 보다 ");
                            monthChartDetail.append(Html.fromHtml("<u>" + Math.abs(differenceFromLastThreeMonth) + "</u>"));
                            monthChartDetail.append("원 덜 사용하셨네요");
                        }
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
                        leftAxis.setAxisMinimum(Integer.parseInt(Info[0].getMonthSum())/2);
                        //leftAxis.setGranularity();
                        leftAxis.setTextColor(Color.rgb(155,155,155));
                        leftAxis.setDrawAxisLine(true);
                        leftAxis.setDrawGridLines(false);

                        rightAxis.setDrawAxisLine(false);
                        rightAxis.setDrawGridLines(false);
                        rightAxis.setDrawLabels(false);

                        BarDataSet set = new BarDataSet(entries, "");

                        set.setValueTextColor(Color.rgb(90,90,90));
                        set.setValueTextSize(9);
                        set.setColors(new int[]{normColor, normColor, normColor, minColor});

                        BarData data = new BarData(set);

                        data.setBarWidth(0.5f);

                        setChartData(monthChart, data);
                    }
                });
                ////////////////////////////////////////////////////////////////////////////////
            }
        };

        getContext().registerReceiver(receiver, intentFilter);
        Intent intent = new Intent();
        intent.setAction("GET_USERID");
        getActivity().sendBroadcast(intent);


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
