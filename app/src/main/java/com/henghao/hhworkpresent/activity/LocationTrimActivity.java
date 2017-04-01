package com.henghao.hhworkpresent.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
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
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.mapapi.search.poi.PoiNearbySearchOption;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.mapapi.utils.CoordinateConverter;
import com.benefit.buy.library.views.xlistview.XListView;
import com.henghao.hhworkpresent.ActivityFragmentSupport;
import com.henghao.hhworkpresent.R;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bryanrady on 2017/3/16.
 *
 * 地点微调界面
 */

public class LocationTrimActivity extends ActivityFragmentSupport implements OnGetPoiSearchResultListener {

    @ViewInject(R.id.placeweitiao_lisview)
    private XListView mListView;

    // 百度地图控件
    @ViewInject(R.id.location_mapView)
    private MapView mMapView;
    // 百度地图对象
    private BaiduMap mBaiduMap;

    private Marker mCurrentMarker;

    private PoiInfo poiInfo;
    private List<PoiInfo> poiInfoList;

    // 搜索周边相关
    private PoiSearch mPoiSearch = null;

    private double longitude;// 精度
    private double latitude;// 维度

    public LocationClient mLocationClient = null;

    private ListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplicationContext());
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.mActivityFragmentView.viewMain(R.layout.activity_locationtrim);
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
        mLeftTextView.setText("地点微调");
        mLeftTextView.setVisibility(View.VISIBLE);
        mLeftTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                setResult(RESULT_OK,intent);
                finish();
            }
        });
        mLeftImageView.setImageResource(R.drawable.item_point_left);
        mLeftImageView.setVisibility(View.VISIBLE);
        mLeftImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                setResult(RESULT_OK,intent);
                finish();
            }
        });
        initWithRightBar();
        mRightTextView.setText("确定");
        mRightTextView.setVisibility(View.GONE);
        onSure();

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

    public void onSure(){
        mRightTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("wangqingbin", "poiInfo==" + poiInfo);
                if (poiInfo != null) {
                    Intent intent = new Intent();
                    latitude = poiInfo.location.latitude;
                    longitude = poiInfo.location.longitude;
                    String addressName = poiInfo.name.toString();
                    intent.putExtra("latitude", latitude);
                    intent.putExtra("longitude", longitude);
                    intent.putExtra("addressName", addressName);
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
        });
    }

    @Override
    public void initData() {
        super.initData();
        poiInfoList = new ArrayList<PoiInfo>();
        //如果这里让选中图标在第一位显示，会出现poiInfo对象为空的情况
        adapter = new ListAdapter(-1);
        mListView.setAdapter(adapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                mRightTextView.setVisibility(View.VISIBLE);
                adapter.setCheckposition(position-1);
                adapter.notifyDataSetChanged();
                if(poiInfoList.size() > position){
                    poiInfo = (PoiInfo) adapter.getItem(position-1);
                    MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(poiInfo.location);
                    mBaiduMap.animateMapStatus(u);
                    mCurrentMarker.setPosition(poiInfo.location);
                }
            }
        });

        /**
         * 定位
         */
        this.mLocationClient = new LocationClient(getApplicationContext()); // 实例化LocationClient类
        this.mLocationClient.registerLocationListener(this.myListener); // 注册监听函数
        this.setLocationOption(); // 设置定位参数
        this.mLocationClient.start(); // 开始定位
    }

    public BDLocationListener myListener = new BDLocationListener() {
        @Override
        public void onReceiveLocation(BDLocation location) {
            if (location == null) {
                return;
            }
            latitude = location.getLatitude();
            longitude = location.getLongitude();
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

            //poi 搜索周边
            new Thread(new Runnable() {
                @Override
                public void run() {
              //      Looper.prepare();
                    searchNeayBy();
             //       Looper.loop();
                }
            }).start();
        }

        @Override
        public void onConnectHotSpotMessage(String s, int i) {

        }
    };

    class ListAdapter extends BaseAdapter {

        private int checkPosition;

        public ListAdapter(int checkPosition){
            this.checkPosition = checkPosition;
        }

        public void setCheckposition(int checkPosition){
            this.checkPosition = checkPosition;
        }

        @Override
        public int getCount() {
            return poiInfoList.size();
        }

        @Override
        public Object getItem(int position) {
            return poiInfoList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if(convertView == null){
                holder = new ViewHolder();
                convertView = LayoutInflater.from(LocationTrimActivity.this).inflate(R.layout.listview_location_item, null);

                holder.textView = (TextView) convertView.findViewById(R.id.text_name);
                holder.textAddress = (TextView) convertView.findViewById(R.id.text_address);
                holder.imageLl = (ImageView) convertView.findViewById(R.id.image);
                convertView.setTag(holder);

            }else{
                holder = (ViewHolder)convertView.getTag();
            }
            Log.i("mybaidumap", "name地址是："+ poiInfoList.get(position).name);
            Log.i("mybaidumap", "address地址是："+ poiInfoList.get(position).address);

            holder.textView.setText(poiInfoList.get(position).name);
            holder.textAddress.setText(poiInfoList.get(position).address);
            if(checkPosition == position){
                holder.imageLl.setVisibility(View.VISIBLE);
            }else{
                holder.imageLl.setVisibility(View.GONE);
            }
            return convertView;
        }

    }

    class ViewHolder{
        TextView textView;
        TextView textAddress;
        ImageView imageLl;
    }

    /**
     * 搜索周边
     */
    private void searchNeayBy() {
        // POI初始化搜索模块，注册搜索事件监听
        mPoiSearch = PoiSearch.newInstance();
        mPoiSearch.setOnGetPoiSearchResultListener(this);
        PoiNearbySearchOption poiNearbySearchOption = new PoiNearbySearchOption();

        poiNearbySearchOption.keyword("公司");
        poiNearbySearchOption.location(new LatLng(latitude, longitude));
        poiNearbySearchOption.radius(100);  // 检索半径，单位是米
        poiNearbySearchOption.pageCapacity(20);  // 默认每页10条
        mPoiSearch.searchNearby(poiNearbySearchOption);  // 发起附近检索请求
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
        this.mLocationClient.setLocOption(option);
    }


    @Override
    public void onGetPoiIndoorResult(PoiIndoorResult poiIndoorResult) {

    }

    @Override
    public void onGetPoiResult(PoiResult poiResult) {
        // 获取POI检索结果
        if (poiResult == null || poiResult.error == SearchResult.ERRORNO.RESULT_NOT_FOUND) {// 没有找到检索结果
            Toast.makeText(LocationTrimActivity.this, "未找到结果",Toast.LENGTH_LONG).show();
            return;
        }

        if (poiResult.error == SearchResult.ERRORNO.NO_ERROR) {// 检索结果正常返回
            mBaiduMap.clear();
            if(poiResult != null){
                if(poiResult.getAllPoi()!= null && poiResult.getAllPoi().size()>0){
                    poiInfoList.addAll(poiResult.getAllPoi());
                    adapter.notifyDataSetChanged();
                    Message msg = new Message();
                    msg.what = 0;
                    handler.sendMessage(msg);
                }
            }
        }

    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    adapter.notifyDataSetChanged();
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public void onGetPoiDetailResult(PoiDetailResult poiDetailResult) {

    }

    @Override
    public void onResume() {
        super.onResume();
        this.mLocationClient.start(); // 开始定位
        mMapView.onResume();
    }

    @Override
    protected void onDestroy() {
        Log.d("wangqingbin","LocationTrimActivity onDestroy.....");
        // 退出时销毁定位
        if(handler!=null){
            handler.removeMessages(0);
            handler = null;
        }
        adapter = null;
        poiInfoList = null;
        poiInfo = null;
        mPoiSearch = null;
        this.mLocationClient.stop();
        mMapView.onDestroy();
        Log.d("wangqingbin","LocationTrimActivity mMapView.onDestroy();.....");
        super.onDestroy();
    }

    @Override
    public void onPause() {
        Log.d("wangqingbin","LocationTrimActivity onPause.....");
        if(handler!=null){
            handler.removeMessages(0);
            handler = null;
        }
        this.mLocationClient.stop();
        mMapView.onPause();
        Log.d("wangqingbin","LocationTrimActivity mMapView.onPause();.....");
        super.onPause();
    }
}
