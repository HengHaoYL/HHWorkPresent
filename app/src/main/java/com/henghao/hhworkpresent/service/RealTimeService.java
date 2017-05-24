package com.henghao.hhworkpresent.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.sqlite.SQLiteDatabase;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.henghao.hhworkpresent.Constant;
import com.henghao.hhworkpresent.ProtocolUrl;
import com.henghao.hhworkpresent.utils.SqliteDBUtils;
import com.henghao.hhworkpresent.views.DatabaseHelper;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 用来实时上传经纬度的服务 45秒上传一次
 * Created by bryanrady on 2017/4/20.
 */

public class RealTimeService extends Service {

    private DatabaseHelper dbHelper;
    private SQLiteDatabase db;

    private UploadLatLonThread uploadLatLonThread;
    // 定位相关声明
    public LocationClient locationClient = null;
    private String latitude;
    private String longitude;

    private MyReceiver myReceiver;
    private SqliteDBUtils sqliteDBUtils;

    @Override
    public void onCreate() {
        super.onCreate();
        dbHelper = new DatabaseHelper(RealTimeService.this,"user_login.db");
        db = dbHelper.getReadableDatabase();
        uploadLatLonThread = new UploadLatLonThread();
        myReceiver = new MyReceiver();
        sqliteDBUtils = new SqliteDBUtils(this);
        initData();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (!uploadLatLonThread.isRunning) {
            IntentFilter filter = new IntentFilter();
            filter.addAction(Constant.STOP_REALTIMESERVICE);
            registerReceiver(myReceiver,filter);

            uploadLatLonThread.isRunning = true;
            uploadLatLonThread.start();
            this.locationClient.start(); // 开始定位
        }
        return super.onStartCommand(intent, flags, startId);
    }

    public void initData(){
        /**
         * 定位
         */
        this.locationClient = new LocationClient(getApplicationContext()); // 实例化LocationClient类
        this.locationClient.registerLocationListener(this.myListener); // 注册监听函数
        this.setLocationOption(); // 设置定位参数
        this.locationClient.start(); // 开始定位
    }

    /**
     * 设置定位参数
     */
    private void setLocationOption() {
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true); // 打开GPS
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);// 设置定位模式
        option.setCoorType("bd09ll"); // 返回的定位结果是百度经纬度,默认值gcj02
        option.setScanSpan(5000); // 设置发起定位请求的间隔时间为5000ms
        option.setIsNeedAddress(true); // 返回的定位结果包含地址信息
        option.setNeedDeviceDirect(true); // 返回的定位结果包含手机机头的方向
        this.locationClient.setLocOption(option);
    }

    /**
     * 开辟线程上传经纬度
     */
    class UploadLatLonThread extends Thread{

        public boolean isRunning = false;

        @Override
        public void run() {
            while (isRunning) {
                try{
                    httpRequest();
                    httpUploadLatLon();
                    Thread.sleep(45000);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 查询出勤率
     */
    private void httpRequest(){
        Date date1 = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
        String date = format.format(date1);
        OkHttpClient okHttpClient = new OkHttpClient();
        Request.Builder builder = new Request.Builder();
        FormEncodingBuilder requestBodyBuilder = new FormEncodingBuilder();
        requestBodyBuilder.add("userId", sqliteDBUtils.getLoginUid());
        requestBodyBuilder.add("date", date);
        RequestBody requestBody = requestBodyBuilder.build();
        String request_url = ProtocolUrl.ROOT_URL + "/"+ ProtocolUrl.APP_QUERY_MOUNTH_KAOQING;
        Request request = builder.url(request_url).post(requestBody).build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
            }

            @Override
            public void onResponse(Response response) throws IOException {
                String result_str = response.body().string();
                try {
                    JSONObject jsonObject = new JSONObject(result_str);
                    int status = jsonObject.getInt("status");
                    if (status == 0) {
                    }
                    JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                    chuqinglv = jsonObject1.optString("chuqingli");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private String chuqinglv = null;

    /**
     * 调用接口上传经纬度/出勤率
     */
    public void httpUploadLatLon(){
        OkHttpClient okHttpClient = new OkHttpClient();
        Request.Builder builder = new Request.Builder();
        FormEncodingBuilder requestBodyBuilder = new FormEncodingBuilder();
        if(latitude == null|| longitude ==null){
            return;
        }
        requestBodyBuilder.add("uid", sqliteDBUtils.getLoginUid());
        requestBodyBuilder.add("latitude", latitude);
        requestBodyBuilder.add("longitude", longitude);
        requestBodyBuilder.add("attendance", chuqinglv);
        RequestBody requestBody = requestBodyBuilder.build();
        String request_url = ProtocolUrl.ROOT_URL + "/"+ ProtocolUrl.APP_REALTIME_UPLOAD_LATLON;
        Request request = builder.url(request_url).post(requestBody).build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
            }

            @Override
            public void onResponse(Response response) throws IOException {
            }
        });
    }

    public BDLocationListener myListener = new BDLocationListener() {

        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            if (bdLocation == null) {
                return;
            }
            latitude = String.valueOf(bdLocation.getLatitude());
            longitude = String.valueOf(bdLocation.getLongitude());
        }

        @Override
        public void onConnectHotSpotMessage(String s, int i) {

        }
    };


    class MyReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if((Constant.STOP_REALTIMESERVICE).equals(action)){
                Intent mIntent = new Intent(context,RealTimeService.class);
                stopService(mIntent);

            }
        }
    }

    @Override
    public void onDestroy() {
        uploadLatLonThread.isRunning = false;
        this.locationClient.stop();
        unregisterReceiver(myReceiver);
        super.onDestroy();
    }

}
