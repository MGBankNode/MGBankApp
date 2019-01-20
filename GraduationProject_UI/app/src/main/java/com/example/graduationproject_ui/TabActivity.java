package com.example.graduationproject_ui;

import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class TabActivity extends AppCompatActivity {
    ViewPager pager;
    ImageButton btn_first;
    ImageButton btn_second;
    ImageButton btn_third;
    ImageButton btn_quad;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab);
        pager = (ViewPager)findViewById(R.id.pager);
        btn_first = (ImageButton) findViewById(R.id.btn_first);
        btn_second = (ImageButton)findViewById(R.id.btn_second);
        btn_third = (ImageButton)findViewById(R.id.btn_third);
        btn_quad = (ImageButton)findViewById(R.id.btn_quad);

        pager.setAdapter(new pagerAdapter(getSupportFragmentManager()));
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
        btn_quad.setOnClickListener(movePageListener);
        btn_quad.setTag(3);

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
                        btn_quad.setBackgroundResource(R.drawable.tab_btn);
                        break;
                    case 1:
                        btn_first.setBackgroundResource(R.drawable.tab_btn);
                        btn_second.setBackgroundResource(R.drawable.tabbed_btn);
                        btn_third.setBackgroundResource(R.drawable.tab_btn);
                        btn_quad.setBackgroundResource(R.drawable.tab_btn);
                        break;
                    case 2:
                        btn_first.setBackgroundResource(R.drawable.tab_btn);
                        btn_second.setBackgroundResource(R.drawable.tab_btn);
                        btn_third.setBackgroundResource(R.drawable.tabbed_btn);
                        btn_quad.setBackgroundResource(R.drawable.tab_btn);
                        break;
                    case 3:
                        btn_first.setBackgroundResource(R.drawable.tab_btn);
                        btn_second.setBackgroundResource(R.drawable.tab_btn);
                        btn_third.setBackgroundResource(R.drawable.tab_btn);
                        btn_quad.setBackgroundResource(R.drawable.tabbed_btn);
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
        btn_quad.setBackgroundResource(R.drawable.tab_btn);
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
                    return new test1();
                case 1:
                    return new test2();
                case 2:
                    return new test3();
                case 3:
                    return new test4();
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            // total page count
            return 4;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
