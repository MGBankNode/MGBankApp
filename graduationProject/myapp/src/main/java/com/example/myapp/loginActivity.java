package com.example.myapp;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class loginActivity extends AppCompatActivity {

    EditText loginIDEditText;
    EditText loginPWEditText;

    Button loginCancelBtn;
    Button moveToSignUpBtn;
    Button logInConfirmBtn;
    Button loginCancleBtn;
    Button loginConfirmBtn;

    EditText etLogin;
    String loginID;
    String loginPW;

    private Toast toast;
    private long backKeyPressedTime = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        Intent intent = new Intent(loginActivity.this, MainScreen.class);
        startActivity(intent);

        etLogin = findViewById(R.id.etLogin);
        etLogin.requestFocus();

        SharedPreferences auto = getSharedPreferences("auto", Activity.MODE_PRIVATE);
        loginID = auto.getString("inputId", null);
        loginPW = auto.getString("inputPw", null);

        if(loginID!=null && loginPW!=null) {
            LoginHandler(new VolleyCallback() {
                @Override
                public void onSuccess(UserInfo userInfo) {
                    //Go to MainActivity
                    finish();
                    Intent mIntent = new Intent(loginActivity.this, MainActivity.class);
                    mIntent.putExtra("UserInfoObject", userInfo);
                    startActivity(mIntent);
                }
            });
        }
        logInConfirmBtn = findViewById(R.id.logInConfirmBtn);
        logInConfirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(loginID==null && loginPW ==null) {
                    loginIDEditText = findViewById(R.id.etLogin);
                    loginPWEditText = findViewById(R.id.etLoginPassword);
                    loginID = loginIDEditText.getText().toString();
                    loginPW = loginPWEditText.getText().toString();
                }
                LoginHandler(new VolleyCallback() {
                    @Override
                    public void onSuccess(UserInfo userInfo) {
                        //Go to MainActivity
                        CheckBox checkBox = (CheckBox)findViewById(R.id.autoLoginCheck);
                        if(checkBox.isChecked()) {
                            SharedPreferences auto = getSharedPreferences("auto", Activity.MODE_PRIVATE);
                            SharedPreferences.Editor autoLogin = auto.edit();
                            autoLogin.putString("inputId", loginID);
                            autoLogin.putString("inputPw", loginPW);
                            autoLogin.commit();
                        }
                        finish();
                        Intent mIntent = new Intent(loginActivity.this, MainActivity.class);
                        mIntent.putExtra("UserInfoObject", userInfo);
                        startActivity(mIntent);
                    }
                });
            }
        });

        loginCancelBtn = findViewById(R.id.logInCancleBtn);
        loginCancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exitApp();
            }
        });

        moveToSignUpBtn = findViewById(R.id.moveToSignupBtn);
        moveToSignUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentSignup = new Intent(loginActivity.this, SignUpActivity.class);
                startActivity(intentSignup);
            }
        });
    }

    public void showToast() {
        toast = Toast.makeText(this, "\'뒤로\' 버튼을 한번 더 누르시면 종료됩니다." , Toast.LENGTH_SHORT); toast.show();
    }

    @Override
    public void onBackPressed() {

        if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
            backKeyPressedTime = System.currentTimeMillis();
            showToast();
            return;
        }
        if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
            this.finish();
            toast.cancel();
        }
    }

    public void exitApp() {
        ActivityCompat.finishAffinity(this);
    }

    /*
        LoginHandler: void
        = 로그인 버튼 요청 처리 핸들러
        (Button : logInConfirmBtn)
    */
    public interface VolleyCallback{
        void onSuccess(UserInfo userInfo);
    }

    public void LoginHandler(final VolleyCallback callback){
        if(loginID.equals("")){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            AlertDialog dialog = builder.setMessage("아이디를 입력하세요.").setPositiveButton("OK", null).create();
            dialog.show();
            return;
        }

        if(loginPW.equals("")){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            AlertDialog dialog = builder.setMessage("비밀번호를 입력하세요.").setPositiveButton("OK", null).create();
            dialog.show();
            return;
        }

        RequestInfo requestInfo = new RequestInfo(RequestInfo.RequestType.LOGIN_USER);

        String url = "http://" + requestInfo.GetRequestIP() + ":" + requestInfo.GetRequestPORT() + requestInfo.GetProcessURL();

        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response){
                        LoginResponse(response, callback);
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
                return LoginRequest();
            }
        };
        request.setShouldCache(false);
        Volley.newRequestQueue(this).add(request);
        Log.d("요청 url: ", url);

    }

    /*
        LoginRequest(): Map<String, String>
        = 로그인 요청 전달 파라미터 설정 함수
    */

    private Map<String, String> LoginRequest(){
        Map<String, String> params = new HashMap<>();
        UserInfo postInfo = new UserInfo(
                loginID, loginPW);

        params.put("id", postInfo.getUserID());
        params.put("password", postInfo.getUserPW());
        return params;
    }

    /*
        LoginResponse(String): void
        = 로그인 요청 응답 처리 함수
    */

    private void LoginResponse(String response, final VolleyCallback callback){
        try{
            Log.d("onResponse 호출 ", response);

            JSONObject json = new JSONObject(response);
            String resultString = (String) json.get("message");

            switch (resultString) {
                case "success":
                    ShowToast("로그인 성공");

                    JSONObject data = json.getJSONObject("data");


                    String userID = (String) data.get("id");
                    String userName = (String) data.get("name");
                    String userPhone = (String) data.get("phone");
                    int userAccountCheck =  Integer.parseInt((String)(data.get("accountCheck")));
                    String userUpdateAt = (String) data.get("update_at");

                    UserInfo userInfo = null;
                    if(userAccountCheck == 1){
                            String userABalance = (String) data.get("aBalance");
                            userInfo = new UserInfo(userID, userName, userPhone, userAccountCheck, userUpdateAt, userABalance);
                    }else if(userAccountCheck == 0){

                        userInfo = new UserInfo(userID, userName, userPhone, userAccountCheck, userUpdateAt);

                    }

                    callback.onSuccess(userInfo);
                    break;

                case "fail":
                    ShowToast("로그인 실패");
                    break;

                case "error":
                    ShowToast("로그인 중 오류 발생");
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

}
