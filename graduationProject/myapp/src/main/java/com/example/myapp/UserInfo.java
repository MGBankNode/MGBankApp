package com.example.myapp;

public class UserInfo {
    String userID;
    String userName;
    String userPW;
    String userPhone;

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

}
