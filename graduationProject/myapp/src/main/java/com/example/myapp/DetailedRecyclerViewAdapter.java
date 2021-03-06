package com.example.myapp;

import android.app.Dialog;
import android.content.Intent;
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

        btn_params.weight = percent;
        reverse_params.weight = 100 - percent;
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
                    public void finish(String result) {
                        if(!result.equals("cancel")) {
                            Intent intent = new Intent();
                            intent.setAction("HomeFragment");
                            v.getContext().sendBroadcast(intent);
                        }
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
            int firstValue = data.get(first);
            int secondValue = data.get(second);

            if(firstValue > secondValue) return -1;
            else if(firstValue < secondValue) return 1;
            else return 0;
        }
    }

}
