package com.henghao.hhworkpresent.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.henghao.hhworkpresent.ProtocolUrl;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;

/**
 * Created by bryanrady on 2017/4/24.
 *
 * 测试车辆经纬度上传
 */

public class CheliangService extends Service {

    private UploadLatLonThread uploadLatLonThread;
    // 定位相关声明
    public LocationClient locationClient = null;
    private String latitude;
    private String longitude;
    private String chepaihao;

    @Override
    public void onCreate() {
        super.onCreate();
        uploadLatLonThread = new UploadLatLonThread();
        initData();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        chepaihao = intent.getStringExtra("chepaihao");
        if (!uploadLatLonThread.isRunning) {
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
                    httpUploadLatLon();
                    Thread.sleep(10000);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }



    /**
     * 调用接口上传经纬度
     */
    public void httpUploadLatLon(){
        OkHttpClient okHttpClient = new OkHttpClient();
        Request.Builder builder = new Request.Builder();
        FormEncodingBuilder requestBodyBuilder = new FormEncodingBuilder();
        if(latitude == null|| longitude ==null){
            return;
        }
        requestBodyBuilder.add("car_num", chepaihao);
        requestBodyBuilder.add("car_latitude", latitude);
        requestBodyBuilder.add("car_longitude", longitude);
        RequestBody requestBody = requestBodyBuilder.build();
        String request_url = ProtocolUrl.ROOT_URL + "/"+ "/statisticsInfo/carTest/carItude";
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

    @Override
    public void onDestroy() {
        uploadLatLonThread.isRunning = false;
        this.locationClient.stop();
        super.onDestroy();
    }

}
