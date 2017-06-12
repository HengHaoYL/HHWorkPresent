package com.henghao.hhworkpresent.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.henghao.hhworkpresent.FragmentSupport;
import com.henghao.hhworkpresent.R;
import com.henghao.hhworkpresent.activity.AnquanfuwuActivity;
import com.henghao.hhworkpresent.activity.BanfaActivity;
import com.henghao.hhworkpresent.activity.CheliangyudingActivity;
import com.henghao.hhworkpresent.activity.ChushenActivity;
import com.henghao.hhworkpresent.activity.FushenActivity;
import com.henghao.hhworkpresent.activity.LingdaoQuXiangActivity;
import com.henghao.hhworkpresent.activity.RenwupaifaActivity;
import com.henghao.hhworkpresent.activity.SanzhongyidaActivity;
import com.henghao.hhworkpresent.activity.SendGonggaoActivity;
import com.henghao.hhworkpresent.activity.ShejishenchaActivity;
import com.henghao.hhworkpresent.activity.ZhidingjiahuaActivity;
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


    private Intent intent;

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
                intent.setClass(this.mActivity,CheliangyudingActivity.class);
                mActivity.startActivity(intent);
                break;
            case R.id.anquanfuwu:
                /*if("李苛".equals(sqliteDBUtils.getUsername())){*/
                    intent.setClass(this.mActivity,AnquanfuwuActivity.class);
                    mActivity.startActivity(intent);
                /*}else{
                    Toast.makeText(mActivity, "你不能发起当前流程，当前流程发起人为李苛!", Toast.LENGTH_SHORT).show();
                }*/
                break;
            case R.id.shejishencha:
                /*if("李苛".equals(sqliteDBUtils.getUsername())){*/
                    intent.setClass(this.mActivity,ShejishenchaActivity.class);
                    mActivity.startActivity(intent);
                /*}else{
                    Toast.makeText(mActivity, "你不能发起当前流程，当前流程发起人为李苛!", Toast.LENGTH_SHORT).show();
                }*/
                break;
            case R.id.chushen:
                /*if("唐小兵".equals(sqliteDBUtils.getUsername())){*/
                    intent.setClass(this.mActivity,ChushenActivity.class);
                    mActivity.startActivity(intent);
               /* }else{
                    Toast.makeText(mActivity, "你不能发起当前流程，此流程发起人为唐小兵!", Toast.LENGTH_SHORT).show();
                }*/
                break;
            case R.id.fushen:
//                if("唐小兵".equals(sqliteDBUtils.getUsername())){
                    intent.setClass(this.mActivity,FushenActivity.class);
                    mActivity.startActivity(intent);
                /*}else{
                    Toast.makeText(mActivity, "你不能发起当前流程，此流程发起人为唐小兵!", Toast.LENGTH_SHORT).show();
                }*/
                break;
            case R.id.banfa:
                intent.setClass(this.mActivity,BanfaActivity.class);
                mActivity.startActivity(intent);
                break;
            case R.id.zhidingjihua:
                intent.setClass(this.mActivity,ZhidingjiahuaActivity.class);
                mActivity.startActivity(intent);
                break;
            case R.id.fabugonggao:
                intent.setClass(this.mActivity,SendGonggaoActivity.class);
                mActivity.startActivity(intent);
                break;
            case R.id.renwupaifa:
                intent.setClass(this.mActivity,RenwupaifaActivity.class);
                mActivity.startActivity(intent);
                break;
            case R.id.lingdaoquxiang:
                intent.setClass(this.mActivity,LingdaoQuXiangActivity.class);
                mActivity.startActivity(intent);
                break;
            case R.id.sanzhongyida:
                intent.setClass(this.mActivity,SanzhongyidaActivity.class);
                mActivity.startActivity(intent);
                break;
        }
    }

}
