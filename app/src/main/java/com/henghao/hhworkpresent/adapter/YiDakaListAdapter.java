package com.henghao.hhworkpresent.adapter;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.henghao.hhworkpresent.ActivityFragmentSupport;
import com.henghao.hhworkpresent.R;
import com.henghao.hhworkpresent.entity.YiQiandanEntity;

import java.util.List;

/**
 * Created by bryanrady on 2017/7/27.
 */

public class YiDakaListAdapter extends ArrayAdapter<YiQiandanEntity> {

    private final LayoutInflater inflater;

    public Handler mHandler = new Handler(){};

    private final ActivityFragmentSupport mActivityFragmentSupport;

    public YiDakaListAdapter(ActivityFragmentSupport activityFragment, List<YiQiandanEntity> mList){
        super(activityFragment, R.layout.listview_weidaka_item, mList);
        this.mActivityFragmentSupport = activityFragment;
        this.inflater = LayoutInflater.from(activityFragment);
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        HodlerView mHodlerView = null;
        if (convertView == null) {
            mHodlerView = new HodlerView();
            convertView = this.inflater.inflate(R.layout.listview_weidaka_item, null);
            mHodlerView.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            mHodlerView.tv_dept = (TextView) convertView.findViewById(R.id.tv_dept);
            mHodlerView.tv_state = (TextView) convertView.findViewById(R.id.tv_state);
            convertView.setTag(mHodlerView);
        } else {
            mHodlerView = (HodlerView) convertView.getTag();
        }

        mHodlerView.tv_name.setText(getItem(position).getName());
        mHodlerView.tv_dept.setText(getItem(position).getDept());
        mHodlerView.tv_state.setText(getItem(position).getSignedIn());

        return convertView;
    }


    private class HodlerView {

        TextView tv_name;

        TextView tv_dept;

        TextView tv_state;
    }
}
