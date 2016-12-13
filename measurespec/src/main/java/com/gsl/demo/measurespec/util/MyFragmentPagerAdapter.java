package com.gsl.demo.measurespec.util;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

import java.util.List;

/**
 * Created by guosenlin on 16-10-26.
 */

public class MyFragmentPagerAdapter extends FragmentStatePagerAdapter {
    private static final String TAG = "MyFragmentPagerAdapter";
    private List<Fragment> fragmentList;

    public MyFragmentPagerAdapter(FragmentManager fm, List<Fragment> fragmentList) {
        super(fm);
        this.fragmentList = fragmentList;
    }

    @Override
    public Fragment getItem(int position) {
        //Log.d(TAG, "--------------------getItem----------------------->>>"+position);
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

}
