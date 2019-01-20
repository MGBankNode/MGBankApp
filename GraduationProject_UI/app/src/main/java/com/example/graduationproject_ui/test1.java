package com.example.graduationproject_ui;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class test1 extends Fragment {

    private TextView tvDate;

    private GridAdapter gridAdapter;
    private ArrayList<String> dayList;
    private GridView gridView;
    private Calendar mCal;
    private View v;
    public test1() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.activity_test1, container, false);

        //프래그먼트 구현
        RelativeLayout layout = (RelativeLayout) inflater.inflate(R.layout.activity_test1,
                container, false);
        return layout;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        /////////////////////////////////////////////////////////////////////////////////
        /////////////////////////////그리드 뷰 로드//////////////////////////////////////
        /////////////////////////////////////////////////////////////////////////////////
        tvDate = (TextView)getView().findViewById(R.id.tv_date);
        gridView = (GridView)getView().findViewById(R.id.gridview);
        //오늘날짜 설정
        long now = System.currentTimeMillis();
        final Date date = new Date(now);

        //연,월,일을 따로 저장
        final SimpleDateFormat curYearFormat = new SimpleDateFormat("yyyy", Locale.KOREA);
        final SimpleDateFormat curMonthFormat = new SimpleDateFormat("MM", Locale.KOREA);
        final SimpleDateFormat curDayFormat = new SimpleDateFormat("dd", Locale.KOREA);

        //현재 날짜 텍스트뷰에 설정
        tvDate.setText(curYearFormat.format(date) + "년 " + curMonthFormat.format(date) + "월");
        //dayList 데이터 생성
        dayList = new ArrayList<String>();
        mCal = Calendar.getInstance();
        //이번달 1일은 무슨 요일인가?
        mCal.set(Integer.parseInt(curYearFormat.format(date)), Integer.parseInt(curMonthFormat.format(date)) - 1, 1);
        int dayNum = mCal.get(Calendar.DAY_OF_WEEK);

        //1일 - 요일 매칭 시키기 위해 공백 add
        for (int i = 1; i < dayNum; i++) {
            dayList.add("");
        }
        setCalendarDate(mCal.get(Calendar.MONTH) + 1);
        gridAdapter = new GridAdapter(getActivity(), dayList);
        gridView.setAdapter(gridAdapter);
        /////////////////////////////////////////////////////////////////////////////////
        //////////////////////////////그리드 뷰 로드 끝//////////////////////////////////
        /////////////////////////////////////////////////////////////////////////////////

        super.onActivityCreated(savedInstanceState);
    }

    //해당 월에 표시할 일수 구함
    private void setCalendarDate(int month) {
        mCal.set(Calendar.MONTH, month - 1);
        for (int i = 0; i < mCal.getActualMaximum(Calendar.DAY_OF_MONTH); i++) {
            dayList.add("" + (i + 1));
        }
    }

    //////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////그리드 어댑터/////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////
    public class GridAdapter extends BaseAdapter {
        private final List<String> list;
        private final LayoutInflater inflater;

        public GridAdapter(Context context, List<String> list) {
            this.list = list;
            this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public String getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.item_calendar_gridview, parent, false);
                holder = new ViewHolder();
                holder.tvItemGridView = (TextView) convertView.findViewById(R.id.tv_item_gridview);
                holder.tvPlus = (TextView) convertView.findViewById(R.id.plus);
                holder.tvMinus = (TextView) convertView.findViewById(R.id.minus);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            //////////////////////나중에 데이터베이스로 부터 Benefit과 Loss를 가져와야 함///////////
            holder.tvItemGridView.setText("" + getItem(position));
            if(!getItem(position).equals("")) {
                holder.tvPlus.setText("Benefit");
                holder.tvMinus.setText("Loss");
            }
            //해당 날짜 텍스트 컬러,배경 변경
            mCal = Calendar.getInstance();

            //오늘 day 가져옴
            Integer today = mCal.get(Calendar.DAY_OF_MONTH);
            String sToday = String.valueOf(today);
            if (sToday.equals(getItem(position))) { //오늘 day 텍스트 컬러 변경
                holder.tvItemGridView.setTextColor(Color.BLACK);
            }
            return convertView;
        }
    }
    private class ViewHolder {
        TextView tvItemGridView;
        TextView tvPlus;
        TextView tvMinus;
    }
}