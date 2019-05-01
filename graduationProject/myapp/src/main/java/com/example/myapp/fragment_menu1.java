package com.example.myapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

/////////////////Tab 화면 구현 및 Parent fragment
public class fragment_menu1 extends Fragment {
    public fragment_menu1() {    }

    TabLayout tabLayout;
    ViewPager pager;

    @Override //nested fragment 구현 ---> 현재 parent fragment
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu1, container, false);

        // tab part
        tabLayout = view.findViewById(R.id.layout_tab);
        tabLayout.addTab(tabLayout.newTab().setText("달력"));
        tabLayout.addTab(tabLayout.newTab().setText("내역"));

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        int pageNum = getArguments().getInt("apage");

        pager = getView().findViewById(R.id.pager);

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
            switch(position)
            {
                case 0:
                    return new menu1_fragment_tab1();
                case 1:
                    return new menu1_fragment_tab2();
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            // total page count
            return 2;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
