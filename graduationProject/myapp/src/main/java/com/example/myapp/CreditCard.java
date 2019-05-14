package com.example.myapp;

import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class CreditCard implements Serializable {
    private int icon;
    private String cardName;
    private ArrayList<Benefits> benefits;
    private int discountPrice;
    private int hh;
    public CreditCard(String name, int cardIcon){
        this.cardName = name;
        this.icon = cardIcon;
        benefits = new ArrayList<Benefits>();
    }
    public CreditCard(String name, int cardIcon, ArrayList<Benefits> benefitsArrayList){
        this.cardName = name;
        this.icon = cardIcon;
        benefits = new ArrayList<Benefits>();
        discountPrice = 0;
        benefits.addAll(benefitsArrayList);
    }
    public int getDiscountValue(){return discountPrice;}
    public int getIcon(){return icon;}
    public String getCardName(){return cardName;}
    public ArrayList<Benefits> getBenefits(){return benefits;}

    //사용자의 해쉬맵과 비교하여 할인된 값을 가져옴, 문자열 어레이에 저장
    public int getDiscountedPrice(Stat userStat, ArrayList<String> result) {
        userStat.dataSortByPrice();
        Log.d("KJH", "getDiscountedPrice");
        ArrayList<PayInfomation> userKeys = new ArrayList<PayInfomation>();
        int discountedPrice = 0;
        userKeys.addAll(userStat.getList());
        result.clear();
        for (int benefitsPosition = 0; benefitsPosition < benefits.size(); benefitsPosition++) {
            for (int userPosition = 0; userPosition < userKeys.size(); userPosition++) {
                if (benefits.get(benefitsPosition).isDiscounted(userStat, userKeys.get(userPosition).getAccountName())) {
                    result.add(userKeys.get(userPosition).getAccountName());
                    if (benefits.get(benefitsPosition).getBenefitValue() < 100) {
                        discountedPrice += (userKeys.get(userPosition).getPrice()
                                * benefits.get(benefitsPosition).getBenefitValue()) / 100;
                    } else {
                        discountedPrice += benefits.get(benefitsPosition).getBenefitValue();
                    }
                }
            }
        }

        return discountedPrice;
    }
    //사용자의 해쉬맵과 비교하여 할인된 값을 가져옴
    public int getDiscountedPrice(Stat userStat){
        userStat.dataSortByPrice();
        Log.d("KJH", "getDiscountedPrice");
        ArrayList<PayInfomation> userKeys = new ArrayList<PayInfomation>();
        int discountedPrice = 0;
        userKeys.addAll(userStat.getList());
        for (int benefitsPosition = 0; benefitsPosition < benefits.size(); benefitsPosition++) {
            for (int userPosition = 0; userPosition < userKeys.size(); userPosition++) {
                if (benefits.get(benefitsPosition).isDiscounted(userStat, userKeys.get(userPosition).getAccountName())) {
                    if (benefits.get(benefitsPosition).getBenefitValue() < 100) {
                        discountedPrice += (userKeys.get(userPosition).getPrice()
                                * benefits.get(benefitsPosition).getBenefitValue()) / 100;
                        Log.d("KJH", "discountedPrice : " + discountedPrice + " : "
                                + (userKeys.get(userPosition).getPrice() +
                                " * " + benefits.get(benefitsPosition).getBenefitValue()) + " / 100");
                    } else {
                        discountedPrice += benefits.get(benefitsPosition).getBenefitValue();
                    }
                }
            }
        }
        return discountedPrice;
    }
    public int getDiscountedPrice(Stat userStat, String accountsName, int accountsPrice) {
        userStat.dataSortByPrice();
        int discountedPrice = 0;
        for (int benefitsPosition = 0; benefitsPosition < benefits.size(); benefitsPosition++) {
            if (benefits.get(benefitsPosition).isDiscounted(userStat, accountsName)) {
                if (benefits.get(benefitsPosition).getBenefitValue() < 100) {
                    discountedPrice += (accountsPrice
                            * (benefits.get(benefitsPosition).getBenefitValue())) / 100;
                } else {
                    discountedPrice += benefits.get(benefitsPosition).getBenefitValue();
                }
            }
        }
        return discountedPrice;
    }
    @Override
    public String toString() {
        return cardName;
    }
    public void resetCondition(){
        for(int position = 0; position < benefits.size(); position++){
            benefits.get(position).resetCondition();
        }
    }
}
