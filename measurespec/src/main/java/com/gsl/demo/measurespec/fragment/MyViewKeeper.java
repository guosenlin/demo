package com.gsl.demo.measurespec.fragment;

import android.view.View;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by guosenlin on 16-10-28.
 */

public class MyViewKeeper {

    private static Map<String, View> map = new HashMap<>();

    public static void addView(String key, View view){
        map.put(key, view) ;
    }

    public static void removeView(String key){
        map.remove(key);
    }


    public static View getView(String key) {
        return map.get(key);
    }

}
