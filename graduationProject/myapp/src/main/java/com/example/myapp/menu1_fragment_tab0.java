package com.example.myapp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.ArrayList;

public class menu1_fragment_tab0 extends Fragment {

    String userID;
    protected ListView accountListView;

    public menu1_fragment_tab0() {    }

    @Nullable
    @Override //child fragment 구현
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.menu1_fragment_tab0,
                container, false);

        if(getArguments() != null){
            userID = getArguments().getString("ID");
            Log.i("nkw","menu1_tab0_userID="+userID);
        }


        //계좌 + 잔액 리스트 요청
        HistoryRequest test1 = new HistoryRequest(
                userID,                                 //현재 로그인 아이디
                RequestInfo.RequestType.BALANCE_LIST,   //계좌별 잔액 리스트 요청 할때 고정으로 쓰시면되여
                getContext());                          //이것두 고정이요


        //BalanceListRequest 함수 호출해서 정보 historyInfo
        test1.BalanceListRequest((HistoryInfo[] historyInfo, DailyHistoryInfo[] dailyHistoryInfo) -> {
            int accountNum = historyInfo.length;

            AccountInfo[] accountInfo = new AccountInfo[accountNum];

            for(int i = 0; i < accountNum; ++i){

                accountInfo[i] = historyInfo[i].getAccountInfo();

            }

            //계좌 번호= accountInfo[i].getaNum()
            //계좌 잔액 = accountInfo[i].getaBalance()
            //계좌 이름 = accountInfo[i].getaType()
          
            ArrayList<AccountListData> listItem = new ArrayList<>();

            for(int i = 0; i < accountNum; i++){
                listItem.add(new AccountListData(accountInfo[i].getaType(), accountInfo[i].getaNum(), accountInfo[i].getaBalance()));
            }
            accountListView = layout.findViewById(R.id.accountList);
            accountListViewAdapter accountListViewAdapter = new accountListViewAdapter(getContext(), listItem, R.layout. accountlistitem);
            accountListView.setAdapter(accountListViewAdapter);

        });

        //계좌 별 내역 리스트 요청
        HistoryRequest test2 = new HistoryRequest(
                userID,                                          //현재 로그인 아이디
                "9003-2438-0653-5",                      //계좌 번호
                RequestInfo.RequestType.ACCOUNT_BY_HISTORY,     //계좌별 내역 요청 할때 고정으로 쓰시면되여
                getContext());                                  //이것두 고정이요


        //BalanceListRequest 함수 호출해서 정보 historyInfo
        test2.AccountByRequest((HistoryInfo[] historyInfo, DailyHistoryInfo[] dailyHistoryInfo) -> {
            int historyCount = historyInfo.length;

            String[] hDate =  new String[historyCount];
            String[] hType =  new String[historyCount];
            String[] hValue =  new String[historyCount];
            String[] hName =  new String[historyCount];
            String[] aBalance =  new String[historyCount];
            String[] cType =  new String[historyCount];
            String[] cName =  new String[historyCount];

            for(int i = 0; i < historyCount; ++i){

                hDate[i] = historyInfo[i].gethDate();
                hType[i] = historyInfo[i].gethType();
                hValue[i] = historyInfo[i].gethValue();
                hName[i] = historyInfo[i].gethName();
                aBalance[i] = historyInfo[i].getaBalance();
                cType[i] = historyInfo[i].getcType();
                cName[i] = historyInfo[i].getcName();
            }

        });


        return layout;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
}
