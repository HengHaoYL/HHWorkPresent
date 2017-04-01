package com.henghao.hhworkpresent.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.henghao.hhworkpresent.ActivityFragmentSupport;
import com.henghao.hhworkpresent.ProtocolUrl;
import com.henghao.hhworkpresent.R;
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

/**
 * Created by bryanrady on 2017/3/17.
 *
 * 考勤详情界面
 */

public class KaoqingChidaoDetailActivity extends ActivityFragmentSupport {

    @ViewInject(R.id.kaoqing_chidao_username)
    private TextView tv_userName;

    @ViewInject(R.id.kaoqing_chidao_currentdate)
    private TextView tv_currentDate;

    @ViewInject(R.id.kaoqing_chidao_currentweek)
    private TextView tv_currentWeek;

    @ViewInject(R.id.kaoqing_chidao_shangbantime)
    private TextView tv_shangbanTime;

    @ViewInject(R.id.kaoqing_chidao_shangbanDakaState)
    private TextView tv_shangbanState;

    @ViewInject(R.id.kaoqing_chidao_xiabantime)
    private TextView tv_xiabanTime;

    @ViewInject(R.id.kaoqing_chidao_xiabanDakaState)
    private TextView tv_xiabanState;

    @ViewInject(R.id.kaoqing_chidao_xiabanBuka)
    private Button btn_shangbanBuka;

    @ViewInject(R.id.kaoqing_chidao_xiabanBuka)
    private Button btn_xiabanBuka;

    @ViewInject(R.id.kaoqing_chidao_xiabanLinear)
    private RelativeLayout xiabanLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.mActivityFragmentView.viewMain(R.layout.activity_kaoqingchidaodetail);
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
        mLeftTextView.setText("考勤详情");
        mLeftTextView.setVisibility(View.VISIBLE);
    }

    @Override
    public void initData() {
        super.initData();
        Intent intent = getIntent();
        String currentDate = intent.getStringExtra("currentDate");
        String currentWeek = intent.getStringExtra("currentWeek");
        tv_currentDate.setText(currentDate);
        tv_currentWeek.setText(currentWeek);
    }

    @Override
    public void onResume() {
        super.onResume();
        httpRequestKaoqingChidaoofDate();
    }

    private Handler mHandler = new Handler(){};

    private void httpRequestKaoqingChidaoofDate() {
        OkHttpClient okHttpClient = new OkHttpClient();
        Request.Builder builder = new Request.Builder();
/*        SharedPreferences preferences = getSharedPreferences(Constant.SHARED_SET, 0);
        String UID = preferences.getString(Constant.USERID, null);*/
        FormEncodingBuilder requestBodyBuilder = new FormEncodingBuilder();
        requestBodyBuilder.add("uid", "1");
        requestBodyBuilder.add("date", "2017-03-22");
        RequestBody requestBody = requestBodyBuilder.build();
        String request_url = ProtocolUrl.ROOT_URL + "/"+ ProtocolUrl.APP_QUERY_DAY_OF_CHIDAO;
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
