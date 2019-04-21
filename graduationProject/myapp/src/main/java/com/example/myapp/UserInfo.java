package com.example.myapp;

import java.io.Serializable;

public class UserInfo implements Serializable {
    String userID;
    String userName;
    String userPW;
    String userPhone;
    int userAccountCheck;
    String userUpateAt;

    public UserInfo(String userID){
        this.userID = userID;
    }

    public UserInfo(String userID, String userPW){
        this.userID = userID;
        this.userPW = userPW;
    }

    public UserInfo(String userID, String userName, String userPW, String userPhone){
        this.userID = userID;
        this.userName = userName;
        this.userPW = userPW;
        this.userPhone = userPhone;
    }

    public UserInfo(String userID, String userName, int userAccountCheck, String userUpateAt){
        this.userID = userID;
        this.userName = userName;
        this.userAccountCheck = userAccountCheck;
        this.userUpateAt = userUpateAt;
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

    public String getUserUpateAt() { return this.userUpateAt; }

}
