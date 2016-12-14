package com.gsl.demo.pendemo;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class TestActivity extends Activity {
	private static final String TAG = "test";

	private RelativeLayout mRLMindGuide;
	private ImageView mIVOption01;
	private ImageView mIVOption02;
	private ImageView mIVOption03;
	private ImageView mIVOption04;

	private ImageView mOptionLocater;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_test);

		mRLMindGuide = (RelativeLayout) findViewById(R.id.mind_guide_canvas);
		mOptionLocater = (ImageView) findViewById(R.id.option_locater);

		mIVOption01 = (ImageView) findViewById(R.id.op_01);
		mIVOption02 = (ImageView) findViewById(R.id.op_02);
		mIVOption03 = (ImageView) findViewById(R.id.op_03);
		mIVOption04 = (ImageView) findViewById(R.id.op_04);

		mIVOption01.setOnTouchListener(optionListener);
		mIVOption02.setOnTouchListener(optionListener);
		mIVOption03.setOnTouchListener(optionListener);
		mIVOption04.setOnTouchListener(optionListener);
	}

	private View.OnTouchListener optionListener = new View.OnTouchListener() {
		float lastX;
		float lastY;
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			switch (event.getAction()){
				case MotionEvent.ACTION_DOWN:
					lastX = event.getX();
					lastY = event.getY();
					break;
				case MotionEvent.ACTION_MOVE:
					float dx = event.getX() - lastX;
					float dy = event.getY() - lastY;
					Log.d(TAG, "dx:"+dx+",dy:"+dy);
					if(Math.abs(dx)>10||Math.abs(dy)>10) {
						move(v, dx, dy);
					}

					lastX = event.getX();
					lastY = event.getY();
					break;
				case MotionEvent.ACTION_UP:

					break;
			}

			Log.d(TAG, "lastX:"+lastX+",lastY:"+lastY);

			return false;
		}
	};

	private void move(View v, float dx, float dy){
		Log.d(TAG, "move dx:"+dx+",dy:"+dy);
		float fromX = v.getTranslationX();
		ObjectAnimator animatorX = ObjectAnimator.ofFloat(v, "translationX", fromX, fromX+dx);
		animatorX.setDuration(200);
		animatorX.start();

		float fromY = v.getTranslationY();
		ObjectAnimator animatorY = ObjectAnimator.ofFloat(v, "translationY", fromY, fromY+dy);
		animatorY.setDuration(200);
		animatorY.start();
	}

}
