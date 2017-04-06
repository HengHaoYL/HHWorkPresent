package com.henghao.hhworkpresent.adapter;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.henghao.hhworkpresent.ActivityFragmentSupport;
import com.henghao.hhworkpresent.Constant;
import com.henghao.hhworkpresent.ProtocolUrl;
import com.henghao.hhworkpresent.R;
import com.henghao.hhworkpresent.entity.GonggaoEntity;
import com.henghao.hhworkpresent.protocol.GonggaoProtocol;
import com.henghao.hhworkpresent.views.DatabaseHelper;
import com.lidroid.xutils.BitmapUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import java.util.ArrayList;
import java.util.List;

/**
 * 通知公告已读消息适配器
 * Created by bryanrady on 2017/3/3.
 */

public class NotificatReadGonggaoAdapter extends ArrayAdapter<GonggaoEntity> {

    private ImageLoader imageLoader;

    private DisplayImageOptions options;

    private List<GonggaoEntity> mdeleteList = new ArrayList<>();

    private final LayoutInflater inflater;

    private final BitmapUtils mBitmapUtils;

    private final ActivityFragmentSupport mActivityFragmentSupport;

    public NotificatReadGonggaoAdapter(ActivityFragmentSupport activityFragment, List<GonggaoEntity> mList){
        super(activityFragment, R.layout.listview_notification_item, mList);
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
            convertView = this.inflater.inflate(R.layout.listview_notification_item, null);
            mHodlerView.imageView = (ImageView) convertView.findViewById(R.id.msg_imageview);
            mHodlerView.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
            mHodlerView.tv_titile = (TextView) convertView.findViewById(R.id.tv_content);
            mHodlerView.imageDelete = (ImageView) convertView.findViewById(R.id.msg_delete);
            convertView.setTag(mHodlerView);
        } else {
            mHodlerView = (HodlerView) convertView.getTag();
        }

        // 使用DisplayImageOptions.Builder()创建DisplayImageOptions
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.icon_logo) // 设置图片下载期间显示的图片
                .showImageForEmptyUri(R.drawable.ic_launcher) // 设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(R.drawable.ic_launcher) // 设置图片加载或解码过程中发生错误显示的图片
                .cacheInMemory(true) // 设置下载的图片是否缓存在内存中
                .cacheOnDisk(true) // 设置下载的图片是否缓存在SD卡中
                .displayer(new RoundedBitmapDisplayer(20)) // 设置成圆角图片
                .build(); // 构建完成

        imageLoader = ImageLoader.getInstance();

  //      imageLoader.init(ImageLoaderConfiguration.createDefault(mActivityFragmentSupport));

        String imageUri = ProtocolUrl.ROOT_URL + ProtocolUrl.APP_QUERY_GONGGAO_IMAGE + getItem(position).getGonggao_imageUrl();
        imageLoader.displayImage(imageUri, mHodlerView.imageView, options);
        mHodlerView.tv_titile.setText(getItem(position).getGonggao_titile());
        mHodlerView.tv_time.setText(getItem(position).getGonggao_sendDate());

        /**
         * 删除公告
         */
        mHodlerView.imageDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mActivityFragmentSupport);
                builder.setMessage("是否要要删除这条公告？");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        GonggaoProtocol gonggaoProtocol = new GonggaoProtocol(mActivityFragmentSupport);
                        gonggaoProtocol.addResponseListener(mActivityFragmentSupport);
                        gonggaoProtocol.deleteGonggao(getItem(position).getGid());

                        mdeleteList.add(getItem(position));
                        mdeleteList.remove(position);

                        gonggaoProtocol.queryReadGongao(getLoginUid());
                        dialog.dismiss();
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        return convertView;
    }

    /**
     * 从本地数据库读取登录用户Id 用来作为数据请求id
     * @return
     */
    public String getLoginUid(){
        DatabaseHelper dbHelper = new DatabaseHelper(mActivityFragmentSupport,"user_login.db");
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.query("user",new String[]{"uid"},null,null,null,null,null);
        String uid = null;
        while (cursor.moveToNext()){
            uid = cursor.getString((cursor.getColumnIndex("uid")));
        }
        return uid;
    }

    private class HodlerView {

        ImageView imageView;

        TextView tv_time;

        TextView tv_titile;

        ImageView imageDelete;
    }

}
