package com.henghao.hhworkpresent.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.henghao.hhworkpresent.ActivityFragmentSupport;
import com.henghao.hhworkpresent.Constant;
import com.henghao.hhworkpresent.R;
import com.henghao.hhworkpresent.entity.KaoqingEntity;
import com.lidroid.xutils.BitmapUtils;

import java.util.List;

/**
 * Created by bryanrady on 2017/3/17.
 *
 * 考勤界面 缺卡记录适配器
 */

public class QuekaListAdapter extends ArrayAdapter<KaoqingEntity> {

    private final LayoutInflater inflater;

    private final BitmapUtils mBitmapUtils;

    private final ActivityFragmentSupport mActivityFragmentSupport;

    public QuekaListAdapter(ActivityFragmentSupport activityFragment, List<KaoqingEntity> mList){
        super(activityFragment, R.layout.listview_item_queka, mList);
        this.mActivityFragmentSupport = activityFragment;
        this.inflater = LayoutInflater.from(activityFragment);
        this.mBitmapUtils = new BitmapUtils(activityFragment, Constant.CACHE_DIR_PATH);
        this.mBitmapUtils.configDefaultLoadFailedImage(R.drawable.img_loading_fail_big);
        this.mBitmapUtils.configDefaultLoadingImage(R.drawable.img_loading_default_big);

    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        QuekaListAdapter.HodlerView mHodlerView = null;
        if (convertView == null) {
            mHodlerView = new QuekaListAdapter.HodlerView();
            convertView = this.inflater.inflate(R.layout.listview_item_queka, null);
            mHodlerView.tv_queka_datetime = (TextView) convertView.findViewById(R.id.tv_queka_datetime);
            mHodlerView.tv_queka_week = (TextView) convertView.findViewById(R.id.tv_queka_week);
            mHodlerView.tv_queka_time = (TextView) convertView.findViewById(R.id.tv_queka_time);
            convertView.setTag(mHodlerView);
        } else {
            mHodlerView = (QuekaListAdapter.HodlerView) convertView.getTag();
        }
        mHodlerView.tv_queka_datetime.setText(getItem(position).getCurrentDate());
        mHodlerView.tv_queka_week.setText(getItem(position).getWeek());
        mHodlerView.tv_queka_time.setText(getItem(position).getClockInTime());
        return convertView;
    }

    private class HodlerView {

        TextView tv_queka_datetime;

        TextView tv_queka_week;

        TextView tv_queka_time;

    }
}
