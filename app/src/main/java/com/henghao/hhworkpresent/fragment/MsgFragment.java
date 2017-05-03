package com.henghao.hhworkpresent.fragment;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.allenliu.badgeview.BadgeFactory;
import com.allenliu.badgeview.BadgeView;
import com.henghao.hhworkpresent.FragmentSupport;
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

/**
 * 消息首页
 * Created by bryanrady on 2017/2/28.
 */

public class MsgFragment extends FragmentSupport {

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

            }
        });

    }

    private void initwithContent() {
        initWithCenterBar();
        this.mCenterTextView.setVisibility(View.VISIBLE);
        this.mCenterTextView.setText("我的消息");
    }

    public void initData(){
        drawCriclePoint();
    }

    /**
     * 绘制消息数圆点
     */
    public void drawCriclePoint(){
        BadgeFactory.create(this.mActivity)
                .setTextColor(Color.WHITE)
                .setWidthAndHeight(25,25)
                .setBadgeBackground(Color.RED)
                .setTextSize(10)
                .setBadgeGravity(Gravity.RIGHT|Gravity.CENTER)
                .setBadgeCount(20)
                .setShape(BadgeView.SHAPE_CIRCLE)
                .bind(geredaiban);

        BadgeFactory.create(this.mActivity)
                .setTextColor(Color.WHITE)
                .setWidthAndHeight(25,25)
                .setBadgeBackground(Color.RED)
                .setTextSize(10)
                .setBadgeGravity(Gravity.RIGHT|Gravity.CENTER)
                .setBadgeCount(20)
                .setShape(BadgeView.SHAPE_CIRCLE)
                .bind(faqishiyi);

        BadgeFactory.create(this.mActivity)
                .setTextColor(Color.WHITE)
                .setWidthAndHeight(25,25)
                .setBadgeBackground(Color.RED)
                .setTextSize(10)
                .setBadgeGravity(Gravity.RIGHT|Gravity.CENTER)
                .setBadgeCount(20)
                .setShape(BadgeView.SHAPE_CIRCLE)
                .bind(keyueshiyi);

        BadgeFactory.create(this.mActivity)
                .setTextColor(Color.WHITE)
                .setWidthAndHeight(25,25)
                .setBadgeBackground(Color.RED)
                .setTextSize(10)
                .setBadgeGravity(Gravity.RIGHT|Gravity.CENTER)
                .setBadgeCount(20)
                .setShape(BadgeView.SHAPE_CIRCLE)
                .bind(yibanshiyi);

        BadgeFactory.create(this.mActivity)
                .setTextColor(Color.WHITE)
                .setWidthAndHeight(25,25)
                .setBadgeBackground(Color.RED)
                .setTextSize(10)
                .setBadgeGravity(Gravity.RIGHT|Gravity.CENTER)
                .setBadgeCount(20)
                .setShape(BadgeView.SHAPE_CIRCLE)
                .bind(daibanrenling);

        BadgeFactory.create(this.mActivity)
                .setTextColor(Color.WHITE)
                .setWidthAndHeight(25,25)
                .setBadgeBackground(Color.RED)
                .setTextSize(10)
                .setBadgeGravity(Gravity.RIGHT|Gravity.CENTER)
                .setBadgeCount(20)
                .setShape(BadgeView.SHAPE_CIRCLE)
                .bind(daiyueshiyi);

        BadgeFactory.create(this.mActivity)
                .setTextColor(Color.WHITE)
                .setWidthAndHeight(25,25)
                .setBadgeBackground(Color.RED)
                .setTextSize(10)
                .setBadgeGravity(Gravity.RIGHT|Gravity.CENTER)
                .setBadgeCount(20)
                .setShape(BadgeView.SHAPE_CIRCLE)
                .bind(chebanwenjian);

        BadgeFactory.create(this.mActivity)
                .setTextColor(Color.WHITE)
                .setWidthAndHeight(25,25)
                .setBadgeBackground(Color.RED)
                .setTextSize(10)
                .setBadgeGravity(Gravity.RIGHT|Gravity.CENTER)
                .setBadgeCount(20)
                .setShape(BadgeView.SHAPE_CIRCLE)
                .bind(yiyueshiyi);
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

    /*private Handler mHandler = new Handler(){};

    private void httpRequestMsgList() {
        OkHttpClient okHttpClient = new OkHttpClient();
        Request.Builder builder = new Request.Builder();
        FormEncodingBuilder requestBodyBuilder = new FormEncodingBuilder();
        requestBodyBuilder.add("uid", getLoginUid());
        RequestBody requestBody = requestBodyBuilder.build();
        String request_url = ProtocolUrl.ROOT_URL + "/"+ ProtocolUrl.APP_REQUEST_MSG_LIST;
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
                    }
                });
            }

            @Override
            public void onResponse(Response response) throws IOException {
                String result_str = response.body().string();
                try {
                    JSONObject jsonObject = new JSONObject(result_str);
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    if(("null").equals(jsonArray)||jsonArray==null){
                        mActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mActivityFragmentView.viewLoading(View.GONE);
                                mActivityFragmentView.viewMainGone();
                            }
                        });
                        return;
                    }
                    mList.clear();
                    for(int i=0;i<jsonArray.length();i++){
                        MsgEntity msgEntity = new MsgEntity();
                        JSONObject dataObject = jsonArray.getJSONObject(i);
                        String sendusername = dataObject.optString("sendusername");
                        String sendtime = dataObject.optString("sendtime");
                        String title = dataObject.optString("title");
                        msgEntity.setSendusername(sendusername);
                        msgEntity.setSendtime(sendtime);
                        msgEntity.setTitle(title);
                        mList.add(msgEntity);
                    }
                    mActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mAdapter.notifyDataSetChanged();
                            mActivityFragmentView.viewLoading(View.GONE);
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }*/
}
