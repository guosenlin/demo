package com.gsl.demo.pendemo;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class TestActivity extends Activity implements View.OnClickListener {
	private static final String TAG = "test";

	private RelativeLayout mRLMindGuide;
	private ImageView mIVOption01;
	private ImageView mIVOption02;
	private ImageView mIVOption03;
	private ImageView mIVOption04;
	private ImageView mDestView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_test);

		mRLMindGuide = (RelativeLayout) findViewById(R.id.mind_guide_canvas);
		mIVOption01 = (ImageView) findViewById(R.id.op_01);
		mIVOption02 = (ImageView) findViewById(R.id.op_02);
		mIVOption03 = (ImageView) findViewById(R.id.op_03);
		mIVOption04 = (ImageView) findViewById(R.id.op_04);

		mIVOption01.setOnClickListener(this);
		mIVOption01.setOnLongClickListener(new View.OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				RelativeLayout.LayoutParams vlp = (RelativeLayout.LayoutParams)v.getLayoutParams();
				ImageView iv = new ImageView(TestActivity.this);
				iv.setBackground(v.getBackground());
				iv.setLayoutParams(new RelativeLayout.LayoutParams(vlp));

				iv.setOnDragListener(new OptionViewOnDragListener());
				mRLMindGuide.addView(iv);

				return false;
			}
		});

		mIVOption02.setOnClickListener(this);
		mIVOption03.setOnClickListener(this);
		mIVOption04.setOnClickListener(this);

		mRLMindGuide.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				Log.d(TAG, "v:"+v);
				int x = (int) event.getX();
				int y = (int) event.getY();
				Log.d(TAG, x+","+y);

/*				int w = DensityUtil.dip2px(TestActivity.this, 120);
				int h = DensityUtil.dip2px(TestActivity.this, 65);

				ImageView view = new ImageView(TestActivity.this);
				view.setBackgroundColor(Color.RED);
				RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(w, h);
				lp.leftMargin = x;
				lp.topMargin = y;
				view.setLayoutParams(lp);

				mRLMindGuide.addView(view);*/
				return false;
			}
		});

		initDestImageView();
	}

	private void initDestImageView(){
		int w = DensityUtil.dip2px(TestActivity.this, 280);
		int h = DensityUtil.dip2px(TestActivity.this, 100);

		ImageView view = new ImageView(this);
		RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(w, h);
		lp.leftMargin = 575;
		lp.topMargin = 1;
		view.setLayoutParams(lp);
		view.setOnClickListener(destViewOnLickListener);
		mRLMindGuide.addView(view);

		view = new ImageView(this);
		lp = new RelativeLayout.LayoutParams(w, h);
		lp.leftMargin = 575;
		lp.topMargin = 200;
		view.setLayoutParams(lp);
		view.setOnClickListener(destViewOnLickListener);
		mRLMindGuide.addView(view);

		view = new ImageView(this);
		lp = new RelativeLayout.LayoutParams(w, h);
		lp.leftMargin = 575;
		lp.topMargin = 400;
		view.setLayoutParams(lp);
		view.setOnClickListener(destViewOnLickListener);
		mRLMindGuide.addView(view);

		view = new ImageView(this);
		lp = new RelativeLayout.LayoutParams(w, h);
		lp.leftMargin = 575;
		lp.topMargin = 600;
		view.setLayoutParams(lp);
		view.setOnClickListener(destViewOnLickListener);
		mRLMindGuide.addView(view);
	}

	private View.OnClickListener destViewOnLickListener = new View.OnClickListener(){
		@Override
		public void onClick(View v) {
			Log.d(TAG, "click dest");
			mDestView = (ImageView) v;
		}
	};

	@Override
	public void onClick(View v) {
		Log.d(TAG, "click option");
		if(null!=mDestView) {
			Log.d(TAG, "set dest view");
			ImageView iv = (ImageView) v;
			mDestView.setBackground(iv.getBackground());
		}
	}

	private class OptionViewOnDragListener implements View.OnDragListener{
		@Override
		public boolean onDrag(View v, DragEvent event) {

			return false;
		}
	}

}
