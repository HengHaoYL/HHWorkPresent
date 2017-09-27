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
import com.henghao.hhworkpresent.R;
import com.henghao.hhworkpresent.adapter.JianchaYinhuanListAdpter;
import com.henghao.hhworkpresent.adapter.MeetingMessageListAdapter;
import com.henghao.hhworkpresent.entity.JPushToUser;
import com.henghao.hhworkpresent.entity.MeetingEntity;
import com.henghao.hhworkpresent.entity.SaveCheckTaskEntity;
import com.henghao.hhworkpresent.service.MyJPushReceiver;
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
 * 会议管理
 * Created by ASUS on 2017/9/27.
 */

public class MeetingListActivity extends ActivityFragmentSupport {

    @ViewInject(R.id.meeting_listview)
    private ListView meeting_listview;

    private MeetingMessageListAdapter meetingMessageListAdapter;

    private List<MeetingEntity> meetingEntityList;      //会议集合

    private List<JPushToUser> jPushToUserList;      //用户的推送消息集合 别人推送过来的

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
        initWithCenterBar();
        mCenterTextView.setText("会议推送消息列表");
        mCenterTextView.setVisibility(View.VISIBLE);

        meeting_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                int type = ((JPushToUser) meetingMessageListAdapter.getItem(position)).getType();
                long msg_id = ((JPushToUser) meetingMessageListAdapter.getItem(position)).getMsg_id();
                //0 未查看 ，1已查看
                int isRead = ((JPushToUser) meetingMessageListAdapter.getItem(position)).getIsRead();
                switch (type){
                    case 1: //发给领导 需判断是否查看过 如果未查看过就到审批界面 查看过就进入显示界面
                        if(isRead==0){
                            intent.setClass(MeetingListActivity.this,MeetingReviewActivity.class);
                            intent.putExtra("msg_id",msg_id);
                            startActivity(intent);
                        }else if(isRead==1){
                            //先暂时提示消息不能点击 进入查看页面
                            Toast.makeText(MeetingListActivity.this,"此消息已经审批过了，不用继续审批",Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case 2:   //自己发给自己
                        break;
                    case 3:   //3代表通知大家开会
                        intent.setClass(MeetingListActivity.this,MeetingNotificationActivity.class);
                        intent.putExtra("msg_id",msg_id);
                        startActivity(intent);
                        break;
                }
            }
        });

    }

    @Override
    public void initData() {
        super.initData();
        sqliteDBUtils = new SqliteDBUtils(this);
        meetingEntityList = new ArrayList<>();
        jPushToUserList = new ArrayList<>();
        httpRequestMeetingMessageList();
    }

    /**
     * 根据uid查询别人推送给自己的消息
     */
    public void httpRequestMeetingMessageList(){
        //根据pid查询有隐患的被检查隐患文件列表
        OkHttpClient okHttpClient = new OkHttpClient();
        Request.Builder builder = new Request.Builder();
        final String request_url = "http://172.16.0.81:8080/istration/JPush/queryMeetingEntityByUserIdAll?uid="+sqliteDBUtils.getLoginUid();
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
                    meetingEntityList = gson.fromJson(result_str,new TypeToken<ArrayList<MeetingEntity>>() {}.getType());
                    List<JPushToUser> mJPushToUserList = new ArrayList<JPushToUser>();    //里面包含有其他人的推送消息 只需要取自己的即可
                    for(int i=0;i<meetingEntityList.size();i++){
                        mJPushToUserList.addAll(meetingEntityList.get(i).getjPushToUser());
                    }
                    Log.d("wangqingbin","mJPushToUserList=="+mJPushToUserList);

                    for(JPushToUser jPushToUser : mJPushToUserList){
                        if(jPushToUser.getUid().equals(sqliteDBUtils.getLoginUid())){
                            jPushToUserList.add(jPushToUser);
                        }
                    }
                    Log.d("wangqingbin","jPushToUserList=="+jPushToUserList);
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
