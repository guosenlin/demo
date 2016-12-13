package com.gsl.demo.pendemo.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gsl.demo.pendemo.MainActivity;
import com.gsl.demo.pendemo.R;


/**
 *
 */
public class ConnectionedDevicesFragment extends Fragment{


    private TextView messageTxt;
    private TextView connectionTxt;

    private boolean closed;



    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment SearchDevicesFragment.
     */
    public static ConnectionedDevicesFragment newInstance() {
        ConnectionedDevicesFragment fragment = new ConnectionedDevicesFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_connectioned_devices, container, false);

        messageTxt = (TextView)root.findViewById( R.id.message_txt );
        connectionTxt = (TextView)root.findViewById(R.id.connection_txt);
        return root;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        String[] device = ((MainActivity) getActivity()).getConnectionedDevice();
        if(device != null && device.length > 1)
            connectionTxt.setText("已与" + device[0] + "智能笔配对成功!");

    }

    public void appendMsg( final String msg ){
        messageTxt.append( msg );
        messageTxt.append( "\n" );
    }
}
