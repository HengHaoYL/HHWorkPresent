package com.henghao.hhworkpresent.activity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.benefit.buy.library.utils.tools.ToolsKit;
import com.henghao.hhworkpresent.ActivityFragmentSupport;
import com.henghao.hhworkpresent.R;
import com.henghao.hhworkpresent.views.ImageTextButton;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by bryanrady on 2017/3/1.
 *
 * 外勤签到界面
 */

public class WaiQingQDActivity extends ActivityFragmentSupport {

    private static int QIANDAO_REQUEST = 100;

    private int count = 0;// 签到次数

    // 定位相关声明
    public LocationClient locationClient = null;

    boolean isFirstLoc = true;// 是否首次定位

    /**
     * 签到的时间（年月日）
     */
    @ViewInject(R.id.tv_time_qiandao)
    private TextView tv_time_qiandao;
    /**
     * 当前企业
     */
    @ViewInject(R.id.tv_company_qiandao)
    private TextView tv_company_qiandao;
    /**
     * 签到的地点
     */
    @ViewInject(R.id.tv_place_name)
    private TextView tv_place_qiandao;
    /**
     * 签到的具体时间（时分）
     */
    @ViewInject(R.id.tv_hourminute_qiandao)
    private TextView tv_hourminute_qiandao;
    /**
     * 签到的状态（当前已签到或未签到）
     */
    @ViewInject(R.id.tv_state_qiandao)
    private TextView tv_state_qiandao;
    /**
     * 签到
     */
    @ViewInject(R.id.img_qiandao)
    private ImageView img_qiandao;
    /**
     * 签到打勾
     */
    @ViewInject(R.id.img_confirm_qiandao)
    private ImageView img_confirm_qiandao;

    @ViewInject(R.id.waiqingqd_choose)
    private ImageTextButton imageTextButton;

    // 百度地图控件
    private MapView mMapView = null;
    // 百度地图对象
    private BaiduMap mBaiduMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*try{
            System.loadLibrary("liblocSDK7a");
        }catch (UnsatisfiedLinkError localUnsatisfiedLinkError){
            localUnsatisfiedLinkError.printStackTrace();
        }*/

