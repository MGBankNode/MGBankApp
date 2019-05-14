package com.example.myapp;

public class AccountInfo {


    String aNum;
    String aBalance;

    String aType;

    AccountInfo(String aNum, String aBalance, String aType){
        this.aNum = aNum;
        this.aBalance = aBalance;
    }

    public String getaNum() {
        return aNum;
    }

    public String getaBalance() {
        return aBalance;
    }

    public String getaType() {
        return aType;
    }

}
