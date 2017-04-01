package com.henghao.hhworkpresent.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.henghao.hhworkpresent.ActivityFragmentSupport;
import com.henghao.hhworkpresent.R;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * Created by bryanrady on 2017/4/1.
 */

public class MyTongxunluDetailActivity extends ActivityFragmentSupport {

    @ViewInject(R.id.tongxunlu_tv_name)
    private TextView tv_name;

    @ViewInject(R.id.tongxunlu_tv_zhiwei)
    private TextView tv_sysname;

    @ViewInject(R.id.tongxunlu_tv_zhengzhimianmao)
    private TextView tv_political;

    @ViewInject(R.id.tongxunlu_tv_sex)
    private TextView tv_sex;

    @ViewInject(R.id.tongxunlu_tv_bumen)
    private TextView tv_orgname;

    @ViewInject(R.id.tongxunlu_tv_moblenumber)
    private TextView tv_mobilephone;


    private String username;
    private String sysname;
    private String political;
    private String sex;
    private String orgname;
    private String mobilephone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.mActivityFragmentView.viewMain(R.layout.activity_tongxunlu_detail);
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
        mLeftTextView.setText("通讯录详细界面");
        mLeftTextView.setVisibility(View.VISIBLE);
    }

    @Override
    public void initData() {
        super.initData();
        Intent intent = getIntent();
        username = intent.getStringExtra("username");
        sysname = intent.getStringExtra("sysname");
        political = intent.getStringExtra("political");
        sex = intent.getStringExtra("sex");
        orgname = intent.getStringExtra("orgname");
        mobilephone = intent.getStringExtra("mobilephone");

        tv_name.setText(username);
        tv_sex.setText(sex);
        tv_orgname.setText(orgname);
        tv_political.setText(political);
        tv_sysname.setText(sysname);
        tv_mobilephone.setText(mobilephone);

    }
}
