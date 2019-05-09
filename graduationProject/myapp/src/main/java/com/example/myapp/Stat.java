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
    static final String BEAUTY = "뷰티/미용";
    static final String CULTURE = "문화/여가";
    static final String LIFE = "생활";
    static final String FOOD = "식비";
    static final String PAYMENT = "인터넷 결제";
    static final String TRAFFIC = "교통";
    static final String NONE = "미지정";
    static final String COFFEE = "카페/간식";
    static final String[] statNames = {BEAUTY, CULTURE, LIFE, FOOD, PAYMENT, TRAFFIC, NONE, COFFEE};
    private String s_name;
    private int s_price;
    private ArrayList<PayInfomation> list;
    private HashMap <String, Integer> classificationData = new HashMap<String, Integer>();
    public Stat(String n){
        s_name = n; s_price = 0;
        list = new ArrayList<PayInfomation>();
    }

    public void addInfo(PayInfomation info){
        Log.d("KJH", "Add payinfomation name : " + info.getAccountName() + ", price : " + info.getPrice());
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
        }
    }
    public void setClassificationData() {
        if (list.isEmpty()) return;
        Log.d("KJH", "setClassificationData() loop 0 : Name : " + list.get(0).getAccountName()
                + ", price : " + list.get(0).getPrice());
        classificationData.put(list.get(0).getAccountName(), list.get(0).getPrice());
        classificationData = new HashMap<String, Integer>();
        for (int i = 1; i < list.size(); i++) {
            String temp = list.get(i).getAccountName();
            if(classificationData.containsKey(temp)) {
                Log.d("KJH", "setClassificationData() loop " + i + " : Name : " + temp
                        + ", price : " + list.get(i).getPrice());
                classificationData.put(temp, classificationData.get(temp) + list.get(i).getPrice());
            }
            else {
                Log.d("KJH", "setClassificationData() loop " + i + " : Name : " + temp
                        + ", price : " + list.get(i).getPrice());
                classificationData.put(temp, list.get(i).getPrice());
            }
        }

    }
}
