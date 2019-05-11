package com.example.myapp;

public class AnalysisInfo {

    private String week;
    private String weekSum;



    private String sDate;
    private String lDate;

    AnalysisInfo(String week, String weekSum){
        this.week = week;
        this.weekSum = weekSum;
    }

    AnalysisInfo(String week, String sDate, String lDate){
        this.week = week;
        this.sDate = sDate;
        this.lDate = lDate;
    }

    public String getWeek() {
        return week;
    }

    public String getWeekSum() {
        return weekSum;
    }

    public void setWeekSum(String weekSum) {
        this.weekSum = weekSum;
    }

    public String getsDate() {
        return sDate;
    }

    public void setsDate(String sDate) {
        this.sDate = sDate;
    }

    public String getlDate() {
        return lDate;
    }

    public void setlDate(String lDate) {
        this.lDate = lDate;
    }

}
