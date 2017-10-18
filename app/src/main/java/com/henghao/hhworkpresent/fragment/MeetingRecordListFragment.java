package com.henghao.hhworkpresent.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import com.benefit.buy.library.views.xlistview.XListView;
import com.google.gson.Gson;
import com.henghao.hhworkpresent.FragmentSupport;
import com.henghao.hhworkpresent.ProtocolUrl;
import com.henghao.hhworkpresent.R;
import com.henghao.hhworkpresent.activity.MeetingUploadListInfoActivity;
import com.henghao.hhworkpresent.adapter.MeetingRecordListAdapter;
import com.henghao.hhworkpresent.entity.MeetingDataBean;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 上传后的会议记录
 * Created by bryanrady on 2017/10/16.
 */

public class MeetingRecordListFragment extends FragmentSupport {

    @ViewInject(R.id.meeting_record_xlistview)
    private XListView meeting_record_xlistview;

    private MeetingRecordListAdapter meetingRecordListAdapter;

    private int pageIndex = 1;

    private List<MeetingDataBean.MeetingUploadEntity> meetingUploadEntityList;

    private Handler mHandler = new Handler(){};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        mActivityFragmentView.getNavitionBarView().setVisibility(View.GONE);
        this.mActivityFragmentView.viewMain(R.layout.fragment_meeting_record_list);
        this.mActivityFragmentView.viewEmpty(R.layout.activity_empty);
        this.mActivityFragmentView.viewEmptyGone();
        this.mActivityFragmentView.viewLoading(View.GONE);
        ViewUtils.inject(this, this.mActivityFragmentView);
        initWidget();
        initData();
        return this.mActivityFragmentView;
    }

    public void initWidget(){
        //把下拉刷新、上拉加载开关打开
        meeting_record_xlistview.setPullLoadEnable(true);//设置下拉刷新
        meeting_record_xlistview.setPullLoadEnable(true);
        meeting_record_xlistview.setXListViewListener(new XListView.IXListViewListener() {
            @Override
            public void onRefresh() {
                httpMeetingUploadRecordListInfo();
            }

            @Override
            public void onLoadMore() {
                httpMeetingUploadRecordListInfoFlush();
            }
        });

        meeting_record_xlistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.setClass(mActivity, MeetingUploadListInfoActivity.class);
                MeetingDataBean.MeetingUploadEntity meetingUploadEntity = (MeetingDataBean.MeetingUploadEntity)(meetingRecordListAdapter.getItem(position-1));
                intent.putExtra("meetingUploadEntity",meetingUploadEntity);
                startActivity(intent);
            }
        });

    }

    public void initData(){
        meetingUploadEntityList = new ArrayList<>();
        httpMeetingUploadRecordListInfo();
    }

    /**
     * 初始化数据 也是刷新
     */
    private void httpMeetingUploadRecordListInfo(){
        OkHttpClient okHttpClient = new OkHttpClient();
        Request.Builder builder = new Request.Builder();
        MultipartBuilder multipartBuilder = new MultipartBuilder();
        multipartBuilder.type(MultipartBuilder.FORM)
                .addFormDataPart("page", String.valueOf(1))
                .addFormDataPart("size", String.valueOf(10));
        RequestBody requestBody = multipartBuilder.build();
        String request_url = ProtocolUrl.APP_QUERT_MEETING_UPLOAD_LIST;
        Request request = builder.post(requestBody).url(request_url).build();
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
                    MeetingDataBean meetingDataBean = gson.fromJson(result_str,MeetingDataBean.class);
                    meetingUploadEntityList = meetingDataBean.getList();
                    mActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mActivityFragmentView.viewLoading(View.GONE);
                            meetingRecordListAdapter = new MeetingRecordListAdapter(mActivity,meetingUploadEntityList);
                            meeting_record_xlistview.setAdapter(meetingRecordListAdapter);
                            onLoad();
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 加载更多
     */
    private void httpMeetingUploadRecordListInfoFlush(){
        pageIndex = pageIndex + 1;  //每次都添加一页
        OkHttpClient okHttpClient = new OkHttpClient();
        Request.Builder builder = new Request.Builder();
        MultipartBuilder multipartBuilder = new MultipartBuilder();
        multipartBuilder.type(MultipartBuilder.FORM)
                .addFormDataPart("page", String.valueOf(pageIndex))
                .addFormDataPart("size", String.valueOf(10));
        RequestBody requestBody = multipartBuilder.build();
        String request_url = ProtocolUrl.APP_QUERT_MEETING_UPLOAD_LIST;
        Request request = builder.post(requestBody).url(request_url).build();
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
                    MeetingDataBean meetingDataBean = gson.fromJson(result_str,MeetingDataBean.class);
                    List<MeetingDataBean.MeetingUploadEntity> dataload =meetingDataBean.getList();
                    meetingUploadEntityList.addAll(dataload);
                    mActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mActivityFragmentView.viewLoading(View.GONE);
                            meetingRecordListAdapter.notifyDataSetChanged();
                            onLoad();
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 设置时间
     */
    @SuppressLint("SimpleDateFormat")
    private void onLoad() {
        meeting_record_xlistview.stopRefresh();
        meeting_record_xlistview.stopLoadMore();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String nowTime = df.format(new Date(System.currentTimeMillis()));
        // 释放时提示正在刷新时的当前时间
        meeting_record_xlistview.setRefreshTime(nowTime);
    }
}
