package com.example.myapp;

public class RequestInfo {

    public enum RequestType{
        ID_CHECK,
        JOIN_USER,
        LOGIN_USER
    }

    private final static String myIP = "ec2-13-124-68-124.ap-northeast-2.compute.amazonaws.com";
    private final static String myPort = "1119";
    private RequestType myRequestType;

    RequestInfo(RequestType requestType){
        this.myRequestType = requestType;
    }

    public String GetRequestIP(){
        return myIP;
    }

    public String GetRequestPORT(){
        return myPort;
    }

    public String GetProcessURL(){
        String processURL = "";

        switch (myRequestType){

            case ID_CHECK:
                processURL = "/nodeapi/join/idcheck";
                break;

            case JOIN_USER:
                processURL = "/nodeapi/join/joinuser";
                break;

            case LOGIN_USER:
                processURL = "/nodeapi/login/logincheck";
                break;

        }
        return processURL;
    }

}
