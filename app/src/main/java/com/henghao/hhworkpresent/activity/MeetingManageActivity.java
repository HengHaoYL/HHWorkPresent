package com.henghao.hhworkpresent.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.Window;

import com.henghao.hhworkpresent.ActivityFragmentSupport;
import com.henghao.hhworkpresent.R;
import com.henghao.hhworkpresent.adapter.TopPagerAdapter;
import com.henghao.hhworkpresent.fragment.MeetingPassListFragment;
import com.henghao.hhworkpresent.fragment.MeetingRecordListFragment;
import com.henghao.hhworkpresent.fragment.PushMessageListFragment;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * 会议管理界面
 * Created by ASUS on 2017/9/29.
 */

public class MeetingManageActivity extends ActivityFragmentSupport {

    @ViewInject(R.id.top_tabLayout)
    private TabLayout mTabLayout;

    @ViewInject(R.id.top_viewPager)
    private ViewPager mViewPager;

    private FragmentPagerAdapter mAdapter;
    private List<Fragment> mFragment;
    private List<String> mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.mActivityFragmentView.viewMain(R.layout.activity_xunchajiancha);
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
        mLeftTextView.setText("会议管理");
        mLeftTextView.setVisibility(View.VISIBLE);

        initTabFragment();

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void initData() {
        super.initData();
    }

    private void initTabFragment() {

        //初始化Fragment
        PushMessageListFragment fragment1 = new PushMessageListFragment();
        MeetingPassListFragment fragment2 = new MeetingPassListFragment();
        MeetingRecordListFragment fragment3 = new MeetingRecordListFragment();

        //将Fragment装进列表中
        mFragment = new ArrayList<>();
        mFragment.add(fragment1);
        mFragment.add(fragment2);
        mFragment.add(fragment3);

        //将名称添加daoTab列表
        mTitle = new ArrayList<>();
        mTitle.add("推送消息");
        mTitle.add("审批通过");
        mTitle.add("会议记录");

        //为TabLayout添加Tab名称
        mTabLayout.addTab(mTabLayout.newTab().setText(mTitle.get(0)));
        mTabLayout.addTab(mTabLayout.newTab().setText(mTitle.get(1)));
        mTabLayout.addTab(mTabLayout.newTab().setText(mTitle.get(2)));

        mAdapter = new TopPagerAdapter(this.getSupportFragmentManager(), mFragment, mTitle);

        //ViewPager加载Adapter
        mViewPager.setAdapter(mAdapter);

        //TabLayout加载ViewPager
        mTabLayout.setupWithViewPager(mViewPager);
    }

}
