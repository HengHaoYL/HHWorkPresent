package com.henghao.hhworkpresent.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.henghao.hhworkpresent.ActivityFragmentSupport;
import com.henghao.hhworkpresent.R;
import com.henghao.hhworkpresent.adapter.JianchaYinhuanListAdpter;
import com.henghao.hhworkpresent.entity.JPushToUser;
import com.henghao.hhworkpresent.entity.MeetingEntity;
import com.henghao.hhworkpresent.entity.SaveCheckTaskEntity;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * 会议审批界面
 * Created by ASUS on 2017/9/26.
 */

public class MeetingReviewActivity extends ActivityFragmentSupport {

    @ViewInject(R.id.tv_meeting_theme)
    private TextView tv_meeting_theme;

    @ViewInject(R.id.tv_meeting_faqiren)
    private TextView tv_meeting_faqiren;

    @ViewInject(R.id.tv_meeting_start_time)
    private TextView tv_meeting_start_time;

    @ViewInject(R.id.tv_meeting_duration)
    private TextView tv_meeting_duration;

    @ViewInject(R.id.tv_join_meeting_people_num)
    private TextView tv_join_meeting_people_num;

    @ViewInject(R.id.tv_join_meeting_people)
    private TextView tv_join_meeting_people;

    @ViewInject(R.id.tv_meeting_agree)
    private TextView tv_meeting_agree;

    @ViewInject(R.id.tv_meeting_reject)
    private TextView tv_meeting_reject;

    private long msg_id;
    private int mid;
    private Handler mHandler = new Handler(){};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.mActivityFragmentView.viewMain(R.layout.activity_meeting_review);
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
        initWithCenterBar();
        mCenterTextView.setText("会议审批");
        mCenterTextView.setVisibility(View.VISIBLE);

    }

    @Override
    public void initData() {
        super.initData();
        Intent intent = getIntent();
        msg_id = intent.getLongExtra("msg_id",0);
        httpRequestMeetingContent();
    }

    @OnClick({R.id.tv_meeting_agree,R.id.tv_meeting_reject})
    private void viewOnClick(View v) {
        switch (v.getId()){
            case R.id.tv_meeting_agree:
                httpRequestAgreeOrCancel(1);
                break;
            case R.id.tv_meeting_reject:
                httpRequestAgreeOrCancel(2);
                break;
        }
    }

    /**
     * 1代表同意 2代表不同意
     * 点击同意或取消会走的接口
     * @param whetherPass
     */
    public void httpRequestAgreeOrCancel(int whetherPass){
        //根据pid查询有隐患的被检查隐患文件列表
        OkHttpClient okHttpClient = new OkHttpClient();
        Request.Builder builder = new Request.Builder();
        String request_url = "http://172.16.0.81:8080/istration/JPush/updateMeetingEntityWhetherPass?mid=" +mid+"&whetherPass="+whetherPass;
        Request request = builder.url(request_url).build();
        Call call = okHttpClient.newCall(request);
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
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        msg("调用接口成功");
                    }
                });
            }

        });
    }

    /**
     * 从后台获取会议数据
     */
    public void httpRequestMeetingContent(){
        OkHttpClient okHttpClient = new OkHttpClient();
        Request.Builder builder = new Request.Builder();
        final String request_url = "http://172.16.0.81:8080/istration/JPush/queryMeetingEntityByMsgid?msg_id="+msg_id;
        Request request = builder.url(request_url).build();
        Call call = okHttpClient.newCall(request);
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
                    result_str = jsonObject.getString("data");
                    Gson gson = new Gson();
                    final MeetingEntity meetingEntity = gson.fromJson(result_str,MeetingEntity.class);
                    mid = meetingEntity.getMid();
                    final List<JPushToUser> jPushToUserList = meetingEntity.getjPushToUser();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tv_meeting_theme.setText(meetingEntity.getMeetingTheme());
                            for(JPushToUser jPushToUser : jPushToUserList){
                                if(jPushToUser.getMsg_id()==msg_id){
                                    tv_meeting_faqiren.setText(jPushToUser.getMessageSendPeople());
                                }
                            }
                            tv_meeting_start_time.setText(meetingEntity.getMeetingStartTime());
                            tv_meeting_duration.setText(meetingEntity.getMeetingDuration());
                            String name = meetingEntity.getUserIds();   //获取参会人员
                            String[] strings = name.split(",");
                            tv_join_meeting_people_num.setText(String.valueOf(strings.length));
                            tv_join_meeting_people.setText(name);
                        }
                    });

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        });
    }

}