package com.example.myapp;

public class RequestInfo {

    public enum RequestType{
        ID_CHECK,
        JOIN_USER,
        LOGIN_USER,
        ADD_DEVICE,
        DEVICE_CHECK,
        DELETE_DEVICE,
        ACCOUNT_HISTORY,
        ACCOUNT_BALANCE,
        ADD_ACCOUNT
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

            case ADD_DEVICE:
                processURL = "/nodeapi/device/adddevice";
                break;

            case DEVICE_CHECK:
                processURL = "/nodeapi/device/devicecheck";
                break;

            case DELETE_DEVICE:
                processURL = "/nodeapi/device/deletedevice";
                break;

            case ACCOUNT_HISTORY:
                processURL = "/nodeapi/history/accounthistory";
                break;

            case ACCOUNT_BALANCE:
                processURL = "/nodeapi/history/accountbalance";
                break;

            case ADD_ACCOUNT:
                processURL = "/nodeapi/account/addaccount";
                break;
        }
        return processURL;
    }

}
