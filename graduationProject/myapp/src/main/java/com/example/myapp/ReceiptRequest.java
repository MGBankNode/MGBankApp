package com.example.myapp;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ReceiptRequest {

    private String hDate;
    private String hValue;
    private String hName;
    private String userID;
    private String cId;
    private RequestInfo.RequestType rType;
    private Context context;


    //상점명 확인 요청시
    ReceiptRequest(String hName, RequestInfo.RequestType rType, Context context){

        this.hName = hName;
        this.rType = rType;
        this.context = context;

    }

    //영수증 내역 추가 요청시
    ReceiptRequest(String hDate, String hValue, String hName, String userID, String cId, RequestInfo.RequestType rType, Context context){

        this.hDate = hDate;
        this.hValue = hValue;
        this.hName = hName;
        this.userID = userID;
        this.cId = cId;
        this.rType = rType;
        this.context = context;

    }

    public interface VolleyCallback{
        void onSuccess(String id);
    }

    public void StoreRequest(final VolleyCallback callback){
        RequestInfo requestInfo = new RequestInfo(rType);

        String url = "http://" + requestInfo.GetRequestIP() + ":" + requestInfo.GetRequestPORT() + requestInfo.GetProcessURL();

        StringRequest request = new StringRequest(Request.Method.POST, url,
                response -> HNameCheckResponse(response, callback),
                error -> { error.getMessage(); error.printStackTrace(); })
        {
            @Override
            protected Map<String, String> getParams(){
                return HNameCheckRequest();
            }
        };
        request.setShouldCache(false);
        Volley.newRequestQueue(context).add(request);
        Log.d("요청 url: ", url);

    }

     /*
        HNameCheckRequest(): Map<String, String>
        = 상점명 확인 요청 전달 파라미터 설정 함수
    */

    private Map<String, String> HNameCheckRequest(){

        Map<String, String> params = new HashMap<>();

        params.put("hName", hName);
        return params;

    }

    /*
        HNameCheckResponse(String): void
        = 상점명 확인 요청 응답 처리 함수
    */

    private void HNameCheckResponse(String response, final VolleyCallback callback){
        try{
            Log.d("onResponse 호출 ", response);

            JSONObject json = new JSONObject(response);
            String resultString = (String) json.get("message");

            switch (resultString) {
                case "success":
                    String cId = (String) json.get("data");
                    callback.onSuccess(cId);
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

    public void AddReceipt(final VolleyCallback callback){
        RequestInfo requestInfo = new RequestInfo(rType);

        String url = "http://" + requestInfo.GetRequestIP() + ":" + requestInfo.GetRequestPORT() + requestInfo.GetProcessURL();

        StringRequest request = new StringRequest(Request.Method.POST, url,
                response -> AddReceiptResponse(response, callback),
                error -> { error.getMessage(); error.printStackTrace(); })
        {
            @Override
            protected Map<String, String> getParams(){
                return AddReceiptRequest();
            }
        };
        request.setShouldCache(false);
        Volley.newRequestQueue(context).add(request);
        Log.d("요청 url: ", url);

    }

     /*
        AddReceiptRequest(): Map<String, String>
        = 영수증 추가 요청 전달 파라미터 설정 함수
    */

    private Map<String, String> AddReceiptRequest(){

        Map<String, String> params = new HashMap<>();

        params.put("hDate", hDate);
        params.put("hValue", hValue);
        params.put("hName", hName);
        params.put("id", userID);
        params.put("cId", cId);

        return params;

    }

    /*
        AddReceiptResponse(String): void
        = 영수증 추가 요청 응답 처리 함수
    */

    private void AddReceiptResponse(String response, final VolleyCallback callback){
        try{
            Log.d("onResponse 호출 ", response);

            JSONObject json = new JSONObject(response);
            String resultString = (String) json.get("message");

            switch (resultString) {
                case "success":
                    String hId = (String) json.get("data");
                    callback.onSuccess(hId);
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
