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

import java.util.ArrayList;

public class consumptionEvaluationFragment extends Fragment {

    Fragment fr;
    AnalysisInfo[] analysisWeekInfo;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_consumption_evaluation, container, false);
        final Context context = getContext();

        Button detailBtn = view.findViewById(R.id.detailBtn);

        int weekCount = 4;

        String dates = "2019-04-28,2019-05-05,2019-05-12,2019-05-19,2019-05-26";        //모든 날짜들
        String[] date = dates.split(",");

        analysisWeekInfo = new AnalysisInfo[weekCount];

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
