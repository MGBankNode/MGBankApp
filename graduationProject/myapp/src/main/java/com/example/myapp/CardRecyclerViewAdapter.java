package com.example.myapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class CardRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Util util = new Util();
    public static class ViewHolder extends RecyclerView.ViewHolder{
        View container;
        ImageView cardIcon;
        TextView cardName;
        TextView discountedPrice;

        ViewHolder(View view){
            super(view);
            container = view;
            this.cardIcon = (ImageView) view.findViewById(R.id.card_ic);
            this.cardName = (TextView) view.findViewById(R.id.card_name);
            this.discountedPrice = (TextView) view.findViewById(R.id.discountTextView);
        }
    }
    private HashMap<CreditCard, Integer> priceData ;
    private List<CreditCard> keys;
    private HashMap<CreditCard, HashMap<Stat, Integer>> discountData;


    CardRecyclerViewAdapter(HashMap<CreditCard, Integer> d,
                            HashMap<CreditCard, HashMap<Stat, Integer>> discountData){
        this.priceData = d;
        this.discountData = discountData;
        keys = new ArrayList<CreditCard>();
        keys.addAll(priceData.keySet());
        dataSort();
        Log.d("CardRecyclerViewAdapter", "marin Data size : " + priceData.size() +", data size : " + discountData);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_card_list, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        Log.d("KJH", "CardRecyclerViewAdapter data position : " + position);
        final ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.cardName.setText(keys.get(position).getCardName());
        viewHolder.cardIcon.setImageResource(keys.get(position).getIcon());
        viewHolder.discountedPrice.setText(util.comma(priceData.get(keys.get(position))));

        //아이템 리스너 설정
        viewHolder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("KJH", keys.get(position) + " click Event");

                DetailCardInfo fr = new DetailCardInfo();
                Bundle args = new Bundle();
                args.putSerializable("CARD", keys.get(position));
                Log.d("CardRecyclerViewAdapter", "Card adpater : " + keys.get(position));
                args.putSerializable("DATA", discountData);

                args.putString("PRICE", util.comma(priceData.get(keys.get(position))));

                fr.setArguments(args);
                FragmentActivity f = (FragmentActivity)v.getContext();
                FragmentManager fm = f.getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fm.beginTransaction();
                fragmentTransaction.replace(R.id.dynamic_mainFragment, fr);
                fragmentTransaction.commit();
            }
        });
    }
    @Override
    public int getItemCount(){
        return priceData.size();
    }

    //데이터를 금액순으로 소팅
    public void dataSort(){
        CardRecyclerViewComparator comp = new CardRecyclerViewComparator();
        Collections.sort(keys, comp);
    }

    //소팅에 필요한 비교자
    public class CardRecyclerViewComparator implements Comparator<Object> {
        @Override
        public int compare(Object first, Object second){
            int firstValue = priceData.get(first);
            int secondValue = priceData.get(second);

            if(firstValue > secondValue) return -1;
            else if(firstValue < secondValue) return 1;
            else return 0;
        }
    }

}
