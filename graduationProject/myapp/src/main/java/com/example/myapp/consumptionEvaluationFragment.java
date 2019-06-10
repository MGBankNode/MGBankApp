package com.example.myapp;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class consumptionEvaluationFragment extends Fragment {

    LinearLayout ct;

    View view;
    Context context;

    Fragment fr;
    AnalysisInfo[] analysisWeekInfo;
    TextView mainMonthTv;
    TextView mainYearTv;

    ListView reportListView;

    ImageButton previousBtn;
    ImageButton nextBtn;

    String[] transferStr;

    public int curYear;
    public int curMonth;
    int mainMonth;

    private FragmentActivity myContext;
    public String userID;
    BroadcastReceiver receiver = null;

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
    public void onAttach(Activity activity) {
        myContext=(FragmentActivity) activity;
        super.onAttach(activity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_consumption_evaluation, container, false);
        context = getContext();

        ArrayList<String> lastThreeMonthDate = new ArrayList<>();

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("SEND_USERID");
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                userID = intent.getStringExtra("userID");
                previousBtn = view.findViewById(R.id.previous_month_);
                nextBtn = view.findViewById(R.id.next_month_);

                //현재 날짜 받아오기
                Calendar cal = Calendar.getInstance();

                curYear = cal.get(Calendar.YEAR);
                curMonth = cal.get(Calendar.MONTH);
                // 현재 날짜의 달
                mainMonth = curMonth;

                transferStr = drawList(curMonth, curYear);

                previousBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        curMonth--;
                        if(curMonth < 0) {
                            curMonth = 11;
                            curYear--;
                        }
                        transferStr =  drawList(curMonth, curYear);

                    }
                });

                nextBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if(curMonth == mainMonth)
                            return;

                        curMonth++;

                        if(curMonth >= 12) {
                            curMonth = 0;
                            curYear++;
                        }
                        transferStr = drawList(curMonth, curYear);

                    }

                });

                reportListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        // position 은 ~월 ~주차를 표기 하기 위해 필요한 변수
                        // transferStr은 월간리포트 리스트뷰를 그릴 때 사용 했던 날짜들의 배열
                        // 달력상 현재 월의 처음 월/일 부터 마지막 월/일에 대한 정보를 배열로 담고 있다
                        if(curMonth == mainMonth) {
                            changeFr(new consumptionReportFragment(), position, transferStr[position], transferStr[position + 1]);
                        }
                        // 월간 리포트가 존재하는 경우
                        else if(curMonth != mainMonth){

                            if(position == 0) {
                                // 월간 리포트 이벤트 삽입 부분
                                changeFr(new consumptionMonthReportFragment(), position, transferStr[position], transferStr[position + 1]);
                            }
                            else
                                changeFr(new consumptionReportFragment(), position-1, transferStr[position-1], transferStr[position]);
                        }
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

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public void changeFr(Fragment fr, int position, String tStr_1, String tStr_2) {
        Fragment detailFragment = fr;

        AnalysisInfo frData = new AnalysisInfo(String.valueOf(position+1), tStr_1, tStr_2);
        Bundle bundle = new Bundle();
        bundle.putString("userID", userID);
        bundle.putSerializable("dateData", frData);
        bundle.putString("LastDay", tStr_2);
        bundle.putInt("Year", curYear);
        bundle.putInt("Month", curMonth+1);
        bundle.putInt("Week", position+1);


        detailFragment.setArguments(bundle);

        FragmentManager fm = myContext.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentContainer_viewpager, detailFragment);
        fragmentTransaction.commit();

    }

    public String[] drawList(int month, int year) {


        reportListView = view.findViewById(R.id.reportList);

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");

        Date dte = new Date();
        // 현재 날짜
        String today = df.format(dte);


        String dates = getDates(year, month); //"2019-04-28,2019-05-05,2019-05-12,2019-05-19,2019-05-26";        //모든 날짜들
        String[] date = dates.split(",");

        //현재 날짜에서 크기 비교하기 위해 형태 바꿔줌
        today = today.replace("-", "");

        // 대소 비교 위해 정수형
        int tempToday = Integer.parseInt(today);

        String ridHyphenDates = dates.replace("-", "");
        String[] ridHyphenDatesArray = ridHyphenDates.split(",");


        int[] integerDateArray = new int[ridHyphenDatesArray.length];

        for(int i=0; i<ridHyphenDatesArray.length; i++)
            integerDateArray[i] = Integer.parseInt(ridHyphenDatesArray[i]);

        int arrayCnt = 0;

        for(int i=0; i<integerDateArray.length; i++) {
            if(tempToday < integerDateArray[i])
                break;

            arrayCnt++;
        }

        String[] tempdateArray = new String[arrayCnt];

        for(int i=0; i<arrayCnt; i++)
            tempdateArray[i] = date[i];

        date = tempdateArray; //  현재 날짜 까지만 짜름

        int weekCount = date.length - 1;
        analysisWeekInfo = new AnalysisInfo[weekCount];

        for(int i = 0; i < weekCount; ++i){
            analysisWeekInfo[i] = new AnalysisInfo(Integer.toString(i + 1), date[i], date[i + 1]);  //주차, 시작날자, 끝날짜 저장
        }
       // analysisWeekInfo[i] = new AnalysisInfo()

        //요청 정보 입력!!!!!!!test
        AnalysisRequest test = new AnalysisRequest(
                userID,                               //현재 로그인 아이디
                dates,            //날짜들 list
                RequestInfo.RequestType.ANALYSIS_WEEK,    //고정
                context);                                 //고정

        final String tempMonthStr = (month + 1) + "월";

        //Request 함수 호출해서 정보 accountHistoryInfo 객체와 dailyHistoryInfo 객체에서 받아와서 사용
        test.WeekRequestHandler(new AnalysisRequest.VolleyCallback() {
            @Override
            public void onSuccess(AnalysisInfo[] info) {
                int arrLength = analysisWeekInfo.length;

                ArrayList<reportElement> reportElements = new ArrayList<>();

                if(mainMonth != month)
                    reportElements.add(new reportElement("[" + tempMonthStr + " " + "월간 리포트]", true));

                for(int i = 0; i < arrLength; i++){
                    analysisWeekInfo[i].setWeekSum(info[i].getWeekSum());       //주별 총합 지출 추가 저장

                    reportElements.add(new reportElement("[" + tempMonthStr + " "+ analysisWeekInfo[i].getWeek()  + "주차 주간 리포트]",
                            Integer.parseInt(analysisWeekInfo[i].getWeekSum()), false));

                }
                reportListviewAdapter reportListviewAdapter = new reportListviewAdapter(context, reportElements);
                reportListView.setAdapter(reportListviewAdapter);
            }
        });

        mainMonthTv = view.findViewById(R.id.present_month_);
        mainMonthTv.setText(tempMonthStr);
        mainYearTv = view.findViewById(R.id.present_year_);
        String mainYearStr = String.valueOf(curYear) + "년";
        mainYearTv.setText(mainYearStr);

        return date;
    }




}