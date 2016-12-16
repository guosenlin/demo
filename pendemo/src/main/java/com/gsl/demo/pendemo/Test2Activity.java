package com.gsl.demo.pendemo;

import android.animation.AnimatorSet;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.app.Activity;
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

public class Test2Activity extends Activity {
	private static final String TAG = "test";

	private RelativeLayout mRLTopParent;
	private RelativeLayout mRLMindGuide;
	private ImageView mIVOption01;
	private ImageView mIVOption02;
	private ImageView mIVOption03;


	private List<Test2Activity.OptionPointF> mOptionDestList;
	private int mOptionDestRadius=100;

	private class OptionPointF extends PointF {
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
		setContentView(R.layout.activity_test2);

		mRLTopParent = (RelativeLayout) findViewById(R.id.activity_test2);
		mRLMindGuide = (RelativeLayout) findViewById(R.id.mind_guide_canvas);
		mRLMindGuide.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				Log.d(TAG, "MindGuide x:"+event.getX()+", y:"+event.getY());
				return false;
			}
		});

		mIVOption01 = (ImageView) findViewById(R.id.op_01);
		mIVOption02 = (ImageView) findViewById(R.id.op_02);
		mIVOption03 = (ImageView) findViewById(R.id.op_03);

		mIVOption01.setOnTouchListener(createOnTouchListener());
		mIVOption01.setTag("cpp");
		mIVOption02.setOnTouchListener(createOnTouchListener());
		mIVOption02.setTag("go");
		mIVOption03.setOnTouchListener(createOnTouchListener());
		mIVOption03.setTag("java");

		initOptionDest();
	}

	private void initOptionDest(){
		mOptionDestList = new ArrayList<>();
		mOptionDestList.add(new Test2Activity.OptionPointF(432.40f, 29.04f));
		mOptionDestList.add(new Test2Activity.OptionPointF(432.40f, 305.39f));
		mOptionDestList.add(new Test2Activity.OptionPointF(432.40f, 439.57f));
	}

	private void logDestPoint(){
		for(OptionPointF pf : mOptionDestList){
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
						Log.d(TAG, "ACTION_MOVE");
						float currX = event.getRawX();
						float currY = event.getRawY();

						int dx = (int) (currX - lastX);
						int dy = (int) (currY - lastY);
						Log.d(TAG, "move dx:"+dx+", dy:"+dy);
						if (Math.abs(dx) > 0 || Math.abs(dy) > 0) {
							mlp.leftMargin = v.getLeft() + dx;
							mlp.topMargin = v.getTop() + dy;
							v.setLayoutParams(mlp);

						}
						lastX = currX;
						lastY = currY;
						break;
					case MotionEvent.ACTION_UP:
						Log.e(TAG, "ACTION_UP");
						if (lastPoint == null) {
							Log.e(TAG, "lastPoint is null");
						}

						PointF moveFrom = new PointF(mlp.leftMargin, mlp.topMargin);
						final Test2Activity.OptionPointF dest = getNearestOptionDest(moveFrom, true);

						if (null != dest) {
							long duration = 500;
							PointF moveTo = new PointF(dest.x, dest.y);
							moveTo.y -= v.getHeight() / 2;

							OptionPointF srcPoint=getPointByBindView((ImageView) v);
							ImageView fromDest = dest.getBind();
							if (!dest.isBind()) {
								Log.e(TAG, "find unbind dest");

								if(srcPoint!=null) {
									srcPoint.removeBind();
								}
								Log.d(TAG, "bind destPoint:" + dest + ", v:" + v.getTag());
								dest.bindImageView((ImageView) v);
								viewMoving(v, moveFrom, moveTo, duration);
							} else {
								Log.e(TAG, "find bind dest");

								if(srcPoint!=null) {
									Log.d(TAG, "bind srcPoint:"+srcPoint+", v:"+fromDest.getTag());
									srcPoint.bindImageView(fromDest);
									Log.d(TAG, "bind destPoint:" + dest + ", v:" + v.getTag());
									dest.bindImageView((ImageView) v);
									viewAllMoving(
											createViewMoving(fromDest, dest, lastPoint, duration),
											createViewMoving(v, moveFrom, moveTo, duration)
									);
								}else{
									Log.e(TAG, "cannot find point by lastPoint");
									//viewMoving(v, moveFrom, moveTo, duration);
								}
							}

						}else{
							Log.e(TAG, "cannot find dest");
							OptionPointF srcPoint = getNearestOptionDest(lastPoint, true);
							if(null!=srcPoint){
								srcPoint.removeBind();
							}
						}


						break;
				}

				logDestPoint();
				return false;
			}

		};
	}

	private Test2Activity.OptionPointF getPointByBindView(ImageView view){
		for(OptionPointF pf : mOptionDestList){
			if(pf.isBind() && pf.getBind().equals(view))
				return pf;
		}
		return null;
	}

	private Test2Activity.OptionPointF getNearestOptionDest(PointF p){
		return getNearestOptionDest(p, false);
	}

	private void viewMoving(View v, PointF moveFrom, PointF moveTo, long duration){
		createViewMoving(v, moveFrom, moveTo, duration).start();
	}

	private Test2Activity.OptionPointF getNearestOptionDest(PointF p, boolean allowUsed){
		Log.e(TAG, "option x:"+p.x+", y:"+p.y);
		Test2Activity.OptionPointF dest = null;
		if(mOptionDestList.size()>0) {
			double radius = Math.pow(DensityUtil.dip2px(this,mOptionDestRadius), 2);
			double min = Math.pow(mRLMindGuide.getWidth(), 2) + Math.pow(mRLMindGuide.getHeight(), 2);
			//Log.e(TAG, "init radius:"+radius+", min:"+min);
			for (Test2Activity.OptionPointF pf : mOptionDestList) {
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
				//mRLTopParent.postInvalidate();
			}
		});

		return animator;
	}


}
