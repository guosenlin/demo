package com.gsl.demo.pendemo;

import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.graphics.Point;
import android.graphics.PointF;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

public class TestActivity extends Activity {
	private static final String TAG = "test";

	private RelativeLayout mRLMindGuide;
	private ImageView mIVOption01;
	private ImageView mIVOption02;
	private ImageView mIVOption03;
	private ImageView mIVOption04;

	private ImageView mOptionLocater;
	private int mOptionSrcLeft;
	private int mOptionSrcTop;

	private List<OptionPointF> mOptionDestList;
	private int mOptionDestRadius=100;

	private class OptionPointF extends PointF{
		private boolean used;

		public OptionPointF(float x, float y) {
			super(x, y);
		}

		public OptionPointF(float x, float y, boolean used) {
			super(x, y);
			this.used = used;
		}

		public OptionPointF(Point p, boolean used) {
			super(p);
			this.used = used;
		}

		public boolean isUsed() {
			return used;
		}

		public void setUsed(boolean used) {
			this.used = used;
		}
	}


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_test);

		mRLMindGuide = (RelativeLayout) findViewById(R.id.mind_guide_canvas);
		mRLMindGuide.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				Log.d(TAG, "MindGuide x:"+event.getX()+", y:"+event.getY());
				return false;
			}
		});
		mOptionLocater = (ImageView) findViewById(R.id.option_locater);
		mOptionLocater.setClickable(true);
		mOptionLocater.bringToFront();
		mOptionLocater.setOnTouchListener(optionLocatorListener);

		mIVOption01 = (ImageView) findViewById(R.id.op_01);
		mIVOption02 = (ImageView) findViewById(R.id.op_02);
		mIVOption03 = (ImageView) findViewById(R.id.op_03);
		mIVOption04 = (ImageView) findViewById(R.id.op_04);

		mIVOption01.setOnTouchListener(optionListener);
		mIVOption02.setOnTouchListener(optionListener);
		mIVOption03.setOnTouchListener(optionListener);
		mIVOption04.setOnTouchListener(optionListener);

		initOptionDest();
	}

	private void initOptionDest(){
		mOptionDestList = new ArrayList<>();
		mOptionDestList.add(new OptionPointF(432.40f, 29.04f));
		mOptionDestList.add(new OptionPointF(432.40f, 163.22f));
		mOptionDestList.add(new OptionPointF(432.40f, 305.39f));
		mOptionDestList.add(new OptionPointF(432.40f, 439.57f));

	}

	private OptionPointF getNearestOptionDest(PointF p){
		return getNearestOptionDest(p, false);
	}
	private OptionPointF getNearestOptionDest(PointF p, boolean allowUsed){
		Log.e(TAG, "option x:"+p.x+", y:"+p.y);
		OptionPointF dest = null;
		if(mOptionDestList.size()>0) {
			double radius = Math.pow(DensityUtil.dip2px(this,mOptionDestRadius), 2);
			double min = Math.pow(mRLMindGuide.getWidth(), 2) + Math.pow(mRLMindGuide.getHeight(), 2);
			Log.e(TAG, "init radius:"+radius+", min:"+min);
			for (OptionPointF pf : mOptionDestList) {
				if(allowUsed || !pf.isUsed()) {
					double distance = Math.pow((pf.x - p.x), 2) + Math.pow((pf.y - p.y), 2);
					if (distance < min) {
						min = distance;
						if (min < radius) {
							dest = pf;
						}
					}
				}
			}
		}

		return dest;
	}

	private View.OnTouchListener answerListener = new View.OnTouchListener() {
		float lastX;
		float lastY;
		PointF lastPoint;
		@Override
		public boolean onTouch(final View v, MotionEvent event) {
			ViewGroup.MarginLayoutParams mlp = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
			switch (event.getAction()){
				case MotionEvent.ACTION_DOWN:
					Log.d(TAG, "answerListener ACTION_DOWN");

					lastX = event.getRawX();
					lastY = event.getRawY();
					lastPoint = new PointF(mlp.leftMargin, mlp.topMargin);
					v.bringToFront();
					break;
				case MotionEvent.ACTION_MOVE:
					//Log.d(TAG, "ACTION_MOVE");
					float currX = event.getRawX();
					float currY = event.getRawY();

					int dx = (int) (currX-lastX);
					int dy = (int) (currY-lastY);
					if(Math.abs(dx)>0||Math.abs(dy)>0) {
						mlp.leftMargin = v.getLeft() + dx;
						mlp.topMargin = v.getTop() + dy;
						v.setLayoutParams(mlp);

					}
					lastX = currX;
					lastY = currY;
					break;
				case MotionEvent.ACTION_UP:
					//Log.e(TAG, "ACTION_UP");

					PointF moveFrom = new PointF(mlp.leftMargin, mlp.topMargin);
					final OptionPointF dest = getNearestOptionDest(moveFrom, true);
					PointF moveTo=null;
					if(null != dest){
						if(!dest.isUsed()) {
							Log.e(TAG, "find unused dest");
							dest.setUsed(true);
							moveTo = new PointF(dest.x, dest.y);
							moveTo.y -= mOptionLocater.getHeight() / 2;

							OptionPointF p = getNearestOptionDest(lastPoint, true);
							p.setUsed(false);
						}else{
							Log.e(TAG, "find used dest");

							//viewMoving(v, moveFrom, moveTo);
						}
					}else{
						moveTo = lastPoint;
					}

					viewMoving(v, moveFrom, moveTo);

					break;
			}

			return false;
		}
	};

	private void viewMoving(final View v, PointF moveFrom, PointF moveTo){
		ValueAnimator animator = new ValueAnimator();
		animator.setDuration(200);
		animator.setObjectValues(moveFrom, moveTo);
		animator.setInterpolator(new LinearInterpolator());
		animator.setEvaluator(new TypeEvaluator<PointF>() {
			@Override
			public PointF evaluate(float fraction, PointF startValue, PointF endValue) {
				float x = startValue.x + (endValue.x - startValue.x) * fraction;
				float y = startValue.y + (endValue.y - startValue.y) * fraction;
				return new PointF(x, y);
			}
		});
		animator.start();
		animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

			@Override
			public void onAnimationUpdate(ValueAnimator animation) {
				ViewGroup.MarginLayoutParams mlp = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
				PointF p = (PointF) animation.getAnimatedValue();
				mlp.leftMargin = (int) p.x;
				mlp.topMargin = (int) p.y;
				v.setLayoutParams(mlp);
			}
		});
	}

	private View.OnTouchListener optionLocatorListener = new View.OnTouchListener() {
		float lastX;
		float lastY;
		@Override
		public boolean onTouch(final View v, MotionEvent event) {
			ViewGroup.MarginLayoutParams mlp = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
			switch (event.getAction()){
				case MotionEvent.ACTION_DOWN:
					Log.d(TAG, "optionLocatorListener ACTION_DOWN");

					lastX = event.getRawX();
					lastY = event.getRawY();
					break;
				case MotionEvent.ACTION_MOVE:
					//Log.d(TAG, "ACTION_MOVE");
					float currX = event.getRawX();
					float currY = event.getRawY();

					int dx = (int) (currX-lastX);
					int dy = (int) (currY-lastY);
					if(Math.abs(dx)>0||Math.abs(dy)>0) {
						mlp.leftMargin = v.getLeft() + dx;
						mlp.topMargin = v.getTop() + dy;
						v.setLayoutParams(mlp);

					}
					lastX = currX;
					lastY = currY;
					break;
				case MotionEvent.ACTION_UP:
					//Log.e(TAG, "ACTION_UP");

					PointF moveFrom = new PointF(mlp.leftMargin, mlp.topMargin);
					OptionPointF dest = getNearestOptionDest(moveFrom);
					PointF moveTo=null;
					boolean findDest = false;
					if(null != dest){
						findDest = true;
						dest.setUsed(true);
						moveTo = new PointF(dest.x, dest.y);
					}

					long duration = 100;
					if(findDest){
						Log.e(TAG, "find dest");
						moveTo.y -= mOptionLocater.getHeight()/2;
					}else {
						duration = 500;
						moveTo = new PointF(mOptionSrcLeft, mOptionSrcTop);
					}

					viewMoving(v, moveFrom, moveTo);

					if(findDest){
						ImageView newIv = new ImageView(TestActivity.this);
						newIv.setBackground(mOptionLocater.getBackground());
						mRLMindGuide.addView(newIv);

						ViewGroup.MarginLayoutParams newlp = (ViewGroup.MarginLayoutParams) newIv.getLayoutParams();
						newlp.leftMargin = (int) moveTo.x;
						newlp.topMargin = (int) moveTo.y;
						newIv.setLayoutParams(newlp);
						newIv.setClickable(true);
						newIv.setOnTouchListener(answerListener);

						mOptionLocater.setVisibility(View.GONE);
					}
					break;
			}

			return false;
		}
	};

	private View.OnTouchListener optionListener = new View.OnTouchListener() {
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			switch (event.getAction()){
				case MotionEvent.ACTION_DOWN:
					Log.d(TAG, "optionListener ACTION_DOWN");
					ViewGroup vg = (ViewGroup) v.getParent();

					ViewGroup.MarginLayoutParams locatorlp = (ViewGroup.MarginLayoutParams) mOptionLocater.getLayoutParams();
					locatorlp.leftMargin = vg.getLeft() + v.getLeft();
					locatorlp.topMargin = vg.getTop() + v.getTop();

					mOptionSrcLeft = locatorlp.leftMargin;
					mOptionSrcTop = locatorlp.topMargin;

					mOptionLocater.setLayoutParams(locatorlp);

					mOptionLocater.setBackground(v.getBackground());
					mOptionLocater.setVisibility(View.VISIBLE);
					break;
			}
			mOptionLocater.dispatchTouchEvent(event);
			return false;
		}
	};

}
