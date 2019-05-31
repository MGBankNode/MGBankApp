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

public class HistoryRequest {

    private String userID;
    private String aNum;
    private String sDate;
    private String lDate;
    private RequestInfo.RequestType rType;
    private Context context;
    private HistoryInfo[] historyInfo;
    private DailyHistoryInfo[] dailyHistoryInfo;

    //계좌 + 잔액 리스트 요청
    HistoryRequest(String userID,RequestInfo.RequestType rType, Context context){
        this.userID = userID;
        this.rType = rType;
        this.context = context;
    }

    //계좌별 내역 요청
    HistoryRequest(String userID, String aNum, RequestInfo.RequestType rType, Context context){
        this.userID = userID;
        this.aNum = aNum;
        this.rType = rType;
        this.context = context;
    }

    HistoryRequest(String userID, String sDate, String lDate, RequestInfo.RequestType rType, Context context){
        this.userID = userID;
        this.sDate = sDate;
        this.lDate = lDate;
        this.rType = rType;
        this.context = context;
    }

    public interface VolleyCallback{
        void onSuccess(HistoryInfo[] historyInfo, DailyHistoryInfo[] dailyHistoryInfo);
    }

    public void Request(final VolleyCallback callback){
        RequestInfo requestInfo = new RequestInfo(rType);

        String url = "http://" + requestInfo.GetRequestIP() + ":" + requestInfo.GetRequestPORT() + requestInfo.GetProcessURL();

        StringRequest request = new StringRequest(Request.Method.POST, url,
                response -> AccountHistoryResponse(response, callback),
                error -> { error.getMessage(); error.printStackTrace(); })
        {
            @Override
            protected Map<String, String> getParams(){
                return AccountHistoryRequest();
            }
        };
        request.setShouldCache(false);
        Volley.newRequestQueue(context).add(request);
        Log.d("요청 url: ", url);

    }

    public void HomeRequest(final VolleyCallback callback){
        RequestInfo requestInfo = new RequestInfo(rType);

        String url = "http://" + requestInfo.GetRequestIP() + ":" + requestInfo.GetRequestPORT() + requestInfo.GetProcessURL();

        StringRequest request = new StringRequest(Request.Method.POST, url,
                response-> HomeHistoryResponse(response, callback),
                error -> { error.getMessage(); error.printStackTrace(); })
        {
            @Override
            protected Map<String, String> getParams(){
                return HomeHistoryRequest();
            }
        };
        request.setShouldCache(false);
        Volley.newRequestQueue(context).add(request);
        Log.d("요청 url: ", url);

    }

    public void BalanceListRequest(final VolleyCallback callback){
        RequestInfo requestInfo = new RequestInfo(rType);

        String url = "http://" + requestInfo.GetRequestIP() + ":" + requestInfo.GetRequestPORT() + requestInfo.GetProcessURL();

        StringRequest request = new StringRequest(Request.Method.POST, url,
                response -> ListResponse(response, callback),
                error -> { error.getMessage(); error.printStackTrace(); })
        {
            @Override
            protected Map<String, String> getParams(){
                return ListRequest();
            }
        };
        request.setShouldCache(false);
        Volley.newRequestQueue(context).add(request);
        Log.d("요청 url: ", url);

    }

    public void AccountByRequest(final VolleyCallback callback){
        RequestInfo requestInfo = new RequestInfo(rType);

        String url = "http://" + requestInfo.GetRequestIP() + ":" + requestInfo.GetRequestPORT() + requestInfo.GetProcessURL();

        StringRequest request = new StringRequest(Request.Method.POST, url,
                response -> AccountByHistoryResponse(response, callback),
                error -> { error.getMessage(); error.printStackTrace(); })
        {
            @Override
            protected Map<String, String> getParams(){
                return AccountByHistoryRequest();
            }
        };
        request.setShouldCache(false);
        Volley.newRequestQueue(context).add(request);
        Log.d("요청 url: ", url);

    }


