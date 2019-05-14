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

    private String hName;
    private RequestInfo.RequestType rType;
    private Context context;

    ReceiptRequest(String hName, RequestInfo.RequestType rType, Context context){

        this.hName = hName;
        this.rType = rType;
        this.context = context;

    }

    public interface VolleyCallback{
        void onSuccess(String cId);
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


}
