package com.example.myapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

    EditText etLogin;

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

        logInConfirmBtn = findViewById(R.id.logInConfirmBtn);
        logInConfirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginHandler();
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

    public void LoginHandler(){

        loginIDEditText = findViewById(R.id.etLogin);
        loginPWEditText = findViewById(R.id.etLoginPassword);

        String loginID = loginIDEditText.getText().toString();
        String loginPW = loginPWEditText.getText().toString();

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
                        LoginResponse(response);
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
                loginIDEditText.getText().toString(),
                loginPWEditText.getText().toString());

        params.put("id", postInfo.getUserID());
        params.put("password", postInfo.getUserPW());
        return params;
    }

    /*
        LoginResponse(String): void
        = 로그인 요청 응답 처리 함수
    */

    private void LoginResponse(String response){
        try{
            Log.d("onResponse 호출 ", response);

            JSONObject json = new JSONObject(response);
            String resultString = (String) json.get("message");

            switch (resultString) {
                case "success":
                    ShowToast("로그인 성공");

                    String userID = (String) json.get("id");
                    String userName = (String) json.get("name");
                    int userAccountCheck = (int) json.get("accountCheck");
                    String userUpdateAt = (String) json.get("update_at");

                    UserInfo userInfo = new UserInfo(userID, userName, userAccountCheck, userUpdateAt);

                    //Go to MainActivity
                    finish();
                    Intent mIntent = new Intent(loginActivity.this, MainActivity.class);
                    mIntent.putExtra("UserInfoObject", userInfo);
                    startActivity(mIntent);
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
