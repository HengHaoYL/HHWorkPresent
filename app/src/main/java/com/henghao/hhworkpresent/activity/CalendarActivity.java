package com.henghao.hhworkpresent.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.henghao.hhworkpresent.ActivityFragmentSupport;
import com.henghao.hhworkpresent.ProtocolUrl;
import com.henghao.hhworkpresent.R;
import com.henghao.hhworkpresent.adapter.CalendarViewAdapter;
import com.henghao.hhworkpresent.utils.CustomDate;
import com.henghao.hhworkpresent.views.MyCalendarView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
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

/**
 * Created by bryanrady on 2017/3/10.
 * 打卡月历界面
 */

public class CalendarActivity extends ActivityFragmentSupport implements MyCalendarView.OnCellClickListener {

    public static final String CALENDAR_TIME = "com.henghao.calendar.time";

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
        httpRequestKaoqingofDate();
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
                httpRequestKaoqingofDate();
            }
        }
    }

    /*public void queryKaoqingofDatetime(){
        String currentDatetime = tvCurrentDate.getText().toString();
        KaoqingProtocol kaoqingProtocol = new KaoqingProtocol(this);
        kaoqingProtocol.queryKaoqingofDatetime("1","2017-03-22");
        kaoqingProtocol.addResponseListener(this);
        mActivityFragmentView.viewLoading(View.VISIBLE);
    }*/


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

    }

    @Override
    public void initData() {
        super.initData();
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

    private void httpRequestKaoqingofDate() {
        OkHttpClient okHttpClient = new OkHttpClient();
        Request.Builder builder = new Request.Builder();
/*        SharedPreferences preferences = getSharedPreferences(Constant.SHARED_SET, 0);
        String UID = preferences.getString(Constant.USERID, null);*/
        FormEncodingBuilder requestBodyBuilder = new FormEncodingBuilder();
        requestBodyBuilder.add("uid", "1");
        requestBodyBuilder.add("date", "2017-03-22");
        RequestBody requestBody = requestBodyBuilder.build();
        String request_url = ProtocolUrl.ROOT_URL + "/"+ ProtocolUrl.APP_QUERY_DAY_OF_KAOQING;
        Log.d("wangqingbin","request_url=="+request_url);
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
                    final JSONObject dataObject = jsonObject.getJSONObject("data");
                    final String clockInTime = dataObject.optString("clockInTime");
                    final String clockOutTime = dataObject.optString("clockOutTime");
                    Log.d("wangqingbin","上班打卡=="+clockInTime);
                    Log.d("wangqingbin","下班打卡=="+clockOutTime);
                    //这时的clockInTime是一个null字符串 ，不是null
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            tv_shangbanTime.setText(clockInTime);
                            tv_xiabanTime.setText(clockOutTime);
                        }
                    });

                    //缺卡情况
                    if(("null").equals(clockInTime)){
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                tv_shangbanState.setText("缺卡");
                                tv_shangbanTime.setText("无");
                                btn_shangbanBuka.setVisibility(View.VISIBLE);
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
                                btn_xiabanBuka.setVisibility(View.VISIBLE);
                                mActivityFragmentView.viewLoading(View.GONE);
                            }
                        });
                    }

                    //上班迟到情况
                    if(!("null").equals(clockInTime)){
                        if(equalsStringShangban(clockInTime)){
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
                        if(equalsStringXiaban(clockOutTime)){
                            mHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    tv_xiabanState.setText("早退");
                                }
                            });
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
    public boolean equalsStringShangban(String clockInTime){
        //定义一个标准时间 09:00
        int[] arr = {9,0,0};
        String[] strings = clockInTime.split(":");
        int[] temp = new int[strings.length];
        //将字符数据转为int数组
        for (int i = 0; i < strings.length; i++) {
            temp[i]=Integer.parseInt(strings[i]);
        }
        //比较小时
        if (temp[0]>arr[0]) {
            return true;
        }
        if(temp[0]==arr[0]){
            //比较分钟
            if (temp[1]>arr[1]) {
                return true;
            }
            //如果分钟相等	9.0.0 , 9.0.0
            if (temp[1]==arr[1]) {
                //比较秒的用意，是为了对刚好在时间点打卡（如：9:00:00）的判断
                if (temp[2]>arr[2]) {
                    return true;
                }
            }

        }
        return false;
    }

    /**
     * 比较下班时间  早退返回true
     */
    public boolean equalsStringXiaban(String clockOutTime){
        //定义一个标准时间
        int[] arr = {18,0,0};
        String[] strings = clockOutTime.split(":");
        int[] temp = new int[strings.length];
        //将字符数据转为int数组
        for (int i = 0; i < strings.length; i++) {
            temp[i]=Integer.parseInt(strings[i]);
        }
        //只要是在18点之前，都属于早退，在18点之后，都属于正常下班
        if (temp[0]<arr[0]) {
            return true;
        }
        return false;
    }


}
