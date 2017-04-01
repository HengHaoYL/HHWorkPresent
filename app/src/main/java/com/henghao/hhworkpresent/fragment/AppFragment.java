package com.henghao.hhworkpresent.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.benefit.buy.library.viewpagerindicator.CirclePageIndicator;
import com.henghao.hhworkpresent.FragmentSupport;
import com.henghao.hhworkpresent.R;
import com.henghao.hhworkpresent.activity.BangGongRWActivity;
import com.henghao.hhworkpresent.activity.CheliangSPActivity;
import com.henghao.hhworkpresent.activity.XingZhenZFActivity;
import com.henghao.hhworkpresent.activity.XingZhenSPActivity;
import com.henghao.hhworkpresent.views.AutoScrollViewPager;
import com.henghao.hhworkpresent.views.CommonAutoViewpager;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import static com.henghao.hhworkpresent.R.id.tv_bangongRW;
import static com.henghao.hhworkpresent.R.id.tv_xingzhengSP;
import static com.henghao.hhworkpresent.R.id.tv_xingzhengZF;


/**
 * Created by bryanrady on 2017/2/28.
 */

public class AppFragment extends FragmentSupport {

    @ViewInject(R.id.tv_cheliangSP)
    private TextView cheliangSP;

    @ViewInject(tv_xingzhengSP)
    private TextView xingzhengSP;

    @ViewInject(tv_xingzhengZF)
    private TextView xingzhengZF;

    @ViewInject(tv_bangongRW)
    private TextView bangongRW;

    @ViewInject(R.id.view_pager)
    private AutoScrollViewPager viewPager;


    @ViewInject(R.id.indicator)
    private CirclePageIndicator indicator;

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
        initWithBar();
        mLeftImageView.setVisibility(View.VISIBLE);
        mLeftImageView.setImageResource(R.drawable.item_setting);
        initwithContent();
        initWithRightBar();
        mRightImageView.setVisibility(View.VISIBLE);
        mRightImageView.setImageResource(R.drawable.item_searching);
    }

    public void initData(){
        CommonAutoViewpager.viewPageAdapter(this.mActivity, viewPager, indicator);
    }

    private void initwithContent() {
        // TODO Auto-generated method stub
        initWithCenterBar();
        this.mCenterTextView.setVisibility(View.VISIBLE);
        this.mCenterTextView.setText("应用");
    }

    @OnClick({R.id.tv_cheliangSP,tv_xingzhengSP,tv_bangongRW,tv_xingzhengZF})
    private void viewOnClick(View v){
        Intent intent = new Intent();
        switch (v.getId()){
            case R.id.tv_cheliangSP:
                intent.setClass(getActivity(), CheliangSPActivity.class);
                startActivity(intent);
                break;
            case tv_xingzhengSP:
                intent.setClass(getActivity(), XingZhenSPActivity.class);
                startActivity(intent);
                break;
            case tv_xingzhengZF:
                intent.setClass(getActivity(), XingZhenZFActivity.class);
                startActivity(intent);
                break;
            case tv_bangongRW:
                intent.setClass(getActivity(), BangGongRWActivity.class);
                startActivity(intent);
                break;
        }
    }
}
