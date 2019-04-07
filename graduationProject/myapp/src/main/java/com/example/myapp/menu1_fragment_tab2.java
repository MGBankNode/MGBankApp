package com.example.myapp;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class menu1_fragment_tab2 extends Fragment {
    ListView listView;
    List<BindData> list;

    int date = 0;
    int month = 3;
    int year = 2019;
    ImageButton btn_previous;
    ImageButton btn_next;
    TextView txt_present;
    TextView txt_year;

    int todayBenefit=0, todayLoss=0;

    public menu1_fragment_tab2(){    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        RelativeLayout layout = (RelativeLayout)inflater.inflate(R.layout.menu1_fragment_tab2,
                container, false);
        return layout;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        listView = (ListView)getView().findViewById(R.id.list_view);
        btn_previous=(ImageButton)getView().findViewById(R.id.previous_month);
        btn_next=(ImageButton)getView().findViewById(R.id.next_month);
        txt_present=(TextView)getView().findViewById(R.id.present_month);
        txt_year=(TextView)getView().findViewById(R.id.present_year);

        //오늘 연도, 월
        final Date date = new Date(System.currentTimeMillis());
        final SimpleDateFormat curYearFormat = new SimpleDateFormat("yyyy", Locale.KOREA);
        final SimpleDateFormat curMonthFormat = new SimpleDateFormat("MM", Locale.KOREA);
        year =Integer.parseInt(curYearFormat.format(date));
        month = Integer.parseInt(curMonthFormat.format(date));
        txt_present.setText(month + "월");

        // 인덱스 데이터를 추가
        list = new ArrayList<BindData>();
        for (int i = 0; i < INDEX_DATA.length; i++) {
            list.add(INDEX_DATA[i]);
        }

        // 인덱스 표시 어댑터 설정
        DataAdapter adapter = new DataAdapter(getActivity(), list);
        // 어댑터를 설정
        listView.setAdapter(adapter);

        //월 이동 버튼 누를 때
        btn_previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { //last month
                if (month == 1) {
                    month = 12;
                    year -= 1;
                } else {
                    month -= 1;
                }
                txt_present.setText(month + "월");
                txt_year.setText(year + "년");
                // 인덱스 표시 어댑터 설정
                DataAdapter adapter = new DataAdapter(getActivity(), list);
                // 어댑터를 설정
                listView.setAdapter(adapter);
            }
        });
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { //next month
                if (month == 12) {
                    month = 1;
                    year += 1;
                } else {
                    month += 1;
                }
                txt_present.setText(month + "월");
                txt_year.setText(year + "년");
                // 인덱스 표시 어댑터 설정
                DataAdapter adapter = new DataAdapter(getActivity(), list);
                // 어댑터를 설정
                listView.setAdapter(adapter);
            }
        });

        super.onActivityCreated(savedInstanceState);
    }

    private class DataAdapter extends ArrayAdapter<BindData> {
        private LayoutInflater mInflater;

        public DataAdapter(Context context, List<BindData> objects) {
            super(context, 0, objects);
            this.mInflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public boolean isEnabled(int position) {
            // 선택 불가능하게 함
            return true;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            //금액 천단위 콤마 표시 선언
            DecimalFormat myFormatter = new DecimalFormat("###,###");
            ViewHolder viewHolder;

            // 리스트 아이템 표시용 레이아웃을 읽기 생성
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.menu1_list_item, null);

                // 리스트의 표시를 고속화하기 위해 View 저장용 클래스를 생성해 Tag로 설정
                viewHolder = new ViewHolder();
                viewHolder.date = (TextView) convertView.findViewById(R.id.list_date);
                viewHolder.hour = (TextView) convertView.findViewById(R.id.hh);
                viewHolder.minute = (TextView) convertView.findViewById(R.id.mm);
                viewHolder.name = (TextView) convertView.findViewById(R.id.name);
                viewHolder.card = (TextView) convertView.findViewById(R.id.user_card);
                viewHolder.price = (TextView) convertView.findViewById(R.id.price);

                viewHolder.list_Layout1 = (LinearLayout) convertView.findViewById(R.id.list_layout1);
                viewHolder.list_Layout2 = (LinearLayout) convertView.findViewById(R.id.list_layout2);
                viewHolder.list_Layout3 = (LinearLayout) convertView.findViewById(R.id.list_layout3);
                viewHolder.day_price = (LinearLayout) convertView.findViewById(R.id.day_price);
                viewHolder.today_benefit = (TextView) convertView.findViewById(R.id.day_benefit);
                viewHolder.today_loss = (TextView) convertView.findViewById(R.id.day_loss);
                viewHolder.ingredient = (Button) convertView.findViewById(R.id.ingredient_btn);
                convertView.setTag(viewHolder);
            } else {
                // View에 저장된 인스턴스를 Tag로부터 구함
                viewHolder = (ViewHolder) convertView.getTag();
            }

            // Adapter에 설정된 리스트로부터 BindData를 구함
            BindData data = getItem(position);


            if ((getItem(position).year == year) && (getItem(position).month == month)){    // 텍스트뷰의 연월 비교
                if (getItem(position).date != date) {   //하루 단위를 끊는 역할
                    // 인덱스용의 인덱스 데이터라면 인덱스 타이틀을 표시
                    date = getItem(position).date;
                    viewHolder.day_price.setVisibility(View.VISIBLE);
                    viewHolder.list_Layout1.setVisibility(View.VISIBLE);
                    viewHolder.list_Layout2.setVisibility(View.VISIBLE);
                    viewHolder.list_Layout3.setVisibility(View.VISIBLE);
                    viewHolder.ingredient.setVisibility(View.VISIBLE);

                    viewHolder.date.setVisibility(View.VISIBLE);
                    viewHolder.date.setText(Integer.toString(data.date));
                    viewHolder.hour.setVisibility(View.VISIBLE);
                    viewHolder.hour.setText(data.hour);
                    viewHolder.minute.setVisibility(View.VISIBLE);
                    viewHolder.minute.setText(data.minute);
                    viewHolder.name.setVisibility(View.VISIBLE);
                    viewHolder.name.setText(data.name);
                    viewHolder.card.setVisibility(View.VISIBLE);
                    viewHolder.card.setText(data.card);
                    if (data.price < 0) {
                        viewHolder.ingredient.setText("loss");
                        viewHolder.ingredient.setBackgroundResource(R.drawable.loss_label);
                        viewHolder.ingredient.setTextColor(Color.RED);
                        viewHolder.price.setTextColor(Color.RED);
                        todayLoss = data.price;
                    } else {
                        viewHolder.ingredient.setText("benefit");
                        viewHolder.ingredient.setBackgroundResource(R.drawable.benefit_label);
                        viewHolder.ingredient.setTextColor(Color.rgb(60, 193, 238));
                        viewHolder.price.setTextColor(Color.rgb(60, 193, 238));
                        todayBenefit = data.price;
                    }
                    viewHolder.today_benefit.setText(myFormatter.format(todayBenefit));
                    viewHolder.today_loss.setText(myFormatter.format(todayLoss));

                    viewHolder.price.setVisibility(View.VISIBLE);
                    viewHolder.price.setText(myFormatter.format(data.price) + "원");
                } else {
                    // 인덱스용의 데이터에 없는 경우는 텍스트만 표시
                    viewHolder.day_price.setVisibility(View.GONE);
                    viewHolder.list_Layout1.setVisibility(View.VISIBLE);
                    viewHolder.list_Layout2.setVisibility(View.VISIBLE);
                    viewHolder.list_Layout3.setVisibility(View.VISIBLE);
                    viewHolder.ingredient.setVisibility(View.VISIBLE);

                    viewHolder.hour.setVisibility(View.VISIBLE);
                    viewHolder.hour.setText(data.hour);
                    viewHolder.minute.setVisibility(View.VISIBLE);
                    viewHolder.minute.setText(data.minute);
                    viewHolder.name.setVisibility(View.VISIBLE);
                    viewHolder.name.setText(data.name);
                    viewHolder.card.setVisibility(View.VISIBLE);
                    viewHolder.card.setText(data.card);
                    if (data.price < 0) {
                        viewHolder.ingredient.setText("loss");
                        viewHolder.ingredient.setBackgroundResource(R.drawable.loss_label);
                        viewHolder.ingredient.setTextColor(Color.RED);
                        viewHolder.price.setTextColor(Color.RED);
                    } else {
                        viewHolder.ingredient.setText("benefit");
                        viewHolder.ingredient.setBackgroundResource(R.drawable.benefit_label);
                        viewHolder.ingredient.setTextColor(Color.rgb(60, 193, 238));
                        viewHolder.price.setTextColor(Color.rgb(60, 193, 238));
                    }
                    viewHolder.today_benefit.setText(myFormatter.format(todayBenefit));
                    viewHolder.today_loss.setText(myFormatter.format(todayLoss));

                    viewHolder.price.setVisibility(View.VISIBLE);
                    viewHolder.price.setText(myFormatter.format(data.price) + "원");
                }
            }else {
                date=0;
                viewHolder.day_price.setVisibility(View.GONE);
                viewHolder.list_Layout1.setVisibility(View.GONE);
                viewHolder.list_Layout2.setVisibility(View.GONE);
                viewHolder.list_Layout3.setVisibility(View.GONE);
                viewHolder.ingredient.setVisibility(View.GONE);
            }
            return convertView;
        }
    }
    public class ViewHolder{
        TextView date, hour, minute, name, card, price, today_benefit, today_loss;
        LinearLayout day_price, list_Layout1,list_Layout2,list_Layout3;
        Button ingredient;
    }
    /***
     * 인덱스 데이터
     */
    private class BindData {
        int year,month, date;
        String hour, minute, name, card;
        int price;

        public BindData(int year, int month, int date, String hour, String minute, String name, String card, int price) {
            this.year=year;
            this.month = month;
            this.date = date;
            this.hour = hour;
            this.minute = minute;
            this.name = name;
            this.card = card;
            this.price = price;
        }
    }

    // 인덱스를 표시하기 위한 샘플 데이터---- 디비연동해서 계좌 내역 받아오기
    //디비에서 월별로 따로 불러와야 할듯 ----> 월 변경시 dividerheight에 의해 공백 생김
    //각 날마다 그 날의 수입과 지출 합계를 보여줄수 있는 테이블 필요 -> 리스트뷰에서 연산하려니 너무 복잡하게 꼬임
    private BindData[] INDEX_DATA = new BindData[] {
            new BindData(2019,4,8,"13","56","Node","새마을 금고 이체",-5000),
            new BindData(2019,4,8,"10","00","이자","새마을금고 급여통장",160),
            new BindData(2019,4,7,"09","21","남광우 계좌이체","새마을금고 급여통장",50000),
            new BindData(2019,4,1,"13","56","이삭토스트","kb카드",-26000),
            new BindData(2019,3,12,"13","56","Node","새마을 금고 이체",-5000),
            new BindData(2019,3,12,"10","00","이자","새마을금고 급여통장",160),
            new BindData(2019,3,11,"09","21","광운대 장학금","새마을금고 급여통장",1500000),
            new BindData(2019,3,10,"13","56","이삭토스트","kb카드",-26000),
            new BindData(2019,3,10,"10","50","CU 광운대점","하나카드",-6520),
            new BindData(2019,3,10,"02","10","미니스톱 광운대점","신한카드",-4500),
            new BindData(2019,2,12,"10","00","이자","새마을금고 급여통장",160),
            new BindData(2019,2,11,"09","21","광운대 장학금","새마을금고 급여통장",1500000),
            new BindData(2019,2,10,"13","56","이삭토스트","kb카드",-26000),
            new BindData(2019,2,9,"13","56","이삭토스트","kb카드",-26000),
            new BindData(2019,2,8,"10","50","CU 광운대점","하나카드",-6520),
            new BindData(2019,2,8,"13","56","이삭토스트","kb카드",-26000),
            new BindData(2019,2,1,"10","50","CU 광운대점","하나카드",-6520),
            new BindData(2019,1,11,"09","21","광운대 장학금","새마을금고 급여통장",1500000),
            new BindData(2018,12,30,"10","50","CU 광운대점","하나카드",-6520),
            new BindData(2018,12,12,"10","00","이자","새마을금고 급여통장",160),
            new BindData(2018,12,11,"09","21","광운대 장학금","새마을금고 급여통장",1500000),
            new BindData(2018,12,10,"13","56","이삭토스트","kb카드",-26000),
            new BindData(2018,12,9,"13","56","이삭토스트","kb카드",-26000),
            new BindData(2018,12,8,"10","50","CU 광운대점","하나카드",-6520),
            new BindData(2018,12,8,"13","56","이삭토스트","kb카드",-26000),
            new BindData(2018,12,1,"10","50","CU 광운대점","하나카드",-6520)
    };

}
