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

public class AnalysisRequest {

    private String userID;
    private String dates;
    private String sDate;
    private String lDate;
    private RequestInfo.RequestType rType;
    private Context context;
    private AnalysisInfo[] analysisWeekInfo;
    private AnalysisInfo[] analysisDailyInfo;
    private AnalysisInfo[] analysisMonthInfo;

    AnalysisRequest(String userID, String dates, RequestInfo.RequestType rType, Context context){
        this.userID = userID;
        this.dates = dates;
        this.rType = rType;
        this.context = context;
    }

    AnalysisRequest(String userID, String sDate, String lDate, RequestInfo.RequestType rType, Context context){
        this.userID = userID;
        this.sDate = sDate;
        this.lDate = lDate;
        this.rType = rType;
        this.context = context;
    }

    public interface VolleyCallback{
        void onSuccess(AnalysisInfo[] analysisInfo);
    }

    public void WeekRequestHandler(final VolleyCallback callback){
        RequestInfo requestInfo = new RequestInfo(rType);

        String url = "http://" + requestInfo.GetRequestIP() + ":" + requestInfo.GetRequestPORT() + requestInfo.GetProcessURL();

        StringRequest request = new StringRequest(Request.Method.POST, url,
                response -> WeekResponse(response, callback),
                error -> { error.getMessage(); error.printStackTrace(); }
        ){
            @Override
            protected Map<String, String> getParams(){
                return WeekRequest();
            }
        };
        request.setShouldCache(false);
        Volley.newRequestQueue(context).add(request);
        Log.d("요청 url: ", url);

    }

    public void DailyRequestHandler(final VolleyCallback callback){
        RequestInfo requestInfo = new RequestInfo(rType);

        String url = "http://" + requestInfo.GetRequestIP() + ":" + requestInfo.GetRequestPORT() + requestInfo.GetProcessURL();

        StringRequest request = new StringRequest(Request.Method.POST, url,
                response -> DailyResponse(response, callback),
                error -> { error.getMessage(); error.printStackTrace(); }
        ){
            @Override
            protected Map<String, String> getParams(){
                return DailyRequest();
            }
        };
        request.setShouldCache(false);
        Volley.newRequestQueue(context).add(request);
        Log.d("요청 url: ", url);

    }


    public void MonthRequestHandler(final VolleyCallback callback){
        RequestInfo requestInfo = new RequestInfo(rType);

        String url = "http://" + requestInfo.GetRequestIP() + ":" + requestInfo.GetRequestPORT() + requestInfo.GetProcessURL();

        StringRequest request = new StringRequest(Request.Method.POST, url,
                response -> MonthResponse(response, callback),
                error -> { error.getMessage(); error.printStackTrace(); }
        ){
            @Override
            protected Map<String, String> getParams(){
                return MonthRequest();
            }
        };
        request.setShouldCache(false);
        Volley.newRequestQueue(context).add(request);
        Log.d("요청 url: ", url);

    }

      /*
        MonthRequest(): Map<String, String>
        = 월별 분석 요청 전달 파라미터 설정 함수
    */

    private Map<String, String> MonthRequest(){

        Map<String, String> params = new HashMap<>();

        params.put("id", userID);
        params.put("dates", dates);
        return params;

    }

    /*
        MonthResponse(String, final VolleyCallback): void
        = 월별 분석 요청 응답 처리 함수
    */

    private void MonthResponse(String response, final VolleyCallback callback){
        try{
            Log.d("onResponse 호출 ", response);

            JSONObject json = new JSONObject(response);
            String resultString = (String) json.get("message");

            switch (resultString) {
                case "success":
                    JSONArray dataArray = json.getJSONArray("monthPattern");

                    analysisMonthInfo = new AnalysisInfo[dataArray.length()];

                    for(int i = 0; i < dataArray.length(); ++i){

                        JSONObject record = dataArray.getJSONObject(i);

                        String month = record.getString("month");
                        String monthSum = record.getString("monthSum");

                        analysisMonthInfo[i] = new AnalysisInfo(month, monthSum);

                    }

                    callback.onSuccess(analysisMonthInfo);
                    break;

                case "error":
                    analysisMonthInfo = null;
                    break;

                case "db_fail":
                    analysisMonthInfo = null;
                    break;

            }

        } catch (Exception e){
            e.printStackTrace();
        }
    }


      /*
        WeekRequest(): Map<String, String>
        = 주차 분석 요청 전달 파라미터 설정 함수
    */

    private Map<String, String> WeekRequest(){

        Map<String, String> params = new HashMap<>();

        params.put("id", userID);
        params.put("dates", dates);
        return params;

    }

    /*
        WeekResponse(String, final VolleyCallback): void
        = 주차 분석 요청 응답 처리 함수
    */

    private void WeekResponse(String response, final VolleyCallback callback){
        try{
            Log.d("onResponse 호출 ", response);

            JSONObject json = new JSONObject(response);
            String resultString = (String) json.get("message");

            switch (resultString) {
                case "success":

                    JSONArray dataArray = json.getJSONArray("weekPattern");
                    //Toast.makeText(context, dataArray.toString(), Toast.LENGTH_LONG).show();

                    analysisWeekInfo = new AnalysisInfo[dataArray.length()];

                    for(int i = 0; i < dataArray.length(); ++i){

                        JSONObject record = dataArray.getJSONObject(i);

                        String week = record.getString("week");
                        String weekSum = record.getString("weekSum");

                        analysisWeekInfo[i] = new AnalysisInfo(week, weekSum);

                    }

                    callback.onSuccess(analysisWeekInfo);
                    break;

                case "error":
                    analysisWeekInfo = null;
                    break;

                case "db_fail":
                    analysisWeekInfo = null;
                    break;

            }

        } catch (Exception e){
            e.printStackTrace();
        }
    }


    /*
        DailyRequest(): Map<String, String>
        = 일별 분석 요청 전달 파라미터 설정 함수
    */

    private Map<String, String> DailyRequest(){

        Map<String, String> params = new HashMap<>();

        params.put("id", userID);
        params.put("sDate", sDate);
        params.put("lDate", lDate);
        return params;

    }

    /*
        DailyResponse(String, final VolleyCallback): void
        = 일별 분석 요청 응답 처리 함수
    */

    private void DailyResponse(String response, final VolleyCallback callback){
        try{
            Log.d("onResponse 호출 ", response);

            JSONObject json = new JSONObject(response);
            String resultString = (String) json.get("message");

            switch (resultString) {
                case "success":

                    JSONArray dataArray = json.getJSONArray("dailyPattern");
                    //Toast.makeText(context, dataArray.toString(), Toast.LENGTH_LONG).show();

                    analysisDailyInfo = new AnalysisInfo[dataArray.length()];

                    for(int i = 0; i < dataArray.length(); ++i){

                        JSONObject record = dataArray.getJSONObject(i);

                        String daily = record.getString("daily");
                        String dailySum = record.getString("dailySum");

                        analysisDailyInfo[i] = new AnalysisInfo(daily, dailySum);

                    }

                    callback.onSuccess(analysisDailyInfo);
                    break;

                case "error":
                    analysisDailyInfo = null;
                    break;

                case "db_fail":
                    analysisDailyInfo = null;
                    break;

            }

        } catch (Exception e){
            e.printStackTrace();
        }
    }

}
