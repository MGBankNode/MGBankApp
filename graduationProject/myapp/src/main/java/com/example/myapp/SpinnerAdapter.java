package com.example.myapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class SpinnerAdapter extends BaseAdapter {

    Context context;
    ArrayList<String> category;

    private TextView categoryTextView;

    public SpinnerAdapter(Context context, ArrayList<String> category) {
        this.context = context;
        this.category = category;
    }

    public SpinnerAdapter() {
    }

    @Override
    public int getCount() {
        return category.size();
    }

    @Override
    public Object getItem(int position) {
        return category.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null)
            convertView = LayoutInflater.from(context).inflate(R.layout.spinneritem, null);

        categoryTextView = convertView.findViewById(R.id.category_tv);
        categoryTextView.setText(category.get(position));

        return convertView;
    }
}
