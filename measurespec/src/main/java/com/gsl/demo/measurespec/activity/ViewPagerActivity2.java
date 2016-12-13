package com.gsl.demo.measurespec.activity;

import android.animation.ObjectAnimator;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gsl.demo.measurespec.R;
import com.gsl.demo.measurespec.fragment.Fragment1;
import com.gsl.demo.measurespec.fragment.Fragment2;
import com.gsl.demo.measurespec.fragment.Fragment3;
import com.gsl.demo.measurespec.util.MyFragmentPagerAdapter;
import com.gsl.demo.measurespec.util.MyUtil;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerActivity2 extends FragmentActivity implements View.OnClickListener {
    private ViewPager mViewPager;
    private TextView tvTab1;
    private TextView tvTab2;
    private TextView tvTab3;
    private ImageView ivTabLine;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pager2);

        tvTab1 = (TextView) findViewById(R.id.tv_tab1);
        tvTab2 = (TextView) findViewById(R.id.tv_tab2);
        tvTab3 = (TextView) findViewById(R.id.tv_tab3);
        ivTabLine = (ImageView)findViewById(R.id.iv_tab_line);

        ViewGroup.LayoutParams lp = ivTabLine.getLayoutParams();
        lp.width = MyUtil.getScreenWidth(getWindowManager()) / 3;
        ivTabLine.setLayoutParams(lp);

        tvTab1.setOnClickListener(this);
        tvTab2.setOnClickListener(this);
        tvTab3.setOnClickListener(this);

        List<Fragment> fragmentList = new ArrayList<>();
        Fragment ft1 = new Fragment1();
        Fragment ft2 = new Fragment2();
        Fragment ft3 = new Fragment3();

        fragmentList.add(ft1);
        fragmentList.add(ft2);
        fragmentList.add(ft3);


        MyFragmentPagerAdapter fpAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager(), fragmentList);


        mViewPager = (ViewPager) findViewById(R.id.vp_my_viewpager2);
        mViewPager.setAdapter(fpAdapter);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            long mLastTime;
            int mLastOffset;
            boolean isScrolling;
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                //Log.d("PageScrolled", "-------------------------->>>>"+positionOffsetPixels);

                long currTime = System.currentTimeMillis();

                Log.d("PageChange", "-------------------------->>>>time offset:"+(currTime - mLastTime));

                if(positionOffset==0 && isScrolling) {

                    //Log.d("PageScrolled", "-------------------------->>>>position offset:"+(positionOffsetPixels - mLastOffset));
                    /*if(Math.abs(positionOffsetPixels - mLastOffset) > 90){
                        if(currTime - mLastTime > 500) {
                            //Log.d("PageScrolled", "-------------------------->>>>time offset:"+(currTime - mLastTime));
                            moveTabLineTranslationX(position, positionOffsetPixels / 3);
                        }
                    }*/
                }

                mLastTime = currTime;
                mLastOffset = positionOffsetPixels;
            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        tvTab1.setTextColor(Color.RED);
                        tvTab2.setTextColor(Color.BLACK);
                        tvTab3.setTextColor(Color.BLACK);
                        moveTabLineTranslationX(0, 0);
                        break;
                    case 1:
                        tvTab1.setTextColor(Color.BLACK);
                        tvTab2.setTextColor(Color.RED);
                        tvTab3.setTextColor(Color.BLACK);
                        moveTabLineTranslationX(1, 0);
                        break;
                    case 2:
                        tvTab1.setTextColor(Color.BLACK);
                        tvTab2.setTextColor(Color.BLACK);
                        tvTab3.setTextColor(Color.RED);
                        moveTabLineTranslationX(2, 0);
                        break;
                    default:
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                switch (state){
                    case ViewPager.SCROLL_STATE_IDLE:
                        Log.d("PageChange", "SCROLL_STATE_IDLE");
                        isScrolling = false;
                        break;
                    case ViewPager.SCROLL_STATE_DRAGGING:
                        Log.d("PageChange", "SCROLL_STATE_DRAGGING");
                        isScrolling = true;
                        break;
                    case ViewPager.SCROLL_STATE_SETTLING:
                        Log.d("PageChange", "SCROLL_STATE_SETTLING");
                        isScrolling = true;
                        break;
                }
            }
        });

    }

    private void setTabLineTranslationX(int toPosition, float translationXOffset){
        float toTranslationX = ivTabLine.getWidth() * toPosition + translationXOffset;

        ivTabLine.setTranslationX(toTranslationX);
    }

    private void moveTabLineTranslationX(int toPosition, float translationXOffset){
        float currTranslationX = ivTabLine.getTranslationX();
        float toTranslationX = ivTabLine.getWidth() * toPosition + translationXOffset;

        ObjectAnimator animator = ObjectAnimator.ofFloat(ivTabLine, "translationX", currTranslationX, toTranslationX);
        if(animator.isRunning())

        animator.start();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_tab1:
                mViewPager.setCurrentItem(0);
                break;
            case R.id.tv_tab2:
                mViewPager.setCurrentItem(1);
                break;
            case R.id.tv_tab3:
                mViewPager.setCurrentItem(2);
                break;
            default:
        }
    }
}
