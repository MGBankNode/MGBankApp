package com.example.myapp;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import android.view.Menu;
import android.view.MenuItem;
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



public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    Fragment fr ;

    EditText idEditText;
    EditText pwEditText;
    EditText phoneEditText;
    EditText nameEditText;
    private AlertDialog dialog;

    String myIP = "10.20.12.63";
    String myPort = "3000";


    private backPressCloseHandler backPressCloseHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setElevation(0);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        backPressCloseHandler = new backPressCloseHandler(this);



    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
//            super.onBackPressed();
             backPressCloseHandler.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Intent intent = null;
        String url = "";

        if (id == R.id.menu1) {

//            fr = new fragment_menu1();
            url = "https://www.kfcc.co.kr/index.do";



        } else if (id == R.id.menu2) {

            url = "https://www.slideshare.net/SunghyunHwang2/ss-52119539";
//            fr = new consumptionEvaluation_viewPager();


        } else if (id == R.id.menu3) {

            fr = new fragment_home();

        }

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.dynamic_mainFragment, fr);
        fragmentTransaction.commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        if(!url.equals("")) {
            Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(myIntent);
        }


        return true;
    }
    public void onMenuBtnClicked(View view) {

        switch (view.getId()) {
            case (R.id.menuBtn1) :
                fr = new fragment_menu1();
                break;

            case (R.id.menuBtn2) :
                fr = new consumptionEvaluation_viewPager();
                break;

            case (R.id.menuBtn3) :
                fr = new fragment_menu3();
                break;


            case (R.id.signup) :
                fr = new signUpFragment();

                break;

            case (R.id.logIn) :
                fr = new loginFragment();
                break;

            default:
                break;
        }

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.dynamic_mainFragment, fr);
        fragmentTransaction.commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }

    public void onDetailBtnClicked(View view) {
        Fragment detailFragment = new consumptionReportFragment();
//        Bundle bundle = new Bundle(1);
//        bundle.putString("userId", "AAA");
//
//        detailFragment.setArguments(bundle);


       // Toast.makeText(getApplication(), "버튼 선택됨", Toast.LENGTH_SHORT).show();

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentContainer_viewpager, detailFragment);
        fragmentTransaction.commit();

    }

    /*
        요청 타입 정의
    */

    public enum RequestType{
        IDCHECK, JOINUSER
    }

    /*
        ShowAlertMyDialog(String): void
        = 다이얼로그 생성 + show
     */

    public void ShowAlertMyDialog(String s){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        dialog = builder.setMessage(s)
                .setPositiveButton("OK", null)
                .create();
        dialog.show();
    }

    /*
    MyStringPostRequest(String, final RequestType): void
    = Post 방식으로 RequestType 에 따라 StringRequest 요청
    */
    public void MyStringPostRequest(String processURL, final RequestType checkNum){

        String url = "http://" + myIP + ":" + myPort + processURL;

        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response){
                        switch(checkNum){
                            case IDCHECK:
                                IDCheckResponse(response);
                                break;
                            case JOINUSER:
                                JoinResponse(response);
                                break;
                        }
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
                Map<String, String> params = new HashMap<>();

                switch(checkNum){
                    case IDCHECK:
                        params = IDCheckRequest(params);
                        break;
                    case JOINUSER:
                        params = JoinRequest(params);
                }
                return params;
            }
        };
        request.setShouldCache(false);
        Volley.newRequestQueue(this).add(request);
        Log.d("요청 url: ", url);

    }

    /*
        onCheckBtnClicked(View): void
        = 아이디 중복체크 이벤트 핸들러
        (Button : certificationIdBtn)
    */
    public void onCheckBtnClicked(View v){

        String processURL = "/process/idcheck";

        idEditText = findViewById(R.id.etID);

        String joinID = idEditText.getText().toString();
        if(joinID.equals("")){
            ShowAlertMyDialog("ID is empty");
            return;
        }

        MyStringPostRequest(processURL, RequestType.IDCHECK);
    }

    /*
        IDCheckRequest(Map<String, String>): Map<String, String>
        = 아이디 중복체크 요청 전달 파라미터 설정 함수
    */

    public Map<String, String> IDCheckRequest(Map<String, String> params){

        JoinInfo postInfo = new JoinInfo(idEditText.getText().toString(), null, null, null);
        params.put("id", postInfo.getJoinID());

        return params;
    }

    /*
        IDCheckResponse(String): void
        = 아이디 중복체크 요청 응답 처리 함수
    */

    public void IDCheckResponse(String response){
        try{
            Log.d("onResponse 호출 ", response);

            JSONObject json = new JSONObject(response);
            String resultString = (String) json.get("message");

            if(resultString.equals("success")){

                ShowAlertMyDialog("사용 가능한 아이디");

                /*
                    확인 완료 후 textBox  비활성
                 */

            }else if(resultString.equals("fail")){

                ShowAlertMyDialog("사용 중인 아이디입니다.");

            }else if(resultString.equals("error")){

                ShowAlertMyDialog("ID 확인 중 오류 발생");

            }else if(resultString.equals("db_fail")){

                ShowAlertMyDialog("연결 오류");
            }

        } catch (Exception e){
            e.printStackTrace();
        }
    }

    /*
        onJoinBtnClicked(View): void
        = 회원가입 이벤트 핸들러
        (Button : signupConfirmBtn)
    */

    public void onJoinBtnClicked(View v){

        String processURL = "/process/joinuser";

        idEditText = findViewById(R.id.etID);
        pwEditText = findViewById(R.id.etPassword);
        phoneEditText = findViewById(R.id.etPhoneNum);
        nameEditText = findViewById(R.id.etName);

        String joinID = idEditText.getText().toString();
        String joinPW = pwEditText.getText().toString();
        String joinPhone = phoneEditText.getText().toString();
        String joinName = nameEditText.getText().toString();

        if(joinID.equals("")){
            ShowAlertMyDialog("ID is empty");
            return;
        }

        if(joinPW.equals("")){
            ShowAlertMyDialog("PW is empty");
            return;
        }

        if(joinPhone.equals("")){
            ShowAlertMyDialog("Phone is empty");
            return;
        }

        if(joinName.equals("")){
            ShowAlertMyDialog("Name is empty");
            return;
        }

        MyStringPostRequest(processURL, RequestType.JOINUSER);
    }

    /*
        JoinRequest(Map<String, String>): Map<String, String>
        = 회원가입 요청 전달 파라미터 설정 함수
    */

    public Map<String, String> JoinRequest(Map<String, String> params){

        JoinInfo postInfo = new JoinInfo(
                idEditText.getText().toString(),
                nameEditText.getText().toString(),
                pwEditText.getText().toString(),
                phoneEditText.getText().toString());

        params.put("id", postInfo.getJoinID());
        params.put("password", postInfo.getJoinPW());
        params.put("name", postInfo.getJoinName());
        params.put("phone", postInfo.getJoinPhone());

        return params;
    }

    /*
        JoinResponse(String): void
        = 아이디 중복체크 요청 응답 처리 함수
    */

    public void JoinResponse(String response){
        try{
            Log.d("onResponse 호출 ", response);

            JSONObject json = new JSONObject(response);
            String resultString = (String) json.get("message");

            if(resultString.equals("success")){

                ShowAlertMyDialog("회원가입 성공");

                /*
                    회원가입 성공 이후 처리
                 */

            }else if(resultString.equals("fail")){

                ShowAlertMyDialog("회원가입 실패");

                /*
                    회원가입 실패 이후 처리
                 */

            }else if(resultString.equals("error")){

                ShowAlertMyDialog("ID 확인 중 오류 발생");

            }else if(resultString.equals("db_fail")){

                ShowAlertMyDialog("연결 오류");
            }

        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
