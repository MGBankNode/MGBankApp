package com.example.myapp;

public class JoinInfo {
    String joinID;
    String joinName;
    String joinPW;

    public JoinInfo(String joinID, String joinName, String joinPW){
        this.joinID = joinID;
        this.joinName = joinName;
        this.joinPW = joinPW;
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

    public void setJoinID(String joinID){
        this.joinID = joinID;
    }
    public void setName(String joinName){
        this.joinName = joinName;
    }
    public void setjoinPW(String joinPW){
        this.joinPW = joinPW;
    }
}
