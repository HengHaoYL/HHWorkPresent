package com.henghao.hhworkpresent.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.henghao.hhworkpresent.ActivityFragmentSupport;
import com.henghao.hhworkpresent.R;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * Created by bryanrady on 2017/3/30.
 */

public class KaoqingKuanggongDetailActivity extends ActivityFragmentSupport {

    @ViewInject(R.id.kaoqing_chidao_username)
    private TextView tv_userName;

    @ViewInject(R.id.kaoqing_chidao_currentdate)
    private TextView tv_currentDate;

    @ViewInject(R.id.kaoqing_chidao_currentweek)
    private TextView tv_currentWeek;

    @ViewInject(R.id.kaoqing_chidao_shangbantime)
    private TextView tv_shangbanTime;

    @ViewInject(R.id.kaoqing_chidao_shangbanDakaState)
    private TextView tv_shangbanState;

    @ViewInject(R.id.kaoqing_chidao_shangbanBuka)
    private Button btn_shangbanBuka;

    @ViewInject(R.id.kaoqing_chidao_xiabantime)
    private TextView tv_xiabanTime;

    @ViewInject(R.id.kaoqing_chidao_xiabanDakaState)
    private TextView tv_xiabanState;

    @ViewInject(R.id.kaoqing_chidao_xiabanBuka)
    private Button btn_xiabanBuka;

    @ViewInject(R.id.kaoqing_chidao_xiabanLinear)
    private RelativeLayout xiabanLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.mActivityFragmentView.viewMain(R.layout.activity_kaoqingkuanggongdetail);
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
        mLeftTextView.setText("考勤详情");
        mLeftTextView.setVisibility(View.VISIBLE);

        mActivityFragmentView.viewLoading(View.VISIBLE);
    }

    @Override
    public void initData() {
        super.initData();
        Intent intent = getIntent();
        String currentDate = intent.getStringExtra("currentDate");
        String currentWeek = intent.getStringExtra("currentWeek");
        tv_currentDate.setText(currentDate);
        tv_currentWeek.setText(currentWeek);

        tv_shangbanState.setText("缺卡");
        tv_shangbanTime.setText("无");
        btn_shangbanBuka.setVisibility(View.VISIBLE);
        tv_xiabanState.setText("缺卡");
        tv_xiabanTime.setText("无");
        btn_xiabanBuka.setVisibility(View.VISIBLE);
        mActivityFragmentView.viewLoading(View.GONE);
    }
}
