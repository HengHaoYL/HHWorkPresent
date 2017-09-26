package com.henghao.hhworkpresent.fragment;

import android.os.Bundle;
import android.os.Handler;
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
import com.henghao.hhworkpresent.adapter.WoyaoCheckListAdapter;
import com.henghao.hhworkpresent.adapter.WoyaoFuchaListAdapter;
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
 * 我要复查模块
 * Created by ASUS on 2017/9/19.
 */

public class WoyaoFuchaFragment extends FragmentSupport {

    @ViewInject(R.id.woyaofucha_listview)
    private ListView woyaofucha_listview;

    private List<SaveCheckTaskEntity> saveCheckTaskEntityList;
    private WoyaoFuchaListAdapter woyaoFuchaListAdapter;

    public Handler mHandler = new Handler(){};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        mActivityFragmentView.getNavitionBarView().setVisibility(View.GONE);
        this.mActivityFragmentView.viewMain(R.layout.fragment_woyao_fucha);
        this.mActivityFragmentView.viewEmpty(R.layout.activity_empty);
        this.mActivityFragmentView.viewEmptyGone();
        this.mActivityFragmentView.viewLoading(View.GONE);
        ViewUtils.inject(this, this.mActivityFragmentView);
        initWidget();
        initData();
        return this.mActivityFragmentView;
    }

    public void initWidget(){
        woyaofucha_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
    }

    public void initData(){
        saveCheckTaskEntityList = new ArrayList<>();
    }

    @Override
    public void onResume() {
        super.onResume();
        httpRequesSaveFuchaPlanList();
    }

    /**
     * 根据系统用户id查询我要复查计划列表
     */
    public void httpRequesSaveFuchaPlanList(){
        SqliteDBUtils sqliteDBUtils = new SqliteDBUtils(mActivity);
        String uid = sqliteDBUtils.getLoginUid();
        OkHttpClient okHttpClient = new OkHttpClient();
        Request.Builder builder = new Request.Builder();
        String request_url = "http://172.16.0.81:8080/istration/enforceapp/queryplanbyuser?userid="+uid+"&resultStatus="+ Constant.WOYAO_FUCHA;
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
                            woyaoFuchaListAdapter = new WoyaoFuchaListAdapter(mActivity, saveCheckTaskEntityList);
                            woyaofucha_listview.setAdapter(woyaoFuchaListAdapter);
                            woyaoFuchaListAdapter.notifyDataSetChanged();
                        }
                    });

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
