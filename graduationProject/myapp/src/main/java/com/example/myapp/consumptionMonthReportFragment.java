package com.example.myapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
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

import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

public class consumptionMonthReportFragment extends Fragment {

    final static int minColor = Color.rgb(43, 176, 221);
    final static int normColor = Color.rgb(210, 210, 210);
    int curYear;
    int curMonth;
    BarChart monthChart;
    BroadcastReceiver receiver = null;
    ImageButton monthReportPreviousBtn;
    TextView monthReportMainTv;
    TextView monthChartDetail;
    TextView mostVisitedPlace;
    TextView mostVisitedPlaceDetail;
    TextView mostSpentPlace;
    TextView mostSpentPlaceDetail;

    ListView usageByCardListView;
    Context mContext;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_consumption_month_report, container, false);
        mContext = getContext();

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

                for (int i = 0; i < 5; i++) {
                    int tempCurMonth = curMonth - i;
                    int tempCurYear = curYear;

                    if (tempCurMonth < 1) {
                        tempCurMonth += 12;
                        tempCurYear--;
                    }

                    // 한 자리 수의 숫자에 0을 붙여주기 위한 변수 (9 -> 09)
                    String tempCurMonthStr = "";

                    if (String.valueOf(tempCurMonth).length() == 1) {
                        tempCurMonthStr = "0" + (tempCurMonth);
                    } else {
                        tempCurMonthStr = String.valueOf(tempCurMonth);
                    }

                    labels[i] = tempCurYear + "-" + tempCurMonthStr;
                    requestDates += tempCurYear + "-" + tempCurMonthStr + "-" + "01";

                    if (i < 4)
                        requestDates += ",";

                }
                // 내림차순으로 되어있는 requestDates를 오름차순으로 변경
                String[] requestDatesArray = requestDates.split(",");

                for (int i = 0; i < requestDatesArray.length / 2; i++) {
                    String temp = requestDatesArray[i];
                    requestDatesArray[i] = requestDatesArray[requestDatesArray.length - 1 - i];
                    requestDatesArray[requestDatesArray.length - 1 - i] = temp;
                }
                requestDates = "";

                for (int i = 0; i < requestDatesArray.length; i++) {
                    requestDates += requestDatesArray[i];

                    if (i < requestDatesArray.length - 1)
                        requestDates += ",";
                }
                labels[0] = "";
                // 내림차순으로 되어있는 labels를 오름차순으로 변경
                for (int i = 1; i < labels.length / 2 + 1; i++) {
                    String temp = labels[i];
                    labels[i] = labels[labels.length - i];
                    labels[labels.length - i] = temp;
                }

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


                        for (int i = 0; i < Info.length; i++)
                            entries.add(new BarEntry(i + 1, Integer.parseInt(Info[i].getMonthSum())));

                        // 지난 세 달 간 지출 합
                        for (int i = 0; i < Info.length - 1; i++)
                            lastThreeMonthAvg += Integer.parseInt(Info[i].getMonthSum());

                        lastThreeMonthAvg /= 3;

                        // differenceFromLastThreeMonth 값이 양수이면 많이 지출, 음수이면 적게 지출
                        differenceFromLastThreeMonth = Integer.parseInt(Info[Info.length - 1].getMonthSum()) - lastThreeMonthAvg;

                        if (differenceFromLastThreeMonth >= 0) {
                            monthChartDetail.append(Html.fromHtml("<u>" + (lastThreeMonthAvg) + "</u>"));
                            monthChartDetail.append("원 입니다\n\n이 달은 지난 세 달간 평균 보다 ");
                            monthChartDetail.append(Html.fromHtml("<u>" + differenceFromLastThreeMonth + "</u>"));
                            monthChartDetail.append("원 더 사용하셨네요");
                        } else {
                            monthChartDetail.append(Html.fromHtml("<u>" + (lastThreeMonthAvg) + "</u>"));
                            monthChartDetail.append("원 입니다\n\n이 달은 지난 세 달간 평균 보다 ");
                            monthChartDetail.append(Html.fromHtml("<u>" + Math.abs(differenceFromLastThreeMonth) + "</u>"));
                            monthChartDetail.append("원 덜 사용하셨네요");
                        }
                        YAxis leftAxis = monthChart.getAxisLeft();
                        YAxis rightAxis = monthChart.getAxisRight();
                        XAxis xAxis = monthChart.getXAxis();

                        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                        xAxis.setTextSize(11f);
                        xAxis.setTextColor(Color.rgb(155, 155, 155));

                        xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));
                        xAxis.setLabelCount(4);
                        xAxis.setDrawAxisLine(true);
                        xAxis.setDrawGridLines(false);

                        leftAxis.setDrawLabels(true);
                        leftAxis.setAxisMinimum(Integer.parseInt(Info[0].getMonthSum()) / 2);
                        //leftAxis.setGranularity();
                        leftAxis.setTextColor(Color.rgb(155, 155, 155));
                        leftAxis.setDrawAxisLine(true);
                        leftAxis.setDrawGridLines(false);

                        rightAxis.setDrawAxisLine(false);
                        rightAxis.setDrawGridLines(false);
                        rightAxis.setDrawLabels(false);

                        BarDataSet set = new BarDataSet(entries, "");

                        set.setValueTextColor(Color.rgb(90, 90, 90));
                        set.setValueTextSize(9);
                        set.setColors(new int[]{normColor, normColor, normColor, minColor});

                        BarData data = new BarData(set);

                        data.setBarWidth(0.5f);

                        setChartData(monthChart, data);

                    }
                });
                // 계산의 편의를 위해 증가 시켜주었던 curMonth값을 다시 감소
                curMonth--;
                String startDate = "";
                String endDate = "";
                if(curMonth < 10) {
                    startDate = curYear + "-0" + curMonth + "-" + "01";
                    endDate = curYear + "-0" + curMonth + "-" + "31";
                }
                else {
                    startDate = curYear + "-" + curMonth + "-" + "01";
                    endDate = curYear + "-" + curMonth + "-" + "31";
                }

            Log.d(">>>curMonth", String.valueOf(curMonth));
            Log.d(">>>startDate", startDate);
            Log.d(">>>endDate", endDate);
            Log.d(">>>UserId", userID);
            Log.d(">>>Context", String.valueOf(mContext));


                HistoryRequest testRequest = new HistoryRequest(
                        userID,
                        startDate,
                        endDate,
                        RequestInfo.RequestType.ACCOUNT_HISTORY,
                        mContext);

                testRequest.Request(new HistoryRequest.VolleyCallback() {
                    @Override
                    public void onSuccess(HistoryInfo[] historyInfo, DailyHistoryInfo[] dailyHistoryInfo) {

                        ///////////////////////////////////////
                        // start of mostVisitedPlace setting
                        ArrayList<HistoryInfo> spendCardList = new ArrayList<>();
                        // add data in 'spendCardList' which htype is 카드
                        for(int i=0; i<historyInfo.length; i++) {
                            if(historyInfo[i].gethType().equals("카드"))
                                spendCardList.add(historyInfo[i]);
                        }
                        // Sorting ascending order 'spendCardList', by 'hname'
                        Collections.sort(spendCardList, new Comparator<HistoryInfo>() {
                            @Override
                            public int compare(HistoryInfo o1, HistoryInfo o2) {
                                return o1.gethName().compareTo(o2.gethName());
                            }
                        });


                        ArrayList<MonthReportData> mostVisitedPlaceList = new ArrayList<>();
                        int cnt = 1;
                        int tempSpend = Integer.parseInt(spendCardList.get(0).gethValue());
                        for(int i=0; i<spendCardList.size(); ++i) {

                            if (i < spendCardList.size() - 1) {
                                if (spendCardList.get(i).gethName().equals(spendCardList.get(i + 1).gethName())) {
                                    tempSpend += Integer.parseInt(spendCardList.get(i+1).gethValue());
                                    cnt++;
                                }
                                else {
                                    mostVisitedPlaceList.add(new MonthReportData(spendCardList.get(i).gethName(), cnt, tempSpend));
                                    tempSpend = Integer.parseInt(spendCardList.get(i+1).gethValue());
                                    cnt = 1;
                                }
                            }
                            // if end of list, just add
                            else {
                                     mostVisitedPlaceList.add(new MonthReportData(spendCardList.get(i).gethName(), cnt, tempSpend));
                            }
                        }

                        // Sorting descending order 'mostVisitedPlaceList', by 'count'
                        Collections.sort(mostVisitedPlaceList, new Comparator<MonthReportData>() {
                            @Override
                            public int compare(MonthReportData o1, MonthReportData o2) {
                                return o2.getCount()-o1.getCount();
                            }
                        });
                        MonthReportData mostVisitedPlaceThisMonth = mostVisitedPlaceList.get(0);

                        mostVisitedPlace = view.findViewById(R.id.mostVisitedPlace);
                        mostVisitedPlaceDetail = view.findViewById(R.id.mostVisitedPlaceDetail);
                        mostVisitedPlace.setText("이 달은 " + mostVisitedPlaceThisMonth.getPlace() + " 을/를 가장 많이 방문 했습니다 ");
                        mostVisitedPlaceDetail.setText("총 " + mostVisitedPlaceThisMonth.getCount() + "번,  평균 " + mostVisitedPlaceThisMonth.getPrice() / mostVisitedPlaceThisMonth.getCount() + "원을 사용했습니다!");
                        // end of most visited place setting
                        ///////////////////////////////////////

                        ///////////////////////////////////////
                        // start of most spent place setting
                        Collections.sort(mostVisitedPlaceList, new Comparator<MonthReportData>() {
                            @Override
                            public int compare(MonthReportData o1, MonthReportData o2) {
                                return o2.getPrice()-o1.getPrice();
                            }
                        });
                        MonthReportData mostSpentPlaceThisMonth = mostVisitedPlaceList.get(0);
                        mostSpentPlace = view.findViewById(R.id.mostSpentPlace);
                        mostSpentPlaceDetail = view.findViewById(R.id.mostSpentPlaceDetail);
                        mostSpentPlace.setText(mostSpentPlaceThisMonth.getPlace() + " 에서 가장 많이 지출했습니다");
                        mostSpentPlaceDetail.setText("총 " + mostSpentPlaceThisMonth.getPrice() + "원 을 " + mostSpentPlaceThisMonth.getPlace() + " 에서 사용했습니다!");
                        ///////////////////////////////////////
                        // end of most spent place setting

                        ///////////////////////////////////////
                        // start of usage by card setting
                        Collections.sort(spendCardList, new Comparator<HistoryInfo>() {
                            @Override
                            public int compare(HistoryInfo o1, HistoryInfo o2) {
                                return o1.getcType().compareTo(o2.getcType());
                            }
                        });
                        ArrayList<MonthReportData> usageByCardList = new ArrayList<>();
                        int tempCnt = 1;
                        int tempCardSpend = Integer.parseInt(spendCardList.get(0).gethValue());
                        for(int i=0; i<spendCardList.size(); ++i) {

                            if (i < spendCardList.size() - 1) {
                                if (spendCardList.get(i).getcType().equals(spendCardList.get(i + 1).getcType())) {
                                    tempCardSpend += Integer.parseInt(spendCardList.get(i+1).gethValue());
                                    tempCnt++;
                                }
                                else {
                                    usageByCardList.add(new MonthReportData(spendCardList.get(i).getcType(), tempCnt, tempCardSpend));
                                    tempCardSpend = Integer.parseInt(spendCardList.get(i+1).gethValue());
                                    tempCnt = 1;
                                }
                            }
                            // if end of list, just add
                            else {
                                usageByCardList.add(new MonthReportData(spendCardList.get(i).getcType(), tempCnt, tempCardSpend));
                            }
                        }
                        Log.d(">>>size", String.valueOf(usageByCardList.size()));
                        usageByCardListView = view.findViewById(R.id.cardSpendList);

                        Collections.sort(usageByCardList, new Comparator<MonthReportData>() {
                            @Override
                            public int compare(MonthReportData o1, MonthReportData o2) {
                                return o2.getPrice()-o1.getPrice();
                            }
                        });



                        UsageByCardListViewAdapter usageByCardListViewAdapter = new UsageByCardListViewAdapter(getContext(), usageByCardList);
                        usageByCardListView.setAdapter(usageByCardListViewAdapter);

                        ListAdapter listAdapter = usageByCardListView.getAdapter();
                        int totalHeight = 0;
                        int desiredWidth = View.MeasureSpec.makeMeasureSpec(usageByCardListView.getWidth(), View.MeasureSpec.AT_MOST);

                        for(int i=0; i< listAdapter.getCount(); i++) {
                            View listItem = listAdapter.getView(i, null, usageByCardListView);
                            listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
                            totalHeight += listItem.getMeasuredHeight();
                        }
                        // adjust listview's height dynamically
                        ViewGroup.LayoutParams params = usageByCardListView.getLayoutParams();
                        params.height = totalHeight + (usageByCardListView.getDividerHeight() * (listAdapter.getCount() - 1));
                        usageByCardListView.setLayoutParams(params);
                        usageByCardListView.requestLayout();
                        // end of usage by card setting
                        ///////////////////////////////////////
                        usageByCardListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                ArrayList<MonthReportData> tempList  = new ArrayList<>();
                                String curItem = usageByCardList.get(position).getPlace(); // 클릭하는 item의 카드이름

                                for(HistoryInfo h : spendCardList) {
                                    if(h.getcType().equals(curItem))
                                        tempList.add(new MonthReportData(h.gethDate(), h.gethName(), Integer.parseInt(h.gethValue())));
                                }

                                Intent intent1 = new Intent(getActivity(), UsageByCardDetail.class);
                                intent1.putExtra("cardName", curItem);
                                intent1.putExtra("DetailList", tempList);
                                startActivity(intent1);
                            }
                        });
                    }
                });
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


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        monthReportPreviousBtn = getActivity().findViewById(R.id.monthReportPreviousBtn);

        //뒤로가기 버튼 누르면 프래그먼트 안보이게 하기
        monthReportPreviousBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getView().setVisibility(View.GONE);
            }
        });
        super.onActivityCreated(savedInstanceState);
    }
}
