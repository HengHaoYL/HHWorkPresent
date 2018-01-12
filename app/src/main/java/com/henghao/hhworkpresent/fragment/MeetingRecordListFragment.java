package com.henghao.hhworkpresent.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.benefit.buy.library.utils.ToastUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.henghao.hhworkpresent.FragmentSupport;
import com.henghao.hhworkpresent.ProtocolUrl;
import com.henghao.hhworkpresent.R;
import com.henghao.hhworkpresent.activity.MeetingUploadListInfoActivity;
import com.henghao.hhworkpresent.adapter.MeetingRecordListAdapter;
import com.henghao.hhworkpresent.entity.MeetingTrajectoryEntity;
import com.henghao.hhworkpresent.utils.SqliteDBUtils;
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
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * 上传后的会议记录
 * Created by bryanrady on 2017/10/16.
 */

public class MeetingRecordListFragment extends FragmentSupport {

    @ViewInject(R.id.meeting_record_listview)
    private ListView meeting_record_listview;

    private MeetingRecordListAdapter meetingRecordListAdapter;

    private List<MeetingTrajectoryEntity> meetingTrajectoryEntityList;

    private SqliteDBUtils sqliteDBUtils;

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
        meeting_record_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.setClass(mActivity, MeetingUploadListInfoActivity.class);
                MeetingTrajectoryEntity meetingTrajectoryEntity = (MeetingTrajectoryEntity)(meetingRecordListAdapter.getItem(position));
                intent.putExtra("meetingTrajectoryEntity",meetingTrajectoryEntity);
                startActivity(intent);
            }
        });

    }

    public void initData(){
        sqliteDBUtils = new SqliteDBUtils(mActivity);
        meetingTrajectoryEntityList = new ArrayList<>();
    }

    @Override
    public void onResume() {
        super.onResume();
        httpMeetingUploadRecordListInfo(sqliteDBUtils.getLoginUid());
    }

    /**
     * 初始化数据 也是刷新
     */
    private void httpMeetingUploadRecordListInfo(String userId){
        OkHttpClient okHttpClient = new OkHttpClient();
        Request.Builder builder = new Request.Builder();
        MultipartBuilder multipartBuilder = new MultipartBuilder();
        multipartBuilder.type(MultipartBuilder.FORM)
                .addFormDataPart("userId", userId);
        RequestBody requestBody = multipartBuilder.build();
        String request_url = ProtocolUrl.ROOT_URL + ProtocolUrl.APP_QUERT_MEETING_TRAJECTORY_LIST;
        Request request = builder.post(requestBody).url(request_url).build();
        Call call = okHttpClient.newCall(request);
        mActivityFragmentView.viewLoading(View.VISIBLE);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mActivityFragmentView.viewLoading(View.GONE);
                        ToastUtils.show(getContext(),R.string.app_network_failure);
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
                    Type type = new TypeToken<ArrayList<MeetingTrajectoryEntity>>() {}.getType();
                    meetingTrajectoryEntityList = gson.fromJson(result_str,type);
                    mActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mActivityFragmentView.viewLoading(View.GONE);
                            meetingRecordListAdapter = new MeetingRecordListAdapter(mActivity,meetingTrajectoryEntityList);
                            meeting_record_listview.setAdapter(meetingRecordListAdapter);

                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

}
