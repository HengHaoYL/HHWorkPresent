package com.henghao.hhworkpresent.activity;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.GridView;

import com.henghao.hhworkpresent.ActivityFragmentSupport;
import com.henghao.hhworkpresent.R;
import com.henghao.hhworkpresent.adapter.TextGridAdapter;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * 调查取证界面
 * Created by ASUS on 2017/9/20.
 */

public class DiaochaQuzhengActivity extends ActivityFragmentSupport {

    @ViewInject(R.id.diaocha_text_gridview)
    private GridView gridView;

    private TextGridAdapter textGridAdapter;
    private List<String> mTextList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.mActivityFragmentView.viewMain(R.layout.activity_diaocha_quzheng);
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
        mCenterTextView.setText("调查取证");
        mCenterTextView.setVisibility(View.VISIBLE);

    }

    @Override
    public void initData() {
        super.initData();
        mTextList = new ArrayList<>();
        mTextList.add("询问通知书");
        mTextList.add("询问笔录");
        mTextList.add("勘验笔录");
        mTextList.add("抽样取证凭证");
        mTextList.add("鉴定委托书");
        mTextList.add("先行登记保存证据审批表");
        mTextList.add("先行登记保存证据通知书");
        mTextList.add("先行登记保存证据处理审批表");
        mTextList.add("先行登记保存证据处理决定书");

        textGridAdapter = new TextGridAdapter(this,mTextList);
        gridView.setAdapter(textGridAdapter);
    }
}
