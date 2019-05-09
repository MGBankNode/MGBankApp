package com.example.myapp;

import java.io.Serializable;
import java.util.Calendar;

/**
 * Created by 강지현 on 2019-03-02.
 */

public class PayInfomation implements Serializable {
    private String accountName;
    private int p_price;
    private Calendar p_date;
    private Stat stat;

    public PayInfomation(String n, int p, Stat s){
        accountName = n; p_price = p;
        p_date = Calendar.getInstance();
        stat = s;
        stat.addInfo(this);
    }

    public int getPrice(){
        return p_price;
    }
    public String getAccountName(){return accountName;}
    public String getDate(){return p_date.toString();}
    @Override
    public String toString(){
        return accountName;
    }
}
