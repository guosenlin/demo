package com.gsl.demo.measurespec.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Scroller;
import android.widget.Toast;

import com.gsl.demo.measurespec.R;
import com.gsl.demo.measurespec.util.ActivityCollection;
import com.gsl.demo.measurespec.view.MyCustomView;

public class LoginActivity extends Activity implements View.OnClickListener {
    private static final String TAG = "LoginActivity";
    private Button btLogin;
    private Button btToTest;
    private EditText etAccount;
    private EditText etPassword;
    private MyCustomView myCustomView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        etAccount = (EditText)findViewById(R.id.et_account);
        etPassword = (EditText)findViewById(R.id.et_password);

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        etAccount.setText(sp.getString("account", ""));
        etPassword.setText(sp.getString("password", ""));


        btLogin = (Button) findViewById(R.id.bt_login);
        btLogin.setOnClickListener(this);
        btToTest = (Button) findViewById(R.id.bt_to_test);
        btToTest.setOnClickListener(this);

        myCustomView = (MyCustomView)findViewById(R.id.my_custom_view);
        if(null!=myCustomView) {
            Log.d(TAG, "MyCustomView has bean loaded");
            myCustomView.startDrawPercent();
        }



        ActivityCollection.addActivity(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_login:
                toLogin();
                break;
            case R.id.bt_to_test:
                Log.d(TAG, "start TestActivity");
                Intent intent = new Intent(this, TestActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    private void toLogin() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        String acc = sp.getString("account", "");
        String pwd = sp.getString("password", "");
        boolean login = false;
        if(TextUtils.isEmpty(acc) && TextUtils.isEmpty(pwd)) {
            login = true;
            SharedPreferences.Editor editor = sp.edit();
            editor.putString("account", etAccount.getText().toString());
            editor.putString("password", etPassword.getText().toString());
            editor.commit();
        } else {
            if(acc.equals(etAccount.getText().toString())
                    && pwd.equals(etPassword.getText().toString())) {
                login = true;
            }
        }
        if(login) {
            Intent i = new Intent("com.gsl.demo.action.appIndex");
            startActivityForResult(i, 1);
        } else {
            Toast.makeText(this, "error account or password", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 1:
                if(RESULT_OK == resultCode) {
                    String ret = data.getStringExtra("return_result");
                    Toast.makeText(this, ret, Toast.LENGTH_LONG).show();
                    Log.d("LoginActivity", ret);
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollection.removeActivity(this);
    }
}
