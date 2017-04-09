package com.henghao.hhworkpresent.protocol;

import android.content.Context;

import com.benefit.buy.library.http.query.callback.AjaxStatus;
import com.benefit.buy.library.utils.tools.ToolsJson;
import com.benefit.buy.library.utils.tools.ToolsKit;
import com.google.gson.reflect.TypeToken;
import com.henghao.hhworkpresent.BaseModel;
import com.henghao.hhworkpresent.BeeCallback;
import com.henghao.hhworkpresent.ProtocolUrl;
import com.henghao.hhworkpresent.entity.BaseEntity;
import com.henghao.hhworkpresent.entity.GonggaoEntity;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by bryanrady on 2017/3/15.
 *
 * 关于公告请求
 */

public class GonggaoProtocol extends BaseModel {

    public GonggaoProtocol(Context context) {
        super(context);
    }

    /**
     * 查询未读公告
     */
    public void queryUnreadGongao(String uid){
        try {
            String url = ProtocolUrl.APP_QUERY_UNREAD_GONGGAO;
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("uid", uid);
            this.mBeeCallback.url(url).type(String.class).params(params);
            this.aq.ajax(this.mBeeCallback);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 查询已读公告
     */
    public void queryReadGongao(String uid){
        try {
            String url = ProtocolUrl.APP_QUERY_READ_GONGGAO;
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("uid", uid);
            this.mBeeCallback.url(url).type(String.class).params(params);
            this.aq.ajax(this.mBeeCallback);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 将一条未读变为已读
     * @param gid
     */
    public void addReadGonggao(Integer gid,String uid){
        try {
            String url = ProtocolUrl.APP_ADD_READ_GONGGAO;
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("gid", gid);
            params.put("uid", uid);
            this.mBeeCallback.url(url).type(String.class).params(params);
            this.aq.ajax(this.mBeeCallback);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 将全部未读变为已读
     * @param uid
     */
    public void addAllReadGonggao(String uid){
        try {
            String url = ProtocolUrl.APP_ADD_ALL_READ_GONGGAO;
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("uid", uid);
            this.mBeeCallback.url(url).type(String.class).params(params);
            this.aq.ajax(this.mBeeCallback);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 删除公告接口
     * @param gid
     */
    public void deleteGonggao(Integer gid,String uid){
        try {
            String url = ProtocolUrl.APP_DELETE_GONGGAO;
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("gid", gid);
            params.put("uid", uid);
            this.mBeeCallback.url(url).type(String.class).params(params);
            this.aq.ajax(this.mBeeCallback);
        } catch (Exception e){
            e.printStackTrace();
        }
    }


    private final BeeCallback<String> mBeeCallback = new BeeCallback<String>() {

        @Override
        public void callback(String url, String object, AjaxStatus status) {
            try {
                /**** start ****/
                BaseEntity mBaseEntity = ToolsJson.parseObjecta(object, BaseEntity.class);
                if (mBaseEntity == null) {
                    GonggaoProtocol.this.OnMessageResponse(url, mBaseEntity, status);
                    return;
                }
                Object obj= ToolsJson.toJson(mBaseEntity.getData());
                if(obj==null){
                    GonggaoProtocol.this.OnMessageResponse(url, mBaseEntity, status);
                }
                String data = ToolsJson.toJson(mBaseEntity.getData());
                if (ToolsKit.isEmpty(data)) {
                    GonggaoProtocol.this.OnMessageResponse(url, mBaseEntity, status);
                    return;
                }
                /**** end ****/

                if (url.endsWith(ProtocolUrl.APP_SEND_GONGGAO)) {
                    // 发送公告
                    GonggaoProtocol.this.OnMessageResponse(url, mBaseEntity, status);

                } else if (url.endsWith(ProtocolUrl.APP_QUERY_UNREAD_GONGGAO)){
                    // 查询未读公告
                    Type type = new TypeToken<List<GonggaoEntity>>() {
                    }.getType();
                    List<GonggaoEntity> homeData = ToolsJson.parseObjecta(data, type);
                    GonggaoProtocol.this.OnMessageResponse(url, homeData, status);

                } else if (url.endsWith(ProtocolUrl.APP_QUERY_READ_GONGGAO)){
                    // 查询已读公告
                    Type type = new TypeToken<List<GonggaoEntity>>() {
                    }.getType();
                    List<GonggaoEntity> homeData = ToolsJson.parseObjecta(data, type);
                    GonggaoProtocol.this.OnMessageResponse(url, homeData, status);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };


}
