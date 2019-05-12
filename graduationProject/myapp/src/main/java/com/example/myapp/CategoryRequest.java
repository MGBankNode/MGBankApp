package com.example.myapp;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class CategoryRequest {

    private String userID;
    private int hId;
    private int prevCategory;
    private int curCategory;
    private Context context;
    private RequestInfo.RequestType rType;

    CategoryRequest(String userID, int hId, int prevCategory, int curCategory, Context context, RequestInfo.RequestType rType){
        this.userID = userID;
        this.hId = hId;
        this.prevCategory = prevCategory;
        this.curCategory = curCategory;
        this.context = context;
        this.rType = rType;
    }

    public interface VolleyCallback{
        void onSuccess();
        void onFail();
        void onError();
    }

    public void UpdateCategoryHandler(final VolleyCallback callback){
        RequestInfo requestInfo = new RequestInfo(rType);
        String url = "http://" + requestInfo.GetRequestIP() + ":" + requestInfo.GetRequestPORT() + requestInfo.GetProcessURL();

        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response){
                        UpdateCategoryResponse(response, callback);
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
                return UpdateCategoryRequest();
            }
        };
        request.setShouldCache(false);
        Volley.newRequestQueue(context).add(request);
        Log.d("요청 url: ", url);
    }

    /*
        UpdateCategoryRequest(): Map<String, String>
        = 카테고리 수정 요청 전달 파라미터 설정 함수
    */

    private Map<String, String> UpdateCategoryRequest(){

        Map<String, String> params = new HashMap<>();

        params.put("id", userID);
        params.put("hId", String.valueOf(hId));
        params.put("prevCategory", String.valueOf(prevCategory));
        params.put("curCategory", String.valueOf(curCategory));
        return params;

    }

    /*
        UpdateCategoryResponse(String): void
        =  카테고리 수정 요청 응답 처리 함수
    */

    private void UpdateCategoryResponse(String response, final VolleyCallback callback){
        try{
            Log.d("onResponse 호출 ", response);

            JSONObject json = new JSONObject(response);
            String resultString = (String) json.get("message");

            switch (resultString) {
                case "success":
                    callback.onSuccess();
                    break;

                case "error":
                    callback.onError();
                    break;

                case "db_fail":
                    callback.onFail();
                    break;

            }

        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
