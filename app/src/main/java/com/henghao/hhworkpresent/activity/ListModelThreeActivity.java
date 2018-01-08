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

public class ListModelThreeActivity extends ActivityFragmentSupport {

    @ViewInject(R.id.listone_three_linearlayout1)
    private LinearLayout linearLayout1;

    @ViewInject(R.id.listone_three_linearlayout2)
    private LinearLayout linearLayout2;

    @ViewInject(R.id.listone_three_linearlayout3)
    private LinearLayout linearLayout3;

    @ViewInject(R.id.listone_three_linearlayout4)
    private LinearLayout linearLayout4;

    @ViewInject(R.id.listone_three_linearlayout5)
    private LinearLayout linearLayout5;

    @ViewInject(R.id.listone_three_linearlayout6)
    private LinearLayout linearLayout6;

    @ViewInject(R.id.listone_three_linearlayout7)
    private LinearLayout linearLayout7;

    @ViewInject(R.id.listone_three_linearlayout8)
    private LinearLayout linearLayout8;

    @ViewInject(R.id.listone_three_linearlayout9)
    private LinearLayout linearLayout9;

    @ViewInject(R.id.listone_three_linearlayout10)
    private LinearLayout linearLayout10;

    @ViewInject(R.id.listone_three_linearlayout11)
    private LinearLayout linearLayout11;

    @ViewInject(R.id.listone_three_linearlayout12)
    private LinearLayout linearLayout12;

    @ViewInject(R.id.listone_three_linearlayout13)
    private LinearLayout linearLayout13;

    @ViewInject(R.id.listone_three_linearlayout14)
    private LinearLayout linearLayout14;

    @ViewInject(R.id.listone_three_linearlayout15)
    private LinearLayout linearLayout15;

    @ViewInject(R.id.listone_three_linearlayout16)
    private LinearLayout linearLayout16;

    @ViewInject(R.id.listone_three_linearlayout17)
    private LinearLayout linearLayout17;

    @ViewInject(R.id.listone_three_linearlayout18)
    private LinearLayout linearLayout18;

    @ViewInject(R.id.listone_three_linearlayout19)
    private LinearLayout linearLayout19;

    @ViewInject(R.id.listone_three_linearlayout20)
    private LinearLayout linearLayout20;

