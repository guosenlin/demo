package com.gsl.demo.pendemo;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;

import com.gsl.demo.pendemo.fragment.ConnectionedDevicesFragment;
import com.gsl.demo.pendemo.fragment.HaveDevicesFragment;
import com.gsl.demo.pendemo.fragment.MatchingFailureFragment;
import com.gsl.demo.pendemo.fragment.SearchDevicesFragment;
import com.gsl.demo.pendemo.fragment.SuccessFragment;
import com.tsinghuabigdata.edu.sdk.ConnectionListener;
import com.tsinghuabigdata.edu.sdk.ConnectionManager;
import com.tsinghuabigdata.edu.sdk.PenConst;
import com.tsinghuabigdata.edu.sdk.UploadManager;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

	public static final String LOG_TAG = "sdklog";
	private static final int MAX_DEVICES_SIZE = 10;
	private static final int MAX_SEARCH_TIME  = 10*1000;

	private ConnectionManager connectionManager;
	private UploadManager uploadManager;

	// 句柄
	private int mHWnd = 0x01;

	private Context mContext;

	private String resultId;
	/**
	 * 是否绑定笔
	 */
	private String bindDevice = "";

	private String accountId = "ysxs16";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mContext = this;

		progressDialog = new ProgressDialog(this);
		progressDialog.setMessage("请耐心等待...");
		progressDialog.setCancelable(false);

		registerReceiver();

		switchSearchFramgent();

		//服务连接管理器
		connectionManager = new ConnectionManager(this.getApplicationContext(), new ConnectionListener() {
			@Override
			public void onDisconnected() {
			}

			@Override
			public void onConnected(UploadManager manager) {
				//获取服务连接句柄
				uploadManager = manager;
				Log.i( LOG_TAG, "连接penSdk服务成功" );
			}
		});



		connectionManager.connect();

		//注册状态回调监听
		registPenSdkReveiver();

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver();
		removeMessage();

		connectionManager.disconnect();
	}

	//--------------------------------------------------------------
	// 处理已搜索到的蓝牙设备
	private Handler mhandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			try {
				if (mDevices.size() == 0) {
					Log.e(LOG_TAG, " 没有搜索到的蓝牙设备  ");
					switchMatchingFailureFramgent();
				} else {
					switchHanDeviceFramgent();
				}
			} catch (Exception ex) {
				Log.w( "BLUETOOTH", "err", ex);
			}
		}
	};

	// 搜到到的设备
	private List<String[]> mDevices = new ArrayList<String[]>(10) {
		@Override
		public boolean add(String[] object) {
			String[] device = object;
			// 判断是否重复搜索
			for (String[] item : this) {
				if (item[1].equals(device[1])) {
					// 如果设备名称为null或者空字符，就将重复搜索到的设备名称赋予它
					if (item[0] == null || item.equals("")) {
						item[0] = device[0];
					}
					// 重复返回假
					return false;
				}
			}
			// 没有重复的调用父类Add
			return super.add(object);
		}

	};

	private String[] connectionedDevice;

	private ProgressDialog progressDialog;

	private void registerReceiver() {
		Log.i(LOG_TAG, "启动蓝牙监听");
		IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
		registerReceiver(mReceiver, filter);

		filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
		registerReceiver(mReceiver, filter);
	}

	private void unregisterReceiver() {
		Log.i(LOG_TAG, "删除蓝牙监听");
		unregisterReceiver(mReceiver);
	}

	public void switchSearchFramgent() {
		Log.i(LOG_TAG, "开启搜索设备");
		mDevices.clear();
		getSupportFragmentManager().beginTransaction().replace( R.id.fragment,
				SearchDevicesFragment.newInstance()).commitAllowingStateLoss();
	}

	public void switchHanDeviceFramgent() {
		getSupportFragmentManager().beginTransaction().replace(R.id.fragment, HaveDevicesFragment
				.newInstance()).commitAllowingStateLoss();
	}

	private ConnectionedDevicesFragment connectionedDevicesFragment;
	public void switchConnectionedDeviceFramgent() {
		connectionedDevicesFragment = ConnectionedDevicesFragment.newInstance();
		getSupportFragmentManager().beginTransaction().replace( R.id.fragment, connectionedDevicesFragment ).commitAllowingStateLoss();
	}

	public void switchMatchingFailureFramgent() {
		getSupportFragmentManager().beginTransaction().replace(R.id.fragment,
				MatchingFailureFragment.newInstance()).commitAllowingStateLoss();
	}


	public String getBindDevice() {
		return bindDevice;
	}

	public boolean isBindPen() {
		return !TextUtils.isEmpty(bindDevice);
	}

	public String getResultId(){ return resultId; }
	public String getAccountId(){
		return accountId;
	}

	public void switchSuccessFramgent( String resId ) {
		resultId = resId;

		Log.e( LOG_TAG, "switchSuccessFramgent   resultId = " + resultId );
		getSupportFragmentManager().beginTransaction().replace(R.id.fragment,
				SuccessFragment.newInstance()).commitAllowingStateLoss();
	}

	private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
		@TargetApi(Build.VERSION_CODES.ECLAIR)
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			Log.i(LOG_TAG, "receive " + action);
			// When discovery finds a device
			if (BluetoothDevice.ACTION_FOUND.equals(action)) {
				//扫描到设备
				// Get the BluetoothDevice object from the Intent
				BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

				// Get rssi value
				short rssi = intent.getShortExtra(BluetoothDevice.EXTRA_RSSI, Short.MIN_VALUE);
				Log.i(LOG_TAG, "bluetooth search mac=" + device.getAddress());
				// If it's already paired, skip it, because it's been listed already
				if ( device.getBondState() != BluetoothDevice.BOND_BONDED && isAvailableDevice(device.getAddress())) {
					Log.i(LOG_TAG, device.getName() + "\n" + "[RSSI : " + rssi +  "dBm] " + device.getAddress());
					mDevices.add(new String[]{device.getName(), device.getAddress()});
				}
				//超过搜索大小
				if (mDevices.size() >= MAX_DEVICES_SIZE) {
					Log.i(LOG_TAG, "超过搜索大小");
					removeMessage();
					mhandler.obtainMessage().sendToTarget();
				}
			} else if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)) {
				// 开始扫描，超时后自定进入匹配列表界面
				Log.i(LOG_TAG, "超时后自定进入匹配列表界面");
				mhandler.sendEmptyMessageDelayed(mHWnd, MAX_SEARCH_TIME);
			}
		}
	};

	public List<String[]> getDevices() {
		return mDevices;
	}

	public String[] getConnectionedDevice() {
		return connectionedDevice;
	}

	public void startConnection(){

		if( uploadManager!=null ){
			try{
				Log.i( LOG_TAG, "开始进行智能笔的蓝牙连接" );
				uploadManager.connection( bindDevice, accountId, mContext );
			}catch ( Exception e){
				e.printStackTrace();
			}
		}
	}

	public void connection(String[] device) {
		connectionedDevice = device;
		showProgress();

		if( uploadManager!=null ){
			try{
				Log.i( LOG_TAG, "开始进行智能笔的蓝牙连接" );
				uploadManager.connection( device[1], accountId, mContext );
			}catch ( Exception e){
				e.printStackTrace();
			}
		}
	}

	private void showProgress() {
		if ( !TextUtils.isEmpty(bindDevice) ) {
			progressDialog.show();
		}
	}

	private void hideProgress() {
		if (!TextUtils.isEmpty(bindDevice)) {
			progressDialog.cancel();
		}
	}

	private void removeMessage() {
		if (mhandler.hasMessages(mHWnd)) {
			mhandler.removeMessages(mHWnd);
		}
	}

	/**
	 * 是否是智能笔
	 *
	 * @param mac
	 * @return
	 **/
	public boolean isAvailableDevice(String mac) {
		return mac.startsWith("9C:7B:D2") && !mac.startsWith("9C:7B:D2:01");
	}



	/**
	 * 注册接收Sdk的通知
	 */
	private void registPenSdkReveiver() {
		Log.e(LOG_TAG, "  注册接收Sdk的通知  ");
		PenSDKReceiver receiver = new PenSDKReceiver();
		IntentFilter intentFilter = new IntentFilter( PenConst.PEN_ACTION_BROADCAST );
		mContext.registerReceiver(receiver, intentFilter);
	}


	class PenSDKReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {

			int action = intent.getIntExtra(PenConst.ACTION_RESPONSR, 0);

			switch (action) {

				case PenConst.Response.ACTION_CONNECTION_STATUS:

					boolean status = intent.getBooleanExtra(PenConst.ACTION_STATUS, false);
					Log.e(LOG_TAG, "  蓝牙连接 状态 =  " + status);
					if (status) {
						//
						hideProgress();
						switchConnectionedDeviceFramgent();
					} else {
						//
						hideProgress();
						switchMatchingFailureFramgent();
					}
					break;
				case PenConst.Response.ACTION_UPLOADSUCCESS:

					Log.e(LOG_TAG, "UPLOAD_SUCCESS   数据上传成功 ");
					final String resid = intent.getStringExtra(PenConst.SUCCESS_RES_ID);
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							switchSuccessFramgent(resid);
						}
					});
					break;
				case PenConst.Response.ACTION_UPLOADERROR: {
					final String msg = intent.getStringExtra(PenConst.ERROR_MSG);
					runOnUiThread(new Runnable() {
						@Override
						public void run() {

							if( connectionedDevicesFragment!=null )
								connectionedDevicesFragment.appendMsg( msg );
						}
					});
					break;
				}

				case PenConst.Response.ACTION_PROGRESS: {
					final String msg = intent.getStringExtra(PenConst.PROGRESS_MSG);
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							if( connectionedDevicesFragment!=null )
								connectionedDevicesFragment.appendMsg( msg );
						}
					});
					break;
				}
			}
		}
	}
}

