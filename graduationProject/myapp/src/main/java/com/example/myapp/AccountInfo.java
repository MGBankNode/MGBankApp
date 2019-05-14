package com.example.myapp;

public class AccountInfo {


    String aNum;
    String aBalance;

    AccountInfo(String aNum, String aBalance){
        this.aNum = aNum;
        this.aBalance = aBalance;
    }

    public String getaNum() {
        return aNum;
    }

    public String getaBalance() {
        return aBalance;
    }
}
