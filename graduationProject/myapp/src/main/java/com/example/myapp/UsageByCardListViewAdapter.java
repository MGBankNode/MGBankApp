package com.example.myapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class UsageByCardListViewAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private ArrayList<MonthReportData> data;

    public UsageByCardListViewAdapter(Context context, ArrayList<MonthReportData> data) {
        this.inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.data = data;
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
        convertView = inflater.inflate(R.layout.usage_by_card_list_view_item, parent, false);

        TextView cardName = convertView.findViewById(R.id.cardListName);
        TextView cardValue = convertView.findViewById(R.id.cardListValue);

        cardName.setText(data.get(position).getPlace());
        cardValue.setText(data.get(position).getPrice() + "원");

        return convertView;
    }
}
