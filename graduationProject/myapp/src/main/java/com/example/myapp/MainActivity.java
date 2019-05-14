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
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.ImageView;

import android.widget.LinearLayout;
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
import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Calendar;
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
    protected LinearLayout noticeMenu;
    protected LinearLayout navChild;
    protected Button budgetBtn;
    protected Button setBudget;
    private ExpandableListView listView;
    private static final int MAINFRAGMENT = 1001;
    private static final int BESTCARDFRAGMENT = 1002;


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

    @Override
    public void onBackPressed() {
        Log.i("nkw","onBackpressed()");
        textTitle.setText("");
        super.onBackPressed();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
        temp = new myGroup("통합맴버쉽");
        temp.child.add("통합맴버쉽");

        DataList.add(temp);

        ExpandAdapter adapter = new ExpandAdapter(getApplicationContext(),R.layout.group_row,R.layout.child_row,DataList);
        listView.setGroupIndicator(null);
        listView.setAdapter(adapter);
        st = new Stack<String>();
        st.push("home");
        listView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                // 0 0 계좌조회  0 1 달력   0 2 내역
                // 1 0 소비평가  1 1 카드추천
                // 2 0 통합맴버쉽


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
                                startFlagFragment("2019-01-01", "2019-06-01", BESTCARDFRAGMENT);
                                break;
                        }
                        break;
                    }

                    case 2: {
                        textTitle.setText("통합멤버쉽");
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

            }
        });
//            setBudget = findViewById(R.id.setBudgetBtn);
//            setBudget.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                Intent intent = new Intent(MainActivity.this, SetBudget.class);
//                startActivityForResult(intent, 2);
////                BudgetRequest budgetRequest2 = new BudgetRequest(b, "1000000",RequestInfo.RequestType.CHANGE_BUDGET, getApplicationContext());
////
////                budgetRequest2.ChangeBudgetHandler(budget -> {
////                    Toast.makeText(getApplicationContext(), "예산 설정 성공", Toast.LENGTH_LONG).show();
////                });
//            }
//        });

    }

    public Bundle makeBundle(String str, int num) {
        Bundle bundle = new Bundle(1);
        bundle.putInt(str, num);
        return bundle;
    }


     void MainStart(){
        startFlagFragment("2019-05-01", "2019-06-01", MAINFRAGMENT);

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
        if (userAccountCheck == 1) {
            //계좌 등록이 되어있는 경우
            //잔액은 로그인 시에 myUserInfo의 userABalance에 담겨 넘겨옴
            //getUserABalance() 함수를 호출해서 값을 가져오기만 하면됨
//            TextView userABalanceTxtView = findViewById(R.id.mainFragment_textView);
//
//            String userABalance = myUserInfo.getUserABalance() + "원";
//            Toast.makeText(getApplicationContext(), userABalance, Toast.LENGTH_SHORT).show();
//            userABalanceTxtView.setText(userABalance);

            MainStart();

        } else if (userAccountCheck == 0) {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("");
            builder.setMessage("앱을 사용하려면 계좌등록을 하셔야 합니다. 계좌 등록을 하시겠습니까?");
            builder.setCancelable(false);
            builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (deviceCheckResult.equals("")) {

                        DeviceCheckHandler();

                    } else {

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
                textTitle = (TextView)findViewById(R.id.text_title);
                textTitle.setText("");
                startFlagFragment("2019-05-01", "2019-06-01", MAINFRAGMENT);
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

            if (deviceCheckResult.equals("")) {

                DeviceCheckHandler();

            } else {

                StartActivity(SettingDialogActivity.class);

            }

            return true;
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
                    budgetBtn = findViewById(R.id.setBudgetBtn);
                    String budgetValue = data.getStringExtra("Budget");
                    Log.d(">>>bud", budgetValue);

                  BudgetRequest budgetRequest2 = new BudgetRequest(userID, budgetValue,RequestInfo.RequestType.CHANGE_BUDGET, getApplicationContext());

                  budgetRequest2.ChangeBudgetHandler(budget -> {
                     Toast.makeText(getApplicationContext(), "예산 설정 성공", Toast.LENGTH_LONG).show();
                 });

                    budgetValue += "원";
                    budgetBtn.setText(budgetValue);
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

        fr.setArguments(bundle);

        FragmentManager fm = getSupportFragmentManager();

        FragmentTransaction fragmentTransaction = fm.beginTransaction();

        fragmentTransaction.replace(R.id.dynamic_mainFragment, fr);

        fragmentTransaction.setCustomAnimations(R.anim.fragment_in, R.anim.fragment_out);

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
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        DeviceCheckResponse(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
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

//    public void startMainFragment() {
//        Log.d("KJH", "startMainFragment()");
//        fr = new fragment_home();
//
//        Bundle args = new Bundle();
//        args.putSerializable("DATA", sData);
//        fr.setArguments(args);
//        FragmentManager fm = getSupportFragmentManager();
//        FragmentTransaction fragmentTransaction = fm.beginTransaction();
//        fragmentTransaction.replace(R.id.dynamic_mainFragment, fr);
//        fragmentTransaction.commit();
//
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        drawer.closeDrawer(GravityCompat.START);
//
//    }


    public static int getStatusBarHeight(Context context) {

        int result = 0;

        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");

        if (resourceId > 0) {

            result = context.getResources().getDimensionPixelSize(resourceId);

        }
        return result;
    }
    public void startFlagFragment(String startDate, String endDate, int flag){
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

                String[] hValue = new String[arrLength];
                String[] hName = new String[arrLength];
                String[] cName = new String[arrLength];
                PayInfomation p;
                for (int i = 0; i < arrLength; i++) {
                    cName[i] = historyInfo[i].getcName();        //카테고릐 분류
                    Date date = new Date(historyInfo[i].gethDate());
                    Log.d("KJh", "Date : " + date);
                    Log.d("KJH", "origin Date : " + historyInfo[i].gethDate());
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

                sData.clear();
                for (int i = 0; i < temp.size(); i++) {
                    if (!temp.get(i).isEmpty())
                        sData.add(temp.get(i));
                }

                switch (flag){
                    case MAINFRAGMENT:
                        //////////////////////////////////설정된 예산 요청///////////////////////
                        BudgetRequest budgetRequest1 = new BudgetRequest(userID, RequestInfo.RequestType.DEFAULT_BUDGET, getApplicationContext());

                        budgetRequest1.GetBudgetHandler(budget -> {
                            Toast.makeText(getApplicationContext(), budget, Toast.LENGTH_LONG).show();
                        });
                        //////////////////////////////////////////////////////////////////

                        Log.d("KJH", "startMainFragment()");
                        fr = new fragment_home();

                        Bundle args = new Bundle();
                        args.putSerializable("DATA", sData);
                        fr.setArguments(args);
                        FragmentManager fm = getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fm.beginTransaction();
                        fragmentTransaction.replace(R.id.dynamic_mainFragment, fr);
                        fragmentTransaction.commit();

                        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                        drawer.closeDrawer(GravityCompat.START);
                        break;
                    case BESTCARDFRAGMENT:
                        fr = new bestCard_fragment();

                        Bundle args2 = new Bundle();
                        args2.putSerializable("DATA", sData);
                        fr.setArguments(args2);
                        FragmentManager fm2 = getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction2 = fm2.beginTransaction();
                        fragmentTransaction2.replace(R.id.dynamic_mainFragment, fr);
                        fragmentTransaction2.commit();
                        break;
                }
            }
        });
    }

}