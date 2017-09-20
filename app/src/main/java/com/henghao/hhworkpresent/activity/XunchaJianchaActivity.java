package com.henghao.hhworkpresent.activity;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TabHost;
import android.widget.TextView;

import com.henghao.hhworkpresent.ActivityFragmentSupport;
import com.henghao.hhworkpresent.R;
import com.henghao.hhworkpresent.adapter.TopPagerAdapter;
import com.henghao.hhworkpresent.fragment.HistoryRecordFragment;
import com.henghao.hhworkpresent.fragment.ObtainStatementFragment;
import com.henghao.hhworkpresent.fragment.WoyaoCheckFragment;
import com.henghao.hhworkpresent.fragment.WoyaoFuchaFragment;
import com.henghao.hhworkpresent.utils.AnimationUtil;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * 巡查检查模块
 * Created by ASUS on 2017/9/4.
 */

public class XunchaJianchaActivity extends ActivityFragmentSupport {

    @ViewInject(R.id.gray_layout)
    private View mGrayLayout;

    private PopupWindow mPopupWindow;
    private boolean isPopWindowShowing = false;
    private int fromYDelta;

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
        mLeftTextView.setText("我的任务");
        mLeftTextView.setVisibility(View.VISIBLE);
        initWithRightBar();
        mRightImageView.setImageResource(R.drawable.item_add);
        mRightImageView.setVisibility(View.VISIBLE);
        mRightImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isPopWindowShowing){
                    mPopupWindow.getContentView().startAnimation(AnimationUtil.createOutAnimation(XunchaJianchaActivity.this, fromYDelta));
                    mPopupWindow.getContentView().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mPopupWindow.dismiss();
                        }
                    },AnimationUtil.ANIMATION_OUT_TIME);
                }else{
                    showPopupWindow();
                }
            }
        });

        //对黑色半透明背景做监听，点击时开始退出动画并将popupwindow dismiss掉
        mGrayLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isPopWindowShowing){
                    mPopupWindow.getContentView().startAnimation(AnimationUtil.createOutAnimation(XunchaJianchaActivity.this, fromYDelta));
                    mPopupWindow.getContentView().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mPopupWindow.dismiss();
                        }
                    },AnimationUtil.ANIMATION_OUT_TIME);
                }
            }
        });
        initTabFragment();
    }

    @Override
    public void initData() {
        super.initData();
    }

    private void initTabFragment() {

        //初始化Fragment
        WoyaoCheckFragment fragment1 = new WoyaoCheckFragment();
        WoyaoFuchaFragment fragment2 = new WoyaoFuchaFragment();
        ObtainStatementFragment fragment3 = new ObtainStatementFragment();
        HistoryRecordFragment fragment4 = new HistoryRecordFragment();

        //将Fragment装进列表中
        mFragment = new ArrayList<>();
        mFragment.add(fragment1);
        mFragment.add(fragment2);
        mFragment.add(fragment3);
        mFragment.add(fragment4);

        //将名称添加daoTab列表
        mTitle = new ArrayList<>();
        mTitle.add("我要检查");
        mTitle.add("我要复查");
        mTitle.add("调查取证");
        mTitle.add("历史记录");

        //为TabLayout添加Tab名称
        mTabLayout.addTab(mTabLayout.newTab().setText(mTitle.get(0)));
        mTabLayout.addTab(mTabLayout.newTab().setText(mTitle.get(1)));
        mTabLayout.addTab(mTabLayout.newTab().setText(mTitle.get(2)));
        mTabLayout.addTab(mTabLayout.newTab().setText(mTitle.get(3)));

        mAdapter = new TopPagerAdapter(this.getSupportFragmentManager(), mFragment, mTitle);

        //ViewPager加载Adapter
        mViewPager.setAdapter(mAdapter);

        //TabLayout加载ViewPager
        mTabLayout.setupWithViewPager(mViewPager);
    }


    /**
     * http://blog.csdn.net/lnn368/article/details/51185732
     * 展示PopupWindow
     */
    private void showPopupWindow(){
        final View contentView= LayoutInflater.from(this).inflate(R.layout.selectlist,null);
        TextView t1= (TextView) contentView.findViewById(R.id.text1);
        mPopupWindow=new PopupWindow(contentView, LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
        //将这两个属性设置为false，使点击popupwindow外面其他地方不会消失
        mPopupWindow.setOutsideTouchable(false);
        mPopupWindow.setFocusable(false);
        mGrayLayout.setVisibility(View.VISIBLE);
        //获取popupwindow高度确定动画开始位置
        int contentHeight= com.henghao.hhworkpresent.utils.ViewUtils.getViewMeasuredHeight(contentView);
        mPopupWindow.showAsDropDown(mRightImageView);
        fromYDelta=-contentHeight - 50;
        mPopupWindow.getContentView().startAnimation(AnimationUtil.createInAnimation(this, fromYDelta));

        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                isPopWindowShowing=false;
                mGrayLayout.setVisibility(View.GONE);
            }
        });

        t1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(XunchaJianchaActivity.this,AddJianchaTaskActivity.class);
                startActivity(intent);
                mPopupWindow.dismiss();
            }
        });
        isPopWindowShowing=true;
    }
}
