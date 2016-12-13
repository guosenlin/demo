package com.gsl.demo.measurespec.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;

import com.gsl.demo.measurespec.R;

public class TestActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.test_layout);
    }
}