    @ViewInject(R.id.listone_three_linearlayout21)
    private LinearLayout linearLayout21;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.mActivityFragmentView.viewMain(R.layout.activity_listone_three);
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
        mLeftTextView.setText("市安监局网上办事大厅工作流程");
        mLeftTextView.setVisibility(View.VISIBLE);
    }

    @Override
    public void initData() {
        super.initData();
    }

    @OnClick({R.id.listone_three_linearlayout1,R.id.listone_three_linearlayout2,R.id.listone_three_linearlayout3,
            R.id.listone_three_linearlayout4,R.id.listone_three_linearlayout5,R.id.listone_three_linearlayout6,
            R.id.listone_three_linearlayout7,R.id.listone_three_linearlayout8,R.id.listone_three_linearlayout9,
            R.id.listone_three_linearlayout10,R.id.listone_three_linearlayout11,R.id.listone_three_linearlayout12,
            R.id.listone_three_linearlayout13,R.id.listone_three_linearlayout14,R.id.listone_three_linearlayout15,
            R.id.listone_three_linearlayout16,R.id.listone_three_linearlayout17,R.id.listone_three_linearlayout18,
            R.id.listone_three_linearlayout19,R.id.listone_three_linearlayout20,R.id.listone_three_linearlayout21})
    private void viewOnClick(View v) {
        SqliteDBUtils sqliteDBUtils = new SqliteDBUtils(this);
        switch (v.getId()){
            case R.id.listone_three_linearlayout1:
                WebViewActivity.startToWebActivity(this,"非煤矿山安全生产许可证核发审查流程", WorkflowUrl.WORKFLOW_LIST_VIEW_URL + sqliteDBUtils.getUsername() + WorkflowUrl.FLOW1_FORMID);
                break;

            case R.id.listone_three_linearlayout2:
                WebViewActivity.startToWebActivity(this,"非煤矿山建设项目安全设施设计审查", WorkflowUrl.WORKFLOW_LIST_VIEW_URL + sqliteDBUtils.getUsername() + WorkflowUrl.FLOW2_FORMID);
                break;

            case R.id.listone_three_linearlayout3:
                WebViewActivity.startToWebActivity(this,"非药品类易制毒化学品生产经营备案流程", WorkflowUrl.WORKFLOW_LIST_VIEW_URL + sqliteDBUtils.getUsername() + WorkflowUrl.FLOW3_FORMID);
                break;

            case R.id.listone_three_linearlayout4:
                WebViewActivity.startToWebActivity(this,"建设项目职业病防护设施设计审查", WorkflowUrl.WORKFLOW_LIST_VIEW_URL + sqliteDBUtils.getUsername() + WorkflowUrl.FLOW4_FORMID);
                break;

            case R.id.listone_three_linearlayout5:
                WebViewActivity.startToWebActivity(this,"建设项目职业病危害防护设施竣工备案", WorkflowUrl.WORKFLOW_LIST_VIEW_URL + sqliteDBUtils.getUsername() + WorkflowUrl.FLOW5_FORMID);
                break;

            case R.id.listone_three_linearlayout6:
                WebViewActivity.startToWebActivity(this,"建设项目职业病危害防护设施竣工验收", WorkflowUrl.WORKFLOW_LIST_VIEW_URL + sqliteDBUtils.getUsername() + WorkflowUrl.FLOW6_FORMID);
                break;

            case R.id.listone_three_linearlayout7:
                WebViewActivity.startToWebActivity(this,"建设项目职业病危害预评价报告审核", WorkflowUrl.WORKFLOW_LIST_VIEW_URL + sqliteDBUtils.getUsername() + WorkflowUrl.FLOW7_FORMID);
                break;

            case R.id.listone_three_linearlayout8:
                WebViewActivity.startToWebActivity(this,"生产经营单位应急救援预案备案", WorkflowUrl.WORKFLOW_LIST_VIEW_URL + sqliteDBUtils.getUsername() + WorkflowUrl.FLOW8_FORMID);
                break;

            case R.id.listone_three_linearlayout9:
                WebViewActivity.startToWebActivity(this,"危险化学品安全生产许可证初审流程", WorkflowUrl.WORKFLOW_LIST_VIEW_URL + sqliteDBUtils.getUsername() + WorkflowUrl.FLOW9_FORMID);
                break;

            case R.id.listone_three_linearlayout10:
                WebViewActivity.startToWebActivity(this,"危险化学品安全使用许可流程", WorkflowUrl.WORKFLOW_LIST_VIEW_URL + sqliteDBUtils.getUsername() + WorkflowUrl.FLOW10_FORMID);
                break;

            case R.id.listone_three_linearlayout11:
                WebViewActivity.startToWebActivity(this,"危险化学品建设项目安全设施设计审查流程", WorkflowUrl.WORKFLOW_LIST_VIEW_URL + sqliteDBUtils.getUsername() + WorkflowUrl.FLOW11_FORMID);
                break;

            case R.id.listone_three_linearlayout12:
                WebViewActivity.startToWebActivity(this,"危险化学品建设项目安全条件审查流程", WorkflowUrl.WORKFLOW_LIST_VIEW_URL + sqliteDBUtils.getUsername() + WorkflowUrl.FLOW12_FORMID);
                break;

            case R.id.listone_three_linearlayout13:
                WebViewActivity.startToWebActivity(this,"危险化学品建设项目试生产（使用）方案备案流程", WorkflowUrl.WORKFLOW_LIST_VIEW_URL + sqliteDBUtils.getUsername() + WorkflowUrl.FLOW13_FORMID);
                break;

            case R.id.listone_three_linearlayout14:
                WebViewActivity.startToWebActivity(this,"危险化学品经营许可流程", WorkflowUrl.WORKFLOW_LIST_VIEW_URL + sqliteDBUtils.getUsername() + WorkflowUrl.FLOW14_FORMID);
                break;

            case R.id.listone_three_linearlayout15:
                WebViewActivity.startToWebActivity(this,"烟花爆竹经营（批发）许可审查流程", WorkflowUrl.WORKFLOW_LIST_VIEW_URL + sqliteDBUtils.getUsername() + WorkflowUrl.FLOW15_FORMID);
                break;

            case R.id.listone_three_linearlayout16:
                WebViewActivity.startToWebActivity(this,"用人单位职业病危害现状评价报告备案", WorkflowUrl.WORKFLOW_LIST_VIEW_URL + sqliteDBUtils.getUsername() + WorkflowUrl.FLOW16_FORMID);
                break;

            case R.id.listone_three_linearlayout17:
                WebViewActivity.startToWebActivity(this,"用人单位主要负责人和管理人员职业卫生培训考核", WorkflowUrl.WORKFLOW_LIST_VIEW_URL + sqliteDBUtils.getUsername() + WorkflowUrl.FLOW17_FORMID);
                break;

            case R.id.listone_three_linearlayout18:
                WebViewActivity.startToWebActivity(this,"职业病危害项目申报审核", WorkflowUrl.WORKFLOW_LIST_VIEW_URL + sqliteDBUtils.getUsername() + WorkflowUrl.FLOW18_FORMID);
                break;

            case R.id.listone_three_linearlayout19:
                WebViewActivity.startToWebActivity(this,"职业病危害预评价报告备案", WorkflowUrl.WORKFLOW_LIST_VIEW_URL + sqliteDBUtils.getUsername() + WorkflowUrl.FLOW19_FORMID);
                break;

            case R.id.listone_three_linearlayout20:
                WebViewActivity.startToWebActivity(this,"职业卫生安全许可证书颁发", WorkflowUrl.WORKFLOW_LIST_VIEW_URL + sqliteDBUtils.getUsername() + WorkflowUrl.FLOW20_FORMID);
                break;

            case R.id.listone_three_linearlayout21:
                WebViewActivity.startToWebActivity(this,"职业卫生技术服务机构丙级资质认可和颁发证书", WorkflowUrl.WORKFLOW_LIST_VIEW_URL + sqliteDBUtils.getUsername() + WorkflowUrl.FLOW21_FORMID);
                break;

        }
    }
}
