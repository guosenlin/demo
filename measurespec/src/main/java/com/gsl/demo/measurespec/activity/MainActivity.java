package com.gsl.demo.measurespec.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Scroller;
import android.widget.Toast;

import com.gsl.demo.measurespec.R;
import com.gsl.demo.measurespec.util.ActivityCollection;
import com.gsl.demo.measurespec.util.MyViewPagerAdapter;
import com.gsl.demo.measurespec.view.TopBar;

public class MainActivity extends Activity implements View.OnClickListener {
    private static final String TAG = "MainActivity";
    private Button btRefresh;
    private ProgressDialog progressDialog;

    private TopBar topBar;

    private Scroller mScroller;

    private Button btViewPager;
    private Button btViewPager2;
    private Button btViewPager3;

    private boolean toExit;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Log.d(TAG, "------------------------------->>>handle message");
            if(msg.what == 1) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(1000*2);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        toExit = false;
                    }
                }).start();
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LinearLayout linearLayout =  (LinearLayout) findViewById(R.id.layout_main);

        btViewPager = (Button)findViewById(R.id.bt_viewpager);
        btViewPager.setOnClickListener(this);
        btViewPager2 = (Button)findViewById(R.id.bt_viewpager2);
        btViewPager2.setOnClickListener(this);
        btViewPager3 = (Button)findViewById(R.id.bt_viewpager3);
        btViewPager3.setOnClickListener(this);


        LayoutInflater layoutInflater = LayoutInflater.from(this);
        if(layoutInflater.equals(this.getSystemService(Context.LAYOUT_INFLATER_SERVICE))){
            Log.d(TAG, "LayoutInflater.from(this) equals this.getSystemService(Context.LAYOUT_INFLATER_SERVICE)");
        }

        //加载布局并填充到容器中
        //layoutInflater.inflate(R.layout.item_layout, linearLayout);

        LinearLayout itemLayout = (LinearLayout) layoutInflater.inflate(R.layout.item_layout, null);
        linearLayout.addView(itemLayout);

        btRefresh = (Button)findViewById(R.id.bt_refresh);
        btRefresh.setOnClickListener(this);

        topBar = (TopBar)findViewById(R.id.customView_topbar);
        topBar.setTopbarClickListener(new TopBar.TopbarClickListener() {
            @Override
            public void onLeftClick(View v) {
                Toast.makeText(MainActivity.this, "click button back", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRightClick(View v) {
                Toast.makeText(MainActivity.this, "click button menu", Toast.LENGTH_SHORT).show();
            }
        });

        ActivityCollection.addActivity(this);
    }

    @Override
    public void onBackPressed() {
//        Intent i = new Intent();
//        i.putExtra("return_result", "Hello Login");
//        setResult(RESULT_OK, i);
//        finish();
        if(toExit) {
            ActivityCollection.finishAll();
        } else {
            toExit = true;
            Message msg = new Message();
            msg.what = 1;
            handler.sendMessage(msg);
            Toast.makeText(this, "再按一次退出", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollection.removeActivity(this);
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch(view.getId()) {
            case R.id.bt_refresh:
                new DownloadTask().execute();
                break;
            case R.id.bt_viewpager:
                intent = new Intent(this, ViewPagerActivity.class);
                startActivity(intent);
                break;
            case R.id.bt_viewpager2:
                intent = new Intent(this, ViewPagerActivity2.class);
                startActivity(intent);
                break;
            default:
        }
    }

    class DownloadTask extends AsyncTask<Void, Integer, Boolean> {
        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(MainActivity.this, "loading...", "please waiting...");
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            int step = 0;
            while(step<=100) {
                try {
                    Thread.sleep(1000);
                    publishProgress(step);
                    step += 10;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return true;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            progressDialog.setMessage("downloaded "+values[0]+"%");
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            progressDialog.dismiss();
            if(aBoolean) {
                Toast.makeText(MainActivity.this, "download success", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MainActivity.this, "download fail", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
