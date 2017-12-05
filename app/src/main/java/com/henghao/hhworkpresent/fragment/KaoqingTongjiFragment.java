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
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.SDKInitializer;
import com.henghao.hhworkpresent.FragmentSupport;
import com.henghao.hhworkpresent.ProtocolUrl;
import com.henghao.hhworkpresent.R;
import com.henghao.hhworkpresent.activity.ChidaoZaotuiActivity;
import com.henghao.hhworkpresent.activity.DakaDetailActivity;
import com.henghao.hhworkpresent.activity.QingjiaChuchaiActivity;
import com.henghao.hhworkpresent.entity.AllChidaoEntity;
import com.henghao.hhworkpresent.entity.AllKuanggongEntity;
import com.henghao.hhworkpresent.entity.AllQuekaEntity;
import com.henghao.hhworkpresent.entity.AllZaotuiEntity;
import com.henghao.hhworkpresent.entity.ChidaoTongjiaEntity;
import com.henghao.hhworkpresent.entity.ChuchaiTongjiEntity;
import com.henghao.hhworkpresent.entity.QingjiaTongjiEntity;
import com.henghao.hhworkpresent.entity.WeiQiandaoEntity;
import com.henghao.hhworkpresent.entity.YiQiandanEntity;
import com.henghao.hhworkpresent.entity.ZaotuiTongjiaEntity;
import com.henghao.hhworkpresent.listener.OnDateChooseDialogListener;
import com.henghao.hhworkpresent.utils.DateTimeUtils;
import com.henghao.hhworkpresent.utils.LocationUtils;
import com.henghao.hhworkpresent.views.DateChooseDialog;
import com.henghao.hhworkpresent.views.MyChatView;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 考勤统计界面
 * Created by bryanrady on 2017/7/19.
 */

public class KaoqingTongjiFragment extends FragmentSupport {

    @ViewInject(R.id.kaoqingtongji_tabhost)
    private TabHost tabHost;


    /**
     *
     * 日统计
     *
     *
     */

    /*@ViewInject(R.id.mychatview)
    private MyChatView myChatView;*/

    @ViewInject(R.id.kaoqingtongji_datepicker)
    private TextView datepickerTV;

    @ViewInject(R.id.yidaka)
    private TextView yidaka;

    @ViewInject(R.id.weidaka)
    private TextView weidaka;

    @ViewInject(R.id.tv_chidao_tongji)
    private TextView chidaotongji;

    @ViewInject(R.id.tv_zaotui_tongji)
    private TextView zaotuitongji;

    @ViewInject(R.id.tv_qingjia_tongji)
    private TextView qingjiatongji;

    @ViewInject(R.id.tv_chuchai_tongji)
    private TextView chuchaitongji;

    @ViewInject(R.id.to_dakadetail_linear)
    private LinearLayout to_dakadetail_linear;

    @ViewInject(R.id.myview)
    private LinearLayout myView;

    private MyChatView myChatView;

    public Handler mHandler = new Handler(){};
    private String textString;
    private int total = 0;

    private List<WeiQiandaoEntity> mWeiqiandaoList;
    private List<YiQiandanEntity> mYiqiandaoList;
    private List<ChidaoTongjiaEntity> mChidaoList;
    private List<ZaotuiTongjiaEntity> mZaotuiList;
    private List<ChuchaiTongjiEntity> mChuchaiList;
    private List<QingjiaTongjiEntity> mQingjiaList;

    /**
     * 月统计
     *
     */
    @ViewInject(R.id.kaoqingrenshu)
    private TextView kaoqingrenshu;

    @ViewInject(R.id.tv_month_datepicker)
    private TextView tv_datepicker;

    @ViewInject(R.id.tv_chidao_size)
    private TextView tv_chidao_size;

    @ViewInject(R.id.tv_zaotui_size)
    private TextView tv_zaotui_size;

    @ViewInject(R.id.tv_queka_size)
    private TextView tv_queka_size;

    @ViewInject(R.id.tv_kuanggong_size)
    private TextView tv_kuanggong_size;

    private String[] datas;
    private RadioOnClick listener = new RadioOnClick(0);
    public static final String KAOQING_TIME = "com.henghao.kaoqingtongji.time";

    public List<AllChidaoEntity> allChidaoList;
    public List<AllZaotuiEntity> allZaotuiList;
    public List<AllQuekaEntity>  allQuekaList;
    public List<AllKuanggongEntity> allKuanggongList;

