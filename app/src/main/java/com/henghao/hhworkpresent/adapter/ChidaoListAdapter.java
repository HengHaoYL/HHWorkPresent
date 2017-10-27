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
 * Created by bryanrady on 2017/3/17.
 *
 * 考勤界面 迟到记录适配器
 */

public class ChidaoListAdapter extends ArrayAdapter<KaoqingEntity> {

    private final LayoutInflater inflater;

    private final ActivityFragmentSupport mActivityFragmentSupport;

    public ChidaoListAdapter(ActivityFragmentSupport activityFragment, List<KaoqingEntity> mList){
        super(activityFragment, R.layout.listview_item_chidao, mList);
        this.mActivityFragmentSupport = activityFragment;
        this.inflater = LayoutInflater.from(activityFragment);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ChidaoListAdapter.HodlerView mHodlerView = null;
        if (convertView == null) {
            mHodlerView = new ChidaoListAdapter.HodlerView();
            convertView = this.inflater.inflate(R.layout.listview_item_chidao, null);
            mHodlerView.tv_chidao_datetime = (TextView) convertView.findViewById(R.id.tv_chidao_datetime);
            mHodlerView.tv_chidao_week = (TextView) convertView.findViewById(R.id.tv_chidao_week);
            mHodlerView.tv_chidao_daka_time = (TextView) convertView.findViewById(R.id.tv_chidao_daka_time);
            convertView.setTag(mHodlerView);
        } else {
            mHodlerView = (ChidaoListAdapter.HodlerView) convertView.getTag();
        }
        mHodlerView.tv_chidao_datetime.setText(getItem(position).getCurrentDate());
        mHodlerView.tv_chidao_week.setText(getItem(position).getWeek());
        mHodlerView.tv_chidao_daka_time.setText(getItem(position).getClockInTime());
        return convertView;
    }

    private class HodlerView {

        TextView tv_chidao_datetime;

        TextView tv_chidao_week;

        TextView tv_chidao_daka_time;

    }
}
