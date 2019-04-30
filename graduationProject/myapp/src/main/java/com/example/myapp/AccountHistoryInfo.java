package com.example.myapp;

public class AccountHistoryInfo {

    String hDate;       //내역 사용 날짜
    String hType;       //내역 사용 타입 => 입금/충금
    String hValue;      //내역 사용 금액
    String hName;       //내역 사용처 이름
    String aBalance;    //내역 사용 후 잔액
    String cName;       //내역 사용한 카드 종류 이름


    AccountHistoryInfo(String hDate, String hType, String hValue, String hName, String aBalance, String cName){
        this.hDate = hDate;
        this.hType = hType;
        this.hValue = hValue;
        this.hName = hName;
        this.aBalance = aBalance;
        this.cName = cName;
    }

    public String gethDate() {
        return hDate;
    }

    public void sethDate(String hDate) {
        this.hDate = hDate;
    }

    public String gethType() {
        return hType;
    }

    public void sethType(String hType) {
        this.hType = hType;
    }

    public String gethValue() {
        return hValue;
    }

    public void sethValue(String hValue) {
        this.hValue = hValue;
    }

    public String gethName() {
        return hName;
    }

    public void sethName(String hName) {
        this.hName = hName;
    }

    public String getaBalance() {
        return aBalance;
    }

    public void setaBalance(String aBalance) {
        this.aBalance = aBalance;
    }

    public String getcName() {
        return cName;
    }

    public void setcName(String cName) {
        this.cName = cName;
    }




}