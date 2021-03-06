package com.example.myapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

/////////////////Tab 화면 구현 및 Parent fragment
public class fragment_menu1 extends Fragment {
    public fragment_menu1() {    }

    String userID;
    TabLayout tabLayout;
    ViewPager pager;
    int flag = 0;
    String accountNum;

    @Override //nested fragment 구현 ---> 현재 parent fragment
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu1, container, false);

        // tab part
        tabLayout = view.findViewById(R.id.layout_tab);
        tabLayout.addTab(tabLayout.newTab().setText("계좌조회"));
        tabLayout.addTab(tabLayout.newTab().setText("달력"));
        tabLayout.addTab(tabLayout.newTab().setText("내역"));

        if(getArguments() != null){
            userID = getArguments().getString("ID");
            accountNum = getArguments().getString("accountNum");
            Log.i("nkw","menu1_userID="+userID);
        }

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        int pageNum = getArguments().getInt("apage");
        pager = getView().findViewById(R.id.pager);

        if(pageNum == 3) {
            pageNum = 0;
            flag = 1;
        }
        pager.setAdapter(new pagerAdapter(getFragmentManager()));
        pager.setCurrentItem(pageNum);

        pager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                pager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        super.onActivityCreated(savedInstanceState);
    }

    //페이저 어답터 클래스
    private class pagerAdapter extends FragmentStatePagerAdapter
    {
        public pagerAdapter(FragmentManager fm )
        {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            Fragment fr0 = new menu1_fragment_tab0();
            Fragment fr1 = new menu1_fragment_tab1();
            Fragment fr2 = new menu1_fragment_tab2();
            Bundle bundle = new Bundle(1);
            bundle.putString("ID", userID);
            if(flag == 1) {
                position = 2;
                bundle.putString("accountNum", accountNum);
                flag = 0;
            }

            fr0.setArguments(bundle);
            fr1.setArguments(bundle);
            fr2.setArguments(bundle);

            switch(position)
            {
                case 0:
                    return fr0;
                case 1:
                    return fr1;
                case 2:
                    return fr2;
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            // total page count
            return 3;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}