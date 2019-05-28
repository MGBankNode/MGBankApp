package com.example.myapp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class menu1_fragment_tab0 extends Fragment {

    ArrayList<String> account_number; //계좌번호
    String userID;
    protected ListView accountListView;
    protected TextView total_balance;
    public menu1_fragment_tab0() {    }

    @Nullable
    @Override //child fragment 구현
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.fragment_menu1_search,
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

            account_number = new ArrayList<>();
            ArrayList<AccountListData> listItem = new ArrayList<>();
            int total = 0;
            DecimalFormat decimalFormat = new DecimalFormat("###,###");
            for(int i = 0; i < accountNum; i++){

                int cost = Integer.parseInt(accountInfo[i].getaBalance());
                total+=cost;
                account_number.add(accountInfo[i].getaNum());
                listItem.add(new AccountListData(accountInfo[i].getaType(), accountInfo[i].getaNum(), decimalFormat.format(cost)+" 원"));
            }
            accountListView = layout.findViewById(R.id.accountList2);
            total_balance = (TextView)layout.findViewById(R.id.totalBalance);
            total_balance.setText(decimalFormat.format(total)+" 원");
            accountListViewAdapter accountListViewAdapter = new accountListViewAdapter(getContext(), listItem, R.layout. accountlistitem);
            accountListView.setAdapter(accountListViewAdapter);
            accountListView.setOnItemClickListener(itemClickListener);

        });

        return layout;
    }

    private AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Toast.makeText(getContext(),"position:"+position+"account_number:"+account_number.get(position),Toast.LENGTH_LONG).show();
           Fragment newFragment = new fragment_menu1();

            Bundle bundle = new Bundle();
            bundle.putInt("apage", 3);
            bundle.putString("ID", userID);
            Log.i("CHJ", "menu1_fragment_tab0 : "+userID);
            bundle.putString("accountNum", account_number.get(position));
            newFragment.setArguments(bundle);
            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.dynamic_mainFragment, newFragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }
    };

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
}