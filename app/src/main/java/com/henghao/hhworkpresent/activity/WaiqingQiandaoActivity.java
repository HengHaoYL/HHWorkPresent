package com.henghao.hhworkpresent.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.LocationClient;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.CoordinateConverter;
import com.henghao.hhworkpresent.ActivityFragmentSupport;
import com.henghao.hhworkpresent.ProtocolUrl;
import com.henghao.hhworkpresent.R;
import com.henghao.hhworkpresent.utils.LocationUtils;
import com.henghao.hhworkpresent.utils.SqliteDBUtils;
import com.henghao.hhworkpresent.views.MyImageTextButton;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
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
 * Created by bryanrady on 2017/3/1.
 *
 * 外勤签到界面
 */

public class WaiqingQiandaoActivity extends ActivityFragmentSupport {

    private static int QIANDAO_REQUEST = 100;

    private static final int REQUEST_PLACE_CHANGE = 0x01;

    private int morningCount,afterCount;// 签到次数

    // 定位相关声明
    public LocationClient locationClient = null;

    /*@ViewInject(R.id.waiqing_layout)
    private RelativeLayout waiqing_layout;*/

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
     * 地点微调
     **/
    @ViewInject(R.id.tv_place_weitiao)
    private TextView tv_location_trim;

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

    //判断上班打卡还是下班打卡
    @ViewInject(R.id.tv_qiandao)
    private TextView tv_qiandao;

    @ViewInject(R.id.qiandao_layout)
    private RelativeLayout qiandao_layout;


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
    private MyImageTextButton imageTextButton;

    // 百度地图控件
    private MapView mMapView = null;
    // 百度地图对象
    private BaiduMap mBaiduMap;

    private Marker mCurrentMarker;
    private double latitude;
    private double longitude;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 在使用SDK各组件之前初始化context信息，传入ApplicationContext
        // 注意该方法要再setContentView方法之前实现
        SDKInitializer.initialize(getApplicationContext());
        LocationUtils.Location(getApplicationContext());
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.mActivityFragmentView.viewMain(R.layout.activity_waiqingqd);
        this.mActivityFragmentView.viewEmpty(R.layout.activity_empty);
        this.mActivityFragmentView.viewEmptyGone();
        this.mActivityFragmentView.viewLoading(View.GONE);
    //    this.mActivityFragmentView.viewLoadingError(View.GONE);
        this.mActivityFragmentView.clipToPadding(true);
        ViewUtils.inject(this, this.mActivityFragmentView);
        /**
         * 设置签到时间（年月日）
         */
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日");
        String time = dateFormat.format(date);
        this.tv_time_qiandao.setText(time);
        httpRequestKaoqingofCurrentDay();
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

        imageTextButton.setItemTextResource("选择");
        imageTextButton.setItemImageResource(R.drawable.item_choose);

        /*initLoadingError();
        tv_viewLoadingError.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActivityFragmentView.viewLoadingError(View.GONE);
                httpRequestKaoqingofCurrentDay();
            }
        });*/

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

    /**
     * 进行时间转换
     */
    public String transferDateTime(String date){
        String newDate = date.replace("年","-");
        newDate = newDate.replace("月","-");
        newDate = newDate.replace("日","");
        return newDate;
    }

