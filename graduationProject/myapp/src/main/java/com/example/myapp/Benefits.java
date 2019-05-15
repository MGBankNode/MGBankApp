package com.example.myapp;

import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

public class Benefits implements CardDiscount, Serializable {
    private String b_name;
    private ArrayList<String> benefitsNames;
    private int benefitValue;
    final int DAYCONDITION;
    final int MONTHCONDITION;
    final int YEARCONDITION;
    ArrayList<String> dayList;
    ArrayList<String> monthList;
    private final int PRICE_CONDITION;
    //ArrayList<String> yearList;

    Util util = new Util();

    public Benefits(String b_name, String[] list, int value, int dc, int mc, int yc, int priceC){
        this.b_name = b_name;
        this.benefitsNames = new ArrayList<String>(Arrays.asList(list));
        this.benefitValue = value;
        DAYCONDITION = dc;
        MONTHCONDITION = mc;
        YEARCONDITION = yc;
        PRICE_CONDITION = priceC;
        dayList = new ArrayList<String>();
        monthList = new ArrayList<String>();
    }
    public String getB_name(){return b_name;}
    public ArrayList<String> getBenefitsNames(){return benefitsNames;}
    public int getBenefitValue(){return benefitValue;}
    @Override
    public boolean discountCondition(int dayCondition, int monthCondition, int yearCondition) {
        return false;
    }
    public boolean isDiscounted(Stat stat, String AccountsName){
        stat.dataSortByPrice();
        //Log.d("KJH", "베네핏에서 할인여부 확인, 거래처 : " + AccountsName);
        for(int position = 0; position < benefitsNames.size(); position++)
        {
            if(AccountsName.contains(benefitsNames.get(position))) {
                PayInfomation temp = stat.getPayInfomation(AccountsName);

                Log.d("KJH",  "거래처 : " + AccountsName + "(" + util.dateForm(temp.getDate()) +")"+ ", 할인처 : " + benefitsNames.get(position));
                if(findPriceCondition(temp.getPrice())) {
                    if (findMonth(temp.getDate())) {
                        Log.d("KJH", "거래처 : " + AccountsName + "(" + util.dateForm(temp.getDate()) + ")" + ", 할인처 : " + benefitsNames.get(position) + "월 한도 통");
                        if (findDay(temp.getDate())) {
                            Log.d("KJH", "거래처 : " + AccountsName + "(" + util.dateForm(temp.getDate()) + ")" + ", 할인처 : " +
                                    benefitsNames.get(position) + ", monthSize : " + monthList.size() + " - 할인됨");
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }
    public boolean equalsDate(Date first, Date second){
        return util.dateForm(first).equals(util.dateForm(second));
    }
    public boolean findDay(Date date){
        //Log.d("KJH", "일 한도 테스트");
        if(DAYCONDITION == -1) return true;
        int count = 0;
        ArrayList<String> temp = new ArrayList<String>();
        temp.addAll(dayList);
        //Log.d("KJH", "일 한도 temp size() : " + temp.size());
        if(temp.isEmpty()){
            dayList.add(util.dateForm(date));
            return true;
        }
        for(int position = 0; position < temp.size(); position++){
            if (temp.get(position).equals(util.dateForm(date))) {
                count++;
                if (count >= DAYCONDITION) {
                    //Log.d("KJH", "일 한도 초과");
                    return false;
                }
            }
        }
        //Log.d("KJH", "일 한도 배열에 추가 : " + util.dateForm(date));
        dayList.add(util.dateForm(date));
        return true;
    }
    public boolean findMonth(Date date){
        //Log.d("KJH", "월 한도 테스트");
        if(MONTHCONDITION == -1) return true;
        int count = 0;
        ArrayList<String> temp = new ArrayList<String>();
        temp.addAll(monthList);
        //Log.d("KJH", "월 한도 temp size() : " + temp.size());
        if(temp.isEmpty()){
            monthList.add(util.dateFormToMonth(date));
            return true;
        }
        for(int position = 0; position < temp.size(); position++){
            if (temp.get(position).equals(util.dateFormToMonth(date))){
                count++;
                if(count >= MONTHCONDITION) {
                    //Log.d("KJH", "월 한도 초과");
                    return false;
                }
            }
        }
        //Log.d("KJH", "월 한도 배열에 추가 : " + util.dateFormToMonth(date));
        monthList.add(util.dateFormToMonth(date));
        return true;
    }
    public boolean findPriceCondition(int price){
        if(PRICE_CONDITION < 0) return true;
        else if(PRICE_CONDITION > price) return false;
        else if(PRICE_CONDITION <= price) return true;
        else return false;
    }
    public void resetCondition(){
        dayList.clear();
        monthList.clear();
    }
}
