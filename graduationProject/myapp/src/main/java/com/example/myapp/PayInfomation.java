package com.example.myapp;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by 강지현 on 2019-03-02.
 */

public class PayInfomation implements Serializable {
    private String accountName;
    private int p_price;
    private Date p_date;
    private Stat stat;
    private int hId;

    public PayInfomation(String n, int p, Stat s, Date date, int hId){
        accountName = n; p_price = p;
        p_date = date;
        stat = s;
        stat.addInfo(this);
        this.hId = hId;
    }
    public int gethId(){return hId;}
    public int getPrice(){
        return p_price;
    }
    public String getAccountName(){return accountName;}
    public Date getDate(){return p_date;}
    @Override
    public String toString(){
        return accountName + "(" + stat + ") : " + p_price;
    }
}
