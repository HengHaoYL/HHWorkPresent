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
import com.henghao.hhworkpresent.adapter.ChuchaiTongjiListAdapter;
import com.henghao.hhworkpresent.adapter.QingjiaTongjiaListAdapter;
import com.henghao.hhworkpresent.entity.ChuchaiTongjiEntity;
import com.henghao.hhworkpresent.entity.QingjiaTongjiEntity;
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

public class QingjiaChuchaiActivity extends ActivityFragmentSupport {

    private TabHost tabHost;

    @ViewInject(R.id.qingjia_listview)
    private XListView qingjia_listView;

    @ViewInject(R.id.chuchai_listview)
    private XListView chuchai_listView;

    public List<QingjiaTongjiEntity> qingjiaList;
    public List<ChuchaiTongjiEntity> chuchaiList;

    public QingjiaTongjiaListAdapter qingjiaoTongjiaListAdapter;
    public ChuchaiTongjiListAdapter chuchaiTongjiListAdapter;

    public Handler mHandler = new Handler(){};

    public String date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.mActivityFragmentView.viewMain(R.layout.activity_qingjiachuchai);
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
        mLeftTextView.setText("请假出差明细");
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
        tabHost.addTab(tabHost.newTabSpec("one").setIndicator("请假").setContent(R.id.frame_qingjia));
        tabHost.addTab(tabHost.newTabSpec("two").setIndicator("出差").setContent(R.id.frame_chuchai));

        qingjiaList = new ArrayList<QingjiaTongjiEntity>();
        chuchaiList = new ArrayList<ChuchaiTongjiEntity>();

        qingjiaoTongjiaListAdapter = new QingjiaTongjiaListAdapter(this,qingjiaList);
        chuchaiTongjiListAdapter = new ChuchaiTongjiListAdapter(this,chuchaiList);
        qingjia_listView.setAdapter(qingjiaoTongjiaListAdapter);
        chuchai_listView.setAdapter(chuchaiTongjiListAdapter);
        qingjiaoTongjiaListAdapter.notifyDataSetChanged();
        chuchaiTongjiListAdapter.notifyDataSetChanged();

        Intent intent = getIntent();
        date = intent.getStringExtra("date");
    }

    @Override
    public void onResume() {
        super.onResume();
        queryQingjiaList();
        queryChuchaiList();
    }

    /**
     * 查询请假列表
     */
    public void queryQingjiaList(){
        OkHttpClient okHttpClient = new OkHttpClient();
        Request.Builder builder = new Request.Builder();
        FormEncodingBuilder requestBodyBuilder = new FormEncodingBuilder();
        requestBodyBuilder.add("date", date);
        RequestBody requestBody = requestBodyBuilder.build();
        String request_url = ProtocolUrl.ROOT_URL + ProtocolUrl.APP_QUERY_QINGJIA;
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
                    qingjiaList.clear();
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
                            QingjiaTongjiEntity qingjiaTongjiEntity = new QingjiaTongjiEntity();
                            JSONObject dataObject = jsonArray1.getJSONObject(i);
                            String userId = dataObject.optString("userId");
                            String name = dataObject.optString("name");
                            String dept = dataObject.optString("dept");
                            String leave = dataObject.optString("leave");
                            qingjiaTongjiEntity.setUserId(userId);
                            qingjiaTongjiEntity.setName(name);
                            qingjiaTongjiEntity.setDept(dept);
                            qingjiaTongjiEntity.setLeave(leave);
                            qingjiaList.add(qingjiaTongjiEntity);
                        }
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                qingjiaoTongjiaListAdapter.notifyDataSetChanged();
                                //    setListViewHeightBasedOnChildren(kuanggongListview);
                                qingjia_listView.setAdapter(qingjiaoTongjiaListAdapter);
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
     * 查询出差列表
     */
    public void queryChuchaiList(){
        OkHttpClient okHttpClient = new OkHttpClient();
        Request.Builder builder = new Request.Builder();
        FormEncodingBuilder requestBodyBuilder = new FormEncodingBuilder();
        requestBodyBuilder.add("date", date);
        RequestBody requestBody = requestBodyBuilder.build();
        String request_url = ProtocolUrl.ROOT_URL + ProtocolUrl.APP_QUERY_CHUCHAI;
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
                    chuchaiList.clear();
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
                            ChuchaiTongjiEntity chuchaiTongjiEntity = new ChuchaiTongjiEntity();
                            JSONObject dataObject = jsonArray1.getJSONObject(i);
                            String userId = dataObject.optString("userId");
                            String name = dataObject.optString("name");
                            String dept = dataObject.optString("dept");
                            String evection = dataObject.optString("evection");
                            chuchaiTongjiEntity.setUserId(userId);
                            chuchaiTongjiEntity.setName(name);
                            chuchaiTongjiEntity.setDept(dept);
                            chuchaiTongjiEntity.setEvection(evection);
                            chuchaiList.add(chuchaiTongjiEntity);
                        }
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                chuchaiTongjiListAdapter.notifyDataSetChanged();
                                //    setListViewHeightBasedOnChildren(kuanggongListview);
                                chuchai_listView.setAdapter(chuchaiTongjiListAdapter);
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
