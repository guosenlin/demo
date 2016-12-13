package com.gsl.demo.measurespec.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;

import com.gsl.demo.measurespec.R;
import com.gsl.demo.measurespec.util.MyViewPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerActivity extends Activity {
    private ViewPager mViewPager;
    private LayoutInflater mInflater;
    private List<View> viewList = new ArrayList<>();
    private MyViewPagerAdapter myViewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pager);

        mViewPager = (ViewPager) findViewById(R.id.vp_my_viewpager);

        mInflater = getLayoutInflater();

        View layout1 = mInflater.inflate(R.layout.layout1, null);
        View layout2 = mInflater.inflate(R.layout.layout2, null);
        View layout3 = mInflater.inflate(R.layout.layout3, null);

        viewList.add(layout1);
        viewList.add(layout2);
        viewList.add(layout3);

        myViewPagerAdapter = new MyViewPagerAdapter(viewList);

        mViewPager.setAdapter(myViewPagerAdapter);

    }
}
