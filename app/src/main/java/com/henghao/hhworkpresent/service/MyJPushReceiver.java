package com.henghao.hhworkpresent.service;

import android.content.Context;

import cn.jpush.android.api.JPushMessage;
import cn.jpush.android.service.JPushMessageReceiver;

/**
 * Created by ASUS on 2017/9/26.
 */

public class MyJPushReceiver extends JPushMessageReceiver {

    public MyJPushReceiver(){
        super();
    }

    @Override
    public void onTagOperatorResult(Context context, JPushMessage jPushMessage) {
        super.onTagOperatorResult(context, jPushMessage);
    }

    @Override
    public void onCheckTagOperatorResult(Context context, JPushMessage jPushMessage) {
        super.onCheckTagOperatorResult(context, jPushMessage);
    }

    @Override
    public void onAliasOperatorResult(Context context, JPushMessage jPushMessage) {
        super.onAliasOperatorResult(context, jPushMessage);
    }
}
