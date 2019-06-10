package com.example.myapp;

import java.io.Serializable;

public class MonthReportData implements Serializable {

    String date;
    String place;
    int count;
    int price;

    public MonthReportData(String place, int count, int price) {
        this.place = place;
        this.count = count;
        this.price = price;
    }

    public MonthReportData(String date, String place, int price) {
        this.date = date;
        this.place = place;
        this.price = price;
    }

    public String getDate() {
        return date;
    }

    public String getPlace() {
        return place;
    }

    public int getCount() {
        return count;
    }

    public int getPrice() {
        return price;
    }
}
