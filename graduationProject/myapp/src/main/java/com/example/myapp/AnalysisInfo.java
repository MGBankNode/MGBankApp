package com.example.myapp;

import java.io.Serializable;

public class AnalysisInfo implements Serializable {

    private String one;
    private String oneSum;

    private String sDate;
    private String lDate;

    AnalysisInfo(String one, String oneSum){
        this.one = one;
        this.oneSum = oneSum;
    }

    AnalysisInfo(String one, String sDate, String lDate){
        this.one = one;
        this.sDate = sDate;
        this.lDate = lDate;
    }

    public String getWeek() {
        return one;
    }

    public String getWeekSum() {
        return oneSum;
    }

    public void setWeekSum(String weekSum) {
        this.oneSum = weekSum;
    }

    public String getDaily() { return one; }

    public String getDailySum() { return oneSum; }

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

    public String getMonth() {
        return one;
    }

    public String getMonthSum() {
        return oneSum;
    }

}
