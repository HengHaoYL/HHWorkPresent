package com.henghao.hhworkpresent.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.henghao.hhworkpresent.ActivityFragmentSupport;
import com.henghao.hhworkpresent.Constant;
import com.henghao.hhworkpresent.R;
import com.henghao.hhworkpresent.activity.BackStationActivity;
import com.henghao.hhworkpresent.activity.CarApprovalActivity;
import com.henghao.hhworkpresent.activity.MomentMonitorActivity;
import com.henghao.hhworkpresent.activity.OperationStatisticsActivity;
import com.henghao.hhworkpresent.activity.UseCarApplyActivity;
import com.henghao.hhworkpresent.activity.VehicleMaintenanceActivity;
import com.henghao.hhworkpresent.entity.AppGridEntity;
import com.lidroid.xutils.BitmapUtils;

import java.util.List;

/**
 * Created by bryanrady on 2017/3/8.
 */

public class CheliangSecAdapter extends ArrayAdapter<AppGridEntity> {

    private final LayoutInflater inflater;

    private final BitmapUtils mBitmapUtils;

    private final ActivityFragmentSupport mActivityFragmentSupport;

    private final List<AppGridEntity> mList;

    public CheliangSecAdapter(ActivityFragmentSupport activityFragment, List<AppGridEntity> list) {
        super(activityFragment, R.layout.item_gridview_textimage, list);
        this.mActivityFragmentSupport = activityFragment;
        this.mList = list;
        this.inflater = LayoutInflater.from(activityFragment);
        this.mBitmapUtils = new BitmapUtils(activityFragment, Constant.CACHE_DIR_PATH);
        this.mBitmapUtils.configDefaultLoadFailedImage(R.drawable.img_loading_fail_big);
        this.mBitmapUtils.configDefaultLoadingImage(R.drawable.img_loading_default_big);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CheliangSecAdapter.HodlerView mHodlerView = null;
        if (convertView == null) {
            mHodlerView = new CheliangSecAdapter.HodlerView();
            convertView = this.inflater.inflate(R.layout.item_gridview_textimage, null);
            mHodlerView.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
            mHodlerView.image_title = (ImageView) convertView.findViewById(R.id.image_title);
            convertView.setTag(mHodlerView);
        } else {
            mHodlerView = (CheliangSecAdapter.HodlerView) convertView.getTag();
        }
        mHodlerView.image_title.setImageResource(getItem(position).getImageId());
        mHodlerView.tv_title.setVisibility(View.VISIBLE);
        mHodlerView.tv_title.setText(getItem(position).getName());
        viewOnClick(mHodlerView, convertView, position);
        return convertView;
    }


    /**
     * 点击
     *
     * @param mHodlerView
     * @param convertView
     * @param position
     */
    public void viewOnClick(CheliangSecAdapter.HodlerView mHodlerView, View convertView, final int position){
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                switch (position){
                    case 0:
                        //用车申请
                        intent.setClass(mActivityFragmentSupport, UseCarApplyActivity.class);
                        mActivityFragmentSupport.startActivity(intent);
                        break;
                    case 1:
                        //派车审批
                        intent.setClass(mActivityFragmentSupport, CarApprovalActivity.class);
                        mActivityFragmentSupport.startActivity(intent);
                        break;
                    case 2:
                        //实时监控
                        intent.setClass(mActivityFragmentSupport, MomentMonitorActivity.class);
                        mActivityFragmentSupport.startActivity(intent);
                        break;
                    case 3:
                        //回站收车
                        intent.setClass(mActivityFragmentSupport, BackStationActivity.class);
                        mActivityFragmentSupport.startActivity(intent);
                        break;
                    case 4:
                        //运营统计
                        intent.setClass(mActivityFragmentSupport, OperationStatisticsActivity.class);
                        mActivityFragmentSupport.startActivity(intent);
                        break;
                    case 5:
                        //车辆保养
                        intent.setClass(mActivityFragmentSupport, VehicleMaintenanceActivity.class);
                        mActivityFragmentSupport.startActivity(intent);
                        break;
                }
            }
        });

    }

    private class HodlerView {

        TextView tv_title;

        ImageView image_title;
    }

    @Override
    public int getCount() {
        return (this.mList.size());
    }
}
