package com.gsl.demo.imdemo.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.gsl.demo.imdemo.R;
import com.gsl.demo.imdemo.bean.Msg;
import com.gsl.demo.imdemo.util.MsgAdapter;
import com.gsl.demo.imdemo.util.MsgClient;
import com.gsl.demo.imdemo.util.MsgClientFactory;
import com.tinyteam.im.protocol.PlateformMessage;
import com.tinyteam.im.protocol.TextMsg;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SendMsgActivity extends Activity implements View.OnClickListener {
    private static final String TAG = "SendMsgActivity";
    private ListView lvMsg;
    private ImageView ivMsgBg;
    private Button btSend;
    private EditText etInput;

    private List<Msg> msgList = new ArrayList<>();

    private String senderName;
    private String friendTopic;
    private String username;
    private int friendType;

    private MsgAdapter msgAdapter;
    private MsgClient msgClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_msg);

        Intent intent = getIntent();
        senderName = intent.getStringExtra("nickname");
        friendTopic = intent.getStringExtra("topic");
        friendType = intent.getIntExtra("type", 0);
        username = intent.getStringExtra("username");


        lvMsg = (ListView)findViewById(R.id.lv_send_msg);
        ivMsgBg = (ImageView)findViewById(R.id.empty_view);
        ivMsgBg.setAlpha(0.3f);
        lvMsg.setEmptyView(ivMsgBg);

        msgAdapter = new MsgAdapter(this, msgList);
        lvMsg.setAdapter(msgAdapter);



        etInput = (EditText)findViewById(R.id.et_msg);
        btSend = (Button)findViewById(R.id.bt_send);
        btSend.setOnClickListener(this);

        LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(this);

        msgClient = MsgClientFactory.getMsgClient(username);
        if(msgClient!=null) {
//            msgClient.addOnMsgReceivedListener(new MsgClient.OnMsgReceivedListener() {
//                @Override
//                public void onMsgReceive(String topic, final MqttMessage message) {
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            Msg msg = new Msg(new String(message.getPayload()), Msg.MSG_RECEIVE, "remote", new Date());
//                            msgList.add(msg);
//                            msgAdapter.notifyDataSetChanged();
//                            lvMsg.setSelection(msgList.size()-1);
//                            Log.d(TAG, msg.getType()+" receive msg "+new String(message.getPayload()));
//                        }
//                    });
//                }
//            });
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction("com.sample.receiveMsg");
            localBroadcastManager.registerReceiver(new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, final Intent intent) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Msg msg = new Msg(intent.getStringExtra("content"), Msg.MSG_RECEIVE, "remote", new Date());
                            msgList.add(msg);
                            msgAdapter.notifyDataSetChanged();
                            lvMsg.setSelection(msgList.size()-1);
                            Log.d(TAG, msg.getType()+" receive msg "+intent.getStringExtra("content"));
                        }
                    });
                }
            }, intentFilter);

        }else{
            Log.d(TAG, "msgClient is null");
        }
    }

    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.bt_send){
            String content = etInput.getText().toString();
            if(!TextUtils.isEmpty(content)){
                Log.d(TAG, "send msg "+content+" to "+friendTopic);

                PlateformMessage msg = new TextMsg(
                        PlateformMessage.MSG_TYPE_ONE_TO_ONE,
                        123L,
                        456L,
                        new Date(),
                        content
                        );
                msgClient.sendMsg(friendTopic, msg);

                msgList.add(new Msg(content, Msg.MSG_SEND, senderName, new Date()));
                msgAdapter.notifyDataSetChanged();
                lvMsg.setSelection(msgList.size()-1);
                etInput.setText("");
            }else{
                Toast.makeText(this, "input cannot be empty", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
