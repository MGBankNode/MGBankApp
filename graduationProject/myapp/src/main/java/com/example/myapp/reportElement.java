package com.example.myapp;

public class reportElement {
    private String reportDate;
    private int reportExpenditure;

    public reportElement() {

    }

    public reportElement(String reportDate, int reportExpenditure) {
        this.reportDate = reportDate;
        this.reportExpenditure = reportExpenditure;
    }

    public String getReportDate() {
        return reportDate;
    }

    public int getReportExpenditure() {
        return reportExpenditure;
    }


}
