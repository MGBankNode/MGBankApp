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
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

public class DetailCardInfoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Util util = new Util();
    public static class ViewHolder extends RecyclerView.ViewHolder{
        View container;
        TextView detailStatTv;
        TextView detailPriceTv;

        ViewHolder(View view){
            super(view);
            container = view;
            this.detailStatTv = (TextView) view.findViewById(R.id.detail_stat_name);
            this.detailPriceTv = (TextView)view.findViewById(R.id.detail_account_price);
        }
    }
//    private ArrayList<String> sData;
    private CreditCard selectedCard;
    private ArrayList<Stat> keys;
    private HashMap<CreditCard, HashMap<Stat,Integer>> discountData;
//    private HashMap<String, Integer> datas;


    DetailCardInfoAdapter(HashMap<CreditCard, HashMap<Stat,Integer>> d, CreditCard sc){
        discountData = d;
        this.selectedCard = sc;
        keys = new ArrayList<Stat>();
        keys.addAll(discountData.get(selectedCard).keySet());

//        this.sData = d;
//        Log.d("DetailCardInfiAdapter", "sData Size : " + sData.size());
//        this.selectedCard = sc;
//        this.stats = sd;
//        datas = new HashMap<String, Integer>();
//        for(int sPosition = 0; sPosition < stats.size(); sPosition++){
//            stats.get(sPosition).getSelectedHashMap(sData, datas);
//        }
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
        viewHolder.detailStatTv.setText(keys.get(position).toString());
        viewHolder.detailPriceTv.setText(util.comma(discountData.get(selectedCard).get(keys.get(position))));

        //아이템 리스너 설정
        viewHolder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("KJH", keys.get(position) + "");

                Bundle args = new Bundle();

                Fragment fr = new DetailPayInfo();

                args.putSerializable("STAT", keys.get(position));
                args.putSerializable("CARD", selectedCard);
                fr.setArguments(args);
                FragmentActivity f = (FragmentActivity)v.getContext();
                FragmentManager fm = f.getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fm.beginTransaction();
                fragmentTransaction.replace(R.id.dynamic_mainFragment, fr);
                fragmentTransaction.setCustomAnimations(R.anim.fragment_in, R.anim.fragment_out);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }


        });
    }
    @Override
    public int getItemCount(){
        return keys.size();
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
