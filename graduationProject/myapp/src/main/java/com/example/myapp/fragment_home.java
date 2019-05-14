package com.example.myapp;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
    protected Button setBudget;

    public fragment_home() {
        // Required empty public constructor
    }

    Toolbar toolbar;
    TextView textTitle;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //임시 데이터 셋팅
        View view = inflater.inflate(R.layout.fragment_home, container, false);


        sData = new ArrayList<Stat>();
        sData = (ArrayList<Stat>)getArguments().get("DATA");
        Log.d("homeFragment", sData.toString());


        View view = inflater.inflate(R.layout.fragment_home, container, false);;
        setBudget = view.findViewById(R.id.setBudgetBtn);

        String budget = "";

        // Inflate the layout for this fragment
        return view;
    }

//    public void startBudgetActivity() {
//        Intent intent = new Intent(getActivity(), SetBudget.class);
//        getActivity().startActivityForResult(intent, 2);
//    }



    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        //리사이클러뷰 셋팅
        setRecyclerView();
        //그래프 셋팅
        setPieChart();
        //잔고 콤마 셋팅
        setHomeText();

        setBudget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(">>>", "Btn clicked");
                Intent intent = new Intent(getActivity(), SetBudget.class);
                getActivity().startActivityForResult(intent, 2);
            }
        });

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
        dataSet.setValueLinePart1Length(0.6f);
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
        int allPrice = 0;
        for(int position = 0; position < sData.size(); position++){
            allPrice += sData.get(position).getPrice();
        }
        tv.setText(util.comma(allPrice) + "원");
    }
}
