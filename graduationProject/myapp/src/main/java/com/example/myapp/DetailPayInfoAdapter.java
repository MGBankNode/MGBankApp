package com.example.myapp;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;

public class DetailPayInfoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Util util = new Util();
    public static class ViewHolder extends RecyclerView.ViewHolder{
        View container;
        TextView detailDateTv;
        TextView detailAccountTv;
        TextView detailOriginPriceTv;
        TextView detailDiscountPriceTv;

        ViewHolder(View view){
            super(view);
            container = view;
            this.detailDateTv = (TextView) view.findViewById(R.id.dateTextView);
            this.detailAccountTv = (TextView)view.findViewById(R.id.accountText);
            this.detailOriginPriceTv= (TextView)view.findViewById(R.id.origin_money_text);
            this.detailDiscountPriceTv = (TextView)view.findViewById(R.id.discounted_money_text);
        }
    }
    private Stat selectedStat;
    private CreditCard selectedCard;
    private ArrayList<String> sdata;
    private ArrayList<PayInfomation> data;
    private CardDataBase cdb = new CardDataBase();


    DetailPayInfoAdapter(Stat s, CreditCard c){
        this.selectedStat = s;
        this.selectedCard = c;
        selectedCard.resetCondition();
        sdata = new ArrayList<String>();
        s.dataSortByPrice();
        s.getDiscountedPrice(selectedCard, sdata);
        data = new ArrayList<PayInfomation>();
        for(int sPosition = 0; sPosition < sdata.size(); sPosition++){
            data.add(s.getPayInfomation(sdata.get(sPosition)));
        }
        dataSortByDate();
        c.resetCondition();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_detail_list_view_payinfo, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        Log.d("KJH", "detailAdapter loop : " + position + ", " + data.get(position).getAccountName());
        final ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.detailDateTv.setText(util.dateForm(data.get(position).getDate()));
        viewHolder.detailAccountTv.setText(data.get(position).getAccountName());
        viewHolder.detailOriginPriceTv.setText(util.comma(data.get(position).getPrice()));
        int temp = 0;
        temp = selectedCard.getDiscountedPrice(selectedStat, data.get(position).getAccountName(),
                data.get(position).getPrice());
        if(temp > 0) {
            Log.d("KJH", "test Log : " + data.get(position).getPrice() + ", temp : " + temp);
            viewHolder.detailOriginPriceTv.setPaintFlags(viewHolder.detailOriginPriceTv.getPaintFlags()
                            | Paint.STRIKE_THRU_TEXT_FLAG);
            viewHolder.detailDiscountPriceTv.setText(data.get(position).getPrice() - temp + "");
            viewHolder.detailDiscountPriceTv.setTextColor(Color.parseColor("#2BB0DD"));
        }

        //아이템 리스너 설정
        viewHolder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("KJH", data.get(position).getAccountName() + "");
            }
        });
    }
    @Override
    public int getItemCount(){
        return data.size();
    }


    //데이터를 금액순으로 소팅
    public void dataSortByDate(){
        PayInfoComparatorByDate comp = new PayInfoComparatorByDate();
        Collections.sort(this.data, comp);
    }
    //소팅에 필요한 비교자
    public class PayInfoComparatorByDate implements Comparator<PayInfomation> {
        @Override
        public int compare(PayInfomation first, PayInfomation second){
            Date firstValue = first.getDate();
            Date secondValue = second.getDate();
            int c = firstValue.compareTo(secondValue);
            if(c > 0) return -1;
            else if(c < 0) return 1;
            else return 0;
        }
    }

}
