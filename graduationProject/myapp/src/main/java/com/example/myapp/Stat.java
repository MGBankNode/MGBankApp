package com.example.myapp;

import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

/**
 * Created by 강지현 on 2019-03-02.
 */

public class Stat implements Serializable {
    static final String CULTURE = "문화/여가";
    static final String LIFE = "생활/쇼핑";
    static final String FOOD = "식비";
    static final String TRAFFIC = "교통";
    static final String NONE = "미분류";
    static final String FINANCE = "금융";
    static final String TRAVEL = "여행/숙박";
    static final String DRINK = "술/유흥";
    static final String DWELLING = "주거/통신";
    static final String HOSPITAL = "의료/건강";
    static final String COFFEE = "카페/간식";
    static final String[] statNames = {CULTURE, LIFE, FOOD, TRAFFIC, NONE
            , COFFEE, FINANCE, TRAVEL, DRINK, DWELLING, HOSPITAL};
    private String s_name;
    private int s_price;
    private ArrayList<PayInfomation> list;
    private HashMap <String, Integer> classificationData = new HashMap<String, Integer>();
    public Stat(String n){
        s_name = n; s_price = 0;
        list = new ArrayList<PayInfomation>();
    }

    public void addInfo(PayInfomation info){
        Log.d("KJH", "Add payinfomation " + s_name + " name : " + info.getAccountName() + ", price : " + info.getPrice());
        list.add(info);
        s_price += info.getPrice();
    }
    public int getPrice(){
        return s_price;
    }
    public String getName(){return s_name;}
    public ArrayList<PayInfomation> getList(){return list;}
    public HashMap<String, Integer> getClassificationData(){ return classificationData; }
    public String[] getStatNames(){return statNames;}
    @Override
    public String toString(){
        return s_name;
    }

    //데이터를 금액순으로 소팅
    public void dataSort(){
        PayInfoComparator comp = new PayInfoComparator();
        Collections.sort(this.list, comp);
    }
    //소팅에 필요한 비교자
    public class PayInfoComparator implements Comparator<PayInfomation> {
        @Override
        public int compare(PayInfomation first, PayInfomation second){
            String firstValue = first.getAccountName();
            String secondValue = second.getAccountName();

            if(firstValue.equals(secondValue)) return -1;
            else if(firstValue.equals(secondValue)) return 1;
            else return 0;
        };
    }
    public void setClassificationData() {
        if (list.isEmpty()) return;
        classificationData = new HashMap<String, Integer>();
        classificationData.put(list.get(0).getAccountName(), list.get(0).getPrice());

        for (int i = 1; i < list.size(); i++) {
            String temp = list.get(i).getAccountName();
            if(classificationData.containsKey(temp)) {
                classificationData.put(temp, classificationData.get(temp) + list.get(i).getPrice());
            }
            else {
                classificationData.put(temp, list.get(i).getPrice());
            }
        }
    }
    public void getSelectedHashMap(ArrayList<String> list, HashMap<String, Integer> result){
        for(int position = 0; position < list.size(); position++){
            if(classificationData.containsKey(list.get(position)))
                result.put(list.get(position), classificationData.get(list.get(position)));
        }
    }
    public boolean isEmpty(){return list.isEmpty();}
    public ArrayList<PayInfomation> getPayInformation(String keyword){
        ArrayList<PayInfomation> temp = new ArrayList<PayInfomation>();
        for(int position = 0; position < list.size(); position++){
            if(list.get(position).getAccountName().contains(keyword))
                temp.add(list.get(position));
        }
        return temp;
    }

    //카드와 비교하여 이 분류의 할인 금액을 가져옴
    public int getDiscountedPrice(CreditCard card){
        setClassificationData();
        return card.getDiscountedPrice(this.getClassificationData());
    }
    //카드와 비교하여 이 분류의 할인 금액과, 인자로 받은 리스트에 거래처의 이름을 저장함
        public int getDiscountedPrice(CreditCard card, ArrayList<String> result){
        ArrayList<String> temp = new ArrayList<String>();
        int resultValue = card.getDiscountedPrice(getClassificationData(), temp);
        result.addAll(temp);
        return resultValue;
    }
    public PayInfomation getPayInfomation(String keyword){
        for(int position = 0; position < list.size(); position++){
            if(list.get(position).getAccountName().equals(keyword))
                return list.get(position);
        }
    }
}
