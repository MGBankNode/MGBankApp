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
    static final String BEAUTY = "뷰티/미용";
    static final String CULTURE = "문화/여가";
    static final String LIFE = "생활";
    static final String FOOD = "식비";
    static final String PAYMENT = "인터넷 결제";
    static final String TRAFFIC = "교통";
    static final String NONE = "미지정";
    static final String COFFEE = "카페/간식";


    public fragment_home() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //임시 데이터 셋팅
        sData = new ArrayList<Stat>();
        Stat Beauty = new Stat(BEAUTY);
        Stat Culture = new Stat(CULTURE);
        Stat Food = new Stat(FOOD);
        Stat Payment = new Stat(PAYMENT);
        Stat Traffic = new Stat(TRAFFIC);
        Stat None = new Stat(NONE);
        Stat Life = new Stat(LIFE);
        Stat Coffee = new Stat(COFFEE);

        //식비 데이터
        PayInfomation p;
        p = (new PayInfomation("자판기", 1200, Food));
        p = (new PayInfomation("자판기", 700, Food));
        p = (new PayInfomation("자판기", 1200, Food));
        p = (new PayInfomation("자판기", 1200, Food));
        p = (new PayInfomation("한솥도시락광운대역앞", 3500, Food));
        p = (new PayInfomation("자판기", 1200, Food));
        p = (new PayInfomation("자판기", 1200, Food));
        p = (new PayInfomation("자판기", 1200, Food));
        p = (new PayInfomation("지지고광운대점", 5000, Food));
        p = (new PayInfomation("더에이치엠한솥", 4200, Food));
        p = (new PayInfomation("한솥도시락광운대역앞", 3500, Food));
        p = (new PayInfomation("한솥도시락광운대역앞", 900, Food));
        p = (new PayInfomation("한솥도시락광운대역앞", 4200, Food));
        p = (new PayInfomation("지지고광운대점", 5000, Food));
        p = (new PayInfomation("지지고광운대점", 5000, Food));
        p = (new PayInfomation("후문식당", 5000, Food));
        p = (new PayInfomation("후문식당", 10000, Food));
        p = (new PayInfomation("이삭토스트광운대점", 4400, Food));
        p = (new PayInfomation("동대문엽기떡볶이양주", 13000, Food));
        p = (new PayInfomation("배뷸뚜기", 6000, Food));
        p = (new PayInfomation("배뷸뚜기", 6000, Food));
        p = (new PayInfomation("손큰할매순대국", 16000, Food));
        p = (new PayInfomation("삼형제쭈꾸미대학로점", 22000, Food));
        p = (new PayInfomation("곱창이야기", 43000, Food));
        p = (new PayInfomation("옥탑방고양이", 30800, Food));
        p = (new PayInfomation("타코푸에르토", 13900, Food));
        p = (new PayInfomation("멜팅그릴광운대점", 6500, Food));
        p = (new PayInfomation("멜팅그릴광운대점", 4000, Food));
        p = (new PayInfomation("멜팅그릴광운대점", 1000, Food));
        p = (new PayInfomation("멜팅그릴광운대점", 6500, Food));
        p = (new PayInfomation("열날개&떡볶이", 3200, Food));
        //결제 데이터
        p = (new PayInfomation("Kakao머니", 16987, Payment));
        p = (new PayInfomation("Kakao머니", 10000, Payment));
        p = (new PayInfomation("Kakao머니", 26286, Payment));
        p = (new PayInfomation("Kakao머니", 10000, Payment));
        p = (new PayInfomation("Kakao머니", 10000, Payment));
        p = (new PayInfomation("Kakao머니", 10000, Payment));
        p = (new PayInfomation("Kakao머니", 11100, Payment));
        p = (new PayInfomation("Kakao머니", 10000, Payment));
        p = (new PayInfomation("Kakao머니", 20000, Payment));
        p = (new PayInfomation("Kakao머니", 10000, Payment));
        p = (new PayInfomation("Kakao머니", 10000, Payment));
        p = (new PayInfomation("Kakao머니", 10000, Payment));
        p = (new PayInfomation("Kakao머니", 10000, Payment));
        p = (new PayInfomation("Kakao머니", 10000, Payment));
        p = (new PayInfomation("Kakao머니", 24765, Payment));
        p = (new PayInfomation("Kakao머니", 10000, Payment));
        p = (new PayInfomation("Kakao머니", 16400, Payment));
        p = (new PayInfomation("Kakao머니", 10000, Payment));
        //생활 데이터
        p = (new PayInfomation("세븐일레븐광운대점", 4500, Life));
        p = (new PayInfomation("이마트24광운대역점", 1500, Life));
        p = (new PayInfomation("미니스톱광운스퀘어", 2000, Life));
        p = (new PayInfomation("지에스25광운타운점", 4500, Life));
        p = (new PayInfomation("지에스25광운타운점", 2100, Life));
        p = (new PayInfomation("지에스25광운타운점", 1400, Life));
        p = (new PayInfomation("지에스25광운타운점", 14000, Life));
        p = (new PayInfomation("지에스25광운타운점", 5720, Life));
        p = (new PayInfomation("지에스25광운타운점", 3720, Life));
        p = (new PayInfomation("지에스25광운타운점", 4500, Life));
        p = (new PayInfomation("세븐일레븐광운대후", 4500, Life));
        p = (new PayInfomation("세븐일레븐성북역점", 2000, Life));
        p = (new PayInfomation("지에스25광운타운점", 2900, Life));
        p = (new PayInfomation("지에스25광운타운점", 4500, Life));
        p = (new PayInfomation("농민마트", 2900, Life));
        p = (new PayInfomation("성북마트", 2800, Life));
        p = (new PayInfomation("이마트월계점", 29590, Life));
        p = (new PayInfomation("성북마트", 4500, Life));
        //문화/여가 데이터
        p = (new PayInfomation("쓰리팝PC방", 5000, Culture));
        p = (new PayInfomation("쓰리팝PC방", 3000, Culture));
        p = (new PayInfomation("쓰리팝PC방", 5000, Culture));
        p = (new PayInfomation("쓰리팝PC방", 1500, Culture));
        p = (new PayInfomation("쓰리팝PC방", 5000, Culture));
        p = (new PayInfomation("쓰리팝PC방", 4000, Culture));
        p = (new PayInfomation("쓰리팝PC방", 10000, Culture));
        p = (new PayInfomation("쓰리팝PC방", 5000, Culture));
        p = (new PayInfomation("쓰리팝PC방", 10000, Culture));
        //교통 데이터
        p = (new PayInfomation("서울택시_법인_KSCC", 4500, Traffic));
        p = (new PayInfomation("서울택시_개인영세_KSCC", 3800, Traffic));
        p = (new PayInfomation("서울택시_법인_KSCC", 6000, Traffic));
        p = (new PayInfomation("지방택시_법인_KSCC", 20000, Traffic));
        p = (new PayInfomation("서울택시_개인영세_KSCC", 6100, Traffic));
        //뷰티/미용 데이터
        p = (new PayInfomation("에이치헤어", 15000, Beauty));
        p = (new PayInfomation("에이치헤어", 12000, Beauty));
        //미분류 데이터
        p = (new PayInfomation("성일축산유통", 9300, None));
        p = (new PayInfomation("주식회사정오아카데미", 2400, None));
        p = (new PayInfomation("KOCES결제대행(자판기", 800, None));
        //카페/간식 데이터
        p = (new PayInfomation("커피만광운대점", 1500, Coffee));
        p = (new PayInfomation("탐앤탐스", 8400, Coffee));

        //데이터 추가
        sData.add(Beauty);
        sData.add(Culture);
        sData.add(Food);
        sData.add(Payment);
        sData.add(Traffic);
        sData.add(Coffee);
        sData.add(None);
        sData.add(Life);
//        sData.add(new Stat("쇼핑/의류", 113200));
//        sData.add(new Stat("쇼핑/가전", 14500));
//        sData.add(new Stat("생활/문화", 62800));
//        sData.add(new Stat("교통", 15420));
//        sData.add(new Stat("식비", 414000));
//        sData.add(new Stat("미지정", 105000));
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        //리사이클러뷰 셋팅
        //setRecyclerView();
        //그래프 셋팅
        setPieChart();
        //잔고 콤마 셋팅
        setHomeText();
        super.onActivityCreated(savedInstanceState);
    }
    //리사이클러뷰 셋팅
    /*public void setRecyclerView(){
        recyclerView = (RecyclerView)getView().findViewById(R.id.recyclerView01);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getView().getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setNestedScrollingEnabled(false);

        adapter = new RecyclerViewAdapter(sData);
        recyclerView.setAdapter(adapter);
    }*/
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
        tv.setText(util.comma(123456789) + "원");
    }
}
