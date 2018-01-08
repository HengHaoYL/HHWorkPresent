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

public class ListModelOneActivity extends ActivityFragmentSupport {

    @ViewInject(R.id.listone_one_linearlayout1)
    private LinearLayout linearLayout1;

    @ViewInject(R.id.listone_one_linearlayout2)
    private LinearLayout linearLayout2;

    @ViewInject(R.id.listone_one_linearlayout3)
    private LinearLayout linearLayout3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.mActivityFragmentView.viewMain(R.layout.activity_listone_one);
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
        mLeftTextView.setText("负面清单资料");
        mLeftTextView.setVisibility(View.VISIBLE);
    }

    @Override
    public void initData() {
        super.initData();
    }

    @OnClick({R.id.listone_one_linearlayout1,R.id.listone_one_linearlayout2,R.id.listone_one_linearlayout3})
    private void viewOnClick(View v) {
        SqliteDBUtils sqliteDBUtils = new SqliteDBUtils(this);
        switch (v.getId()){
            case R.id.listone_one_linearlayout1:
                WebViewActivity.startToWebActivity(this,"负面清单相关法律法规", WorkflowUrl.WORKFLOW_LIST_VIEW_URL + sqliteDBUtils.getUsername() + WorkflowUrl.FMQDXGFLFG_FORMID);
                break;

            case R.id.listone_one_linearlayout2:
                WebViewActivity.startToWebActivity(this,"贵阳市安监局负面清单", WorkflowUrl.WORKFLOW_LIST_VIEW_URL + sqliteDBUtils.getUsername() + WorkflowUrl.GYSAJJFMQD_FORMID);
                break;

            case R.id.listone_one_linearlayout3:
                WebViewActivity.startToWebActivity(this,"实行“负面清单”管理工作细则1008", WorkflowUrl.WORKFLOW_LIST_VIEW_URL + sqliteDBUtils.getUsername() + WorkflowUrl.STWFMQD_FORMID);
                break;

        }
    }

}
