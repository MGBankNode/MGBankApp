package com.example.myapp;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class consumptionEvaluationFragment extends Fragment {

    int LastDay;
    int month;

    View view;

    DateActivity Today;

    ImageButton previous_month;
    ImageButton next_month;

    String dates = "";
    TextView mainMonth;
    TextView mainYear;
    Fragment fr;
    AnalysisInfo[] analysisWeekInfo;

    ArrayList<DateActivity> sunDayList = new ArrayList<>();
    ArrayList<DateActivity> tempList = new ArrayList<>();
    DateActivity[][] dateArray;

    Context context;

    DateActivity mDateActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_consumption_evaluation, container, false);
        context = getContext();

        mainYear = view.findViewById(R.id.present_year);
        mainMonth = view.findViewById(R.id.present_month);

        Calendar cal = Calendar.getInstance();

        int year = cal.get(Calendar.YEAR);
        month = cal.get(Calendar.MONTH) + 1;
        final int date = cal.get(Calendar.DATE);
        int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);

        final String mainMonthStr = String.valueOf(month) + "월";
        mainMonth.setText(mainMonthStr);

        Today =  new DateActivity(year,month, date, dayOfWeek);
        // 제일 가까운 일요일
        LastDay = Today.getDay() - Today.getDayOfWeek() + 1;

        mDateActivity = new DateActivity(Today.getYear(), Today.getMonth(), LastDay);
        sunDayList.add(mDateActivity);

        dateArray = new DateActivity[12][7];

        //int tempMonth = Today.getMonth();

        if(LastDay <= 0) {
            // 안내문구
            // return
        }
        else {

            while(true) {

                mDateActivity = mDateActivity.getLastSunday();
                sunDayList.add(mDateActivity);

                if(month != mDateActivity.getMonth())
                    break;
            }
        }
        for(int i=0; i<sunDayList.size(); i++) {
            dateArray[month][i] = sunDayList.get(i);

        }


        previous_month = view.findViewById(R.id.previous_month);
        previous_month.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDateActivity = mDateActivity.recoverDate();

                sunDayList.clear();
                //mDateActivity = new DateActivity(Today.getYear(), Today.getMonth(), LastDay);
                //mDateActivity = new DateActivity(mDateActivity.getYear(), mDateActivity.getMonth(), mDateActivity.getDay());
                sunDayList.add(mDateActivity);

                int cnt = 0;
                int tempYear = mDateActivity.getYear();
                month--;

                while (true) {
                    int tempInt = mDateActivity.getMonth(); // 5월 5일 -> 4월 28 4 21 4 12 4 5
                    mDateActivity = mDateActivity.getLastSunday(); // 4월 28일 4 21 4 12 4 5 3.

                    sunDayList.add(mDateActivity);

                    if (tempInt != mDateActivity.getMonth())
                        cnt++;

                    if (cnt == 2)
                        break;
                }

                String tempMonthStr = String.valueOf(month) + "월";
                mainMonth.setText(tempMonthStr);

                String tempYearStr = String.valueOf(tempYear) + "년";
                mainYear.setText(tempYearStr);

                for(int i=0; i<sunDayList.size(); i++) {
                    dateArray[month][i] = sunDayList.get(i);
                }

                drawList(sunDayList, tempMonthStr);

                }
            });

        next_month = view.findViewById(R.id.report_next_month);
        next_month.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sunDayList.clear();
                month++;



                for (int i = 0; i < dateArray[month].length; i++) {
                    if(dateArray[month][i] == null)
                    sunDayList.add(dateArray[month][i]);
                }

                String tempMonthStr = String.valueOf(month) + "월";
                mainMonth.setText(tempMonthStr);

                drawList(sunDayList, tempMonthStr);
            }
        });


        drawList(sunDayList, mainMonthStr);

        return view;
    }

    public void drawList(ArrayList<DateActivity> List, String mmMonth) {
        final String Month = mmMonth;

        dates = makeFormat(List);

        String[] dateArray = dates.split(",");

        analysisWeekInfo = new AnalysisInfo[List.size() -1];

        for(int i = 0; i < List.size() - 1; ++i){
            analysisWeekInfo[i] = new AnalysisInfo(Integer.toString(i + 1), dateArray[i], dateArray[i + 1]);  //주차, 시작날자, 끝날짜 저장
        }

        //요청 정보 입력!!!!!!!test
        AnalysisRequest test = new AnalysisRequest(
                "b",                               //현재 로그인 아이디
                dates,            //날짜들 list
                RequestInfo.RequestType.ANALYSIS_WEEK,    //고정
                context);                                 //고정

        //Request 함수 호출해서 정보 accountHistoryInfo 객체와 dailyHistoryInfo 객체에서 받아와서 사용
        test.WeekRequestHandler(new AnalysisRequest.VolleyCallback() {
            @Override
            public void onSuccess(AnalysisInfo[] info) {
                int arrLength = analysisWeekInfo.length;

                ArrayList<reportElement> reportElements = new ArrayList<>();
                ListView reportListView = view.findViewById(R.id.reportList);

                for(int i = 0; i < arrLength; i++){

                    analysisWeekInfo[i].setWeekSum(info[i].getWeekSum());       //주별 총합 지출 추가 저장

                    reportElements.add(new reportElement("["+ Month + " " + analysisWeekInfo[i].getWeek()  + "주차 주간 리포트]",
                            Integer.parseInt(analysisWeekInfo[i].getWeekSum())));

                }
                reportListviewAdapter reportListviewAdapter = new reportListviewAdapter(context, R.layout.report_list_item, reportElements);
                reportListView.setAdapter(reportListviewAdapter);
            }
        });
    }

    public String makeFormat(ArrayList<DateActivity> List) {
        String result = "";

        String tempyear = "";
        String tempmonthStr = "";
        String tempdayStr = "";

        for(int i=List.size() -1; i>=0; i--) {
            DateActivity tempDate = List.get(i);
            tempyear = String.valueOf(tempDate.getYear());

            if(tempDate.getMonth() < 10) {
                tempmonthStr = "0" + String.valueOf(tempDate.getMonth());
            }
            else {
                tempmonthStr = String.valueOf(tempDate.getMonth());
            }

            if(tempDate.getDay() < 10) {
                tempdayStr = "0" + String.valueOf(tempDate.getDay());
            }
            else {
                tempdayStr = String.valueOf(tempDate.getDay());
            }

            result += tempyear + "-" + tempmonthStr + "-" + tempdayStr;
            if(i > 0) {
                result  += ",";
            }
        }
        return result;
    }
}
