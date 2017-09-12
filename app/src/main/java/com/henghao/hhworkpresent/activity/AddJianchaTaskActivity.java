package com.henghao.hhworkpresent.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.benefit.buy.library.views.xlistview.XListView;
import com.google.gson.Gson;
import com.henghao.hhworkpresent.ActivityFragmentSupport;
import com.henghao.hhworkpresent.R;
import com.henghao.hhworkpresent.adapter.CompanyInfoXListAdapter;
import com.henghao.hhworkpresent.entity.CompanyInfoEntity;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 添加检查任务界面
 * Created by ASUS on 2017/9/5.
 */

public class AddJianchaTaskActivity extends ActivityFragmentSupport {

    @ViewInject(R.id.edittext_search)
    private EditText edittext_search;

    @ViewInject(R.id.btn_search)
    private Button btn_search;

    @ViewInject(R.id.company_listview)
    private XListView company_listview;

    private CompanyInfoXListAdapter mCompanyInfoXListAdapter;
    private List<CompanyInfoEntity.DataBean> mCompanyInfoEntityList;
    private int pageIndex = 1;

    public Handler mHandler = new Handler(){};

    //用来判断上一次的请求接口是哪个接口 ， 用来判断上拉加载该执行哪个接口
    public static int lastRequestInterface = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.mActivityFragmentView.viewMain(R.layout.activity_add_jiancharenwu);
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
        mLeftImageView.setVisibility(View.VISIBLE);
        initWithCenterBar();
        mCenterTextView.setText("添加检查任务");
        mCenterTextView.setVisibility(View.VISIBLE);

        //把上拉加载开关打开
        company_listview.setPullLoadEnable(true);
        company_listview.setXListViewListener(new XListView.IXListViewListener() {
            @Override
            public void onRefresh() {

            }

            @Override
            public void onLoadMore() {
                if(lastRequestInterface==0){
                    httpGetCompanyInfoFlush();
                }else if(lastRequestInterface==1) {
                    httpGetCompanyInfoChooseFlush();
                }
            }
        });

        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                httpGetCompanyInfoChoose();
            }
        });

        company_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.setClass(AddJianchaTaskActivity.this,JianchaTaskActivity.class);
                //传递对象
                intent.putExtra("dataBean",(CompanyInfoEntity.DataBean) mCompanyInfoXListAdapter.getItem(position - 1));
                startActivity(intent);
            }
        });
    }

    @Override
    public void initData() {
        super.initData();
        mCompanyInfoEntityList = new ArrayList<CompanyInfoEntity.DataBean>();
        httpGetCompanyInfo();
    }

    /**
     * 设置时间
     */
    @SuppressLint("SimpleDateFormat")
    private void onLoad() {
        company_listview.stopRefresh();
        company_listview.stopLoadMore();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String nowTime = df.format(new Date(System.currentTimeMillis()));
        // 释放时提示正在刷新时的当前时间
        company_listview.setRefreshTime(nowTime);
    }

    /**
     * 初始化数据
     * Get
     */
    private void httpGetCompanyInfo(){
        OkHttpClient okHttpClient = new OkHttpClient();
        Request.Builder builder = new Request.Builder();
        String request_url = "http://172.16.0.81:8080/tongji/firmdate/query?page="+pageIndex+"&size="+10;
        Request request = builder.url(request_url).build();
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
                    result_str = jsonObject.getString("data");
                    Gson gson = new Gson();
                    CompanyInfoEntity result = gson.fromJson(result_str,CompanyInfoEntity.class);
                    mCompanyInfoEntityList = result.getData();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mActivityFragmentView.viewLoading(View.GONE);
                            mCompanyInfoXListAdapter = new CompanyInfoXListAdapter(AddJianchaTaskActivity.this,mCompanyInfoEntityList);
                            company_listview.setAdapter(mCompanyInfoXListAdapter);
                            lastRequestInterface = 0;
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Get
     * 加载数据
     */
    private void httpGetCompanyInfoFlush(){
        pageIndex = pageIndex + 1;  //每次都添加一页
        OkHttpClient okHttpClient = new OkHttpClient();
        Request.Builder builder = new Request.Builder();
        String request_url = "http://172.16.0.81:8080/tongji/firmdate/query?page="+pageIndex+"&size="+10;
        Request request = builder.url(request_url).build();
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
                    result_str = jsonObject.getString("data");
                    Gson gson = new Gson();
                    CompanyInfoEntity result = gson.fromJson(result_str,CompanyInfoEntity.class);
                    List<CompanyInfoEntity.DataBean> dataload = result.getData();
                    mCompanyInfoEntityList.addAll(dataload);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mActivityFragmentView.viewLoading(View.GONE);
                            mCompanyInfoXListAdapter.notifyDataSetChanged();
                            onLoad();
                        }
                    });
                    lastRequestInterface=0;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Get
     * 根据输入框选择的数据
     */
    private void httpGetCompanyInfoChoose(){
        mCompanyInfoXListAdapter = null;
        OkHttpClient okHttpClient = new OkHttpClient();
        Request.Builder builder = new Request.Builder();
        String seek = edittext_search.getText().toString().trim();
        if(!(seek!=null||seek.equals("")||seek.equals("null"))){
            Toast.makeText(this,"不能输入空字符",Toast.LENGTH_SHORT).show();
            return;
        }
        String request_url = "http://172.16.0.81:8080/tongji/firmdate/queryseek?firmname="+seek+"&page="+pageIndex+"&size="+10;
        Request request = builder.url(request_url).build();
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
                    result_str = jsonObject.getString("data");
                    Gson gson = new Gson();
                    CompanyInfoEntity result = gson.fromJson(result_str,CompanyInfoEntity.class);
                    mCompanyInfoEntityList = result.getData();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mActivityFragmentView.viewLoading(View.GONE);
                            mCompanyInfoXListAdapter = new CompanyInfoXListAdapter(AddJianchaTaskActivity.this,mCompanyInfoEntityList);
                            company_listview.setAdapter(mCompanyInfoXListAdapter);
                            lastRequestInterface = 1;
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Get
     * 根据输入框选择的数据  加载更多
     */
    private void httpGetCompanyInfoChooseFlush(){
        pageIndex = pageIndex + 1;  //每次都添加一页
        OkHttpClient okHttpClient = new OkHttpClient();
        Request.Builder builder = new Request.Builder();
        String seek = edittext_search.getText().toString().trim();
        if(!(seek!=null||seek.equals("")||seek.equals("null"))){
            Toast.makeText(this,"不能输入空字符",Toast.LENGTH_SHORT).show();
            return;
        }
        String request_url = "http://172.16.0.81:8080/tongji/firmdate/queryseek?firmname="+seek+"&page="+pageIndex+"&size="+10;
        Request request = builder.url(request_url).build();
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
                    result_str = jsonObject.getString("data");
                    Gson gson = new Gson();
                    CompanyInfoEntity result = gson.fromJson(result_str,CompanyInfoEntity.class);
                    List<CompanyInfoEntity.DataBean> dataload = result.getData();
                    mCompanyInfoEntityList.addAll(dataload);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mActivityFragmentView.viewLoading(View.GONE);
                            mCompanyInfoXListAdapter.notifyDataSetChanged();
                            onLoad();
                        }
                    });
                    lastRequestInterface=1;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        });
    }

}
