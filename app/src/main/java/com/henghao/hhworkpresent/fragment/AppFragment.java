package com.henghao.hhworkpresent.fragment;

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
        cheliangyuding.setItemTextResource(getString(R.string.paicheshenqing));
        cheliangyuding.setItemImageResource(R.drawable.item_cheliangyuding);

        anquanfuwu.setItemTextResource(getString(R.string.anquanfuwu));
        anquanfuwu.setItemImageResource(R.drawable.item_anquanfuwu);

        shejishencha.setItemTextResource(getString(R.string.shejishencha));
        shejishencha.setItemImageResource(R.drawable.item_shejishencha);

        chushen.setItemTextResource(getString(R.string.standard_first_application));
        chushen.setItemImageResource(R.drawable.chushen);

        fushen.setItemTextResource(getString(R.string.standard_second_application));
        fushen.setItemImageResource(R.drawable.fushen);

        zhidingjihua.setItemTextResource(getString(R.string.makeplan));
        zhidingjihua.setItemImageResource(R.drawable.zhidingjihua);

        fabugonggao.setItemTextResource(getString(R.string.sendgonggao));
        fabugonggao.setItemImageResource(R.drawable.fabugonggao);

        renwupaifa.setItemTextResource(getString(R.string.renwupaifa));
        renwupaifa.setItemImageResource(R.drawable.renwupaifa);

        lingdaoquxiang.setItemTextResource(getString(R.string.lingdaoquxiang));
        lingdaoquxiang.setItemImageResource(R.drawable.lingdaoquxiang);

        sanzhongyida.setItemTextResource(getString(R.string.sanzhongyida));
        sanzhongyida.setItemImageResource(R.drawable.sanzhongyida);

    }

    private void initwithContent() {
        initWithCenterBar();
        this.mCenterTextView.setVisibility(View.VISIBLE);
        this.mCenterTextView.setText(getString(R.string.hc_app));
    }

    @OnClick({R.id.cheliangyuding,R.id.anquanfuwu,R.id.shejishencha,R.id.chushen, R.id.fushen,
            R.id.zhidingjihua,R.id.fabugonggao,R.id.renwupaifa,R.id.lingdaoquxiang,R.id.sanzhongyida})
    private void viewOnClick(View v){
        SqliteDBUtils sqliteDBUtils = new SqliteDBUtils(mActivity);
        switch (v.getId()){
            case R.id.cheliangyuding:
                WebViewActivity.startToWebActivity(mActivity,getString(R.string.paicheshenqing), WorkflowUrl.WORKFLOW_VIEW_URL + sqliteDBUtils.getUsername()+WorkflowUrl.CHELIANG_FLOWID);
                break;
            case R.id.anquanfuwu:
                WebViewActivity.startToWebActivity(mActivity,getString(R.string.anquanfuwu), WorkflowUrl.WORKFLOW_VIEW_URL + sqliteDBUtils.getUsername()+WorkflowUrl.ANQUANFUWU_FLOWID);
                break;
            case R.id.shejishencha:
                WebViewActivity.startToWebActivity(mActivity,getString(R.string.shejishencha), WorkflowUrl.WORKFLOW_VIEW_URL+sqliteDBUtils.getUsername()+ WorkflowUrl.SHEJISHENCHA_FLOWID);
                break;
            case R.id.chushen:
                WebViewActivity.startToWebActivity(mActivity,getString(R.string.standard_first_application), WorkflowUrl.WORKFLOW_VIEW_URL+sqliteDBUtils.getUsername()+WorkflowUrl.CHUSHEN_FLOWID);
                break;
            case R.id.fushen:
                WebViewActivity.startToWebActivity(mActivity,getString(R.string.standard_second_application), WorkflowUrl.WORKFLOW_VIEW_URL+sqliteDBUtils.getUsername()+WorkflowUrl.FUSHEN_FLOWID);
                break;
            case R.id.zhidingjihua:
                WebViewActivity.startToWebActivity(mActivity,getString(R.string.makeplan), WorkflowUrl.WORKFLOW_VIEW_URL+sqliteDBUtils.getUsername()+WorkflowUrl.ZHIDINGJIHUA_FLOWID);
                break;
            case R.id.fabugonggao:
                WebViewActivity.startToWebActivity(mActivity,getString(R.string.sendgonggao), WorkflowUrl.WORKFLOW_VIEW_URL+ sqliteDBUtils.getUsername()+ WorkflowUrl.SENDGONGGAO_FLOWID);
                break;
            case R.id.renwupaifa:
                WebViewActivity.startToWebActivity(mActivity,getString(R.string.renwupaifa), WorkflowUrl.WORKFLOW_VIEW_URL+sqliteDBUtils.getUsername()+WorkflowUrl.RENWUPAIFA_FLOWID);
                break;
            case R.id.lingdaoquxiang:
                WebViewActivity.startToWebActivity(mActivity,getString(R.string.lingdaoquxiang), WorkflowUrl.WORKFLOW_VIEW_URL+sqliteDBUtils.getUsername()+WorkflowUrl.LINGDAOQUXIANG_VIEWID);
                break;
            case R.id.sanzhongyida:
                WebViewActivity.startToWebActivity(mActivity,getString(R.string.sanzhongyida), WorkflowUrl.WORKFLOW_VIEW_URL+sqliteDBUtils.getUsername()+ WorkflowUrl.SANZHONGYIDA_FLOWID);
                break;
        }
    }

}
