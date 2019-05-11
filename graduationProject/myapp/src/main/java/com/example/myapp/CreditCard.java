package com.example.myapp;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;

public class CreditCard {
    private int icon;
    private String cardName;
    private HashMap<String, Integer> benefits;
    public CreditCard(String name, int cardIcon){
        this.cardName = name;
        this.icon = cardIcon;
        benefits = new HashMap<String, Integer>();
    }
    public int getIcon(){return icon;}
    public String getCardName(){return cardName;}
    public HashMap<String, Integer> getBenefits(){return benefits;}
    public void addBenefits(String benefitsAccountName, int benefitValue){
        if(!benefits.containsKey(benefitsAccountName))
            benefits.put(benefitsAccountName, benefitValue);
        else return;
    }
    //사용자의 해쉬맵과 비교하여 할인된 값을 가져옴
    public int getDiscountedPice(HashMap<String, Integer> userClassificationData, ArrayList<String> result){
        Log.d("KJH", "getDiscountedPrice");
        ArrayList<String> benefitsKeys = new ArrayList<String>();
        ArrayList<String> userKeys = new ArrayList<String>();
        ArrayList<String> resultAccountNames = new ArrayList<String>();
        int discountedPrice = 0;
        benefitsKeys.addAll(benefits.keySet());
        userKeys.addAll(userClassificationData.keySet());
        for(int benefitsPosition = 0; benefitsPosition < benefitsKeys.size(); benefitsPosition++){
            for(int userPosition = 0; userPosition < userKeys.size(); userPosition++){
                Log.d("KJH", "getDiscountedPrice : " + benefitsPosition + ", " + userPosition);
                if(userKeys.get(userPosition).contains(benefitsKeys.get(benefitsPosition))){
                    resultAccountNames.add(userKeys.get(userPosition));
                    discountedPrice += (userClassificationData.get(userKeys.get(userPosition))
                            * benefits.get(benefitsKeys.get(benefitsPosition)) / 100);
                    Log.d("KJH", "User : " + userKeys.get(userPosition) + ", benefits : " + benefitsKeys.get(benefitsPosition) +
                            ", total : " + discountedPrice);
                }
            }
        }
        result = resultAccountNames;
        return discountedPrice;
    }
}
