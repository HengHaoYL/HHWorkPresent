package com.henghao.hhworkpresent.activity;

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

public class ListModelTwoActivity extends ActivityFragmentSupport {

    @ViewInject(R.id.listone_two_linearlayout1)
    private LinearLayout linearLayout1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.mActivityFragmentView.viewMain(R.layout.activity_listone_two);
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
        mLeftTextView.setText("各处室负面清单");
        mLeftTextView.setVisibility(View.VISIBLE);
    }

    @Override
    public void initData() {
        super.initData();
    }

    @OnClick({R.id.listone_two_linearlayout1})
    private void viewOnClick(View v) {
        switch (v.getId()){
            case R.id.listone_two_linearlayout1:
                SqliteDBUtils sqliteDBUtils = new SqliteDBUtils(this);
                WebViewActivity.startToWebActivity(this,"企业负面清单3.28二处", WorkflowUrl.WORKFLOW_LIST_VIEW_URL + sqliteDBUtils.getUsername()+ WorkflowUrl.QYFMQD_FORMID);
                break;
        }
    }
}
