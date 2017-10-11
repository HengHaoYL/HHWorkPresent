package com.henghao.hhworkpresent.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.henghao.hhworkpresent.activity.MeetingManageActivity;

import cn.jpush.android.api.JPushInterface;

/**
 * 用户自定义的极光推送广播接收器
 * Created by ASUS on 2017/9/26.
 */

public class MyJPushReceiver extends BroadcastReceiver {

    public static String msg_id;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
            // 在这里可以做些统计，或者做些其他工作

        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
            // 在这里可以自己写代码去定义用户点击后的行为  可以统一进入推送消息列表
            Intent intent1 = new Intent();
            intent1.setClass(context, MeetingManageActivity.class);
            intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent1);
            /*Bundle bundle = intent.getExtras();
            msg_id = bundle.getString(JPushInterface.EXTRA_MSG_ID);    //唯一标识通知消息的 ID
            String result_str = bundle.getString(JPushInterface.EXTRA_EXTRA);
            try {
                JSONObject jsonObject = new JSONObject(result_str);
                String extra = jsonObject.getString("extra");
                if(extra!=null){
                    MeetingEntity meetingEntity = new Gson().fromJson(extra,MeetingEntity.class);
                    //    messageMeetingList.add(meetingEntity);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }*/
        } else if(JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())){   //用户接收SDK消息的intent
            Bundle bundle = intent.getExtras();
            String msg_id = bundle.getString(JPushInterface.EXTRA_MSG_ID);    //唯一标识通知消息的 ID
            String result_str = bundle.getString(JPushInterface.EXTRA_EXTRA);
            /*try {
            JSONObject jsonObject = new JSONObject(result_str);
                String extra = jsonObject.getString("extra");
                if(extra!=null){
                    MeetingEntity meetingEntity = new Gson().fromJson(extra,MeetingEntity.class);
                //    messageMeetingList.add(meetingEntity);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }*/
        }
    }
}
