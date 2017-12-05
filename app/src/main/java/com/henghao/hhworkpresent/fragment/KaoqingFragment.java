package com.henghao.hhworkpresent.fragment;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.henghao.hhworkpresent.FragmentSupport;
import com.henghao.hhworkpresent.ProtocolUrl;
import com.henghao.hhworkpresent.R;
import com.henghao.hhworkpresent.activity.CalendarActivity;
import com.henghao.hhworkpresent.activity.KaoqingDetailActivity;
import com.henghao.hhworkpresent.activity.KaoqingKGDetailActivity;
import com.henghao.hhworkpresent.adapter.ChidaoListAdapter;
import com.henghao.hhworkpresent.adapter.KuanggongListAdapter;
import com.henghao.hhworkpresent.adapter.QuekaListAdapter;
import com.henghao.hhworkpresent.adapter.ZaotuiListAdapter;
import com.henghao.hhworkpresent.entity.KaoqingEntity;
import com.henghao.hhworkpresent.utils.DateTimeUtils;
import com.henghao.hhworkpresent.utils.SqliteDBUtils;
import com.henghao.hhworkpresent.views.CircleImageView;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.henghao.hhworkpresent.ProtocolUrl.APP_LODAING_HEAD_IMAGE_URI;
import static com.henghao.hhworkpresent.utils.DateTimeUtils.transferDateTime;

/**
 * Created by bryanrady on 2017/3/10.
 * 考勤界面的考勤记录模块
 */

public class KaoqingFragment extends FragmentSupport {

    public static final String KAOQING_TIME = "com.henghao.kaoqing.time";

    private ImageLoader imageLoader;

    private DisplayImageOptions options;

    @ViewInject(R.id.kaoqing_headimage)
    private CircleImageView headImage;

    @ViewInject(R.id.kaoqing_name)
    private TextView tv_loginName;

    @ViewInject(R.id.tv_datepicker)
    private TextView tv_datepicker;

    @ViewInject(R.id.kaoqing_lookyueli)
    private Button lookBtn;

    private String textString;
    private String[] datas;
    private RadioOnClick listener = new RadioOnClick(0);

    private Handler mHandler = new Handler(){};

    @ViewInject(R.id.listview_kaoqing_chidao)
    private ListView chidaoListview;

    @ViewInject(R.id.listview_kaoqing_zaotui)
    private ListView zaotuiListview;

    @ViewInject(R.id.listview_kaoqing_queka)
    private ListView quekaListview;

    @ViewInject(R.id.listview_kaoqing_kuanggong)
    private ListView kuanggongListview;

    @ViewInject(R.id.tv_attendanceDays)
    private TextView tv_attendanceDays;

    @ViewInject(R.id.tv_normalPunchTimes)
    private TextView tv_normalPunchTimes;

    @ViewInject(R.id.tv_leaveEarlyDay)
    private TextView tv_leaveEarlyDay;

    @ViewInject(R.id.tv_missingCardTimes)
    private TextView tv_missingCardTimes;

    @ViewInject(R.id.tv_leaveDays)
    private TextView tv_leaveDays;

    @ViewInject(R.id.tv_kuanggongDays)
    private TextView tv_kuanggongDays;

    @ViewInject(R.id.tv_businessTravelDays)
    private TextView tv_businessTravelDays;

    @ViewInject(R.id.tv_lateTimes)
    private TextView tv_lateTimes;

    private SqliteDBUtils sqliteDBUtils;

    private MyBroadcastReceiver myBroadcastReceiver;

    private ChidaoListAdapter mChidaoAdapter;
    private ZaotuiListAdapter mZaotuiAdapter;
    private QuekaListAdapter mQuekaAdapter;
    private KuanggongListAdapter mKuanggongAdapter;

