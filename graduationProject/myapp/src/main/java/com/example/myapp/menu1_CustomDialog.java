package com.example.myapp;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class menu1_CustomDialog {
    private Context context;
    public ArrayList<String> arrayList;
    String categori;
    int prevCategory, curCategory;
    int hId_num;


    public menu1_CustomDialog(Context context){

        arrayList = new ArrayList<String>();
        arrayList.add("술/유흥");
        arrayList.add("생활(쇼핑)");
        arrayList.add("교통");
        arrayList.add("주거/통신");
        arrayList.add("의료/건강");
        arrayList.add("금융");
        arrayList.add("문화/여가");
        arrayList.add("여행/숙박");
        arrayList.add("식비");
        arrayList.add("카페/간식");
        arrayList.add("미분류");
        this.context=context;
    }

    // 호출할 다이얼로그 함수를 정의한다.
    public void callFunction(final int position, int year, int month, String userID) { //파라미터에 textview와 같은것을 넘겨받을수  잇음

        // 커스텀 다이얼로그를 정의하기위해 Dialog클래스를 생성한다.
        final Dialog dlg = new Dialog(context);

        // 액티비티의 타이틀바를 숨긴다.
        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE);

        // 커스텀 다이얼로그의 레이아웃을 설정한다.
        dlg.setContentView(R.layout.menu1_customdialog);
        dlg.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT)); //배경 투명하게

        // 커스텀 다이얼로그 사이즈를 조정한다.
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dlg.getWindow().getAttributes());
        lp.width=WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height=WindowManager.LayoutParams.WRAP_CONTENT;
        Window window = dlg.getWindow();
        window.setAttributes(lp);

        // 커스텀 다이얼로그를 노출한다.
        dlg.show();

        // 커스텀 다이얼로그의 각 위젯들을 정의한다.
        final Button editButton = (Button) dlg.findViewById(R.id.editButton);
        final Button okButton = (Button) dlg.findViewById(R.id.okButton);
        final EditText d_name = (EditText) dlg.findViewById(R.id.detail_name);
        final EditText d_price = (EditText) dlg.findViewById(R.id.detail_price);
        final TextView d_categori = (TextView) dlg.findViewById(R.id.detail_cate);
        final EditText d_output = (EditText) dlg.findViewById(R.id.detail_output);
        final EditText d_input = (EditText) dlg.findViewById(R.id.detail_input);
        final EditText d_time = (EditText) dlg.findViewById(R.id.detail_time);
        final Spinner spinner =(Spinner) dlg.findViewById(R.id.categorySpinner);


        /////////////////////////////////
        //요청 정보 입력!!!!!!!test
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
                context);                             //이것두 고정이요

        //Request 함수 호출해서 정보 accountHistoryInfo 객체와 dailyHistoryInfo 객체에서 받아와서 사용
        test.Request(new HistoryRequest.VolleyCallback() {
            @Override
            public void onSuccess(HistoryInfo[] accountHistoryInfo, DailyHistoryInfo[] dailyHistoryInfo) {
                int arrLength = accountHistoryInfo.length;

                int[] hId = new int[arrLength];
                String[] hDate = new String[arrLength];
                String[] hType = new String[arrLength];
                String[] hValue = new String[arrLength];
                String[] hName = new String[arrLength];
                String[] aBalance = new String[arrLength];
                String[] cType = new String[arrLength];
                String[] cName = new String[arrLength];

                for(int i = 0; i < arrLength; i++){

                    hId[i] = accountHistoryInfo[i].gethId();            //내역 사용 처 아이디
                    hDate[i] = accountHistoryInfo[i].gethDate();        //내역 사용 날짜
                    hType[i] = accountHistoryInfo[i].gethType();        //내역 사용 타입 => 입금 / 출금 / 카드
                    hValue[i] = accountHistoryInfo[i].gethValue();      //내역 사용 금액
                    hName[i] = accountHistoryInfo[i].gethName();        //내역 사용 처 이름
                    aBalance[i] = accountHistoryInfo[i].getaBalance();  //내역 사용 후 잔액
                    cType[i] = accountHistoryInfo[i].getcType();        //카드 이름
                    cName[i] = accountHistoryInfo[i].getcName();        //카테고릐 분류
                }
                //위에 처럼 각각 AccountHistoryInfo 에는 각각 정보들 get으로 얻어서 사용하시면 되요

                int arrLength2 = dailyHistoryInfo.length;
                String day[] = new String[arrLength2];
                String dailyBenefit[] = new String[arrLength2];
                String dailyLoss[] = new String[arrLength2];

                for(int i = 0; i < arrLength2; i++){

                    day[i] = dailyHistoryInfo[i].getDay();                      //일
                    dailyBenefit[i] = dailyHistoryInfo[i].getDailyBenefit();    //수익
                    dailyLoss[i] = dailyHistoryInfo[i].getDailyLoss();          //지출

                }

                //위에 처럼 각각 DailyHistoryInfo 에는 각각 정보들 get으로 얻어서 사용하시면 되요


                DecimalFormat myFormatter = new DecimalFormat("###,###");

                hId_num=hId[position];
                d_name.setText(hName[position]);
                if(hType[position].equals("입금"))
                    d_price.setText("+"+myFormatter.format(Integer.parseInt(hValue[position]))+" 원");
                else
                    d_price.setText("-"+myFormatter.format(Integer.parseInt(hValue[position]))+" 원");
                d_categori.setText(cName[position]);
                d_output.setText(cType[position]);
                d_input.setText(hName[position]);
                d_time.setText(hDate[position].substring(0,16));

                categori=cName[position];
                //카테고리 스피너
                SpinnerAdapter adapter = new SpinnerAdapter(context, arrayList);
                spinner.setAdapter(adapter);

                for(int i=0; i<arrayList.size();i++){
                    if(arrayList.get(i).equals(categori)){
                        spinner.setSelection(i);
                        prevCategory=i;
                        break;
                    }else{
                        spinner.setSelection(10);
                        prevCategory=10;
                    }
                }
                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        d_categori.setText(arrayList.get(i));
                        curCategory=i;
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) { }
                });
            }
        });

        ///////////////////////////////////////////////
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(context, "수정 모드로 전환합니다.", Toast.LENGTH_SHORT).show();

                if(editButton.getText().equals("수정")){
                    editButton.setText("적용");
/*                    d_name.setEnabled(true);
                    d_price.setEnabled(true);
                    d_output.setEnabled(true);
                    d_input.setEnabled(true);
                    d_time.setEnabled(true);*/
                    d_categori.setVisibility(View.GONE);
                    spinner.setVisibility(View.VISIBLE);
                }
                else{

                    // /////////////////////카테고리 변경 요청////////////////////////////
                    ///////////////////////////////////////////////////////////////////
                    CategoryRequest categoryRequest = new CategoryRequest(
                            userID,                    //사용자 아이디
                            hId_num,       //사용처 이름
                            prevCategory,                  //기존 카테고리 번호
                            curCategory,                  //바꿀 카테고리 번호
                            context,                   //context 고정
                            RequestInfo.RequestType.UPDATE_CATEGORY);   //고정

                    categoryRequest.UpdateCategoryHandler(new CategoryRequest.VolleyCallback() {
                        @Override
                        public void onSuccess() {
                            editButton.setText("수정");
                            d_name.setEnabled(false);
                            d_price.setEnabled(false);
                            d_output.setEnabled(false);
                            d_input.setEnabled(false);
                            d_time.setEnabled(false);
                            d_categori.setVisibility(View.VISIBLE);
                            spinner.setVisibility(View.GONE);
                        }

                        @Override
                        public void onFail() {

                        }

                        @Override
                        public void onError() {

                        }
                    });
                    ///////////////////////////////////////////////////////////////////
                    ///////////////////////////////////////////////////////////////////

                }

            }
        });
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // '확인' 버튼 클릭시 메인 액티비티에서 설정한 main_label에
                // 커스텀 다이얼로그에서 입력한 메시지를 대입한다.
                //main_label.setText(message.getText().toString());

                // 커스텀 다이얼로그를 종료한다.
                dlg.dismiss();
            }
        });

    }
}
