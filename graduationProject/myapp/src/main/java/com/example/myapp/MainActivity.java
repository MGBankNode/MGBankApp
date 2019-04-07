package com.example.myapp;

import android.content.DialogInterface;
import android.content.Intent;
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

import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import android.widget.TextView;

import android.app.AlertDialog;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    Fragment fr ;
    TextView welcomeTextView;
    ImageView closeMenu;

    Toolbar toolbar;

    final FragmentManager fm = getSupportFragmentManager();
    private backPressCloseHandler backPressCloseHandler;
    private Intent intent;

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
            return true;
        }

        return super.onOptionsItemSelected(item);
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

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.dynamic_mainFragment, fr);
        fragmentTransaction.commit();

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        if(!url.equals("")) {
            Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(myIntent);
        }


        return true;
    }
    public void onMenuBtnClicked(View view) {

//        switch (view.getId()) {
//            case (R.id.menuBtn1) :
//                fr = new fragment_menu1();
//                break;
//
//            case (R.id.menuBtn2) :
//                fr = new consumptionEvaluation_viewPager();
//                break;
//
//            case (R.id.menuBtn3) :
//                fr = new fragment_menu3();
//                break;
//
//            default:
//                break;
//        }

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.dynamic_mainFragment, fr);
        fragmentTransaction.commit();

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }

    public void onDetailBtnClicked(View view) {
        Fragment detailFragment = new consumptionReportFragment();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentContainer_viewpager, detailFragment);
        fragmentTransaction.commit();
    }

}
