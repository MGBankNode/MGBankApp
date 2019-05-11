package com.example.myapp;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class menu1_CustomDialog {
    private Context context;

    public menu1_CustomDialog(Context context){
        this.context=context;
    }

    // 호출할 다이얼로그 함수를 정의한다.
    public void callFunction(final int position, int year, int month) { //파라미터에 textview와 같은것을 넘겨받을수  잇음

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
        final Button okButton = (Button) dlg.findViewById(R.id.okButton);
        final TextView d_name = (TextView) dlg.findViewById(R.id.detail_name);
        final TextView d_price = (TextView) dlg.findViewById(R.id.detail_price);
        final TextView d_categori = (TextView) dlg.findViewById(R.id.detail_categori);
        final TextView d_output = (TextView) dlg.findViewById(R.id.detail_output);
        final TextView d_input = (TextView) dlg.findViewById(R.id.detail_input);
        final TextView d_time = (TextView) dlg.findViewById(R.id.detail_time);


        /////////////////////////////////
        //요청 정보 입력!!!!!!!test
        HistoryRequest test = new HistoryRequest(
                "b",                          //현재 로그인 아이디
                year+"-"+month+"-1",                         //요청할 해당 달의 시작 날짜
                year+"-"+month+"-31",                     //요청할 해당 달의 마지막 날짜
                RequestInfo.RequestType.ACCOUNT_HISTORY,   //내역 요청 할때 고정으로 쓰시면되여
                context);                             //이것두 고정이요


        //Request 함수 호출해서 정보 accountHistoryInfo 객체와 dailyHistoryInfo 객체에서 받아와서 사용
        test.Request(new HistoryRequest.VolleyCallback() {
            @Override
            public void onSuccess(HistoryInfo[] accountHistoryInfo, DailyHistoryInfo[] dailyHistoryInfo) {
                int arrLength = accountHistoryInfo.length;

                String[] hDate = new String[arrLength];
                String[] hType = new String[arrLength];
                String[] hValue = new String[arrLength];
                String[] hName = new String[arrLength];
                String[] aBalance = new String[arrLength];
                String[] cType = new String[arrLength];
                String[] cName = new String[arrLength];

                for(int i = 0; i < arrLength; i++){

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

                d_name.setText(hName[position]);
                if(hType[position].equals("입금"))
                    d_price.setText("+"+myFormatter.format(Integer.parseInt(hValue[position]))+" 원");
                else
                    d_price.setText("-"+myFormatter.format(Integer.parseInt(hValue[position]))+" 원");
                d_categori.setText(cName[position]);
                d_output.setText(cType[position]);
                d_input.setText(hName[position]);
                d_time.setText(hDate[position].substring(0,16));

            }
        });

        ///////////////////////////////////////////////
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
