package com.example.myapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
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

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class menu1_fragment_tab1 extends Fragment {

    String userID;
    DecimalFormat myFormatter;
    int month = 3;
    int year = 2019;
    int day = 1;
    int startDay = 0;
    int lastDay =31;
    int monthlyBenefit = 0, monthlyLoss = 0;

    ImageButton btn_previous;
    ImageButton btn_next;
    TextView txt_present;
    TextView txt_year;
    Button btn_benefit;
    Button btn_loss;
    ImageButton btn_receipt;

    private GridAdapter gridAdapter;
    private ArrayList<String> dayList;
    private ArrayList<String> benefitList;
    private ArrayList<String> lossList;

    private GridView gridView;
    private Calendar mCal;
    public menu1_fragment_tab1() {    }


    String[] hDate;
    String[] hType;
    String[] hValue;
    String[] hName;
    String[] aBalance;
    String[] cType;
    String[] cName;
    String[] aType;
    int arrLength;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        myFormatter = new DecimalFormat("###,###");
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override //child fragment 구현
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        RelativeLayout layout = (RelativeLayout) inflater.inflate(R.layout.menu1_fragment_tab1,
                container, false);

        if(getArguments() != null){
            userID = getArguments().getString("ID");
            Log.i("nkw","menu1_tab1_userID="+userID);
        }
        return layout;
    }

    public void request_test(){
        /////////////////////////////////
        //요청 정보 입력
        int request_year=year, request_month=month+1;
        if((request_month)==13){
            request_year=year+1;
            request_month=1;
        }

        HistoryRequest test = new HistoryRequest(
                userID,                          //현재 로그인 아이디
                year+"-"+month+"-1",                       //요청할 해당 달의 시작 날짜
                request_year+"-"+request_month+"-1",       //요청할 해당 다음달의 시작 날짜
                RequestInfo.RequestType.ACCOUNT_HISTORY,   //내역 요청 할때 고정으로 쓰시면되여
                getContext());                             //이것두 고정이요


        //Request 함수 호출해서 정보 accountHistoryInfo 객체와 dailyHistoryInfo 객체에서 받아와서 사용
        test.Request((HistoryInfo[] historyInfo, DailyHistoryInfo[] dailyHistoryInfo) -> {
            arrLength = historyInfo.length;

            hDate = new String[arrLength];
            hType = new String[arrLength];
            hValue = new String[arrLength];
            hName = new String[arrLength];
            aBalance = new String[arrLength];
            cType = new String[arrLength];
            cName = new String[arrLength];
            aType = new String[arrLength];

            for(int i = 0; i < arrLength; i++){

                hDate[i] = historyInfo[i].gethDate();        //내역 사용 날짜
                hType[i] = historyInfo[i].gethType();        //내역 사용 타입 => 입금 / 출금 / 카드
                hValue[i] = historyInfo[i].gethValue();      //내역 사용 금액
                hName[i] = historyInfo[i].gethName();        //내역 사용 처 이름
                aBalance[i] = historyInfo[i].getaBalance();  //내역 사용 후 잔액
                cType[i] = historyInfo[i].getcType();        //카드 이름
                cName[i] = historyInfo[i].getcName();        //카테고릐 분류
                aType[i] = historyInfo[i].getaType();        // 계좌 이름
            }


            monthlyBenefit=0;
            monthlyLoss=0;

            String[] day_ ;
            String[] dailyBenefit;
            String[] dailyLoss;

            int arrLength2 = dailyHistoryInfo.length;
            day_ = new String[arrLength2];
            dailyBenefit = new String[arrLength2];
            dailyLoss = new String[arrLength2];
            String[] arr1, arr2;

            arr1 = new String[lastDay];
            arr2 = new String[lastDay];

            for(int i = 0; i < arrLength2; i++){
                day_[i] = dailyHistoryInfo[i].getDay();                      //일
                dailyBenefit[i] = dailyHistoryInfo[i].getDailyBenefit();    //수익
                dailyLoss[i] = dailyHistoryInfo[i].getDailyLoss();          //지출

                monthlyBenefit+=Integer.parseInt(dailyBenefit[i]);  //월 수익
                monthlyLoss+=Integer.parseInt(dailyLoss[i]);        //월 지출
            }

            btn_benefit.setText("+"+myFormatter.format(monthlyBenefit)+"원");
            btn_loss.setText("-"+myFormatter.format(monthlyLoss) +"원");

            //위에 처럼 각각 DailyHistoryInfo 에는 각각 정보들 get으로 얻어서 사용하시면 되요
            for (int i = 0; i < lastDay; i++) {
                for (int j = 0; j < arrLength2; j++) {
                    arr1[i] = "0";
                    arr2[i] = "0";
                }
            }
            int date1,date2;
            for (int i = 0; i <lastDay; i++) {
                for(int j=0; j<arrLength2; j++){
                    if(Integer.parseInt(dailyBenefit[j])!=0){
                        date1=Integer.parseInt(day_[j]);
                        arr1[date1-1]=dailyBenefit[j];
                    }
                    if(Integer.parseInt(dailyLoss[j])!=0){
                        date2=Integer.parseInt(day_[j]);
                        arr2[date2-1]=dailyLoss[j];
                    }
                }
            }

            //1일 - 요일 매칭 시키기 위해 공백 add
            for (int i = 1; i < startDay; i++) {
                dayList.add("");
                benefitList.add("");
                lossList.add("");
            }
            //해당 월에 표시할 일수 구함
            for (int i = 0; i < lastDay; i++) {
                dayList.add("" + (i + 1));
                benefitList.add(""+arr1[i]);
                lossList.add(""+arr2[i]);
            }
            Log.i("nkw","월: "+month+", 마지막 날 : "+Calendar.DAY_OF_MONTH+","+mCal.getActualMaximum(Calendar.DAY_OF_MONTH));
            //그리드 뷰 설정 및 표시
            gridAdapter = new GridAdapter(getActivity(), dayList, benefitList, lossList);
            gridView.setAdapter(gridAdapter);
        });
        ///////////////////////////////////////////////
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        btn_previous=(ImageButton)getView().findViewById(R.id.previous_month);
        btn_next=(ImageButton)getView().findViewById(R.id.next_month);
        txt_present=(TextView)getView().findViewById(R.id.present_month);
        txt_year=(TextView)getView().findViewById(R.id.present_year);
        btn_benefit=(Button)getView().findViewById(R.id.result_benefit_btn);
        btn_loss=(Button)getView().findViewById(R.id.result_loss_btn);
        btn_receipt = (ImageButton)getView().findViewById(R.id.receipt_btn);
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
        benefitList = new ArrayList<String>();
        lossList = new ArrayList<String>();
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
        //월 마지막 날
        lastDay = mCal.getActualMaximum(Calendar.DAY_OF_MONTH);
        request_test();


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

                mCal = Calendar.getInstance();
                mCal.set(year, month-1, 1);
                startDay = mCal.get(Calendar.DAY_OF_WEEK);
                lastDay = mCal.getActualMaximum(Calendar.DAY_OF_MONTH);

                dayList=new ArrayList<>();
                benefitList=new ArrayList<>();
                lossList=new ArrayList<>();
                request_test();
            }
        });
        btn_next.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) { //next month
                if((year==Integer.parseInt(curYearFormat.format(date)))&&
                        (month == Integer.parseInt(curMonthFormat.format(date)))){
                    year=Integer.parseInt(curYearFormat.format(date));
                    month=Integer.parseInt(curMonthFormat.format(date));
                }else if (month == 12) {
                    month =1;
                    year+=1;
                }
                else{
                    month+=1;
                }
                txt_present.setText(month + "월");
                txt_year.setText(year + "년");

                mCal = Calendar.getInstance();
                mCal.set(year, month-1, 1);
                startDay = mCal.get(Calendar.DAY_OF_WEEK);
                lastDay = mCal.getActualMaximum(Calendar.DAY_OF_MONTH);

                dayList=new ArrayList<>();
                benefitList=new ArrayList<>();
                lossList=new ArrayList<>();

                request_test();
            }
        });

        ///그리드뷰 아이템 버튼 클릭시
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                day = (int)id - startDay + 2;  //해당 날짜

                String datetime = year+"-";
                if(month<10)
                    datetime+="0"+month;
                else
                    datetime+=month;

                datetime+="-";
                if(day<10)
                    datetime+="0"+day;
                else
                    datetime+=day;

                int startNum = -1;
                int endNum = -1;

                Log.i("aaaa", Integer.toString(arrLength));
                for(int i=0; i<arrLength; i++) {
                    if(startNum==-1 && hDate[i].contains(datetime)) {
                        startNum = i;
                        Log.i("aa", hDate[i]);
                    }

                    if(startNum!=-1 &&  !hDate[i].contains(datetime)) {
                        endNum = i;
                        Log.i("bb", hDate[i]);
                        break;
                    }
                }

                if(endNum == -1)
                    endNum = arrLength;

                if(startNum == -1)
                    Toast.makeText(getActivity(),"해당 날짜는 거래내역이 없습니다.",Toast.LENGTH_LONG).show();
                else {
                    String benefit = benefitList.get(position);
                    String loss = lossList.get(position);
                    Intent intent = new Intent(getActivity(), menu1_calendar_popup.class);
                    intent.putExtra("hDate", hDate);
                    intent.putExtra("hName", hName);
                    intent.putExtra("hType", hType);
                    intent.putExtra("hValue", hValue);
                    intent.putExtra("aBalance", aBalance);
                    intent.putExtra("cType", cType);
                    intent.putExtra("cName", cName);
                    intent.putExtra("aType", aType);
                    intent.putExtra("startNum", startNum);
                    intent.putExtra("endNum", endNum);
                    intent.putExtra("benefit", benefit);
                    intent.putExtra("loss", loss);
                    startActivityForResult(intent, 1);
                }
