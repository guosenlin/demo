package com.gsl.demo.imdemo.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.gsl.demo.imdemo.R;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btA;
    private Button btB;
    private Button btC;
    private EditText etIp;
    private EditText etPort;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btA = (Button)findViewById(R.id.bt_a);
        btA.setOnClickListener(this);
        btB = (Button)findViewById(R.id.bt_b);
        btB.setOnClickListener(this);
        btC = (Button)findViewById(R.id.bt_c);
        btC.setOnClickListener(this);

        etIp = (EditText)findViewById(R.id.et_ip);
        etPort = (EditText)findViewById(R.id.et_port);

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        etIp.setText(sp.getString("ip", "192.168.100.32"));
        etPort.setText(sp.getString("port", "1883"));
    }

    @Override
    public void onClick(View view) {
        String username;
        String password;
        switch (view.getId()){
            case R.id.bt_a:
                username = "A";
                password = "a123";
                break;
            case R.id.bt_b:
                username = "B";
                password = "b123";
                break;
            case R.id.bt_c:
                username = "C";
                password = "c123";
                break;
            default:
                username = "admin";
                password = "admin";
        }

        if(signIn(username, password)){
            Intent intent = new Intent(this, FriendsActivity.class);
            intent.putExtra("username", username);
            intent.putExtra("passwd", password);
            String ip = etIp.getText().toString();
            String port = etPort.getText().toString();
            if(TextUtils.isEmpty(ip) || TextUtils.isEmpty(port)){
                Toast.makeText(this, "empty ip or port", Toast.LENGTH_SHORT).show();
            }else {
                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
                SharedPreferences.Editor editor = sp.edit();
                editor.putString("ip", etIp.getText().toString());
                editor.putString("port", etPort.getText().toString());
                editor.commit();

                intent.putExtra("ip", ip);
                intent.putExtra("port", port);
                startActivity(intent);
            }
        }else{
            Toast.makeText(this, "error username or password", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean signIn(String username, String pwd){

        boolean success = TextUtils.equals("A", username)
                || TextUtils.equals("B", username)
                || TextUtils.equals("C", username);

        return success;
    }
}
