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
import com.henghao.hhworkpresent.activity.ComplaintFeedbackActivity;
import com.henghao.hhworkpresent.activity.LawIndexActivity;
import com.henghao.hhworkpresent.activity.LawMonitorActivity;
import com.henghao.hhworkpresent.activity.StaffAnalysisActivity;
import com.henghao.hhworkpresent.activity.ZFTotalAnalysisActivity;
import com.henghao.hhworkpresent.activity.ZFTotalQueryActivity;
import com.henghao.hhworkpresent.entity.AppGridEntity;
import com.lidroid.xutils.BitmapUtils;

import java.util.List;

/**
 * Created by bryanrady on 2017/3/8.
 */

public class XingzhenZFSecAdapter extends ArrayAdapter<AppGridEntity> {

    private final LayoutInflater inflater;

    private final BitmapUtils mBitmapUtils;

    private final ActivityFragmentSupport mActivityFragmentSupport;

    private final List<AppGridEntity> mList;

    public XingzhenZFSecAdapter(ActivityFragmentSupport activityFragment, List<AppGridEntity> list) {
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
        XingzhenZFSecAdapter.HodlerView mHodlerView = null;
        if (convertView == null) {
            mHodlerView = new XingzhenZFSecAdapter.HodlerView();
            convertView = this.inflater.inflate(R.layout.item_gridview_textimage, null);
            mHodlerView.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
            mHodlerView.image_title = (ImageView) convertView.findViewById(R.id.image_title);
            convertView.setTag(mHodlerView);
        } else {
            mHodlerView = (XingzhenZFSecAdapter.HodlerView) convertView.getTag();
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
    public void viewOnClick(XingzhenZFSecAdapter.HodlerView mHodlerView, View convertView, final int position){
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                switch (position){
                    case 0:
                        //执法监控
                        intent.setClass(mActivityFragmentSupport, LawMonitorActivity.class);
                        mActivityFragmentSupport.startActivity(intent);
                        break;
                    case 1:
                        //执法指标
                        intent.setClass(mActivityFragmentSupport, LawIndexActivity.class);
                       mActivityFragmentSupport.startActivity(intent);
                        break;
                    case 2:
                        //员工分析
                        intent.setClass(mActivityFragmentSupport, StaffAnalysisActivity.class);
                        mActivityFragmentSupport.startActivity(intent);
                        break;
                    case 3:
                        //综合查询
                        intent.setClass(mActivityFragmentSupport, ZFTotalQueryActivity.class);
                        mActivityFragmentSupport.startActivity(intent);
                        break;
                    case 4:
                        //统计分析
                        intent.setClass(mActivityFragmentSupport, ZFTotalAnalysisActivity.class);
                        mActivityFragmentSupport.startActivity(intent);
                        break;
                    case 5:
                        //投诉反馈
                        intent.setClass(mActivityFragmentSupport, ComplaintFeedbackActivity.class);
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