    private List<KaoqingEntity> mChidaoData,mZaotuiData,mQuekaData,mKuanggongData;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        this.mActivityFragmentView.viewMain(R.layout.fragment_kaoqing);
        this.mActivityFragmentView.viewEmpty(R.layout.activity_empty);
        this.mActivityFragmentView.viewEmptyGone();
        this.mActivityFragmentView.viewLoading(View.GONE);
        ViewUtils.inject(this, this.mActivityFragmentView);
        initWidget();
        initData();
        return this.mActivityFragmentView;
    }

    public void initWidget(){
        initWithBar();
        mLeftTextView.setText("考勤");
        mLeftTextView.setVisibility(View.VISIBLE);
        mLeftTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            mActivity.finish();
            }
        });
        mLeftImageView.setImageResource(R.drawable.item_point_left);
        mLeftImageView.setVisibility(View.VISIBLE);
        mLeftImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActivity.finish();
            }
        });

        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月");
        tv_datepicker.setText(format.format(date));
    }

    public void httpLoadingHeadImage(){
        OkHttpClient okHttpClient = new OkHttpClient();
        Request.Builder builder = new Request.Builder();
        FormEncodingBuilder requestBodyBuilder = new FormEncodingBuilder();
        requestBodyBuilder.add("uid",sqliteDBUtils.getLoginUid());
        RequestBody requestBody = requestBodyBuilder.build();
        String request_url = ProtocolUrl.ROOT_URL + "/"+ ProtocolUrl.APP_LODAING_HEAD_IMAGE;;
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
                    JSONObject jsonObject = new JSONObject(result_str);
                    int status = jsonObject.getInt("status");
                    if (status == 1) {
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
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
                         //       kaoqing_layout.setVisibility(View.VISIBLE);

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

    public void initData(){
        sqliteDBUtils = new SqliteDBUtils(mActivity);
        httpLoadingHeadImage();
        tv_loginName.setText(sqliteDBUtils.getLoginFirstName() + sqliteDBUtils.getLoginGiveName());

        myBroadcastReceiver = new MyBroadcastReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(KAOQING_TIME);
        mActivity.registerReceiver(myBroadcastReceiver, filter);

        datas = DateTimeUtils.getDynamicArray();

        mChidaoData = new ArrayList<>();
        mZaotuiData = new ArrayList<>();
        mQuekaData = new ArrayList<>();
        mKuanggongData = new ArrayList<>();

        mChidaoAdapter = new ChidaoListAdapter(mActivity, mChidaoData);
        mZaotuiAdapter = new ZaotuiListAdapter(mActivity,mZaotuiData);
        mQuekaAdapter = new QuekaListAdapter(mActivity, mQuekaData);
        mKuanggongAdapter = new KuanggongListAdapter(mActivity,mKuanggongData);
        setListViewHeightBasedOnChildren(chidaoListview);
        setListViewHeightBasedOnChildren(zaotuiListview);
        setListViewHeightBasedOnChildren(quekaListview);
        setListViewHeightBasedOnChildren(kuanggongListview);
        chidaoListview.setAdapter(mChidaoAdapter);
        zaotuiListview.setAdapter(mZaotuiAdapter);
        quekaListview.setAdapter(mQuekaAdapter);
        kuanggongListview.setAdapter(mKuanggongAdapter);
        mChidaoAdapter.notifyDataSetChanged();
        mZaotuiAdapter.notifyDataSetChanged();
        mQuekaAdapter.notifyDataSetChanged();
        mKuanggongAdapter.notifyDataSetChanged();

        initRequest();
        initEvent();
    }

    public void initRequest(){
        httpRequest();
        httpRequestChidao();
        httpRequestZaotui();
        httpRequestQueka();
        httpRequestKuanggong();
    }

    /**
     * 根据Item数设定ListView高度
     * @param listView
     */
    public void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * listAdapter.getCount());
        listView.setLayoutParams(params);
    }

    private void httpRequest(){
        OkHttpClient okHttpClient = new OkHttpClient();
        Request.Builder builder = new Request.Builder();
        FormEncodingBuilder requestBodyBuilder = new FormEncodingBuilder();
        requestBodyBuilder.add("userId", sqliteDBUtils.getLoginUid());
        requestBodyBuilder.add("date", DateTimeUtils.transferDateTime(tv_datepicker.getText().toString()));
        RequestBody requestBody = requestBodyBuilder.build();
        String request_url = ProtocolUrl.ROOT_URL + "/"+ ProtocolUrl.APP_QUERY_MOUNTH_KAOQING;
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
                    JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                    final String attendanceDays = jsonObject1.optString("qddays");
                    final String normalPunchTimes = jsonObject1.optString("normalCkdays");
                    final String leaveEarlyDay  = jsonObject1.optString("ztdays");
                    final String missingCardTimes = jsonObject1.optString("qkdays");
                    final String lateTimes = jsonObject1.optString("cddays");
                    final String kuanggongDays = jsonObject1.optString("kgdays");
                    final String businessTravelDays = jsonObject1.optString("ccdays");
                    final String leaveDays = jsonObject1.optString("qjdays");
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            tv_attendanceDays.setText(attendanceDays+"天");
                            tv_normalPunchTimes.setText(normalPunchTimes+"天");
                  //          tv_waiqingPunchTimes.setText(waiqingPunchTimes+"次");
                            tv_leaveEarlyDay.setText(leaveEarlyDay+"次");
                            tv_missingCardTimes.setText(missingCardTimes+"次");
                            tv_lateTimes.setText(lateTimes+"次");
                            tv_kuanggongDays.setText(kuanggongDays+"次");
                            tv_businessTravelDays.setText(businessTravelDays+"天");
                            tv_leaveDays.setText(leaveDays+"天");
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void httpRequestChidao() {
        OkHttpClient okHttpClient = new OkHttpClient();
        Request.Builder builder = new Request.Builder();
        FormEncodingBuilder requestBodyBuilder = new FormEncodingBuilder();
        requestBodyBuilder.add("userId", sqliteDBUtils.getLoginUid());
        requestBodyBuilder.add("date", transferDateTime(tv_datepicker.getText().toString()));
        RequestBody requestBody = requestBodyBuilder.build();
        String request_url = ProtocolUrl.ROOT_URL + "/"+ ProtocolUrl.APP_QUERY_MONTH_CHIDAO;
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
                    mChidaoData.clear();
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    for(int i=0;i<jsonArray.length();i++){
                        KaoqingEntity kaoqingEntity = new KaoqingEntity();
                        JSONObject dataObject = jsonArray.getJSONObject(i);
                        String currentDate = dataObject.optString("currentDate");
                        String workDay = dataObject.optString("week");
                        String clockInTime = dataObject.optString("clockInTime");
                        kaoqingEntity.setCurrentDate(currentDate);
                        kaoqingEntity.setWeek(workDay);
                        kaoqingEntity.setClockInTime(clockInTime);
                        mChidaoData.add(kaoqingEntity);
                    }
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            mChidaoAdapter.notifyDataSetChanged();
                            setListViewHeightBasedOnChildren(chidaoListview);
                            chidaoListview.setAdapter(mChidaoAdapter);
                            mActivityFragmentView.viewLoading(View.GONE);
                        }
                    });

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void httpRequestZaotui() {
        OkHttpClient okHttpClient = new OkHttpClient();
        Request.Builder builder = new Request.Builder();
        FormEncodingBuilder requestBodyBuilder = new FormEncodingBuilder();
        requestBodyBuilder.add("userId", sqliteDBUtils.getLoginUid());
        requestBodyBuilder.add("date", transferDateTime(tv_datepicker.getText().toString()));
        RequestBody requestBody = requestBodyBuilder.build();
        String request_url = ProtocolUrl.ROOT_URL + "/"+ ProtocolUrl.APP_QUERY_MONTH_ZAOTUI;
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
                    mZaotuiData.clear();
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    for(int i=0;i<jsonArray.length();i++){
                        KaoqingEntity kaoqingEntity = new KaoqingEntity();
                        JSONObject dataObject = jsonArray.getJSONObject(i);
                        String currentDate = dataObject.optString("currentDate");
                        String workDay = dataObject.optString("week");
                        String clockOutTime = dataObject.optString("clockOutTime");
                        kaoqingEntity.setCurrentDate(currentDate);
                        kaoqingEntity.setWeek(workDay);
                        kaoqingEntity.setClockOutTime(clockOutTime);
                        mZaotuiData.add(kaoqingEntity);
                    }
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            mZaotuiAdapter.notifyDataSetChanged();
                            setListViewHeightBasedOnChildren(zaotuiListview);
                            zaotuiListview.setAdapter(mZaotuiAdapter);
                            mActivityFragmentView.viewLoading(View.GONE);
                        }
                    });

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void httpRequestQueka() {
        OkHttpClient okHttpClient = new OkHttpClient();
        Request.Builder builder = new Request.Builder();
        FormEncodingBuilder requestBodyBuilder = new FormEncodingBuilder();
        requestBodyBuilder.add("userId", sqliteDBUtils.getLoginUid());
        requestBodyBuilder.add("date", transferDateTime(tv_datepicker.getText().toString()));
        RequestBody requestBody = requestBodyBuilder.build();
        String request_url = ProtocolUrl.ROOT_URL + "/"+ ProtocolUrl.APP_QUERY_MONTH_QUEKA;
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
                    mQuekaData.clear();
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
                    for(int i=0;i<jsonArray.length();i++){
                        KaoqingEntity kaoqingEntity = new KaoqingEntity();
                        JSONObject dataObject = jsonArray.getJSONObject(i);
                        String currentDate = dataObject.optString("currentDate");
                        String workDay = dataObject.optString("week");
                        String clockInTime = dataObject.optString("clockInTime");
                        //这时的clockInTime是一个null字符串 ，不是null
                        if(("null").equals(clockInTime)){
                            clockInTime = "09:00";
                        }else{
                            clockInTime = "17:00";
                        }
                        //如果日期不是当天
                        if(!date.equals(currentDate)){
                            kaoqingEntity.setClockInTime(clockInTime);
                            kaoqingEntity.setCurrentDate(currentDate);
                            kaoqingEntity.setWeek(workDay);
                            mQuekaData.add(kaoqingEntity);
                        }
                    }
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            mQuekaAdapter.notifyDataSetChanged();
                            setListViewHeightBasedOnChildren(quekaListview);
                            quekaListview.setAdapter(mQuekaAdapter);
                            mActivityFragmentView.viewLoading(View.GONE);
                        }
                    });

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void httpRequestKuanggong() {
        OkHttpClient okHttpClient = new OkHttpClient();
        Request.Builder builder = new Request.Builder();
        FormEncodingBuilder requestBodyBuilder = new FormEncodingBuilder();
        requestBodyBuilder.add("userId", sqliteDBUtils.getLoginUid());
        requestBodyBuilder.add("date", transferDateTime(tv_datepicker.getText().toString()));
        RequestBody requestBody = requestBodyBuilder.build();
        String request_url = ProtocolUrl.ROOT_URL + "/"+ ProtocolUrl.APP_QUERY_MONTH_KUANGGONG;
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
                    mKuanggongData.clear();
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    for(int i=0;i<jsonArray.length();i++){
                        KaoqingEntity kaoqingEntity = new KaoqingEntity();
                        JSONObject dataObject = jsonArray.getJSONObject(i);
                        String currentDate = dataObject.optString("stardate");
                        String workDay = dataObject.optString("weekend");
                        kaoqingEntity.setCurrentDate(currentDate);
                        kaoqingEntity.setWeek(workDay);
                        mKuanggongData.add(kaoqingEntity);
                    }
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            mKuanggongAdapter.notifyDataSetChanged();
                            setListViewHeightBasedOnChildren(kuanggongListview);
                            kuanggongListview.setAdapter(mKuanggongAdapter);
                            mActivityFragmentView.viewLoading(View.GONE);
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void initEvent(){
        chidaoListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String currentDate = mChidaoAdapter.getItem(position).getCurrentDate();
                String currentWeek = mChidaoAdapter.getItem(position).getWeek();
                Intent intent = new Intent();
                intent.putExtra("currentDate",currentDate);
                intent.putExtra("currentWeek",currentWeek);
                intent.setClass(getContext(), KaoqingDetailActivity.class);
                startActivity(intent);
            }
        });

        zaotuiListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String currentDate = mZaotuiAdapter.getItem(position).getCurrentDate();
                String currentWeek = mZaotuiAdapter.getItem(position).getWeek();
                Intent intent = new Intent();
                intent.putExtra("currentDate",currentDate);
                intent.putExtra("currentWeek",currentWeek);
                intent.setClass(getContext(), KaoqingDetailActivity.class);
                startActivity(intent);
            }
        });

        quekaListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String currentDate = mQuekaAdapter.getItem(position).getCurrentDate();
                String currentWeek = mQuekaAdapter.getItem(position).getWeek();
                Intent intent = new Intent();
                intent.putExtra("currentDate",currentDate);
                intent.putExtra("currentWeek",currentWeek);
                intent.setClass(getContext(), KaoqingDetailActivity.class);
                startActivity(intent);
            }
        });

        kuanggongListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String currentDate = mKuanggongAdapter.getItem(position).getCurrentDate();
                String currentWeek = mKuanggongAdapter.getItem(position).getWeek();
                Intent intent = new Intent();
                intent.putExtra("currentDate",currentDate);
                intent.putExtra("currentWeek",currentWeek);
                intent.setClass(getContext(), KaoqingKGDetailActivity.class);
                startActivity(intent);
            }
        });
    }

    @OnClick({R.id.tv_datepicker,R.id.kaoqing_lookyueli})
    private void viewOnClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.tv_datepicker:
                showSingleChoiceButton();
                break;

            case R.id.kaoqing_lookyueli:
                intent.setClass(getActivity(), CalendarActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mActivity.unregisterReceiver(myBroadcastReceiver);
    }

    class MyBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(KAOQING_TIME)) {
                initRequest();
            }
        }
    }

    /**
     * 展示单选对话框
     */
    public void showSingleChoiceButton() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        builder.setSingleChoiceItems(datas, listener.getIndex(), listener);
        builder.show();
    }

    class RadioOnClick implements DialogInterface.OnClickListener {

        private int index;

        public RadioOnClick(int index) {
            this.index = index;
        }

        public void setIndex(int index) {
            this.index = index;
        }

        public int getIndex() {
            return index;
        }

        @Override
        public void onClick(DialogInterface dialog, int whichButton){
            setIndex(whichButton);
            tv_datepicker.setText(datas[index]);
            Intent intent = new Intent(KAOQING_TIME);
            getContext().sendBroadcast(intent);
            dialog.dismiss();
        }

    }

}
