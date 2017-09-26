package com.henghao.hhworkpresent.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.henghao.hhworkpresent.Constant;
import com.henghao.hhworkpresent.FragmentSupport;
import com.henghao.hhworkpresent.R;
import com.henghao.hhworkpresent.activity.WoyaoJianchaActivity;
import com.henghao.hhworkpresent.activity.WoyaoJianchaNotEditActivity;
import com.henghao.hhworkpresent.adapter.WoyaoCheckListAdapter;
import com.henghao.hhworkpresent.entity.SaveCheckTaskEntity;
import com.henghao.hhworkpresent.utils.SqliteDBUtils;
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
import java.util.ArrayList;
import java.util.List;

/**
 * 我要检查模块
 * Created by ASUS on 2017/9/19.
 */

public class WoyaoCheckFragment extends FragmentSupport{

    @ViewInject(R.id.woyaojiancha_listview)
    private ListView woyaojiancha_listview;

    private List<SaveCheckTaskEntity> saveCheckTaskEntityList;
    private WoyaoCheckListAdapter woyaoCheckListAdapter;

    public Handler mHandler = new Handler(){};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        mActivityFragmentView.getNavitionBarView().setVisibility(View.GONE);        //要把fragment的这个导航栏去掉，要不然一直显示出来
        this.mActivityFragmentView.viewMain(R.layout.fragment_woyao_check);
        this.mActivityFragmentView.viewEmpty(R.layout.activity_empty);
        this.mActivityFragmentView.viewEmptyGone();
        this.mActivityFragmentView.viewLoading(View.GONE);
        ViewUtils.inject(this, this.mActivityFragmentView);
        initWidget();
        initData();
        return this.mActivityFragmentView;
    }

    public void initWidget(){
        woyaojiancha_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                //判断上次保存的时候检查现场是否有填写，分别进入不同的页面
                if(((SaveCheckTaskEntity) woyaoCheckListAdapter.getItem(position)).getCheckSite()==null){
                    intent.setClass(mActivity, WoyaoJianchaActivity.class);
                    intent.putExtra("Pid",((SaveCheckTaskEntity) woyaoCheckListAdapter.getItem(position)).getPid());
                    startActivity(intent);
                }else{
                    //如果不为null就进入我要检查不可编辑页面
                    intent.setClass(mActivity, WoyaoJianchaNotEditActivity.class);
                    intent.putExtra("Pid",((SaveCheckTaskEntity) woyaoCheckListAdapter.getItem(position)).getPid());
                    startActivity(intent);
                }
            }
        });
    }

    public void initData(){
        saveCheckTaskEntityList = new ArrayList<>();
    }

    @Override
    public void onResume() {
        super.onResume();
        httpRequesSaveCheckPlanList();
    }

    /**
     * 根据系统用户id查询我要检查计划列表
     */
    public void httpRequesSaveCheckPlanList(){
        SqliteDBUtils sqliteDBUtils = new SqliteDBUtils(mActivity);
        String uid = sqliteDBUtils.getLoginUid();
        OkHttpClient okHttpClient = new OkHttpClient();
        Request.Builder builder = new Request.Builder();
        String request_url = "http://172.16.0.81:8080/istration/enforceapp/queryplanbyuser?userid="+uid+"&resultStatus="+ Constant.WOYAO_CHECK;
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
                    saveCheckTaskEntityList = gson.fromJson(result_str, new TypeToken<ArrayList<SaveCheckTaskEntity>>() {
                    }.getType());
                    mActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            woyaoCheckListAdapter = new WoyaoCheckListAdapter(mActivity, saveCheckTaskEntityList);
                            woyaojiancha_listview.setAdapter(woyaoCheckListAdapter);
                            woyaoCheckListAdapter.notifyDataSetChanged();
                        }
                    });

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

}
