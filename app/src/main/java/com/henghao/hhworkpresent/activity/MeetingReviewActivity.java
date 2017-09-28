package com.henghao.hhworkpresent.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.henghao.hhworkpresent.ActivityFragmentSupport;
import com.henghao.hhworkpresent.ProtocolUrl;
import com.henghao.hhworkpresent.R;
import com.henghao.hhworkpresent.adapter.JianchaYinhuanListAdpter;
import com.henghao.hhworkpresent.entity.JPushToUser;
import com.henghao.hhworkpresent.entity.MeetingEntity;
import com.henghao.hhworkpresent.entity.SaveCheckTaskEntity;
import com.henghao.hhworkpresent.utils.SqliteDBUtils;
import com.henghao.hhworkpresent.views.CustomDialog;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
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
 * 会议审批界面
 * Created by ASUS on 2017/9/26.
 */

public class MeetingReviewActivity extends ActivityFragmentSupport {

    @ViewInject(R.id.tv_meeting_theme)
    private TextView tv_meeting_theme;

    @ViewInject(R.id.tv_meeting_faqiren)
    private TextView tv_meeting_faqiren;

    @ViewInject(R.id.tv_meeting_start_time)
    private TextView tv_meeting_start_time;

    @ViewInject(R.id.tv_meeting_duration)
    private TextView tv_meeting_duration;

    @ViewInject(R.id.tv_join_meeting_people_num)
    private TextView tv_join_meeting_people_num;

    @ViewInject(R.id.tv_join_meeting_people)
    private TextView tv_join_meeting_people;

    @ViewInject(R.id.tv_meeting_agree)
    private TextView tv_meeting_agree;

    @ViewInject(R.id.tv_meeting_reject)
    private TextView tv_meeting_reject;

    private long msg_id;
    private int mid;
    private Handler mHandler = new Handler(){};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.mActivityFragmentView.viewMain(R.layout.activity_meeting_review);
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
        mCenterTextView.setText("会议审批");
        mCenterTextView.setVisibility(View.VISIBLE);

    }

    @Override
    public void initData() {
        super.initData();
        Intent intent = getIntent();
        msg_id = intent.getLongExtra("msg_id",0);
        httpRequestMeetingContent();
    }

    @OnClick({R.id.tv_meeting_agree,R.id.tv_meeting_reject})
    private void viewOnClick(View v) {
        switch (v.getId()){
            case R.id.tv_meeting_agree:
                httpRequestAgreeOrCancel(1,"");
                break;
            case R.id.tv_meeting_reject:
                showNoPassDialog();
                break;
        }
    }

    /**
     * 展示不同意对话框
     */
    public void showNoPassDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("您选择的处理方式");
        builder.setMessage("此会议确定驳回？");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 执行点击确定按钮的业务逻辑
                dialog.dismiss();
                showNoPassReasonDialog();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    /**
     * 显示不同意理由对话框
     */
    public void showNoPassReasonDialog(){
        final View customView = View.inflate(this,R.layout.layout_no_pass_dialog,null);
        final EditText et_no_pass_reason = (EditText) customView.findViewById(R.id.et_no_pass_reason);
        CustomDialog.Builder dialog=new CustomDialog.Builder(this);
        dialog.setTitle("请填写驳回会议的原因")
                .setContentView(customView)//设置自定义customView
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(et_no_pass_reason.getText().toString().equals("")){
                            Toast.makeText(MeetingReviewActivity.this,"请填写不通过原因",Toast.LENGTH_SHORT).show();
                            return;
                        }
                        httpRequestAgreeOrCancel(2,et_no_pass_reason.getText().toString());
                        dialogInterface.dismiss();
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        }).create().show();
    }

    /**
     * 1代表同意 2代表不同意
     * 点击同意或取消会走的接口并且上传理由
     * @param whetherPass
     */
    public void httpRequestAgreeOrCancel(final int whetherPass,String noPassReason){
        OkHttpClient okHttpClient = new OkHttpClient();
        Request.Builder builder = new Request.Builder();
        MultipartBuilder multipartBuilder = new MultipartBuilder();
        multipartBuilder.type(MultipartBuilder.FORM)
                .addFormDataPart("mid", String.valueOf(mid))
                .addFormDataPart("whetherPass",String.valueOf(whetherPass))
                .addFormDataPart("noPassReason",noPassReason);
        RequestBody requestBody = multipartBuilder.build();
        String request_url = ProtocolUrl.APP_ONCLICK_AGREE_OR_REJECT;
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
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent();
                        intent.setClass(MeetingReviewActivity.this,MeetingShenpiResultsActivity.class);
                        intent.putExtra("msg_id",msg_id);
                        startActivity(intent);
                        finish();
                    }
                });
            }

        });
    }

    /**
     * 从后台获取会议数据和消息数据
     */
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
                    mid = meetingEntity.getMid();
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
                            tv_meeting_start_time.setText(meetingEntity.getMeetingStartTime());
                            tv_meeting_duration.setText(meetingEntity.getMeetingDuration());
                            String name = meetingEntity.getUserIds();   //获取参会人员
                            String[] strings = name.split(",");
                            tv_join_meeting_people_num.setText(String.valueOf(strings.length));
                            tv_join_meeting_people.setText(name);
                        }
                    });

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        });
    }

}
