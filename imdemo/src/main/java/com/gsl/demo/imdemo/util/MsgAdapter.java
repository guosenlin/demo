package com.gsl.demo.imdemo.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.gsl.demo.imdemo.R;
import com.gsl.demo.imdemo.bean.Msg;

import java.util.List;

/**
 * Created by guosenlin on 16-10-18.
 */

public class MsgAdapter extends BaseAdapter {
    private List<Msg> mMsgList;
    private LayoutInflater mInflater;

    public MsgAdapter(Context context, List<Msg> data){
        mMsgList = data;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return mMsgList.size();
    }

    @Override
    public Object getItem(int i) {
        return mMsgList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public int getItemViewType(int position) {
        return mMsgList.get(position).getType();
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        MsgHolder holder;
        if(null==view){
            holder = new MsgHolder();
            if (getItemViewType(i)==Msg.MSG_RECEIVE) {
                view = mInflater.inflate(R.layout.msg_left, null);
                holder.header = (ImageView) view.findViewById(R.id.iv_msg_head_left);
                holder.content = (TextView)view.findViewById(R.id.tv_msg_left);
            }else {
                view = mInflater.inflate(R.layout.msg_right, null);
                holder.header = (ImageView) view.findViewById(R.id.iv_msg_head_right);
                holder.content = (TextView)view.findViewById(R.id.tv_msg_right);
            }

            view.setTag(holder);

        }else{
            holder = (MsgHolder)view.getTag();
        }

        holder.content.setText(mMsgList.get(i).getContent());

        return view;
    }



    public class MsgHolder{
        public ImageView header;
        public TextView content;
    }
}
