package com.example.myapp;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class UsageByCardDetailListViewAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private ArrayList<MonthReportData> data;

    public UsageByCardDetailListViewAdapter(Context context, ArrayList<MonthReportData> data) {
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
        convertView = inflater.inflate(R.layout.usage_by_detail_card_list_view_item, parent, false);

        TextView cardCount = convertView.findViewById(R.id.cardDetailListCount);
        TextView cardDate = convertView.findViewById(R.id.cardDetailListDate);
        TextView cardPlace = convertView.findViewById(R.id.cardDetailListPlace);
        TextView cardValue = convertView.findViewById(R.id.cardDetailListValue);



        // tempDate 는 2019-05-07 09:26:00 의 형태
        String tempDate = data.get(position).getDate();
        String[] tempDateArray1 = tempDate.split(" ");
        String[] tempDateArray2 = tempDateArray1[0].split("-");

        String tempDateMonth = tempDateArray2[1];
        String tempDateDay = tempDateArray2[2];

        int tempDateMonthInt = Integer.parseInt(tempDateMonth);
        int tempDateDayInt = Integer.parseInt(tempDateDay);

        if(tempDateMonthInt < 10)
            tempDateMonth = String.valueOf(tempDateMonthInt);

        if(tempDateDayInt < 10)
            tempDateDay = String.valueOf(tempDateDayInt);

        Log.d(">>>", tempDateMonthInt + " " + tempDateDayInt);

        String dateResult = tempDateMonth + "월 " + tempDateDay + "일";

        cardCount.setText(String.valueOf(position+1));
        cardDate.setText(dateResult);
        cardPlace.setText(data.get(position).getPlace());
        cardValue.setText(data.get(position).getPrice() + "원");

        return convertView;
    }
}
