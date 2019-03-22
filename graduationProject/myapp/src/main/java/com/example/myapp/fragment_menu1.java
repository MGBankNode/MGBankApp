package com.example.myapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

/////////////////Tab 화면 구현 및 Parent fragment
public class fragment_menu1 extends Fragment {
    public fragment_menu1() {    }

    ViewPager pager;
    ImageButton btn_first;
    ImageButton btn_second;
    ImageButton btn_third;

    @Override //nested fragment 구현 ---> 현재 parent fragment
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu1, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        pager = (ViewPager)getView().findViewById(R.id.pager);
        btn_first = (ImageButton) getView().findViewById(R.id.btn_first);
        btn_second = (ImageButton)getView().findViewById(R.id.btn_second);
        btn_third = (ImageButton)getView().findViewById(R.id.btn_third);

        pager.setAdapter(new pagerAdapter(getFragmentManager()));
        pager.setCurrentItem(0);

        View.OnClickListener movePageListener = new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                int tag = (int)view.getTag();
                pager.setCurrentItem(tag);
            }
        };

        btn_first.setOnClickListener(movePageListener);
        btn_first.setTag(0);
        btn_second.setOnClickListener(movePageListener);
        btn_second.setTag(1);
        btn_third.setOnClickListener(movePageListener);
        btn_third.setTag(2);


        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener(){
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels)
            {

            }
            //페이지가 바뀔때
            @Override
            public void onPageSelected(int position){
                switch(position)
                {
                    case 0:
                        btn_first.setBackgroundResource(R.drawable.tabbed_btn);
                        btn_second.setBackgroundResource(R.drawable.tab_btn);
                        btn_third.setBackgroundResource(R.drawable.tab_btn);
                        break;
                    case 1:
                        btn_first.setBackgroundResource(R.drawable.tab_btn);
                        btn_second.setBackgroundResource(R.drawable.tabbed_btn);
                        btn_third.setBackgroundResource(R.drawable.tab_btn);
                        break;
                    case 2:
                        btn_first.setBackgroundResource(R.drawable.tab_btn);
                        btn_second.setBackgroundResource(R.drawable.tab_btn);
                        btn_third.setBackgroundResource(R.drawable.tabbed_btn);
                        break;

                    default:
                        break;
                }
            }
            @Override
            public void onPageScrollStateChanged(int state)
            {

            }
        });
        btn_first.setBackgroundResource(R.drawable.tabbed_btn);
        btn_second.setBackgroundResource(R.drawable.tab_btn);
        btn_third.setBackgroundResource(R.drawable.tab_btn);


        super.onActivityCreated(savedInstanceState);
    }
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
                case 2:
                    return new menu1_fragment_tab3();
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
