package com.henghao.hhworkpresent.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.henghao.hhworkpresent.FragmentSupport;
import com.henghao.hhworkpresent.R;
import com.henghao.hhworkpresent.adapter.MeetingRecordListAdapter;
import com.henghao.hhworkpresent.entity.MeetingUploadEntity;
import com.henghao.hhworkpresent.utils.SqliteDBUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.List;

/**
 * 上传后的会议记录
 * Created by bryanrady on 2017/10/16.
 */

public class MeetingRecordListFragment extends FragmentSupport {

    @ViewInject(R.id.meeting_listview)
    private ListView meeting_listview;

    private MeetingRecordListAdapter meetingRecordListAdapter;

    private List<MeetingUploadEntity> meetingUploadEntityList;

    private Handler mHandler = new Handler(){};

    private SqliteDBUtils sqliteDBUtils;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        mActivityFragmentView.getNavitionBarView().setVisibility(View.GONE);
        this.mActivityFragmentView.viewMain(R.layout.fragment_push_message_meeting_list);
        this.mActivityFragmentView.viewEmpty(R.layout.activity_empty);
        this.mActivityFragmentView.viewEmptyGone();
        this.mActivityFragmentView.viewLoading(View.GONE);
        ViewUtils.inject(this, this.mActivityFragmentView);
        initWidget();
        initData();
        return this.mActivityFragmentView;
    }

    public void initWidget(){

    }

    public void initData(){
    }
}
