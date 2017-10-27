package com.henghao.hhworkpresent.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.henghao.hhworkpresent.ActivityFragmentSupport;
import com.henghao.hhworkpresent.R;
import com.henghao.hhworkpresent.entity.KaoqingEntity;

import java.util.List;

/**
 * Created by bryanrady on 2017/3/28.
 * 旷工记录适配器
 */

public class KuanggongListAdapter extends ArrayAdapter<KaoqingEntity> {

    private final LayoutInflater inflater;

    private final ActivityFragmentSupport mActivityFragmentSupport;

    public KuanggongListAdapter(ActivityFragmentSupport activityFragment, List<KaoqingEntity> mList){
        super(activityFragment, R.layout.listview_item_kuanggong, mList);
        this.mActivityFragmentSupport = activityFragment;
        this.inflater = LayoutInflater.from(activityFragment);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        KuanggongListAdapter.HodlerView mHodlerView = null;
        if (convertView == null) {
            mHodlerView = new KuanggongListAdapter.HodlerView();
            convertView = this.inflater.inflate(R.layout.listview_item_kuanggong, null);
            mHodlerView.tv_kuanggong_datetime = (TextView) convertView.findViewById(R.id.tv_kuanggong_datetime);
            mHodlerView.tv_kuanggong_week = (TextView) convertView.findViewById(R.id.tv_kuanggong_week);
            convertView.setTag(mHodlerView);
        } else {
            mHodlerView = (KuanggongListAdapter.HodlerView) convertView.getTag();
        }
        mHodlerView.tv_kuanggong_datetime.setText(getItem(position).getCurrentDate());
        mHodlerView.tv_kuanggong_week.setText(getItem(position).getWeek());
        mHodlerView.tv_kuanggong_datetime.setText(getItem(position).getCurrentDate());
        return convertView;
    }

    private class HodlerView {

        TextView tv_kuanggong_datetime;

        TextView tv_kuanggong_week;

    }
}
