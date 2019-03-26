package com.example.myapp;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

public class recommendCard_fragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recommendcard, container, false);

        ListView listView =(ListView)view.findViewById(R.id.card_list);

        ArrayList<cardElement> cardData = new ArrayList<>();
        cardData.add(new cardElement(R.drawable.card_kakao,"카카오페이 체크카드","편의점·소셜 커머스·어학 응시·영화·커피 전문점·스타일 푸드 할인"));
        cardData.add(new cardElement(R.drawable.card_samsung,"새마을금고 카앤모아","모든 주요소 리터당 60원 할인(LPG 30원)"));
        cardData.add(new cardElement(R.drawable.card_lime, "LIME 체크카드", "주유·커피·편의점 할인"));
        cardData.add(new cardElement(R.drawable.card_on, "ON 체크카드", "온라인 간편결제·영화·어학시험 할인"));

        ListviewAdapter listviewAdapter = new ListviewAdapter(getContext(),R.layout.card_list,cardData);
        listView.setAdapter(listviewAdapter);

        return view;
    }
}
