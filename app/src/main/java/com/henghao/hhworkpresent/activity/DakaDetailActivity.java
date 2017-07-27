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
import com.henghao.hhworkpresent.adapter.WeiDakaListAdapter;
import com.henghao.hhworkpresent.adapter.YiDakaListAdapter;
import com.henghao.hhworkpresent.entity.WeiQiandaoEntity;
import com.henghao.hhworkpresent.entity.YiQiandanEntity;
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

public class DakaDetailActivity extends ActivityFragmentSupport {

    private TabHost tabHost;

    @ViewInject(R.id.weidaka_listview)
    private XListView weidaka_listView;

    @ViewInject(R.id.yidaka_listview)
    private XListView yidaka_listView;

    public List<WeiQiandaoEntity> weidakaList;
    public List<YiQiandanEntity> yidakaList;

    public WeiDakaListAdapter weiDakaListAdapter;
    public YiDakaListAdapter yiDakaListAdapter;

    public Handler mHandler = new Handler(){};

    public String date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.mActivityFragmentView.viewMain(R.layout.activity_dakadetail);
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
        mLeftTextView.setText("打卡明细");
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
        tabHost.addTab(tabHost.newTabSpec("one").setIndicator("未打卡").setContent(R.id.frame_weidaka));
        tabHost.addTab(tabHost.newTabSpec("two").setIndicator("已打卡").setContent(R.id.frame_yidaka));

        weidakaList = new ArrayList<WeiQiandaoEntity>();
        yidakaList = new ArrayList<YiQiandanEntity>();

        weiDakaListAdapter = new WeiDakaListAdapter(this,weidakaList);
        yiDakaListAdapter = new YiDakaListAdapter(this,yidakaList);
        weidaka_listView.setAdapter(weiDakaListAdapter);
        yidaka_listView.setAdapter(yiDakaListAdapter);
        weiDakaListAdapter.notifyDataSetChanged();
        yiDakaListAdapter.notifyDataSetChanged();

        Intent intent = getIntent();
        date = intent.getStringExtra("date");
    }

    @Override
    public void onResume() {
        super.onResume();
        queryWeidakaList();
        queryYidakaList();
    }

    /**
     * 查询未打卡列表
     */
    public void queryWeidakaList(){
        OkHttpClient okHttpClient = new OkHttpClient();
        Request.Builder builder = new Request.Builder();
        FormEncodingBuilder requestBodyBuilder = new FormEncodingBuilder();
        requestBodyBuilder.add("date", date);
        RequestBody requestBody = requestBodyBuilder.build();
        String request_url = ProtocolUrl.ROOT_URL + ProtocolUrl.APP_QUERY_WEIQIANDAO;
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
                    weidakaList.clear();
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
                            weidakaList.add(weiQiandaoEntity);
                        }
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                weiDakaListAdapter.notifyDataSetChanged();
                            //    setListViewHeightBasedOnChildren(kuanggongListview);
                                weidaka_listView.setAdapter(weiDakaListAdapter);
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
     * 查询已打卡列表
     */
    public void queryYidakaList(){
        OkHttpClient okHttpClient = new OkHttpClient();
        Request.Builder builder = new Request.Builder();
        FormEncodingBuilder requestBodyBuilder = new FormEncodingBuilder();
        requestBodyBuilder.add("date", date);
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
                    yidakaList.clear();
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
                            YiQiandanEntity yiQiandanEntity = new YiQiandanEntity();
                            JSONObject dataObject = jsonArray1.getJSONObject(i);
                            String userId = dataObject.optString("userId");
                            String name = dataObject.optString("name");
                            String dept = dataObject.optString("dept");
                            String signedIn = dataObject.optString("signedIn");
                            yiQiandanEntity.setUserId(userId);
                            yiQiandanEntity.setName(name);
                            yiQiandanEntity.setDept(dept);
                            yiQiandanEntity.setSignedIn(signedIn);
                            yidakaList.add(yiQiandanEntity);
                        }
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                yiDakaListAdapter.notifyDataSetChanged();
                                //    setListViewHeightBasedOnChildren(kuanggongListview);
                                yidaka_listView.setAdapter(yiDakaListAdapter);
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
