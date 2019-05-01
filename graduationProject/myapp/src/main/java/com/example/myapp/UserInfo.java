package com.example.myapp;

import java.io.Serializable;

public class UserInfo implements Serializable {
    String userID;
    String userName;
    String userPW;
    String userPhone;
    int userAccountCheck;
    String userUpdateAt;


    String userABalance;

    public UserInfo(String userID){
        this.userID = userID;
    }

    public UserInfo(String userID, String userPW){
        this.userID = userID;
        this.userPW = userPW;
    }

    //회원가입시 사용
    public UserInfo(String userID, String userName, String userPW, String userPhone){
        this.userID = userID;
        this.userName = userName;
        this.userPW = userPW;
        this.userPhone = userPhone;
    }

    //로그인 시 사용자 정보 -> 계좌 등록이 안된경우
    public UserInfo(String userID, String userName, String userPhone, int userAccountCheck, String userUpdateAt){
        this.userID = userID;
        this.userName = userName;
        this.userPhone = userPhone;
        this.userAccountCheck = userAccountCheck;
        this.userUpdateAt = userUpdateAt;
    }

    //로그인 시 사용자 정보 -> 계좌 등록이 된경우 잔액 조회 가능
    public UserInfo(String userID, String userName, String userPhone, int userAccountCheck, String userUpdateAt, String userABalance){
        this.userID = userID;
        this.userName = userName;
        this.userPhone = userPhone;
        this.userAccountCheck = userAccountCheck;
        this.userUpdateAt = userUpdateAt;
        this.userABalance = userABalance;
    }

    public String getUserID(){
        return this.userID;
    }

    public String getUserName(){
        return this.userName;
    }

    public String getUserPW(){
        return this.userPW;
    }

    public String getUserPhone() {
        return this.userPhone;
    }

    public int getUserAccountCheck() { return this.userAccountCheck; }

    public String getUserUpateAt() { return this.userUpdateAt; }

    public String getUserABalance() {
        return userABalance;
    }

    public void setUserABalance(String userABalance) {
        this.userABalance = userABalance;
    }

}
