package com.gsl.demo.pendemo.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.gsl.demo.pendemo.MainActivity;
import com.gsl.demo.pendemo.R;

/**
 * Created by Administrator on 2016/12/5.
 */
public class MatchingFailureFragment extends Fragment implements View.OnClickListener {


    private Button gotoBtn;
    private TextView connectionTxt;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment SearchDevicesFragment.
     */
    public static MatchingFailureFragment newInstance() {
        MatchingFailureFragment fragment = new MatchingFailureFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_matching_failure, container, false);

        gotoBtn = (Button)root.findViewById( R.id.goto_btn );
        connectionTxt = (TextView)root.findViewById( R.id.connection_txt );
        return root;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        gotoBtn.setOnClickListener(this);

        /*String[] device = ((BoundDeviceListActivity) getActivity()).getConnectionedDevice();
        if(device != null && device.length > 1)
            connectionTxt.setText(device[0]);*/
    }

    @Override
    public void onClick(View v) {
        ((MainActivity) getActivity()).switchSearchFramgent();
    }
}

