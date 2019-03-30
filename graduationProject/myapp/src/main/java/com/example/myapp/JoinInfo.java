package com.example.myapp;

public class JoinInfo {
    String joinID;
    String joinName;
    String joinPW;
    String joinPhone;

    public JoinInfo(String joinID, String joinName, String joinPW, String joinPhone){
        this.joinID = joinID;
        this.joinName = joinName;
        this.joinPW = joinPW;
        this.joinPhone = joinPhone;
    }
    public String getJoinID(){
        return this.joinID;
    }
    public String getJoinName(){
        return this.joinName;
    }
    public String getJoinPW(){
        return this.joinPW;
    }
    public String getJoinPhone() { return this.joinPhone; }

    public void setJoinID(String joinID){
        this.joinID = joinID;
    }
    public void setJoinName(String joinName){   this.joinName = joinName; }
    public void setjoinPW(String joinPW){
        this.joinPW = joinPW;
    }
    public void setJoinPhone(String joinPhone){
        this.joinPhone = joinPhone;
    }
}
