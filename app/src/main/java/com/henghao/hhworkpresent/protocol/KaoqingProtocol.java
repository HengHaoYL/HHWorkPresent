package com.henghao.hhworkpresent.protocol;

import android.content.Context;

import com.benefit.buy.library.http.query.callback.AjaxStatus;
import com.benefit.buy.library.utils.tools.ToolsJson;
import com.benefit.buy.library.utils.tools.ToolsKit;
import com.henghao.hhworkpresent.BaseModel;
import com.henghao.hhworkpresent.BeeCallback;
import com.henghao.hhworkpresent.entity.BaseEntity;

/**
 * Created by bryanrady on 2017/3/22.
 *
 * 考勤请求
 */

public class KaoqingProtocol extends BaseModel {

    public KaoqingProtocol(Context context) {
        super(context);
    }



    private final BeeCallback<String> mBeeCallback = new BeeCallback<String>() {

        @Override
        public void callback(String url, String object, AjaxStatus status) {
            try {
                /**** start ****/
                BaseEntity mBaseEntity = ToolsJson.parseObjecta(object, BaseEntity.class);
                if (mBaseEntity == null) {
                    KaoqingProtocol.this.OnMessageResponse(url, mBaseEntity, status);
                    return;
                }
                Object obj = ToolsJson.toJson(mBaseEntity.getData());
                if (obj == null) {
                    KaoqingProtocol.this.OnMessageResponse(url, mBaseEntity, status);
                }
                String data = ToolsJson.toJson(mBaseEntity.getData());
                if (ToolsKit.isEmpty(data)) {
                    KaoqingProtocol.this.OnMessageResponse(url, mBaseEntity, status);
                    return;
                }
                /**** end ****/

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };
}
