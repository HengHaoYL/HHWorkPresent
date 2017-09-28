package com.henghao.hhworkpresent.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.henghao.hhworkpresent.ActivityFragmentSupport;
import com.henghao.hhworkpresent.ProtocolUrl;
import com.henghao.hhworkpresent.R;
import com.henghao.hhworkpresent.adapter.MeetingMessageListAdapter;
import com.henghao.hhworkpresent.entity.JPushToUser;
import com.henghao.hhworkpresent.entity.MeetingEntity;
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
import java.util.ArrayList;
import java.util.List;


/**
 * 会议管理
 * Created by ASUS on 2017/9/27.
 */

public class MeetingListActivity extends ActivityFragmentSupport {

    @ViewInject(R.id.meeting_listview)
    private ListView meeting_listview;

    private MeetingMessageListAdapter meetingMessageListAdapter;

    private List<MeetingEntity> meetingEntityList;      //会议集合

    public static List<JPushToUser> jPushToUserList;      //用户的推送消息集合 别人推送过来的

    private Handler mHandler = new Handler(){};

    private SqliteDBUtils sqliteDBUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.mActivityFragmentView.viewMain(R.layout.activity_meeting_list);
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
        mLeftTextView.setVisibility(View.VISIBLE);
        initWithCenterBar();
        mCenterTextView.setText("会议推送消息");
        mCenterTextView.setVisibility(View.VISIBLE);

        initWithRightBar();
        mRightTextView.setText("会议上传");
        mRightTextView.setVisibility(View.VISIBLE);
        mRightTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MeetingListActivity.this,MeetingUploadActivity.class);
                startActivity(intent);
                finish();
            }
        });

        meeting_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                int type = ((JPushToUser) meetingMessageListAdapter.getItem(position)).getType();
                long msg_id = ((JPushToUser) meetingMessageListAdapter.getItem(position)).getMsg_id();
                //0 未查看 ，1已查看
                int isRead = ((JPushToUser) meetingMessageListAdapter.getItem(position)).getIsRead();
                int jpush_mid = ((JPushToUser) meetingMessageListAdapter.getItem(position)).getMid();
                switch (type){
                    case 1: //发给领导审批 需判断是否查看过 如果未查看过就到审批界面
                        if(isRead==0){  // 未查看
                            intent.setClass(MeetingListActivity.this,MeetingReviewActivity.class);
                            intent.putExtra("msg_id",msg_id);
                            startActivity(intent);
                        }else if(isRead==1){    //领导查看自己审批的会议 结果界面
                            for(MeetingEntity meetingEntity :meetingEntityList){
                                if(meetingEntity.getMid() == jpush_mid){   //说明消息的mid 和 会议的mid 是一样的 是绑定的
                                    if(meetingEntity.getWhetherPass()==0){              //表示还没开始审批，进入审批界面
                                        intent.setClass(MeetingListActivity.this,MeetingReviewActivity.class);
                                        intent.putExtra("msg_id",msg_id);
                                        startActivity(intent);
                                    }else if(meetingEntity.getWhetherPass()==1 || meetingEntity.getWhetherPass()==2){        //表示已经审批过了而且结果是不通过审批
                                        intent.setClass(MeetingListActivity.this,MeetingShenpiResultsActivity.class);
                                        intent.putExtra("msg_id",msg_id);
                                        startActivity(intent);
                                    }
                                }
                            }
                        }
                        break;
                    case 2:   //领导推送给发起会议人审批不通过
                        intent.setClass(MeetingListActivity.this,MeetingShenpiNoPassActivity.class);
                        intent.putExtra("msg_id",msg_id);
                        startActivity(intent);
                        break;
                    case 3:   //3代表通知大家开会
                        intent.setClass(MeetingListActivity.this,MeetingNotificationActivity.class);
                        intent.putExtra("msg_id",msg_id);
                        startActivity(intent);
                        break;
                    case 4:   //4代表等待审批结果
                        intent.setClass(MeetingListActivity.this,MeetingWaitResultActivity.class);
                        intent.putExtra("msg_id",msg_id);
                        startActivity(intent);
                        break;
                }

                httpRequestIsRead(((JPushToUser) meetingMessageListAdapter.getItem(position)).getCid());
            }
        });

    }

    @Override
    public void initData() {
        super.initData();
        sqliteDBUtils = new SqliteDBUtils(this);
        meetingEntityList = new ArrayList<>();
        jPushToUserList = new ArrayList<>();
    }

    @Override
    public void onResume() {
        super.onResume();
        httpRequestMeetingMessageList();
    }

    /**
     * 将未读消息变为已读
     * @param cid
     */
    public void httpRequestIsRead(int cid){
        OkHttpClient okHttpClient = new OkHttpClient();
        Request.Builder builder = new Request.Builder();
        MultipartBuilder multipartBuilder = new MultipartBuilder();
        multipartBuilder.type(MultipartBuilder.FORM)
                .addFormDataPart("cid", String.valueOf(cid));
        RequestBody requestBody = multipartBuilder.build();
        String request_url = ProtocolUrl.APP_SET_UNREAD_TO_READ;
        Request request = builder.post(requestBody).url(request_url).build();
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
            }

        });
    }

    /**
     * 根据uid查询别人推送给自己的消息
     */
    public void httpRequestMeetingMessageList(){
        OkHttpClient okHttpClient = new OkHttpClient();
        Request.Builder builder = new Request.Builder();
        MultipartBuilder multipartBuilder = new MultipartBuilder();
        multipartBuilder.type(MultipartBuilder.FORM)
                .addFormDataPart("uid", sqliteDBUtils.getLoginUid());
        RequestBody requestBody = multipartBuilder.build();
        String request_url = ProtocolUrl.APP_QUERY_TUI_SONG_MESSAGE_LIST;
        Request request = builder.post(requestBody).url(request_url).build();
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
                    meetingEntityList = gson.fromJson(result_str,new TypeToken<ArrayList<MeetingEntity>>() {}.getType());
                    jPushToUserList.clear();
                    for(int i=0;i<meetingEntityList.size();i++){
                        jPushToUserList.addAll(meetingEntityList.get(i).getjPushToUser());
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            meetingMessageListAdapter = new MeetingMessageListAdapter(MeetingListActivity.this,jPushToUserList);
                            meeting_listview.setAdapter(meetingMessageListAdapter);
                            meetingMessageListAdapter.notifyDataSetChanged();
                        }
                    });

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        });
    }


}
