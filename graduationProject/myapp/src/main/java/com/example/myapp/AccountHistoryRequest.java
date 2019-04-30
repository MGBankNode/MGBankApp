package com.example.myapp;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AccountHistoryRequest {

    private String userID;
    private String sDate;
    private String lDate;
    private RequestInfo.RequestType rType;
    private Context context;
    private AccountHistoryInfo[] accountHistoryInfo;


    AccountHistoryRequest(String userID, String sDate, String lDate, RequestInfo.RequestType rType, Context context){
        this.userID = userID;
        this.sDate = sDate;
        this.lDate = lDate;
        this.rType = rType;
        this.context = context;
    }

    public interface VolleyCallback{
        void onSuccess(AccountHistoryInfo[] accountHistoryInfo);
    }

    public void Request(final VolleyCallback callback){
        RequestInfo requestInfo = new RequestInfo(rType);

        String url = "http://" + requestInfo.GetRequestIP() + ":" + requestInfo.GetRequestPORT() + requestInfo.GetProcessURL();

        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response){
                        HistoryResponse(response, callback);
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
                return HistoryRequest();
            }
        };
        request.setShouldCache(false);
        Volley.newRequestQueue(context).add(request);
        Log.d("요청 url: ", url);

    }

      /*
        HistoryRequest(): Map<String, String>
        = 내역 조회 요청 전달 파라미터 설정 함수
    */

    private Map<String, String> HistoryRequest(){

        Map<String, String> params = new HashMap<>();

        params.put("id", userID);
        params.put("sDate", sDate);
        params.put("lDate", lDate);
        return params;

    }

    /*
        HistoryResponse(String): void
        = 내역 조회 요청 응답 처리 함수
    */

    private void HistoryResponse(String response, final  VolleyCallback callback){
        try{
            Log.d("onResponse 호출 ", response);

            JSONObject json = new JSONObject(response);
            String resultString = (String) json.get("message");

            switch (resultString) {
                case "success":

                    JSONArray dataArray = json.getJSONArray("history");
                    Toast.makeText(context, dataArray.toString(), Toast.LENGTH_LONG).show();

                    accountHistoryInfo = new AccountHistoryInfo[dataArray.length()];

                    for(int i = 0; i < dataArray.length(); ++i){

                        JSONObject record = dataArray.getJSONObject(i);

                        String hDate = record.getString("hDate");
                        int hType = record.getInt("hType");
                        String hValue = record.getString("hValue");
                        String hName = record.getString("hName");
                        String aBalance = record.getString("aBalance");
                        String cName = record.getString("cName");

                        String hTypeName;

                        if(hType == 0){

                            hTypeName = "입금";

                        }else{

                            hTypeName = "출금";

                        }

                        accountHistoryInfo[i] = new AccountHistoryInfo(hDate, hTypeName, hValue, hName, aBalance, cName);

                    }
                    callback.onSuccess(accountHistoryInfo);
                    break;

                case "error":
                    accountHistoryInfo = null;
                    break;

                case "db_fail":
                    accountHistoryInfo = null;
                    break;

            }

        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
