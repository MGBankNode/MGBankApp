package com.example.myapp;

public class reportElement {
    private String reportDate;
    private int reportExpenditure;
    private boolean isMonthReport;

    public reportElement(String reportDate, int reportExpenditure, boolean isMonthReport) {
        this.reportDate = reportDate;
        this.reportExpenditure = reportExpenditure;
        this.isMonthReport = isMonthReport;
    }
    public reportElement(String reportDate, boolean isMonthReport) {
        this.reportDate = reportDate;
        this.reportExpenditure = 0;
        this.isMonthReport = isMonthReport;

    }

    public String getReportDate() {
        return reportDate;
    }

    public int getReportExpenditure() {
        return reportExpenditure;
    }


}
