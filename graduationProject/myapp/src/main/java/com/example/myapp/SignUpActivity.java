package com.example.myapp;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static android.graphics.Color.rgb;

public class SignUpActivity extends AppCompatActivity {

    EditText joinIDEditText;
    EditText joinPWEditText;
    EditText joinPhoneEditText;
    EditText joinNameEditText;
    Button idCheckBtn;
    Button joinBtn;

    boolean idCheckFlag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);

        Spinner spinner = findViewById(R.id.spinner);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.bankarray, R.layout.spinneritem);

        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    Toast.makeText(getApplicationContext(), parent.getItemAtPosition(position) + "을 선택", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        idCheckBtn = findViewById(R.id.certificationIdBtn);
        idCheckBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IDCheckHandler();
            }
        });

        joinBtn = findViewById(R.id.signupConfirmBtn);
        joinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JoinHandler();
            }
        });
    }

    /*
        IDCheckHandler(): void
        = 아이디 중복체크 이벤트 핸들러
        (Button : certificationIdBtn)
    */
    public void IDCheckHandler(){

        String btnString = idCheckBtn.getText().toString();
        if(btnString.equals("중복체크")){

            joinIDEditText = findViewById(R.id.etID);

            String joinID = joinIDEditText.getText().toString();

            if(joinID.equals("")){
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                AlertDialog dialog = builder.setMessage("아이디를 입력하세요.").setPositiveButton("OK", null).create();
                dialog.show();
                return;
            }

            RequestInfo requestInfo = new RequestInfo(RequestInfo.RequestType.ID_CHECK);

            String url = "http://" + requestInfo.GetRequestIP() + ":" + requestInfo.GetRequestPORT() + requestInfo.GetProcessURL();

            StringRequest request = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>(){
                        @Override
                        public void onResponse(String response){
                            IDCheckResponse(response);
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
                    return IDCheckRequest();
                }
            };
            request.setShouldCache(false);
            Volley.newRequestQueue(this).add(request);
            Log.d("요청 url: ", url);

        }else if(btnString.equals("수정")){

            joinIDEditText = findViewById(R.id.etID);
            joinIDEditText.setFocusable(true);
            joinIDEditText.setEnabled(true);
            joinIDEditText.setCursorVisible(true);
            joinIDEditText.setTextColor(Color.BLACK);
            joinIDEditText.setFocusableInTouchMode(true);

            idCheckBtn = findViewById(R.id.certificationIdBtn);
            idCheckBtn.setText("중복체크");
            joinIDEditText.setSelectAllOnFocus(true);
            joinIDEditText.requestFocus();

            idCheckFlag = false;
        }
    }

    /*
        JoinHandler(): void
        = 회원가입 이벤트 핸들러
        (Button : signupConfirmBtn)
    */

    public void JoinHandler(){

        joinIDEditText = findViewById(R.id.etID);
        joinPWEditText = findViewById(R.id.etPassword);
        joinPhoneEditText = findViewById(R.id.etPhoneNum);
        joinNameEditText = findViewById(R.id.etName);

        String joinID = joinIDEditText.getText().toString();
        String joinPW = joinPWEditText.getText().toString();
        String joinPhone = joinPhoneEditText.getText().toString();
        String joinName = joinNameEditText.getText().toString();

        if(joinID.equals("")){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            AlertDialog dialog = builder.setMessage("아이디를 입력하세요.").setPositiveButton("OK", null).create();
            dialog.show();
            return;
        }

        if(!idCheckFlag){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            AlertDialog dialog = builder.setMessage("아이디 중복체크를 하십시오.").setPositiveButton("OK", null).create();
            dialog.show();
            return;
        }

        if(joinPW.equals("")){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            AlertDialog dialog = builder.setMessage("비밀번호를 입력하세요.").setPositiveButton("OK", null).create();
            dialog.show();
            return;
        }

        if(joinPhone.equals("")){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            AlertDialog dialog = builder.setMessage("전화번호를 입력하세요.").setPositiveButton("OK", null).create();
            dialog.show();
            return;
        }

        if(joinName.equals("")){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            AlertDialog dialog = builder.setMessage("이름은 입력하세요.").setPositiveButton("OK", null).create();
            dialog.show();
            return;
        }

        RequestInfo requestInfo = new RequestInfo(RequestInfo.RequestType.JOIN_USER);

        String url = "http://" + requestInfo.GetRequestIP() + ":" + requestInfo.GetRequestPORT() + requestInfo.GetProcessURL();

        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response){
                        JoinResponse(response);
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
                return JoinRequest();
            }
        };
        request.setShouldCache(false);
        Volley.newRequestQueue(this).add(request);
        Log.d("요청 url: ", url);
    }

    /*
        IDCheckRequest(): Map<String, String>
        = 아이디 중복체크 요청 전달 파라미터 설정 함수
    */

    private Map<String, String> IDCheckRequest(){
        Map<String, String> params = new HashMap<>();

        UserInfo postInfo = new UserInfo(joinIDEditText.getText().toString());
        params.put("id", postInfo.getUserID());

        return params;
    }

    /*
        IDCheckResponse(String): void
        = 아이디 중복체크 요청 응답 처리 함수
    */

    private void IDCheckResponse(String response){
        try{
            Log.d("onResponse 호출 ", response);

            JSONObject json = new JSONObject(response);
            String resultString = (String) json.get("message");

            switch (resultString){
                case "success":
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("중복 체크");
                    builder.setMessage("사용 가능한 아이디 입니다.");
                    builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            joinIDEditText = findViewById(R.id.etID);
                            joinIDEditText.setFocusable(false);
                            joinIDEditText.setEnabled(false);
                            joinIDEditText.setCursorVisible(false);
                            joinIDEditText.setTextColor(rgb(155, 155, 155));

                            idCheckBtn = findViewById(R.id.certificationIdBtn);
                            idCheckBtn.setText("수정");
                            idCheckFlag = true;

                        }
                    });
                    builder.show();
                    break;

                case "fail":
                    ShowToast("사용 중인 아이디입니다.");
                    break;

                case "error":
                    ShowToast("ID 확인 중 오류 발생");
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
        JoinRequest(): Map<String, String>
        = 회원가입 요청 전달 파라미터 설정 함수
    */

    public Map<String, String> JoinRequest(){
        Map<String, String> params = new HashMap<>();

        UserInfo postInfo = new UserInfo(
                joinIDEditText.getText().toString(),
                joinNameEditText.getText().toString(),
                joinPWEditText.getText().toString(),
                joinPhoneEditText.getText().toString());

        params.put("id", postInfo.getUserID());
        params.put("password", postInfo.getUserPW());
        params.put("name", postInfo.getUserName());
        params.put("phone", postInfo.getUserPhone());

        return params;
    }

    /*
        JoinResponse(String): void
        = 회원가입 중복체크 요청 응답 처리 함수
    */

    public void JoinResponse(String response){
        try{
            Log.d("onResponse 호출 ", response);

            JSONObject json = new JSONObject(response);
            String resultString = (String) json.get("message");

            switch (resultString){
                case "success":
                    ShowToast("회원가입 성공");
                    finish();
                    break;

                case "fail":
                    ShowToast("회원가입 실패");
                    break;

                case "error":
                    ShowToast("회원가입 중 오류 발생");
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
