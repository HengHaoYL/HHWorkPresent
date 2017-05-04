package com.henghao.hhworkpresent.fragment;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.allenliu.badgeview.BadgeFactory;
import com.allenliu.badgeview.BadgeView;
import com.henghao.hhworkpresent.FragmentSupport;
import com.henghao.hhworkpresent.ProtocolUrl;
import com.henghao.hhworkpresent.R;
import com.henghao.hhworkpresent.activity.ChebanwenjianActivity;
import com.henghao.hhworkpresent.activity.DaibanrenlingActivity;
import com.henghao.hhworkpresent.activity.DaiyueshiyiActivity;
import com.henghao.hhworkpresent.activity.FaqishiyiActivity;
import com.henghao.hhworkpresent.activity.GerendaibanActivity;
import com.henghao.hhworkpresent.activity.KeyueshiyiActivity;
import com.henghao.hhworkpresent.activity.YibanshiyiActivity;
import com.henghao.hhworkpresent.activity.YiyueshiyiActivity;
import com.henghao.hhworkpresent.views.DatabaseHelper;
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

/**
 * 消息首页
 * Created by bryanrady on 2017/2/28.
 */

public class MsgFragment extends FragmentSupport {

    @ViewInject(R.id.scrollview_layout)
    private ScrollView scrollview_layout;

    @ViewInject(R.id.gerendaiban)
    private LinearLayout geredaiban;

    @ViewInject(R.id.faqishiyi)
    private LinearLayout faqishiyi;

    @ViewInject(R.id.keyueshiyi)
    private LinearLayout keyueshiyi;

    @ViewInject(R.id.yibanshiyi)
    private LinearLayout yibanshiyi;

    @ViewInject(R.id.daibanrenling)
    private LinearLayout daibanrenling;

    @ViewInject(R.id.daiyueshiyi)
    private LinearLayout daiyueshiyi;

    @ViewInject(R.id.chebanwenjian)
    private LinearLayout chebanwenjian;

    @ViewInject(R.id.yiyueshiyi)
    private LinearLayout yiyueshiyi;

