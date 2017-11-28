package com.henghao.hhworkpresent.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.henghao.hhworkpresent.FragmentSupport;
import com.henghao.hhworkpresent.R;
import com.henghao.hhworkpresent.WorkflowUrl;
import com.henghao.hhworkpresent.activity.WebViewActivity;
import com.henghao.hhworkpresent.utils.SqliteDBUtils;
import com.henghao.hhworkpresent.views.MyImageTextButton;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;


/**
 * Created by bryanrady on 2017/2/28.
 */

public class AppFragment extends FragmentSupport {

    @ViewInject(R.id.cheliangyuding)
    private MyImageTextButton cheliangyuding;

    @ViewInject(R.id.anquanfuwu)
    private MyImageTextButton anquanfuwu;

    @ViewInject(R.id.shejishencha)
    private MyImageTextButton shejishencha;

    @ViewInject(R.id.chushen)
    private MyImageTextButton chushen;

    @ViewInject(R.id.fushen)
    private MyImageTextButton fushen;

    @ViewInject(R.id.banfa)
    private MyImageTextButton banfa;

    @ViewInject(R.id.zhidingjihua)
    private MyImageTextButton zhidingjihua;

    @ViewInject(R.id.fabugonggao)
    private MyImageTextButton fabugonggao;

    @ViewInject(R.id.renwupaifa)
    private MyImageTextButton renwupaifa;

    @ViewInject(R.id.lingdaoquxiang)
    private MyImageTextButton lingdaoquxiang;

    @ViewInject(R.id.sanzhongyida)
    private MyImageTextButton sanzhongyida;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        this.mActivityFragmentView.viewMain(R.layout.fragment_app);
        this.mActivityFragmentView.viewEmpty(R.layout.activity_empty);
        this.mActivityFragmentView.viewEmptyGone();
        this.mActivityFragmentView.viewLoading(View.GONE);
        ViewUtils.inject(this, this.mActivityFragmentView);
        initWidget();
        initData();
        return this.mActivityFragmentView;
    }

    public void initWidget(){
        initwithContent();
    }

    public void initData(){
        cheliangyuding.setItemTextResource("派车申请");
        cheliangyuding.setItemImageResource(R.drawable.item_cheliangyuding);

        anquanfuwu.setItemTextResource("安全服务(非煤矿山)");
        anquanfuwu.setItemImageResource(R.drawable.item_anquanfuwu);

        shejishencha.setItemTextResource("设计审查(非煤矿山)");
        shejishencha.setItemImageResource(R.drawable.item_shejishencha);

        chushen.setItemTextResource("标准化初次申请");
        chushen.setItemImageResource(R.drawable.chushen);

        fushen.setItemTextResource("标准化复审");
        fushen.setItemImageResource(R.drawable.fushen);

        /*banfa.setItemTextResource("许可颁发");
        banfa.setItemImageResource(R.drawable.banfa);*/

        zhidingjihua.setItemTextResource("年初计划");
        zhidingjihua.setItemImageResource(R.drawable.zhidingjihua);

        fabugonggao.setItemTextResource("发布公告");
        fabugonggao.setItemImageResource(R.drawable.fabugonggao);

        renwupaifa.setItemTextResource("任务派发");
        renwupaifa.setItemImageResource(R.drawable.renwupaifa);

        lingdaoquxiang.setItemTextResource("领导去向");
        lingdaoquxiang.setItemImageResource(R.drawable.lingdaoquxiang);

        sanzhongyida.setItemTextResource("三重一大");
        sanzhongyida.setItemImageResource(R.drawable.sanzhongyida);

    }

    private void initwithContent() {
        initWithCenterBar();
        this.mCenterTextView.setVisibility(View.VISIBLE);
        this.mCenterTextView.setText("应用");
    }

    @OnClick({R.id.cheliangyuding,R.id.anquanfuwu,R.id.shejishencha,R.id.chushen, R.id.fushen,
            R.id.banfa,R.id.zhidingjihua,R.id.fabugonggao,R.id.renwupaifa,R.id.lingdaoquxiang,R.id.sanzhongyida})
    private void viewOnClick(View v){
        Intent intent = new Intent();
        SqliteDBUtils sqliteDBUtils = new SqliteDBUtils(mActivity);
        switch (v.getId()){
            case R.id.cheliangyuding:
                WebViewActivity.startToWebActivity(mActivity,"派车申请", WorkflowUrl.WORKFLOW_VIEW_URL + sqliteDBUtils.getUsername()+WorkflowUrl.CHELIANG_FLOWID);
                break;
            case R.id.anquanfuwu:
                WebViewActivity.startToWebActivity(mActivity,"安全服务", WorkflowUrl.WORKFLOW_VIEW_URL + sqliteDBUtils.getUsername()+WorkflowUrl.ANQUANFUWU_FLOWID);
                break;
            case R.id.shejishencha:
                WebViewActivity.startToWebActivity(mActivity,"设计审查", WorkflowUrl.WORKFLOW_VIEW_URL+sqliteDBUtils.getUsername()+ WorkflowUrl.SHEJISHENCHA_FLOWID);
                break;
            case R.id.chushen:
                WebViewActivity.startToWebActivity(mActivity,"标准化初次申请", WorkflowUrl.WORKFLOW_VIEW_URL+sqliteDBUtils.getUsername()+WorkflowUrl.CHUSHEN_FLOWID);
                break;
            case R.id.fushen:
                WebViewActivity.startToWebActivity(mActivity,"标准化复审", WorkflowUrl.WORKFLOW_VIEW_URL+sqliteDBUtils.getUsername()+WorkflowUrl.FUSHEN_FLOWID);
                break;
            case R.id.banfa:
                WebViewActivity.startToWebActivity(mActivity,"许可颁发", WorkflowUrl.WORKFLOW_URL+sqliteDBUtils.getUsername()+WorkflowUrl.XUKEBANFA_FLOWID);
                break;
            case R.id.zhidingjihua:
                WebViewActivity.startToWebActivity(mActivity,"年初计划", WorkflowUrl.WORKFLOW_VIEW_URL+sqliteDBUtils.getUsername()+WorkflowUrl.ZHIDINGJIHUA_FLOWID);
                break;
            case R.id.fabugonggao:
                WebViewActivity.startToWebActivity(mActivity,"发布公告", WorkflowUrl.WORKFLOW_VIEW_URL+ sqliteDBUtils.getUsername()+ WorkflowUrl.SENDGONGGAO_FLOWID);
                break;
            case R.id.renwupaifa:
                WebViewActivity.startToWebActivity(mActivity,"任务派发", WorkflowUrl.WORKFLOW_VIEW_URL+sqliteDBUtils.getUsername()+WorkflowUrl.RENWUPAIFA_FLOWID);
                break;
            case R.id.lingdaoquxiang:
                WebViewActivity.startToWebActivity(mActivity,"领导去向", WorkflowUrl.WORKFLOW_VIEW_URL+sqliteDBUtils.getUsername()+WorkflowUrl.LINGDAOQUXIANG_VIEWID);
                break;
            case R.id.sanzhongyida:
                WebViewActivity.startToWebActivity(mActivity,"三重一大", WorkflowUrl.WORKFLOW_VIEW_URL+sqliteDBUtils.getUsername()+ WorkflowUrl.SANZHONGYIDA_FLOWID);
                break;
        }
    }

}