      /*
        AccountHistoryRequest(): Map<String, String>
        = 내역 조회 요청 전달 파라미터 설정 함수
    */

    private Map<String, String> AccountHistoryRequest(){

        Map<String, String> params = new HashMap<>();

        params.put("id", userID);
        params.put("sDate", sDate);
        params.put("lDate", lDate);
        return params;

    }

    /*
        AccountHistoryResponse(String): void
        = 내역 조회 요청 응답 처리 함수
    */

    private void AccountHistoryResponse(String response, final VolleyCallback callback){
        try{
            Log.d("onResponse 호출 ", response);

            JSONObject json = new JSONObject(response);
            String resultString = (String) json.get("message");

            switch (resultString) {
                case "success":

                    JSONArray dataArray = json.getJSONArray("history");
                    //Toast.makeText(context, dataArray.toString(), Toast.LENGTH_LONG).show();

                    historyInfo = new HistoryInfo[dataArray.length()];

                    for(int i = 0; i < dataArray.length(); ++i){

                        JSONObject record = dataArray.getJSONObject(i);

                        int hId = record.getInt("hId");
                        String hDate = record.getString("hDate");
                        String hType = record.getString("hType");
                        String hValue = record.getString("hValue");
                        String hName = record.getString("hName");
                        String aBalance = record.getString("aBalance");
                        String aType = record.getString("aType");
                        String cType = record.getString("cType");
                        String cName = record.getString("cName");


                        historyInfo[i] = new HistoryInfo(hId, hDate, hType, hValue, hName, aBalance, aType, cType, cName);

                    }

                    JSONArray dailyDataArray = json.getJSONArray("daily_history");
                    dailyHistoryInfo = new DailyHistoryInfo[dailyDataArray.length()];

                    for(int i = 0; i < dailyDataArray.length(); ++i){

                        JSONObject record = dailyDataArray.getJSONObject(i);

                        String day = record.getString("day");
                        String dailyBenefit = record.getString("benefit");
                        String dailyLoss = record.getString("loss");

                        dailyHistoryInfo[i] = new DailyHistoryInfo(day, dailyBenefit, dailyLoss);

                    }

                    callback.onSuccess(historyInfo, dailyHistoryInfo);
                    break;

                case "error":
                    historyInfo = null;
                    break;

                case "db_fail":
                    historyInfo = null;
                    break;

            }

        } catch (Exception e){
            e.printStackTrace();
        }
    }


    /*
        HomeHistoryRequest(): Map<String, String>
        = 홈 내역 조회 요청 전달 파라미터 설정 함수
    */

    private Map<String, String> HomeHistoryRequest(){

        Map<String, String> params = new HashMap<>();

        params.put("id", userID);
        params.put("sDate", sDate);
        params.put("lDate", lDate);
        return params;

    }

    /*
        HomeHistoryResponse(String): void
        = 홈 내역 조회 요청 응답 처리 함수
    */

    private void HomeHistoryResponse(String response, final VolleyCallback callback){
        try{
            Log.d("onResponse 호출 ", response);

            JSONObject json = new JSONObject(response);
            String resultString = (String) json.get("message");

            switch (resultString) {
                case "success":

                    JSONArray dataArray = json.getJSONArray("history");
                    //Toast.makeText(context, dataArray.toString(), Toast.LENGTH_LONG).show();

                    historyInfo = new HistoryInfo[dataArray.length()];

                    for(int i = 0; i < dataArray.length(); ++i){

                        JSONObject record = dataArray.getJSONObject(i);

                        int hId = record.getInt("hId");
                        String hDate = record.getString("hDate");
                        String hValue = record.getString("hValue");
                        String hName = record.getString("hName");
                        String cName = record.getString("cName");

                        historyInfo[i] = new HistoryInfo(hId, hDate,hValue, hName, cName);

                    }

                    callback.onSuccess(historyInfo, null);
                    break;

                case "error":
                    historyInfo = null;
                    break;

                case "db_fail":
                    historyInfo = null;
                    break;

            }

        } catch (Exception e){
            e.printStackTrace();
        }
    }

