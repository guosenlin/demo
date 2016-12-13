package com.gsl.demo.pendemo.view;


import android.view.animation.Animation;
import android.view.animation.Transformation;

/**
 * <p>
 * Created by yanshen@tsinghuabigdata.com on 2015/10/30.
 * </p>
 * <p/>
 * 圆周运动动画效果
 *
 * @author yanshen@tsinghuabigdata.com
 * @version V1.0
 * @packageName: com.tsinghuabigdata.edu.zxapp.android.animation
 * @createTime: 2015/10/30 9:42
 */
public class CircleAnimation extends Animation {

    private int radii;

    public CircleAnimation(int radii) {
        this.radii = radii;
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        float d = 360 * interpolatedTime + 180;
        if (d > 360) {
            d = d - 360;
        }
        int[] ps = getNewLocation((int) d, radii, 0, 0);
        t.getMatrix().setTranslate(ps[0], ps[1] - radii);
    }

    public int[] getNewLocation(int newAngle, int r, int width, int height) {
        int newAngle1;
        int newX = 0, newY = 0;
        /**
         * 0-90
         */
        if (newAngle == 0) {
            newX = width;
            newY = height - r;
        } else if (newAngle == 90) {
            newX = width + r;
            newY = height;
        } else if (newAngle == 180) {
            newX = width;
            newY = height + r;
        } else if (newAngle == 270) {
            newX = width - r;
            newY = height;
        } else if (newAngle == 360) {
            newX = width;
            newY = height - r;
        } else if (newAngle > 360) {
            newX = width;
            newY = height - r;
        } else if (newAngle > 0 && newAngle < 90) {
            newX = (int) (width + (r * Math.sin(newAngle * Math.PI / 180)));
            newY = (int) (height - (r * Math.cos(newAngle * Math.PI / 180)));
        }

        /**
         * 90-180
         */
        else if (newAngle > 90 && newAngle < 180) {
            newAngle1 = 180 - newAngle;
            newX = (int) (width + (r * Math.sin(newAngle1 * Math.PI / 180)));
            newY = (int) (height + (r * Math.cos(newAngle1 * Math.PI / 180)));
        }

        /**
         * 180-270
         */
        else if (newAngle > 180 && newAngle < 270) {
            newAngle1 = 270 - newAngle;
            newX = (int) (width - (r * Math.cos(newAngle1 * Math.PI / 180)));
            newY = (int) (height + (r * Math.sin(newAngle1 * Math.PI / 180)));
        }

        /**
         * 270-360
         */
        else if (newAngle > 270 && newAngle < 360) {
            newAngle1 = 360 - newAngle;
            newX = (int) (width - (r * Math.sin(newAngle1 * Math.PI / 180)));
            newY = (int) (height - (r * Math.cos(newAngle1 * Math.PI / 180)));
        }

        return new int[]{newX, newY};
    }

}