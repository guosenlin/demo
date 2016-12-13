package com.gsl.demo.measurespec.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Scroller;

/**
 * Created by guosenlin on 16-10-17.
 */

public class MyCustomViewGroup extends ViewGroup {
    private static final String TAG = "MyCustomViewGroup";
    private int mScreenHeight;

    private int mLastY;
    private Scroller mScroller;
    private float mStart;
    private float mEnd;

    public MyCustomViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        mScreenHeight = 2048;
        mScroller = new Scroller(context);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childCounts = getChildCount();
        LayoutParams layoutParams = getLayoutParams();
        layoutParams.height = mScreenHeight * childCounts;

        setLayoutParams(layoutParams);
        for(int i=0; i<childCounts; i++){
            View childView = getChildAt(i);
            if(childView.getVisibility()!=View.GONE){
                childView.layout(
                        l,
                        i*mScreenHeight,
                        r,
                        (i+1)*mScreenHeight
                );
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int y = (int) event.getY();

        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                mStart = getScrollY();
                mLastY = y;
                break;
            case MotionEvent.ACTION_MOVE:
                if(!mScroller.isFinished()){
                    mScroller.abortAnimation();
                }
                int dy = y - mLastY;
                if(getScrollY()<0){
                    dy = 0;
                }
                if(getScrollY()>getHeight() - mScreenHeight){
                    dy = 0;
                }

                scrollBy(0, dy);
                mLastY = y;
                break;
            case MotionEvent.ACTION_UP:
                mEnd = getScrollY();
                int dScrollY = (int) (mEnd - mStart);
                if(dScrollY>0){
                    if(dScrollY<mScreenHeight/4){
                        mScroller.startScroll(0, getScrollY(), 0, -dScrollY);
                    }else{
                        mScroller.startScroll(0, getScrollY(), 0, mScreenHeight - dScrollY);
                    }
                }else{
                    if(-dScrollY<mScreenHeight/4){
                        mScroller.startScroll(0, getScrollY(), 0, -dScrollY);
                    }else{
                        mScroller.startScroll(0, getScrollY(), 0, -mScreenHeight - dScrollY);
                    }
                }
                break;
            default:
        }

        postInvalidate();
        return true;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int childCounts = getChildCount();
        for(int i=0; i<childCounts; i++){
            measureChild(getChildAt(i), widthMeasureSpec, heightMeasureSpec);
        }
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        //Call this when you want to know the new location. If it returns true, the animation is not yet finished.
        if(mScroller.computeScrollOffset()){
            scrollTo(0, mScroller.getCurrY());
            postInvalidate();
        }
    }
}
