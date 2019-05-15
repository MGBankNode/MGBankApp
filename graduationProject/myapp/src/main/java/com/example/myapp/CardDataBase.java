package com.example.myapp;

import java.util.ArrayList;

public class CardDataBase {
    public ArrayList<CreditCard> CardList;

    /*카드 데이터 베이스 입력방법*/
    /*

    * 1. 혜택 집합 선언하기 (카드당 1개)
    * ArrayList<Benefits> 해당카드의혜택들의 집합 이름 = new ArrayList<Benefits>();
    * ex) ArrayList<Benefits> PeachCardBenefits = new ArrayList<Benefits>();
    *
    * 2. 혜택 집합에 혜택 추가하기 (횟수제한이나 금액제한이 없을시 int 인자값을 -1 로 준다.)
    * 해당카드혜택들의 집합 이름.add(new Benefits(String "혜택이름", new String[]{"혜택이", "적용", "될만한", "확실한", "문자열들"}
    *       , int (혜택 퍼센트, 금액은 그냥 숫자로 기입), int (일별 횟수제한), int (월별 횟수제한), int (연도별 횟수제한), int (건당 금액제한));
    * ex)PeachCardBenefits.add(new Benefits("PeachConvenience",
                new String[]{"GS25", "지에스25", "쥐에스25", "세븐일레븐", "CU", "씨유"}, 5, 1, 2, 0, -1));
    *
    * 3. 카드리스트에 카드를 추가한다.
    * CardList.add(new CreditCard(String "카드이름(어플에 적용됨)", int 카드의 이미지(어플에 적용됨), ArrayList<Benefits> 혜택집합);
    * ex) CardList.add(new CreditCard("PeachCard", R.drawable.card_peach, PeachCardBenefits));
    *
    *

     *
    * */
    public CardDataBase(){
        CardList = new ArrayList<CreditCard>();
        ArrayList<Benefits> PeachCardBenefits = new ArrayList<Benefits>();
        ArrayList<Benefits> LimeCardBenefits = new ArrayList<Benefits>();
        ArrayList<Benefits> KakaoCardBenefits = new ArrayList<Benefits>();
        ArrayList<Benefits> SamsungCardBenefits = new ArrayList<Benefits>();


        //PeachCard
        //임시로 한 것
        PeachCardBenefits.add(new Benefits("PeachConvenience",
                new String[]{"GS25", "지에스25", "쥐에스25", "세븐일레븐", "CU", "씨유"}, 5, 1, 2, 0, -1));
        PeachCardBenefits.add(new Benefits("PeachTheater",
                new String[]{"CGV", "씨지비", "megabpx", "MEGABOX", "메가박스", "롯데시네마"}, 2000, -1, -1, 0, 10000));
        //LimeCard
        //임시로 한것
        LimeCardBenefits.add(new Benefits("LimeConvenience",
                new String[]{"GS25", "지에스25", "쥐에스25", "세븐일레븐", "CU", "씨유"}, 3,  1, 2, 0, -1));
        LimeCardBenefits.add(new Benefits("LimeTheater",
                new String[]{"CGV", "씨지비", "megabpx", "MEGABOX", "메가박스", "롯데시네마"}, 3000,  0, 0, 0, 10000));
        //KakaoCard
        //임시로 한것
        KakaoCardBenefits.add(new Benefits("KakaoConvenience",
                new String[]{"GS25", "지에스25", "쥐에스25", "세븐일레븐", "CU", "씨유"}, 10,  1, 2, 0, -1));
        //SamsungCard
        //임시로 한것
        SamsungCardBenefits.add(new Benefits("SamsungTheater",
                new String[]{"CGV", "씨지비", "megabpx", "MEGABOX", "메가박스", "롯데시네마"}, 5000,  -1, -1, 0, 10000));

        CardList.add(new CreditCard("PeachCard", R.drawable.card_peach, PeachCardBenefits));
        CardList.add(new CreditCard("LimeCard", R.drawable.card_lime, LimeCardBenefits));
        CardList.add(new CreditCard("KakaoCard", R.drawable.card_kakao, KakaoCardBenefits));
        CardList.add(new CreditCard("Samsung", R.drawable.card_samsung, SamsungCardBenefits));


    }
}
