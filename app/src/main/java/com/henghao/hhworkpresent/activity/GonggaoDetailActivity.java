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
 * 未读已读公告详情页面
 * Created by bryanrady on 2017/3/24.
 */

public class GonggaoDetailActivity extends ActivityFragmentSupport {

    @ViewInject(R.id.gonggao_detail_content)
    private TextView gonggao_content;

    @ViewInject(R.id.gonggao_detail_author)
    private TextView gonggao_author;

    @ViewInject(R.id.gonggao_detail_date)
    private TextView gonggao_date;

    @ViewInject(R.id.gonggao_detail_titile)
    private TextView gonggao_titile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.mActivityFragmentView.viewMain(R.layout.activity_gonggao_detail);
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
        mLeftTextView.setText(getString(R.string.announcement_detail));
        mLeftTextView.setVisibility(View.VISIBLE);
    }

    @Override
    public void initData() {
        super.initData();
        Intent intent = getIntent();
        String titile = intent.getStringExtra("gongao_title");
        String author = intent.getStringExtra("gongao_author");
        String date = intent.getStringExtra("gongao_date");
        String content = intent.getStringExtra("gongao_content");
        gonggao_titile.setText(getString(R.string.announcement_title) + titile);
        gonggao_author.setText(getString(R.string.announcement_author) + author);
        gonggao_date.setText(getString(R.string.announcement_time) + date);
        gonggao_content.setText(getString(R.string.announcement_content)+content);
    }
}
