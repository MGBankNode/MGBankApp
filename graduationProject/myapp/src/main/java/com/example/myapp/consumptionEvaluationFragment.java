package com.example.myapp;

import android.app.Activity;
import android.content.Context;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class consumptionEvaluationFragment extends Fragment {

    Fragment fr;
    AnalysisInfo[] analysisWeekInfo;

    private String getMonthDay(int year, int month, String startLastCheck){
        Calendar cal = Calendar.getInstance();
        cal.set(year, month,1);

        String day = "01";
        if(startLastCheck.equals("LAST")){

            day = String.valueOf(cal.getActualMaximum(Calendar.DAY_OF_MONTH));

        }

        return day;
    }

    private String getCalToString(Calendar cal){

        String dateString;

        String year = Integer.toString(cal.get(Calendar.YEAR));
        String month = Integer.toString(cal.get(Calendar.MONTH) + 1);
        String day = Integer.toString(cal.get(Calendar.DATE));

        if(month.length() == 1){

            month = "0" + month;

        }

        if(day.length() == 1){

            day = "0" + day;

        }

        dateString = year + "-" + month + "-" + day;

        return dateString;
    }

    public String getDates(int year, int month){

        Calendar cal = Calendar.getInstance();

        //달의 시작 날짜 + 요일
        String monthStartDay = getMonthDay(year, month, "START");
        cal.set(year, month, Integer.parseInt(monthStartDay));
        int monthStartWeek = cal.get(Calendar.DAY_OF_WEEK);


        //===> 달 시작 날짜로 real 시작 날짜 구하기
        int startGap = monthStartWeek - 1;
        cal.add(Calendar.DATE, -startGap);

        String startDate = getCalToString(cal);
        int sYear = cal.get(Calendar.YEAR);
        int sMonth = cal.get(Calendar.MONTH);
        int sDay = cal.get(Calendar.DATE);

        //달의 마지막 날짜 + 요일
        String monthLastDay = getMonthDay(year, month, "LAST");
        cal.set(year, month, Integer.parseInt(monthLastDay));
        int monthLastWeek = cal.get(Calendar.DAY_OF_WEEK);

        //===>  달 마지막 날짜로 real 마지막 날짜 구하기
        int LastGap = 8 - monthLastWeek;
        cal.add(Calendar.DATE, LastGap);

        String lastDate = getCalToString(cal);

        int tempYear = sYear;
        int tempMonth = sMonth;
        int tempDay = sDay;

        cal.set(tempYear, tempMonth, tempDay);
        String tempDates = startDate;
        cal.add(Calendar.DATE, 7);
        String tempString;
        tempString = getCalToString(cal);

        while(true){
            if(tempString.equals(lastDate)){
                tempDates = tempDates + "," + lastDate;
                break;
            }
            tempDates = tempDates + "," + tempString;

            tempYear = cal.get(Calendar.YEAR);
            tempMonth = cal.get(Calendar.MONTH);
            tempDay = cal.get(Calendar.DATE);

            cal.set(tempYear, tempMonth, tempDay);
            cal.add(Calendar.DATE, 7);
            tempString = getCalToString(cal);
        }

        return tempDates;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_consumption_evaluation, container, false);
        final Context context = getContext();

        //현재 날짜 받아오기
        Calendar cal = Calendar.getInstance();
        String todayDate = getCalToString(cal);
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
//        int day = cal.get(Calendar.DATE);

        Button detailBtn = view.findViewById(R.id.detailBtn);

        String dates = getDates(year, month); //"2019-04-28,2019-05-05,2019-05-12,2019-05-19,2019-05-26";        //모든 날짜들
        String[] date = dates.split(",");

        int weekCount = date.length - 1;
        analysisWeekInfo = new AnalysisInfo[weekCount];

        for(int i = 0; i < weekCount; ++i){



        }
        for(int i = 0; i < weekCount; ++i){

            analysisWeekInfo[i] = new AnalysisInfo(Integer.toString(i + 1), date[i], date[i + 1]);  //주차, 시작날자, 끝날짜 저장

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

                    reportElements.add(new reportElement("[5월 "+ analysisWeekInfo[i].getWeek()  + "주차 주간 리포트]",
                            Integer.parseInt(analysisWeekInfo[i].getWeekSum())));

                    reportElements.add(new reportElement());

                }

                reportListviewAdapter reportListviewAdapter = new reportListviewAdapter(context, R.layout.report_list_item, reportElements);
                reportListView.setAdapter(reportListviewAdapter);
            }
        });


        return view;
    }
}
