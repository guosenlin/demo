package com.gsl.demo.pendemo.fragment;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.ImageView;

import com.gsl.demo.pendemo.AlertManager;
import com.gsl.demo.pendemo.MainActivity;
import com.gsl.demo.pendemo.R;
import com.gsl.demo.pendemo.view.CircleAnimation;

import java.lang.reflect.Method;
import java.util.Set;


/**
 *
 */
public class SearchDevicesFragment  extends Fragment {


    private ImageView searchBtn;

    private BluetoothAdapter mBtAdapter;
    private BluetoothRecevie mReceiver;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment SearchDevicesFragment.
     */
    public static SearchDevicesFragment newInstance() {
        SearchDevicesFragment fragment = new SearchDevicesFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * 清空已经匹配成功的设备
     */
    private void clearDevices(){
        MainActivity activity = ((MainActivity) getActivity());
        Set<BluetoothDevice> pairedDevices = mBtAdapter.getBondedDevices();
        if(pairedDevices != null && pairedDevices.size() > 0) {
            for (BluetoothDevice pairedDevice : pairedDevices) {
                if(activity!=null&&activity.isAvailableDevice(pairedDevice.getAddress())) {
                    //activity.getDevices().add(new String[]{pairedDevice.getName(), pairedDevice.getAddress()});
                    unpairDevice(pairedDevice);
                }
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_search_devices, container, false);

        searchBtn = (ImageView)root.findViewById( R.id.search_btn );
        return root;
    }

    private void unpairDevice(BluetoothDevice device) {
        try {
            Method m = device.getClass().getMethod("removeBond");
            m.invoke(device);
        } catch (Exception e) {
            Log.i("err", e.toString());
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Get the local Bluetooth adapter
        mBtAdapter = BluetoothAdapter.getDefaultAdapter();

        // 如果没有打开蓝牙，就申请打开
        if(!mBtAdapter.isEnabled()){
            AlertManager.show(getActivity(), "是否打开蓝牙搜索设备?", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // 监听开启过程
                    mReceiver = new BluetoothRecevie();
                    IntentFilter filter = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
                    if (getActivity() != null)getActivity().registerReceiver(mReceiver, filter);
                    // enable是异步开启
                    boolean res = mBtAdapter.enable();
                    Log.i(MainActivity.LOG_TAG, "申请打开蓝牙 " + res);
                    if (!res) {
                        Log.i(MainActivity.LOG_TAG,"蓝牙请求授权被用户拒绝或者用户搜索过程中关闭了蓝牙");
                        if (getActivity() != null) {
                            AlertManager.toast(getActivity(), "您拒绝了蓝牙权限");
                            getActivity().finish();
                        }
                    }
                }
            }, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (getActivity() != null)getActivity().finish();
                }
            });
        } else{
            bindPen();
        }
    }

    private void bindPen() {
        // 清空已绑定的设备
        clearDevices();

        // 开始搜索设备
        // 兼容学生绑定笔，如果已绑定则不用搜索直接连接，否则需要搜索
        //TODO

        if( ((MainActivity)getActivity()).isBindPen()) {
            ((MainActivity)getActivity()).startConnection();
        } else {
            openSearch();
        }
    }


    private void openSearch() {

        startSearch();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startAnimation();
            }
        }, 500);
    }

    private void startAnimation(){
        Animation animation = new CircleAnimation(30);
        animation.setDuration(2000);
        animation.setRepeatCount(Animation.INFINITE);
//        animation.setInterpolator(new LinearInterpolator());
        searchBtn.startAnimation(animation);
    }

    private void startSearch(){
        if(!mBtAdapter.isDiscovering()) {
            mBtAdapter.startDiscovery();
            Log.i(MainActivity.LOG_TAG, "开始蓝牙扫描...");
        }

    }
    private void stopSearch(){
        if(mBtAdapter.isDiscovering()) {
            mBtAdapter.cancelDiscovery();
            Log.i(MainActivity.LOG_TAG, "停止蓝牙扫描...");
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        stopSearch();
        if(mReceiver != null&&getActivity()!=null) {
            getActivity().unregisterReceiver(mReceiver);
            mReceiver = null;
        }
    }

    class BluetoothRecevie extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            int cstate;
            cstate = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, -1);
            // 蓝牙开启成功
            if(cstate == BluetoothAdapter.STATE_ON){
                bindPen();
            } else if (cstate == BluetoothAdapter.STATE_OFF) {
                Log.i(MainActivity.LOG_TAG, "蓝牙请求授权被用户拒绝或者用户搜索过程中关闭了蓝牙");
                if (getActivity() != null) {
                    AlertManager.toast(getActivity(), "您拒绝了蓝牙权限或者蓝牙断开了");
                    AlertManager.toast(getActivity(), "蓝牙已被关闭");
                    getActivity().finish();
                }
            }
        }

    }

}
