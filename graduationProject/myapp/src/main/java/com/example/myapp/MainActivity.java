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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;

import android.widget.ListView;
import android.widget.TextView;

import android.app.AlertDialog;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    Fragment fr ;
    TextView welcomeTextView;
    ImageView closeMenu;

    private ListView menu1list;
    private ListView menu2list;
    private ListView menu3list;

    Toolbar toolbar;

    final FragmentManager fm = getSupportFragmentManager();
    private backPressCloseHandler backPressCloseHandler;

    private static int MY_PERMISSIONS_REQUEST_READ_SMS = 1;
    private static int MY_PERMISSIONS_REQUEST_READ_PHONE_STATE = 2;

    public DeviceInfo myDeviceInfo;
    public String deviceCheckResult = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // Device 정보 불러오기 + 권할 설정
        myDeviceInfo = getDeviceInfo();

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

        menu1list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {

                    case 0:
                        fr = new fragment_menu1();
                        fr.setArguments(makeBundle("apage", 0));
                        break;

                    case 1:
                        fr = new fragment_menu1();
                        fr.setArguments(makeBundle("apage", 1));
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
                        break;

                    case 1:
                        fr = new consumptionEvaluation_viewPager();
                        fr.setArguments(makeBundle("cpage", 1));
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
            backPressCloseHandler.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        welcomeTextView = findViewById(R.id.welcomeTv);
        Intent intent = getIntent();
        String tempLoginUser = intent.getStringExtra("loginUser");
        if(tempLoginUser != null) {
            welcomeTextView.setText(Html.fromHtml("<u>" + tempLoginUser + "</u>")); // 밑줄 긋기
        }
        getMenuInflater().inflate(R.menu.main, menu);

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

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {

            Intent intent = new Intent(this, SettingDialogActivity.class);
            intent.putExtra("DeviceInfoObject", myDeviceInfo);
            if(deviceCheckResult.equals("")){

                /*
                    정보가 없는 경우 요청 처리 기능
                 */
                deviceCheckResult = "NO";
                deviceCheckResult = "YES";
            }
            intent.putExtra("DeviceCheckResult", deviceCheckResult);
            startActivityForResult(intent, 1);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 1){
            if(resultCode == RESULT_OK){
                deviceCheckResult = data.getStringExtra("DeviceCheckResult");
            }
        }
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
        getDeviceInfo : DeviceInfo
        = 디바이스 정보 얻는 함수
     */

    public DeviceInfo getDeviceInfo(){
        DeviceInfo myDevice;

        String mobile;
        if((mobile = getPhoneNumber(this)) == null){
            return null;
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

    public String getPhoneNumber(Activity activity) {
        int permissionCheck = ContextCompat.checkSelfPermission(activity.getApplicationContext(), Manifest.permission.READ_SMS);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    activity,
                    new String[]{Manifest.permission.READ_SMS},
                    MY_PERMISSIONS_REQUEST_READ_SMS);
        } else {
            permissionCheck = ContextCompat.checkSelfPermission(activity.getApplicationContext(), Manifest.permission.READ_PHONE_STATE);
            if(permissionCheck != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(
                        activity,
                        new String[]{Manifest.permission.READ_PHONE_STATE},
                        MY_PERMISSIONS_REQUEST_READ_PHONE_STATE);
                TelephonyManager phoneMgr = (TelephonyManager) activity.getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
                if(phoneMgr.getLine1Number() != null){
                    return phoneMgr.getLine1Number();
                }
            }
        }
        return null;
    }

    /*
        onRequestPermissionsResult : void
        = 권한 요청 결과 처리
     */

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE);
                    if(permissionCheck != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(
                                this,
                                new String[]{Manifest.permission.READ_PHONE_STATE},
                                MY_PERMISSIONS_REQUEST_READ_PHONE_STATE);
                    }

                } else {
                    finish();
                }
                return;
            }

            case 2: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

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
}
