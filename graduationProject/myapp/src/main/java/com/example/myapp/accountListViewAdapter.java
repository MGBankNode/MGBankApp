package com.example.myapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class accountListViewAdapter extends BaseAdapter {

    public accountListViewAdapter(Context context, ArrayList<AccountListData> data, int layout) {
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.data = data;
        this.layout = layout;
    }

    private LayoutInflater inflater;
    private ArrayList<AccountListData> data;
    private int layout;

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
        if(convertView == null)
            convertView=inflater.inflate(layout, parent, false);

        TextView listName = convertView.findViewById(R.id.accountListName);
        listName.setText(data.get(position).getAccountName());

        TextView listNumber = convertView.findViewById(R.id.accountListNumber);
        listNumber.setText(data.get(position).getAccountNumber());

        TextView listValue = convertView.findViewById(R.id.accountListValue);
        listValue.setText(data.get(position).getAccountValue());

        return convertView;
    }
}
