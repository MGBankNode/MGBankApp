package com.example.myapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Layout;
import android.util.Log;
import android.util.Size;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class menu1_fragment_tab1 extends Fragment {

    int month = 3;
    int year = 2019;
    int day = 1;
    int startDay = 0;
    ImageButton btn_previous;
    ImageButton btn_next;
    TextView txt_present;
    TextView txt_year;
    Button btn_benefit;
    Button btn_loss;

    private GridAdapter gridAdapter;
    private ArrayList<String> dayList;
    private GridView gridView;
    private Calendar mCal;
    public menu1_fragment_tab1() {    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override //child fragment 구현
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        RelativeLayout layout = (RelativeLayout) inflater.inflate(R.layout.menu1_fragment_tab1,
                container, false);
        return layout;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        btn_previous=(ImageButton)getView().findViewById(R.id.previous_month);
        btn_next=(ImageButton)getView().findViewById(R.id.next_month);
        txt_present=(TextView)getView().findViewById(R.id.present_month);
        txt_year=(TextView)getView().findViewById(R.id.present_year);
        btn_benefit=(Button)getView().findViewById(R.id.result_benefit_btn);
        btn_loss=(Button)getView().findViewById(R.id.result_loss_btn);

        /////////////////////////////그리드 뷰 로드//////////////////////////////////////
        gridView = (GridView)getView().findViewById(R.id.gridview);
        //오늘날짜 설정
        long now = System.currentTimeMillis();
        final Date date = new Date(now);

        //연,월,일을 따로 저장
        final SimpleDateFormat curYearFormat = new SimpleDateFormat("yyyy", Locale.KOREA);
        final SimpleDateFormat curMonthFormat = new SimpleDateFormat("MM", Locale.KOREA);
        final SimpleDateFormat curDayFormat = new SimpleDateFormat("dd", Locale.KOREA);

        //dayList 데이터 생성
        dayList = new ArrayList<String>();
        mCal = Calendar.getInstance();

        //현재 날짜 텍스트뷰에 설정
        year =Integer.parseInt(curYearFormat.format(date));
        month = Integer.parseInt(curMonthFormat.format(date));
        day = Integer.parseInt(curDayFormat.format(date));

        txt_present.setText(month + "월");
        txt_year.setText(year + "년");

        //달력 년,월,일 세팅
        mCal.set(year, month-1, 1);
        //월 시작 요일
        startDay = mCal.get(Calendar.DAY_OF_WEEK);

        //1일 - 요일 매칭 시키기 위해 공백 add
        for (int i = 1; i < startDay; i++) {
            dayList.add("");
        }
        setCalendarDate();
        //그리드 뷰 설정 및 표시
        gridAdapter = new GridAdapter(getActivity(), dayList);
        gridView.setAdapter(gridAdapter);

        //월 이동 버튼 누를 때
        btn_previous.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) { //last month
                if(month == 1){
                    month =12;
                    year -=1;
                }
                else{
                    month-=1;
                }
                txt_present.setText(month + "월");
                txt_year.setText(year + "년");

                mCal.set(year, month-1, 1);
                startDay = mCal.get(Calendar.DAY_OF_WEEK);
                dayList=new ArrayList<>();
                //1일 - 요일 매칭 시키기 위해 공백 add
                for (int i = 1; i < startDay; i++) {
                    dayList.add("");
                }
                setCalendarDate();
                gridAdapter = new GridAdapter(getActivity(), dayList);
                gridView.setAdapter(gridAdapter);
            }
        });
        btn_next.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) { //next month
                if(month == 12){
                    month =1;
                    year+=1;
                }
                else{
                    month+=1;
                }
                txt_present.setText(month + "월");
                txt_year.setText(year + "년");

                mCal.set(year, month-1, 1);
                startDay = mCal.get(Calendar.DAY_OF_WEEK);
                dayList=new ArrayList<>();
                //1일 - 요일 매칭 시키기 위해 공백 add
                for (int i = 1; i < startDay; i++) {
                    dayList.add("");
                }
                setCalendarDate();
                gridAdapter = new GridAdapter(getActivity(), dayList);
                gridView.setAdapter(gridAdapter);
            }
        });

        ///그리드뷰 아이템 버튼 클릭시
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                day = (int)id - startDay + 2;  //해당 날짜
                Log.i("NKW","id : "+id+" startDay:"+startDay+" day:"+day);
                Toast.makeText(getActivity(),""+year+"년 "+month+"월 "+day +"일 입니다.",Toast.LENGTH_LONG).show();
                alertDialog();
            }
        });

        //benefit,loss 버튼 클릭시
        //월별 총 수입과 지출 금액 디비에서 받아오기
        btn_benefit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(btn_benefit.getText().equals("benefit")){
                    btn_benefit.setTextSize(13);
                    btn_benefit.setText("+"+"1,500,000"+"원");

                    gridAdapter = new GridAdapter(getActivity(), dayList);
                    gridView.setAdapter(gridAdapter);
                }
                else{
                    btn_benefit.setTextSize(15);
                    btn_benefit.setText("benefit");

                    gridAdapter = new GridAdapter(getActivity(), dayList);
                    gridView.setAdapter(gridAdapter);
                }

            }
        });

        btn_loss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(btn_loss.getText().equals("loss")){
                    btn_loss.setTextSize(13);
                    btn_loss.setText("-1,000,000"+"원");

                    gridAdapter = new GridAdapter(getActivity(), dayList);
                    gridView.setAdapter(gridAdapter);
                }
                else {
                    btn_loss.setTextSize(15);
                    btn_loss.setText("loss");

                    gridAdapter = new GridAdapter(getActivity(), dayList);
                    gridView.setAdapter(gridAdapter);
                }
            }
        });

        super.onActivityCreated(savedInstanceState);
    }

    //해당 월에 표시할 일수 구함
    private void setCalendarDate() {
        for (int i = 0; i < mCal.getActualMaximum(Calendar.DAY_OF_MONTH); i++) {
            dayList.add("" + (i + 1));
        }
    }

    // 팝업창 다이얼로그
    public void alertDialog() {

        final String url = "https://www.naver.com";

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(""+year+"년 "+month+"월 "+day +"일");
        builder.setMessage("수입은 123456789 원 입니다 \n지출은 987654321 원 입니다 ");
        builder.setPositiveButton("수정", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(myIntent);
            }
        });

        builder.setNegativeButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
