package com.henghao.hhworkpresent.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * 用来接收系统发出的广播 启动 服务  不能启动了APP后在启动服务
 * Created by bryanrady on 2017/6/8.
 */

public class BootCompletedReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        Log.e("wangqingbin","action=="+action);
        //系统启动完成的广播
        if(action.equals(Intent.ACTION_BOOT_COMPLETED)){
            /**
             * 开启消息通知服务
             */
            Intent intent2 = new Intent(context, NotificationService.class);
            context.startService(intent2);

            /**
             * 开启考勤通知服务
             */
            Intent intent3 = new Intent(context, KaoqingService.class);
            context.startService(intent3);
        }
    }
}
