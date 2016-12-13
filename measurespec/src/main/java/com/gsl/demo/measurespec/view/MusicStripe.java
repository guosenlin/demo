package com.gsl.demo.measurespec.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by guosenlin on 16-10-15.
 */

public class MusicStripe extends View {
    private static final String TAG = "MusicStripe";
    private Paint mPaint;
    private int stripeCounts = 10;
    private float mOffset = 5;
    private float mWidth;
    private float mRectWidth;
    private float mRectHeight;

    private static final int MINLENGTH=200;

    public MusicStripe(Context context, AttributeSet attrs) {
        super(context, attrs);

        mPaint = new Paint();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        for(int i=0; i<stripeCounts; i++){
            float currentHeight = (float) (Math.random()* mRectHeight);
            canvas.drawRect(
                    (float) (0.4*mWidth/2 + i*mRectWidth + mOffset),
                    currentHeight,
                    (float) (0.4*mWidth/2 + (i+1)*mRectWidth),
                    mRectHeight,
                    mPaint
            );
        }

        postInvalidateDelayed(300);

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mWidth = getWidth();
        mRectHeight = getHeight();
        mRectWidth = (float) (0.6*mWidth/stripeCounts);
        LinearGradient linearGradient = new LinearGradient(
                0,
                0,
                mRectWidth,
                mRectHeight,
                Color.YELLOW,
                Color.BLUE,
                Shader.TileMode.CLAMP
        );

        mPaint.setShader(linearGradient);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(
                measureWidth(widthMeasureSpec),
                measureHeight(heightMeasureSpec)
        );
    }


    private int measureWidth(int widthMeasureSpec){
        int model = View.MeasureSpec.getMode(widthMeasureSpec);

        //a max size that parent view can provide to child view
        int size = View.MeasureSpec.getSize(widthMeasureSpec);

        if(model== View.MeasureSpec.EXACTLY){
            Log.d(TAG, "EXACTLY");
            return size;
        }else{
            int result=MINLENGTH;

            if(model== View.MeasureSpec.AT_MOST){
                Log.d(TAG, "AT_MOST");
                result = Math.min(result, size);
            }else{
                Log.d(TAG, "UNSPECIFIED");
            }

            return result;
        }
    }

    private int measureHeight(int heightMeasureSpec){
        int model = View.MeasureSpec.getMode(heightMeasureSpec);

        //a max size that parent view can provide to child view
        int size = View.MeasureSpec.getSize(heightMeasureSpec);

        if(model== View.MeasureSpec.EXACTLY){
            Log.d(TAG, "EXACTLY");
            return size;
        }else{
            int result=MINLENGTH;

            if(model== View.MeasureSpec.AT_MOST){
                Log.d(TAG, "AT_MOST");
                result = Math.min(result, size);
            }else{
                Log.d(TAG, "UNSPECIFIED");
            }

            return result;
        }
    }
}
