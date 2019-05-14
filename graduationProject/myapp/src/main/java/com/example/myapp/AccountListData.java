package com.example.myapp;

public class AccountListData {

    public AccountListData(String accountName, String accountNumber, String accountValue) {
        this.accountName = accountName;
        this.accountNumber = accountNumber;
        this.accountValue = accountValue;
    }

    private String accountName;
    private String accountNumber;



    private String accountValue;


    public String getAccountName() {
        return accountName;
    }

    public String getAccountNumber() {
        return accountNumber;
    }
    public String getAccountValue() {
        return accountValue;
    }
}
