package com.example.myapp;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class reportListviewAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private ArrayList<reportElement> data;
    private int layout;
    int rpmoney;

    public reportListviewAdapter(Context context, int layout, ArrayList<reportElement> data){
        this.inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.layout=layout;
        this.data=data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null){
            convertView=inflater.inflate(layout,parent,false);
        }
        TextView rpDate = convertView.findViewById(R.id.reportDate);
        TextView rpExpenditure = convertView.findViewById(R.id.reportExpenditure);

        if(position % 2 == 1) {
            LinearLayout linearLayout = convertView.findViewById(R.id.reportContainer);
            rpDate.setText("");
            rpExpenditure.setText("");
            LinearLayout.LayoutParams lparam = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            lparam.height = 30;
            linearLayout.setLayoutParams(lparam);
            linearLayout.setBackgroundColor(Color.rgb(230,230,230));
        }
        else {

            rpDate.setText(data.get(position).getReportDate());

            rpmoney = data.get(position).getReportExpenditure();
            String tempStr = String.format("%,d", rpmoney);
            String result = "총 " +tempStr + "원 사용";
            rpExpenditure.setText(result);

        }
        return convertView;
    }
}
