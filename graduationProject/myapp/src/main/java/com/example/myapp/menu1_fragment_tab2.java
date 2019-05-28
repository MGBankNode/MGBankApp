package com.example.myapp;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.security.PublicKey;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class menu1_fragment_tab2 extends Fragment implements menu1_RecyclerAdapter.RecyclerViewClickListener{
    List<menu1_rvData> menu1rvDataList;
    RecyclerView recyclerView;
    menu1_RecyclerAdapter rv_adapter;

    public menu1_rvData[] indexData;

    int month = 3;
    int year = 2019;
    ImageButton btn_previous;
    ImageButton btn_next;
    ImageButton btn_receipt;
    TextView txt_present;
    TextView txt_year;

    public String userID;
    public String accountNum;
    public menu1_fragment_tab2(){    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        RelativeLayout layout = (RelativeLayout)inflater.inflate(R.layout.menu1_fragment_tab2,
                container, false);
        if(getArguments() != null){
            userID = getArguments().getString("ID");
            try {
                accountNum = getArguments().getString("accountNum");
            } catch(Exception e) {
                accountNum = null;
            }
            Log.i("nkw","menu1_tab2_userID="+userID + "/accountNum="+accountNum);

         //   Toast.makeText(getContext(), "accountNum="+accountNum,Toast.LENGTH_LONG).show();
        }
        return layout;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        recyclerView = (RecyclerView)getView().findViewById(R.id.recycler_view);
        btn_previous=(ImageButton)getView().findViewById(R.id.previous_month);
        btn_receipt = (ImageButton)getView().findViewById(R.id.receipt2_btn);
        btn_next=(ImageButton)getView().findViewById(R.id.next_month);
        txt_present=(TextView)getView().findViewById(R.id.present_month);
        txt_year=(TextView)getView().findViewById(R.id.present_year);

        //오늘 연도, 월
        final Date date = new Date(System.currentTimeMillis());
        final SimpleDateFormat curYearFormat = new SimpleDateFormat("yyyy", Locale.KOREA);
        final SimpleDateFormat curMonthFormat = new SimpleDateFormat("MM", Locale.KOREA);
        year =Integer.parseInt(curYearFormat.format(date));
        month = Integer.parseInt(curMonthFormat.format(date));
        txt_present.setText(month + "월");

        // 소비내역 리스트 데이터를 추가
        request_test();


        //월 이동 버튼 누를 때
        btn_previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { //last month
                if (month == 1) {
                    month = 12;
                    year -= 1;
                } else {
                    month -= 1;
                }
                txt_present.setText(month + "월");
                txt_year.setText(year + "년");

                request_test();

            }
        });
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { //next month
                if((year==Integer.parseInt(curYearFormat.format(date)))&&
                        (month == Integer.parseInt(curMonthFormat.format(date)))){
                    year=Integer.parseInt(curYearFormat.format(date));
                    month=Integer.parseInt(curMonthFormat.format(date));
                }else if (month == 12) {
                    month = 1;
                    year += 1;
                } else {
                    month += 1;
                }
                txt_present.setText(month + "월");
                txt_year.setText(year + "년");

                request_test();
            }
        });

        btn_receipt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                if(android.os.Build.VERSION.SDK_INT >= 26) {
                    intent = new Intent(getActivity(), AddReceiptActivity.class);
                    intent.putExtra("userID", userID);
                    intent.putExtra("receiptFlag", "T");
                    startActivityForResult(intent, 1);
                }

                else {
                    intent = new Intent(getActivity(), AddReceiptActivity.class);
                    intent.putExtra("userID", userID);
                    intent.putExtra("receiptFlag", "F");
                    Toast.makeText(getContext(), "해당 기능을 사용할 수 없는 기기 입니다.\n수기로 입력해주세요", Toast.LENGTH_SHORT).show();
                    startActivityForResult(intent, 1);
                }

            }
        });
        super.onActivityCreated(savedInstanceState);
    }
    public void request_test(){
        /////////////////////////////////
        //요청 정보 입력
        int request_year=year, request_month=month+1;
        if((request_month)==13){
            request_year=year+1;
            request_month=1;
        }
        if(accountNum == null) {

            HistoryRequest test = new HistoryRequest(
                    userID,                          //현재 로그인 아이디
                    year + "-" + month + "-1",                       //요청할 해당 달의 시작 날짜
                    request_year + "-" + request_month + "-1",       //요청할 해당 다음달의 시작 날짜
                    RequestInfo.RequestType.ACCOUNT_HISTORY,   //내역 요청 할때 고정으로 쓰시면되여
                    getContext());                             //이것두 고정이요


            //Request 함수 호출해서 정보 accountHistoryInfo 객체와 dailyHistoryInfo 객체에서 받아와서 사용
            test.Request((HistoryInfo[] accountHistoryInfo, DailyHistoryInfo[] dailyHistoryInfo) -> {
                int arrLength = accountHistoryInfo.length;

                String[] hDate = new String[arrLength];
                String[] hType = new String[arrLength];
                String[] hValue = new String[arrLength];
                String[] hName = new String[arrLength];
                String[] aBalance = new String[arrLength];
                String[] cType = new String[arrLength];
                String[] cName = new String[arrLength];
                String[] aType = new String[arrLength];

                for (int i = 0; i < arrLength; i++) {

                    hDate[i] = accountHistoryInfo[i].gethDate();        //내역 사용 날짜
                    hType[i] = accountHistoryInfo[i].gethType();        //내역 사용 타입 => 입금 / 출금 / 카드
                    hValue[i] = accountHistoryInfo[i].gethValue();      //내역 사용 금액
                    hName[i] = accountHistoryInfo[i].gethName();        //내역 사용 처 이름
                    aBalance[i] = accountHistoryInfo[i].getaBalance();  //내역 사용 후 잔액
                    cType[i] = accountHistoryInfo[i].getcType();        //카드 이름
                    cName[i] = accountHistoryInfo[i].getcName();        //카테고릐 분류
                    aType[i] = accountHistoryInfo[i].getaType();        // 계좌 이름
                }

                //위에 처럼 각각 AccountHistoryInfo 에는 각각 정보들 get으로 얻어서 사용하시면 되요

                int arrLength2 = dailyHistoryInfo.length;
                String day[] = new String[arrLength2];
                String dailyBenefit[] = new String[arrLength2];
                String dailyLoss[] = new String[arrLength2];

                for (int i = 0; i < arrLength2; i++) {

                    day[i] = dailyHistoryInfo[i].getDay();                      //일
                    dailyBenefit[i] = dailyHistoryInfo[i].getDailyBenefit();    //수익
                    dailyLoss[i] = dailyHistoryInfo[i].getDailyLoss();          //지출

                }

                indexData = new menu1_rvData[arrLength];

                for (int i = 0; i < arrLength; i++) {
                    indexData[i] = new menu1_rvData(Integer.parseInt(hDate[i].substring(0, 4)),  //연도
                            Integer.parseInt(hDate[i].substring(5, 7)),  //월
                            Integer.parseInt(hDate[i].substring(8, 10)), //일
                            hDate[i].substring(11, 13),  //시
                            hDate[i].substring(14, 16),  //분
                            hName[i],   //사용 처
                            cType[i],   //카드 이름
                            Integer.parseInt(hValue[i]), //금액
                            hType[i],    //내역 타입
                            cName[i],    //카테고리 분류
                            Integer.parseInt(aBalance[i]),
                            aType[i]
                    );
                }

                // 리사이클러뷰에 담을 데이터 저장
                menu1rvDataList = new ArrayList<menu1_rvData>();
                for (int i = indexData.length - 1; i >= 0; i--) {
                    menu1rvDataList.add(indexData[i]);
                }
                //리사이클러뷰 어댑터 등록 및 설정
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new LinearLayoutManager(getView().getContext()));
                recyclerView.setItemAnimator(new DefaultItemAnimator());

                rv_adapter = new menu1_RecyclerAdapter(menu1rvDataList, R.layout.menu1_recycler_item);
                rv_adapter.setOnClickListener(menu1_fragment_tab2.this);    //클릭리스너 연결
                recyclerView.setAdapter(rv_adapter);


            });
            ///////////////////////////////////////////////
        } else {
            Log.i("CHJ", "menu1_request : "+accountNum);
            HistoryRequest test = new HistoryRequest(
                    accountNum,                          // 선택한 계좌
                    year + "-" + month + "-1",                       //요청할 해당 달의 시작 날짜
                    request_year + "-" + request_month + "-1",       //요청할 해당 다음달의 시작 날짜
                    RequestInfo.RequestType.ACCOUNT_BY_HISTORY,   //내역 요청 할때 고정으로 쓰시면되여
                    getContext());                             //이것두 고정이요


            //Request 함수 호출해서 정보 accountHistoryInfo 객체와 dailyHistoryInfo 객체에서 받아와서 사용
            test.AccountByRequest((HistoryInfo[] accountHistoryInfo, DailyHistoryInfo[] dailyHistoryInfo) -> {
                int arrLength = accountHistoryInfo.length;

                String[] hDate = new String[arrLength];
                String[] hType = new String[arrLength];
                String[] hValue = new String[arrLength];
                String[] hName = new String[arrLength];
                String[] aBalance = new String[arrLength];
                String[] cType = new String[arrLength];
                String[] cName = new String[arrLength];
                String[] aType = new String[arrLength];

                for (int i = 0; i < arrLength; i++) {

                    hDate[i] = accountHistoryInfo[i].gethDate();        //내역 사용 날짜
                    hType[i] = accountHistoryInfo[i].gethType();        //내역 사용 타입 => 입금 / 출금 / 카드
                    hValue[i] = accountHistoryInfo[i].gethValue();      //내역 사용 금액
                    hName[i] = accountHistoryInfo[i].gethName();        //내역 사용 처 이름
                    aBalance[i] = accountHistoryInfo[i].getaBalance();  //내역 사용 후 잔액
                    cType[i] = accountHistoryInfo[i].getcType();        //카드 이름
                    cName[i] = accountHistoryInfo[i].getcName();        //카테고릐 분류
                    aType[i] = accountHistoryInfo[i].getaType();        // 계좌 이름
                }

                //위에 처럼 각각 AccountHistoryInfo 에는 각각 정보들 get으로 얻어서 사용하시면 되요

                int arrLength2 = dailyHistoryInfo.length;
                String day[] = new String[arrLength2];
                String dailyBenefit[] = new String[arrLength2];
                String dailyLoss[] = new String[arrLength2];

                for (int i = 0; i < arrLength2; i++) {

                    day[i] = dailyHistoryInfo[i].getDay();                      //일
                    dailyBenefit[i] = dailyHistoryInfo[i].getDailyBenefit();    //수익
                    dailyLoss[i] = dailyHistoryInfo[i].getDailyLoss();          //지출

                }

                indexData = new menu1_rvData[arrLength];

                for (int i = 0; i < arrLength; i++) {
                    indexData[i] = new menu1_rvData(Integer.parseInt(hDate[i].substring(0, 4)),  //연도
                            Integer.parseInt(hDate[i].substring(5, 7)),  //월
                            Integer.parseInt(hDate[i].substring(8, 10)), //일
                            hDate[i].substring(11, 13),  //시
                            hDate[i].substring(14, 16),  //분
                            hName[i],   //사용 처
                            cType[i],   //카드 이름
                            Integer.parseInt(hValue[i]), //금액
                            hType[i],    //내역 타입
                            cName[i],    //카테고리 분류
                            Integer.parseInt(aBalance[i]),
                            aType[i]
                    );
                }

                // 리사이클러뷰에 담을 데이터 저장
                menu1rvDataList = new ArrayList<menu1_rvData>();
                for (int i = indexData.length - 1; i >= 0; i--) {
                    menu1rvDataList.add(indexData[i]);
                }
                //리사이클러뷰 어댑터 등록 및 설정
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new LinearLayoutManager(getView().getContext()));
                recyclerView.setItemAnimator(new DefaultItemAnimator());

                rv_adapter = new menu1_RecyclerAdapter(menu1rvDataList, R.layout.menu1_recycler_item);
                rv_adapter.setOnClickListener(menu1_fragment_tab2.this);    //클릭리스너 연결
                recyclerView.setAdapter(rv_adapter);

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            });
        }
    }
    @Override
    public void onItemClicked(int position) {
       // Toast.makeText(getContext(),position+"번 아이템 클릭",Toast.LENGTH_LONG).show();
        //커스텀 다이얼로그 생성
        menu1_CustomDialog customDialog = new menu1_CustomDialog(getActivity());
        customDialog.callFunction(indexData.length-1-position,year,month,userID);

        customDialog.setDialogResult(new menu1_CustomDialog.OnMyDialogResult() {
            @Override
            public void finish(String result) {
                request_test();
            }
        });
    }

}
