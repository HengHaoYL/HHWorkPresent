package com.henghao.hhworkpresent.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.SpatialRelationUtil;
import com.henghao.hhworkpresent.FragmentSupport;
import com.henghao.hhworkpresent.ProtocolUrl;
import com.henghao.hhworkpresent.R;
import com.henghao.hhworkpresent.activity.MainActivity;
import com.henghao.hhworkpresent.activity.QiandaoShangbanSubmitActivity;
import com.henghao.hhworkpresent.activity.QiandaoXiabanSubmitActivity;
import com.henghao.hhworkpresent.adapter.CommonListStringAdapter;
import com.henghao.hhworkpresent.listener.OnDateChooseDialogListener;
import com.henghao.hhworkpresent.utils.LocationUtils;
import com.henghao.hhworkpresent.utils.PopupWindowHelper;
import com.henghao.hhworkpresent.utils.SqliteDBUtils;
import com.henghao.hhworkpresent.views.CircleImageView;
import com.henghao.hhworkpresent.views.DateChooseDialog;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static android.app.Activity.RESULT_OK;
import static com.henghao.hhworkpresent.ProtocolUrl.APP_LODAING_HEAD_IMAGE_URI;
import static com.henghao.hhworkpresent.R.id.daka_xiaban_shangbanstate;

/**
 * Created by bryanrady on 2017/3/10.
 * 打卡界面
 */

public class DakaFragment extends FragmentSupport {

    private ImageLoader imageLoader;

    private DisplayImageOptions options;

    @ViewInject(R.id.tv_datepicker)
    private TextView datepickerTV;
    private String textString;

    @ViewInject(R.id.daka_headimage)
    private CircleImageView headImage;

    @ViewInject(R.id.daka_name)
    private TextView tv_loginName;

    @ViewInject(R.id.daka_position)
    private TextView tv_daka_position;

    @ViewInject(R.id.daka_layout)
    private RelativeLayout daka_layout;

    /**
     * 没有日程显示的布局
     */
    @ViewInject(R.id.fragment_null_daka_layout)
    private RelativeLayout null_daka_layout;

    private View popView;
    private PopupWindowHelper popupWindowHelper;

    private SqliteDBUtils sqliteDBUtils;

    public final static int HOLIDAY_TRUE = 2;
    public final static int HOLIDAY_FALSE = 1;
    public final static int HOLIDAY_DIALOG_FALSE = 3;
    public final static int HOLIDAY_DIALOG_TRUE = 4;

