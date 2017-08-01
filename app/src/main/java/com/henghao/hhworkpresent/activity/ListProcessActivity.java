package com.henghao.hhworkpresent.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;

import com.henghao.hhworkpresent.ActivityFragmentSupport;
import com.henghao.hhworkpresent.R;
import com.henghao.hhworkpresent.WorkflowUrl;
import com.henghao.hhworkpresent.utils.SqliteDBUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * Created by bryanrady on 2017/8/1.
 */

public class ListProcessActivity extends ActivityFragmentSupport {

    @ViewInject(R.id.linearlayout1)
    private LinearLayout linearLayout1;

    @ViewInject(R.id.linearlayout2)
    private LinearLayout linearLayout2;

    @ViewInject(R.id.linearlayout3)
    private LinearLayout linearLayout3;

    @ViewInject(R.id.linearlayout4)
    private LinearLayout linearLayout4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.mActivityFragmentView.viewMain(R.layout.activity_listprocess);
        this.mActivityFragmentView.viewEmpty(R.layout.activity_empty);
        this.mActivityFragmentView.viewEmptyGone();
        this.mActivityFragmentView.viewLoading(View.GONE);
        this.mActivityFragmentView.viewLoadingError(View.GONE);
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
        mLeftTextView.setText("清单流程");
        mLeftTextView.setVisibility(View.VISIBLE);
    }

    @Override
    public void initData() {
        super.initData();
    }

    @OnClick({R.id.linearlayout1,R.id.linearlayout2,R.id.linearlayout3,R.id.linearlayout4})
    private void viewOnClick(View v) {
        Intent intent = new Intent();
        SqliteDBUtils sqliteDBUtils = new SqliteDBUtils(this);
        switch (v.getId()){
            case R.id.linearlayout1:
                intent.setClass(this, ListModelOneActivity.class);
                startActivity(intent);
                break;

            case R.id.linearlayout2:
                intent.setClass(this, ListModelTwoActivity.class);
                startActivity(intent);
                break;

            case R.id.linearlayout3:
                intent.setClass(this, ListModelThreeActivity.class);
                startActivity(intent);
                break;

            case R.id.linearlayout4:
                WebViewActivity.startToWebActivity(this,"权力清单和责任清单目录", WorkflowUrl.WORKFLOW_LIST_VIEW_URL + sqliteDBUtils.getUsername()+WorkflowUrl.ZRQDMF_FORMID);
                break;

        }
    }

}
