package com.henghao.hhworkpresent.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.henghao.hhworkpresent.ActivityFragmentSupport;
import com.henghao.hhworkpresent.ProtocolUrl;
import com.henghao.hhworkpresent.R;
import com.henghao.hhworkpresent.entity.TrajectoryEntity;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bryanrady on 2017/7/18.
 */

public class WorkTrajectoryListAdapter extends ArrayAdapter<TrajectoryEntity> {

    private final LayoutInflater inflater;

    private final ActivityFragmentSupport mActivityFragmentSupport;

    public WorkTrajectoryListAdapter(ActivityFragmentSupport activityFragment, List<TrajectoryEntity> mList){
        super(activityFragment, R.layout.listview_item_trajectory, mList);
        this.mActivityFragmentSupport = activityFragment;
        this.inflater = LayoutInflater.from(activityFragment);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        WorkTrajectoryListAdapter.HodlerView mHodlerView = null;
        if (convertView == null) {
            mHodlerView = new WorkTrajectoryListAdapter.HodlerView();
            convertView = this.inflater.inflate(R.layout.listview_item_trajectory, null);
            mHodlerView.tv_tarjectory_event = (TextView) convertView.findViewById(R.id.tarjectory_event);
            mHodlerView.tv_tarjectory_time = (TextView) convertView.findViewById(R.id.tarjectory_time);
            mHodlerView.tv_tarjectory_place = (TextView) convertView.findViewById(R.id.tarjectory_place);
            mHodlerView.trajectory_picture_gridView = (GridView) convertView.findViewById(R.id.trajectory_picture_gridView);
            convertView.setTag(mHodlerView);
        } else {
            mHodlerView = (WorkTrajectoryListAdapter.HodlerView) convertView.getTag();
        }
        mHodlerView.tv_tarjectory_event.setText(getItem(position).getEventName());
        mHodlerView.tv_tarjectory_time.setText(getItem(position).getEventTime());
        mHodlerView.tv_tarjectory_place.setText(getItem(position).getEventAddress());
        String filePath = getItem(position).getEventImagePath();
        String[] arr = filePath.split(",");
        List<String> imageList = new ArrayList<>();
        for(int i=0;i<arr.length;i++){
            imageList.add(arr[i]);
        }
        GridAdapter gridAdapter = new GridAdapter(mActivityFragmentSupport,imageList);
        mHodlerView.trajectory_picture_gridView.setAdapter(gridAdapter);
        return convertView;
    }

    private class HodlerView {
        TextView tv_tarjectory_event;

        TextView tv_tarjectory_time;

        TextView tv_tarjectory_place;

        GridView trajectory_picture_gridView;

    }

    class GridAdapter extends ArrayAdapter<String> {

        List<String> eventImageNameList;

        ImageLoader imageLoader;

        DisplayImageOptions options;

        ActivityFragmentSupport mActivityFragmentSupport;

        LayoutInflater inflater;

        public GridAdapter(ActivityFragmentSupport mActivityFragmentSupport,List<String> mEventImageNameList){
            super(mActivityFragmentSupport,R.layout.trajectory_picture_item_gridview, mEventImageNameList);
            this.mActivityFragmentSupport = mActivityFragmentSupport;
            this.eventImageNameList = mEventImageNameList;
            this.inflater = LayoutInflater.from(mActivityFragmentSupport);
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null ;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = inflater.inflate(R.layout.trajectory_picture_item_gridview, null);
                holder.image = (ImageView) convertView.findViewById(R.id.imageView);
                holder.image.setScaleType(ImageView.ScaleType.CENTER_CROP);
                convertView.setTag(holder);
            }
            else{
                holder = (ViewHolder) convertView.getTag();
            }

            options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.icon_logo) // 设置图片下载期间显示的图片
                .showImageForEmptyUri(R.drawable.ic_launcher) // 设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(R.drawable.ic_launcher) // 设置图片加载或解码过程中发生错误显示的图片
                .cacheInMemory(true) // 设置下载的图片是否缓存在内存中
                .cacheOnDisk(true) // 设置下载的图片是否缓存在SD卡中
                .build(); // 构建完成
            imageLoader =ImageLoader.getInstance();
            String[] imageUrl = eventImageNameList.toArray(new String[eventImageNameList.size()]);
            imageLoader.displayImage(ProtocolUrl.ROOT_URL + ProtocolUrl.APP_DOWNLOAD_WORK_TRAJECTORY_IMAGE + imageUrl[position], holder.image, options);
            //用for循环会显示最后一张url的图片重复显示
            /*for(String imageUri : eventImageNameList){
                imageUri = ProtocolUrl.ROOT_URL + "/"+ProtocolUrl.APP_DOWNLOAD_WORK_TRAJECTORY_IMAGE + imageUri;
                Log.d("wangqingbin","imageUri=="+imageUri);
                imageLoader.displayImage(imageUri, holder.image, options);
            }*/
            return convertView;
        }
    }

    public class ViewHolder {
        public ImageView image;
    }
}
