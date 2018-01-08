package com.henghao.hhworkpresent.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
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
 * 会议审批结果界面
 * Created by ASUS on 2017/9/28.
 */

public class MeetingShenpiResultsActivity extends ActivityFragmentSupport {

    @ViewInject(R.id.tv_meeting_theme)
    private TextView tv_meeting_theme;

    @ViewInject(R.id.tv_meeting_faqiren)
    private TextView tv_meeting_faqiren;

    @ViewInject(R.id.tv_meeting_place)
    private TextView tv_meeting_place;

    @ViewInject(R.id.tv_meeting_start_time)
    private TextView tv_meeting_start_time;

    @ViewInject(R.id.tv_meeting_duration)
    private TextView tv_meeting_duration;

    @ViewInject(R.id.tv_join_meeting_people_num)
    private TextView tv_join_meeting_people_num;

    @ViewInject(R.id.tv_join_meeting_people)
    private TextView tv_join_meeting_people;

    @ViewInject(R.id.tv_meeting_shenpi_result)
    private TextView tv_meeting_shenpi_result;

    @ViewInject(R.id.tv_meeting_dispass_reason)
    private TextView tv_meeting_dispass_reason;

    @ViewInject(R.id.linear_meeting_dispass_reason)
    private LinearLayout linear_meeting_dispass_reason;

    private Handler mHandler = new Handler(){};
    private long msg_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.mActivityFragmentView.viewMain(R.layout.activity_meeting_shenpi_pass);
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
        mCenterTextView.setText("会议审批结果");
        mCenterTextView.setVisibility(View.VISIBLE);
    }

    @Override
    public void initData() {
        super.initData();
        Intent intent = getIntent();
        msg_id = intent.getLongExtra("msg_id",0);
        httpRequestMeetingContent();
    }

    /**
     * 从后台获取会议数据
     */
    public void httpRequestMeetingContent(){
        OkHttpClient okHttpClient = new OkHttpClient();
        Request.Builder builder = new Request.Builder();
        MultipartBuilder multipartBuilder = new MultipartBuilder();
        multipartBuilder.type(MultipartBuilder.FORM)
                .addFormDataPart("msg_id", String.valueOf(msg_id))
                .addFormDataPart("uid", new SqliteDBUtils(this).getLoginUid());
        RequestBody requestBody = multipartBuilder.build();
        String request_url = ProtocolUrl.ROOT_URL + ProtocolUrl.APP_QUERY_TUI_SONG_MESSAGE;
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
                            tv_meeting_theme.setText(meetingEntity.getMeetingTheme());
                            for(JPushToUser jPushToUser : jPushToUserList){
                                if(jPushToUser.getMsg_id()==msg_id){
                                    tv_meeting_faqiren.setText(jPushToUser.getMessageSendPeople());
                                }
                            }
                            tv_meeting_place.setText(meetingEntity.getMeetingPlace());
                            tv_meeting_start_time.setText(meetingEntity.getMeetingStartTime());
                            tv_meeting_duration.setText(meetingEntity.getMeetingDuration());
                            String name = meetingEntity.getUserIds();   //获取参会人员
                            String[] strings = name.split(",");
                            tv_join_meeting_people_num.setText(String.valueOf(strings.length));
                            tv_join_meeting_people.setText(name);
                            if(meetingEntity.getWhetherPass()==2){  //表示未通过审批
                                tv_meeting_shenpi_result.setText("未通过");
                                linear_meeting_dispass_reason.setVisibility(View.VISIBLE);
                                tv_meeting_dispass_reason.setText(meetingEntity.getNoPassReason());
                            } else if(meetingEntity.getWhetherPass()==1){
                                tv_meeting_shenpi_result.setText("已通过");
                                linear_meeting_dispass_reason.setVisibility(View.GONE);
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
