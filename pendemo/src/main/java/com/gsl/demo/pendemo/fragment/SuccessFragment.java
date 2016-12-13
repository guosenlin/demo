package com.gsl.demo.pendemo.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.gsl.demo.pendemo.MainActivity;
import com.gsl.demo.pendemo.R;
import com.squareup.picasso.Picasso;
import com.tsinghuabigdata.edu.sdk.PenConst;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 *
 */
public class SuccessFragment  extends Fragment{


    private TextView resultIDView;
    private ListView listView;

    private String rsultId;
    private String accountId;

    private TaskAdapter taskAdapter;

    private JSONArray imageJsonArray;
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment SearchDevicesFragment.
     */
    public static SuccessFragment newInstance() {
        SuccessFragment fragment = new SuccessFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_success, container, false);

        resultIDView = (TextView)root.findViewById( R.id.text_imageid );
        listView     = (ListView)root.findViewById(R.id.image_List);

        taskAdapter = new TaskAdapter();
        listView.setAdapter( taskAdapter );
        return root;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        rsultId = ((MainActivity) getActivity()).getResultId();
        accountId = ((MainActivity) getActivity()).getAccountId();

        if( !TextUtils.isEmpty(rsultId)){
            resultIDView.setText( rsultId );

            GetImageTask task = new GetImageTask();
            task.execute();
        }
    }

    class GetImageTask extends AsyncTask<Void,Integer,JSONArray> {


        /**
         * 运行在UI线程中，在调用doInBackground()之前执行
         */
        @Override
        protected void onPreExecute() {
            //
        }
        /**
         * 后台运行的方法，可以运行非UI线程，可以执行耗时的方法
         */
        @Override
        protected JSONArray doInBackground(Void... params) {

            try {
                String pathUrl = PenConst.BASE_URL +PenConst.URL_GETIMAGE + "?accountId="+ URLEncoder.encode(accountId,"utf-8")+"&exertId="+URLEncoder.encode(rsultId,"utf-8");

                // 建立连接
                URL url = new URL(pathUrl);
                HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
                httpConn.setReadTimeout(90000);// 设置超时的时间
                httpConn.setConnectTimeout(90000);// 设置链接超时的时间
                httpConn.setRequestMethod("GET");// 设置URL请求方法
                httpConn.connect();

                // 获得响应状态
                int responseCode = httpConn.getResponseCode();

                if (HttpURLConnection.HTTP_OK == responseCode) {// 连接成功
                    // 当正确响应时处理数据
                    StringBuffer sb = new StringBuffer();
                    String readLine;
                    BufferedReader responseReader;
                    // 处理响应流，必须与服务器响应流输出的编码一致
                    responseReader = new BufferedReader(new InputStreamReader(httpConn.getInputStream(), "UTF-8"));
                    while ((readLine = responseReader.readLine()) != null) {
                        sb.append(readLine).append("\n");
                    }
                    responseReader.close();

                    Log.e("XXXX", "return data = "+sb.toString() );

                    JSONObject json = new JSONObject( sb.toString() );
                    if( json.optInt("code")==10000 && json.has("data")&& json.getJSONArray("data").length()>0 ){
                        return json.getJSONArray("data");
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return null;
        }

        /**
         * 运行在ui线程中，在doInBackground()执行完毕后执行
         */
        @Override
        protected void onPostExecute(JSONArray array) {
            if( array==null || array.length()==0 )
                Toast.makeText( getContext(), "获取数据失败", Toast.LENGTH_SHORT ).show();

            imageJsonArray = array;
            taskAdapter.notifyDataSetChanged();
        }

        /**
         * 在publishProgress()被调用以后执行，publishProgress()用于更新进度
         */
        @Override
        protected void onProgressUpdate(Integer... values) {
        }
    }

    class TaskAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return imageJsonArray != null ? imageJsonArray.length() : 0;
        }

        @Override
        public JSONObject getItem(int position) {
            try{
                return imageJsonArray.getJSONObject(position);
            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from( getContext() ).inflate( R.layout.listview_image_item, parent, false);
                convertView.setTag(new ViewHolder(convertView));
            }
            ViewHolder viewHolder = (ViewHolder) convertView.getTag();

            JSONObject json = getItem( position );

            try{

                String imgpath = json.getString("imagePath");
                String name    = json.getString("accountId");

                viewHolder.textView.setText( name );
                 Picasso.with(getContext()).load( PenConst.IMAGE_FILE_SERVER + imgpath ).error( R.drawable.nobmp).into( viewHolder.imageView );
            }catch (Exception e){
                e.printStackTrace();
            }

            return convertView;
        }

        class ViewHolder {
            private ImageView imageView;
            private TextView  textView;

            public ViewHolder(View view) {
                imageView = (ImageView) view.findViewById( R.id.list_item_image );
                textView = (TextView)view.findViewById( R.id.list_item_account );
            }
        }

    }

}

