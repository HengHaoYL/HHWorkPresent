package com.henghao.hhworkpresent.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.henghao.hhworkpresent.ActivityFragmentSupport;
import com.henghao.hhworkpresent.ProtocolUrl;
import com.henghao.hhworkpresent.R;
import com.henghao.hhworkpresent.adapter.CalendarViewAdapter;
import com.henghao.hhworkpresent.utils.CustomDate;
import com.henghao.hhworkpresent.utils.SqliteDBUtils;
import com.henghao.hhworkpresent.views.CircleImageView;
import com.henghao.hhworkpresent.views.MyCalendarView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
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
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static com.henghao.hhworkpresent.ProtocolUrl.APP_LODAING_HEAD_IMAGE_URI;
import static com.henghao.hhworkpresent.fragment.DakaFragment.HOLIDAY_DIALOG_FALSE;
import static com.henghao.hhworkpresent.fragment.DakaFragment.HOLIDAY_FALSE;
import static com.henghao.hhworkpresent.fragment.DakaFragment.HOLIDAY_TRUE;

/**
 * Created by bryanrady on 2017/3/10.
 * 打卡月历界面
 */

public class CalendarActivity extends ActivityFragmentSupport implements MyCalendarView.OnCellClickListener {

    public static final String CALENDAR_TIME = "com.henghao.calendar.time";

    private ImageLoader imageLoader;

    private DisplayImageOptions options;

    @ViewInject(R.id.calendar_headimage)
    private CircleImageView headImage;

    @ViewInject(R.id.calendar_loginname)
    private TextView tv_loginName;

    @ViewInject(R.id.tv_current_date)
    private TextView tvCurrentDate;

    @ViewInject(R.id.tv_current_week)
    private TextView tvCurrentWeek;

    @ViewInject(R.id.vp_calendar)
    private ViewPager mViewPager;

    @ViewInject(R.id.kaoqing_calendar_dakatimes)
    private TextView tv_dakaTimes;

    @ViewInject(R.id.kaoqing_calendar_shangbantime)
    private TextView tv_shangbanTime;

    @ViewInject(R.id.kaoqing_calendar_shangbanstate)
    private TextView tv_shangbanState;

    @ViewInject(R.id.kaoqing_calendar_xiabantime)
    private TextView tv_xiabanTime;

    @ViewInject(R.id.kaoqing_calendar_xiabanstate)
    private TextView tv_xiabanState;

    @ViewInject(R.id.kaoqing_calendar_shangbanBuka)
    private Button btn_shangbanBuka;

    @ViewInject(R.id.kaoqing_calendar_xiabanBuka)
    private Button btn_xiabanBuka;

    @ViewInject(R.id.daka_shangban_layout)
    private RelativeLayout shangban_daka_layout;

    @ViewInject(R.id.daka_xiaban_layout)
    private RelativeLayout xiaban_daka_layout;

    @ViewInject(R.id.daka_qiandao_layout)
    private LinearLayout daka_qiandao_layout;

    /*@ViewInject(R.id.calendar_layout)
    private LinearLayout calendar_layout;*/

    private SqliteDBUtils sqliteDBUtils;

    private int mCurrentIndex = 498;
    private MyCalendarView[] mShowViews;
    private CalendarViewAdapter<MyCalendarView> adapter;
    private SildeDirection mDirection = SildeDirection.NO_SILDE;
    enum SildeDirection {
         RIGHT, LEFT, NO_SILDE;
    }

