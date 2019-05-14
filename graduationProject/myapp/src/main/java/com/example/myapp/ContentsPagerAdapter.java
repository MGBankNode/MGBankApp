package com.example.myapp;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class ContentsPagerAdapter extends FragmentStatePagerAdapter {

    private int mPageCount;

    public ContentsPagerAdapter(FragmentManager fm, int mPageCount) {
        super(fm);
        this.mPageCount = mPageCount;
    }

    @Override
    public Fragment getItem(int position) {
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
