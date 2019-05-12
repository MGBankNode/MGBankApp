package com.example.myapp;

import java.util.ArrayList;

public class CardDataBase {
    public ArrayList<CreditCard> CardList;

    public CardDataBase(){
        CardList = new ArrayList<CreditCard>();
        ArrayList<Benefits> PeachCardBenefits = new ArrayList<Benefits>();
        ArrayList<Benefits> LimeCardBenefits = new ArrayList<Benefits>();
        ArrayList<Benefits> KakaoCardBenefits = new ArrayList<Benefits>();
        ArrayList<Benefits> SamsungCardBenefits = new ArrayList<Benefits>();


        //PeachCard
        //임시로 한 것
        PeachCardBenefits.add(new Benefits("PeachConvenience",
                new String[]{"GS25", "지에스25", "쥐에스25", "세븐일레븐", "CU", "씨유"}, 5){
            @Override
            public boolean discountCondition() {
                return super.discountCondition();
            }
        });
        PeachCardBenefits.add(new Benefits("PeachTheater",
                new String[]{"CGV", "씨지비", "megabpx", "MEGABOX", "메가박스", "롯데시네마"}, 2000));
        //LimeCard
        //임시로 한것
        LimeCardBenefits.add(new Benefits("LimeConvenience",
                new String[]{"GS25", "지에스25", "쥐에스25", "세븐일레븐", "CU", "씨유"}, 3));
        LimeCardBenefits.add(new Benefits("LimeTheater",
                new String[]{"CGV", "씨지비", "megabpx", "MEGABOX", "메가박스", "롯데시네마"}, 3000));
        //KakaoCard
        //임시로 한것
        KakaoCardBenefits.add(new Benefits("KakaoConvenience",
                new String[]{"GS25", "지에스25", "쥐에스25", "세븐일레븐", "CU", "씨유"}, 10));
        //SamsungCard
        //임시로 한것
        SamsungCardBenefits.add(new Benefits("SamsungTheater",
                new String[]{"CGV", "씨지비", "megabpx", "MEGABOX", "메가박스", "롯데시네마"}, 5000));

        CardList.add(new CreditCard("PeachCard", R.drawable.card_peach, PeachCardBenefits));
        CardList.add(new CreditCard("LimeCard", R.drawable.card_lime, LimeCardBenefits));
        CardList.add(new CreditCard("KakaoCard", R.drawable.card_kakao, KakaoCardBenefits));
        CardList.add(new CreditCard("Samsung", R.drawable.card_samsung, SamsungCardBenefits));


    }
}
