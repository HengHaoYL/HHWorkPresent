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
import com.henghao.hhworkpresent.activity.ItemApplicationActivity;
import com.henghao.hhworkpresent.activity.LawFeedbackActivity;
import com.henghao.hhworkpresent.activity.ProgressTrackActivity;
import com.henghao.hhworkpresent.activity.TaskOrderActivity;
import com.henghao.hhworkpresent.activity.TaskScoreActivity;
import com.henghao.hhworkpresent.activity.TaskWarningActivity;
import com.henghao.hhworkpresent.entity.AppGridEntity;
import com.lidroid.xutils.BitmapUtils;

import java.util.List;

/**
 * Created by bryanrady on 2017/3/8.
 */

public class BangongRWSecAdapter extends ArrayAdapter<AppGridEntity> {

    private final LayoutInflater inflater;

    private final BitmapUtils mBitmapUtils;

    private final ActivityFragmentSupport mActivityFragmentSupport;

    private final List<AppGridEntity> mList;

    public BangongRWSecAdapter(ActivityFragmentSupport activityFragment, List<AppGridEntity> list) {
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
        BangongRWSecAdapter.HodlerView mHodlerView = null;
        if (convertView == null) {
            mHodlerView = new BangongRWSecAdapter.HodlerView();
            convertView = this.inflater.inflate(R.layout.item_gridview_textimage, null);
            mHodlerView.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
            mHodlerView.image_title = (ImageView) convertView.findViewById(R.id.image_title);
            convertView.setTag(mHodlerView);
        } else {
            mHodlerView = (BangongRWSecAdapter.HodlerView) convertView.getTag();
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
    public void viewOnClick(BangongRWSecAdapter.HodlerView mHodlerView, View convertView, final int position){
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                switch (position){
                    case 0:
                        //任务派单
                        intent.setClass(mActivityFragmentSupport, TaskOrderActivity.class);
                        mActivityFragmentSupport.startActivity(intent);
                        break;
                    case 1:
                        //执法反馈
                        intent.setClass(mActivityFragmentSupport, LawFeedbackActivity.class);
                        mActivityFragmentSupport.startActivity(intent);
                        break;
                    case 2:
                        //结项申请
                        intent.setClass(mActivityFragmentSupport, ItemApplicationActivity.class);
                        mActivityFragmentSupport.startActivity(intent);
                        break;
                    case 3:
                        //任务预警
                        intent.setClass(mActivityFragmentSupport, TaskWarningActivity.class);
                        mActivityFragmentSupport.startActivity(intent);
                        break;
                    case 4:
                        //任务评分
                        intent.setClass(mActivityFragmentSupport, TaskScoreActivity.class);
                        mActivityFragmentSupport.startActivity(intent);
                        break;
                    case 5:
                        //进度跟踪
                        intent.setClass(mActivityFragmentSupport, ProgressTrackActivity.class);
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
