package com.henghao.hhworkpresent.activity;

import android.os.Bundle;
import android.view.View;
import android.view.Window;

import com.henghao.hhworkpresent.ActivityFragmentSupport;
import com.henghao.hhworkpresent.R;
import com.lidroid.xutils.ViewUtils;

/**
 * 新增隐患界面
 * Created by ASUS on 2017/9/4.
 */

public class AddYinhuanActivity extends ActivityFragmentSupport {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.mActivityFragmentView.viewMain(R.layout.activity_add_yinhuan);
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
        mLeftImageView.setVisibility(View.VISIBLE);
        initWithCenterBar();
        mCenterTextView.setText("新增隐患");
        mCenterTextView.setVisibility(View.VISIBLE);
    }

    @Override
    public void initData() {
        super.initData();
    }
}