    /*
        ListRequest(): Map<String, String>
        = 계좌별 잔액 조회 요청 전달 파라미터 설정 함수
    */

    private Map<String, String> ListRequest(){

        Map<String, String> params = new HashMap<>();

        params.put("id", userID);
        return params;

    }

    /*
        ListResponse(String): void
        = 계좌별 잔액 조회 요청 응답 처리 함수
    */

    private void ListResponse(String response, final VolleyCallback callback){
        try{
            Log.d("onResponse 호출 ", response);

            JSONObject json = new JSONObject(response);
            String resultString = (String) json.get("message");

            switch (resultString) {
                case "success":

                    JSONArray dataArray = json.getJSONArray("data");

                    historyInfo = new HistoryInfo[dataArray.length()];

                    for(int i = 0; i < dataArray.length(); ++i){

                        JSONObject record = dataArray.getJSONObject(i);

                        String aNum = record.getString("aNum");
                        String aBalance = record.getString("aBalance");
                        String aType = record.getString("aType");

                        historyInfo[i] = new HistoryInfo(aNum, aBalance, aType);
                    }

                    callback.onSuccess(historyInfo, null);
                    break;

                case "error":
                    historyInfo = null;
                    break;

                case "db_fail":
                    historyInfo = null;
                    break;

            }

        } catch (Exception e){
            e.printStackTrace();
        }
    }

    /*
        AccountByHistoryRequest(): Map<String, String>
        = 계좌별 내역 조회 요청 전달 파라미터 설정 함수
    */

    private Map<String, String> AccountByHistoryRequest(){

        Map<String, String> params = new HashMap<>();

        params.put("aNum", userID);
        params.put("sDate", sDate);
        params.put("lDate", lDate);
        return params;

    }

    /*
        AccountByHistoryResponse(String): void
        = 계좌별 내역 조회 요청 응답 처리 함수
    */

    private void AccountByHistoryResponse(String response, final VolleyCallback callback){
        try{
            Log.d("onResponse 호출 ", response);

            JSONObject json = new JSONObject(response);
            String resultString = (String) json.get("message");

            switch (resultString) {
                case "success":

                    JSONArray dataArray = json.getJSONArray("history");

                    historyInfo = new HistoryInfo[dataArray.length()];

                    for(int i = 0; i < dataArray.length(); ++i){

                        JSONObject record = dataArray.getJSONObject(i);

                        int hId = record.getInt("hId");
                        String hDate = record.getString("hDate");
                        String hType = record.getString("hType");
                        String hValue = record.getString("hValue");
                        String hName = record.getString("hName");
                        String aBalance = record.getString("aBalance");
                        String aType = record.getString("aType");
                        String cType = record.getString("cType");
                        String cName = record.getString("cName");

                        historyInfo[i] = new HistoryInfo(hId, hDate, hType, hValue, hName, aBalance, aType, cType, cName);

                    }


                    JSONArray dailyDataArray = json.getJSONArray("daily_history");
                    dailyHistoryInfo = new DailyHistoryInfo[dailyDataArray.length()];

                    for(int i = 0; i < dailyDataArray.length(); ++i){

                        JSONObject record = dailyDataArray.getJSONObject(i);

                        String day = record.getString("day");
                        String dailyBenefit = record.getString("benefit");
                        String dailyLoss = record.getString("loss");

                        dailyHistoryInfo[i] = new DailyHistoryInfo(day, dailyBenefit, dailyLoss);

                    }

                    callback.onSuccess(historyInfo, dailyHistoryInfo);
                    break;

                case "error":
                    historyInfo = null;
                    break;

                case "db_fail":
                    historyInfo = null;
                    break;

            }

        } catch (Exception e){
            e.printStackTrace();
        }
    }
}