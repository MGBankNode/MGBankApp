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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
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
    private ArrayList<Stat> data ;
    int allPrice = 0;

    RecyclerViewAdapter(ArrayList<Stat> d){
        this.data = d;
        dataSort();
        getAllPrice();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_list_view_fragment_home, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.labelTv.setText(Integer.toString(position + 1));

        viewHolder.nameTv.setText(data.get(position).getName());
        LinearLayout.LayoutParams btn_params =
                (LinearLayout.LayoutParams) viewHolder.percentBtn.getLayoutParams();
        LinearLayout.LayoutParams reverse_params =
                (LinearLayout.LayoutParams) viewHolder.percentReverseTv.getLayoutParams();
        int percent = (int)(100 * data.get(position).getPrice() / allPrice);

        Log.d("KJH", "price : " + data.get(position).getPrice());
        Log.d("KJH", "allprice : " + allPrice);
        Log.d("KJH", "price / allprice : " + data.get(position).getPrice() / allPrice);
        btn_params.weight = percent;
        reverse_params.weight = 100 - percent;
        Log.d("KJH", "percent : " + percent + ", weight : " + btn_params.weight);
        viewHolder.moneyTv.setText(util.comma(data.get(position).getPrice()) + "원");
        viewHolder.percentTv.setText(Integer.toString(percent) + "%");
        //아이템 리스너 설정
        viewHolder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("KJH", data.get(position) + "");
                Fragment fr = new PayInfoList();
                //새로운 프래그먼트에 전달할 객체
                Bundle args = new Bundle();
                args.putSerializable("STAT", data.get(position));
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
        return data.size();
    }

    //데이터를 금액순으로 소팅
    public void dataSort(){
        RecyclerViewComparator comp = new RecyclerViewComparator();
        Collections.sort(this.data, comp);
    }
    //모든 데이터의 총금액을 구함
    public void getAllPrice(){
        for(int i = 0; i < data.size(); i++){
            allPrice += data.get(i).getPrice();
        }
    }
    //소팅에 필요한 비교자
    public class RecyclerViewComparator implements Comparator<Stat> {
        @Override
        public int compare(Stat first, Stat second){
            int firstValue = first.getPrice();
            int secondValue = second.getPrice();

            if(firstValue > secondValue) return -1;
            else if(firstValue < secondValue) return 1;
            else return 0;
        }
    }

}
