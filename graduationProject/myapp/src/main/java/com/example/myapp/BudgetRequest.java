package com.example.myapp;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class BudgetRequest {

    private String userID;
    private String budget;
    private RequestInfo.RequestType rType;
    private Context context;


    //예산요청
    BudgetRequest(String userID, RequestInfo.RequestType rType, Context context){

        this.userID = userID;
        this.rType = rType;
        this.context = context;

    }


    //예산 변경 요청
    BudgetRequest(String userID, String budget, RequestInfo.RequestType rType, Context context){

        this.userID = userID;
        this.budget = budget;
        this.rType = rType;
        this.context = context;

    }

    public interface VolleyCallback{
        void onSuccess(String budget);
    }

    public void GetBudgetHandler(final VolleyCallback callback){
        RequestInfo requestInfo = new RequestInfo(rType);

        String url = "http://" + requestInfo.GetRequestIP() + ":" + requestInfo.GetRequestPORT() + requestInfo.GetProcessURL();

        StringRequest request = new StringRequest(Request.Method.POST, url,
                response -> GetBudgetResponse(response, callback),
                error -> { error.getMessage(); error.printStackTrace(); })
        {
            @Override
            protected Map<String, String> getParams(){
                return GetBudgetRequest();
            }
        };
        request.setShouldCache(false);
        Volley.newRequestQueue(context).add(request);
        Log.d("요청 url: ", url);

    }

    /*
        GetBudgetRequest(): Map<String, String>
        = 예산 확인 요청 전달 파라미터 설정 함수
    */

    private Map<String, String> GetBudgetRequest(){

        Map<String, String> params = new HashMap<>();

        params.put("id", userID);
        return params;

    }

    /*
        GetBudgetResponse(String): void
        = 예산 확인 요청 응답 처리 함수
    */

    private void GetBudgetResponse(String response, final VolleyCallback callback){
        try{
            Log.d("onResponse 호출 ", response);

            JSONObject json = new JSONObject(response);
            String resultString = (String) json.get("message");

            switch (resultString) {
                case "success":
                    String budget = (String) json.get("data");
                    callback.onSuccess(budget);
                    break;

                case "fail":
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


    public void ChangeBudgetHandler(final VolleyCallback callback){
        RequestInfo requestInfo = new RequestInfo(rType);

        String url = "http://" + requestInfo.GetRequestIP() + ":" + requestInfo.GetRequestPORT() + requestInfo.GetProcessURL();

        StringRequest request = new StringRequest(Request.Method.POST, url,
                response -> ChangeBudgetResponse(response, callback),
                error -> { error.getMessage(); error.printStackTrace(); })
        {
            @Override
            protected Map<String, String> getParams(){
                return ChangeBudgetRequest();
            }
        };
        request.setShouldCache(false);
        Volley.newRequestQueue(context).add(request);
        Log.d("요청 url: ", url);

    }

    /*
        ChangeBudgetRequest(): Map<String, String>
        = 예산 변경 요청 전달 파라미터 설정 함수
    */

    private Map<String, String> ChangeBudgetRequest(){

        Map<String, String> params = new HashMap<>();

        params.put("id", userID);
        params.put("budget", budget);
        return params;

    }

    /*
        ChangeBudgetRequest(String): void
        = 예산 변경 요청 응답 처리 함수
    */

    private void ChangeBudgetResponse(String response, final VolleyCallback callback){
        try{
            Log.d("onResponse 호출 ", response);

            JSONObject json = new JSONObject(response);
            String resultString = (String) json.get("message");

            switch (resultString) {
                case "success":
                    callback.onSuccess("");
                    break;

                case "fail":
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