    /**
     * 查询当天签到信息
     */
    private void httpRequestKaoqingofCurrentDay() {
        SqliteDBUtils sqliteDBUtils = new SqliteDBUtils(this);
        OkHttpClient okHttpClient = new OkHttpClient();
        Request.Builder builder = new Request.Builder();
        FormEncodingBuilder requestBodyBuilder = new FormEncodingBuilder();
        requestBodyBuilder.add("userId", sqliteDBUtils.getLoginUid());
        requestBodyBuilder.add("date", transferDateTime(tv_time_qiandao.getText().toString()));
        RequestBody requestBody = requestBodyBuilder.build();
        String request_url = ProtocolUrl.ROOT_URL + "/"+ ProtocolUrl.APP_QUERY_DAY_OF_KAOQING;
        Request request = builder.url(request_url).post(requestBody).build();
        Call call = okHttpClient.newCall(request);
        mActivityFragmentView.viewLoading(View.VISIBLE);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mActivityFragmentView.viewLoading(View.GONE);
                        /*waiqing_layout.setVisibility(View.GONE);
                        mActivityFragmentView.viewLoadingError(View.VISIBLE);*/
                    }
                });
            }

            @Override
            public void onResponse(Response response) throws IOException {
                String result_str = response.body().string();
                try {
                    final JSONObject jsonObject = new JSONObject(result_str);

                    String data = jsonObject.getString("data");
                    JSONObject jsonObject1 = new JSONObject(data);
                    String checkInfo = jsonObject1.getString("ck");
                    final String shouldSBTime = jsonObject1.getString("ClockIn");
                    final String shouldXBTime = jsonObject1.getString("ClockOut");
                    final String middleTime = jsonObject1.getString("MiddleTime");

                    if (("null").equals(checkInfo)) {
                        Date date = new Date();
                        final SimpleDateFormat format = new SimpleDateFormat("HH:mm");
                        String currentTime = format.format(date);
                        //如果没超过12.00 表示上午
                        if (equalsStringMiddle(currentTime,middleTime)) {
                            mHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    mActivityFragmentView.viewLoading(View.GONE);
                                    tv_qiandao.setText("上班打卡");
                                    Date date1 = new Date();
                                    String currentTime1 = format.format(date1);
                                    tv_hourminute_qiandao.setText(currentTime1);
                                }
                            });
                        } else {
                            mHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    //下午
                                    mActivityFragmentView.viewLoading(View.GONE);
                                    tv_qiandao.setText("下班打卡");
                                    Date date1 = new Date();
                                    String currentTime1 = format.format(date1);
                                    tv_hourminute_qiandao.setText(currentTime1);
                                }
                            });
                        }
                    } else {
                        int status = jsonObject.getInt("status");
                        final String msg = jsonObject.getString("msg");
                        if (status == 1) {
                            mHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    mActivityFragmentView.viewLoading(View.GONE);
                                    msg(msg);
                                }
                            });
                        }
                        final JSONObject dataObject = jsonObject1.getJSONObject("ck");
                        final String morningCount = dataObject.optString("morningCount");
                        final String afterCount = dataObject.optString("afterCount");
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                mActivityFragmentView.viewLoading(View.GONE);
                        //        waiqing_layout.setVisibility(View.VISIBLE);
                                //代表上午还没有签到
                                if("0".equals(morningCount)){
                                    Date date = new Date();
                                    SimpleDateFormat format = new SimpleDateFormat("HH:mm");
                                    String currentTime = format.format(date);
                                    //如果没超过12.00 表示上午
                                    if(equalsStringMiddle(currentTime,middleTime)){
                                        tv_qiandao.setText("上班打卡");
                                    }else {
                                        tv_qiandao.setText("下班打卡");
                                    }
                                    return;
                                }else {
                                    tv_state_qiandao.setText("你上班已打卡成功!");
                                    tv_qiandao.setText("下班打卡");
                                }

                                //代表下午还没有签到
                                if("0".equals(afterCount)){
                                    tv_qiandao.setText("下班打卡");
                                }else{
                                    qiandao_layout.setVisibility(View.GONE);
                                    tv_state_qiandao.setText("你下班已打卡成功!");
                                    tv_state_qiandao.setVisibility(View.VISIBLE);
                                }
                            }
                        });
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void initData() {
        super.initData();

        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
        String time = dateFormat.format(date);
        this.tv_hourminute_qiandao.setText(time);

        initPosition();

    }

    @OnClick({
            R.id.img_qiandao,
            R.id.tv_place_weitiao
    })
    private void viewClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            // 点击签到
            case R.id.img_qiandao:
                if ("下班打卡".equals(tv_qiandao.getText().toString())) {
                    Date date = new Date();
                    SimpleDateFormat format = new SimpleDateFormat("HH:mm");
                    String currentTime = format.format(date);
                    //如果没超过12.00 表示上午
                    if (equalsString12(currentTime)) {
                        Toast.makeText(getContext(), "时间还没到下午，不能打下班卡!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

                String time = this.tv_hourminute_qiandao.getText().toString();// 签到时间
                String address = this.tv_place_qiandao.getText().toString(); // 签到地址
                String company = this.tv_company_qiandao.getText().toString();// 当前企业
                if (address.equals("没有定位信息！")) {
                    Toast.makeText(getApplicationContext(), "当前没有定位，请定位后再签到！", Toast.LENGTH_SHORT).show();
                    return;
                }
                intent.setClass(WaiqingQiandaoActivity.this, WaiqingQiandaoSubmitActivity.class);
                intent.putExtra("time", time);
                intent.putExtra("lat", latitude);
                intent.putExtra("lon", longitude);
                intent.putExtra("address", address);
                intent.putExtra("company", company);
                startActivityForResult(intent, QIANDAO_REQUEST);
                break;

            //地点微调
            case R.id.tv_place_weitiao:
                intent.setClass(WaiqingQiandaoActivity.this, LocationTrimActivity.class);
                startActivityForResult(intent, REQUEST_PLACE_CHANGE);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == QIANDAO_REQUEST && resultCode == RESULT_OK) {
            Date date = new Date();
            SimpleDateFormat format = new SimpleDateFormat("HH:mm");
            String currentTime = format.format(date);
            //如果没超过12.00 表示上午
            if(equalsString12(currentTime)){
                this.morningCount++;
                if(morningCount>0){
                    tv_qiandao.setText("下班打卡");
                    tv_state_qiandao.setVisibility(View.GONE);
                }
            }else {
                this.afterCount++;
                if(afterCount>0){
                    qiandao_layout.setVisibility(View.GONE);
                    tv_state_qiandao.setText("你下班已打卡成功!");
                    tv_state_qiandao.setVisibility(View.VISIBLE);
                }
            }

     //       this.img_confirm_qiandao.setVisibility(View.VISIBLE);

        }  else if (requestCode == REQUEST_PLACE_CHANGE){
            if ((resultCode == Activity.RESULT_OK) || (resultCode == Activity.RESULT_CANCELED)) {
                Bundle bundle = data.getExtras();
                if (bundle != null) {
                    latitude = bundle.getDouble("latitude");
                    longitude = bundle.getDouble("longitude");
                   /* LatLng ll = new LatLng(latitude,longitude);
                    mCurrentMarker.setPosition(ll);*/
                    String addressName = bundle.getString("addressName");
                    tv_place_qiandao.setText(addressName);

                }
            }
        }
    }

    private Handler mHandler = new Handler(){};

    public void initPosition(){
        SDKInitializer.initialize(getApplicationContext());
        LocationUtils.Location(getApplicationContext());
        latitude = LocationUtils.getLat();
        longitude = LocationUtils.getLng();
        String addrStr = LocationUtils.getAddress();

        LatLng ll = new LatLng(latitude,longitude);
        // 构造定位数据
        MyLocationData locData = new MyLocationData.Builder()
                //      .accuracy(location.getRadius())
                // 此处设置开发者获取到的方向信息，顺时针0-360
                .direction(100).latitude(latitude)
                .longitude(longitude).build();
        mBaiduMap.setMyLocationData(locData);

        //画标志
        CoordinateConverter converter = new CoordinateConverter();
        converter.coord(ll);
        converter.from(CoordinateConverter.CoordType.COMMON);
        LatLng convertLatLng = converter.convert();

        OverlayOptions ooA = new MarkerOptions().position(ll).icon(BitmapDescriptorFactory.fromResource(R.drawable.item_position));
        mCurrentMarker = (Marker) mBaiduMap.addOverlay(ooA);
        mBaiduMap.addOverlay(ooA);

        MapStatusUpdate u = MapStatusUpdateFactory.newLatLngZoom(convertLatLng, 17.0f);
        mBaiduMap.animateMapStatus(u);

        //画当前定位标志
        MapStatusUpdate uc = MapStatusUpdateFactory.newLatLng(ll);
        mBaiduMap.animateMapStatus(uc);

        if(("").equals(addrStr)||("null").equals(addrStr)||addrStr==null){
            tv_place_qiandao.setText("没有定位信息！");
            img_qiandao.setImageResource(R.drawable.icon_grayciecle);
            img_qiandao.setClickable(false);
            Toast.makeText(this, "没有准确获取到你当前的位置信息，请再次点击！", Toast.LENGTH_SHORT).show();
            return;
        }else{
            tv_place_qiandao.setText(addrStr);
            img_qiandao.setClickable(true);
            img_qiandao.setImageResource(R.drawable.icon_orangecircle);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    // 三个状态实现地图生命周期管理
    @Override
    protected void onDestroy() {
        // 退出时销毁定位
        LocationUtils.onDestory();
        super.onDestroy();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    /**
     * 比较是否超过了12:00  超过返回false
     */
    public boolean equalsString12(String currentdate){
        //定义一个标准时间
        int[] arr = {12,0,0};
        String[] strings = currentdate.split(":");
        int[] temp = new int[strings.length];
        //将字符数据转为int数组
        for (int i = 0; i < strings.length; i++) {
            temp[i]=Integer.parseInt(strings[i]);
        }
        //只要是在12点之前，都属于上午，在12点之后，都属于下午
        if (temp[0]<arr[0]) {
            return true;
        }
        return false;
    }

    /**
     * 比较是否超过了中间时间  超过返回false
     */
    public boolean equalsStringMiddle(String currentdate,String middleTime){
        //定义一个标准时间
        String[] strings = currentdate.split(":");
        String[] middleArr = middleTime.split(":");
        int[] temp = new int[strings.length];
        int[] middle = new int[middleArr.length];
        //将字符数据转为int数组
        for (int i = 0; i < strings.length; i++) {
            temp[i]=Integer.parseInt(strings[i]);
        }
        for (int i = 0; i < middle.length; i++) {
            middle[i]=Integer.parseInt(middleArr[i]);
        }
        //只要是在12点之前，都属于上午，在12点之后，都属于下午
        if (temp[0]<middle[0]) {
            return true;
        }
        return false;
    }

}
