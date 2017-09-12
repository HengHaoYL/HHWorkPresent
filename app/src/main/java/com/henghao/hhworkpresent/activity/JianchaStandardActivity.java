package com.henghao.hhworkpresent.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.henghao.hhworkpresent.ActivityFragmentSupport;
import com.henghao.hhworkpresent.R;
import com.henghao.hhworkpresent.adapter.ProblemStandardListAdapter;
import com.henghao.hhworkpresent.entity.JianchaMaterialEntity;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.List;

/**
 * 执法检查标准
 * Created by ASUS on 2017/9/12.
 */

public class JianchaStandardActivity extends ActivityFragmentSupport {

    @ViewInject(R.id.problem_checked_listview)
    private ListView problem_checked_listview;

    @ViewInject(R.id.tv_standard_save)
    private TextView tv_standard_save;

    private ProblemStandardListAdapter mProblemStandardListAdapter;
    private List<JianchaMaterialEntity> mJianchaMaterialEntityList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.mActivityFragmentView.viewMain(R.layout.activity_jiancha_standard);
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
        mCenterTextView.setText("执法检查标准");
        mCenterTextView.setVisibility(View.VISIBLE);

        //保存操作
        tv_standard_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    @Override
    public void initData() {
        super.initData();
        mJianchaMaterialEntityList = (List<JianchaMaterialEntity>)getIntent().getSerializableExtra("mSelectDescriptData");
        mProblemStandardListAdapter = new ProblemStandardListAdapter(this,mJianchaMaterialEntityList);
        problem_checked_listview.setAdapter(mProblemStandardListAdapter);
    }
}
