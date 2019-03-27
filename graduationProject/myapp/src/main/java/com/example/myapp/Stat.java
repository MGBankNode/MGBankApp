package com.example.myapp;

/**
 * Created by 강지현 on 2019-03-02.
 */

public class Stat {
    String s_name;
    int s_price;
    public Stat(){
        s_name = "NO_NAME";
        s_price = 0;
    }
    public Stat(int price){
        s_name = "NO_NAME";
        s_price = price;
    }
    public Stat(String n, int p){
        s_name = n; s_price = p;
    }
    public int getPrice(){
        return s_price;
    }
    public String getName(){return s_name;}
}
