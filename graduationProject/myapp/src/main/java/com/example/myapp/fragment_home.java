package com.example.myapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class fragment_home extends Fragment {

    //잔고 뷰페이저와 소비 뷰페이저
    ViewPager mainViewPager = null;
    ViewPager mainPayViewPager = null;
    //뷰페이저 어댑터
    FragmentAdapter fragmentPagerAdapter = null;
    FragmentAdapter payFragmentPagerAdapter = null;
    public fragment_home() {    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    //프래그먼트의 어댑터 설정은 엑티비티가 생성된후 설정해야함(NullPointer 에러)
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        //잔고 뷰페이저 셋팅
        setHomeViewPager();
        //소비 뷰페이저 셋팅
        setHomePayViewPager();

        super.onActivityCreated(savedInstanceState);
    }

    //잔고 뷰페이저에 프래그먼트 연결
    public void setHomeViewPager()
    {
        //뷰페이저와 어댑터 연결
        mainViewPager = getView().findViewById(R.id.mainViewPager);
        fragmentPagerAdapter = new FragmentAdapter(getFragmentManager());
        mainViewPager.setAdapter(fragmentPagerAdapter);

        //어댑터에 프레그먼트 추가
        fragmentPagerAdapter.addItem(new HomeTextFragment());
        fragmentPagerAdapter.addItem(new HomeListFragment());

        fragmentPagerAdapter.notifyDataSetChanged();
    }
    //소비 뷰페이저에 프래그먼트 연결
    public void setHomePayViewPager()
    {
        //뷰페이저와 어댑터 연결
        mainPayViewPager = getView().findViewById(R.id.payViewPager);
        payFragmentPagerAdapter = new FragmentAdapter(getFragmentManager());
        mainPayViewPager.setAdapter(payFragmentPagerAdapter);

        //어댑터에 프레그먼트 추가
        payFragmentPagerAdapter.addItem(new HomePayTextFragment());
        payFragmentPagerAdapter.addItem(new HomePayListFragment());

        payFragmentPagerAdapter.notifyDataSetChanged();
    }

    //뷰페이저 어댑터
    public class FragmentAdapter extends FragmentPagerAdapter{
        // ViewPager에 들어갈 Fragment들을 담을 리스트
        private ArrayList<Fragment> fragments = new ArrayList<>();

        // 필수 생성자
        public FragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        // List에 Fragment를 담을 함수
        void addItem(Fragment fragment) {
            fragments.add(fragment);
        }
    }

}
