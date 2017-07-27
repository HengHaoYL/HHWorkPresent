package com.henghao.hhworkpresent.adapter;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.henghao.hhworkpresent.ActivityFragmentSupport;
import com.henghao.hhworkpresent.Constant;
import com.henghao.hhworkpresent.R;
import com.henghao.hhworkpresent.entity.ZaotuiTongjiaEntity;
import com.lidroid.xutils.BitmapUtils;

import java.util.List;

/**
 * Created by bryanrady on 2017/7/27.
 */

public class ZaotuiTongjiListAdapter extends ArrayAdapter<ZaotuiTongjiaEntity> {

    private final LayoutInflater inflater;

    private final BitmapUtils mBitmapUtils;

    public Handler mHandler = new Handler(){};

    private final ActivityFragmentSupport mActivityFragmentSupport;

    public ZaotuiTongjiListAdapter(ActivityFragmentSupport activityFragment, List<ZaotuiTongjiaEntity> mList){
        super(activityFragment, R.layout.listview_weidaka_item, mList);
        this.mActivityFragmentSupport = activityFragment;
        this.inflater = LayoutInflater.from(activityFragment);
        this.mBitmapUtils = new BitmapUtils(activityFragment, Constant.CACHE_DIR_PATH);
        this.mBitmapUtils.configDefaultLoadFailedImage(R.drawable.img_loading_fail_big);
        this.mBitmapUtils.configDefaultLoadingImage(R.drawable.img_loading_default_big);

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
        mHodlerView.tv_state.setText(getItem(position).getClockOutTime());

        return convertView;
    }

    private class HodlerView {

        TextView tv_name;

        TextView tv_dept;

        TextView tv_state;
    }
}