    private int gerendaiban_count;
    private int faqishiyi_count;
    private int keyueshiyi_count;
    private int yibanshiyi_count;
    private int daibanrenling_count;
    private int daiyueshiyi_count;
    private int chebanwenjian_count;
    private int yiyueshiyi_count;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        this.mActivityFragmentView.viewMain(R.layout.fragment_msg);
        this.mActivityFragmentView.viewEmpty(R.layout.activity_empty);
        this.mActivityFragmentView.viewEmptyGone();
        this.mActivityFragmentView.viewLoading(View.GONE);
        this.mActivityFragmentView.viewLoadingError(View.GONE);
        ViewUtils.inject(this, this.mActivityFragmentView);
        initWidget();
        initData();
        return this.mActivityFragmentView;
    }

    public void initWidget(){
        initwithContent();
        //显示错误页面，点击重试
        initLoadingError();
        this.tv_viewLoadingError.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActivityFragmentView.viewLoadingError(View.GONE);
                httpRequesMsgCounts();
            }
        });

    }

    private void initwithContent() {
        initWithCenterBar();
        this.mCenterTextView.setVisibility(View.VISIBLE);
        this.mCenterTextView.setText("我的消息");
    }

    public void initData(){
        httpRequesMsgCounts();
    }

    @OnClick({R.id.gerendaiban,R.id.faqishiyi,R.id.keyueshiyi,R.id.yibanshiyi,
              R.id.daibanrenling,R.id.daiyueshiyi,R.id.chebanwenjian,R.id.yiyueshiyi})
    private void viewOnClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()){
            case R.id.gerendaiban:
                intent.setClass(mActivity, GerendaibanActivity.class);
                mActivity.startActivity(intent);
                break;

            case R.id.faqishiyi:
                intent.setClass(mActivity, FaqishiyiActivity.class);
                mActivity.startActivity(intent);
                break;

            case R.id.keyueshiyi:
                intent.setClass(mActivity, KeyueshiyiActivity.class);
                mActivity.startActivity(intent);
                break;

            case R.id.yibanshiyi:
                intent.setClass(mActivity, YibanshiyiActivity.class);
                mActivity.startActivity(intent);
                break;

            case R.id.daibanrenling:
                intent.setClass(mActivity, DaibanrenlingActivity.class);
                mActivity.startActivity(intent);
                break;

            case R.id.daiyueshiyi:
                intent.setClass(mActivity, DaiyueshiyiActivity.class);
                mActivity.startActivity(intent);
                break;

            case R.id.chebanwenjian:
                intent.setClass(mActivity, ChebanwenjianActivity.class);
                mActivity.startActivity(intent);
                break;

            case R.id.yiyueshiyi:
                intent.setClass(mActivity, YiyueshiyiActivity.class);
                mActivity.startActivity(intent);
                break;

        }
    }

    /**
     * 从本地数据库读取登录用户Id 用来作为数据请求id
     * @return
     */
    public String getLoginUid(){
        DatabaseHelper dbHelper = new DatabaseHelper(this.mActivity,"user_login.db");
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.query("user",new String[]{"uid"},null,null,null,null,null);
        String uid = null;
        while (cursor.moveToNext()){
            uid = cursor.getString((cursor.getColumnIndex("uid")));
        }
        return uid;
    }

    private Handler mHandler = new Handler(){};

    /**
     * 请求消息内容数量
     */
    private void httpRequesMsgCounts(){
        OkHttpClient okHttpClient = new OkHttpClient();
        Request.Builder builder = new Request.Builder();
        FormEncodingBuilder requestBodyBuilder = new FormEncodingBuilder();
        requestBodyBuilder.add("uid", getLoginUid());
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
                        mActivityFragmentView.viewLoadingError(View.VISIBLE);
                        scrollview_layout.setVisibility(View.GONE);
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
                                mActivityFragmentView.viewLoadingError(View.GONE);
                                mActivityFragmentView.viewLoading(View.GONE);
                                scrollview_layout.setVisibility(View.VISIBLE);
                            }
                        });
                        JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                        gerendaiban_count = Integer.parseInt(jsonObject1.optString("gerendaiban_count"));
                        faqishiyi_count = Integer.parseInt(jsonObject1.optString("faqishiyi_count"));
                        keyueshiyi_count = Integer.parseInt(jsonObject1.optString("keyueshiyi_count"));
                        yibanshiyi_count = Integer.parseInt(jsonObject1.optString("yibanshiyi_count"));
                        daibanrenling_count = Integer.parseInt(jsonObject1.optString("daibanrenling_count"));
                        chebanwenjian_count = Integer.parseInt(jsonObject1.optString("chebanwenjian_count"));
                        yiyueshiyi_count = Integer.parseInt(jsonObject1.optString("yiyueshiyi_count"));

                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                /**
                                 * 绘制消息数圆点
                                 * http://www.see-source.com/androidwidget/detail.html?wid=996
                                 */
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
                                            .setBadgeCount("99+")
                                            .setShape(BadgeView.SHAPE_CIRCLE)
                                            .bind(geredaiban);
                                }

                                if(faqishiyi_count>0){
                                    BadgeFactory.create(mActivity)
                                            .setTextColor(Color.WHITE)
                                            .setWidthAndHeight(20,20)
                                            .setBadgeBackground(Color.RED)
                                            .setTextSize(10)
                                            .setBadgeGravity(Gravity.RIGHT|Gravity.CENTER)
                                            .setBadgeCount(faqishiyi_count)
                                            .setShape(BadgeView.SHAPE_CIRCLE)
                                            .bind(faqishiyi);
                                }
                                if(faqishiyi_count>99){
                                    BadgeFactory.create(mActivity)
                                            .setTextColor(Color.WHITE)
                                            .setWidthAndHeight(20,20)
                                            .setBadgeBackground(Color.RED)
                                            .setTextSize(10)
                                            .setBadgeGravity(Gravity.RIGHT|Gravity.CENTER)
                                            .setBadgeCount("99+")
                                            .setShape(BadgeView.SHAPE_CIRCLE)
                                            .bind(faqishiyi);
                                }

                                if(keyueshiyi_count>0){
                                    BadgeFactory.create(mActivity)
                                            .setTextColor(Color.WHITE)
                                            .setWidthAndHeight(20,20)
                                            .setBadgeBackground(Color.RED)
                                            .setTextSize(10)
                                            .setBadgeGravity(Gravity.RIGHT|Gravity.CENTER)
                                            .setBadgeCount(keyueshiyi_count)
                                            .setShape(BadgeView.SHAPE_CIRCLE)
                                            .bind(keyueshiyi);
                                }
                                if(keyueshiyi_count>99){
                                    BadgeFactory.create(mActivity)
                                            .setTextColor(Color.WHITE)
                                            .setWidthAndHeight(20,20)
                                            .setBadgeBackground(Color.RED)
                                            .setTextSize(10)
                                            .setBadgeGravity(Gravity.RIGHT|Gravity.CENTER)
                                            .setBadgeCount("99+")
                                            .setShape(BadgeView.SHAPE_CIRCLE)
                                            .bind(keyueshiyi);
                                }

                                if(yibanshiyi_count>0){
                                    BadgeFactory.create(mActivity)
                                            .setTextColor(Color.WHITE)
                                            .setWidthAndHeight(20,20)
                                            .setBadgeBackground(Color.RED)
                                            .setTextSize(10)
                                            .setBadgeGravity(Gravity.RIGHT|Gravity.CENTER)
                                            .setBadgeCount(yibanshiyi_count)
                                            .setShape(BadgeView.SHAPE_CIRCLE)
                                            .bind(yibanshiyi);
                                }
                                if(yibanshiyi_count>99){
                                    BadgeFactory.create(mActivity)
                                            .setTextColor(Color.WHITE)
                                            .setWidthAndHeight(20,20)
                                            .setBadgeBackground(Color.RED)
                                            .setTextSize(10)
                                            .setBadgeGravity(Gravity.RIGHT|Gravity.CENTER)
                                            .setBadgeCount("99+")
                                            .setShape(BadgeView.SHAPE_CIRCLE)
                                            .bind(yibanshiyi);
                                }

                                if(daibanrenling_count>0){
                                    BadgeFactory.create(mActivity)
                                            .setTextColor(Color.WHITE)
                                            .setWidthAndHeight(20,20)
                                            .setBadgeBackground(Color.RED)
                                            .setTextSize(10)
                                            .setBadgeGravity(Gravity.RIGHT|Gravity.CENTER)
                                            .setBadgeCount(daibanrenling_count)
                                            .setShape(BadgeView.SHAPE_CIRCLE)
                                            .bind(daibanrenling);
                                }
                                if(daibanrenling_count>99){
                                    BadgeFactory.create(mActivity)
                                            .setTextColor(Color.WHITE)
                                            .setWidthAndHeight(20,20)
                                            .setBadgeBackground(Color.RED)
                                            .setTextSize(10)
                                            .setBadgeGravity(Gravity.RIGHT|Gravity.CENTER)
                                            .setBadgeCount("99+")
                                            .setShape(BadgeView.SHAPE_CIRCLE)
                                            .bind(daibanrenling);
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

                                if(chebanwenjian_count>0){
                                    BadgeFactory.create(mActivity)
                                            .setTextColor(Color.WHITE)
                                            .setWidthAndHeight(20,20)
                                            .setBadgeBackground(Color.RED)
                                            .setTextSize(10)
                                            .setBadgeGravity(Gravity.RIGHT|Gravity.CENTER)
                                            .setBadgeCount(chebanwenjian_count)
                                            .setShape(BadgeView.SHAPE_CIRCLE)
                                            .bind(chebanwenjian);
                                }
                                if(chebanwenjian_count>99){
                                    BadgeFactory.create(mActivity)
                                            .setTextColor(Color.WHITE)
                                            .setWidthAndHeight(20,20)
                                            .setBadgeBackground(Color.RED)
                                            .setTextSize(10)
                                            .setBadgeGravity(Gravity.RIGHT|Gravity.CENTER)
                                            .setBadgeCount("99+")
                                            .setShape(BadgeView.SHAPE_CIRCLE)
                                            .bind(chebanwenjian);
                                }

                                if(yiyueshiyi_count>0){
                                    BadgeFactory.create(mActivity)
                                            .setTextColor(Color.WHITE)
                                            .setWidthAndHeight(20,20)
                                            .setBadgeBackground(Color.RED)
                                            .setTextSize(10)
                                            .setBadgeGravity(Gravity.RIGHT|Gravity.CENTER)
                                            .setBadgeCount(yiyueshiyi_count)
                                            .setShape(BadgeView.SHAPE_CIRCLE)
                                            .bind(yiyueshiyi);
                                }
                                if(yiyueshiyi_count>99){
                                    BadgeFactory.create(mActivity)
                                            .setTextColor(Color.WHITE)
                                            .setWidthAndHeight(20,20)
                                            .setBadgeBackground(Color.RED)
                                            .setTextSize(10)
                                            .setBadgeGravity(Gravity.RIGHT|Gravity.CENTER)
                                            .setBadgeCount("99+")
                                            .setShape(BadgeView.SHAPE_CIRCLE)
                                            .bind(yiyueshiyi);
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
}
