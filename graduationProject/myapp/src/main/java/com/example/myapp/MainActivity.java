package com.example.myapp;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.telephony.TelephonyManager;
import android.text.Html;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;

import android.widget.ListView;
import android.widget.TextView;

import android.app.AlertDialog;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    protected Fragment fr ;
    protected TextView welcomeTextView;
    protected TextView userLastAtTxt;
    protected ImageView closeMenu;
    protected ImageView homeMenu;
    protected ImageView userMenu;
    protected ImageView noticeMenu;

    private ListView menu1list;
    private ListView menu2list;
    private ListView menu3list;

    Toolbar toolbar;

    final FragmentManager fm = getSupportFragmentManager();
    private backPressCloseHandler backPressCloseHandler;

    private static final int MY_PERMISSIONS_REQUEST_READ_PHONE_STATE = 1;
    String phonePermission = Manifest.permission.READ_PHONE_STATE;

    public String mobile = "";

    public UserInfo myUserInfo;
    public DeviceInfo myDeviceInfo;
    public String deviceCheckResult = "";

    Stack<String> st;

    public int userAccountCheck;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setElevation(0);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        backPressCloseHandler = new backPressCloseHandler(this);

        startMainFragment();
        makeMenuList();
    }

    public void makeMenuList() {

        menu1list = findViewById(R.id.menu1_list);
        menu2list = findViewById(R.id.menu2_list);
        menu3list = findViewById(R.id.menu3_list);

        ArrayList<String> menu1items = new ArrayList<>();
        ArrayList<String> menu2items = new ArrayList<>();
        ArrayList<String> menu3items = new ArrayList<>();

        menu1items.add("달력");
        menu1items.add("내역");
        menu2items.add("소비평가");
        menu2items.add("카드추천");
        menu3items.add("통합맴버쉽");

        MainMenuListviewAdapter menu1ListviewAdapter = new MainMenuListviewAdapter(this, menu1items ,R.layout.mainmenuitem);
        MainMenuListviewAdapter menu2ListviewAdapter = new MainMenuListviewAdapter(this, menu2items ,R.layout.mainmenuitem);
        MainMenuListviewAdapter menu3ListviewAdapter = new MainMenuListviewAdapter(this, menu3items ,R.layout.mainmenuitem);
        menu1list.setAdapter(menu1ListviewAdapter);
        menu2list.setAdapter(menu2ListviewAdapter);
        menu3list.setAdapter(menu3ListviewAdapter);

        st = new Stack<String>();
        st.push("home");
        menu1list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {

                    case 0:
                        fr = new fragment_menu1();
                        fr.setArguments(makeBundle("apage", 0));
                        st.push("a");
                        break;

                    case 1:
                        fr = new fragment_menu1();
                        fr.setArguments(makeBundle("apage", 1));
                        st.push("b");
                        break;

                }
                changeFragment(fr);
            }
        });
        menu2list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        fr = new consumptionEvaluation_viewPager();
                        fr.setArguments(makeBundle("cpage", 0));
                        st.push("c");
                        break;

                    case 1:
                        fr = new consumptionEvaluation_viewPager();
                        fr.setArguments(makeBundle("cpage", 1));
                        st.push("d");
                        break;
                }
                changeFragment(fr);
            }
        });

        menu3list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        fr = new fragment_menu3();
                        st.push("e");
                        break;
                }
                changeFragment(fr);
            }
        });
    }


    public Bundle makeBundle(String str, int num) {
        Bundle bundle = new Bundle(1);
        bundle.putInt(str, num);
        return bundle;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer =  findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
//            super.onBackPressed();
            if(st.peek() == "home")
                backPressCloseHandler.onBackPressed();
            else{
                st.pop();
                switch (st.peek()) {
                    case "a":
                        fr = new fragment_menu1();
                        fr.setArguments(makeBundle("apage", 0));
                        break;

                    case "b":
                        fr = new fragment_menu1();
                        fr.setArguments(makeBundle("apage", 1));
                        break;
                    case "c":
                        fr = new consumptionEvaluation_viewPager();
                        fr.setArguments(makeBundle("cpage", 0));
                        break;

                    case "d":
                        fr = new consumptionEvaluation_viewPager();
                        fr.setArguments(makeBundle("cpage", 1));
                        break;
                    case "e":
                        fr = new fragment_menu3();
                        break;
                    case "home":
                        fr = new fragment_home();
                        break;
                    default:
                        break;
                }
                changeFragment(fr);
            }

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        welcomeTextView = findViewById(R.id.welcomeTv);
        userLastAtTxt = findViewById(R.id.userLastAtTxt);

        // Device 정보 불러오기 + 권한 설정
        myDeviceInfo = getDeviceInfo();

        //로그인 액티비티에서, User 정보 전달 받기
        Intent intent = getIntent();
        myUserInfo = (UserInfo) intent.getSerializableExtra("UserInfoObject");

        //사용자 이름 변경
        String tempLoginUser = myUserInfo.getUserName();
        if(tempLoginUser != null) {

            welcomeTextView.setText(Html.fromHtml("<u>" + tempLoginUser + "</u>")); // 밑줄 긋기

        }
        getMenuInflater().inflate(R.menu.main, menu);

        welcomeTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                StartActivity(MypageActivity.class);

            }
        });

        //사용자 마지막 접속시간 변경
        String changeText = userLastAtTxt.getText().toString() + myUserInfo.getUserUpateAt();
        userLastAtTxt.setText(changeText);


        userAccountCheck = myUserInfo.getUserAccountCheck();
        //사용자 잔액 -> 계좌 등록이 있는 경우에만 메인 화면 변경
        if(userAccountCheck == 1){

            TextView userABalanceTxtView = findViewById(R.id.mainFragment_textView);

            String userABalance = myUserInfo.getUserABalance() + "원";
            userABalanceTxtView.setText(userABalance);

        }else if(userAccountCheck == 0){
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("");
            builder.setMessage("앱을 사용하려면 계좌등록을 하셔야 합니다. 계좌 등록을 하시겠습니까?");
            builder.setCancelable(false);
            builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if(deviceCheckResult.equals("")){

                        DeviceCheckHandler();

                    }else{

                        StartActivity(SettingDialogActivity.class);

                    }
                }
            });
            builder.setNegativeButton("아니오(로그아웃)", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                    Intent returnLogin = new Intent(MainActivity.this, loginActivity.class);
                    startActivity(returnLogin);
                }
            });
            builder.show();
        }


        closeMenu = findViewById(R.id.closeMenu);
        closeMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("");
                builder.setMessage("정말로 로그아웃 하시겠습니까? ");
                builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                        Intent returnLogin = new Intent(MainActivity.this, loginActivity.class);
                        startActivity(returnLogin);
                    }
                });
                builder.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.show();
            }
        });

        homeMenu = findViewById(R.id.homeMenu);
        homeMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fr = new fragment_home();
                changeFragment(fr);
            }
        });

        userMenu = findViewById(R.id.userMenu);
        userMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                StartActivity(MypageActivity.class);

            }
        });

        return true;
    }




    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {

            if(deviceCheckResult.equals("")){

                DeviceCheckHandler();

            }else{

                StartActivity(SettingDialogActivity.class);

            }

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 1){
            if(resultCode == RESULT_OK){
                deviceCheckResult = data.getStringExtra("DeviceCheckResult");
                userAccountCheck = Integer.parseInt(data.getStringExtra("UserAccountCheck"));
            }
            else if(resultCode == RESULT_CANCELED){
                finish();
                Intent returnLogin = new Intent(MainActivity.this, loginActivity.class);
                startActivity(returnLogin);
            }
        }
    }

    protected void StartActivity(Class startClass){
        Intent intent = new Intent(MainActivity.this, startClass);
        intent.putExtra("DeviceInfoObject", myDeviceInfo);
        intent.putExtra("DeviceCheckResult", deviceCheckResult);
        intent.putExtra("UserAccountCheck", String.valueOf(userAccountCheck));
        intent.putExtra("UserID", myUserInfo.getUserID());
        startActivityForResult(intent, 1);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        String url = "";

        if (id == R.id.menu1) {

            url = "https://www.kfcc.co.kr/index.do";

        } else if (id == R.id.menu2) {

            url = "https://www.slideshare.net/SunghyunHwang2/ss-52119539";

        } else if (id == R.id.menu3) {

            fr = new fragment_home();

        }

        changeFragment(fr);

        if(!url.equals("")) {
            Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(myIntent);
        }


        return true;
    }


    public void onDetailBtnClicked(View view) {
        Fragment detailFragment = new consumptionReportFragment();
//        Bundle bundle = new Bundle(1);
//        bundle.putString("userId", "AAA");
//
//        detailFragment.setArguments(bundle);


       //Toast.makeText(getApplication(), "버튼 선택됨", Toast.LENGTH_SHORT).show();

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentContainer_viewpager, detailFragment);
        fragmentTransaction.commit();
    }

    public void changeFragment(Fragment fr) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.dynamic_mainFragment, fr);
        fragmentTransaction.commit();

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }

    /*
        DeviceCheckHandler
        = 단말 정보 확인 처리 핸들러
     */
    public void DeviceCheckHandler(){

        RequestInfo requestInfo = new RequestInfo(RequestInfo.RequestType.DEVICE_CHECK);
        String url = "http://" + requestInfo.GetRequestIP() + ":" + requestInfo.GetRequestPORT() + requestInfo.GetProcessURL();
        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response){
                        DeviceCheckResponse(response);
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
                return DeviceCheckRequest();
            }
        };
        request.setShouldCache(false);
        Volley.newRequestQueue(getApplicationContext()).add(request);
        Log.d("요청 url: ", url);
    }

    /*
        DeviceCheckRequest(): Map<String, String>
        = 단말 정보 확인 요청 전달 파라미터 설정 함수
    */

    private Map<String, String> DeviceCheckRequest(){
        Map<String, String> params = new HashMap<>();

        params.put("mobile", myDeviceInfo.getMobile());

        return params;
    }

    /*
        DeviceCheckResponse(String): void
        = 단말 정보 확인 요청 응답 처리 함수
    */

    private void DeviceCheckResponse(String response){
        try{
            Log.d("onResponse 호출 ", response);

            JSONObject json = new JSONObject(response);
            String resultString = (String) json.get("message");

            switch (resultString) {
                case "YES":
                    deviceCheckResult = "YES";
                    StartActivity(SettingDialogActivity.class);
                    break;

                case "NO":
                    deviceCheckResult = "NO";
                    StartActivity(SettingDialogActivity.class);
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

    /*
        getDeviceInfo : DeviceInfo
        = 디바이스 정보 얻는 함수
     */

    public DeviceInfo getDeviceInfo(){
        DeviceInfo myDevice;

        if (!CheckPermission(phonePermission)) {

            RequestPermission(phonePermission);

        } else {

            mobile = getPhoneNumber();

        }

        String osVersion = Build.VERSION.RELEASE;
        String model = Build.MODEL;
        String display = getDisplay(this);
        String manufacturer = Build.MANUFACTURER;
        String macAddress = getMacAddress(this);

        myDevice = new DeviceInfo(mobile, osVersion, model, display, manufacturer, macAddress);

        return myDevice;
    }

    /*
        getPhoneNumber : String
        = 디바이스 전화번호 정보 얻는 함수
     */

    public String getPhoneNumber() {

        TelephonyManager phoneMgr = (TelephonyManager) getSystemService (Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission (this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            return "";
        }
        return phoneMgr.getLine1Number();
    }

    /*
        CheckPermission(String): boolean
        = 권한 확인 함수
     */

    private boolean CheckPermission(String permission){
        if (Build.VERSION.SDK_INT >= 23) {
            int result = ContextCompat.checkSelfPermission(this, permission);
            if (result == PackageManager.PERMISSION_GRANTED){

                return true;

            } else {

                return false;

            }
        } else {

            return true;

        }
    }

    /*
        RequestPermission(String): void
        = 권한 허가 요청 함수
     */

    private void RequestPermission(String permission){
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)){
            Toast.makeText(this, "단말 정보를 위해 휴대전화 상태 권한을 허가해야 합니다. 추가적인 기능을 위해 허가해 주시기 바랍니다.", Toast.LENGTH_LONG).show();
        }
        ActivityCompat.requestPermissions(this, new String[]{permission},MY_PERMISSIONS_REQUEST_READ_PHONE_STATE);
    }

    /*
        onRequestPermissionsResult : void
        = 권한 요청 결과 처리
     */

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_PHONE_STATE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    mobile = getPhoneNumber();

                } else {

                    finish();

                }
            }
            break;
        }
    }

    /*
        getDisplay : String
        = 디바이스 화면 정보 얻는 함수
    */

    private static String getDisplay(Context context){

        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);

        int deviceWidth = displayMetrics.widthPixels;
        int deviceHeight = displayMetrics.heightPixels;

        return deviceWidth + "x" + deviceHeight;
    }

    /*
        getMacAddress : String
        = 디바이스 MacAddress 정보 얻는 함수
    */

    private static String getMacAddress(Context context){
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifiManager.getConnectionInfo();
        return info.getMacAddress();
    }
    public void startMainFragment(){

        fr = new fragment_home();

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.dynamic_mainFragment, fr);
        fragmentTransaction.commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

    }

}
