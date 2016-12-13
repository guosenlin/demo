package com.gsl.demo.imdemo.util;

import android.content.Context;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by guosenlin on 16-10-19.
 */

public class MsgClientFactory {
    private static Map<String, MsgClient> map = new HashMap<>();

    public static MsgClient createMsgClient(Context context, String clientId, String broker, String user, String passwd, int qos){
        MsgClient client = new MsgClient(context, clientId, broker, user, passwd, qos);
        map.put(clientId, client);
        return client;
    }

    public static MsgClient getMsgClient(String clientId){
        return map.get(clientId);
    }

}
