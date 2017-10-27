package com.henghao.hhworkpresent.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.henghao.hhworkpresent.ActivityFragmentSupport;
import com.henghao.hhworkpresent.R;
import com.henghao.hhworkpresent.activity.BuqianActivity;
import com.henghao.hhworkpresent.activity.QingjiaActivity;
import com.henghao.hhworkpresent.activity.WaichuActivity;
import com.henghao.hhworkpresent.entity.AppGridEntity;

import java.util.List;

/**
 * Created by bryanrady on 2017/4/5.
 * 请假、申请、补签适配器
 */

public class QingjiaGridAdapter extends ArrayAdapter<AppGridEntity> {

    private final LayoutInflater inflater;

    private final ActivityFragmentSupport mActivityFragmentSupport;

    public QingjiaGridAdapter(ActivityFragmentSupport activityFragment, List<AppGridEntity> mList){
        super(activityFragment, R.layout.item_work_fragment_adapter, mList);
        this.mActivityFragmentSupport = activityFragment;
        this.inflater = LayoutInflater.from(activityFragment);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        HodlerView mHodlerView = null;
        if (convertView == null) {
            mHodlerView = new HodlerView();
            convertView = this.inflater.inflate(R.layout.item_gridview_textimage, null);
            mHodlerView.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
            mHodlerView.image_title = (ImageView) convertView.findViewById(R.id.image_title);
            convertView.setTag(mHodlerView);
        } else {
            mHodlerView = (HodlerView) convertView.getTag();
        }
        mHodlerView.image_title.setImageResource(getItem(position).getImageId());
        mHodlerView.tv_title.setVisibility(View.VISIBLE);
        mHodlerView.tv_title.setText(getItem(position).getName());
        viewOnClick(mHodlerView, convertView, position);
        return convertView;
    }

    /**
     * 点击事件
     *
     * @param mHodlerView
     * @param convertView
     * @param position
     */
    private void viewOnClick(HodlerView mHodlerView, View convertView, final int position) {
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                switch (position) {
                    case 0:
                        //请假
                        intent.setClass(mActivityFragmentSupport, QingjiaActivity.class);
                        mActivityFragmentSupport.startActivity(intent);
                        break;
                    case 1:
                        //外出
                        intent.setClass(mActivityFragmentSupport, WaichuActivity.class);
                        mActivityFragmentSupport.startActivity(intent);
                        break;
                    case 2:
                        //补签
                        intent.setClass(mActivityFragmentSupport, BuqianActivity.class);
                        mActivityFragmentSupport.startActivity(intent);
                        break;
                }
            }
        });

    }


    class HodlerView {

        TextView tv_title;

        ImageView image_title;
    }
}
