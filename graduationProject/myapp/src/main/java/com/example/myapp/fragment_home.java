package com.example.myapp;


import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;
//그래프
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

/**
 * A simple {@link Fragment} subclass.
 */
public class fragment_home extends Fragment {
    ArrayList<Stat> sData = null;
    RecyclerView recyclerView = null;
    RecyclerViewAdapter adapter = null;
    LinearLayoutManager layoutManager = null;
    private PieChart pieChart = null;
    public fragment_home() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        sData = new ArrayList<Stat>();
        sData.add(new Stat("쇼핑/의류", 113200));
        sData.add(new Stat("쇼핑/가전", 14500));
        sData.add(new Stat("생활/문화", 62800));
        sData.add(new Stat("교통", 15420));
        sData.add(new Stat("식비", 414000));
        sData.add(new Stat("미지정", 105000));
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        //리사이클러뷰 셋팅
        setRecyclerView();
        //그래프 셋팅
        setPieChart();
        //잔고 콤마 셋팅
        setHomeText();
        super.onActivityCreated(savedInstanceState);
    }
    //리사이클러뷰 셋팅
    public void setRecyclerView(){
        recyclerView = (RecyclerView)getView().findViewById(R.id.recyclerView01);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getView().getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setNestedScrollingEnabled(false);

        adapter = new RecyclerViewAdapter(sData);
        recyclerView.setAdapter(adapter);
    }
    //그래프 그리기
    public void setPieChart(){
        pieChart = (PieChart)getView().findViewById(R.id.piechart);

        pieChart.setUsePercentValues(true);
        pieChart.getDescription().setEnabled(false);
        pieChart.setExtraOffsets(5,10,5,5);
        pieChart.setDragDecelerationFrictionCoef(0.95f);
        pieChart.setDrawHoleEnabled(false);
        pieChart.setHoleColor(Color.WHITE);
        pieChart.setEntryLabelColor(Color.BLACK);
        pieChart.setEntryLabelTextSize(14);
        pieChart.setTransparentCircleRadius(61f);

        ArrayList<PieEntry> yValues = new ArrayList<PieEntry>();

        //원형 차트의 구성값
        for(int i = 0; i < sData.size(); i++){
            yValues.add(new PieEntry(sData.get(i).getPrice(), sData.get(i).getName()));
        }
        //애니메이션 추가
        pieChart.animateY(1000, Easing.EaseInOutCubic); //애니메이션

        PieDataSet dataSet = new PieDataSet(yValues,"");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);
        //텍스트를 바깥으로
        dataSet.setValueLinePart1Length(0.8f);
        dataSet.setValueLinePart2Length(.2f);
        dataSet.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);

        //색 지정
        Random rand = new Random();
        ArrayList<Integer> colors = new ArrayList<Integer>();
        for(int i = 0; i < sData.size(); i++){

            colors.add(Color.rgb(rand.nextInt(43 - 0 + 1), rand.nextInt(80) + 160, rand.nextInt(80) + 160));
        }
        dataSet.setColors(colors);

        PieData data = new PieData((dataSet));
        data.setValueTextSize(14);
        data.setValueTextColor(Color.WHITE);

        Legend l  = pieChart.getLegend();
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setTextSize(13);
        l.setEnabled(false);
        pieChart.setData(data);
    }
    //잔고 텍스트 콤마
    public void setHomeText(){
        Util util = new Util();
        TextView tv = (TextView)getView().findViewById(R.id.mainFragment_textView);
        tv.setText(util.comma(123456789) + "원");
    }
}