//                Toast.makeText(getContext(), "아니오를 선택했습니다", Toast.LENGTH_SHORT).show();
            }
        });
        builder.show();
    }

    ////////////////// 그리드뷰 어댑터
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
            final ViewHolder holder;
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.item_calendar_gridview, parent, false);
                holder = new ViewHolder();

                holder.ItemLayout = (LinearLayout)convertView.findViewById(R.id.item_gridview_layout);
                holder.tvItemGridView = (TextView) convertView.findViewById(R.id.tv_item_gridview);
                holder.tvPlus = (TextView) convertView.findViewById(R.id.plus);
                holder.tvMinus = (TextView) convertView.findViewById(R.id.minus);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            //나중에 데이터베이스로 부터 Benefit과 Loss를 가져와야 함
            //position에 day를 대입해서 디비 연동하면 될듯

            holder.tvItemGridView.setText("" + getItem(position));
            if(!getItem(position).equals("")) {
                holder.tvPlus.setText("Benefit");
                holder.tvMinus.setText("Loss");
                //버튼 눌럿을때 수입 지출 보이게 하는 조건
                if(!btn_benefit.getText().equals("benefit") && !btn_loss.getText().equals("loss")){
                    holder.tvPlus.setVisibility(View.VISIBLE);
                    holder.tvMinus.setVisibility(View.VISIBLE);
                }else if(!btn_benefit.getText().equals("benefit") && btn_loss.getText().equals("loss")){
                    holder.tvPlus.setVisibility(View.VISIBLE);
                    holder.tvMinus.setVisibility(View.INVISIBLE);
                }
                else if(btn_benefit.getText().equals("benefit") && !btn_loss.getText().equals("loss")){
                    holder.tvPlus.setVisibility(View.INVISIBLE);
                    holder.tvMinus.setVisibility(View.VISIBLE);
                }else{
                    holder.tvPlus.setVisibility(View.INVISIBLE);
                    holder.tvMinus.setVisibility(View.INVISIBLE);
                }
            }
            //해당 날짜 텍스트 컬러,배경 변경
            mCal = Calendar.getInstance();

            //오늘 day 가져옴
            Integer today = mCal.get(Calendar.DAY_OF_MONTH);
            int curMonth = mCal.get(Calendar.MONTH); //1월=0; 2월=1;...
            String sToday = String.valueOf(today);
            if (sToday.equals(getItem(position)) && (curMonth == month-1)) { //오늘 day 텍스트 컬러 변경
                holder.tvItemGridView.setTextColor(Color.WHITE);
                holder.tvItemGridView.setBackgroundResource(R.drawable.griditem_label);
            }


            return convertView;
        }
    }
    private class ViewHolder {
        LinearLayout ItemLayout;
        TextView tvItemGridView;
        TextView tvPlus;
        TextView tvMinus;
    }


}