package com.henghao.hhworkpresent.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.benefit.buy.library.views.xlistview.XListView;
import com.henghao.hhworkpresent.FragmentSupport;
import com.henghao.hhworkpresent.R;
import com.henghao.hhworkpresent.adapter.MsgNotificationAdapter;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bryanrady on 2017/2/28.
 */

public class MsgFragment extends FragmentSupport {

    @ViewInject(R.id.fragment_msg_listview)
    private XListView mXlistView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        this.mActivityFragmentView.viewMain(R.layout.fragment_msg);
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
        initwithContent();
        initWithRightBar();
        mRightImageView.setVisibility(View.VISIBLE);
        mRightImageView.setImageResource(R.drawable.item_searching);
    }

    private void initwithContent() {
        // TODO Auto-generated method stub
        initWithCenterBar();
        this.mCenterTextView.setVisibility(View.VISIBLE);
        this.mCenterTextView.setText("我的消息");
    }

    public void initData(){
        List<String> mList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            mList.add("测试");
        }
        MsgNotificationAdapter mAdapter = new MsgNotificationAdapter(this.mActivity, mList);
        mXlistView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }
}
