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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

public class Test3Activity extends Activity {
	private static final String TAG = "test";

	private RelativeLayout mRLMindGuide;
	private RelativeLayout mRLTopParent;
	private LinearLayout mLLOptionBoxLeft;
	private LinearLayout mLLOptionBoxRight;
	private ImageView mOptionLocater;

	private int mOptionSrcLeft;
	private int mOptionSrcTop;

	private List<OptionPointF> mOptionDestList;
	private int mOptionDestRadius=100;
	private int count;

	enum PointDirect{
		left, right, top ,down
	}

	private class OptionPointF extends PointF{
		private ImageView view;
		private PointDirect direct;

		public OptionPointF(float x, float y) {
			this(x, y, PointDirect.left);
		}
		public OptionPointF(float x, float y, PointDirect direct) {
			super(x, y);
			this.direct = direct;
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

		public PointDirect getDirect() {
			return direct;
		}

		public void setDirect(PointDirect direct) {
			this.direct = direct;
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
		//requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_test3);
		mRLTopParent = (RelativeLayout) findViewById(R.id.activity_test3);
		mRLTopParent.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				Log.d(TAG, "mRLTopParent x:"+event.getX()+", y:"+event.getY());
				return false;
			}
		});
		mLLOptionBoxLeft = (LinearLayout) findViewById(R.id.select_box_left);
		mLLOptionBoxRight = (LinearLayout) findViewById(R.id.select_box_right);

		mRLMindGuide = (RelativeLayout) findViewById(R.id.mind_guide_canvas);
		mOptionLocater = (ImageView) findViewById(R.id.option_locater);
		mOptionLocater.setClickable(true);
		mOptionLocater.bringToFront();
		mOptionLocater.setOnTouchListener(optionLocatorListener);

		initOptionBox();

