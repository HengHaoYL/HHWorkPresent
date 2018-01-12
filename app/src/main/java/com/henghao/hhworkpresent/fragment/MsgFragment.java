package com.henghao.hhworkpresent.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.allenliu.badgeview.BadgeFactory;
import com.allenliu.badgeview.BadgeView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.henghao.hhworkpresent.FragmentSupport;
import com.henghao.hhworkpresent.ProtocolUrl;
import com.henghao.hhworkpresent.R;
import com.henghao.hhworkpresent.WorkflowUrl;
import com.henghao.hhworkpresent.activity.GongGaoActivity;
import com.henghao.hhworkpresent.activity.MeetingManageActivity;
import com.henghao.hhworkpresent.activity.WebViewActivity;
import com.henghao.hhworkpresent.entity.JPushToUser;
import com.henghao.hhworkpresent.utils.NotificationUtils;
import com.henghao.hhworkpresent.utils.SqliteDBUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 消息首页
 * Created by bryanrady on 2017/2/28.
 */

public class MsgFragment extends FragmentSupport {

    @ViewInject(R.id.huiyiguanli)
    private LinearLayout huiyiguanli;

    @ViewInject(R.id.tongzhigonggao)
    private LinearLayout tongzhigonggao;

    @ViewInject(R.id.gerendaiban)
    private LinearLayout geredaiban;

    @ViewInject(R.id.daiyueshiyi)
    private LinearLayout daiyueshiyi;

    private int unread_gonggao_count;
    private int gerendaiban_count;
    private int faqishiyi_count;
    private int yibanshiyi_count;

    private SqliteDBUtils sqliteDBUtils;
    private NotificationUtils notificationUtils;

    private List<JPushToUser> jPushToUserList;

    //通知的唯一标识，在一个应用程序中不同的通知要区别开来
    public static final int NO_1 = 1001;    //通知公告
    public static final int NO_2 = 1002;    //需办理
    public static final int NO_3 = 1003;    //我发起
    public static final int NO_4 = 1004;    //审批过
    public static final int NO_5 = 1005;    //需阅知

