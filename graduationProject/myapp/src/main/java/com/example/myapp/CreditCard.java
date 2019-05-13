package com.example.myapp;

import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class CreditCard implements Serializable {
    private int icon;
    private String cardName;
    private HashMap<String, Integer> benefits;
    private int discountPrice;
    public CreditCard(String name, int cardIcon){
        this.cardName = name;
        this.icon = cardIcon;
        benefits = new HashMap<String, Integer>();
    }
    public CreditCard(String name, int cardIcon, ArrayList<Benefits> benefitsArrayList){
        this.cardName = name;
        this.icon = cardIcon;
        benefits = new HashMap<String, Integer>();
        discountPrice = 0;
        for(int position = 0; position < benefitsArrayList.size(); position++) {
            addBenefits(benefitsArrayList.get(position).getBenefitsNames()
                    , benefitsArrayList.get(position).getBenefitValue());
        }
    }
    public int getDiscountValue(){return discountPrice;}
    public int getIcon(){return icon;}
    public String getCardName(){return cardName;}
    public HashMap<String, Integer> getBenefits(){return benefits;}
    public void addBenefits(String benefitsAccountName, int benefitValue){
        if(!benefits.containsKey(benefitsAccountName))
            benefits.put(benefitsAccountName, benefitValue);
        else return;
    }
    public void addBenefits(ArrayList<String> benefitsAccountName, int benefitValue){
        if(!benefitsAccountName.isEmpty()) {
            for (int position = 0; position < benefitsAccountName.size(); position++) {
                if (!benefits.containsKey(benefitsAccountName.get(position)))
                    benefits.put(benefitsAccountName.get(position), benefitValue);
            }
        }
    }
    //사용자의 해쉬맵과 비교하여 할인된 값을 가져옴, 문자열 어레이에 저장
    public int getDiscountedPrice(HashMap<String, Integer> userClassificationData, ArrayList<String> result){
        Log.d("KJH", "getDiscountedPrice");
        ArrayList<String> benefitsKeys = new ArrayList<String>();
        ArrayList<String> userKeys = new ArrayList<String>();
        ArrayList<String> resultAccountNames = new ArrayList<String>();
        int discountedPrice = 0;
        benefitsKeys.addAll(benefits.keySet());
        userKeys.addAll(userClassificationData.keySet());
        result.clear();
        for(int benefitsPosition = 0; benefitsPosition < benefitsKeys.size(); benefitsPosition++){
            for(int userPosition = 0; userPosition < userKeys.size(); userPosition++){
                //Log.d("KJH", "getDiscountedPrice : " + benefitsPosition + ", " + userPosition);
                if(userKeys.get(userPosition).contains(benefitsKeys.get(benefitsPosition))){
                    result.add(userKeys.get(userPosition));
                    Log.d("TESTLOG", "result.add(" + userKeys.get(userPosition) + ")");
                    if(benefits.get(benefitsKeys.get(benefitsPosition)) < 100) {
                        discountedPrice += (userClassificationData.get(userKeys.get(userPosition))
                                * benefits.get(benefitsKeys.get(benefitsPosition)) / 100);
                    }
                    else{
                        discountedPrice += benefits.get(benefitsKeys.get(benefitsPosition));
                    }
                }
            }
        }
        return discountedPrice;
    }
    //사용자의 해쉬맵과 비교하여 할인된 값을 가져옴
    public int getDiscountedPrice(HashMap<String, Integer> userClassificationData){
        Log.d("KJH", "getDiscountedPrice");
        ArrayList<String> benefitsKeys = new ArrayList<String>();
        ArrayList<String> userKeys = new ArrayList<String>();
        ArrayList<String> resultAccountNames = new ArrayList<String>();
        int discountedPrice = 0;
        benefitsKeys.addAll(benefits.keySet());
        userKeys.addAll(userClassificationData.keySet());
        for(int benefitsPosition = 0; benefitsPosition < benefitsKeys.size(); benefitsPosition++){
            for(int userPosition = 0; userPosition < userKeys.size(); userPosition++){
                if(userKeys.get(userPosition).contains(benefitsKeys.get(benefitsPosition))){
                    Log.d("TESTLOG", "result.add(" + userKeys.get(userPosition) + ")");
                    if(benefits.get(benefitsKeys.get(benefitsPosition)) < 100) {
                        discountedPrice += (userClassificationData.get(userKeys.get(userPosition))
                                * benefits.get(benefitsKeys.get(benefitsPosition)) / 100);
                    }
                    else{
                        discountedPrice += benefits.get(benefitsKeys.get(benefitsPosition));
                    }
                }
            }
        }
        return discountedPrice;
    }
    public int getDiscountedPrice(String accountsName, int accountsPrice){
        ArrayList<String> benefitsKeys = new ArrayList<String>();
        int discountedPrice = 0;
        benefitsKeys.addAll(benefits.keySet());
        for(int benefitsPosition = 0; benefitsPosition < benefitsKeys.size(); benefitsPosition++){
                if(accountsName.contains(benefitsKeys.get(benefitsPosition))){
                    if(benefits.get(benefitsKeys.get(benefitsPosition)) < 100) {
                        discountedPrice += (accountsPrice
                                * (benefits.get(benefitsKeys.get(benefitsPosition))) / 100);

                        Log.d("KJH", "getDiscountedPrice String : " + accountsName + ", price : " +
                                accountsPrice + ", discountedPrice : " + discountedPrice );
                    }
                    else{
                        discountedPrice += benefits.get(benefitsKeys.get(benefitsPosition));
                    }
                }
        }
        return discountedPrice;
    }

    @Override
    public String toString() {
        return cardName;
    }
}
