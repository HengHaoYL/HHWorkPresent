package com.henghao.hhworkpresent.fragment;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.benefit.buy.library.views.xlistview.XListView;
import com.henghao.hhworkpresent.FragmentSupport;
import com.henghao.hhworkpresent.ProtocolUrl;
import com.henghao.hhworkpresent.R;
import com.henghao.hhworkpresent.activity.MsgDetailActivity;
import com.henghao.hhworkpresent.adapter.MsgNotificationAdapter;
import com.henghao.hhworkpresent.entity.MsgEntity;
import com.henghao.hhworkpresent.views.DatabaseHelper;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
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
import java.util.ArrayList;
import java.util.List;

/**
 * 消息首页
 * Created by bryanrady on 2017/2/28.
 */

public class MsgFragment extends FragmentSupport {

    @ViewInject(R.id.fragment_msg_listview)
    private XListView mXlistView;

    private List<MsgEntity> mList;

    private MsgNotificationAdapter mAdapter;

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
                httpRequestMsgList();
            }
        });

        mXlistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.setClass(getContext(), MsgDetailActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initwithContent() {
        initWithCenterBar();
        this.mCenterTextView.setVisibility(View.VISIBLE);
        this.mCenterTextView.setText("我的消息");
    }

    public void initData(){
        mList = new ArrayList<>();
        mAdapter = new MsgNotificationAdapter(this.mActivity, mList);
        mXlistView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();

        httpRequestMsgList();
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
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            mAdapter.notifyDataSetChanged();
                            mXlistView.setAdapter(mAdapter);
                            mActivityFragmentView.viewLoading(View.GONE);
                        }
                    });

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
