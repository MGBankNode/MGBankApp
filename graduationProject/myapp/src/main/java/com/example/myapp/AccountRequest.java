package com.example.myapp;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AccountRequest {


    private String userID;
    private RequestInfo.RequestType rType;
    private Context context;

    AccountRequest(String userID, RequestInfo.RequestType rType, Context context){
        this.userID = userID;
        this.rType = rType;
        this.context = context;
    }
    /*
        AccountRefreshHandler: void
        = 새로고침 요청 처리 핸들러
    */

    public interface VolleyCallback{
        void onSuccess();
    }

    public void AccountRefreshHandler(final VolleyCallback callback){
        RequestInfo requestInfo = new RequestInfo(rType);

        String url = "http://" + requestInfo.GetRequestIP() + ":" + requestInfo.GetRequestPORT() + requestInfo.GetProcessURL();

        StringRequest request = new StringRequest(Request.Method.POST, url,
                response -> AccountRefreshResponse(response, callback),
                (error) -> { error.getMessage(); error.printStackTrace();}

        ){
            @Override
            protected Map<String, String> getParams(){
                return AccountRefreshRequest();
            }
        };
        request.setShouldCache(false);
        Volley.newRequestQueue(context).add(request);
        Log.d("요청 url: ", url);
    }

    /*
        AccountRefreshRequest(): Map<String, String>
        = 새로고침 요청 전달 파라미터 설정 함수
    */

    private Map<String, String> AccountRefreshRequest(){
        Map<String, String> params = new HashMap<>();

        params.put("id", userID);

        return params;
    }

    /*
        AccountRefreshResponse(String): void
        = 새로고침 요청 응답 처리 함수
    */

    private void AccountRefreshResponse(String response, final VolleyCallback callback){
        try{
            Log.d("onResponse 호출 ", response);

            JSONObject json = new JSONObject(response);
            String resultString = (String) json.get("message");

            switch (resultString) {
                case "success":
                    callback.onSuccess();
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
