package com.gsl.demo.measurespec.util;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gsl.demo.measurespec.R;

import java.util.List;

/**
 * Created by guosenlin on 16-10-24.
 */

public class MyViewPagerAdapter extends PagerAdapter {
    private List<View> viewList;

    public MyViewPagerAdapter(List<View> data) {
        this.viewList = data;
    }

    @Override
    public int getCount() {
        return viewList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(viewList.get(position));
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = viewList.get(position);
        container.addView(view);
        return view;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        ViewGroup view = (ViewGroup) viewList.get(position);
        TextView tv = (TextView)view.getChildAt(0);
        return tv.getText();
    }
}
