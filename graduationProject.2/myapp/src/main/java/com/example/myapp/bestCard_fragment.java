package com.example.myapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class bestCard_fragment extends Fragment {

    public bestCard_fragment() {}

    ArrayList<cardElement> arraylist = new ArrayList<>();
    ImageView bestCardImage;
    TextView bestCardNmae;
    TextView bestCardMerit;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bestcard, container, false);


//    ㆍ

        arraylist.add(new cardElement(R.drawable.card_peach, "피치 체크카드", "영화ㆍ문화공연 에서 더 많은 혜택 을 받을 수 있어요!"));

        bestCardImage = view.findViewById(R.id.bestCardImage);
        bestCardNmae = view.findViewById(R.id.bestCardName);
        bestCardMerit = view.findViewById(R.id.bestCardMerit);

        bestCardImage.setImageResource(arraylist.get(0).getIcon());
        bestCardNmae.setText(Html.fromHtml("<u>" +arraylist.get(0).getName() + "</u>")); // 밑줄 긋기
        bestCardMerit.setText(arraylist.get(0).getMerit());



        bestCardImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog();
            }
        });

        return view;
    }

    public void alertDialog() {

        final String url = "https://mgcheck.kfcc.co.kr/pers/appl/persPeachGuid.do";

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("링크연결");
        builder.setMessage("더 자세한 혜택을 보시겠습니까?");
        builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(myIntent);
            }
        });

        builder.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.show();
    }
}
