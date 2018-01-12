package com.henghao.hhworkpresent.service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.henghao.hhworkpresent.ProtocolUrl;
import com.henghao.hhworkpresent.R;
import com.henghao.hhworkpresent.activity.GongGaoActivity;
import com.henghao.hhworkpresent.activity.MainActivity;
import com.henghao.hhworkpresent.utils.NotificationUtils;
import com.henghao.hhworkpresent.utils.SqliteDBUtils;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import static com.henghao.hhworkpresent.fragment.MsgFragment.NO_1;
import static com.henghao.hhworkpresent.fragment.MsgFragment.NO_2;

/**
 * Created by bryanrady on 2017/6/5.
 */
public class NotificationService extends Service{

    private int gerendaiban_count;
    private SqliteDBUtils sqliteDBUtils;
    private NotificationUtils notificationUtils;
    private DownLoadMsgAndGonggaoThread thread;
    private Handler mHandler = new Handler();

    @Override
    public void onCreate() {
        super.onCreate();
        sqliteDBUtils = new SqliteDBUtils(this);
        notificationUtils = new NotificationUtils(this);
        thread = new DownLoadMsgAndGonggaoThread();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (!thread.isRunning) {
            thread.isRunning = true;
            thread.start();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    /**
     * 开辟线程下载 消息数目和公告未读数目
     */
    class DownLoadMsgAndGonggaoThread extends Thread{

        public boolean isRunning = false;

        @Override
        public void run() {
            while (isRunning) {
                try{
                    httpRequesMsgCounts();
                    queryUnReadGonggao();
                    Thread.sleep(600000);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }

    private void queryUnReadGonggao(){
        OkHttpClient okHttpClient = new OkHttpClient();
        Request.Builder builder = new Request.Builder();
        FormEncodingBuilder requestBodyBuilder = new FormEncodingBuilder();
        requestBodyBuilder.add("uid", sqliteDBUtils.getLoginUid());
        RequestBody requestBody = requestBodyBuilder.build();
        String request_url = ProtocolUrl.ROOT_URL + "/"+ ProtocolUrl.APP_QUERY_UNREAD_GONGGAO;
        Request request = builder.url(request_url).post(requestBody).build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
            }

            @Override
            public void onResponse(Response response) throws IOException {
                String result_str = response.body().string();
                try {
                    JSONObject jsonObject = new JSONObject(result_str);
                    int status = jsonObject.getInt("status");
                    if (status == 0) {
                        final JSONArray jsonArray = jsonObject.getJSONArray("data");
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                if(jsonArray.length()>0){
                                    notificationUtils.addNotfication(getString(R.string.unread_announcement), getString(R.string.new_unread_announcement), NO_1 , new GongGaoActivity());
                                }
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 请求消息内容数量
     */
    private void httpRequesMsgCounts(){
        OkHttpClient okHttpClient = new OkHttpClient();
        Request.Builder builder = new Request.Builder();
        FormEncodingBuilder requestBodyBuilder = new FormEncodingBuilder();
        requestBodyBuilder.add("uid", sqliteDBUtils.getLoginUid());
        RequestBody requestBody = requestBodyBuilder.build();
        String request_url = ProtocolUrl.ROOT_URL + "/"+ ProtocolUrl.APP_REQUEST_MSG_LIST_COUNTS;
        Request request = builder.url(request_url).post(requestBody).build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
            }

            @Override
            public void onResponse(Response response) throws IOException {
                String result_str = response.body().string();
                try {
                    JSONObject jsonObject = new JSONObject(result_str);
                    int status = jsonObject.getInt("status");
                    if (status == 0) {
                        JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                        gerendaiban_count = Integer.parseInt(jsonObject1.optString("gerendaiban_count"));

                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                if(gerendaiban_count>0){
                                    notificationUtils.addNotfication(getString(R.string.work_tobe_done), getString(R.string.new_work_tobe_done), NO_2 , new MainActivity());
                                }
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
