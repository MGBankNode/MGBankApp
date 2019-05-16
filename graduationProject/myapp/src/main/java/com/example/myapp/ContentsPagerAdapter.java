package com.example.myapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

public class ContentsPagerAdapter extends FragmentStatePagerAdapter {

    private int mPageCount;

    String userID;
    public ContentsPagerAdapter(FragmentManager fm, int mPageCount, String userID) {
        super(fm);
        this.mPageCount = mPageCount;
        this.userID=userID;
        Log.d("KJH", "ContentsPagerAdapter userID : " + userID);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fr = new consumptionEvaluationFragment();
        Bundle bundle = new Bundle(1);
        bundle.putString("userID",userID);
        fr.setArguments(bundle);

        switch (position) {

            case 0:
                viewpagerMenu1Fragment viewpagerMenu1Fragment = new viewpagerMenu1Fragment();
                return viewpagerMenu1Fragment;

            case 1:
                viewpagerMenu1Fragment bestCard_fragment = new viewpagerMenu1Fragment();
                return bestCard_fragment;

            default:
                return null;
        }

    }

    @Override
    public int getCount() {
        return mPageCount;
    }
}
