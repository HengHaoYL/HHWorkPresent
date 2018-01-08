package com.henghao.hhworkpresent.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.widget.TabHost;
import android.widget.Toast;

import com.benefit.buy.library.views.xlistview.XListView;
import com.henghao.hhworkpresent.ActivityFragmentSupport;
import com.henghao.hhworkpresent.ProtocolUrl;
import com.henghao.hhworkpresent.R;
import com.henghao.hhworkpresent.adapter.ChidaoTongjiaListAdapter;
import com.henghao.hhworkpresent.adapter.ZaotuiTongjiListAdapter;
import com.henghao.hhworkpresent.entity.ChidaoTongjiaEntity;
import com.henghao.hhworkpresent.entity.ZaotuiTongjiaEntity;
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
 * Created by bryanrady on 2017/7/27.
 */

public class ChidaoZaotuiActivity extends ActivityFragmentSupport {

    private TabHost tabHost;

    @ViewInject(R.id.chidao_listview)
    private XListView chidao_listView;

    @ViewInject(R.id.zaotui_listview)
    private XListView zaotui_listView;

    public List<ChidaoTongjiaEntity> chidaoList;
    public List<ZaotuiTongjiaEntity> zaotuiList;

    public ChidaoTongjiaListAdapter chidaoTongjiaListAdapter;
    public ZaotuiTongjiListAdapter zaotuiTongjiListAdapter;

    public Handler mHandler = new Handler(){};

    public String date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.mActivityFragmentView.viewMain(R.layout.activity_chidaozaotui);
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
        mLeftTextView.setText("考勤明细");
        mLeftTextView.setVisibility(View.VISIBLE);
    }

    @Override
    public void initData() {
        super.initData();
        //得到TabHost对象实例
        tabHost =(TabHost) findViewById(R.id.tabhost);
        //调用 TabHost.setup()
        tabHost.setup();
        //创建Tab标签
        tabHost.addTab(tabHost.newTabSpec("one").setIndicator("迟到").setContent(R.id.frame_chidao));
        tabHost.addTab(tabHost.newTabSpec("two").setIndicator("早退").setContent(R.id.frame_zaotui));

        chidaoList = new ArrayList<ChidaoTongjiaEntity>();
        zaotuiList = new ArrayList<ZaotuiTongjiaEntity>();

        chidaoTongjiaListAdapter = new ChidaoTongjiaListAdapter(this,chidaoList);
        zaotuiTongjiListAdapter = new ZaotuiTongjiListAdapter(this,zaotuiList);
        chidao_listView.setAdapter(chidaoTongjiaListAdapter);
        zaotui_listView.setAdapter(zaotuiTongjiListAdapter);
        chidaoTongjiaListAdapter.notifyDataSetChanged();
        zaotuiTongjiListAdapter.notifyDataSetChanged();

        Intent intent = getIntent();
        date = intent.getStringExtra("date");
    }

    @Override
    public void onResume() {
        super.onResume();
        queryChidaoList();
        queryZaotuiList();
    }

    /**
     * 查询迟到列表
     */
    public void queryChidaoList(){
        OkHttpClient okHttpClient = new OkHttpClient();
        Request.Builder builder = new Request.Builder();
        FormEncodingBuilder requestBodyBuilder = new FormEncodingBuilder();
        requestBodyBuilder.add("date", date);
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
                    chidaoList.clear();
                    final JSONArray jsonArray1 = jsonObject.getJSONArray("data");
                    if(jsonArray1.length()==0){
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                msg("没有人未打卡");
                            }
                        });
                    }else {
                        for (int i = 0; i < jsonArray1.length(); i++) {
                            ChidaoTongjiaEntity chidaoTongjiaEntity = new ChidaoTongjiaEntity();
                            JSONObject dataObject = jsonArray1.getJSONObject(i);
                            String userId = dataObject.optString("userId");
                            String name = dataObject.optString("name");
                            String dept = dataObject.optString("dept");
                            String clockInTime = dataObject.optString("clockInTime");
                            chidaoTongjiaEntity.setUserId(userId);
                            chidaoTongjiaEntity.setName(name);
                            chidaoTongjiaEntity.setDept(dept);
                            chidaoTongjiaEntity.setClockInTime(clockInTime);
                            chidaoList.add(chidaoTongjiaEntity);
                        }
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                chidaoTongjiaListAdapter.notifyDataSetChanged();
                                //    setListViewHeightBasedOnChildren(kuanggongListview);
                                chidao_listView.setAdapter(chidaoTongjiaListAdapter);
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
     * 查询早退列表
     */
    public void queryZaotuiList(){
        OkHttpClient okHttpClient = new OkHttpClient();
        Request.Builder builder = new Request.Builder();
        FormEncodingBuilder requestBodyBuilder = new FormEncodingBuilder();
        requestBodyBuilder.add("date", date);
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
                    zaotuiList.clear();
                    final JSONArray jsonArray1 = jsonObject.getJSONArray("data");
                    if(jsonArray1.length()==0){
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                msg("暂时没有人打卡");
                            }
                        });
                    }else {
                        for (int i = 0; i < jsonArray1.length(); i++) {
                            ZaotuiTongjiaEntity zaotuiTongjiaEntity = new ZaotuiTongjiaEntity();
                            JSONObject dataObject = jsonArray1.getJSONObject(i);
                            String userId = dataObject.optString("userId");
                            String name = dataObject.optString("name");
                            String dept = dataObject.optString("dept");
                            String clockOutTime = dataObject.optString("clockOutTime");
                            zaotuiTongjiaEntity.setUserId(userId);
                            zaotuiTongjiaEntity.setName(name);
                            zaotuiTongjiaEntity.setDept(dept);
                            zaotuiTongjiaEntity.setClockOutTime(clockOutTime);
                            zaotuiList.add(zaotuiTongjiaEntity);
                        }
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                zaotuiTongjiListAdapter.notifyDataSetChanged();
                                //    setListViewHeightBasedOnChildren(kuanggongListview);
                                zaotui_listView.setAdapter(zaotuiTongjiListAdapter);
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
}
