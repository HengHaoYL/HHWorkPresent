package com.henghao.hhworkpresent.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.henghao.hhworkpresent.ActivityFragmentSupport;
import com.henghao.hhworkpresent.R;
import com.henghao.hhworkpresent.entity.MeetingEntity;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 会议现场签到界面
 * Created by bryanrady on 2017/10/23.
 */

public class MeetingQiandaoActivity extends ActivityFragmentSupport {

    @ViewInject(R.id.tv_meeting_qiandao_theme)
    private TextView tv_meeting_qiandao_theme;

    @ViewInject(R.id.tv_meeting_qiandao_place)
    private TextView tv_meeting_qiandao_place;

    @ViewInject(R.id.tv_meeting_qiandao_start_time)
    private TextView tv_meeting_qiandao_start_time;

    @ViewInject(R.id.tv_meeting_qiandao_duration)
    private TextView tv_meeting_qiandao_duration;

    @ViewInject(R.id.tv_meeting_qiandao_people_num)
    private TextView tv_meeting_qiandao_people_num;

    @ViewInject(R.id.image_meeting_qiandao_start)
    private ImageView image_meeting_qiandao_start;

    @ViewInject(R.id.image_meeting_qiandao_end)
    private ImageView image_meeting_qiandao_end;

    @ViewInject(R.id.tv_qiandao_start_text)
    private TextView tv_qiandao_start_text;

    @ViewInject(R.id.tv_qiandao_start_time)
    private TextView tv_qiandao_start_time;

    @ViewInject(R.id.tv_qiandao_end_text)
    private TextView tv_qiandao_end_text;

    @ViewInject(R.id.tv_qiandao_end_time)
    private TextView tv_qiandao_end_time;

    private MeetingEntity meetingEntity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.mActivityFragmentView.viewMain(R.layout.activity_meeting_qiandao);
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
        mCenterTextView.setText("会议签到");
        mCenterTextView.setVisibility(View.VISIBLE);
    }

    @Override
    public void initData() {
        super.initData();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
        String currentDate = simpleDateFormat.format(new Date());
        tv_qiandao_start_time.setText(currentDate);
        tv_qiandao_end_time.setText(currentDate);

        meetingEntity = (MeetingEntity) getIntent().getSerializableExtra("meetingEntity");
        tv_meeting_qiandao_theme.setText(meetingEntity.getMeetingTheme());
        tv_meeting_qiandao_place.setText(meetingEntity.getMeetingPlace());
        tv_meeting_qiandao_start_time.setText(meetingEntity.getMeetingStartTime());
        tv_meeting_qiandao_duration.setText(meetingEntity.getMeetingDuration());
        tv_meeting_qiandao_people_num.setText(meetingEntity.getUserIds());
    }

    @OnClick({R.id.image_meeting_qiandao_start,R.id.image_meeting_qiandao_end})
    private void viewOnClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()){
            case R.id.image_meeting_qiandao_start:  //进场签到
                break;
            case R.id.image_meeting_qiandao_end:    //退场签到
                break;
        }
    }

}
