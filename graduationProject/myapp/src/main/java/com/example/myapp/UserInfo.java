package com.example.myapp;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

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

    public interface VolleyCallback{
        void onSuccess(String aBalance);
    }

    public void UpdateABalance(Context context, final VolleyCallback callback){
        RequestInfo requestInfo = new RequestInfo(RequestInfo.RequestType.ACCOUNT_BALANCE);

        String url = "http://" + requestInfo.GetRequestIP() + ":" + requestInfo.GetRequestPORT() + requestInfo.GetProcessURL();

        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response){
                        BalanceResponse(response, callback);
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error){
                        error.printStackTrace();
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams(){
                return BalanceRequest();
            }
        };
        request.setShouldCache(false);
        Volley.newRequestQueue(context).add(request);
        Log.d("요청 url: ", url);
    }
      /*
        BalanceRequest(): Map<String, String>
        = 잔액 조회 요청 전달 파라미터 설정 함수
    */

    private Map<String, String> BalanceRequest(){

        Map<String, String> params = new HashMap<>();

        params.put("id", this.userID);
        return params;

    }

    /*
        HistoryResponse(String): void
        = 내역 조회 요청 응답 처리 함수
    */

    private void BalanceResponse(String response, final VolleyCallback callback){
        try{
            Log.d("onResponse 호출 ", response);

            JSONObject json = new JSONObject(response);
            String resultString = (String) json.get("message");

            switch (resultString) {
                case "success":

                    String aBalance = (String) json.get("aBalance");

                    callback.onSuccess(aBalance);
                    break;

                case "error":
                    break;

                case "db_fail":
                    break;

            }

        } catch (Exception e){
            e.printStackTrace();
        }
    }


}