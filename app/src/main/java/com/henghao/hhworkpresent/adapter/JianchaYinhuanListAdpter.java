package com.henghao.hhworkpresent.adapter;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.henghao.hhworkpresent.ActivityFragmentSupport;
import com.henghao.hhworkpresent.Constant;
import com.henghao.hhworkpresent.R;
import com.henghao.hhworkpresent.entity.GonggaoEntity;
import com.henghao.hhworkpresent.entity.SaveCheckTaskEntity;
import com.lidroid.xutils.BitmapUtils;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 我要检查界面的被选中的检查隐患列表适配器
 * Created by ASUS on 2017/9/14.
 */

public class JianchaYinhuanListAdpter extends ArrayAdapter<SaveCheckTaskEntity.JianchaMaterialEntityListBean> {

    private final LayoutInflater inflater;

    private final BitmapUtils mBitmapUtils;

    public Handler mHandler = new Handler(){};

    private final ActivityFragmentSupport mActivityFragmentSupport;

    public JianchaYinhuanListAdpter(ActivityFragmentSupport activityFragment, List<SaveCheckTaskEntity.JianchaMaterialEntityListBean> mList){
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

        mHodlerView.tv_check_description.setText(getItem(position).getCheckDescript());
        mHodlerView.image_delete_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //通知后台删除相应的数据
                OkHttpClient okHttpClient = new OkHttpClient();
                Request.Builder builder = new Request.Builder();
                String request_url = "http://172.16.0.81:8080/istration/enforceapp/deletecheckcontent?cid="+getItem(position).getCid();
                Request request = builder.url(request_url).build();
                Call call = okHttpClient.newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Request request, IOException e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(Response response) throws IOException {

                    }
                });
                //点击删除  必须先通知后台删除才能删除本地 否则会报数组越界异常 原因问哦也不知道
                remove(getItem(position));
                notifyDataSetChanged();
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