    public MyBroadcastReceiver myBroadcastReceiver;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        SDKInitializer.initialize(getActivity().getApplication().getApplicationContext());
        LocationUtils.Location(getActivity().getApplication().getApplicationContext());
        this.mActivityFragmentView.viewMain(R.layout.fragment_kaoqingtongji);
        this.mActivityFragmentView.viewEmpty(R.layout.activity_empty);
        this.mActivityFragmentView.viewEmptyGone();
        this.mActivityFragmentView.viewLoading(View.GONE);
        this.mActivityFragmentView.viewLoadingError(View.GONE);
        ViewUtils.inject(this, this.mActivityFragmentView);
        //注册定位监听  必须用全局 context  不能用 this.mActivity
        initWidget();
        initData();
        return this.mActivityFragmentView;
    }

    public void initWidget() {
        initWithBar();
        mLeftTextView.setText("人员考勤统计");
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

    }

    public static String[] str = new String[]{"已打卡","未打卡"};
 //   public static int[] strPercent = new int[2];
    public static List<Integer> strPercent = new ArrayList<Integer>();
    public static int[] mColor = new int[]{0xFF4FC3F7, 0xFF9575CD};

    public void initData() {
        //调用 TabHost.setup()
        tabHost.setup();
        //创建Tab标签
        tabHost.addTab(tabHost.newTabSpec("day").setIndicator("日统计").setContent(R.id.frame_day_tongji));
        tabHost.addTab(tabHost.newTabSpec("month").setIndicator("月统计").setContent(R.id.frame_month_tongji));

        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                if (tabId.equals("day")) {

                } else if (tabId.equals("month")) {

                }
            }
        });

        mWeiqiandaoList = new ArrayList<WeiQiandaoEntity>();
        mYiqiandaoList = new ArrayList<YiQiandanEntity>();
        mChidaoList = new ArrayList<ChidaoTongjiaEntity>();
        mZaotuiList = new ArrayList<ZaotuiTongjiaEntity>();
        mQingjiaList = new ArrayList<QingjiaTongjiEntity>();
        mChuchaiList = new ArrayList<ChuchaiTongjiEntity>();

        allChidaoList = new ArrayList<AllChidaoEntity>();
        allZaotuiList = new ArrayList<AllZaotuiEntity>();
        allQuekaList = new ArrayList<AllQuekaEntity>();
        allKuanggongList = new ArrayList<AllKuanggongEntity>();

        SimpleDateFormat f=new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        textString = f.format(date);
        datepickerTV.setText(textString);

        f = new SimpleDateFormat("yyyy年MM月");
        date = new Date();
        textString = f.format(date);
        tv_datepicker.setText(textString);

        myBroadcastReceiver = new MyBroadcastReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(KAOQING_TIME);
        mActivity.registerReceiver(myBroadcastReceiver, filter);

        datas = DateTimeUtils.getDynamicArray();
    }

    @Override
    public void onResume() {
        super.onResume();
        initHttp();
        initRequest();
    }

    public void initHttp(){
        httpRequestChidao();
        httpRequestZaotui();
        httpRequestChuchai();
        httpRequestQingjia();
        httpRequestYidaka();
    }

    private void setView(){
        myChatView = new MyChatView(mActivity);
        myView.addView(myChatView);
        myView.setVisibility(View.VISIBLE);

        myChatView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("date",datepickerTV.getText().toString());
                intent.setClass(mActivity,DakaDetailActivity.class);
                startActivity(intent);
            }
        });
    }

    @OnClick({R.id.kaoqingtongji_datepicker,R.id.tv_month_datepicker,R.id.to_dakadetail_linear,
            R.id.tv_chidao_tongji,R.id.tv_zaotui_tongji,R.id.tv_qingjia_tongji,R.id.tv_chuchai_tongji })
    private void viewOnClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.kaoqingtongji_datepicker:
                onClickDatePicker();
                break;

            case R.id.tv_month_datepicker:
                showSingleChoiceButton();
                break;

            case R.id.to_dakadetail_linear:
                intent.putExtra("date",datepickerTV.getText().toString());
                intent.setClass(mActivity,DakaDetailActivity.class);
                startActivity(intent);
                break;

            case R.id.tv_chidao_tongji:
                intent.putExtra("date",datepickerTV.getText().toString());
                intent.setClass(mActivity,ChidaoZaotuiActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_zaotui_tongji:
                intent.putExtra("date",datepickerTV.getText().toString());
                intent.setClass(mActivity,ChidaoZaotuiActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_qingjia_tongji:
                intent.putExtra("date",datepickerTV.getText().toString());
                intent.setClass(mActivity,QingjiaChuchaiActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_chuchai_tongji:
                intent.putExtra("date",datepickerTV.getText().toString());
                intent.setClass(mActivity,QingjiaChuchaiActivity.class);
                startActivity(intent);
                break;

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
                int type = DateTimeUtils.equalsDate(datepickerTV.getText().toString());
                //大于当前日期：1，    等于当前日期：0，      小于当前日期：-1
                if(type==0 || type == -1){
                    initHttp();
                } else if(type==1){
                    mActivity.msg("不能查看大于当前日期的考勤");
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
     * 查询已签到/已签到的人数
     */
    private void httpRequestYidaka() {
        OkHttpClient okHttpClient = new OkHttpClient();
        Request.Builder builder = new Request.Builder();
        FormEncodingBuilder requestBodyBuilder = new FormEncodingBuilder();
        requestBodyBuilder.add("date", datepickerTV.getText().toString());
        RequestBody requestBody = requestBodyBuilder.build();
        String request_url = ProtocolUrl.ROOT_URL + ProtocolUrl.APP_QUERY_YIQIANDAO;
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
                        mActivity.msg("网络访问错误！");
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
                    mYiqiandaoList.clear();
                    strPercent.clear();
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            myView.removeView(myChatView);
                        }
                    });
                    final JSONArray jsonArray = jsonObject.getJSONArray("data");
                    if(jsonArray.length()==0){
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                yidaka.setText("0");
                            }
                        });
                    }else{
                        for(int i=0;i<jsonArray.length();i++){
                            YiQiandanEntity yiQiandanEntity = new YiQiandanEntity();
                            JSONObject dataObject = jsonArray.getJSONObject(i);
                            String userId = dataObject.optString("userId");
                            String name = dataObject.optString("name");
                            String dept = dataObject.optString("dept");
                            String signedIn = dataObject.optString("signedIn");
                            yiQiandanEntity.setUserId(userId);
                            yiQiandanEntity.setName(name);
                            yiQiandanEntity.setDept(dept);
                            yiQiandanEntity.setSignedIn(signedIn);
                            mYiqiandaoList.add(yiQiandanEntity);
                        }
                    }

                    OkHttpClient okHttpClient = new OkHttpClient();
                    Request.Builder builder = new Request.Builder();
                    FormEncodingBuilder requestBodyBuilder = new FormEncodingBuilder();
                    requestBodyBuilder.add("date", datepickerTV.getText().toString());
                    RequestBody requestBody = requestBodyBuilder.build();
                    String request_url = ProtocolUrl.ROOT_URL + ProtocolUrl.APP_QUERY_WEIQIANDAO;
                    Request request = builder.url(request_url).post(requestBody).build();
                    Call call = okHttpClient.newCall(request);
                    call.enqueue(new Callback() {
                        @Override
                        public void onFailure(Request request, IOException e) {
                            mHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    mActivityFragmentView.viewLoading(View.GONE);
                                    Toast.makeText(mActivity, "网络访问错误！", Toast.LENGTH_SHORT).show();
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
                                mWeiqiandaoList.clear();
                                final JSONArray jsonArray1 = jsonObject.getJSONArray("data");
                                if(jsonArray1.length()==0){
                                    mHandler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            yidaka.setText("0");
                                        }
                                    });
                                }else{
                                    for(int i=0;i<jsonArray1.length();i++){
                                        WeiQiandaoEntity weiQiandaoEntity = new WeiQiandaoEntity();
                                        JSONObject dataObject = jsonArray1.getJSONObject(i);
                                        String userId = dataObject.optString("userId");
                                        String name = dataObject.optString("name");
                                        String dept = dataObject.optString("dept");
                                        String noSignIn = dataObject.optString("noSignIn");
                                        weiQiandaoEntity.setUserId(userId);
                                        weiQiandaoEntity.setName(name);
                                        weiQiandaoEntity.setDept(dept);
                                        weiQiandaoEntity.setNoSignIn(noSignIn);
                                        mWeiqiandaoList.add(weiQiandaoEntity);
                                    }
                                    mHandler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            yidaka.setText(String.valueOf(mYiqiandaoList.size()));
                                            weidaka.setText(String.valueOf(mWeiqiandaoList.size()));
                                            strPercent.add(mYiqiandaoList.size());
                                            strPercent.add(mWeiqiandaoList.size());
                                            kaoqingrenshu.setText("考勤人数："+(mYiqiandaoList.size()+mWeiqiandaoList.size())+"人");
                                        }
                                    });
                                }
                                mHandler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        if(jsonArray1.length()==0){
                                            strPercent.add(mYiqiandaoList.size());
                                            strPercent.add(0);
                                            kaoqingrenshu.setText("考勤人数："+mYiqiandaoList.size()+"人");
                                        }
                                        if (strPercent.size()==2)
                                            setView();

                                    }
                                });
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                    });

                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            if(jsonArray.length()==0){
                                mActivity.msg("暂时没有人打卡，请过后再查询");
                            }
                        }
                    });

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 迟到人数查询
     */
    private void httpRequestChidao() {
        OkHttpClient okHttpClient = new OkHttpClient();
        Request.Builder builder = new Request.Builder();
        FormEncodingBuilder requestBodyBuilder = new FormEncodingBuilder();
        requestBodyBuilder.add("date", datepickerTV.getText().toString());
        RequestBody requestBody = requestBodyBuilder.build();
        String request_url = ProtocolUrl.ROOT_URL + ProtocolUrl.APP_QUERY_CHIDAO;
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
                        mActivity.msg("网络访问错误！");
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
                    mChidaoList.clear();
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    if(jsonArray.length()==0){
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                chidaotongji.setText("0");
                            }
                        });
                    }else{
                        for(int i=0;i<jsonArray.length();i++){
                            ChidaoTongjiaEntity chidaoTongjiaEntity = new ChidaoTongjiaEntity();
                            JSONObject dataObject = jsonArray.getJSONObject(i);
                            String userId = dataObject.optString("userId");
                            String name = dataObject.optString("name");
                            String dept = dataObject.optString("dept");
                            String late = dataObject.optString("late");
                            String clockInTime = dataObject.optString("clockInTime");
                            chidaoTongjiaEntity.setUserId(userId);
                            chidaoTongjiaEntity.setName(name);
                            chidaoTongjiaEntity.setDept(dept);
                            chidaoTongjiaEntity.setLate(late);
                            chidaoTongjiaEntity.setClockInTime(clockInTime);
                            mChidaoList.add(chidaoTongjiaEntity);
                        }
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                chidaotongji.setText(String.valueOf(mChidaoList.size()));
                            }
                        });
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 早退人数查询
     */
    private void httpRequestZaotui() {
        OkHttpClient okHttpClient = new OkHttpClient();
        Request.Builder builder = new Request.Builder();
        FormEncodingBuilder requestBodyBuilder = new FormEncodingBuilder();
        requestBodyBuilder.add("date", datepickerTV.getText().toString());
        RequestBody requestBody = requestBodyBuilder.build();
        String request_url = ProtocolUrl.ROOT_URL + ProtocolUrl.APP_QUERY_ZAOTUI;
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
                        mActivity.msg("网络访问错误！");
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
                    mZaotuiList.clear();
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    if(jsonArray.length()==0){
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                zaotuitongji.setText("0");
                            }
                        });
                    } else{
                        for(int i=0;i<jsonArray.length();i++){
                            ZaotuiTongjiaEntity zaotuiTongjiaEntity = new ZaotuiTongjiaEntity();
                            JSONObject dataObject = jsonArray.getJSONObject(i);
                            String userId = dataObject.optString("userId");
                            String name = dataObject.optString("name");
                            String dept = dataObject.optString("dept");
                            String early = dataObject.optString("early");
                            String clockOutTime = dataObject.optString("clockOutTime");
                            zaotuiTongjiaEntity.setUserId(userId);
                            zaotuiTongjiaEntity.setName(name);
                            zaotuiTongjiaEntity.setDept(dept);
                            zaotuiTongjiaEntity.setEarly(early);
                            zaotuiTongjiaEntity.setClockOutTime(clockOutTime);
                            mZaotuiList.add(zaotuiTongjiaEntity);
                        }
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                zaotuitongji.setText(String.valueOf(mZaotuiList.size()));
                            }
                        });
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 请假人数查询
     */
    private void httpRequestQingjia() {
        OkHttpClient okHttpClient = new OkHttpClient();
        Request.Builder builder = new Request.Builder();
        FormEncodingBuilder requestBodyBuilder = new FormEncodingBuilder();
        requestBodyBuilder.add("date", datepickerTV.getText().toString());
        RequestBody requestBody = requestBodyBuilder.build();
        String request_url = ProtocolUrl.ROOT_URL + ProtocolUrl.APP_QUERY_QINGJIA;
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
                        mActivity.msg("网络访问错误！");
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
                    mQingjiaList.clear();
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    if(jsonArray.length()==0){
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                qingjiatongji.setText("0");
                            }
                        });
                    }else{
                        for(int i=0;i<jsonArray.length();i++){
                            QingjiaTongjiEntity qingjiaTongjiEntity = new QingjiaTongjiEntity();
                            JSONObject dataObject = jsonArray.getJSONObject(i);
                            String userId = dataObject.optString("userId");
                            String name = dataObject.optString("name");
                            String dept = dataObject.optString("dept");
                            String leave = dataObject.optString("leave");
                            qingjiaTongjiEntity.setUserId(userId);
                            qingjiaTongjiEntity.setName(name);
                            qingjiaTongjiEntity.setDept(dept);
                            qingjiaTongjiEntity.setLeave(leave);
                            mQingjiaList.add(qingjiaTongjiEntity);
                        }
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                qingjiatongji.setText(String.valueOf(mQingjiaList.size()));
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 出差人数查询
     */
    private void httpRequestChuchai() {
        OkHttpClient okHttpClient = new OkHttpClient();
        Request.Builder builder = new Request.Builder();
        FormEncodingBuilder requestBodyBuilder = new FormEncodingBuilder();
        requestBodyBuilder.add("date", datepickerTV.getText().toString());
        RequestBody requestBody = requestBodyBuilder.build();
        String request_url = ProtocolUrl.ROOT_URL + ProtocolUrl.APP_QUERY_CHUCHAI;
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
                        mActivity.msg("网络访问错误！");
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
                    mChuchaiList.clear();
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    if(jsonArray.length()==0){
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                chuchaitongji.setText("0");
                            }
                        });
                    }else{
                        for(int i=0;i<jsonArray.length();i++){
                            ChuchaiTongjiEntity kaoqingTongjiEntity = new ChuchaiTongjiEntity();
                            JSONObject dataObject = jsonArray.getJSONObject(i);
                            String userId = dataObject.optString("userId");
                            String name = dataObject.optString("name");
                            String dept = dataObject.optString("dept");
                            String evection = dataObject.optString("evection");
                            kaoqingTongjiEntity.setUserId(userId);
                            kaoqingTongjiEntity.setName(name);
                            kaoqingTongjiEntity.setDept(dept);
                            kaoqingTongjiEntity.setEvection(evection);
                            mChuchaiList.add(kaoqingTongjiEntity);
                        }
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                chuchaitongji.setText(String.valueOf(mChuchaiList.size()));
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
     * 月统计的网络请求
     */
    public void initRequest(){
        httpQueryChidaoList();
        httpQueryZaotuiList();
        httpQueryQuekaList();
        httpQueryKuanggongList();
    }

    /**
     * 根据月份查询所有人迟到集合
     */
    public void httpQueryChidaoList(){
        OkHttpClient okHttpClient = new OkHttpClient();
        Request.Builder builder = new Request.Builder();
        FormEncodingBuilder requestBodyBuilder = new FormEncodingBuilder();
        requestBodyBuilder.add("date", DateTimeUtils.transferDateTime(tv_datepicker.getText().toString()));
        RequestBody requestBody = requestBodyBuilder.build();
        String request_url = ProtocolUrl.ROOT_URL + ProtocolUrl.APP_QUERY_ALL_CHIDAOLIST;
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
                        mActivity.msg("网络访问错误！");
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
                    allChidaoList.clear();
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    if(jsonArray.length()==0){
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                tv_chidao_size.setText(0+"人");
                            }
                        });
                    }else{
                        final List<AllChidaoEntity> sameList = new ArrayList<AllChidaoEntity>();
                        for(int i=0;i<jsonArray.length();i++){
                            AllChidaoEntity allChidaoEntity = new AllChidaoEntity();
                            JSONObject dataObject = jsonArray.getJSONObject(i);
                            String userId = dataObject.optString("userId");
                            String name = dataObject.optString("name");
                            String dept = dataObject.optString("dept");
                            String late = dataObject.optString("late");
                            String clockInTime = dataObject.optString("clockInTime");
                            String currentDate = dataObject.optString("currentDate");
                            allChidaoEntity.setUserId(userId);
                            allChidaoEntity.setName(name);
                            allChidaoEntity.setDept(dept);
                            allChidaoEntity.setLate(late);
                            allChidaoEntity.setClockInTime(clockInTime);
                            allChidaoEntity.setCurrentDate(currentDate);
                            allChidaoList.add(allChidaoEntity);
                            sameList.add(allChidaoEntity);
                        }

                        /**
                         * 去除id相同的元素 获取这个月旷工的总人数
                         */
                        for(int m = 0; m<sameList.size()-1;m++){
                            for (int j = sameList.size()-1; j > m;j--){
                                if(sameList.get(j).getUserId().equals(sameList.get(m).getUserId())){
                                    sameList.remove(j);
                                }
                            }
                        }

                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                tv_chidao_size.setText(sameList.size()+"人");
                            /*mChidaoAdapter.notifyDataSetChanged();
                            setListViewHeightBasedOnChildren(chidaoListview);
                            chidaoListview.setAdapter(mChidaoAdapter);*/
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

    /**
     * 根据月份查询所有人早退集合
     */
    public void httpQueryZaotuiList(){
        OkHttpClient okHttpClient = new OkHttpClient();
        Request.Builder builder = new Request.Builder();
        FormEncodingBuilder requestBodyBuilder = new FormEncodingBuilder();
        requestBodyBuilder.add("date", DateTimeUtils.transferDateTime(tv_datepicker.getText().toString()));
        RequestBody requestBody = requestBodyBuilder.build();
        String request_url = ProtocolUrl.ROOT_URL + ProtocolUrl.APP_QUERY_ALL_ZAOTUILIST;
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
                        mActivity.msg("网络访问错误！");
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
                    allZaotuiList.clear();
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    if(jsonArray.length()==0){
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                tv_zaotui_size.setText(0+"人");
                            }
                        });
                    }else{
                        final List<AllZaotuiEntity> sameList = new ArrayList<AllZaotuiEntity>();
                        for(int i=0;i<jsonArray.length();i++){
                            AllZaotuiEntity allZaotuiEntity = new AllZaotuiEntity();
                            JSONObject dataObject = jsonArray.getJSONObject(i);
                            String userId = dataObject.optString("userId");
                            String name = dataObject.optString("name");
                            String dept = dataObject.optString("dept");
                            String early = dataObject.optString("early");
                            String clockOutTime = dataObject.optString("clockOutTime");
                            String currentDate = dataObject.optString("currentDate");
                            allZaotuiEntity.setUserId(userId);
                            allZaotuiEntity.setName(name);
                            allZaotuiEntity.setDept(dept);
                            allZaotuiEntity.setEarly(early);
                            allZaotuiEntity.setClockOutTime(clockOutTime);
                            allZaotuiEntity.setCurrentDate(currentDate);
                            allZaotuiList.add(allZaotuiEntity);
                            sameList.add(allZaotuiEntity);
                        }

                        /**
                         * 去除id相同的元素 获取这个月旷工的总人数
                         */
                        for(int m = 0; m<sameList.size()-1;m++){
                            for (int j = sameList.size()-1; j > m;j--){
                                if(sameList.get(j).getUserId().equals(sameList.get(m).getUserId())){
                                    sameList.remove(j);
                                }
                            }
                        }
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                tv_zaotui_size.setText(sameList.size()+"人");
                            /*mChidaoAdapter.notifyDataSetChanged();
                            setListViewHeightBasedOnChildren(chidaoListview);
                            chidaoListview.setAdapter(mChidaoAdapter);*/
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

    /**
     * 根据月份查询所有人缺卡集合
     */
    public void httpQueryQuekaList(){
        OkHttpClient okHttpClient = new OkHttpClient();
        Request.Builder builder = new Request.Builder();
        FormEncodingBuilder requestBodyBuilder = new FormEncodingBuilder();
        requestBodyBuilder.add("date", DateTimeUtils.transferDateTime(tv_datepicker.getText().toString()));
        RequestBody requestBody = requestBodyBuilder.build();
        String request_url = ProtocolUrl.ROOT_URL + ProtocolUrl.APP_QUERY_ALL_QUEKALIST;
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
                        mActivity.msg("网络访问错误！");
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
                    allQuekaList.clear();
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    if(jsonArray.length()==0){
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                tv_queka_size.setText(0+"人");
                            }
                        });
                    }else{
                        final List<AllQuekaEntity> sameList = new ArrayList<AllQuekaEntity>();
                        for(int i=0;i<jsonArray.length();i++){
                            AllQuekaEntity allQuekaEntity = new AllQuekaEntity();
                            JSONObject dataObject = jsonArray.getJSONObject(i);
                            String userId = dataObject.optString("userId");
                            String name = dataObject.optString("name");
                            String dept = dataObject.optString("dept");
                            String clockInTime = dataObject.optString("clockInTime");
                            String clockOutTime = dataObject.optString("clockOutTime");
                            String currentDate = dataObject.optString("currentDate");
                            allQuekaEntity.setUserId(userId);
                            allQuekaEntity.setName(name);
                            allQuekaEntity.setDept(dept);
                            allQuekaEntity.setClockInTime(clockInTime);
                            allQuekaEntity.setClockOutTime(clockOutTime);
                            allQuekaEntity.setCurrentDate(currentDate);
                            allQuekaList.add(allQuekaEntity);
                            sameList.add(allQuekaEntity);
                        }

                        /**
                         * 去除id相同的元素 获取这个月旷工的总人数
                         */
                        for(int m = 0; m<sameList.size()-1;m++){
                            for (int j = sameList.size()-1; j > m;j--){
                                if(sameList.get(j).getUserId().equals(sameList.get(m).getUserId())){
                                    sameList.remove(j);
                                }
                            }
                        }
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                tv_queka_size.setText(sameList.size()+"人");
                            /*mChidaoAdapter.notifyDataSetChanged();
                            setListViewHeightBasedOnChildren(chidaoListview);
                            chidaoListview.setAdapter(mChidaoAdapter);*/
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

    /**
     * 根据月份查询所有人旷工集合
     */
    public void httpQueryKuanggongList(){
        OkHttpClient okHttpClient = new OkHttpClient();
        Request.Builder builder = new Request.Builder();
        FormEncodingBuilder requestBodyBuilder = new FormEncodingBuilder();
        requestBodyBuilder.add("date", DateTimeUtils.transferDateTime(tv_datepicker.getText().toString()));
        RequestBody requestBody = requestBodyBuilder.build();
        String request_url = ProtocolUrl.ROOT_URL + ProtocolUrl.APP_QUERY_ALL_KUANGGONGLIST;
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
                        mActivity.msg("网络访问错误!");
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
                    allKuanggongList.clear();
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    if(jsonArray.length()==0){
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                tv_kuanggong_size.setText(0+"人");
                            }
                        });
                    }else{
                        final List<AllKuanggongEntity> sameList = new ArrayList<AllKuanggongEntity>();
                        for(int i=0;i<jsonArray.length();i++){
                            AllKuanggongEntity allKuanggongEntity = new AllKuanggongEntity();
                            JSONObject dataObject = jsonArray.getJSONObject(i);
                            String userId = dataObject.optString("userID");
                            String name = dataObject.optString("name");
                            String dept = dataObject.optString("dept");
                            String absenteeismDate = dataObject.optString("absenteeismDate");
                            String absenteeism = dataObject.optString("absenteeism");
                            allKuanggongEntity.setUserID(userId);
                            allKuanggongEntity.setName(name);
                            allKuanggongEntity.setDept(dept);
                            allKuanggongEntity.setAbsenteeismDate(absenteeismDate);
                            allKuanggongEntity.setAbsenteeism(absenteeism);
                            allKuanggongList.add(allKuanggongEntity);
                            sameList.add(allKuanggongEntity);

                        }

                        /**
                         * 去除id相同的元素 获取这个月旷工的总人数
                         */
                        for(int m = 0; m<sameList.size()-1;m++){
                            for (int j = sameList.size()-1; j > m;j--){
                                if(sameList.get(j).getUserID().equals(sameList.get(m).getUserID())){
                                    sameList.remove(j);
                                }
                            }
                        }

                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                tv_kuanggong_size.setText(sameList.size()+"人");
                            /*mChidaoAdapter.notifyDataSetChanged();
                            setListViewHeightBasedOnChildren(chidaoListview);
                            chidaoListview.setAdapter(mChidaoAdapter);*/
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
            mActivity.sendBroadcast(intent);
            dialog.dismiss();

        }

    }

}
