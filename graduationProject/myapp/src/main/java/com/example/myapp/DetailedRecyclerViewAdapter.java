package com.example.myapp;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class DetailedRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Util util = new Util();
    public static class ViewHolder extends RecyclerView.ViewHolder{
        View container;
        TextView labelTv;
        TextView nameTv;
        Button percentBtn;
        TextView percentReverseTv;
        TextView percentTv;
        TextView moneyTv;
        ViewHolder(View view){
            super(view);
            container = view;
            this.labelTv = (TextView) view.findViewById(R.id.list_number);
            this.nameTv = (TextView)view.findViewById(R.id.stat_textView);
            this.percentBtn = (Button)view.findViewById(R.id.percent_button);
            this.percentReverseTv = (TextView)view.findViewById(R.id.percent_reverse);
            this.percentTv = (TextView)view.findViewById(R.id.percent_text);
            this.moneyTv = (TextView)view.findViewById(R.id.money_textView);
        }
    }
    private HashMap<String, Integer> data ;
    Stat stat;
    int allPrice = 0;
    List<String> keys;


    DetailedRecyclerViewAdapter(HashMap<String, Integer> d, Stat stat){
        this.data = d;
        this.stat = stat;
        keys = new ArrayList<String>();
        keys.addAll(data.keySet());
        Log.d("KJH", "keys size : " + keys.size());
        Log.d("KJH", "keys value : " + keys.get(0));
        dataSort();
        getAllPrice();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_list_view_payinfo, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.labelTv.setText(Integer.toString(position + 1));
        viewHolder.nameTv.setText(keys.get(position));
        LinearLayout.LayoutParams btn_params =
                (LinearLayout.LayoutParams) viewHolder.percentBtn.getLayoutParams();
        LinearLayout.LayoutParams reverse_params =
                (LinearLayout.LayoutParams) viewHolder.percentReverseTv.getLayoutParams();
        int percent = (int)(100 * data.get(keys.get(position))/ allPrice);

        Log.d("KJH", "price : " + data.get(keys.get(position)));
        Log.d("KJH", "allprice : " + allPrice);
        Log.d("KJH", "price / allprice : " + data.get(keys.get(position))/ allPrice);
        btn_params.weight = percent;
        reverse_params.weight = 100 - percent;
        Log.d("KJH", "percent : " + percent + ", weight : " + btn_params.weight);
        viewHolder.moneyTv.setText(util.comma(data.get(keys.get(position))) + "원");
        viewHolder.percentTv.setText(Integer.toString(percent) + "%");
        //아이템 리스너 설정
        viewHolder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                /////////////////////////////////
//                CardDataBase cardDataBase = new CardDataBase();
//                ArrayList<String> temp = new ArrayList<String>();
//                int tempInt = cardDataBase.PeachCard.getDiscountedPrice(data, temp);
                /////////////////////////////////
                Log.d("KJH", data.get(position) + "");
                CustomDialog cd = new CustomDialog();
                TextView tv = (TextView)v.findViewById(R.id.testTextView);

                Bundle args = new Bundle();

                args.putString("PAY_ACCOUNT", keys.get(position));
                args.putSerializable("STAT", stat);

                cd.setArguments(args);
                final FragmentActivity f = (FragmentActivity) v.getContext();
                FragmentManager fm = f.getSupportFragmentManager();
                cd.show(fm, "OpenDialog");
                //다이얼로그로 부터 결과값을 받아오고 그 후 처리
                cd.setDialogResult(new CustomDialog.CustomDialogResult(){
                    @Override
                    public void finish(String result){
                        Toast.makeText(f,"Correct Test : " + result , Toast.LENGTH_LONG).show();
                        HistoryRequest testRequest = new HistoryRequest(
                                stat.getUserId(),                            //사용자 아이디 myUserInfo 객체에서 getstat.getUserId()()받아와 사용하시면되요
                                "2019-05-01",                               //전달의 시작 날짜 - 일은 01로 고정시키고 년도랑 월만 계산해서 가져오시면되요(시작일은 무조건 01이므로)
                                "2019-06-01",                               //전달의 마지막 날짜 - 일은 31로 고정시키고 년도랑 월만 게산해서 가져오시면되요 (최대 31일이므로)
                                RequestInfo.RequestType.ACCOUNT_HOME_HISTORY,      //이거는 고정
                                v.getContext());                          //이거는 context 얻어오는 건데 여기는 액티비티라서 getApplicationContext()해서 받아오는데

                        //fragment 쪽에서는 getContext()하시면 될 것 같아요
                        //HomeRequest(callback - onSuccess Override)를해서 정보 받아옴
                        testRequest.HomeRequest(new HistoryRequest.VolleyCallback() {
                            @Override
                            public void onSuccess(HistoryInfo[] historyInfo, DailyHistoryInfo[] dailyHistoryInfo) {
                                int arrLength = historyInfo.length;

                                ArrayList<Stat> temp = new ArrayList<Stat>();
                                Stat Culture = new Stat(Stat.CULTURE, stat.getUserId());
                                Stat Food = new Stat(Stat.FOOD, stat.getUserId());
                                Stat Finance = new Stat(Stat.FINANCE, stat.getUserId());
                                Stat Traffic = new Stat(Stat.TRAFFIC, stat.getUserId());
                                Stat None = new Stat(Stat.NONE, stat.getUserId());
                                Stat Life = new Stat(Stat.LIFE, stat.getUserId());
                                Stat Coffee = new Stat(Stat.COFFEE, stat.getUserId());
                                Stat Dwelling = new Stat(Stat.DWELLING, stat.getUserId());
                                Stat Drink = new Stat(Stat.DRINK, stat.getUserId());
                                Stat Travel = new Stat(Stat.TRAVEL, stat.getUserId());
                                Stat Hospital = new Stat(Stat.HOSPITAL, stat.getUserId());

                                String[] hValue = new String[arrLength];
                                String[] hName = new String[arrLength];
                                String[] cName = new String[arrLength];
                                PayInfomation p;
                                for (int i = 0; i < arrLength; i++) {
                                    cName[i] = historyInfo[i].getcName();        //카테고릐 분류
                                    Date date = new Date(historyInfo[i].gethDate());
                                    Log.d("KJh", "Date : " + date);
                                    Log.d("KJH", "origin Date : " + historyInfo[i].gethDate());
                                    switch (cName[i]) {
                                        case "술/유흥":
                                            p = new PayInfomation(historyInfo[i].gethName(),
                                                    Integer.parseInt(historyInfo[i].gethValue()), Drink, date, historyInfo[i].gethId());
                                            break;
                                        case "생활(쇼핑)":
                                            p = new PayInfomation(historyInfo[i].gethName(),
                                                    Integer.parseInt(historyInfo[i].gethValue()), Life, date, historyInfo[i].gethId());
                                            break;
                                        case "교통":
                                            p = new PayInfomation(historyInfo[i].gethName(),
                                                    Integer.parseInt(historyInfo[i].gethValue()), Traffic, date, historyInfo[i].gethId());
                                            break;
                                        case "주거/통신":
                                            p = new PayInfomation(historyInfo[i].gethName(),
                                                    Integer.parseInt(historyInfo[i].gethValue()), Dwelling, date, historyInfo[i].gethId());
                                            break;
                                        case "의료/건강":
                                            p = new PayInfomation(historyInfo[i].gethName(),
                                                    Integer.parseInt(historyInfo[i].gethValue()), Hospital, date, historyInfo[i].gethId());
                                            break;
                                        case "금융":
                                            p = new PayInfomation(historyInfo[i].gethName(),
                                                    Integer.parseInt(historyInfo[i].gethValue()), Finance, date, historyInfo[i].gethId());
                                            break;
                                        case "문화/여가":
                                            p = new PayInfomation(historyInfo[i].gethName(),
                                                    Integer.parseInt(historyInfo[i].gethValue()), Culture, date, historyInfo[i].gethId());
                                            break;
                                        case "여행/숙박":
                                            p = new PayInfomation(historyInfo[i].gethName(),
                                                    Integer.parseInt(historyInfo[i].gethValue()), Travel, date, historyInfo[i].gethId());
                                            break;
                                        case "식비":
                                            p = new PayInfomation(historyInfo[i].gethName(),
                                                    Integer.parseInt(historyInfo[i].gethValue()), Food, date, historyInfo[i].gethId());
                                            break;
                                        case "카페/간식":
                                            p = new PayInfomation(historyInfo[i].gethName(),
                                                    Integer.parseInt(historyInfo[i].gethValue()), Coffee, date, historyInfo[i].gethId());
                                            break;
                                        case "미분류":
                                            p = new PayInfomation(historyInfo[i].gethName(),
                                                    Integer.parseInt(historyInfo[i].gethValue()), None, date, historyInfo[i].gethId());
                                            break;
                                        default:
                                            break;
                                    }
                                }

                                temp.add(Drink);
                                temp.add(Life);
                                temp.add(Traffic);
                                temp.add(Dwelling);
                                temp.add(Hospital);
                                temp.add(Finance);
                                temp.add(Culture);
                                temp.add(Travel);
                                temp.add(Food);
                                temp.add(Coffee);
                                temp.add(None);

                                ArrayList<Stat> sData = new ArrayList<Stat>();
                                sData.clear();
                                for (int i = 0; i < temp.size(); i++) {
                                    if (!temp.get(i).isEmpty())
                                        sData.add(temp.get(i));
                                }


                                //////////////////////////////////설정된 예산 요청///////////////////////
                                BudgetRequest budgetRequest1 = new BudgetRequest(stat.getUserId(), RequestInfo.RequestType.DEFAULT_BUDGET, v.getContext());

                                budgetRequest1.GetBudgetHandler(budget -> {
                                    Toast.makeText(v.getContext(), budget, Toast.LENGTH_LONG).show();
                                });
                                //////////////////////////////////////////////////////////////////



                                //////////////////////////////////설정된 예산 요청///////////////////////
                                BudgetRequest budgetRequest2 = new BudgetRequest(stat.getUserId(), "1000000",RequestInfo.RequestType.CHANGE_BUDGET, v.getContext());

                                budgetRequest2.ChangeBudgetHandler(budget -> {
                                    Toast.makeText(v.getContext(), "예산 설정 성공", Toast.LENGTH_LONG).show();
                                });
                                //////////////////////////////////////////////////////////////////
                                Log.d("KJH", "change Start");
                                Fragment fr = new fragment_home();

                                Bundle args = new Bundle();
                                args.putSerializable("DATA", sData);
                                fr.setArguments(args);
                                FragmentManager fm = ((FragmentActivity) v.getContext()).getSupportFragmentManager();
                                FragmentTransaction fragmentTransaction = fm.beginTransaction();
                                fragmentTransaction.replace(R.id.dynamic_mainFragment, fr);
                                for(int i = 0; i < fm.getBackStackEntryCount(); ++i) {
                                    fm.popBackStack();
                                }
                                fragmentTransaction.commit();
                                //위에 처럼 각각 HistoryInfo 에는 각각 정보들 get으로 얻어서 사용하시면 되요
                            }
                        });
                    }
                });

            }
        });
    }
    @Override
    public int getItemCount(){
        return data.size();
    }

    //모든 데이터의 총금액을 구함
    public void getAllPrice(){
        for(int i = 0; i < data.size(); i++){
            allPrice += data.get(keys.get(i));
        }
    }

    //데이터를 금액순으로 소팅
    public void dataSort(){
        ClassificationComparator comp = new ClassificationComparator();
        Collections.sort(keys, comp);
    }

    //소팅에 필요한 비교자
    public class ClassificationComparator implements Comparator<Object> {
        @Override
        public int compare(Object first, Object second){
            Log.d("KJH", "ClassificationSort");
            int firstValue = data.get(first);
            int secondValue = data.get(second);

            if(firstValue > secondValue) return -1;
            else if(firstValue < secondValue) return 1;
            else return 0;
        }
    }

}
