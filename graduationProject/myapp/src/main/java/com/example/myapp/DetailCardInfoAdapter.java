package com.example.myapp;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class DetailCardInfoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Util util = new Util();
    public static class ViewHolder extends RecyclerView.ViewHolder{
        View container;
        TextView detailAccountTv;
        TextView detailPriceTv;

        ViewHolder(View view){
            super(view);
            container = view;
            this.detailAccountTv = (TextView) view.findViewById(R.id.detail_account_name);
            this.detailPriceTv = (TextView)view.findViewById(R.id.detail_account_price);
        }
    }
    private ArrayList<String> sData;
    private CreditCard selectedCard;
    private ArrayList<Stat> stats;
    private HashMap<String, Integer> datas;


    DetailCardInfoAdapter(ArrayList<String> d, CreditCard sc, ArrayList<Stat> sd){
        this.sData = d;
        Log.d("DetailCardInfiAdapter", "sData Size : " + sData.size());
        this.selectedCard = sc;
        this.stats = sd;
        datas = new HashMap<String, Integer>();
        for(int sPosition = 0; sPosition < stats.size(); sPosition++){
            stats.get(sPosition).getSelectedHashMap(sData, datas);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_detail_card_info_list, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        Log.d("KJH", "detailAdapter loop : " + position);
        final ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.detailAccountTv.setText(sData.get(position));
        viewHolder.detailPriceTv.setText(util.comma(selectedCard.getDiscountedPrice(sData.get(position), datas.get(sData.get(position)))));

        //아이템 리스너 설정
        viewHolder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("KJH", sData.get(position) + "");
            }
        });
    }
    @Override
    public int getItemCount(){
        return sData.size();
    }


    //데이터를 금액순으로 소팅
    public void dataSort(){
        ClassificationComparator comp = new ClassificationComparator();
        //Collections.sort(keys, comp);
    }

    //소팅에 필요한 비교자
    public class ClassificationComparator implements Comparator<Object> {
        @Override
        public int compare(Object first, Object second){
//            Log.d("KJH", "ClassificationSort");
//            int firstValue = sData.get(first);
//            int secondValue = sData.get(second);
//
//            if(firstValue > secondValue) return -1;
//            else if(firstValue < secondValue) return 1;
//            else return 0;
            return 1;
        }
    }

}