    private MyBroadcastReceiver myBroadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.mActivityFragmentView.viewMain(R.layout.activity_calendar);
        this.mActivityFragmentView.viewEmpty(R.layout.activity_empty);
        this.mActivityFragmentView.viewEmptyGone();
        this.mActivityFragmentView.viewLoading(View.GONE);
    //    this.mActivityFragmentView.viewLoadingError(View.GONE);
        this.mActivityFragmentView.clipToPadding(true);
        ViewUtils.inject(this, this.mActivityFragmentView);
        setContentView(this.mActivityFragmentView);
        initWidget();
        initData();
    }

    @Override
    public void clickDate(CustomDate date) {

    }

    @Override
    public void changeDate(CustomDate date) {
        String time = null;
        int year = date.year;
        int month = date.month;
        int dayOfMonth = date.day;
        String month1 = null;
        String dayOfMonth1 = null;
        time = year + "年" + month + "月" + dayOfMonth + "日";
        if(month<10 && dayOfMonth<10){
            month1 = "0"+ month;
            dayOfMonth1 = "0"+ dayOfMonth;
            time = year + "年" + month1 + "月" + dayOfMonth1+"日";
        }
        if(month<10 && dayOfMonth>=10){
            month1 = "0"+ month;
            dayOfMonth1 = dayOfMonth+"";
            time = year + "年" + month1 + "月" + dayOfMonth1+"日";
        }
        if(month>=10 && dayOfMonth<10){
            month1 = month+"";
            dayOfMonth1 = "0"+ dayOfMonth;
            time = year + "年" + month1 + "月" + dayOfMonth1+"日";
        }
        tvCurrentDate.setText(time);
        tvCurrentWeek.setText(getWeek(date+""));
    }

    @Override
    public void onResume() {
        super.onResume();
        //初始化数据前，先判断时候是家假日
        equalsHoliday(transferDateTime(tvCurrentDate.getText().toString().trim()));
    }

    class MyBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(CALENDAR_TIME)) {
                String time = intent.getStringExtra("time");
                String weekDay = intent.getStringExtra("weekDay");
                tvCurrentDate.setText(time);
                tvCurrentWeek.setText("星期"+weekDay);
                equalsHoliday(transferDateTime(tvCurrentDate.getText().toString().trim()));
            }
        }
    }

    @Override
    public void initWidget() {
        super.initWidget();
        initWithBar();
        mLeftTextView.setText("考勤月历");
        mLeftTextView.setVisibility(View.VISIBLE);
        mLeftTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        initWithRightBar();
        mRightTextView.setText("月汇总");
        mRightTextView.setVisibility(View.VISIBLE);
        mRightTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        /*initLoadingError();
        tv_viewLoadingError.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActivityFragmentView.viewLoadingError(View.GONE);
                httpLoadingHeadImage();
                httpRequestKaoqingofDate();
            }
        });*/

    }

    @Override
    public void initData() {
        super.initData();
        sqliteDBUtils = new SqliteDBUtils(this);

        httpLoadingHeadImage();
        tv_loginName.setText(sqliteDBUtils.getLoginFirstName() + sqliteDBUtils.getLoginGiveName());

        SimpleDateFormat f1 = new SimpleDateFormat("yyyy年MM月dd日");
        Date date = new Date();
        tvCurrentDate.setText(f1.format(date));
        SimpleDateFormat f2 = new SimpleDateFormat("EEEE");
        tvCurrentWeek.setText(f2.format(date));

        myBroadcastReceiver = new MyBroadcastReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(CALENDAR_TIME);
        registerReceiver(myBroadcastReceiver, filter);

        MyCalendarView[] views = new MyCalendarView[3];
        for (int i = 0; i < 3; i++) {
            views[i] = new MyCalendarView(this,this);
        }
        adapter = new CalendarViewAdapter<>(views);
        setViewPager();

        //初始化数据前，先判断时候是家假日
        equalsHoliday(transferDateTime(tvCurrentDate.getText().toString().trim()));
    }

    public Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == HOLIDAY_TRUE){
                mActivityFragmentView.viewLoading(View.GONE);
                carlendar_kaoing_layout.setVisibility(View.GONE);
                calendar_null_layout.setVisibility(View.VISIBLE);
            }else if(msg.what == HOLIDAY_FALSE){
                httpRequestKaoqingofDate();

            }else if(msg.what == HOLIDAY_DIALOG_FALSE){
                httpRequestKaoqingofDate();
            }
        }
    };

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
                        if((transferDateTime(tvCurrentDate.getText().toString())).equals(new SimpleDateFormat("yyyy-MM-dd").format(new Date()))){
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
                        /*calendar_layout.setVisibility(View.GONE);
                        mActivityFragmentView.viewLoadingError(View.VISIBLE);*/
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
                                mActivityFragmentView.viewLoadingError(View.VISIBLE);
                                msg("下载错误");
                            }
                        });
                    }
                    if(status == 0){
                        final String imageName = jsonObject.optString("data");
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                         //       calendar_layout.setVisibility(View.VISIBLE);

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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (myBroadcastReceiver != null) {
            unregisterReceiver(myBroadcastReceiver);
        }
    }

    private void setViewPager() {
        mViewPager.setAdapter(adapter);
        mViewPager.setCurrentItem(498);
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                measureDirection(position);
                updateCalendarView(position);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {

            }
        });
    }

    /**
     * 计算方向
     *
     * @param arg0
     */
    private void measureDirection(int arg0) {
        if (arg0 > mCurrentIndex) {
            mDirection = SildeDirection.RIGHT;
        } else if (arg0 < mCurrentIndex) {
            mDirection = SildeDirection.LEFT;
        }
        mCurrentIndex = arg0;
    }

    // 更新日历视图
    private void updateCalendarView(int arg0) {
        mShowViews = adapter.getAllItems();
        if (mDirection == SildeDirection.RIGHT) {
            mShowViews[arg0 % mShowViews.length].rightSlide();
        } else if (mDirection == SildeDirection.LEFT) {
            mShowViews[arg0 % mShowViews.length].leftSlide();
        }
        mDirection = SildeDirection.NO_SILDE;
    }

    public String getWeek(String sdate) {
        // 再转换为时间
        Date date = strToDate(sdate);
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        // int hour=c.get(Calendar.DAY_OF_WEEK);
        // hour中存的就是星期几了，其范围 1~7
        // 1=星期日 7=星期六，其他类推
        return new SimpleDateFormat("EEEE").format(c.getTime());
    }

    public Date strToDate(String strDate) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        ParsePosition pos = new ParsePosition(0);
        Date strtodate = formatter.parse(strDate, pos);
        return strtodate;
    }

    private Handler mHandler = new Handler(){};

    @ViewInject(R.id.calendar_kaoqing_layout)
    private LinearLayout carlendar_kaoing_layout;

    @ViewInject(R.id.calendar_null_layout)
    private RelativeLayout calendar_null_layout;

    private void httpRequestKaoqingofDate() {
        OkHttpClient okHttpClient = new OkHttpClient();
        Request.Builder builder = new Request.Builder();
        FormEncodingBuilder requestBodyBuilder = new FormEncodingBuilder();
        requestBodyBuilder.add("userId", sqliteDBUtils.getLoginUid());
        final String date = tvCurrentDate.getText().toString().trim();
        int type = equalsDate(transferDateTime(date));
        //大于当前日期：1，    等于当前日期：0，      小于当前日期：-1
        if(type==1||type==0){
            carlendar_kaoing_layout.setVisibility(View.GONE);
            calendar_null_layout.setVisibility(View.VISIBLE);
            return;
        }


        requestBodyBuilder.add("date", transferDateTime(date));
        RequestBody requestBody = requestBodyBuilder.build();
        String request_url = ProtocolUrl.ROOT_URL + "/"+ ProtocolUrl.APP_QUERY_DAY_OF_KAOQING;
        Request request = builder.url(request_url).post(requestBody).
                build();
        Call call = okHttpClient.newCall(request);
        mActivityFragmentView.viewLoading(View.VISIBLE);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mActivityFragmentView.viewLoading(View.GONE);
                        /*calendar_layout.setVisibility(View.GONE);
                        mActivityFragmentView.viewLoadingError(View.VISIBLE);*/
                    }
                });
            }

            @Override
            public void onResponse(Response response) throws IOException {
                String result_str = response.body().string();
                try {
                    JSONObject jsonObject = new JSONObject(result_str);
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
                                mActivityFragmentView.viewLoading(View.GONE);
                                carlendar_kaoing_layout.setVisibility(View.GONE);
                                calendar_null_layout.setVisibility(View.VISIBLE);
                            }
                        });

                    }else{
                        int status = jsonObject.getInt("status");
                        if (status == 0) {
                            mHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    mActivityFragmentView.viewLoading(View.GONE);
                                    carlendar_kaoing_layout.setVisibility(View.VISIBLE);
                                    calendar_null_layout.setVisibility(View.GONE);
                                }
                            });
                        }
                        final JSONObject dataObject = jsonObject1.getJSONObject("ck");
                        final String clockInTime = dataObject.optString("clockInTime");
                        final String clockOutTime = dataObject.optString("clockOutTime");
                        final String checkType = dataObject.optString("checkType");
                        //这时的clockInTime是一个null字符串 ，不是null
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                tv_shangbanTime.setText(clockInTime);
                                tv_xiabanTime.setText(clockOutTime);
                            }
                        });

                        if("请假".equals(checkType)){
                            mHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    tv_shangbanState.setText("请假");
                                    tv_shangbanTime.setText("无");
                                    tv_xiabanState.setText("请假");
                                    tv_xiabanTime.setText("无");
                                    mActivityFragmentView.viewLoading(View.GONE);
                                }
                            });
                            return;
                        }

                        if("出差".equals(checkType)){
                            mHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    tv_shangbanState.setText("出差");
                                    tv_shangbanTime.setText("无");
                                    tv_xiabanState.setText("出差");
                                    tv_xiabanTime.setText("无");
                                    mActivityFragmentView.viewLoading(View.GONE);
                                }
                            });
                            return;
                        }

                        if("补签".equals(checkType)){
                            mHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    tv_shangbanState.setText("补签");
                                    tv_shangbanTime.setText(shouldSBTime);
                                    tv_xiabanState.setText("补签");
                                    tv_xiabanTime.setText(shouldXBTime);
                                    mActivityFragmentView.viewLoading(View.GONE);
                                }
                            });
                            return;
                        }

                        //后台已经处理 这里的周末指的是上班的周末

                        if("周末".equals(checkType)){
                            mHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    mActivityFragmentView.viewLoading(View.GONE);
                                    carlendar_kaoing_layout.setVisibility(View.GONE);
                                    calendar_null_layout.setVisibility(View.VISIBLE);
                                }
                            });
                            return;
                        }

                        if("法定假日".equals(checkType)){
                            mHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    mActivityFragmentView.viewLoading(View.GONE);
                                    carlendar_kaoing_layout.setVisibility(View.GONE);
                                    calendar_null_layout.setVisibility(View.VISIBLE);
                                }
                            });
                            return;
                        }

                        if("旷工".equals(checkType)){
                            mHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    tv_shangbanState.setText("缺卡");
                                    tv_shangbanTime.setText("无");
                                    tv_xiabanState.setText("缺卡");
                                    tv_xiabanTime.setText("无");
                                    mActivityFragmentView.viewLoading(View.GONE);
                                }
                            });
                            return;
                        }

                        //缺卡情况
                        if(("null").equals(clockInTime)){
                            mHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    tv_shangbanState.setText("缺卡");
                                    tv_shangbanTime.setText("无");
                                    //        btn_shangbanBuka.setVisibility(View.VISIBLE);
                                    mActivityFragmentView.viewLoading(View.GONE);
                                }
                            });
                        }

                        if(("null").equals(clockOutTime)){
                            mHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    tv_xiabanState.setText("缺卡");
                                    tv_xiabanTime.setText("无");
                                    //        btn_xiabanBuka.setVisibility(View.VISIBLE);
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
                                        tv_shangbanState.setText("迟到");
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
                                        tv_xiabanState.setText("早退");
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
        //定义一个标准时间 09:00
    //    int[] arr = {9,0,0};
        String[] strings = clockInTime.split(":");
        String[] shangTimes = shouldSBTime.split(":");
        int[] temp = new int[strings.length];
        int[] shangTime = new int[shangTimes.length];
        //将字符数据转为int数组
        for (int i = 0; i < strings.length; i++) {
            temp[i]=Integer.parseInt(strings[i]);
        }
        for (int i = 0; i < shangTime.length; i++) {
            shangTime[i]=Integer.parseInt(shangTimes[i]);
        }
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
        String[] xiaTimes = shouldXBTime.split(":");
        int[] temp = new int[strings.length];
        int[] xiaTime = new int[xiaTimes.length];
        //将字符数据转为int数组
        for (int i = 0; i < strings.length; i++) {
            temp[i]=Integer.parseInt(strings[i]);
        }
        //将字符数据转为int数组
        for (int i = 0; i < xiaTime.length; i++) {
            xiaTime[i]=Integer.parseInt(xiaTimes[i]);
        }
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
