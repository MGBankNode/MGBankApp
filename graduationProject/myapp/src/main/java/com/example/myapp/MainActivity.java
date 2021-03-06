package com.example.myapp;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
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
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;

import android.widget.LinearLayout;
import android.widget.TextView;

import android.app.AlertDialog;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    protected Fragment fr;
    protected TextView welcomeTextView;
    protected TextView userLastAtTxt;
    protected TextView remainBudget;

    protected ImageView closeMenu;
    protected LinearLayout homeMenu;
    protected LinearLayout userMenu;
    protected LinearLayout settingMenu;
    protected LinearLayout navChild;
    protected TextView budgetBtn;
    protected Button setBudget;
    private ExpandableListView listView;
    private static final int MAINFRAGMENT = 1001;
    private static final int BESTCARDFRAGMENT = 1002;

    public String previous_date;
    public String cardPrevMonth;
    public String currentMonth;
    public String nextMonth;

    public String mainUserId;

    //데이터베이스로 부터 받은 정보를 저장함
    ArrayList<Stat> sData = null;

    Toolbar toolbar;
    TextView textTitle;
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
    public String userID;
    BroadcastReceiver receiver = null;
    BroadcastReceiver receiver2 = null;
    BroadcastReceiver receiver3 = null;


    Util util = new Util();
    @Override
    public void onBackPressed() {
        Log.i("nkw","onBackpressed()");
        textTitle.setText("");
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private static Date addMonth(Date date, int months) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, months);
        return calendar.getTime();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM");
        cardPrevMonth = dateFormat.format(addMonth(date, -5)) + "-01";
        currentMonth = dateFormat.format(date) + "-01";
        nextMonth = dateFormat.format(addMonth(date, +1)) +"-01";
        Log.i("CHJ", "6달 전 : " + cardPrevMonth);
        Log.i("CHJ", "현재 달 : " + currentMonth);
        Log.i("CHJ", "다음 달 : " + nextMonth);
        //홈프래그먼트 브로드캐스트
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("HomeFragment");
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                startFlagFragment(currentMonth, nextMonth, MAINFRAGMENT);
            }
        };
        registerReceiver(receiver, intentFilter);

        //예산설정 브로드캐스트
        IntentFilter intentFilter2 = new IntentFilter();
        intentFilter2.addAction("budget");
        receiver2 = new BroadcastReceiver() {
            Intent intent2 = new Intent();

            @Override
            public void onReceive(Context context, Intent intent) {
                //////////////////////////////////설정된 예산 요청///////////////////////

                BudgetRequest budgetRequest1 = new BudgetRequest(userID, RequestInfo.RequestType.DEFAULT_BUDGET, getApplicationContext());

                budgetRequest1.GetBudgetHandler(budget -> {
                    //Toast.makeText(getApplicationContext(), budget, Toast.LENGTH_LONG).show();

                    Log.d("KJH", "send budget : " + budget);
                    intent2.putExtra("BUDGET", budget);
                    intent2.setAction("sendbudget");
                    context.sendBroadcast(intent2);
                });
                //////////////////////////////////////////////////////////////////
            }
        };
        registerReceiver(receiver2, intentFilter2);

        //예산설정 브로드캐스트
        IntentFilter intentFilter3 = new IntentFilter();
        intentFilter3.addAction("GET_USERID");
        receiver3 = new BroadcastReceiver() {
            Intent intent3 = new Intent();

            @Override
            public void onReceive(Context context, Intent intent) {
                Log.d("KJH", "send userID : " + userID);
                intent3.putExtra("userID", userID);
                intent3.setAction("SEND_USERID");
                context.sendBroadcast(intent3);
            }
        };
        registerReceiver(receiver3, intentFilter3);
        toolbar = findViewById(R.id.toolbar);
        textTitle = (TextView)findViewById(R.id.text_title);
        textTitle.setText("");
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
        sData = new ArrayList<Stat>();

        remainBudget = findViewById(R.id.remainBudget);

        ArrayList<myGroup> DataList = new ArrayList<myGroup>();
        listView = (ExpandableListView)findViewById(R.id.mylist);
        myGroup temp = new myGroup("가계부");
        temp.child.add("계좌조회");
        temp.child.add("달력");
        temp.child.add("내역");
        DataList.add(temp);
        temp = new myGroup("금융비서");
        temp.child.add("소비평가");
        temp.child.add("카드추천");

        DataList.add(temp);
        temp = new myGroup("통합멤버십");
        temp.child.add("통합멤버십");

        DataList.add(temp);

        ExpandAdapter adapter = new ExpandAdapter(getApplicationContext(),R.layout.group_row,R.layout.child_row,DataList);
        listView.setGroupIndicator(null);
        listView.setAdapter(adapter);
        st = new Stack<String>();
        st.push("home");
        listView.setOnChildClickListener((ExpandableListView parent, View v, int groupPosition, int childPosition, long id) -> {

              switch (groupPosition) {
                  case 0: {
                      textTitle.setText("가계부");
                      Bundle bundle1 = new Bundle(1);
                      switch (childPosition) {
                          case 0:
                              fr = new fragment_menu1();
                              bundle1.putString("ID", userID);
                              bundle1.putInt("apage", 0);
                              st.push("a");
                              break;


                          case 1:
                              fr = new fragment_menu1();
                              bundle1.putString("ID", userID);
                              bundle1.putInt("apage", 1);
                              st.push("b");
                              break;

                          case 2:
                              fr = new fragment_menu1();
                              bundle1.putString("ID", userID);
                              bundle1.putInt("apage", 2);

                              st.push("c");

                              break;
                      }
                      changeFragment(fr, bundle1);
                      break;
                  }
                  case 1: {
                      textTitle.setText("금융비서");
                      Bundle bundle1 = new Bundle(1);

                      switch (childPosition) {
                          case 0:
                              fr = new consumptionEvaluation_viewPager();
                              bundle1.putInt("cpage", 0);
                              st.push("d");
                              changeFragment(fr, bundle1);

                              break;

                          case 1:
                              fr = new bestCard_fragment();
                              bundle1.putInt("cpage", 1);
                              st.push("e");

                              startFlagFragment(cardPrevMonth, nextMonth, BESTCARDFRAGMENT);
                              break;
                      }
                      changeFragment(fr, bundle1);
                      break;
                  }
                  case 2: {
                      textTitle.setText("통합멤버십");
                      Bundle bundle1 = new Bundle(1);
                      switch (childPosition) {
                          case 0:
                              fr = new fragment_menu3();
                              bundle1.putString("ID", userID);
                              bundle1.putInt("apage", 0);
                              st.push("f");
                              break;
                      }
                      changeFragment(fr, bundle1);
                      break;
                }
            }
            
            return false;

        });
    }


     void MainStart(){
        startFlagFragment(currentMonth, nextMonth, MAINFRAGMENT);
     }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d("KJH", "onCreateOptionsMenu()");
        welcomeTextView = findViewById(R.id.welcomeTv);
        userLastAtTxt = findViewById(R.id.userLastAtTxt);

        // 상태바 만큼 띄우기
        navChild = findViewById(R.id.nav_view_child);
        NavigationView.LayoutParams layoutParams = (NavigationView.LayoutParams) navChild.getLayoutParams();
        layoutParams.topMargin = getStatusBarHeight(getApplicationContext());
        navChild.setLayoutParams(layoutParams);


        // Device 정보 불러오기 + 권한 설정
        myDeviceInfo = getDeviceInfo();

        //로그인 액티비티에서, User 정보 전달 받기
        Intent intent = getIntent();
        myUserInfo = (UserInfo) intent.getSerializableExtra("UserInfoObject");

        userID = myUserInfo.getUserID();
        //사용자 이름 변경
        String tempLoginUser = myUserInfo.getUserName();
        if (tempLoginUser != null) {

            welcomeTextView.setText(Html.fromHtml("<u>" + tempLoginUser + "</u>")); // 밑줄 긋기

        }
        getMenuInflater().inflate(R.menu.main, menu);

        welcomeTextView.setOnClickListener(v -> StartActivity(MypageActivity.class));


        // 디비 업데이트
        previous_date = myUserInfo.getUserUpateAt();
        userLastAtTxt.setText(userLastAtTxt.getText().toString()+previous_date);


        userAccountCheck = myUserInfo.getUserAccountCheck();
        //사용자 잔액 -> 계좌 등록이 있는 경우에만 메인 화면 변경
        if (userAccountCheck == 1) {
            AccountRequest accountRequest = new AccountRequest(userID, previous_date, RequestInfo.RequestType.ACCOUNT_REFRESH, getApplicationContext());
            accountRequest.AccountRefreshHandler(time -> {
                String changeText = userLastAtTxt.getText().toString().substring(0,6) + time;
                userLastAtTxt.setText(changeText);
                MainStart();
            });

        } else if (userAccountCheck == 0) {

            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("");
            builder.setMessage("앱을 사용하려면 계좌등록을 하셔야 합니다. 계좌 등록을 하시겠습니까?");
            builder.setCancelable(false);
            builder.setPositiveButton("예", (DialogInterface dialog, int which) -> {


                        StartActivity(SettingDialogActivity.class);

            });
            builder.setNegativeButton("아니오(로그아웃)", (DialogInterface dialog, int which) -> {
                    finish();
                    Intent returnLogin = new Intent(MainActivity.this, loginActivity.class);
                    startActivity(returnLogin);
            });
            builder.show();
        }


        closeMenu = findViewById(R.id.closeMenu);
        closeMenu.setOnClickListener(v -> {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("");
                builder.setMessage("정말로 로그아웃 하시겠습니까? ");
                builder.setPositiveButton("예", (DialogInterface dialog, int which) -> {
                    SharedPreferences auto = getSharedPreferences("auto", Activity.MODE_PRIVATE);
                    SharedPreferences.Editor autoLogin = auto.edit();
                    autoLogin.clear();
                    autoLogin.commit();
                        finish();
                        Intent returnLogin = new Intent(MainActivity.this, loginActivity.class);
                        startActivity(returnLogin);
                });
                builder.setNegativeButton("아니오", (DialogInterface dialog, int which) -> {});
                builder.show();
        });

        homeMenu = findViewById(R.id.homeMenu);
        homeMenu.setOnClickListener(v -> {
                textTitle = findViewById(R.id.text_title);
                textTitle.setText("");

                startFlagFragment(currentMonth, nextMonth, MAINFRAGMENT);
        });

        userMenu = findViewById(R.id.userMenu);
        userMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                StartActivity(MypageActivity.class);

            }
        });

        settingMenu = findViewById(R.id.settingMenu);
        settingMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (deviceCheckResult.equals("")) {

                    DeviceCheckHandler();

                } else {

                    StartActivity(SettingDialogActivity.class);

                }
            }
        });

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
                   int id = item.getItemId();