        // 在使用SDK各组件之前初始化context信息，传入ApplicationContext
        // 注意该方法要再setContentView方法之前实现
        SDKInitializer.initialize(getApplicationContext());
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.mActivityFragmentView.viewMain(R.layout.activity_waiqingqd);
        this.mActivityFragmentView.viewEmpty(R.layout.activity_empty);
        this.mActivityFragmentView.viewEmptyGone();
        this.mActivityFragmentView.viewLoading(View.GONE);
        this.mActivityFragmentView.clipToPadding(true);
        ViewUtils.inject(this, this.mActivityFragmentView);
        setContentView(this.mActivityFragmentView);
        initWidget();
        initData();
    }

    @Override
    public void initWidget() {
        super.initWidget();
        initWithBar();
        mLeftTextView.setText("外勤签到");
        mLeftTextView.setVisibility(View.VISIBLE);
        mLeftImageView.setVisibility(View.VISIBLE);
        mLeftImageView.setImageResource(R.drawable.item_point_left);
        initWithRightBar();
        mRightTextView.setVisibility(View.VISIBLE);
        mRightTextView.setText("");
        mRightImageView.setVisibility(View.VISIBLE);
        mRightImageView.setImageResource(R.drawable.item_wenhao);

        imageTextButton.setItemTextResource("选择");
        imageTextButton.setItemImageResource(R.drawable.item_choose);

        mMapView = (MapView) findViewById(R.id.bmapview);
        mBaiduMap = mMapView.getMap();
        //普通地图
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);

        //开启交通图
        mBaiduMap.setTrafficEnabled(true);

        //将显示位置的功能开启
        mBaiduMap.setMyLocationEnabled(true);

        mMapView.showZoomControls(false);
        mMapView.showScaleControl(false);

    }

    @Override
    public void initData() {
        super.initData();
        /**
         * 定位
         */
        this.locationClient = new LocationClient(getApplicationContext()); // 实例化LocationClient类
        this.locationClient.registerLocationListener(this.myListener); // 注册监听函数
        this.setLocationOption(); // 设置定位参数
        this.locationClient.start(); // 开始定位

        /**
         * 设置签到时间（年月日）
         */
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日");
        String time = dateFormat.format(date);
        this.tv_time_qiandao.setText(time);
        /**
         * 设置签到具体时间（时、分）
         */
        dateFormat = new SimpleDateFormat("HH:mm");
        time = dateFormat.format(date);
        this.tv_hourminute_qiandao.setText(time);

    }

    private double latitude;
    private double longitude;

    public BDLocationListener myListener = new BDLocationListener() {
        @Override
        public void onReceiveLocation(BDLocation location) {
            // map view 销毁后不在处理新接收的位置
            if (location == null) {
                return;
            }
            latitude = location.getLatitude();
            longitude = location.getLongitude();
            String addrStr = location.getAddrStr();
            System.out.println("经度：" + longitude + "，纬度：" + latitude);

            showMapLabel(latitude,longitude);

            /**
             * 如果GPS未打开且无网络
             */
            Log.d("!checkNetworkState()",!checkNetworkState()+"");
            if (!checkNetworkState()) {
                WaiQingQDActivity.this.tv_place_qiandao.setText("没有定位信息！");
                addrStr = null;
                // Toast.makeText(QiandaoActivity.this,
                // "对不起，获取不到当前的地理位置！请开启GPS和网络", Toast.LENGTH_SHORT).show();
            }
            if (ToolsKit.isEmpty(addrStr)) {
                WaiQingQDActivity.this.tv_place_qiandao.setText("没有定位信息！");
                WaiQingQDActivity.this.img_qiandao.setImageResource(R.drawable.icon_grayciecle);
                WaiQingQDActivity.this.img_qiandao.setClickable(false);
                return;
            }
            WaiQingQDActivity.this.tv_place_qiandao.setText(addrStr);
            WaiQingQDActivity.this.img_qiandao.setClickable(true);
            WaiQingQDActivity.this.img_qiandao.setImageResource(R.drawable.icon_orangecircle);
        }

        @Override
        public void onConnectHotSpotMessage(String s, int i) {

        }
    };

    /**
     * 根据定位得到的经纬度信息显示位置在地图上
     */
    private void showMapLabel(double latitude,double longitude){
        // 定义Maker坐标点
        LatLng point = new LatLng(latitude, longitude);
        // 构建Marker图标
        BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.drawable.item_position);
        // 构建MarkerOption，用于在地图上添加Marker
        OverlayOptions option = new MarkerOptions().position(point).icon(bitmap);
        // 在地图上添加Marker，并显示
        mBaiduMap.addOverlay(option);
        MapStatusUpdate ms = MapStatusUpdateFactory.newLatLngZoom(point, 16.0f);
        mBaiduMap.animateMapStatus(ms);
    }

    /**
     * 检测网络是否连接
     * @return
     */
    private boolean checkNetworkState() {
        boolean flag = false;
        // 得到网络连接信息
        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        // 去进行判断网络是否连接
        if (manager.getActiveNetworkInfo() != null) {
            flag = manager.getActiveNetworkInfo().isAvailable();
        }
        return flag;
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

    @OnClick({
            R.id.img_qiandao
    })
    private void viewClick(View v) {
        switch (v.getId()) {
            // 点击签到
            case R.id.img_qiandao:
                String time = this.tv_hourminute_qiandao.getText().toString();// 签到时间
                String address = this.tv_place_qiandao.getText().toString(); // 签到地址
                String company = this.tv_company_qiandao.getText().toString();// 当前企业
                if (address.equals("没有定位信息！")) {
                    Toast.makeText(WaiQingQDActivity.this, "当前没有定位，请定位后再签到！", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent = new Intent(WaiQingQDActivity.this, QiandaoSubmitActivity.class);
                intent.putExtra("time", time);
                intent.putExtra("lat", latitude);
                intent.putExtra("lon", longitude);
                intent.putExtra("address", address);
                intent.putExtra("company", company);
                startActivityForResult(intent, QIANDAO_REQUEST);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        if (requestCode == QIANDAO_REQUEST && resultCode == RESULT_OK) {
            this.count++;
            this.img_confirm_qiandao.setVisibility(View.VISIBLE);
            this.tv_state_qiandao.setText("今日你已签到" + this.count + "次");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        this.locationClient.start(); // 开始定位
        mMapView.onResume();
    }

    // 三个状态实现地图生命周期管理
    @Override
    protected void onDestroy() {
        // 退出时销毁定位
        this.locationClient.stop();
        mMapView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onPause() {
        super.onPause();
        this.locationClient.stop();
        mMapView.onPause();
    }

}
