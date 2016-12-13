package com.gsl.demo.imdemo.util;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by guosenlin on 16-8-28.
 */

public class ActivityCollection {
    private static List<Activity> list = new ArrayList<>();

    public static void addActivity(Activity activity) {
        list.add(activity);
    }

    public static void removeActivity(Activity activity) {
        if(list.contains(activity)) {
            list.remove(activity);
        }
    }

    public static void finishAll() {
        for(Activity activity : list) {
            if(!activity.isFinishing()) {
                activity.finish();
            }
        }
    }
}
