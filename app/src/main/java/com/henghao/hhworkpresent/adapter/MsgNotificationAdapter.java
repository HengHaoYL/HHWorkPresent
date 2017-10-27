package com.henghao.hhworkpresent.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.henghao.hhworkpresent.ActivityFragmentSupport;
import com.henghao.hhworkpresent.R;
import com.henghao.hhworkpresent.entity.MsgEntity;

import java.util.List;

/**
 * 首页消息适配器
 * Created by bryanrady on 2017/3/2.
 */

public class MsgNotificationAdapter extends ArrayAdapter<MsgEntity> {

    private final LayoutInflater inflater;

    private final ActivityFragmentSupport mActivityFragmentSupport;

    public MsgNotificationAdapter (ActivityFragmentSupport activityFragment, List<MsgEntity> mList){
        super(activityFragment, R.layout.item_msgnotification, mList);
        this.mActivityFragmentSupport = activityFragment;
        this.inflater = LayoutInflater.from(activityFragment);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        HodlerView mHodlerView = null;
        if (convertView == null) {
            mHodlerView = new HodlerView();
            convertView = this.inflater.inflate(R.layout.item_msgnotification, null);
            mHodlerView.imageView = (ImageView) convertView.findViewById(R.id.msg_imageview);
            mHodlerView.tv_title = (TextView) convertView.findViewById(R.id.msg_tv_title);
            mHodlerView.tv_time = (TextView) convertView.findViewById(R.id.msg_tv_time);
            mHodlerView.tv_name = (TextView) convertView.findViewById(R.id.msg_tv_name);
            convertView.setTag(mHodlerView);
        } else {
            mHodlerView = (HodlerView) convertView.getTag();
        }
        mHodlerView.tv_title.setText(getItem(position).getTitle());
        mHodlerView.tv_time.setText(getItem(position).getSendtime());
        mHodlerView.tv_name.setText(getItem(position).getSendusername());
        return convertView;
    }

    private class HodlerView {

        ImageView imageView;

        TextView tv_title;

        TextView tv_time;

        TextView tv_name;
    }
}
