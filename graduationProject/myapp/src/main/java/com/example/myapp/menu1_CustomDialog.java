package com.example.myapp;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;

public class menu1_CustomDialog {
    private Context context;

    public menu1_CustomDialog(Context context){
        this.context=context;
    }
    // 호출할 다이얼로그 함수를 정의한다.
    public void callFunction(final int position) { //파라미터에 textview와 같은것을 넘겨받을수  잇음

        // 커스텀 다이얼로그를 정의하기위해 Dialog클래스를 생성한다.
        final Dialog dlg = new Dialog(context);

        // 액티비티의 타이틀바를 숨긴다.
        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE);

        // 커스텀 다이얼로그의 레이아웃을 설정한다.
        dlg.setContentView(R.layout.menu1_customdialog);

        // 커스텀 다이얼로그 사이즈를 조정한다.
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dlg.getWindow().getAttributes());
        lp.width=WindowManager.LayoutParams.MATCH_PARENT;
        lp.height=WindowManager.LayoutParams.WRAP_CONTENT;
        Window window = dlg.getWindow();
        window.setAttributes(lp);

        // 커스텀 다이얼로그를 노출한다.
        dlg.show();

        // 커스텀 다이얼로그의 각 위젯들을 정의한다.
        final Button okButton = (Button) dlg.findViewById(R.id.okButton);
        final Button cancelButton = (Button) dlg.findViewById(R.id.cancelButton);
        final TextView d_name = (TextView) dlg.findViewById(R.id.detail_name);
        final TextView d_price = (TextView) dlg.findViewById(R.id.detail_price);
        final TextView d_categori = (TextView) dlg.findViewById(R.id.detail_categori);
        final TextView d_output = (TextView) dlg.findViewById(R.id.detail_output);
        final TextView d_input = (TextView) dlg.findViewById(R.id.detail_input);
        final TextView d_time = (TextView) dlg.findViewById(R.id.detail_time);

        DecimalFormat myFormatter = new DecimalFormat("###,###");

        d_name.setText(menu1_consumption_Data.nameArray[position]);
        if(menu1_consumption_Data.priceArray_[position]>=0)
            d_price.setText("+"+myFormatter.format(menu1_consumption_Data.priceArray_[position])+" 원");
        else
            d_price.setText(myFormatter.format(menu1_consumption_Data.priceArray_[position])+" 원");
        //d_categori.setText("");
        d_output.setText(menu1_consumption_Data.detailArray[position]);
        d_input.setText(menu1_consumption_Data.nameArray[position]);
        d_time.setText(menu1_consumption_Data.yearArray[position]+"/"+menu1_consumption_Data.monthArray[position]+"/"+menu1_consumption_Data.dayArray[position]+"/"
                +menu1_consumption_Data.hourArray[position]+":"+menu1_consumption_Data.minuteArray[position]);

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
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "취소 했습니다.", Toast.LENGTH_SHORT).show();

                // 커스텀 다이얼로그를 종료한다.
                dlg.dismiss();
            }
        });
    }
}
