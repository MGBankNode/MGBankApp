package com.example.myapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
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
    private Switch pushSwitch;
    private Switch accountSwitch;

    private int originDeviceCheckValue = 0;
    private int originAccountCheckValue = 0;

    private DeviceInfo myDeviceInfo;
    String deviceCheckResult;
    private int userAccountCheck;
    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_dialog);

        switchFinishBtn = findViewById(R.id.switchFinishBtn);
        pushSwitch = findViewById(R.id.pushSwitch);
        accountSwitch = findViewById(R.id.accountSwitch);

        Intent intent = getIntent();
        myDeviceInfo = (DeviceInfo) intent.getSerializableExtra("DeviceInfoObject");
        deviceCheckResult = intent.getStringExtra("DeviceCheckResult");
        userAccountCheck = Integer.parseInt(intent.getStringExtra("UserAccountCheck"));
        userID = intent.getStringExtra("UserID");


        if (deviceCheckResult.equals("YES")) {

            pushSwitch.setChecked(true);
            originDeviceCheckValue = 1;

        } else if (deviceCheckResult.equals("NO")) {

            pushSwitch.setChecked(false);
            originDeviceCheckValue = 0;

        }

        if (userAccountCheck == 1) {

            accountSwitch.setChecked(true);
            originAccountCheckValue = 1;

        } else if (userAccountCheck == 0) {

            accountSwitch.setChecked(false);
            originAccountCheckValue = 0;

        }

        //완료 버튼을 눌렀을 때
        switchFinishBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //pushSwitch 검사
                if (pushSwitch.isChecked()) {

                    if (originDeviceCheckValue == 1) {  //기존과 같은 경우 accountSwitch 검사 후
                                                        // 아무작업도 안함(0)
                        deviceCheckResult = "YES";
                        CheckOriginAccountCheckValue(0);

                    } else {                            //기존과 다른 경우 accountSwitch 검사 후
                                                        // ADD 작업 해야함(1)
                        CheckOriginAccountCheckValue(1);

                    }


                } else {

                    if (originDeviceCheckValue == 1) {  //기존과 다른 경우 accountSwitch 검사 후
                                                        // Delete 작업 해야함(2)
                        CheckOriginAccountCheckValue(2);

                    } else {                            //기존과 같은 경우 accountSwitch 검사 후
                                                        //아무작없도 안함(0)
                        deviceCheckResult = "NO";
                        CheckOriginAccountCheckValue(0);

                    }

                }
            }
        });

    }

    private void FinishActivity(){
        Intent intent = new Intent();
        intent.putExtra("DeviceCheckResult", deviceCheckResult);
        intent.putExtra("UserAccountCheck", String.valueOf(userAccountCheck));
        setResult(RESULT_OK, intent);
        finish();
    }

    private void execFunction(int sameCheck){
        if(sameCheck == 1){         //기존이랑 같으면 단순히 액티비티 종료

            FinishActivity();

        }else{                      //기존이랑 다르면 계좌 등록하고 종료

            AddAccountHandler();

        }
    }
    private void CheckOriginAccountCheckValue(int HandlerCheck){

        final int sameCheck;
        if(accountSwitch.isChecked()){
            if(originAccountCheckValue == 1){       //기존 값도 계좌 등록이 되어있는 경우

                userAccountCheck = 1;
                sameCheck = 1;

            }else{

                sameCheck = 0;

            }


            switch(HandlerCheck){
                case 0:                                         //아무작업도 안함
                    execFunction(sameCheck);
                    break;

                case 1:
                    AddDeviceHandler(new VolleyCallback(){      //단말 추가 후 작업
                        public void onSuccess() {

                            execFunction(sameCheck);

                        }
                    });
                    break;

                case 2:
                    DeleteDeviceHandler(new VolleyCallback(){      //단말 삭제 후 작업
                        public void onSuccess() {

                            execFunction(sameCheck);

                        }
                    });
                    break;
            }

        } else {
            //계좌해지 -> 로그아웃
            AlertDialog.Builder builder = new AlertDialog.Builder(SettingDialogActivity.this);
            builder.setTitle("");
            builder.setMessage("앱을 사용하려면 계좌등록을 하셔야 합니다. 계좌 등록을 하지 않고 앱을 종료하시겠습니까?");
            builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    accountSwitch.setChecked(true);
                }
            });
            builder.setPositiveButton("예(로그아웃)", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    finish();
                    Intent intent = new Intent();
                    intent.putExtra("DeviceCheckResult", deviceCheckResult);
                    intent.putExtra("UserAccountCheck", String.valueOf(userAccountCheck));
                    setResult(RESULT_CANCELED, intent);
                    finish();

                }
            });
            builder.setNegativeButton("아니오(계좌등록)", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    accountSwitch.setChecked(true);
                }
            });
            builder.show();
        }
    }
    /*
        AddDeviceHandler: void
        = 단말 추가 요청 처리 핸들러
    */

    public interface VolleyCallback{
        void onSuccess();
    }

    public void AddDeviceHandler(final VolleyCallback callback){
        RequestInfo requestInfo = new RequestInfo(RequestInfo.RequestType.ADD_DEVICE);

        String url = "http://" + requestInfo.GetRequestIP() + ":" + requestInfo.GetRequestPORT() + requestInfo.GetProcessURL();

        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response){
                        AddDeviceResponse(response, callback);
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

    private void AddDeviceResponse(String response, VolleyCallback callback){
        try{
            Log.d("onResponse 호출 ", response);

            JSONObject json = new JSONObject(response);
            String resultString = (String) json.get("message");

            switch (resultString) {
                case "success":
                    ShowToast("Push 알림 ON");
                    deviceCheckResult = "YES";
                    callback.onSuccess();
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

    public void DeleteDeviceHandler(final VolleyCallback callback){
        RequestInfo requestInfo = new RequestInfo(RequestInfo.RequestType.DELETE_DEVICE);

        String url = "http://" + requestInfo.GetRequestIP() + ":" + requestInfo.GetRequestPORT() + requestInfo.GetProcessURL();

        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response){
                        DeleteDeviceResponse(response, callback);
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

    private void DeleteDeviceResponse(String response, VolleyCallback callback){
        try{
            Log.d("onResponse 호출 ", response);

            JSONObject json = new JSONObject(response);
            String resultString = (String) json.get("message");

            switch (resultString) {
                case "success":
                    ShowToast("Push 알림 OFF");
                    deviceCheckResult = "NO";
                    callback.onSuccess();
                    break;

                case "fail":
                    ShowToast("Push 알림 OFF 실패");
                    pushSwitch.setChecked(true);
                    break;

                case "error":
                    ShowToast("단말 추가 중 오류 발생");
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
        AddAccountHandler: void
        = 계좌 등록 요청 처리 핸들러
    */

    public void AddAccountHandler(){
        RequestInfo requestInfo = new RequestInfo(RequestInfo.RequestType.ADD_ACCOUNT);

        String url = "http://" + requestInfo.GetRequestIP() + ":" + requestInfo.GetRequestPORT() + requestInfo.GetProcessURL();

        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response){
                        AddAccountResponse(response);
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
                return AddAccountRequest();
            }
        };
        request.setShouldCache(false);
        Volley.newRequestQueue(getApplicationContext()).add(request);
        Log.d("요청 url: ", url);
    }

    /*
        AddAccountRequest(): Map<String, String>
        = 계좌 등록 요청 전달 파라미터 설정 함수
    */

    private Map<String, String> AddAccountRequest(){
        Map<String, String> params = new HashMap<>();

        params.put("id", userID);

        return params;
    }

    /*
        AddAccountResponse(String): void
        = 계좌 등록 요청 응답 처리 함수
    */

    private void AddAccountResponse(String response){
        try{
            Log.d("onResponse 호출 ", response);

            JSONObject json = new JSONObject(response);
            String resultString = (String) json.get("message");

            switch (resultString) {
                case "success":
                    ShowToast("계좌 등록 ON");

                    userAccountCheck = 1;
                    Intent intent = new Intent();
                    intent.putExtra("DeviceCheckResult", deviceCheckResult);
                    intent.putExtra("UserAccountCheck", String.valueOf(userAccountCheck));
                    setResult(RESULT_OK, intent);
                    finish();
                    break;

                case "history_insert_fail":
                    ShowToast("계좌 내역 추가 실패");
                    accountSwitch.setChecked(true);
                    break;

                case "caweigth_update_fail":
                    ShowToast("가중치 업데이트 실패");
                    accountSwitch.setChecked(true);
                    break;

                case "category_insert_fail":
                    ShowToast("카테고리 내역 추가 실패");
                    accountSwitch.setChecked(true);
                    break;

                case "account_update_fail":
                    ShowToast("계좌등록 업데이트 실패");
                    accountSwitch.setChecked(true);
                    break;

                case "id_check_fail":
                    ShowToast("아이디 미 존재");
                    accountSwitch.setChecked(true);
                    break;

                case "error":
                    ShowToast("계좌 등록 중 오류 발생");
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

    @Override public void onBackPressed() {
        //super.onBackPressed();
    }

}
