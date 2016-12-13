package com.gsl.demo.measurespec.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;

/**
 * Created by guosenlin on 16-10-13.
 */

public class MyTextView extends TextView {

    private final static String TAG = "MyTextView";

    public MyTextView(Context context, AttributeSet attrs){
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Paint painter = new Paint();
        painter.setColor(getResources().getColor(android.R.color.holo_blue_light));
        painter.setStyle(Paint.Style.FILL);

        Paint painter1 = new Paint();
        painter1.setColor(Color.YELLOW);
        painter1.setStyle(Paint.Style.FILL);

        canvas.drawRect(
                0,
                0,
                getMeasuredWidth(),
                getMeasuredHeight(),
                painter
        );
        canvas.drawRect(
                10,
                10,
                getMeasuredWidth() - 10,
                getMeasuredHeight() - 10,
                painter1
        );
        canvas.save();
        canvas.translate(10, 0);

        super.onDraw(canvas);
        canvas.restore();

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(
                measureWidth(widthMeasureSpec),
                measureHeight(heightMeasureSpec)
        );
    }

    private int measureWidth(int widthMeasureSpec){
        int model = MeasureSpec.getMode(widthMeasureSpec);

        //a max size that parent view can provide to child view
        int size = MeasureSpec.getSize(widthMeasureSpec);

        if(model==MeasureSpec.EXACTLY){
            return size;
        }else{
            int result=200;

            if(model==MeasureSpec.AT_MOST){
                result = Math.min(result, size);
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
            int result=200;

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
