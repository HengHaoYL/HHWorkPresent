package com.henghao.hhworkpresent.utils;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

import com.henghao.hhworkpresent.R;

/**
 * Created by bryanrady on 2017/6/5.
 */

public class NotificationUtils {

    private Context mContext;
    private NotificationManager mNotificationManager;

    public NotificationUtils(Context context){
        this.mContext = context;
        mNotificationManager =  (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
    }

    /**
     * 添加通知公告
     * @param title
     * @param content
     * @param NO
     * @param activity
     */
    public void addNotfication(String title, String content, int NO, Activity activity){
        Intent intent = new Intent(mContext, activity.getClass());
        PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext);
        builder.setContentTitle(title);
        builder.setContentText(content);
        builder.setSmallIcon(R.drawable.icon_ajj_logo);
        builder.setContentIntent(pendingIntent);
        builder.setTicker("你有新消息来了");
     //   builder.setWhen(System.currentTimeMillis());
        Notification notification = builder.build();
        notification.flags = Notification.FLAG_AUTO_CANCEL;
        mNotificationManager.notify(NO,notification);

    }

    /**
     * 添加通知公告  携带数据Bundle
     * @param title
     * @param content
     * @param NO
     * @param activity
     */
    public void addNotficationBundle(String title, String content, int NO, Activity activity, Bundle bundle){
        Intent intent = new Intent(mContext, activity.getClass());
        intent.putExtras(bundle);
        PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext);
        builder.setContentTitle(title);
        builder.setContentText(content);
        builder.setSmallIcon(R.drawable.icon_ajj_logo);
        builder.setContentIntent(pendingIntent);
        builder.setTicker("你有新消息来了");
        //   builder.setWhen(System.currentTimeMillis());
        Notification notification = builder.build();
        notification.flags = Notification.FLAG_AUTO_CANCEL;
        mNotificationManager.notify(NO,notification);

    }

}
