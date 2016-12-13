package com.gsl.demo.measurespec.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Scroller;

import com.gsl.demo.measurespec.R;

/**
 * Created by guosenlin on 16-10-13.
 */

public class MyCustomView extends View {

    private final static String TAG = "MyCustomView";
    private String showText;
    private float showTextSize;
    private int showTextColor;
    private int innerCircleColor;
    private int outerCircleColor;
    private float outerCirclePercent;

    private float circleXY;
    private int length;
    private static final int MINLENGTH=200;

    private float currentPercent;

    private Paint textPaint;
    private Paint circlePaint;
    private Paint archPaint;
    private RectF archRectF;

    private Handler hander = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what==2){
                Log.d(TAG, "handleMessage");
                invalidate();
            }
        }
    };

    private Scroller mScroller;
    private int mLastX;
    private int mLastY;

    public MyCustomView(Context context, AttributeSet attrs){
        super(context, attrs);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.MyCustomView);
        showText = ta.getString(R.styleable.MyCustomView_showTitleText);
        showTextColor = ta.getColor(R.styleable.MyCustomView_showTextColor, 0);
        showTextSize = ta.getDimension(R.styleable.MyCustomView_showTextSize, 12);
        innerCircleColor = ta.getColor(R.styleable.MyCustomView_innerCircleColor, 0);
        outerCircleColor = ta.getColor(R.styleable.MyCustomView_outerCircleColor, 0);
        outerCirclePercent = ta.getFloat(R.styleable.MyCustomView_outerCirclePercent, 50);

        ta.recycle();

        mScroller = new Scroller(context);
    }

    public class OuterCirclePercenter extends AsyncTask<Void, Float, Void>{

        @Override
        protected Void doInBackground(Void... voids) {
            while(currentPercent<=outerCirclePercent){
                try {
                    Thread.sleep(100);
                    currentPercent += 5;
                    publishProgress();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Float... values) {
            Message msg = new Message();
            msg.what = 2;
            hander.sendMessage(msg);
        }
    }

    public void startDrawPercent(){
        Log.d(TAG, "startDrawPercent");
        new OuterCirclePercenter().execute();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        length = getMeasuredWidth();

        textPaint = new Paint();
        textPaint.setColor(showTextColor);
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setTextSize(showTextSize);
        textPaint.setTextAlign(Paint.Align.CENTER);

        circlePaint = new Paint();
        circlePaint.setColor(innerCircleColor);
        circlePaint.setStyle(Paint.Style.FILL);

        archPaint = new Paint();
        archPaint.setColor(outerCircleColor);
        archPaint.setStyle(Paint.Style.STROKE);
        archPaint.setStrokeWidth((float) (length*0.5/4));


        circleXY = length/2;


        archRectF  = new RectF(
                (float) (length*0.1),
                (float) (length*0.1),
                (float) (length*0.9),
                (float) (length*0.9)
        );

        float sweepAngle = (float) (360*currentPercent*0.01);
        canvas.drawArc(archRectF, 270, sweepAngle, false, archPaint);

        float radius = (float) (length*0.5/2);

        canvas.drawCircle(circleXY, circleXY, radius, circlePaint);
        canvas.drawText(showText, 0, showText.length(), circleXY, circleXY+(showTextSize/4), textPaint);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(
                measureWidth(widthMeasureSpec),
                measureHeight(heightMeasureSpec)
        );
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();

        View viewGroup = (View)getParent();
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                mLastX = x;
                mLastY = y;
                break;
            case MotionEvent.ACTION_MOVE:
                if(!mScroller.isFinished()){
                    mScroller.abortAnimation();
                }
                int dx = x - mLastX;
                int dy = y - mLastY;

                scrollBy(-dx, -dy);

                mLastX = x;
                mLastY = y;

                break;
            case MotionEvent.ACTION_UP:
                mScroller.startScroll(
                        getScrollX(), getScrollY(),
                        -getScrollX(), -getScrollY()
                );


                break;
            default:
        }
        invalidate();

        return true;
    }

    @Override
    public void computeScroll() {
        super.computeScroll();

        if(mScroller.computeScrollOffset()){
            scrollTo(
                    mScroller.getCurrX(),
                    mScroller.getCurrY()
            );
            invalidate();
        }
    }

    private int measureWidth(int widthMeasureSpec){
        int model = MeasureSpec.getMode(widthMeasureSpec);

        //a max size that parent view can provide to child view
        int size = MeasureSpec.getSize(widthMeasureSpec);

        if(model==MeasureSpec.EXACTLY){
            Log.d(TAG, "EXACTLY");
            return size;
        }else{
            int result=MINLENGTH;

            if(model==MeasureSpec.AT_MOST){
                Log.d(TAG, "AT_MOST");
                result = Math.min(result, size);
            }else{
                Log.d(TAG, "UNSPECIFIED");
            }

            return result;
        }
    }

    private int measureHeight(int heightMeasureSpec){
        int model = MeasureSpec.getMode(heightMeasureSpec);

        //a max size that parent view can provide to child view
        int size = MeasureSpec.getSize(heightMeasureSpec);

        if(model==MeasureSpec.EXACTLY){
            Log.d(TAG, "EXACTLY");
            return size;
        }else{
            int result=MINLENGTH;

            if(model==MeasureSpec.AT_MOST){
                Log.d(TAG, "AT_MOST");
                result = Math.min(result, size);
            }else{
                Log.d(TAG, "UNSPECIFIED");
            }

            return result;
        }
    }
}
