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
 * Created by bryanrady on 2017/3/28.
 * 缺卡记录适配器
 */

public class ZaotuiListAdapter extends ArrayAdapter<KaoqingEntity> {

    private final LayoutInflater inflater;

    private final BitmapUtils mBitmapUtils;

    private final ActivityFragmentSupport mActivityFragmentSupport;

    public ZaotuiListAdapter(ActivityFragmentSupport activityFragment, List<KaoqingEntity> mList){
        super(activityFragment, R.layout.listview_item_zaotui, mList);
        this.mActivityFragmentSupport = activityFragment;
        this.inflater = LayoutInflater.from(activityFragment);
        this.mBitmapUtils = new BitmapUtils(activityFragment, Constant.CACHE_DIR_PATH);
        this.mBitmapUtils.configDefaultLoadFailedImage(R.drawable.img_loading_fail_big);
        this.mBitmapUtils.configDefaultLoadingImage(R.drawable.img_loading_default_big);

    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ZaotuiListAdapter.HodlerView mHodlerView = null;
        if (convertView == null) {
            mHodlerView = new ZaotuiListAdapter.HodlerView();
            convertView = this.inflater.inflate(R.layout.listview_item_zaotui, null);
            mHodlerView.tv_zaotui_datetime = (TextView) convertView.findViewById(R.id.tv_zaotui_datetime);
            mHodlerView.tv_zaotui_work = (TextView) convertView.findViewById(R.id.tv_zaotui_work);
            mHodlerView.tv_zaotui_time = (TextView) convertView.findViewById(R.id.tv_zaotui_time);
            convertView.setTag(mHodlerView);
        } else {
            mHodlerView = (ZaotuiListAdapter.HodlerView) convertView.getTag();
        }
        mHodlerView.tv_zaotui_datetime.setText(getItem(position).getCurrentDate());
        mHodlerView.tv_zaotui_work.setText(getItem(position).getWorkDay());
        mHodlerView.tv_zaotui_time.setText(getItem(position).getClockOutTime());
        return convertView;
    }

    private class HodlerView {

        TextView tv_zaotui_datetime;

        TextView tv_zaotui_work;

        TextView tv_zaotui_time;

    }
}