		initOptionDest();
	}

	private void initOptionBox(){
		ImageView mIVOption01 = (ImageView) findViewById(R.id.op_01);
		ImageView mIVOption02 = (ImageView) findViewById(R.id.op_02);
		ImageView mIVOption03 = (ImageView) findViewById(R.id.op_03);
		ImageView mIVOption04 = (ImageView) findViewById(R.id.op_04);
		ImageView mIVOption05 = (ImageView) findViewById(R.id.op_05);
		ImageView mIVOption06 = (ImageView) findViewById(R.id.op_06);
		ImageView mIVOption07 = (ImageView) findViewById(R.id.op_07);
		ImageView mIVOption08 = (ImageView) findViewById(R.id.op_08);
		ImageView mIVOption09 = (ImageView) findViewById(R.id.op_09);
		ImageView mIVOption10 = (ImageView) findViewById(R.id.op_10);
		ImageView mIVOption11 = (ImageView) findViewById(R.id.op_11);
		ImageView mIVOption12 = (ImageView) findViewById(R.id.op_12);
		ImageView mIVOption13 = (ImageView) findViewById(R.id.op_13);
		ImageView mIVOption14 = (ImageView) findViewById(R.id.op_14);
		ImageView mIVOption15 = (ImageView) findViewById(R.id.op_15);
		ImageView mIVOption16 = (ImageView) findViewById(R.id.op_16);
		ImageView mIVOption17 = (ImageView) findViewById(R.id.op_17);
		ImageView mIVOption18 = (ImageView) findViewById(R.id.op_18);
		ImageView mIVOption19 = (ImageView) findViewById(R.id.op_19);
		ImageView mIVOption20 = (ImageView) findViewById(R.id.op_20);
		ImageView mIVOption21 = (ImageView) findViewById(R.id.op_21);
		ImageView mIVOption22 = (ImageView) findViewById(R.id.op_22);
		ImageView mIVOption23 = (ImageView) findViewById(R.id.op_23);
		ImageView mIVOption24 = (ImageView) findViewById(R.id.op_24);
		ImageView mIVOption25 = (ImageView) findViewById(R.id.op_25);
		ImageView mIVOption26 = (ImageView) findViewById(R.id.op_26);
		ImageView mIVOption27 = (ImageView) findViewById(R.id.op_27);
		ImageView mIVOption28 = (ImageView) findViewById(R.id.op_28);
		ImageView mIVOption29 = (ImageView) findViewById(R.id.op_29);
		ImageView mIVOption30 = (ImageView) findViewById(R.id.op_30);
		ImageView mIVOption31 = (ImageView) findViewById(R.id.op_31);
		ImageView mIVOption32 = (ImageView) findViewById(R.id.op_32);

		mIVOption01.setOnTouchListener(optionListener);
		mIVOption02.setOnTouchListener(optionListener);
		mIVOption03.setOnTouchListener(optionListener);
		mIVOption04.setOnTouchListener(optionListener);
		mIVOption05.setOnTouchListener(optionListener);
		mIVOption06.setOnTouchListener(optionListener);
		mIVOption07.setOnTouchListener(optionListener);
		mIVOption08.setOnTouchListener(optionListener);
		mIVOption09.setOnTouchListener(optionListener);
		mIVOption10.setOnTouchListener(optionListener);
		mIVOption11.setOnTouchListener(optionListener);
		mIVOption12.setOnTouchListener(optionListener);
		mIVOption13.setOnTouchListener(optionListener);
		mIVOption14.setOnTouchListener(optionListener);
		mIVOption15.setOnTouchListener(optionListener);
		mIVOption16.setOnTouchListener(optionListener);
		mIVOption17.setOnTouchListener(optionListener);
		mIVOption18.setOnTouchListener(optionListener);
		mIVOption19.setOnTouchListener(optionListener);
		mIVOption20.setOnTouchListener(optionListener);
		mIVOption21.setOnTouchListener(optionListener);
		mIVOption22.setOnTouchListener(optionListener);
		mIVOption23.setOnTouchListener(optionListener);
		mIVOption24.setOnTouchListener(optionListener);
		mIVOption25.setOnTouchListener(optionListener);
		mIVOption26.setOnTouchListener(optionListener);
		mIVOption27.setOnTouchListener(optionListener);
		mIVOption28.setOnTouchListener(optionListener);
		mIVOption29.setOnTouchListener(optionListener);
		mIVOption30.setOnTouchListener(optionListener);
		mIVOption31.setOnTouchListener(optionListener);
		mIVOption32.setOnTouchListener(optionListener);

	}

	private void initOptionDest(){
		mOptionDestList = new ArrayList<>();
		//simulator
//		mOptionDestList.add(new OptionPointF(281.25f, 186.24f, PointDirect.right));
//		mOptionDestList.add(new OptionPointF(281.25f, 212.28f, PointDirect.right));
//		mOptionDestList.add(new OptionPointF(267.25f, 241.32f, PointDirect.right));
//		mOptionDestList.add(new OptionPointF(267.25f, 266.35f, PointDirect.right));
//		mOptionDestList.add(new OptionPointF(267.25f, 290.37f, PointDirect.right));
//		mOptionDestList.add(new OptionPointF(267.25f, 315.40f, PointDirect.right));
//		mOptionDestList.add(new OptionPointF(267.25f, 346.46f, PointDirect.right));
//		mOptionDestList.add(new OptionPointF(267.25f, 381.5f, PointDirect.right));
//		mOptionDestList.add(new OptionPointF(267.25f, 415.53f, PointDirect.right));
//		mOptionDestList.add(new OptionPointF(264.25f, 444.57f, PointDirect.right));
//		mOptionDestList.add(new OptionPointF(264.25f, 478.62f, PointDirect.right));
//		mOptionDestList.add(new OptionPointF(379.34f, 499.64f, PointDirect.right));
//		mOptionDestList.add(new OptionPointF(379.34f, 522.68f, PointDirect.right));
//		mOptionDestList.add(new OptionPointF(379.34f, 550.71f, PointDirect.right));
//		mOptionDestList.add(new OptionPointF(379.34f, 573.73f, PointDirect.right));
//		mOptionDestList.add(new OptionPointF(379.34f, 598.76f, PointDirect.right));
//
//		mOptionDestList.add(new OptionPointF(775.71f, 118.15f));
//		mOptionDestList.add(new OptionPointF(775.71f, 141.19f));
//		mOptionDestList.add(new OptionPointF(775.71f, 166.22f));
//		mOptionDestList.add(new OptionPointF(775.71f, 192.26f));
//		mOptionDestList.add(new OptionPointF(775.71f, 216.28f));
//		mOptionDestList.add(new OptionPointF(775.71f, 239.32f));
//		mOptionDestList.add(new OptionPointF(775.71f, 265.34f));
//		mOptionDestList.add(new OptionPointF(775.71f, 291.38f));
//		mOptionDestList.add(new OptionPointF(775.71f, 315.40f));
//		mOptionDestList.add(new OptionPointF(775.71f, 339.45f));
//		mOptionDestList.add(new OptionPointF(775.71f, 366.47f));
//		mOptionDestList.add(new OptionPointF(775.71f, 392.51f));
//		mOptionDestList.add(new OptionPointF(695.65f, 433.55f));
//		mOptionDestList.add(new OptionPointF(695.65f, 468.61f));
//		mOptionDestList.add(new OptionPointF(695.65f, 510.66f));
//		mOptionDestList.add(new OptionPointF(695.65f, 545.70f));

		//true tab
		mOptionDestList.add(new OptionPointF(200.7f, 153.7f, PointDirect.right));
		mOptionDestList.add(new OptionPointF(200.7f, 185.77f, PointDirect.right));
		mOptionDestList.add(new OptionPointF(180.88f, 224.26f, PointDirect.right));
		mOptionDestList.add(new OptionPointF(180.88f, 256.50f, PointDirect.right));
		mOptionDestList.add(new OptionPointF(180.88f, 288.42f, PointDirect.right));
		mOptionDestList.add(new OptionPointF(180.88f, 317.95f, PointDirect.right));
		mOptionDestList.add(new OptionPointF(180.88f, 359.46f, PointDirect.right));
		mOptionDestList.add(new OptionPointF(180.88f, 406.02f, PointDirect.right));
		mOptionDestList.add(new OptionPointF(180.88f, 446.44f, PointDirect.right));
		mOptionDestList.add(new OptionPointF(175.98f, 482.85f, PointDirect.right));
		mOptionDestList.add(new OptionPointF(175.98f, 528.11f, PointDirect.right));
		mOptionDestList.add(new OptionPointF(328.74f, 554.41f, PointDirect.right));
		mOptionDestList.add(new OptionPointF(328.74f, 584.25f, PointDirect.right));
		mOptionDestList.add(new OptionPointF(328.74f, 617.58f, PointDirect.right));
		mOptionDestList.add(new OptionPointF(331.46f, 649.09f, PointDirect.right));
		mOptionDestList.add(new OptionPointF(332.46f, 682.68f, PointDirect.right));

		mOptionDestList.add(new OptionPointF(863.68f, 63.74f));
		mOptionDestList.add(new OptionPointF(863.68f, 95.88f));
		mOptionDestList.add(new OptionPointF(863.68f, 124.7f));
		mOptionDestList.add(new OptionPointF(863.68f, 157.18f));
		mOptionDestList.add(new OptionPointF(863.68f, 187.28f));
		mOptionDestList.add(new OptionPointF(863.68f, 219.10f));
		mOptionDestList.add(new OptionPointF(863.68f, 252.54f));
		mOptionDestList.add(new OptionPointF(863.68f, 283.94f));
		mOptionDestList.add(new OptionPointF(863.68f, 315.45f));
		mOptionDestList.add(new OptionPointF(863.68f, 347.22f));
		mOptionDestList.add(new OptionPointF(863.68f, 381.55f));
		mOptionDestList.add(new OptionPointF(863.68f, 416.91f));
		mOptionDestList.add(new OptionPointF(759.47f, 466.75f));
		mOptionDestList.add(new OptionPointF(759.47f, 511.75f));
		mOptionDestList.add(new OptionPointF(759.47f, 566.28f));
		mOptionDestList.add(new OptionPointF(759.47f, 613.31f));
	}

	private void logDestPoint(){
		for(Test3Activity.OptionPointF pf : mOptionDestList){
			Log.e(TAG, pf+"--->"+(pf.isBind()?pf.getBind().getTag():"unbind"));
		}
	}

	private View.OnTouchListener createOnTouchListener() {
		return new View.OnTouchListener() {
			float lastX;
			float lastY;
			PointF lastPointLeft;
			OptionPointF lastOptionPoint;
			private boolean move2left=true;

			@Override
			public boolean onTouch(final View v, MotionEvent event) {
				ViewGroup.MarginLayoutParams mlp = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
				switch (event.getAction()) {
					case MotionEvent.ACTION_DOWN:
						Log.d(TAG, "answerListener ACTION_DOWN");

						lastX = event.getRawX();
						lastY = event.getRawY();
						lastPointLeft = new PointF(mlp.leftMargin, mlp.topMargin);
						lastOptionPoint = getPointByBindView((ImageView) v);
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
						move2left = dx<0;
						break;
					case MotionEvent.ACTION_UP:
						//Log.e(TAG, "ACTION_UP");
						PointF moveFrom = new PointF(mlp.leftMargin, mlp.topMargin);
						final OptionPointF dest = getNearestOptionDest(getViewLocation(v, move2left), true);
						long duration = 200;

						if (null != dest) {
							PointF moveTo = new PointF(dest.x, dest.y);
							moveTo.y -= v.getHeight() / 2;
							if(dest.direct==PointDirect.right){
								moveTo.x -= v.getWidth();
							}

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
								ViewGroup.MarginLayoutParams srcMlp = (ViewGroup.MarginLayoutParams) fromDest.getLayoutParams();
								PointF destMoveFrom = new PointF(srcMlp.leftMargin, srcMlp.topMargin);

								PointF destMoveTo = new PointF(lastOptionPoint.x, lastOptionPoint.y);
								destMoveTo.y -= fromDest.getHeight() / 2;
								if(lastOptionPoint.direct==PointDirect.right){
									destMoveTo.x -= fromDest.getWidth();
								}

								dest.bindImageView((ImageView) v);
								srcPoint.bindImageView(fromDest);
								viewAllMoving(
										createViewMoving(fromDest, destMoveFrom, destMoveTo, duration),
										createViewMoving(v, moveFrom, moveTo, duration)
								);
							}

						} else {
							viewMoving(v, moveFrom, lastPointLeft, duration);
						}

						break;
				}

				//logDestPoint();
				return false;
			}

		};
	}

	private PointF getViewLocation(View v, boolean move2left){
		ViewGroup.MarginLayoutParams mlp = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
		PointF p;
		if(move2left) {
			p = new PointF(mlp.leftMargin, mlp.topMargin);
		}else{
			p = new PointF(mlp.leftMargin+v.getWidth(), mlp.topMargin+v.getHeight());
		}
		return p;
	}

	private Test3Activity.OptionPointF getPointByBindView(ImageView view){
		for(Test3Activity.OptionPointF pf : mOptionDestList){
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
			double min = Math.pow(mRLTopParent.getWidth(), 2) + Math.pow(mRLTopParent.getHeight(), 2);
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
		boolean move2left;
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
					move2left = dx<0;
					break;
				case MotionEvent.ACTION_UP:
					Log.d(TAG, "ACTION_UP");

					PointF moveFrom = new PointF(mlp.leftMargin, mlp.topMargin);
					OptionPointF dest = getNearestOptionDest(getViewLocation(v, move2left), true);
					PointF moveTo;
					boolean findDest;
					long duration = 100;

					if (findDest = (null != dest)) {
						moveTo = new PointF(dest.x, dest.y);
						moveTo.y -= v.getHeight() / 2;
						if(dest.direct==PointDirect.right) {
							moveTo.x -= v.getWidth();
						}

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
							ImageView newIv = new ImageView(Test3Activity.this);
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
