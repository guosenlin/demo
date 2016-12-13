package com.gsl.demo.measurespec.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gsl.demo.measurespec.R;

/**
 * Created by guosenlin on 16-10-13.
 */

public class TopBar extends RelativeLayout {
    private String title;
    private float titleSize;
    private int titleColor;
    private String leftText;
    private int leftTextColor;
    private Drawable leftBackground;
    private String rightText;
    private int rightTextColor;
    private Drawable rightBackground;

    private LayoutParams leftParams;
    private LayoutParams rightParams;
    private LayoutParams titleParams;

    private Button leftBt;
    private Button rightBt;
    private TextView titleText;

    public TopbarClickListener topbarClickListener;

    public static final int BUTTON_LEFT = 0;
    public static final int BUTTON_RIGHT = 1;

    public TopBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.TopBar);
        title = ta.getString(R.styleable.TopBar_titleText);
        titleColor = ta.getColor(R.styleable.TopBar_titleColor, 0);
        titleSize = ta.getDimension(R.styleable.TopBar_titleSize, 12);
        leftText = ta.getString(R.styleable.TopBar_leftText);
        leftTextColor = ta.getColor(R.styleable.TopBar_leftTextColor, 0);
        leftBackground = ta.getDrawable(R.styleable.TopBar_leftBackground);
        rightText = ta.getString(R.styleable.TopBar_rightText);
        rightTextColor = ta.getColor(R.styleable.TopBar_rightTextColor, 0);
        rightBackground = ta.getDrawable(R.styleable.TopBar_rightBackground);

        ta.recycle();

        leftBt = new Button(context);
        rightBt = new Button(context);
        titleText = new TextView(context);

        Typeface iconfont = Typeface.createFromAsset(context.getAssets(), "iconfont/iconfont.ttf");

        titleText.setText(title);
        titleText.setTextSize(titleSize);
        titleText.setTextColor(titleColor);
        titleText.setGravity(Gravity.CENTER);

        leftBt.setText(leftText);
        leftBt.setTextColor(leftTextColor);
        leftBt.setBackground(leftBackground);
        leftBt.setTextSize(titleSize);
        leftBt.setTypeface(iconfont);

        rightBt.setText(rightText);
        rightBt.setTextColor(rightTextColor);
        rightBt.setBackground(rightBackground);
        rightBt.setTextSize(titleSize);
        rightBt.setTypeface(iconfont);

        leftParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
        leftParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT, TRUE);
        addView(leftBt, leftParams);

        rightParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
        rightParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, TRUE);
        addView(rightBt, rightParams);

        titleParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
        titleParams.addRule(RelativeLayout.CENTER_IN_PARENT, TRUE);
        addView(titleText, titleParams);

        leftBt.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(null!=topbarClickListener){
                    topbarClickListener.onLeftClick(view);
                }
            }
        });
        rightBt.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(null!=topbarClickListener){
                    topbarClickListener.onRightClick(view);
                }
            }
        });
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }


    public void setTopbarClickListener(TopbarClickListener topbarClickListener){
        this.topbarClickListener = topbarClickListener;
    }

    public void setButtonVisible(int button, boolean flag){
        if(button==BUTTON_LEFT){
            leftBt.setVisibility(flag?View.VISIBLE:View.GONE);
        }else if(button==BUTTON_RIGHT){
            rightBt.setVisibility(flag?View.VISIBLE:View.GONE);
        }
    }

    public interface TopbarClickListener {
        void onLeftClick(View v);

        void onRightClick(View v);
    }
}