//                Log.i("NKW",startNum +" "+endNum);
//
//                Log.i("NKW","id : "+id+" startDay:"+startDay+" day:"+day);
//                Toast.makeText(getActivity(),""+year+"년 "+month+"월 "+day +"일 입니다.",Toast.LENGTH_LONG).show();
//                alertDialog(position);
            }
        });

        //benefit,loss 버튼 클릭시
        //월별 총 수입과 지출 금액 디비에서 받아오기
        btn_benefit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(btn_benefit.getText().equals("benefit")){
                    btn_benefit.setTextSize(13);
                    btn_benefit.setText("+"+myFormatter.format(monthlyBenefit)+"원");

                    gridAdapter = new GridAdapter(getActivity(), dayList, benefitList,lossList);
                    gridView.setAdapter(gridAdapter);
                }
                else{
                    btn_benefit.setTextSize(15);
                    btn_benefit.setText("benefit");

                    gridAdapter = new GridAdapter(getActivity(), dayList, benefitList,lossList);
                    gridView.setAdapter(gridAdapter);
                }

            }
        });

        btn_loss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(btn_loss.getText().equals("loss")){
                    btn_loss.setTextSize(13);
                    btn_loss.setText("-"+myFormatter.format(monthlyLoss)+"원");

                    gridAdapter = new GridAdapter(getActivity(), dayList, benefitList,lossList);
                    gridView.setAdapter(gridAdapter);
                }
                else {
                    btn_loss.setTextSize(15);
                    btn_loss.setText("loss");

                    gridAdapter = new GridAdapter(getActivity(), dayList, benefitList,lossList);
                    gridView.setAdapter(gridAdapter);
                }
            }
        });

        btn_receipt.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent;
                if(android.os.Build.VERSION.SDK_INT >= 26) {
                    intent = new Intent(getActivity(), AddReceiptActivity.class);
                    intent.putExtra("userID", userID);
                    intent.putExtra("receiptFlag", "T");
                    startActivityForResult(intent, 1);
                }
                else {
                    intent = new Intent(getActivity(), AddReceiptActivity.class);
                    intent.putExtra("userID", userID);
                    intent.putExtra("receiptFlag", "F");

                    Toast.makeText(getContext(), "해당 기능을 사용할 수 없는 기기 입니다.\n수기로 입력해주세요", Toast.LENGTH_SHORT).show();
                    startActivityForResult(intent, 1);
                }

            }
        });

        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==1){
            if(resultCode==1){
                Log.i("CHJ","영수증 추가 완료");
                request_test();
            }
        }
    }
    // 팝업창 다이얼로그
    public void alertDialog(int position) {

        final String url = "https://www.naver.com";

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(""+year+"년 "+month+"월 "+day +"일");
        builder.setMessage("수입은 "+benefitList.get(position) +"원 입니다 \n지출은 "+ lossList.get(position) + "원 입니다 ");
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
        private final List<String> benefitList;
        private final List<String> lossList;


        public GridAdapter(Context context, List<String> list, List<String> benefitList, List<String> lossList) {
            this.list = list;
            this.benefitList=benefitList;
            this.lossList=lossList;
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

            if(!getItem(position).equals("") && !benefitList.get(position).equals("null") && !lossList.get(position).equals("null")) {
                if((Integer.parseInt(benefitList.get(position))!=0) || (Integer.parseInt(lossList.get(position))!=0)){
                    holder.tvPlus.setText(""+myFormatter.format(Integer.parseInt(benefitList.get(position))) );
                    holder.tvMinus.setText(""+myFormatter.format(Integer.parseInt(lossList.get(position))) );
                }
                //버튼 눌럿을때 수입 지출 보이게 하는 조건
                if(!btn_benefit.getText().equals("benefit")){
                    holder.tvPlus.setVisibility(View.VISIBLE);
                }
                else {
                    holder.tvPlus.setVisibility(View.INVISIBLE);
                }
                if(!btn_loss.getText().equals("loss")){
                    holder.tvMinus.setVisibility(View.VISIBLE);
                }
                else{
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