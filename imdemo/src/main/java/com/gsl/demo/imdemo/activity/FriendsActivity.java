package com.gsl.demo.imdemo.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.gsl.demo.imdemo.R;
import com.gsl.demo.imdemo.bean.FriendItem;
import com.gsl.demo.imdemo.util.FriendsArrayAdapter;
import com.gsl.demo.imdemo.util.MsgClient;
import com.gsl.demo.imdemo.util.MsgClientFactory;

import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.ArrayList;
import java.util.List;

public class FriendsActivity extends Activity {
    private static final String TAG = "FriendsActivity";
    private ListView lvFriends;
    private ListView lvGroups;

    private List<FriendItem> friendList;
    private List<FriendItem> groupList;

    private String serverIP;
    private String serverPort;
    private String user;
    private String passwd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_friends);

        lvFriends = (ListView)findViewById(R.id.lv_friend);
        lvGroups = (ListView)findViewById(R.id.lv_group);

        Intent intent = getIntent();

        user = intent.getStringExtra("username");
        passwd = intent.getStringExtra("passwd");
        final String username = user;
        friendList = createFriendList(username);
        groupList = createGroupList(username);
        serverIP = intent.getStringExtra("ip");
        serverPort = intent.getStringExtra("port");

        ListAdapter fAdapter = new FriendsArrayAdapter(this, R.layout.item_friend, friendList);
        lvFriends.setAdapter(fAdapter);
        lvFriends.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //Toast.makeText(FriendsActivity.this, "click friend "+friendList.get(i).getNickName(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(FriendsActivity.this, SendMsgActivity.class);
                FriendItem item = friendList.get(i);
                intent.putExtra("username", username);
                intent.putExtra("nickname", item.getNickName());
                intent.putExtra("topic", item.getTopicUrl());
                intent.putExtra("type", item.getType());
                startActivity(intent);
            }
        });

        ListAdapter gAdapter = new FriendsArrayAdapter(this, R.layout.item_friend, groupList);
        lvGroups.setAdapter(gAdapter);
        lvGroups.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //Toast.makeText(FriendsActivity.this, "click group "+groupList.get(i).getNickName(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(FriendsActivity.this, SendMsgActivity.class);
                FriendItem item = groupList.get(i);
                intent.putExtra("username", username);
                intent.putExtra("nickname", item.getNickName());
                intent.putExtra("topic", item.getTopicUrl());
                intent.putExtra("type", item.getType());
                startActivity(intent);
            }
        });

        MsgClient msgClient = MsgClientFactory.createMsgClient(this, username, "tcp://"+serverIP+":"+serverPort, user, passwd, 0);
        msgClient.addOnMsgReceivedListener(new MsgClient.OnMsgReceivedListener() {
            @Override
            public void onMsgReceive(final String topic, final MqttMessage message) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.d(TAG, "receive msg "+new String(message.getPayload())+" from "+topic);
                    }
                });

            }
        });
        msgClient.connect();
        for(FriendItem item:friendList){
            if(item.getUserId().equals(username)) {
                msgClient.subscribe(item.getTopicUrl());
                Log.d(TAG, "subscribe "+item.getTopicUrl());
            }
        }
        for(FriendItem item:groupList){
            msgClient.subscribe(item.getTopicUrl());
            Log.d(TAG, "subscribe "+item.getTopicUrl());
        }
    }

    public List<FriendItem> createFriendList(String username) {
        List<FriendItem> friendItems = new ArrayList<>();

        friendItems.add(new FriendItem("A", "a", "IM/test/P/a", 1));
        friendItems.add(new FriendItem("B", "b", "IM/test/P/b", 1));
        friendItems.add(new FriendItem("C", "c", "IM/test/P/c", 1));

        return friendItems;
    }

    public List<FriendItem> createGroupList(String username) {
        List<FriendItem> friendItems = new ArrayList<>();

        friendItems.add(new FriendItem("abc", "ABC", "IM/test/G/abc", 2));

        if(!TextUtils.equals("A", username)){
            friendItems.add(new FriendItem("bc", "BC", "IM/test/G/bc", 2));
        }
        if(!TextUtils.equals("B", username)){
            friendItems.add(new FriendItem("ac", "AC", "IM/test/G/ac", 2));
        }
        if(!TextUtils.equals("C", username)){
            friendItems.add(new FriendItem("ab", "AB", "IM/test/G/ab", 2));
        }

        return friendItems;
    }

}
