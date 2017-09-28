package com.henghao.hhworkpresent.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.henghao.hhworkpresent.ActivityFragmentSupport;
import com.henghao.hhworkpresent.ProtocolUrl;
import com.henghao.hhworkpresent.R;
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
import java.util.List;

/**
 * 发起会议人等待审批结果界面
 * Created by ASUS on 2017/9/28.
 */

public class MeetingWaitResultActivity extends ActivityFragmentSupport {

    @ViewInject(R.id.tv_wait_meeting_descript)
    private TextView tv_wait_meeting_descript;

    @ViewInject(R.id.tv_wait_meeting_faqiren)
    private TextView tv_wait_meeting_faqiren;

    @ViewInject(R.id.tv_wait_meeting_shenpiren)
    private TextView tv_wait_meeting_shenpiren;

    @ViewInject(R.id.tv_wait_message_time)
    private TextView tv_wait_message_time;

    private Handler mHandler = new Handler(){};
    private long msg_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.mActivityFragmentView.viewMain(R.layout.activity_meeting_wait_result);
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
        mCenterTextView.setText("等待审批结果");
        mCenterTextView.setVisibility(View.VISIBLE);
    }

    @Override
    public void initData() {
        super.initData();
        Intent intent = getIntent();
        msg_id = intent.getLongExtra("msg_id",0);
        httpRequestMeetingContent();
    }

    public void httpRequestMeetingContent(){
        OkHttpClient okHttpClient = new OkHttpClient();
        Request.Builder builder = new Request.Builder();
        MultipartBuilder multipartBuilder = new MultipartBuilder();
        multipartBuilder.type(MultipartBuilder.FORM)
                .addFormDataPart("msg_id", String.valueOf(msg_id))
                .addFormDataPart("uid", new SqliteDBUtils(this).getLoginUid());
        RequestBody requestBody = multipartBuilder.build();
        String request_url = ProtocolUrl.APP_QUERY_TUI_SONG_MESSAGE;
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
                    final MeetingEntity meetingEntity = gson.fromJson(result_str,MeetingEntity.class);
                    final List<JPushToUser> jPushToUserList = meetingEntity.getjPushToUser();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            for(JPushToUser jPushToUser : jPushToUserList){
                                if(jPushToUser.getMsg_id()==msg_id){
                                    tv_wait_meeting_descript.setText(
                                            "你好，" + "你发起的主题为"+meetingEntity.getMeetingTheme()+"的预约会议已经成功推送给"
                                                    +meetingEntity.getLeadName()+"审批，请留意通知消息等待审批结果。");
                                    tv_wait_meeting_faqiren.setText("会议发起人："+jPushToUser.getMessageSendPeople());
                                    tv_wait_meeting_shenpiren.setText("会议审批人："+meetingEntity.getLeadName());
                                    tv_wait_message_time.setText(jPushToUser.getMessageSendTime());
                                }
                            }

                        }
                    });

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        });
    }
}
