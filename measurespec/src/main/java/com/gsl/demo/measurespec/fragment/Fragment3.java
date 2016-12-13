package com.gsl.demo.measurespec.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gsl.demo.measurespec.R;

/**
 * Created by guosenlin on 16-10-26.
 */

public class Fragment3 extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d("Fragment3", "onCreateView 3");
        View view = MyViewKeeper.getView("Fragment3");
        if(view==null){
            view = inflater.inflate(R.layout.layout3, null);
            MyViewKeeper.addView("Fragment3", view);
        }
        return view;
    }
}
