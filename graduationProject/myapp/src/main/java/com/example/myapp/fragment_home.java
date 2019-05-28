package com.example.myapp;


import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;
import java.util.Random;
//그래프
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import org.w3c.dom.Text;


/**
 * A simple {@link Fragment} subclass.
 */
public class fragment_home extends Fragment {
    ArrayList<Stat> sData = null;
    RecyclerView recyclerView = null;
    RecyclerViewAdapter adapter = null;
    LinearLayoutManager layoutManager = null;
    private PieChart pieChart = null;
    String budget;
    Util util = new Util();
    int allPriceValue;

    protected TextView budgetBtn;


    BroadcastReceiver receiver = null;


    public fragment_home() {
        // Required empty public constructor
    }

    Toolbar toolbar;
    TextView textTitle;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        sData = new ArrayList<Stat>();
        sData = (ArrayList<Stat>)getArguments().get("DATA");
        budget = (String)getArguments().getString("BUDGET");
        Log.d("homeFragment", sData.toString());

        budgetBtn = (TextView)view.findViewById(R.id.setBudgetBtn);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("sendbudget");
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                budgetBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), SetBudget.class);
                        getActivity().startActivityForResult(intent, 2);
                    }
                });
                String budgetString = intent.getStringExtra("BUDGET");

                Log.d("KJH", "budgetString : " + budgetString);
                if(!budgetString.isEmpty()){
                    Log.d("KJH", "예산 텍스트 설정");

                    Log.d("CHJ", util.comma(Integer.parseInt(budgetString)));
                    if(Integer.parseInt(budgetString)== -1)
                        budgetBtn.setText("예산을 설정해주세요.");
                    else
                    budgetBtn.setText("예산 : " + util.comma(Integer.parseInt(budgetString)) + "원");

                }
                setBudget(view, budgetString);
            }
        };
        getContext().registerReceiver(receiver, intentFilter);

        // Inflate the layout for this fragment
        return view;
    }



    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        //리사이클러뷰 셋팅
        setRecyclerView();
        //그래프 셋팅
        setPieChart();
        //잔고 콤마 셋팅
        setHomeText();


        Intent intent = new Intent();
        intent.setAction("budget");
        getActivity().sendBroadcast(intent);

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
        dataSet.setValueLinePart1Length(0.5f);
        dataSet.setValueLinePart2Length(.6f);
        dataSet.setValueTextSize(7);
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
        allPriceValue = allPrice;
        tv.setText(util.comma(allPrice) + "원");
    }
    public void setBudget(View v, String budgetString){
        Log.d("KJH", "setBudget budgetString : " + budgetString);
        int budget = Integer.parseInt(budgetString);
        int remainbudget = budget - allPriceValue;
        TextView tv = (TextView)v.findViewById(R.id.remainBudget);
        TextView tv2 = (TextView)v.findViewById(R.id.statusText);
        double todayValue = new Date().getDate() / 31.00;
        Log.d("KJH", "today value : " + todayValue + "date : " + new Date().getDate());
        ImageView imageView = (ImageView)v.findViewById(R.id.faceImage);
        TextView tv3 = (TextView)v.findViewById(R.id.notice_text);
        if(remainbudget > (budget - (budget * todayValue))){
            imageView.setImageResource(R.drawable.smile);
            tv2.setText("아주 좋아요!");
            tv.setText(util.comma(remainbudget) + "원 남음");
            tv.setTextColor(Color.parseColor("#2BB0DD"));
            tv3.setText("고객님의 일 평균 사용 금액은 " + util.comma(allPriceValue / new Date().getDate()) + "원 입니다.\n" +
                    "이대로만 사용하시면 문제없어요!");
        }else{
            if(budget == -1) {
                tv2.setText("아주 좋아요!");
                tv.setText("설정된 예산이 없습니다.");
                tv.setTextColor(Color.parseColor("#2BB0DD"));
                imageView.setImageResource(R.drawable.smile);
                tv3.setText("금융비서 알림이 없습니다.\n 예산을 설정하면 알림을 보실 수 있습니다.");
            }
            else if(remainbudget <= 0) {
                tv2.setText("눈물이 흘러요ㅜ_ㅜ");
                tv.setText(util.comma(remainbudget * -1) + "원 초과");
                tv.setTextColor(Color.parseColor("#FF2222"));
                imageView.setImageResource(R.drawable.crying);
                tv3.setText("고객님의 일 평균 사용 금액은 " + util.comma(allPriceValue / new Date().getDate()) + "원 입니다.\n" +
                        "검소한 삶을 사십시오 ...");
            }
            else {
                tv2.setText("좋진 않아요..");
                tv.setText(util.comma(remainbudget) + "원 남음");
                tv.setTextColor(Color.parseColor("#995555"));
                imageView.setImageResource(R.drawable.sad);
                tv3.setText("고객님의 일 평균 사용 금액은 " + util.comma(allPriceValue / new Date().getDate()) + "원 입니다.\n" +
                        "약간의 낭비를 줄여보는 것은 어떨까요?");
            }
        }

    }
}
