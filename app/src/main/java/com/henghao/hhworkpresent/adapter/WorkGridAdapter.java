package com.henghao.hhworkpresent.adapter;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.henghao.hhworkpresent.ActivityFragmentSupport;
import com.henghao.hhworkpresent.Constant;
import com.henghao.hhworkpresent.R;
import com.henghao.hhworkpresent.activity.KaoQingActivity;
import com.henghao.hhworkpresent.activity.WaiqingQiandaoActivity;
import com.henghao.hhworkpresent.entity.AppGridEntity;
import com.lidroid.xutils.BitmapUtils;

import java.util.List;

/**
 * Created by bryanrady on 2017/2/28.
 */

public class WorkGridAdapter extends ArrayAdapter<AppGridEntity> {

    private final LayoutInflater inflater;

    private final BitmapUtils mBitmapUtils;

    private final ActivityFragmentSupport mActivityFragmentSupport;

    private final List<AppGridEntity> mList;

    public WorkGridAdapter(ActivityFragmentSupport activityFragment, List<AppGridEntity> mList2){
        super(activityFragment, R.layout.item_work_fragment_adapter, mList2);
        this.mActivityFragmentSupport = activityFragment;
        this.mList = mList2;
        this.inflater = LayoutInflater.from(activityFragment);
        this.mBitmapUtils = new BitmapUtils(activityFragment, Constant.CACHE_DIR_PATH);
        this.mBitmapUtils.configDefaultLoadFailedImage(R.drawable.img_loading_fail_big);
        this.mBitmapUtils.configDefaultLoadingImage(R.drawable.img_loading_default_big);
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
                        //外勤签到
                        intent.setClass(mActivityFragmentSupport, WaiqingQiandaoActivity.class);
                        mActivityFragmentSupport.startActivity(intent);
                        break;
                    case 1:
                        //考勤
                        intent.setClass(mActivityFragmentSupport, KaoQingActivity.class);
                        mActivityFragmentSupport.startActivity(intent);
                        break;
                    case 2:
                        //邮箱
                        PackageManager packageManager = mActivityFragmentSupport.getPackageManager();
                        try {
                            //启动网易邮箱    com.netease.mail
                            //com.microsoft.office.outlook  outlook邮箱  pm list package
                            intent =packageManager.getLaunchIntentForPackage("com.microsoft.office.outlook");
                            mActivityFragmentSupport.startActivity(intent);
                        } catch (Exception e) {
                            //如果系统找不到此应用，就提示下面的信息
                            mActivityFragmentSupport.msg("你的手机里没有Outlook电子邮箱，" +
                                    "请安装！");
                        }
                        break;
                    /*case 3:
                        //通知公告
                        intent.setClass(mActivityFragmentSupport, GongGaoActivity.class);
                        mActivityFragmentSupport.startActivity(intent);
                        break;*/
                    case 3:
                        //行政执法
                         packageManager = mActivityFragmentSupport.getPackageManager();
                        try {
                            //启动网易邮箱    com.netease.mail
                            //com.microsoft.office.outlook  outlook邮箱  pm list package
                            intent = packageManager.getLaunchIntentForPackage("com.example.safetysupervision");
                            mActivityFragmentSupport.startActivity(intent);
                        } catch (Exception e) {
                            //如果系统找不到此应用，就提示下面的信息
                            mActivityFragmentSupport.msg("你的手机里没有安监执法应用，请安装！");
                        }
                        break;
                   /* case 5:
                        //考勤
                        intent.setClass(mActivityFragmentSupport, TestActivity.class);
                        mActivityFragmentSupport.startActivity(intent);
                        break;*/
                }
            }
        });

    }


    class HodlerView {

        TextView tv_title;

        ImageView image_title;
    }
}