    private Handler mHandler = new Handler(){};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        this.mActivityFragmentView.viewMain(R.layout.fragment_msg);
        this.mActivityFragmentView.viewEmpty(R.layout.activity_empty);
        this.mActivityFragmentView.viewEmptyGone();
        this.mActivityFragmentView.viewLoading(View.GONE);
        ViewUtils.inject(this, this.mActivityFragmentView);
        initWidget();
        initData();
        return this.mActivityFragmentView;
    }

    public void initWidget(){
        initWithCenterBar();
        this.mCenterTextView.setVisibility(View.VISIBLE);
        this.mCenterTextView.setText(getString(R.string.my_message));
    }

    public void initData(){
        jPushToUserList = new ArrayList<>();
        sqliteDBUtils = new SqliteDBUtils(mActivity);
        notificationUtils = new NotificationUtils(mActivity);
    }

    @Override
    public void onResume() {
        super.onResume();
        queryUnReadGonggao();
        httpRequesMsgCounts();
        queryUnReadMeeting();
    }

    @OnClick({R.id.huiyiguanli,R.id.tongzhigonggao,R.id.gerendaiban, R.id.daiyueshiyi})
    private void viewOnClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()){
            case R.id.huiyiguanli:
                intent.setClass(mActivity, MeetingManageActivity.class);
                mActivity.startActivity(intent);
                break;
            case R.id.tongzhigonggao:
                intent.setClass(mActivity, GongGaoActivity.class);
                mActivity.startActivity(intent);
                break;
            case R.id.gerendaiban:
                WebViewActivity.startToWebActivity(mActivity,getString(R.string.work_tobe_done),
                        WorkflowUrl.WORKFLOW_VIEW_URL+sqliteDBUtils.getUsername()+WorkflowUrl.GERENDAIBAN_VIEWID);
                break;
            case R.id.daiyueshiyi:
                WebViewActivity.startToWebActivity(mActivity,getString(R.string.matters_tobe_read),
                        WorkflowUrl.WORKFLOW_VIEW_URL+sqliteDBUtils.getUsername()+WorkflowUrl.DAIYUESHIYI_VIEWID);
                break;
        }
    }

    /**
     * 查询未读的推送消息
     */
    private void queryUnReadMeeting(){
        OkHttpClient okHttpClient = new OkHttpClient();
        Request.Builder builder = new Request.Builder();
        MultipartBuilder multipartBuilder = new MultipartBuilder();
        multipartBuilder.type(MultipartBuilder.FORM)
                .addFormDataPart("uid", sqliteDBUtils.getLoginUid());
        RequestBody requestBody = multipartBuilder.build();
        String request_url = ProtocolUrl.ROOT_URL + ProtocolUrl.APP_QUERY_UNREAD_MESSAGE;
        Request request = builder.post(requestBody).url(request_url).build();
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
                try {
                    JSONObject jsonObject = new JSONObject(result_str);
                    result_str = jsonObject.getString("data");
                    if(result_str!=null){
                        Gson gson = new Gson();
                        jPushToUserList.clear();
                        jPushToUserList = gson.fromJson(result_str,new TypeToken<ArrayList<JPushToUser>>() {}.getType());
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                if(jPushToUserList.size()>0){
                                    BadgeFactory.create(mActivity)
                                            .setTextColor(Color.WHITE)
                                            .setWidthAndHeight(20,20)
                                            .setBadgeBackground(Color.RED)
                                            .setTextSize(10)
                                            .setBadgeGravity(Gravity.RIGHT|Gravity.CENTER)
                                            .setBadgeCount(jPushToUserList.size())
                                            .setShape(BadgeView.SHAPE_CIRCLE)
                                            .bind(huiyiguanli);
                                }else if(jPushToUserList.size()==0){
                                    BadgeFactory.create(mActivity)
                                            .setWidthAndHeight(50,50)
                                            .setBadgeBackground(Color.WHITE)
                                            .setTextSize(0)
                                            .setBadgeGravity(Gravity.RIGHT|Gravity.CENTER)
                                            .setBadgeCount(getString(R.string.tv_null))
                                            .setShape(BadgeView.SHAPE_CIRCLE)
                                            .bind(huiyiguanli);
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

    /**
     * 查询未读公告数目
     */
    private void queryUnReadGonggao(){
        OkHttpClient okHttpClient = new OkHttpClient();
        Request.Builder builder = new Request.Builder();
        FormEncodingBuilder requestBodyBuilder = new FormEncodingBuilder();
        requestBodyBuilder.add("uid", sqliteDBUtils.getLoginUid());
        RequestBody requestBody = requestBodyBuilder.build();
        String request_url = ProtocolUrl.ROOT_URL + "/"+ ProtocolUrl.APP_QUERY_UNREAD_GONGGAO;
        Request request = builder.url(request_url).post(requestBody).build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
            }

            @Override
            public void onResponse(Response response) throws IOException {
                String result_str = response.body().string();
                try {
                    JSONObject jsonObject = new JSONObject(result_str);
                    int status = jsonObject.getInt("status");
                    if (status == 0) {
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        unread_gonggao_count = jsonArray.length();
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                if(unread_gonggao_count==0){
                                    BadgeFactory.create(mActivity)
                                            .setWidthAndHeight(50,50)
                                            .setBadgeBackground(Color.WHITE)
                                            .setTextSize(0)
                                            .setBadgeGravity(Gravity.RIGHT|Gravity.CENTER)
                                            .setBadgeCount("")
                                            .setShape(BadgeView.SHAPE_CIRCLE)
                                            .bind(tongzhigonggao);
                                }
                                if(unread_gonggao_count>0){
                                    BadgeFactory.create(mActivity)
                                            .setTextColor(Color.WHITE)
                                            .setWidthAndHeight(20,20)
                                            .setBadgeBackground(Color.RED)
                                            .setTextSize(10)
                                            .setBadgeGravity(Gravity.RIGHT|Gravity.CENTER)
                                            .setBadgeCount(unread_gonggao_count)
                                            .setShape(BadgeView.SHAPE_CIRCLE)
                                            .bind(tongzhigonggao);
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

    /**
     * 请求消息内容数量
     */
    private void httpRequesMsgCounts(){
        OkHttpClient okHttpClient = new OkHttpClient();
        Request.Builder builder = new Request.Builder();
        FormEncodingBuilder requestBodyBuilder = new FormEncodingBuilder();
        requestBodyBuilder.add("uid", sqliteDBUtils.getLoginUid());
        RequestBody requestBody = requestBodyBuilder.build();
        String request_url = ProtocolUrl.ROOT_URL + "/"+ ProtocolUrl.APP_REQUEST_MSG_LIST_COUNTS;
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
                    if (status == 0) {
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                mActivityFragmentView.viewLoading(View.GONE);
                            }
                        });
                        JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                        if(jsonObject1!=null){
                            if(!"".equals(jsonObject1.optString("gerendaiban_count"))){
                                gerendaiban_count = Integer.parseInt(jsonObject1.optString("gerendaiban_count"));
                            }
                            if(!"".equals(jsonObject1.optString("faqishiyi_count"))){
                                faqishiyi_count = Integer.parseInt(jsonObject1.optString("faqishiyi_count"));
                            }
                            if(!"".equals(jsonObject1.optString("yibanshiyi_count"))){
                                yibanshiyi_count = Integer.parseInt(jsonObject1.optString("yibanshiyi_count"));
                            }
                        }

                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                /**
                                 * 绘制消息数圆点
                                 * http://www.see-source.com/androidwidget/detail.html?wid=996
                                 */
                                if(gerendaiban_count==0){
                                    BadgeFactory.create(mActivity)
                                            .setWidthAndHeight(50,50)
                                            .setBadgeBackground(Color.WHITE)
                                            .setTextSize(0)
                                            .setBadgeGravity(Gravity.RIGHT|Gravity.CENTER)
                                            .setBadgeCount(getString(R.string.tv_null))
                                            .setShape(BadgeView.SHAPE_CIRCLE)
                                            .bind(geredaiban);
                                }
                                if(gerendaiban_count>0){
                                    BadgeFactory.create(mActivity)
                                            .setTextColor(Color.WHITE)
                                            .setWidthAndHeight(20,20)
                                            .setBadgeBackground(Color.RED)
                                            .setTextSize(10)
                                            .setBadgeGravity(Gravity.RIGHT|Gravity.CENTER)
                                            .setBadgeCount(gerendaiban_count)
                                            .setShape(BadgeView.SHAPE_CIRCLE)
                                            .bind(geredaiban);
                                }
                                if(gerendaiban_count>99){
                                    BadgeFactory.create(mActivity)
                                            .setTextColor(Color.WHITE)
                                            .setWidthAndHeight(20,20)
                                            .setBadgeBackground(Color.RED)
                                            .setTextSize(10)
                                            .setBadgeGravity(Gravity.RIGHT|Gravity.CENTER)
                                            .setBadgeCount(getString(R.string.more_message_count))
                                            .setShape(BadgeView.SHAPE_CIRCLE)
                                            .bind(geredaiban);
                                }

                                //待阅事宜直接显示小红点点
                                BadgeFactory.createDot(mActivity)
                                        .setTextColor(Color.WHITE)
                                        .setWidthAndHeight(10,10)
                                        .setBadgeBackground(Color.RED)
                                        .setTextSize(10)
                                        .setBadgeGravity(Gravity.RIGHT|Gravity.CENTER)
                                        .setShape(BadgeView.SHAPE_CIRCLE)
                                        .bind(daiyueshiyi);

                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

}
