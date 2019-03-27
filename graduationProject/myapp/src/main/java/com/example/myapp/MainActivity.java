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
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    Fragment fr ;

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

    public void onConfirmBtnClicked(View view) {

        EditText etID;
        EditText etPassword;
        EditText etName;
        EditText etPhoneNum;


        // 아이디 비밀번호 이름 전화번호
        etID = findViewById(R.id.etID);
        etPassword = findViewById(R.id.etPassword);
        etName = findViewById(R.id.etName);
        etPhoneNum = findViewById(R.id.etPhoneNum);


        if(etID.getText().toString().length() == 0) {
            Toast.makeText(getApplication(), "아이디를 입력하지 않았습니다", Toast.LENGTH_SHORT).show();
            etID.requestFocus();
        }

        else if(etPassword.getText().toString().length() == 0) {
            Toast.makeText(getApplication(), "비밀번호를 입력하지 않았습니다", Toast.LENGTH_SHORT).show();
            etPassword.requestFocus();
        }

        else if(etName.getText().toString().length() == 0) {
            Toast.makeText(getApplication(), "이름을 입력하지 않았습니다", Toast.LENGTH_SHORT).show();
            etName.requestFocus();
        }

        else if(etPhoneNum.getText().toString().length() == 0) {
            Toast.makeText(getApplication(), "전화번호 입력하지 않았습니다", Toast.LENGTH_SHORT).show();
            etPhoneNum.requestFocus();
        }

        else {
            Toast.makeText(getApplication(), "다음화면으로 넘어갑니다", Toast.LENGTH_SHORT).show();
        }
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

}
