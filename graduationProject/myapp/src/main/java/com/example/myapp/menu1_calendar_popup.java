package com.example.myapp;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Window;
import android.content.Intent;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class menu1_calendar_popup extends Activity {
    public menu1_rvData[] indexData;
    List<menu1_rvData> menu1rvDataList;
    RecyclerView recyclerView;
    menu1_RecyclerAdapter rv_adapter;

    TextView use_money;
    TextView earn_money;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.calendar_popup);
        recyclerView = (RecyclerView)findViewById(R.id.recycler_view_popup);
        use_money = (TextView)findViewById(R.id.useMoney);
        earn_money = (TextView)findViewById(R.id.earnMoney);
        Intent intent = getIntent();
        String[] hDate = intent.getStringArrayExtra("hDate");
        String[] hName = intent.getStringArrayExtra("hName");
        String[] hType =intent.getStringArrayExtra("hType");
        String[] hValue = intent.getStringArrayExtra("hValue");
        String[] aBalance = intent.getStringArrayExtra("aBalance");
        String[] cType = intent.getStringArrayExtra("cType");
        String[] cName = intent.getStringArrayExtra("cName");

        String benefit = intent.getStringExtra("benefit");
        String loss = intent.getStringExtra("loss");
        int startNum = intent.getIntExtra("startNum", 0);
        int endNum = intent.getIntExtra("endNum", 0);

        DecimalFormat myFormat = new DecimalFormat("###,###");
        use_money.setText(myFormat.format(loss));
        earn_money.setText(myFormat.format(benefit));

        Log.i("err", Integer.toString(startNum)+" "+Integer.toString(endNum));
        indexData = new menu1_rvData[endNum-startNum];
        int count =0;
        for(int i=startNum; i<endNum; i++){
            indexData[count++]= new menu1_rvData(Integer.parseInt(hDate[i].substring(0,4)),  //연도
                    Integer.parseInt(hDate[i].substring(5,7)),  //월
                    Integer.parseInt(hDate[i].substring(8,10)), //일
                    hDate[i].substring(11,13),  //시
                    hDate[i].substring(14,16),  //분
                    hName[i],   //사용 처
                    cType[i],   //카드 이름
                    Integer.parseInt(hValue[i]), //금액
                    hType[i],    //내역 타입
                    cName[i],    //카테고리 분류
                    Integer.parseInt(aBalance[i])
            );
        }

        menu1rvDataList = new ArrayList<menu1_rvData>();
        for (int i = indexData.length-1; i >= 0; i--) {
            menu1rvDataList.add(indexData[i]);
        }
        //리사이클러뷰 어댑터 등록 및 설정
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        rv_adapter =new menu1_RecyclerAdapter(menu1rvDataList,R.layout.menu1_recycler_item);
//        rv_adapter.setOnClickListener(menu1_calendar_popup.this);    //클릭리스너 연결
        recyclerView.setAdapter(rv_adapter);


    }
}
