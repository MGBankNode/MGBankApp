package com.example.myapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class UsageByCardDetail extends AppCompatActivity {

    ArrayList<MonthReportData> receiveList;
    ListView usageByCardDetailList;

    TextView mainCardName;

    String cardName;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.usagebycarddetail);

        receiveList = (ArrayList<MonthReportData>) getIntent().getSerializableExtra("DetailList");
        cardName = getIntent().getStringExtra("cardName");
        mainCardName = findViewById(R.id.mainCardName);
        mainCardName.setText(cardName);

        Collections.sort(receiveList, new Comparator<MonthReportData>() {
            @Override
            public int compare(MonthReportData o1, MonthReportData o2) {
                return o1.getDate().compareTo(o2.getDate());
            }
        });

        usageByCardDetailList = findViewById(R.id.usageByCardDetailList);
        UsageByCardDetailListViewAdapter usageByCardListViewAdapter = new UsageByCardDetailListViewAdapter(getApplicationContext(), receiveList);
        usageByCardDetailList.setAdapter(usageByCardListViewAdapter);
    }
}
