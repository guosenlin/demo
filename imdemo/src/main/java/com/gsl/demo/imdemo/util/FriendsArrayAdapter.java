package com.gsl.demo.imdemo.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.gsl.demo.imdemo.R;
import com.gsl.demo.imdemo.bean.FriendItem;

import java.util.List;

/**
 * Created by guosenlin on 16-10-18.
 */

public class FriendsArrayAdapter extends ArrayAdapter {
    private LayoutInflater mLayoutInflater;
    private int mResource;
    private List<FriendItem> mFriendList;

    public FriendsArrayAdapter(Context context, int resource, List<FriendItem> objects) {
        super(context, resource, objects);

        mResource = resource;
        mFriendList = objects;
        mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        FriendItemHolder holder = null;

        if(convertView==null){
            holder = new FriendItemHolder();

            convertView = mLayoutInflater.inflate(mResource, null);
            holder.nickname = (TextView) convertView.findViewById(R.id.tv_friend_nickname);
            holder.topic = (TextView) convertView.findViewById(R.id.tv_friend_topic);
            convertView.setTag(holder);
        }else{
            holder = (FriendItemHolder)convertView.getTag();
        }

        FriendItem item = mFriendList.get(position);
        holder.nickname.setText(item.getNickName());
        holder.topic.setText(item.getTopicUrl());

        return convertView;
    }

    private class FriendItemHolder {
        public TextView nickname;
        public TextView topic;
    }
}