    public Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == HOLIDAY_TRUE){
                pastdate_layout.setVisibility(View.GONE);
                shangban_layout.setVisibility(View.GONE);
                xiaban_layout.setVisibility(View.GONE);
                null_daka_layout.setVisibility(View.VISIBLE);
            }else if(msg.what == HOLIDAY_FALSE){
                httpRequestKaoqingofCurrentDay();

            }else if(msg.what == HOLIDAY_DIALOG_FALSE){
                pastdate_layout.setVisibility(View.VISIBLE);
                shangban_layout.setVisibility(View.GONE);
                xiaban_layout.setVisibility(View.GONE);
                null_daka_layout.setVisibility(View.GONE);
                httpRequestKaoqingofPastDate();
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        this.mActivityFragmentView.viewMain(R.layout.fragment_currentdate_shangbandaka);
        this.mActivityFragmentView.viewEmpty(R.layout.activity_empty);
        this.mActivityFragmentView.viewEmptyGone();
        this.mActivityFragmentView.viewLoading(View.GONE);
        this.mActivityFragmentView.viewLoadingError(View.GONE);
        ViewUtils.inject(this, this.mActivityFragmentView);
        //注册定位监听  必须用全局 context  不能用 this.mActivity
        SDKInitializer.initialize(getActivity().getApplication().getApplicationContext());
        LocationUtils.Location(getActivity().getApplication().getApplicationContext());
        initWidget();
        initData();
        return this.mActivityFragmentView;
    }


    public void initWidget(){
        initWithBar();
        mLeftTextView.setText("打卡");
        mLeftTextView.setVisibility(View.VISIBLE);
        mLeftTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toMainActivity();
            }
        });
        mLeftImageView.setImageResource(R.drawable.item_point_left);
        mLeftImageView.setVisibility(View.VISIBLE);
        mLeftImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toMainActivity();
            }
        });

        initWithRightBar();
        mRightTextView.setText("打卡位置");
        mRightTextView.setVisibility(View.GONE);
        mRightTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindowHelper.showFromTop(v);
            }
        });

        this.popView = LayoutInflater.from(mActivity).inflate(R.layout.common_android_listview, null);
        ListView mListView = (ListView) this.popView.findViewById(R.id.mlistview);
        final List<String> mList = new ArrayList<String>();
        //判断一个指定的经纬度点是否落在一个多边形区域内
        //http://wiki.lbsyun.baidu.com/cms/androidsdk/doc/v3_7_0/com/baidu/mapapi/utils/SpatialRelationUtil.html#isPolygonContainsPoint(java.util.List,%20com.baidu.mapapi.model.LatLng)
        mList.add("贵阳市观山湖区金阳行政中心二期综合办公大楼");
        mList.add("贵州省贵阳市乌当区林城东路7号");
        CommonListStringAdapter mListStringAdapter = new CommonListStringAdapter(this.mActivity, mList);
        mListView.setAdapter(mListStringAdapter);
        mListStringAdapter.notifyDataSetChanged();
        this.popupWindowHelper = new PopupWindowHelper(this.popView);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                String whatSelect = mList.get(arg2);
                tv_daka_position.setText(whatSelect);
                popupWindowHelper.dismiss();
            }
        });


        initLoadingError();
        this.tv_viewLoadingError.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActivityFragmentView.viewLoadingError(View.GONE);
                httpLoadingHeadImage();
            }
        });
    }

    /**
     * 根据位置获取经纬度  然后封装为LatLng
     * @param position
     * @return
     */
    private double latitude ;
    private double longitude ;
    public LatLng getLatlng(final String position) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                List<android.location.Address> addressList = null;
                if (position != null) {
                    Geocoder gc = new Geocoder(mActivity, Locale.CHINA);
                    try {
                        //这是个耗时操作
                        addressList = gc.getFromLocationName(position, 1);
                        if (!addressList.isEmpty()) {
                            android.location.Address address_temp = addressList.get(0);
                            latitude = address_temp.getLatitude();
                            longitude = address_temp.getLongitude();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        thread.start();
        return new LatLng(latitude,longitude);
    }

    public void toMainActivity(){
        Intent intent = new Intent();
        intent.setClass(getContext(),MainActivity.class);
        startActivity(intent);
    }

    public void initData(){
        sqliteDBUtils = new SqliteDBUtils(mActivity);

        httpLoadingHeadImage();
        tv_loginName.setText(sqliteDBUtils.getLoginFirstName() + sqliteDBUtils.getLoginGiveName());

        SimpleDateFormat f=new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        textString = f.format(date);
        datepickerTV.setText(textString);

        //初始化数据前，先判断时候是家假日
        equalsHoliday(textString);
    }

    public void httpLoadingHeadImage(){
        OkHttpClient okHttpClient = new OkHttpClient();
        Request.Builder builder = new Request.Builder();
        FormEncodingBuilder requestBodyBuilder = new FormEncodingBuilder();
        requestBodyBuilder.add("uid",sqliteDBUtils.getLoginUid());
        RequestBody requestBody = requestBodyBuilder.build();
        String request_url = ProtocolUrl.ROOT_URL + "/"+ ProtocolUrl.APP_LODAING_HEAD_IMAGE;
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
                        daka_layout.setVisibility(View.GONE);
                        mActivityFragmentView.viewLoadingError(View.VISIBLE);
                    }
                });
            }

            @Override
            public void onResponse(Response response) throws IOException {
                String result_str = response.body().string();
                try {
                    JSONObject jsonObject = new JSONObject(result_str);
                    int status = jsonObject.getInt("status");
                    if (status == 1) {
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                mActivityFragmentView.viewLoadingError(View.VISIBLE);
                                daka_layout.setVisibility(View.GONE);
                                mActivityFragmentView.viewLoading(View.GONE);
                                mActivity.msg("下载错误");
                            }
                        });
                    }
                    if(status == 0){
                        final String imageName = jsonObject.optString("data");
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                daka_layout.setVisibility(View.VISIBLE);

                                // 使用DisplayImageOptions.Builder()创建DisplayImageOptions
                                options = new DisplayImageOptions.Builder()
                                        .showImageOnLoading(R.drawable.icon_logo) // 设置图片下载期间显示的图片
                                        .showImageForEmptyUri(R.drawable.icon_logo) // 设置图片Uri为空或是错误的时候显示的图片
                                        .showImageOnFail(R.drawable.icon_logo) // 设置图片加载或解码过程中发生错误显示的图片
                                        .cacheInMemory(true) // 设置下载的图片是否缓存在内存中
                                        .cacheOnDisk(true) // 设置下载的图片是否缓存在SD卡中
                                        //              .displayer(new RoundedBitmapDisplayer(20)) // 设置成圆角图片  如果使用这句代码，图片直接显示不出来。
                                        .build(); // 构建完成

                                imageLoader = ImageLoader.getInstance();
                                //imageLoader.init(ImageLoaderConfiguration.createDefault(mActivity));
                                String imageUri = ProtocolUrl.ROOT_URL + APP_LODAING_HEAD_IMAGE_URI + imageName;
                                imageLoader.displayImage(imageUri, headImage, options);
                                mActivityFragmentView.viewLoading(View.GONE);
                            }
                        });

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

   /*************** 三大布局 *********************/
    @ViewInject(R.id.fragment_pastdate_daka_layout)
    private RelativeLayout pastdate_layout;

    @ViewInject(R.id.fragment_shangban_daka_layout)
    private RelativeLayout shangban_layout;

    @ViewInject(R.id.fragment_xiaban_daka_layout)
    private RelativeLayout xiaban_layout;
    /************************************/

    /*************** 上班打卡布局 *********************/
    //上班签到图片
    @ViewInject(R.id.daka_shangban_qiandao_image)
    private ImageView shangban_qiandao_image;

    @ViewInject(R.id.daka_shangban_qiandao_date)
    private TextView shangban_qiandao_date;

    @ViewInject(R.id.daka_shangban_qiandao_state)
    private TextView shangban_qiandao_location;
    /************************************/

    /*************** 下班打卡布局 *********************/
    //下班布局的上班时间
    @ViewInject(R.id.daka_xiaban_shangbantime)
    private TextView xiaban_shangbantime;

    //下班布局的上班状态
    @ViewInject(daka_xiaban_shangbanstate)
    private TextView xiaban_shangbanstate;

    //下班布局的上班补卡申请
    @ViewInject(R.id.daka_xiaban_shangbanbuka)
    private Button xiaban_shangbanbuka;

    //下班签到图片
    @ViewInject(R.id.daka_xiaban_qiandao_image)
    private ImageView xiaban_qiandao_image;

    //下班打卡时间
    @ViewInject(R.id.daka_xiaban_qiandao_datetime)
    private TextView xiaban_qiandao_date;

    @ViewInject(R.id.daka_xiaban_qiandao_state)
    private TextView xiaban_qiandao_location;

    /************************************/


    /*************** 过去日期布局获取控件 *********************/
    @ViewInject(R.id.daka_pastdate_shangbantime)
    private TextView pastdate_shangbantime;

    @ViewInject(R.id.daka_pastdate_shangbanstate)
    private TextView pastdate_shangbanstate;

    @ViewInject(R.id.daka_pastdate_shangbanbuka)
    private Button pastdate_shangbanbuka;

    @ViewInject(R.id.daka_pastdate_xiabantime)
    private TextView pastdate_xiabantime;

    @ViewInject(R.id.daka_pastdate_xiabanstate)
    private TextView pastdate_xiabanstate;

    @ViewInject(R.id.daka_pastdate_xiabanbuka)
    private Button pastdate_xiabanbuka;

    /************************************/

    @OnClick({R.id.tv_datepicker,R.id.daka_shangban_qiandao_image,R.id.daka_xiaban_qiandao_image})
    private void viewOnClick(View v) {
        switch (v.getId()) {
            case R.id.tv_datepicker:
                onClickDatePicker();
                break;

            case R.id.daka_shangban_qiandao_image:
                onClickShangbanDaka();
                break;

            case R.id.daka_xiaban_qiandao_image:
                onClickXiabanDaka();
                break;
        }
    }

    private static int SHANGBAN_QIANDAO_REQUEST = 1001;
    private static int XIABAN_QIANDAO_REQUEST = 1002;

    private String shangbanDakaAdrress;
    private String xiabanDakaAdrress;

    private SpatialRelationUtil spatialRelationUtil = new SpatialRelationUtil();

    @Override
    public void onResume() {
        super.onResume();
        LocationUtils.Location(getActivity().getApplication().getApplicationContext());
        shangbanDakaAdrress = LocationUtils.getAddress();
        if(("").equals(shangbanDakaAdrress)||("null").equals(shangbanDakaAdrress)){
            shangban_qiandao_location.setText("当前没有定位信息!");
        }else{
            shangban_qiandao_location.setText(shangbanDakaAdrress);
        }

        xiabanDakaAdrress = LocationUtils.getAddress();
        if(("").equals(xiabanDakaAdrress)||("null").equals(xiabanDakaAdrress)){
            xiaban_qiandao_location.setText("当前没有定位信息!");
        }else{
            xiaban_qiandao_location.setText(xiabanDakaAdrress);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LocationUtils.onDestory();
    }

    public void onClickShangbanDaka(){
        /*if("暂时没有选择地点!".equals(tv_daka_position.getText().toString())){
            Toast.makeText(this.mActivity, "请点击右上角选择打卡地点，否则不能打卡!", Toast.LENGTH_SHORT).show();
            return;
        }*/
        String daka_position1 = "贵阳市观山湖区金阳行政中心二期综合办公大楼";
        String daka_position2 = "贵州省贵阳市乌当区林城东路7号";
        LatLng center1 = getLatlng(daka_position1);
        int radius = 500;
        LatLng point1 = new LatLng(LocationUtils.getLat(),LocationUtils.getLng());
        if(center1.latitude == 0.0||center1.longitude==0.0){
            return;
        }
        if(point1==null){
            Toast.makeText(mActivity, "暂时没有定位信息！", Toast.LENGTH_SHORT).show();
            return;
        }
        boolean isContans1 = spatialRelationUtil.isCircleContainsPoint(center1,radius,point1);

        LatLng center2 = getLatlng(daka_position2);
        LatLng point2 = new LatLng(LocationUtils.getLat(),LocationUtils.getLng());
        if(center2.latitude == 0.0||center2.longitude==0.0){
            return;
        }
        if(point2==null){
            Toast.makeText(mActivity, "暂时没有定位信息！", Toast.LENGTH_SHORT).show();
            return;
        }
        boolean isContans2 = spatialRelationUtil.isCircleContainsPoint(center2,radius,point2);

        if(!isContans1 && !isContans2){
            Toast.makeText(mActivity, "你当前不在可打卡区域，请移动到打卡区域方能打卡!", Toast.LENGTH_SHORT).show();
            return;
        }else {
            Intent intent = new Intent(mActivity, QiandaoShangbanSubmitActivity.class);
            String time = shangban_qiandao_date.getText().toString();// 签到时间
            String address = shangban_qiandao_location.getText().toString(); // 签到地址
            double longitude = LocationUtils.getLng();
            double latitude = LocationUtils.getLat();
            if (address.equals("当前没有定位信息!")) {
                Toast.makeText(mActivity, "当前没有定位，请定位后再签到！", Toast.LENGTH_SHORT).show();
                return;
            }
            intent.putExtra("time", time);
            intent.putExtra("latitude", latitude);
            intent.putExtra("longitude", longitude);
            intent.putExtra("address", address);
            startActivityForResult(intent,SHANGBAN_QIANDAO_REQUEST);
        }
    }

    public void onClickXiabanDaka(){
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        String currentTime = format.format(date);

        /**
        * public static boolean isCircleContainsPoint(LatLng center,int radius,LatLng point)
        *    判断圆形是否包含传入的经纬度点
        参数:
        center - 构成圆的中心点
        radius - 圆的半径
        point - 待判断点
        返回:
        true 包含，false 为不包含
        */
        /*if("暂时没有选择地点!".equals(tv_daka_position.getText().toString())){
            Toast.makeText(this.mActivity, "请点击右上角选择打卡地点，否则不能打卡!", Toast.LENGTH_SHORT).show();
           return;
        }*/
        String daka_position1 = "贵阳市观山湖区金阳行政中心二期综合办公大楼";
        String daka_position2 = "贵州省贵阳市乌当区林城东路7号";
        LatLng center1 = getLatlng(daka_position1);
        int radius = 500;
        LatLng point1 = new LatLng(LocationUtils.getLat(),LocationUtils.getLng());
        if(center1.latitude == 0.0||center1.longitude==0.0){
            return;
        }
        if(point1==null){
            Toast.makeText(mActivity, "暂时没有定位信息！", Toast.LENGTH_SHORT).show();
            return;
        }
        boolean isContans1 = spatialRelationUtil.isCircleContainsPoint(center1,radius,point1);

        LatLng center2 = getLatlng(daka_position2);
        LatLng point2 = new LatLng(LocationUtils.getLat(),LocationUtils.getLng());
        if(center2.latitude == 0.0||center2.longitude==0.0){
            return;
        }
        if(point2==null){
            Toast.makeText(mActivity, "暂时没有定位信息！", Toast.LENGTH_SHORT).show();
            return;
        }
        boolean isContans2 = spatialRelationUtil.isCircleContainsPoint(center2,radius,point2);

        if(!isContans1&&!isContans2){
            Toast.makeText(mActivity, "你当前不在可打卡区域，请移动到打卡区域方能打卡!", Toast.LENGTH_SHORT).show();
            return;
        }else {
            //如果没超过12.00 表示上午
            if(equalsString12(currentTime)){
                Toast.makeText(this.mActivity, "时间还没到下午，不能打下班卡!", Toast.LENGTH_SHORT).show();
                return;
            }else {
                //下午
                pastdate_layout.setVisibility(View.GONE);
                shangban_layout.setVisibility(View.GONE);
                xiaban_layout.setVisibility(View.VISIBLE);
                httpRequestKaoqingofCurrentDateShangwu();
                Date date1 = new Date();
                String currentTime1 = format.format(date1);
                xiaban_qiandao_date.setText(currentTime1);
            }
            Intent intent = new Intent(mActivity, QiandaoXiabanSubmitActivity.class);
            String time = xiaban_qiandao_date.getText().toString();// 签到时间
            String address = xiaban_qiandao_location.getText().toString(); // 签到地址
            double longitude = LocationUtils.getLng();
            double latitude = LocationUtils.getLat();
            if (address.equals("当前没有定位信息!")) {
                Toast.makeText(mActivity, "当前没有定位，请定位后再签到！", Toast.LENGTH_SHORT).show();
                return;
            }
            intent.putExtra("time", time);
            intent.putExtra("latitude", latitude);
            intent.putExtra("longitude", longitude);
            intent.putExtra("address", address);
            startActivityForResult(intent,XIABAN_QIANDAO_REQUEST);
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //上班打卡成功
        if (requestCode == SHANGBAN_QIANDAO_REQUEST && resultCode == RESULT_OK) {
            //显示下班签到布局
            pastdate_layout.setVisibility(View.GONE);
            shangban_layout.setVisibility(View.GONE);
            xiaban_layout.setVisibility(View.VISIBLE);
            httpRequestKaoqingofCurrentDateShangwu();
            Date date = new Date();
            SimpleDateFormat format = new SimpleDateFormat("HH:mm");
            String currentTime = format.format(date);
            xiaban_qiandao_date.setText(currentTime);

        } else if(requestCode == XIABAN_QIANDAO_REQUEST && resultCode == RESULT_OK){
            //下班打卡成功
            pastdate_layout.setVisibility(View.VISIBLE);
            shangban_layout.setVisibility(View.GONE);
            xiaban_layout.setVisibility(View.GONE);
            httpRequestKaoqingofPastDate();
        }
    }

    /**
     * 日期选择控件 点击
     */
    public void onClickDatePicker(){
        final DateChooseDialog.Builder builder = new DateChooseDialog.Builder(this.mActivity);
        builder.setTitle("选择日期");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                //设置你的操作事项
                dialog.dismiss();
            }
        });

        //监听对话框确定键，得到返回的数据
        builder.setOnDateChooseDialogListener(new OnDateChooseDialogListener() {
            @Override
            public void onDateSetting(String textDate) {
                datepickerTV.setText(textDate);
                int type = equalsDate(datepickerTV.getText().toString());
                //大于当前日期：1，    等于当前日期：0，      小于当前日期：-1
                if(type==0){
                    null_daka_layout.setVisibility(View.GONE);
                    httpRequestKaoqingofCurrentDay();
                } else if(type==1){
                    pastdate_layout.setVisibility(View.GONE);
                    shangban_layout.setVisibility(View.GONE);
                    xiaban_layout.setVisibility(View.GONE);
                    null_daka_layout.setVisibility(View.VISIBLE);
                } else if(type==-1){
                    equalsHoliday(datepickerTV.getText().toString());
                }
            }
        });

        builder.setNegativeButton("取消", new android.content.DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    /**
     * 判断日期date是不是节假日 isHoliday true代表是节假日和周末不上班的日子  false代表不是节假日要上班
     * date 需要处理
     */

    private void equalsHoliday(String date){
        String textdate = date.replace("-","");
        OkHttpClient okHttpClient = new OkHttpClient();
        Request.Builder builder = new Request.Builder();
        FormEncodingBuilder requestBodyBuilder = new FormEncodingBuilder();
        //这个id是日期在数据库里的id 形式20170530
        requestBodyBuilder.add("id", textdate);
        RequestBody requestBody = requestBodyBuilder.build();
        String request_url = ProtocolUrl.ROOT_URL + "/"+ ProtocolUrl.APP_QUERY_DAY_OF_HOLIDAY;
        Request request = builder.url(request_url).post(requestBody).build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mActivityFragmentView.viewLoading(View.GONE);
                    }
                });
            }

            @Override
            public void onResponse(Response response) throws IOException {
                String result_str = response.body().string();
                //result_str=={"id":20170530,"date":"2017-05-30","status":"法定假日"}
                // {"id":20170528,"date":"2017-05-28","status":"周末"}
                Log.d("wangqingbin","result_str=="+result_str);
                //如果是空，  result_str.length()==0这个判断才有用
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mActivityFragmentView.viewLoading(View.GONE);
                    }
                });
                try{
                    JSONObject jsonObject = new JSONObject(result_str);
                    String status = jsonObject.getString("status");
                    //代表是要上班的日子
                    if("无节假日信息".equals(status)){
                        //如果日期是当天
                        if((datepickerTV.getText().toString()).equals(new SimpleDateFormat("yyyy-MM-dd").format(new Date()))){
                            Message message = Message.obtain();
                            message.what = HOLIDAY_FALSE;
                            handler.sendMessage(message);
                        }else{
                            Message message = Message.obtain();
                            message.what = HOLIDAY_DIALOG_FALSE;
                            handler.sendMessage(message);
                        }
                    }else{
                        Message message = Message.obtain();
                        message.what = HOLIDAY_TRUE;
                        handler.sendMessage(message);
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 格式化日期  将.换为-
     */
    public String changeDate(String date){
        String newDate = null;
        String[] arr = date.split("\\.");
        int year = Integer.parseInt(arr[0]);
        int month = Integer.parseInt(arr[1]);
        int dayOfMonth = Integer.parseInt(arr[2]);
        String month1 = null;
        String dayOfMonth1 = null;

        newDate = year + "-" + month + "-" + dayOfMonth;
        if(month<10 && dayOfMonth<10){
            month1 = "0"+ month;
            dayOfMonth1 = "0"+ dayOfMonth;
            newDate = year + "-" + month1 + "-" + dayOfMonth1;
        }
        if(month<10 && dayOfMonth>=10){
            month1 = "0"+ month;
            dayOfMonth1 = dayOfMonth+"";
            newDate = year + "-" + month1 + "-" + dayOfMonth1;
        }
        if(month>=10 && dayOfMonth<10){
            month1 = month+"";
            dayOfMonth1 = "0"+ dayOfMonth;
            newDate = year + "-" + month1 + "-" + dayOfMonth1;
        }
        return newDate;
    }

    /**
     * 查询当天签到信息
     */
    private void httpRequestKaoqingofCurrentDay() {
        OkHttpClient okHttpClient = new OkHttpClient();
        Request.Builder builder = new Request.Builder();
        FormEncodingBuilder requestBodyBuilder = new FormEncodingBuilder();
        requestBodyBuilder.add("userId", sqliteDBUtils.getLoginUid());
        requestBodyBuilder.add("date", datepickerTV.getText().toString());
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
                    }
                });
            }

            @Override
            public void onResponse(Response response) throws IOException {
                String result_str = response.body().string();
                try {
                    Date date = new Date();
                    final SimpleDateFormat format = new SimpleDateFormat("HH:mm");
                    final String currentTime = format.format(date);

                    JSONObject jsonObject = new JSONObject(result_str);
                    //开始用String 来接收 放回 data出现Null的情况 ,导致布局无法显示
                    String data = jsonObject.getString("data");
                    JSONObject jsonObject1 = new JSONObject(data);
                    String checkInfo = jsonObject1.getString("ck");
                    final String shouldSBTime = jsonObject1.getString("ClockIn");
                    final String shouldXBTime = jsonObject1.getString("ClockOut");
                    final String middleTime = jsonObject1.getString("MiddleTime");

                    if (("null").equals(checkInfo)) {
                        //如果没超过中间时间, 表示上午
                        if (equalsStringMiddle(currentTime,middleTime)) {
                            mHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    mActivityFragmentView.viewLoading(View.GONE);
                                    pastdate_layout.setVisibility(View.GONE);
                                    shangban_layout.setVisibility(View.VISIBLE);
                                    xiaban_layout.setVisibility(View.GONE);
                                    Date date1 = new Date();
                                    String currentTime1 = format.format(date1);
                                    shangban_qiandao_date.setText(currentTime1);
                                }
                            });
                        } else {
                            mHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    //下午
                                    mActivityFragmentView.viewLoading(View.GONE);
                                    pastdate_layout.setVisibility(View.GONE);
                                    shangban_layout.setVisibility(View.GONE);
                                    xiaban_layout.setVisibility(View.VISIBLE);
                                    httpRequestKaoqingofCurrentDateShangwu();
                                    Date date1 = new Date();
                                    String currentTime1 = format.format(date1);
                                    xiaban_qiandao_date.setText(currentTime1);
                                    xiaban_shangbanstate.setText("缺卡");
                                }
                            });
                        }
                    } else {
                        JSONObject dataObject = jsonObject1.getJSONObject("ck");
                        String morningCount = dataObject.optString("morningCount");
                        String afterCount = dataObject.optString("afterCount");
                        String checkType = dataObject.optString("checkType");

                        //如果checkType 是这几个值i 直接显示下午布局
                        if("请假".equals(checkType)){
                            mHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    mActivityFragmentView.viewLoading(View.GONE);
                                    pastdate_layout.setVisibility(View.VISIBLE);
                                    shangban_layout.setVisibility(View.GONE);
                                    xiaban_layout.setVisibility(View.GONE);
                                    pastdate_shangbantime.setText("无");
                                    pastdate_shangbanstate.setText("请假");
                                    pastdate_xiabantime.setText("无");
                                    pastdate_xiabanstate.setText("请假");
                                }
                            });
                            return;
                        }else if("补签".equals(checkType)){
                            mHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    mActivityFragmentView.viewLoading(View.GONE);
                                    pastdate_layout.setVisibility(View.VISIBLE);
                                    shangban_layout.setVisibility(View.GONE);
                                    xiaban_layout.setVisibility(View.GONE);
                                    pastdate_shangbanstate.setText("补签");
                                    pastdate_shangbantime.setText(shouldSBTime);
                                    pastdate_xiabanstate.setText("补签");
                                    pastdate_xiabantime.setText(shouldXBTime);
                                }
                            });
                            return;
                        }else if("出差".equals(checkType)){
                            mHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    mActivityFragmentView.viewLoading(View.GONE);
                                    pastdate_layout.setVisibility(View.VISIBLE);
                                    shangban_layout.setVisibility(View.GONE);
                                    xiaban_layout.setVisibility(View.GONE);
                                    pastdate_shangbantime.setText("无");
                                    pastdate_shangbanstate.setText("出差");
                                    pastdate_xiabantime.setText("无");
                                    pastdate_xiabanstate.setText("出差");
                                }
                            });
                            return;
                        }else{
                            //代表上午还没有签到
                            if ("0".equals(morningCount)) {
                                //如果没超过12.00 表示上午
                                if (equalsStringMiddle(currentTime,middleTime)) {
                                    mHandler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            mActivityFragmentView.viewLoading(View.GONE);
                                            pastdate_layout.setVisibility(View.GONE);
                                            shangban_layout.setVisibility(View.VISIBLE);
                                            xiaban_layout.setVisibility(View.GONE);
                                            Date date1 = new Date();
                                            String currentTime1 = format.format(date1);
                                            shangban_qiandao_date.setText(currentTime1);
                                        }
                                    });
                                    return;
                                } else {
                                    mHandler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            //下午
                                            mActivityFragmentView.viewLoading(View.GONE);
                                            pastdate_layout.setVisibility(View.GONE);
                                            shangban_layout.setVisibility(View.GONE);
                                            xiaban_layout.setVisibility(View.VISIBLE);
                                            httpRequestKaoqingofCurrentDateShangwu();
                                            Date date1 = new Date();
                                            String currentTime1 = format.format(date1);
                                            xiaban_qiandao_date.setText(currentTime1);
                                            xiaban_shangbanstate.setText("缺卡");
                                        }
                                    });
                                }
                            } else {
                                mHandler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        mActivityFragmentView.viewLoading(View.GONE);
                                        //如果不是0 则显示下午签到布局
                                        pastdate_layout.setVisibility(View.GONE);
                                        shangban_layout.setVisibility(View.GONE);
                                        xiaban_layout.setVisibility(View.VISIBLE);
                                        httpRequestKaoqingofCurrentDateShangwu();
                                        Date date1 = new Date();
                                        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
                                        String currentTime1 = format.format(date1);
                                        xiaban_qiandao_date.setText(currentTime1);
                                    }
                                });
                            }

                            //代表下午还没有签到
                            if ("0".equals(afterCount)) {
                                mHandler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        mActivityFragmentView.viewLoading(View.GONE);
                                        pastdate_layout.setVisibility(View.GONE);
                                        shangban_layout.setVisibility(View.GONE);
                                        xiaban_layout.setVisibility(View.VISIBLE);
                                        httpRequestKaoqingofCurrentDateShangwu();
                                        Date date1 = new Date();
                                        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
                                        String currentTime1 = format.format(date1);
                                        xiaban_qiandao_date.setText(currentTime1);
                                    }
                                });
                            } else {
                                mHandler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        mActivityFragmentView.viewLoading(View.GONE);
                                        pastdate_layout.setVisibility(View.VISIBLE);
                                        shangban_layout.setVisibility(View.GONE);
                                        xiaban_layout.setVisibility(View.GONE);
                                        httpRequestKaoqingofPastDate();
                                    }
                                });
                            }
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private Handler mHandler = new Handler(){};

    private void httpRequestKaoqingofCurrentDateShangwu() {
        OkHttpClient okHttpClient = new OkHttpClient();
        Request.Builder builder = new Request.Builder();
        FormEncodingBuilder requestBodyBuilder = new FormEncodingBuilder();
        String date = datepickerTV.getText().toString();
        requestBodyBuilder.add("userId", sqliteDBUtils.getLoginUid());
        requestBodyBuilder.add("date", date);
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
                        Toast.makeText(getContext(), "网络访问错误！", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Response response) throws IOException {
                String result_str = response.body().string();
                try {
                    JSONObject jsonObject = new JSONObject(result_str);
                    int status = jsonObject.getInt("status");
                    if (status == 0) {
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                mActivityFragmentView.viewLoading(View.GONE);
                            }
                        });
                    }
                    //开始用String 来接收 放回 data出现Null的情况 ,导致布局无法显示
                    String data = jsonObject.getString("data");
                    JSONObject jsonObject1 = new JSONObject(data);
                    String checkInfo = jsonObject1.getString("ck");
                    final String shouldSBTime = jsonObject1.getString("ClockIn");
                    final String shouldXBTime = jsonObject1.getString("ClockOut");
                    final String middleTime = jsonObject1.getString("MiddleTime");
                    if (("null").equals(checkInfo)) {
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                xiaban_shangbanstate.setText("缺卡");
                                xiaban_shangbantime.setText("无");
                                //       xiaban_shangbanbuka.setVisibility(View.VISIBLE);
                                mActivityFragmentView.viewLoading(View.GONE);
                            }
                        });
                    }else{
                        final JSONObject dataObject = jsonObject1.getJSONObject("ck");
                        final String clockInTime = dataObject.optString("clockInTime");
                        //这时的clockInTime是一个null字符串 ，不是null
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                xiaban_shangbantime.setText(clockInTime);
                            }
                        });

                        //缺卡情况
                        if(("null").equals(clockInTime)||clockInTime==null){
                            mHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    xiaban_shangbanstate.setText("缺卡");
                                    xiaban_shangbantime.setText("无");
                                    //       xiaban_shangbanbuka.setVisibility(View.VISIBLE);
                                    mActivityFragmentView.viewLoading(View.GONE);
                                }
                            });
                        }

                        //上班迟到情况
                        if(!("null").equals(clockInTime)){
                            if(equalsStringShangban(clockInTime,shouldSBTime)){
                                mHandler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        xiaban_shangbanstate.setText("迟到");
                                        xiaban_shangbanbuka.setVisibility(View.GONE);
                                    }
                                });

                            }
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void httpRequestKaoqingofPastDate() {
        OkHttpClient okHttpClient = new OkHttpClient();
        Request.Builder builder = new Request.Builder();
        FormEncodingBuilder requestBodyBuilder = new FormEncodingBuilder();
        String date = datepickerTV.getText().toString();
        requestBodyBuilder.add("userId", sqliteDBUtils.getLoginUid());
        requestBodyBuilder.add("date", date);
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
                        Toast.makeText(getContext(), "网络访问错误！", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Response response) throws IOException {
                String result_str = response.body().string();
                try {
                    JSONObject jsonObject = new JSONObject(result_str);
                    String data = jsonObject.getString("data");
                    JSONObject jsonObject1 = new JSONObject(data);
                    String checkInfo = jsonObject1.getString("ck");
                    final String shouldSBTime = jsonObject1.getString("ClockIn");
                    final String shouldXBTime = jsonObject1.getString("ClockOut");
                    final String middleTime = jsonObject1.getString("MiddleTime");
                    if (("null").equals(checkInfo)) {
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                pastdate_layout.setVisibility(View.GONE);
                                shangban_layout.setVisibility(View.GONE);
                                xiaban_layout.setVisibility(View.GONE);
                                null_daka_layout.setVisibility(View.VISIBLE);
                                mActivityFragmentView.viewLoading(View.GONE);
                            }
                        });
                    }else{
                        int status = jsonObject.getInt("status");
                        if (status == 0) {
                            mHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    mActivityFragmentView.viewLoading(View.GONE);
                                    null_daka_layout.setVisibility(View.GONE);
                                }
                            });
                        }
                        final JSONObject dataObject = jsonObject1.getJSONObject("ck");
                        final String clockInTime = dataObject.optString("clockInTime");
                        final String clockOutTime = dataObject.optString("clockOutTime");
                        //这时的clockInTime是一个null字符串 ，不是null
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                pastdate_shangbantime.setText(clockInTime);
                                pastdate_xiabantime.setText(clockOutTime);
                            }
                        });

                        String checkType = dataObject.optString("checkType");

                        //如果checkType 是这几个值i 直接显示下午布局
                        if("请假".equals(checkType)){
                            mHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    mActivityFragmentView.viewLoading(View.GONE);
                                    pastdate_layout.setVisibility(View.VISIBLE);
                                    shangban_layout.setVisibility(View.GONE);
                                    xiaban_layout.setVisibility(View.GONE);
                                    pastdate_shangbantime.setText("无");
                                    pastdate_shangbanstate.setText("请假");
                                    pastdate_xiabantime.setText("无");
                                    pastdate_xiabanstate.setText("请假");
                                }
                            });
                            return;
                        }else if("补签".equals(checkType)){
                            mHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    mActivityFragmentView.viewLoading(View.GONE);
                                    pastdate_layout.setVisibility(View.VISIBLE);
                                    shangban_layout.setVisibility(View.GONE);
                                    xiaban_layout.setVisibility(View.GONE);
                                    pastdate_shangbanstate.setText("补签");
                                    pastdate_shangbantime.setText(shouldSBTime);
                                    pastdate_xiabanstate.setText("补签");
                                    pastdate_xiabantime.setText(shouldXBTime);
                                }
                            });
                            return;
                        }else if("出差".equals(checkType)) {
                            mHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    mActivityFragmentView.viewLoading(View.GONE);
                                    pastdate_layout.setVisibility(View.VISIBLE);
                                    shangban_layout.setVisibility(View.GONE);
                                    xiaban_layout.setVisibility(View.GONE);
                                    pastdate_shangbantime.setText("无");
                                    pastdate_shangbanstate.setText("出差");
                                    pastdate_xiabantime.setText("无");
                                    pastdate_xiabanstate.setText("出差");
                                }
                            });
                            return;
                        }

                        //缺卡情况
                        if(("null").equals(clockInTime)){
                            mHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    pastdate_shangbanstate.setText("缺卡");
                                    pastdate_shangbantime.setText("无");
                                    //           pastdate_shangbanbuka.setVisibility(View.VISIBLE);
                                    mActivityFragmentView.viewLoading(View.GONE);
                                }
                            });
                        }

                        if(("null").equals(clockOutTime)){
                            mHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    pastdate_xiabanstate.setText("缺卡");
                                    pastdate_xiabantime.setText("无");
                                    //          pastdate_xiabanbuka.setVisibility(View.VISIBLE);
                                    mActivityFragmentView.viewLoading(View.GONE);
                                }
                            });
                        }

                        //上班迟到情况
                        if(!("null").equals(clockInTime)){
                            if(equalsStringShangban(clockInTime,shouldSBTime)){
                                mHandler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        pastdate_shangbanstate.setText("迟到");
                                    }
                                });

                            }
                        }

                        //下班早退情况
                        if(!("null").equals(clockOutTime)){
                            if(equalsStringXiaban(clockOutTime,shouldXBTime)){
                                mHandler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        pastdate_xiabanstate.setText("早退");
                                    }
                                });
                            }
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 比较上班时间  迟到返回true
     */
    public boolean equalsStringShangban(String clockInTime,String shouldSBTime){
        //定义一个标准时间 08:00
        //    int[] arr = {9,0,0};
        String[] strings = clockInTime.split(":");
    //    String[] shangTimes = shouldSBTime.split(":");
        int[] temp = new int[strings.length];
   //     int[] shangTime = new int[shangTimes.length];
        int[] shangTime ={8,50,0};
        //将字符数据转为int数组
        for (int i = 0; i < strings.length; i++) {
            temp[i]=Integer.parseInt(strings[i]);
        }
        /*for (int i = 0; i < shangTime.length; i++) {
            shangTime[i]=Integer.parseInt(shangTimes[i]);
        }*/
        //比较小时
        if (temp[0]>shangTime[0]) {
            return true;
        }
        if(temp[0]==shangTime[0]){
            //比较分钟
            if (temp[1]>shangTime[1]) {
                return true;
            }
            //如果分钟相等	9.0.0 , 9.0.0
            if (temp[1]==shangTime[1]) {
                //比较秒的用意，是为了对刚好在时间点打卡（如：9:00:00）的判断
                if (temp[2]>shangTime[2]) {
                    return true;
                }
            }

        }
        return false;
    }

    /**
     * 比较下班时间  早退返回true
     */
    public boolean equalsStringXiaban(String clockOutTime,String shouldXBTime){
        //定义一个标准时间
        //    int[] arr = {17,0,0};
        String[] strings = clockOutTime.split(":");
    //    String[] xiaTimes = shouldXBTime.split(":");
        int[] temp = new int[strings.length];
    //    int[] xiaTime = new int[xiaTimes.length];
        int[] xiaTime = {16,50,0};
        //将字符数据转为int数组
        for (int i = 0; i < strings.length; i++) {
            temp[i]=Integer.parseInt(strings[i]);
        }
        /*//将字符数据转为int数组
        for (int i = 0; i < xiaTime.length; i++) {
            xiaTime[i]=Integer.parseInt(xiaTimes[i]);
        }*/
        //只要是在18点之前，都属于早退，在18点之后，都属于正常下班
        if (temp[0]<xiaTime[0]) {
            return true;
        }
        return false;
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

    //日期比较测试       返回值：大于当前日期：1，等于当前日期：0，小于当前日期：-1
    public static int equalsDate(String date){
        //定义一个系统当前日期
        Date date1 = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String currentTime = format.format(date1);
        //传进来的日期数组
        int[] dataArr = StringToIntArr(date);
        //当前日期数组
        int[] current = StringToIntArr(currentTime);
        //进行比较
        if (dataArr[0]>current[0]) {
            System.out.println("大于当前日期");
            return 1;
        }
        if (dataArr[0]==current[0]) {
            //年份相等，判断月份
            if (dataArr[1]>current[1]) {
                System.out.println("大于当前日期");
                return 1;
            }else if(dataArr[1]==current[1]){
                //月份相等，判断天
                if (dataArr[2]>current[2]) {
                    System.out.println("大于当前日期");
                    return 1;
                }else if(dataArr[2]==current[2]){
                    System.out.println("等于当前日期");
                    return 0;
                }
                System.out.println("小于当前日期");
                return -1;
            }
        }
        //年份小于
        System.out.println("小于当前日期");
        return -1;
    }

    //传入String类型日期，返回int 数组
    public static int[] StringToIntArr(String date){
        String[] strings = date.split("-");
        int[] arr = new int[strings.length];
        for (int i = 0; i < strings.length; i++) {
            arr[i] = Integer.parseInt(strings[i]);
        }
        return arr;
    }
}