//            if (id == R.id.action_settings) {
//
//                if (deviceCheckResult.equals("")) {
//
//                    DeviceCheckHandler();
//
//                } else {
//
//                    StartActivity(SettingDialogActivity.class);
//
//                }
//
//            return true;
//        } else
        if (id == R.id.refresh_btn){
            previous_date = userLastAtTxt.getText().toString().substring(6);
            //사용자 마지막 접속시간 변경
            AccountRequest accountRequest = new AccountRequest(userID, previous_date, RequestInfo.RequestType.ACCOUNT_REFRESH, getApplicationContext());
            accountRequest.AccountRefreshHandler((time) -> {
                String changeText = userLastAtTxt.getText().toString().substring(0,6) + time;
                userLastAtTxt.setText(changeText);
                Toast.makeText(getApplicationContext(), "새로고침 성공", Toast.LENGTH_LONG).show();

                for(Fragment fragment:getSupportFragmentManager().getFragments()) {
                    if(fragment.isVisible() && fragment instanceof fragment_home) {
                        MainStart();
                        textTitle.setText("");
                    }else if(fragment.isVisible() && fragment instanceof fragment_membership) {
                        Bundle bundle1 = new Bundle(1);
                        fr = new fragment_menu3();
                        bundle1.putString("ID", userID);
                        bundle1.putInt("apage", 0);
                        st.push("f");
                        changeFragment(fr, bundle1);
                    }
                }

            });
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                deviceCheckResult = data.getStringExtra("DeviceCheckResult");
                userAccountCheck = Integer.parseInt(data.getStringExtra("UserAccountCheck"));

                if (userAccountCheck == 1) {
                    MainStart();
                }
            } else if (resultCode == RESULT_CANCELED) {
                finish();
                Intent returnLogin = new Intent(MainActivity.this, loginActivity.class);
                startActivity(returnLogin);
            }
        }

        else if(requestCode == 2) {

                if(resultCode == RESULT_OK) {
                    budgetBtn = (TextView) findViewById(R.id.setBudgetBtn);
                    String budgetValue = data.getStringExtra("Budget");
                    Log.d(">>>bud", budgetValue);

                    BudgetRequest budgetRequest2 = new BudgetRequest(userID, budgetValue, RequestInfo.RequestType.CHANGE_BUDGET, getApplicationContext());

                    budgetRequest2.ChangeBudgetHandler(budget -> {
                        //Toast.makeText(getApplicationContext(), "예산 설정 성공", Toast.LENGTH_LONG).show();
                        Log.d("KJH", "MainActivity correct budget : " + budgetValue);
                        Intent intent = new Intent();
                        intent.putExtra("BUDGET", budgetValue);
                        intent.setAction("sendbudget");
                        sendBroadcast(intent);
                    });

                }
            }
    }

    protected void StartActivity(Class startClass) {
        Intent intent = new Intent(MainActivity.this, startClass);
        intent.putExtra("DeviceInfoObject", myDeviceInfo);
        intent.putExtra("DeviceCheckResult", deviceCheckResult);
        intent.putExtra("UserAccountCheck", String.valueOf(userAccountCheck));
        intent.putExtra("UserID", myUserInfo.getUserID());
        intent.putExtra("UserName", myUserInfo.getUserName());
        intent.putExtra("UserPhone", myUserInfo.getUserPhone());

        startActivityForResult(intent, 1);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        return true;
    }

    public void changeFragment(Fragment fr, Bundle bundle) {

        if(bundle == null){
            bundle = new Bundle(1);
        }
        bundle.putSerializable("DATA", sData);
        bundle.putString("userID", userID);

        fr.setArguments(bundle);

        FragmentManager fm = getSupportFragmentManager();

        FragmentTransaction fragmentTransaction = fm.beginTransaction();

        fragmentTransaction.replace(R.id.dynamic_mainFragment, fr);


        fragmentTransaction.addToBackStack(null);

        fragmentTransaction.commit();


        DrawerLayout drawer = findViewById(R.id.drawer_layout);

        drawer.closeDrawer(GravityCompat.START);



    }

    /*
        DeviceCheckHandler
        = 단말 정보 확인 처리 핸들러
     */
    public void DeviceCheckHandler() {

        RequestInfo requestInfo = new RequestInfo(RequestInfo.RequestType.DEVICE_CHECK);
        String url = "http://" + requestInfo.GetRequestIP() + ":" + requestInfo.GetRequestPORT() + requestInfo.GetProcessURL();
        StringRequest request = new StringRequest(Request.Method.POST, url,
            (response) -> DeviceCheckResponse(response),
                (error) -> {
                    error.getMessage();
                    error.printStackTrace();
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
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

    private Map<String, String> DeviceCheckRequest() {
        Map<String, String> params = new HashMap<>();

        params.put("mobile", myDeviceInfo.getMobile());

        return params;
    }

    /*
        DeviceCheckResponse(String): void
        = 단말 정보 확인 요청 응답 처리 함수
    */

    private void DeviceCheckResponse(String response) {
        try {
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

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void ShowToast(String s) {
        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
    }

    /*
        getDeviceInfo : DeviceInfo
        = 디바이스 정보 얻는 함수
     */

    public DeviceInfo getDeviceInfo() {
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

        TelephonyManager phoneMgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            return "";
        }
        return phoneMgr.getLine1Number();
    }

    /*
        CheckPermission(String): boolean
        = 권한 확인 함수
     */

    private boolean CheckPermission(String permission) {
        if (Build.VERSION.SDK_INT >= 23) {
            int result = ContextCompat.checkSelfPermission(this, permission);
            if (result == PackageManager.PERMISSION_GRANTED) {

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

    private void RequestPermission(String permission) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
            Toast.makeText(this, "단말 정보를 위해 휴대전화 상태 권한을 허가해야 합니다. 추가적인 기능을 위해 허가해 주시기 바랍니다.", Toast.LENGTH_LONG).show();
        }
        ActivityCompat.requestPermissions(this, new String[]{permission}, MY_PERMISSIONS_REQUEST_READ_PHONE_STATE);
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

    private static String getDisplay(Context context) {

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

    private static String getMacAddress(Context context) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifiManager.getConnectionInfo();
        return info.getMacAddress();
    }

    public static int getStatusBarHeight(Context context) {

        int result = 0;

        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");

        if (resourceId > 0) {

            result = context.getResources().getDimensionPixelSize(resourceId);

        }
        return result;
    }

    public void startFlagFragment(String startDate, String endDate, int flag) {
        //내역을 얻어와야함
        //먼저 HistoryRequest 각각 정보 입력하여 객체생성
        HistoryRequest testRequest = new HistoryRequest(
                myUserInfo.getUserID(),                            //사용자 아이디 myUserInfo 객체에서 getUserID()받아와 사용하시면되요
                startDate,                               //전달의 시작 날짜 - 일은 01로 고정시키고 년도랑 월만 계산해서 가져오시면되요(시작일은 무조건 01이므로)
                endDate,                               //전달의 마지막 날짜 - 일은 31로 고정시키고 년도랑 월만 게산해서 가져오시면되요 (최대 31일이므로)
                RequestInfo.RequestType.ACCOUNT_HOME_HISTORY,      //이거는 고정
                getApplicationContext());                          //이거는 context 얻어오는 건데 여기는 액티비티라서 getApplicationContext()해서 받아오는데

        //fragment 쪽에서는 getContext()하시면 될 것 같아요


        //HomeRequest(callback - onSuccess Override)를해서 정보 받아옴
        testRequest.HomeRequest(new HistoryRequest.VolleyCallback() {
            @Override
            public void onSuccess(HistoryInfo[] historyInfo, DailyHistoryInfo[] dailyHistoryInfo) {
                AsyncTask<Void, Void, Void> MyTask = new AsyncTask<Void, Void, Void>() {
                    CustomProgressDialog dialog = new CustomProgressDialog(MainActivity.this);

                    @Override
                    protected void onPreExecute() {
                        dialog.show();
                        super.onPreExecute();
                    }

                    @Override
                    protected Void doInBackground(Void... voids) {
                        int arrLength = historyInfo.length;

                        ArrayList<Stat> temp = new ArrayList<Stat>();
                        Stat Culture = new Stat(Stat.CULTURE, userID);
                        Stat Food = new Stat(Stat.FOOD, userID);
                        Stat Finance = new Stat(Stat.FINANCE, userID);
                        Stat Traffic = new Stat(Stat.TRAFFIC, userID);
                        Stat None = new Stat(Stat.NONE, userID);
                        Stat Life = new Stat(Stat.LIFE, userID);
                        Stat Coffee = new Stat(Stat.COFFEE, userID);
                        Stat Dwelling = new Stat(Stat.DWELLING, userID);
                        Stat Drink = new Stat(Stat.DRINK, userID);
                        Stat Travel = new Stat(Stat.TRAVEL, userID);
                        Stat Hospital = new Stat(Stat.HOSPITAL, userID);
                        Stat AccountsLoss = new Stat(Stat.ACCOUNTSLOSS, userID);

                        String[] cName = new String[arrLength];
                        PayInfomation p;
                        for (int i = 0; i < arrLength; i++) {
                            cName[i] = historyInfo[i].getcName();        //카테고릐 분류
                            Date date = new Date(historyInfo[i].gethDate());
                            switch (cName[i]) {
                                case "술/유흥":
                                    p = new PayInfomation(historyInfo[i].gethName(),
                                            Integer.parseInt(historyInfo[i].gethValue()), Drink, date, historyInfo[i].gethId());
                                    break;
                                case "생활(쇼핑)":
                                    p = new PayInfomation(historyInfo[i].gethName(),
                                            Integer.parseInt(historyInfo[i].gethValue()), Life, date, historyInfo[i].gethId());
                                    break;
                                case "교통":
                                    p = new PayInfomation(historyInfo[i].gethName(),
                                            Integer.parseInt(historyInfo[i].gethValue()), Traffic, date, historyInfo[i].gethId());
                                    break;
                                case "주거/통신":
                                    p = new PayInfomation(historyInfo[i].gethName(),
                                            Integer.parseInt(historyInfo[i].gethValue()), Dwelling, date, historyInfo[i].gethId());
                                    break;
                                case "의료/건강":
                                    p = new PayInfomation(historyInfo[i].gethName(),
                                            Integer.parseInt(historyInfo[i].gethValue()), Hospital, date, historyInfo[i].gethId());
                                    break;
                                case "금융":
                                    p = new PayInfomation(historyInfo[i].gethName(),
                                            Integer.parseInt(historyInfo[i].gethValue()), Finance, date, historyInfo[i].gethId());
                                    break;
                                case "문화/여가":
                                    p = new PayInfomation(historyInfo[i].gethName(),
                                            Integer.parseInt(historyInfo[i].gethValue()), Culture, date, historyInfo[i].gethId());
                                    break;
                                case "여행/숙박":
                                    p = new PayInfomation(historyInfo[i].gethName(),
                                            Integer.parseInt(historyInfo[i].gethValue()), Travel, date, historyInfo[i].gethId());
                                    break;
                                case "식비":
                                    p = new PayInfomation(historyInfo[i].gethName(),
                                            Integer.parseInt(historyInfo[i].gethValue()), Food, date, historyInfo[i].gethId());
                                    break;
                                case "카페/간식":
                                    p = new PayInfomation(historyInfo[i].gethName(),
                                            Integer.parseInt(historyInfo[i].gethValue()), Coffee, date, historyInfo[i].gethId());
                                    break;
                                case "미분류":
                                    p = new PayInfomation(historyInfo[i].gethName(),
                                            Integer.parseInt(historyInfo[i].gethValue()), None, date, historyInfo[i].gethId());
                                    break;
                                case "계좌출금":
                                    p = new PayInfomation(historyInfo[i].gethName(),
                                            Integer.parseInt(historyInfo[i].gethValue()), AccountsLoss, date, historyInfo[i].gethId());
                                    break;
                                default:
                                    break;
                            }
                        }

                        temp.add(Drink);
                        temp.add(Life);
                        temp.add(Traffic);
                        temp.add(Dwelling);
                        temp.add(Hospital);
                        temp.add(Finance);
                        temp.add(Culture);
                        temp.add(Travel);
                        temp.add(Food);
                        temp.add(Coffee);
                        temp.add(None);
                        temp.add(AccountsLoss);

                        sData.clear();
                        for (int i = 0; i < temp.size(); i++) {
                            if (!temp.get(i).isEmpty())
                                sData.add(temp.get(i));
                        }
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        switch (flag) {
                            case MAINFRAGMENT:
                                Bundle args = new Bundle();
                                fr = new fragment_home();

                                Log.d("KJH", "startMainFragment()");

                                args.putSerializable("DATA", sData);

                                fr.setArguments(args);
                                FragmentManager fm = getSupportFragmentManager();
                                FragmentTransaction fragmentTransaction = fm.beginTransaction();
                                fragmentTransaction.replace(R.id.dynamic_mainFragment, fr);
                                fragmentTransaction.commit();

                                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        drawer.closeDrawer(GravityCompat.START);
                                    }
                                });
                                break;
                            case BESTCARDFRAGMENT:
                                fr = new bestCard_fragment();

                                Bundle args2 = new Bundle();
                                args2.putSerializable("DATA", sData);
                                fr.setArguments(args2);
                                FragmentManager fm2 = getSupportFragmentManager();
                                FragmentTransaction fragmentTransaction2 = fm2.beginTransaction();
                                fragmentTransaction2.replace(R.id.dynamic_mainFragment, fr);
                                fragmentTransaction2.addToBackStack(null);
                                fragmentTransaction2.commit();

                                drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                                if (drawer.isDrawerOpen(GravityCompat.START)) {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            drawer.closeDrawer(GravityCompat.START);
                                        }
                                    });
                                }
                                break;
                        }
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void aVoid) {
                        dialog.dismiss();
                        super.onPostExecute(aVoid);
                    }
                };
                MyTask.execute();
            }
        });
    }
    @Override
    protected void onDestroy() {
        unregisterReceiver(receiver);
        super.onDestroy();
    }

}