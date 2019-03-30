package com.example.myapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class fragment_pointcard extends Fragment {
    public fragment_pointcard(){}

    ImageButton imgbutton;
    int point_id;
    ImageView point_logo;
    TextView point_name;
    TextView point_score;
    CardView point_card;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pointcard, container, false);

        if(getArguments() != null){
            point_id = getArguments().getInt("point_id");
            Log.i("NKW","point_id = "+point_id);
        }
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        imgbutton = (ImageButton)getActivity().findViewById(R.id.close_fr_btn);

        point_logo = (ImageView)getActivity().findViewById(R.id.point_img);
        point_name = (TextView)getActivity().findViewById(R.id.point_name);
        point_score = (TextView)getActivity().findViewById(R.id.point_score);
        point_card = (CardView) getActivity().findViewById(R.id.pointCard_card_view);

        Log.i("NKW","name = "+membership_Data.nameArray[point_id]);

        point_logo.setImageResource(membership_Data.drawableArray[point_id]);
        point_name.setText(membership_Data.nameArray[point_id]);
        point_score.setText(membership_Data.scoreArray[point_id]);
        point_card.setCardBackgroundColor(membership_Data.backgroundArray[point_id]);

        //뒤로가기 버튼 누르면 프래그먼트 안보이게 하기
        imgbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getView().setVisibility(View.GONE);
            }
        });

        super.onActivityCreated(savedInstanceState);
    }

}
