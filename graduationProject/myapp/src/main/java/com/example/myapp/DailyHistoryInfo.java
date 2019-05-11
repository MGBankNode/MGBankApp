package com.example.myapp;

public class DailyHistoryInfo {

    String day;     //일
    String dailyBenefit; //수익
    String dailyLoss;    //지출

    DailyHistoryInfo(String day, String dailyBenefit, String dailyLoss){
        this.day = day;
        this.dailyBenefit = dailyBenefit;
        this.dailyLoss = dailyLoss;
    }

    public String getDay() {
        return day;
    }

    public String getDailyBenefit() {
        return dailyBenefit;
    }

    public String getDailyLoss() {
        return dailyLoss;
    }

}
