package com.gsl.demo.imdemo.util;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.tinyteam.im.protocol.MessageDecoder;
import com.tinyteam.im.protocol.TextMsg;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by guosenlin on 16-10-19.
 */

public class MsgCallBack implements MqttCallback {
    private static final String TAG = "MsgCallBack";
    private List<MsgClient.OnMsgReceivedListener> listeners = new ArrayList<>();
    private LocalBroadcastManager lbManager;

    public MsgCallBack(Context context) {
        lbManager = LocalBroadcastManager.getInstance(context);
    }

    public void addListener(MsgClient.OnMsgReceivedListener listener){
        listeners.add(listener);
    }

    public void removeListener(MsgClient.OnMsgReceivedListener listener){
        listeners.remove(listener) ;
    }

    @Override
    public void connectionLost(Throwable arg0) {
        Log.d(TAG, "CallBack--->Connection lost");

    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken arg0) {

        Log.d(TAG, "CallBack--->deliverComplete");
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        Log.d(TAG, "CallBack--->Message arrived");
        String time = new Date().toString();
        Log.d(TAG, "Time:\t" +time +
                "  Topic:\t" + topic +
                "  Message:\t" + new String(message.getPayload()) +
                "  QoS:\t" + message.getQos());
        Intent intent = new Intent("com.sample.receiveMsg");
        intent.putExtra("topic", topic);

        TextMsg msg = (TextMsg) MessageDecoder.decodeMsg(message.getPayload());

        intent.putExtra("content", msg.getText());
//        intent.putExtra("content", new String(message.getPayload()));
        lbManager.sendBroadcast(intent);
        for(MsgClient.OnMsgReceivedListener listener:listeners){
            listener.onMsgReceive(topic, message);
        }
    }
}
