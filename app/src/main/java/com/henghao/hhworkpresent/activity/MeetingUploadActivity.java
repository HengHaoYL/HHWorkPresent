package com.henghao.hhworkpresent.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;

import com.henghao.hhworkpresent.ActivityFragmentSupport;
import com.henghao.hhworkpresent.R;
import com.henghao.hhworkpresent.utils.SqliteDBUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * 会议上传界面
 * Created by ASUS on 2017/9/28.
 */

public class MeetingUploadActivity extends ActivityFragmentSupport {

    @ViewInject(R.id.tv_meeting_upload_theme)
    private TextView tv_meeting_upload_theme;

    @ViewInject(R.id.tv_meeting_upload_people)
    private TextView tv_meeting_upload_people;

    @ViewInject(R.id.tv_meeting_upload_start_time)
    private TextView tv_meeting_upload_start_time;

    @ViewInject(R.id.tv_meeting_upload_duration)
    private TextView tv_meeting_upload_duration;

    @ViewInject(R.id.tv_meeting_upload_place)
    private TextView tv_meeting_upload_place;

    @ViewInject(R.id.tv_meeting_join_people)
    private TextView tv_meeting_join_people;

    @ViewInject(R.id.et_meeting_upload_qiandao_people)
    private EditText et_meeting_upload_qiandao_people;

    @ViewInject(R.id.et_meeting_upload_content)
    private EditText et_meeting_upload_content;

    @ViewInject(R.id.et_meeting_upload_summary)
    private EditText et_meeting_upload_summary;

    @ViewInject(R.id.upload_meeting_picture_gridview)
    private GridView upload_meeting_picture_gridview;

    @ViewInject(R.id.tv_meeting_upload_save)
    private TextView tv_meeting_upload_save;

    @ViewInject(R.id.tv_meeting_upload_cancel)
    private TextView tv_meeting_upload_cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.mActivityFragmentView.viewMain(R.layout.activity_meeting_upload);
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
        mCenterTextView.setText("会议上传");
        mCenterTextView.setVisibility(View.VISIBLE);
    }

    @Override
    public void initData() {
        super.initData();
        SqliteDBUtils sqliteDBUtils = new SqliteDBUtils(this);
        Intent data = getIntent();
        tv_meeting_upload_theme.setText(data.getStringExtra("meetingTheme"));
        tv_meeting_upload_people.setText(sqliteDBUtils.getLoginFirstName()+sqliteDBUtils.getLoginGiveName());
        tv_meeting_upload_start_time.setText(data.getStringExtra("meetingStartTime"));
        tv_meeting_upload_duration.setText(data.getStringExtra("meetingDuration"));
        tv_meeting_upload_place.setText(data.getStringExtra("meetingPlace"));
        tv_meeting_join_people.setText(data.getStringExtra("meetingJoinPeople"));
    }

    @OnClick({R.id.tv_meeting_upload_save,R.id.tv_meeting_upload_cancel})
    public void viewClick(View v) {
        switch (v.getId()) {
            case R.id.tv_meeting_upload_save:
                break;
            case R.id.tv_meeting_upload_cancel:
                break;
        }
    }

}
