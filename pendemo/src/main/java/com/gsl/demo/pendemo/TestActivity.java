package com.gsl.demo.pendemo;

import android.animation.AnimatorSet;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.graphics.PointF;
import android.graphics.drawable.BitmapDrawable;
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

	private RelativeLayout mRLTopParent;
	private RelativeLayout mRLMindGuide;
	private ImageView mIVOption01;
	private ImageView mIVOption02;
	private ImageView mOptionLocater;
	private ImageView mIVOption03;

	private ImageView mIVOption04;
	private int mOptionSrcLeft;
	private int mOptionSrcTop;

	private List<OptionPointF> mOptionDestList;
	private int mOptionDestRadius=100;
	private int count;

	private class OptionPointF extends PointF{
		private ImageView view;

		public OptionPointF(float x, float y) {
			super(x, y);
		}

		public OptionPointF(float x, float y, ImageView view) {
			super(x, y);
			this.view = view;
		}

		public boolean isBind() {
			return view!=null;
		}

		public void bindImageView(ImageView view) {
			this.view = view;
		}

		public void removeBind(){
			this.view = null;
		}

		public ImageView getBind(){
			return this.view;
		}

		@Override
		public int hashCode() {
			return super.hashCode();
		}

		@Override
		public boolean equals(Object obj) {
			return super.equals(obj);
		}
	}


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_test);
		mRLTopParent = (RelativeLayout) findViewById(R.id.activity_test);

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

	private void logDestPoint(){
		for(TestActivity.OptionPointF pf : mOptionDestList){
			Log.e(TAG, pf+"--->"+(pf.isBind()?pf.getBind().getTag():"unbind"));
		}
	}

	private View.OnTouchListener createOnTouchListener() {
		return new View.OnTouchListener() {
			float lastX;
			float lastY;
			PointF lastPoint;

			@Override
			public boolean onTouch(final View v, MotionEvent event) {
				ViewGroup.MarginLayoutParams mlp = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
				switch (event.getAction()) {
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

						int dx = (int) (currX - lastX);
						int dy = (int) (currY - lastY);
						if (Math.abs(dx) > 0 || Math.abs(dy) > 0) {
							mlp.leftMargin = v.getLeft() + dx;
							mlp.topMargin = v.getTop() + dy;
							v.setLayoutParams(mlp);

						}
						lastX = currX;
						lastY = currY;
						break;
					case MotionEvent.ACTION_UP:
						//Log.e(TAG, "ACTION_UP");
						if (lastPoint == null) {
							Log.e(TAG, "lastPoint is null");
						}

						PointF moveFrom = new PointF(mlp.leftMargin, mlp.topMargin);
						final OptionPointF dest = getNearestOptionDest(moveFrom, true);
						long duration = 200;

						if (null != dest) {
							PointF moveTo = new PointF(dest.x, dest.y);
							moveTo.y -= v.getHeight() / 2;

							OptionPointF srcPoint = getPointByBindView((ImageView) v);
							if (!dest.isBind()) {
								Log.e(TAG, "find unbind dest");

								if(null!=srcPoint) {
									srcPoint.removeBind();
								}else{
									Log.e(TAG, "cannot find src point");
								}
								dest.bindImageView((ImageView) v);
								viewMoving(v, moveFrom, moveTo, duration);
							} else {
								Log.e(TAG, "find bind dest");

								ImageView fromDest = dest.getBind();
								dest.bindImageView((ImageView) v);
								srcPoint.bindImageView(fromDest);
								viewAllMoving(
										createViewMoving(fromDest, dest, lastPoint, duration),
										createViewMoving(v, moveFrom, moveTo, duration)
								);
							}

						} else {
							viewMoving(v, moveFrom, lastPoint, duration);
						}


						break;
				}

				//logDestPoint();
				return false;
			}

		};
	}

	private TestActivity.OptionPointF getPointByBindView(ImageView view){
		for(TestActivity.OptionPointF pf : mOptionDestList){
			if(pf.isBind() && pf.getBind().equals(view))
				return pf;
		}
		return null;
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
			//Log.e(TAG, "init radius:"+radius+", min:"+min);
			for (OptionPointF pf : mOptionDestList) {
				if(allowUsed || !pf.isBind()) {
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

	private void viewMoving(View v, PointF moveFrom, PointF moveTo, long duration){
		createViewMoving(v, moveFrom, moveTo, duration).start();
	}

	private void viewAllMoving(ValueAnimator... animators){
		if(animators.length>0) {
			AnimatorSet animatorSet = new AnimatorSet();
			AnimatorSet.Builder builder = animatorSet.play(animators[0]);
			for(int i=1; i<animators.length; i++){
				builder.with(animators[i]);
			}
			animatorSet.start();
		}
	}

	private ValueAnimator createViewMoving(final View v, PointF moveFrom, PointF moveTo, long duration){
		Log.e(TAG, "move from:"+moveFrom+", to:"+moveTo+" "+duration);
		ValueAnimator animator = new ValueAnimator();
		animator.setDuration(duration);
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
		//animator.start();
		animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

			@Override
			public void onAnimationUpdate(ValueAnimator animation) {
				ViewGroup.MarginLayoutParams mlp = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
				PointF p = (PointF) animation.getAnimatedValue();
				mlp.leftMargin = (int) p.x;
				mlp.topMargin = (int) p.y;
				//Log.d(TAG, "move id="+v.getId()+" leftMargin:"+mlp.leftMargin+", topMargin:"+mlp.topMargin);
				v.setLayoutParams(mlp);
			}
		});

		return animator;
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
					v.bringToFront();
					break;
				case MotionEvent.ACTION_MOVE:
					Log.d(TAG, "ACTION_MOVE");
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
					Log.d(TAG, "ACTION_UP");

					PointF moveFrom = new PointF(mlp.leftMargin, mlp.topMargin);
					OptionPointF dest = getNearestOptionDest(moveFrom, true);
					PointF moveTo;
					boolean findDest;
					long duration = 100;

					if (findDest = (null != dest)) {
						moveTo = new PointF(dest.x, dest.y);
						moveTo.y -= v.getHeight() / 2;
					} else {
						duration = 500;
						moveTo = new PointF(mOptionSrcLeft, mOptionSrcTop);
					}
					if(!moveFrom.equals(moveTo)) {
						viewMoving(v, moveFrom, moveTo, duration);
					}else{
						Log.e(TAG, "from eq to");
					}

					if(findDest){
						Log.e(TAG, "find dest");
						if(dest.isBind()){
							dest.getBind().setImageBitmap(((BitmapDrawable) mOptionLocater.getBackground()).getBitmap());
						}else {
							Log.e(TAG, "add new imageView");
							ImageView newIv = new ImageView(TestActivity.this);
							newIv.setImageBitmap(((BitmapDrawable) mOptionLocater.getBackground()).getBitmap());
							mRLTopParent.addView(newIv);

							ViewGroup.MarginLayoutParams newlp = (ViewGroup.MarginLayoutParams) newIv.getLayoutParams();
							newlp.leftMargin = (int) moveTo.x;
							newlp.topMargin = (int) moveTo.y;
							newIv.setLayoutParams(newlp);
							newIv.setClickable(true);
							newIv.setOnTouchListener(createOnTouchListener());
							newIv.setTag("No."+(count++));
							dest.bindImageView(newIv);
						}

						mOptionLocater.setVisibility(View.GONE);
					}
					break;
			}

			//logDestPoint();
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
				case MotionEvent.ACTION_MOVE:
					//Log.d(TAG, "optionListener ACTION_MOVE");
					break;
				case MotionEvent.ACTION_UP:
					Log.d(TAG, "optionListener ACTION_UP");
					break;
			}
			mOptionLocater.dispatchTouchEvent(event);
			return false;
		}
	};

}
