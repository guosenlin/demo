package com.gsl.demo.imdemo.util;

import android.content.Context;
import android.util.Log;

import com.tinyteam.im.protocol.MessageEncoder;
import com.tinyteam.im.protocol.PlateformMessage;
import com.tinyteam.im.protocol.TextMsg;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

/**
 * Created by guosenlin on 16-10-19.
 */

public class MsgClient {
    private static final String TAG = "MsgClient";
    private int qos = 0;
    private String broker = "tcp://192.168.100.29:1883";
    private String clientId = "Demo-publisher01";
    private String user = "user2";
    private String passwd = "passwd2";
    private MqttClient client;
    private MsgCallBack msgCallBack;
    private Context mContext;

    public MsgClient(Context context, String clientId, String broker, String user, String passwd, int qos) {
        this.qos = qos;
        this.broker = broker;
        this.clientId = clientId;
        this.user = user;
        this.passwd = passwd;
        this.mContext = context;
        msgCallBack = new MsgCallBack(context);
    }

    public void connect(){
        MemoryPersistence persistence = new MemoryPersistence();

        try {
            client = new MqttClient(broker, clientId, persistence);
            client.setCallback(msgCallBack);
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(true);
            //connOpts.setConnectionTimeout(timeout);
            connOpts.setKeepAliveInterval(0);
            Log.d(TAG, "Connecting to broker: " + broker);

            connOpts.setUserName(user);
            connOpts.setPassword(passwd.toCharArray());
            client.connect(connOpts);
            Log.d(TAG, "Connected");


        } catch(MqttException me) {
            Log.d(TAG, me.getMessage());
            me.printStackTrace();
        }
    }

    public void addOnMsgReceivedListener(OnMsgReceivedListener listener){
        msgCallBack.addListener(listener);
    }

    public void subscribe(String topic) {

        try{
            client.subscribe(topic, qos);
            Log.d(TAG, "Subscribed");
        } catch(MqttException me) {
            Log.d(TAG, me.getMessage());
            me.printStackTrace();
        }

    }

    public void sendMsg(String topic, PlateformMessage msg) {
        try {

            MqttMessage message = new MqttMessage(MessageEncoder.encodeText((TextMsg) msg));
            message.setQos(qos);
            client.publish(topic, message);
            Log.d(TAG, "Message published");
        } catch(MqttException me) {
            Log.d(TAG, me.getMessage());
            me.printStackTrace();
        }

    }

    public void close(){
        try {
            client.disconnect();
            Log.d(TAG, "Disconnected");
        } catch (MqttException e) {
            Log.d(TAG, e.getMessage());
            e.printStackTrace();
        }
    }

    public interface OnMsgReceivedListener{
       void onMsgReceive(String topic, MqttMessage message);
    }
}
