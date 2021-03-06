package com.example.myapp;

public class HistoryInfo {

    AccountInfo accountInfo;
    int hId;            //카테고리 변경시에 필요한 아이디
    String hDate;       //내역 사용 날짜
    String hType;       //내역 사용 타입 => 입금/충금/카드
    String hValue;      //내역 사용 금액
    String hName;       //내역 사용처 이름
    String aBalance;    //내역 사용 후 잔액
    String aType;       //계좌
    String cType;       //카드 이름
    String cName;       //카테고리 분류

    HistoryInfo(String aNum, String aBalance, String aType) {
        accountInfo = new AccountInfo(aNum, aBalance, aType);
    }

    HistoryInfo(int hId, String hDate, String hValue, String hName, String cName) {
        this.hId = hId;
        this.hDate = hDate;
        this.hValue = hValue;
        this.hName = hName;
        this.cName = cName;
    }

    HistoryInfo(int hId, String hDate, String hType, String hValue, String hName, String aBalance, String aType, String cType, String cName) {
        this.hId = hId;
        this.hDate = hDate;
        this.hType = hType;
        this.hValue = hValue;
        this.hName = hName;
        this.aBalance = aBalance;
        this.aType = aType;
        this.cType = cType;
        this.cName = cName;
    }


    HistoryInfo(int hId, String hDate, String hType, String hValue, String hName, String aBalance, String cType, String cName) {
        this.hId = hId;
        this.hDate = hDate;
        this.hType = hType;
        this.hValue = hValue;
        this.hName = hName;
        this.aBalance = aBalance;
        this.cType = cType;
        this.cName = cName;
    }

    public AccountInfo getAccountInfo() {
        return accountInfo;
    }

    public int gethId() {
        return hId;
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

    public String getaType() {
        return aType;
    }

    public void setaType(String aType) {
        this.aType = aType;
    }

    public String getcType() {
        return cType;
    }

    public void setcType(String cType) {
        this.cType = cType;
    }

    public String getcName() {
        return cName;
    }

    public void setcName(String cName) {
        this.cName = cName;
    }
}