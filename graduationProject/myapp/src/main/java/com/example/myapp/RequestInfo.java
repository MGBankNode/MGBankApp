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
        ACCOUNT_HOME_HISTORY,
        ACCOUNT_BALANCE,
        ADD_ACCOUNT,
        ANALYSIS_WEEK,
        ANALYSIS_DAILY,
        BARCODE_POINT,
        UPDATE_CATEGORY,
        ACCOUNT_BY_HISTORY,
        BALANCE_LIST,
        STORE_CHECK,
        ADD_RECEIPT,
        ADD_NEW_RECEIPT,
        DEFAULT_BUDGET,
        CHANGE_BUDGET,
        ACCOUNT_REFRESH
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

            case ACCOUNT_HOME_HISTORY:
                processURL = "/nodeapi/history/accounthomehistory";
                break;

            case ACCOUNT_BALANCE:
                processURL = "/nodeapi/history/accountbalance";
                break;

            case ADD_ACCOUNT:
                processURL = "/nodeapi/account/addaccount";
                break;

            case ANALYSIS_WEEK:
                processURL = "/nodeapi/analysis/analysisweek";
                break;

            case ANALYSIS_DAILY:
                processURL = "/nodeapi/analysis/analysisdaily";
                break;

            case BARCODE_POINT:
                processURL = "/nodeapi/barcode/barcodepoint";
                break;

            case UPDATE_CATEGORY:
                processURL = "/nodeapi/category/updatecategory";
                break;

            case ACCOUNT_BY_HISTORY:
                processURL = "/nodeapi/history/accountbyhistory";
                break;

            case BALANCE_LIST:
                processURL = "/nodeapi/history/balancelist";
                break;

            case STORE_CHECK:
                processURL = "/nodeapi/receipt/hnamecheck";
                break;

            case ADD_RECEIPT:
                processURL = "/nodeapi/receipt/addreceipt";
                break;

            case ADD_NEW_RECEIPT:
                processURL = "/nodeapi/receipt/addnewreceipt";
                break;

            case DEFAULT_BUDGET:
                processURL = "/nodeapi/budget/defaultbudget";
                break;

            case CHANGE_BUDGET:
                processURL = "/nodeapi/budget/changebudget";
                break;

            case ACCOUNT_REFRESH:
                processURL = "/nodeapi/account/accountrefresh";
                break;
        }
        return processURL;
    }

}
