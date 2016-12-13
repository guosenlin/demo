package com.gsl.demo.pendemo.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.gsl.demo.pendemo.MainActivity;
import com.gsl.demo.pendemo.R;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link HaveDevicesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HaveDevicesFragment extends Fragment implements AdapterView.OnItemClickListener{


    //@InjectView(R.id.list_view)
    private ListView devicesListView;

    private BaseAdapter madpater;

    private MainActivity parentActivity;

    private List<String[]> mDevices;

    private LayoutInflater mInflater;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment SearchDevicesFragment.
     */
    public static HaveDevicesFragment newInstance() {
        HaveDevicesFragment fragment = new HaveDevicesFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_devices, container, false);
        devicesListView = (ListView)root.findViewById(R.id.list_view);
        return root;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        parentActivity = (MainActivity)getActivity();
        mDevices = parentActivity.getDevices();

        mInflater = LayoutInflater.from(parentActivity);

        madpater = new DeviceAdapter();
        devicesListView.setAdapter(madpater);
        devicesListView.setOnItemClickListener(this);
    }

    class DeviceAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return mDevices.size();
        }

        @Override
        public String[] getItem(int position) {
            return mDevices.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView == null){
                convertView = mInflater.inflate(R.layout.listview_have_devices_item, null);
            }
            TextView itemTxt = ((TextView) convertView.findViewById(R.id.item_txt));
            itemTxt.setText(getItem(position)[0] + "\n[" + getItem(position)[1] + "]");
            return convertView;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String[] device = ((String[]) madpater.getItem(position));
        parentActivity.connection(device);
    }
}
