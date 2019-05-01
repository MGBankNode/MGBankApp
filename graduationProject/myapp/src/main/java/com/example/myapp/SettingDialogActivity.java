package com.example.myapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


/*
    SettingDialogActivity
    = 설정 모달 창 엑티비티
 */

public class SettingDialogActivity extends Activity {

    Button switchFinishBtn;
    Switch pushSwitch;
    DeviceInfo myDeviceInfo;
    int originValue = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_dialog);

        switchFinishBtn = findViewById(R.id.switchFinishBtn);
        pushSwitch = findViewById(R.id.pushSwitch);

        Intent intent = getIntent();
        myDeviceInfo = (DeviceInfo) intent.getSerializableExtra("DeviceInfoObject");
        String deviceCheckResult = intent.getStringExtra("DeviceCheckResult");

        if (deviceCheckResult.equals("YES")) {

            pushSwitch.setChecked(true);
            originValue = 1;

        } else if (deviceCheckResult.equals("NO")) {

            pushSwitch.setChecked(false);
            originValue = 0;

        }

        switchFinishBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pushSwitch.isChecked()) {

                    if (originValue == 1) {

                        Intent intent = new Intent();
                        intent.putExtra("DeviceCheckResult", "YES");
                        setResult(RESULT_OK, intent);
                        finish();

                    } else {

                        AddDeviceHandler();

                    }

                } else {

                    if (originValue == 1) {

                        DeleteDeviceHandler();

                    } else {

                        Intent intent = new Intent();
                        intent.putExtra("DeviceCheckResult", "NO");
                        setResult(RESULT_OK, intent);
                        finish();

                    }

                }
            }
        });
    }

    /*
        AddDeviceHandler: void
        = 단말 추가 요청 처리 핸들러
    */

    public void AddDeviceHandler(){
        RequestInfo requestInfo = new RequestInfo(RequestInfo.RequestType.ADD_DEVICE);

        String url = "http://" + requestInfo.GetRequestIP() + ":" + requestInfo.GetRequestPORT() + requestInfo.GetProcessURL();

        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response){
                        AddDeviceResponse(response);
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
                return AddDeviceRequest();
            }
        };
        request.setShouldCache(false);
        Volley.newRequestQueue(getApplicationContext()).add(request);
        Log.d("요청 url: ", url);
    }

    /*
        AddDeviceRequest(): Map<String, String>
        = 단말 추가 요청 전달 파라미터 설정 함수
    */

    private Map<String, String> AddDeviceRequest(){
        Map<String, String> params = new HashMap<>();

        params.put("mobile", myDeviceInfo.getMobile());
        params.put("osVersion", myDeviceInfo.getOsVersion());
        params.put("model", myDeviceInfo.getModel());
        params.put("display", myDeviceInfo.getDisplay());
        params.put("manufacturer", myDeviceInfo.getManufacturer());
        params.put("macAddress", myDeviceInfo.getMacAddress());

        String regId = FirebaseInstanceId.getInstance().getToken();
        params.put("registrationId", regId);

        return params;
    }

    /*
        AddDeviceResponse(String): void
        = 단말 추가 요청 응답 처리 함수
    */

    private void AddDeviceResponse(String response){
        try{
            Log.d("onResponse 호출 ", response);

            JSONObject json = new JSONObject(response);
            String resultString = (String) json.get("message");

            switch (resultString) {
                case "success":
                    ShowToast("Push 알림 ON");

                    Intent intent = new Intent();
                    intent.putExtra("DeviceCheckResult", "YES");
                    setResult(RESULT_OK, intent);

                    finish();
                    break;

                case "fail":
                    ShowToast("Push 알림 ON 실패");
                    pushSwitch.setChecked(false);
                    break;

                case "error":
                    ShowToast("단말 추가  중 오류 발생");
                    break;

                case "db_fail":
                    ShowToast("연결 오류");
                    break;

            }

        } catch (Exception e){
            e.printStackTrace();
        }
    }

    /*
        DeleteDeviceHandler: void
        = 단말 제거 요청 처리 핸들러
    */

    public void DeleteDeviceHandler(){
        RequestInfo requestInfo = new RequestInfo(RequestInfo.RequestType.DELETE_DEVICE);

        String url = "http://" + requestInfo.GetRequestIP() + ":" + requestInfo.GetRequestPORT() + requestInfo.GetProcessURL();

        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response){
                        DeleteDeviceResponse(response);
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
                return DeleteDeviceRequest();
            }
        };
        request.setShouldCache(false);
        Volley.newRequestQueue(getApplicationContext()).add(request);
        Log.d("요청 url: ", url);
    }

    /*
        DeleteDeviceRequest(): Map<String, String>
        = 단말 제거 요청 전달 파라미터 설정 함수
    */

    private Map<String, String> DeleteDeviceRequest(){
        Map<String, String> params = new HashMap<>();

        params.put("mobile", myDeviceInfo.getMobile());

        return params;
    }

    /*
        DeleteDeviceResponse(String): void
        = 단말 제거 요청 응답 처리 함수
    */

    private void DeleteDeviceResponse(String response){
        try{
            Log.d("onResponse 호출 ", response);

            JSONObject json = new JSONObject(response);
            String resultString = (String) json.get("message");

            switch (resultString) {
                case "success":
                    ShowToast("Push 알림 OFF");

                    Intent intent = new Intent();
                    intent.putExtra("DeviceCheckResult", "NO");
                    setResult(RESULT_OK, intent);

                    finish();
                    break;

                case "fail":
                    ShowToast("Push 알림 OFF 실패");
                    pushSwitch.setChecked(true);
                    break;

                case "error":
                    ShowToast("단말 추가  중 오류 발생");
                    break;

                case "db_fail":
                    ShowToast("연결 오류");
                    break;

            }

        } catch (Exception e){
            e.printStackTrace();
        }
    }


    private void ShowToast(String s){
        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //바깥레이어 클릭시 안닫히게
        if(event.getAction() == MotionEvent.ACTION_OUTSIDE){
            return false;
        }

        return true;
    }


}
