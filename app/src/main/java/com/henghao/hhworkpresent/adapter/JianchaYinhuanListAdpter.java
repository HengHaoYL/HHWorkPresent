package com.henghao.hhworkpresent.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.henghao.hhworkpresent.ActivityFragmentSupport;
import com.henghao.hhworkpresent.Constant;
import com.henghao.hhworkpresent.R;
import com.henghao.hhworkpresent.activity.AddYinhuanActivity;
import com.henghao.hhworkpresent.entity.JianchaMaterialEntity;
import com.henghao.hhworkpresent.entity.JianchaYinhuanEntity;
import com.lidroid.xutils.BitmapUtils;

import java.util.List;

/**
 * 我要检查界面的被选中的检查隐患列表适配器
 * Created by ASUS on 2017/9/14.
 */

public class JianchaYinhuanListAdpter extends ArrayAdapter<JianchaYinhuanEntity> {

    private final LayoutInflater inflater;

    private final BitmapUtils mBitmapUtils;

    public Handler mHandler = new Handler(){};

    private final ActivityFragmentSupport mActivityFragmentSupport;

    public JianchaYinhuanListAdpter(ActivityFragmentSupport activityFragment, List<JianchaYinhuanEntity> mList){
        super(activityFragment, R.layout.listview_jiancha_yinhuan_item, mList);
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
            convertView = this.inflater.inflate(R.layout.listview_jiancha_yinhuan_item, null);
            mHodlerView.image_check_picture = (ImageView) convertView.findViewById(R.id.image_check_picture);
            mHodlerView.tv_check_description = (TextView) convertView.findViewById(R.id.tv_check_description);
            mHodlerView.image_delete_check = (ImageView) convertView.findViewById(R.id.image_delete_check);
            convertView.setTag(mHodlerView);
        } else {
            mHodlerView = (HodlerView) convertView.getTag();
        }

        mHodlerView.tv_check_description.setText(getItem(position).getThreat_description());
        mHodlerView.image_delete_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击删除
                remove(getItem(position));
            }
        });
        return convertView;
    }


    private class HodlerView {

        ImageView image_check_picture;

        TextView tv_check_description;

        ImageView image_delete_check;
    }
}
