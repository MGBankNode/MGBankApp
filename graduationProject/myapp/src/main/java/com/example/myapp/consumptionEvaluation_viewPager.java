package com.example.myapp;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class consumptionEvaluation_viewPager extends Fragment {

    private TabLayout tabLayout;

    private ViewPager viewPager;
    private ContentsPagerAdapter contentsPagerAdapter;
    private FragmentActivity myContext;
    private FragmentManager fragmanager;

    @Override
    public void onAttach(Activity activity) {
        myContext = (FragmentActivity) activity;

        super.onAttach(activity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.viewpager_consumption_analysis, container, false) ;

        int pageNum = getArguments().getInt("cpage");

        // tab part
        tabLayout = view.findViewById(R.id.layout_tab);
        tabLayout.addTab(tabLayout.newTab().setText("소비평가"));
        tabLayout.addTab(tabLayout.newTab().setText("카드추천"));


        fragmanager = myContext.getSupportFragmentManager();

        // connect ViewPager and TabLayout
        viewPager = view.findViewById(R.id.pager_content);
        contentsPagerAdapter = new ContentsPagerAdapter(fragmanager, tabLayout.getTabCount());

        viewPager.setAdapter(contentsPagerAdapter);
        viewPager.setCurrentItem(pageNum);


        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        return view;
    }
}
