package com.henghao.hhworkpresent.adapter;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.henghao.hhworkpresent.ActivityFragmentSupport;
import com.henghao.hhworkpresent.R;
import com.henghao.hhworkpresent.entity.SaveCheckTaskEntity;

import java.util.List;

/**
 * 被选中的材料文书描述适配器
 * Created by ASUS on 2017/9/8.
 */

public class DescriptListAdapter extends ArrayAdapter<SaveCheckTaskEntity.JianchaMaterialEntityListBean> {

    private final LayoutInflater inflater;

    public Handler mHandler = new Handler(){};

    private ActivityFragmentSupport mActivityFragmentSupport;

    public DescriptListAdapter(ActivityFragmentSupport activityFragment, List<SaveCheckTaskEntity.JianchaMaterialEntityListBean> mList){
        super(activityFragment, R.layout.listview_selected_descript_item, mList);
        this.mActivityFragmentSupport = activityFragment;
        this.inflater = LayoutInflater.from(activityFragment);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        HodlerView mHodlerView = null;
        if (convertView == null) {
            mHodlerView = new HodlerView();
            convertView = this.inflater.inflate(R.layout.listview_selected_descript_item, null);
            mHodlerView.tv_check_title = (TextView) convertView.findViewById(R.id.tv_check_title);
            mHodlerView.tv_check_content = (TextView) convertView.findViewById(R.id.tv_check_content);
            mHodlerView.image_delete_check = (ImageView) convertView.findViewById(R.id.image_delete_check);
            convertView.setTag(mHodlerView);
        } else {
            mHodlerView = (HodlerView) convertView.getTag();
        }

        mHodlerView.tv_check_title.setText(getItem(position).getTitle());
        mHodlerView.tv_check_content.setText(getItem(position).getDescript());
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

        TextView tv_check_title;

        TextView tv_check_content;

        ImageView image_delete_check;
    }
}
