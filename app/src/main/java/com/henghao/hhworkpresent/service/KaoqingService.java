package com.henghao.hhworkpresent.service;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.henghao.hhworkpresent.ProtocolUrl;
import com.henghao.hhworkpresent.activity.KaoQingActivity;
import com.henghao.hhworkpresent.activity.KaoqingDetailActivity;
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
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by bryanrady on 2017/6/5.
 */

public class KaoqingService extends Service {

    private SqliteDBUtils sqliteDBUtils;
    private NotificationUtils notificationUtils;
    private DownLoadKaoqingThread thread;
    private Handler mHandler = new Handler(){};
    public static final int NO_6 = 1006;    //8:50  缺勤预警
    public static final int NO_7 = 1007;    //异常通知
    public static final int NO_8 = 1008;    //异常通知

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sqliteDBUtils = new SqliteDBUtils(this);
        notificationUtils = new NotificationUtils(this);
        thread = new DownLoadKaoqingThread();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (!thread.isRunning) {
            thread.isRunning = true;
            thread.start();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * 开辟线程下载 考勤信息
     */
    class DownLoadKaoqingThread extends Thread{

        public boolean isRunning = false;

        @Override
        public void run() {
            while (isRunning) {
                try{
                    httpRequestQueka();
                    httpRequestKaoqingofCurrentDay();
                    Thread.sleep(600000);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 查询当天签到信息
     */
    private void httpRequestKaoqingofCurrentDay() {
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String textDate = format.format(date);
        SqliteDBUtils sqliteDBUtils = new SqliteDBUtils(this);
        OkHttpClient okHttpClient = new OkHttpClient();
        Request.Builder builder = new Request.Builder();
        FormEncodingBuilder requestBodyBuilder = new FormEncodingBuilder();
        requestBodyBuilder.add("userId", sqliteDBUtils.getLoginUid());
        requestBodyBuilder.add("date", textDate);
        RequestBody requestBody = requestBodyBuilder.build();
        String request_url = ProtocolUrl.ROOT_URL + "/"+ ProtocolUrl.APP_QUERY_DAY_OF_KAOQING;
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
                    final JSONObject jsonObject = new JSONObject(result_str);
                    //开始用String 来接收 放回 data出现Null的情况 ,导致布局无法显示
                    String data = jsonObject.getString("data");
                    JSONObject jsonObject1 = new JSONObject(data);
                    String checkInfo = jsonObject1.getString("ck");
                    if (("null").equals(checkInfo)) {
                        Date date = new Date();
                        final SimpleDateFormat format = new SimpleDateFormat("HH:mm");
                        String currentTime = format.format(date);
                        //如果等于8:50
                        if (equalsString850(currentTime)==1) {
                            notificationUtils.addNotfication("打卡信息", "还有10分钟就要上班了，请记得打卡！", NO_6 , new KaoQingActivity());
                        }
                    } else {
                        int status = jsonObject.getInt("status");
                        if(status==0){
                            final JSONObject dataObject = jsonObject1.getJSONObject("ck");
                            final String morningCount = dataObject.optString("morningCount");
                            final String afterCount = dataObject.optString("afterCount");
                            Date date = new Date();
                            SimpleDateFormat format = new SimpleDateFormat("HH:mm");
                            String currentTime = format.format(date);
                            //代表上午还没有签到
                            if("0".equals(morningCount)) {
                                //如果等于8:50
                                if (equalsString850(currentTime) == 1) {
                                    notificationUtils.addNotfication("打卡信息", "还有10分钟就要上班了，请记得打卡！", NO_6 , new KaoQingActivity());
                                    return;
                                }
                            }

                            //代表下午还没有签到
                            if("0".equals(afterCount)){
                                //如果超过了17:00
                                if(equalsString17(currentTime)){
                                    notificationUtils.addNotfication("打卡信息", "已经到了下班时间，请记得打卡！", NO_6 , new KaoQingActivity());
                                }
                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 查询当月缺卡记录
     */
    private void httpRequestQueka() {
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
        String dateText = format.format(date);
        OkHttpClient okHttpClient = new OkHttpClient();
        Request.Builder builder = new Request.Builder();
        FormEncodingBuilder requestBodyBuider = new FormEncodingBuilder();
        requestBodyBuider.add("userId", sqliteDBUtils.getLoginUid());
        requestBodyBuider.add("date", dateText);
        RequestBody requestBody = requestBodyBuider.build();
        String request_url = ProtocolUrl.ROOT_URL + "/"+ ProtocolUrl.APP_QUERY_MONTH_QUEKA;
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
                    if(status==0){
                        String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        for(int i=0;i<jsonArray.length();i++){
                            JSONObject dataObject = jsonArray.getJSONObject(i);
                            String currentDate = dataObject.optString("currentDate");
                            String workDay = dataObject.optString("week");
                            String clockInTime = dataObject.optString("clockInTime");
                            String clockOutTime = dataObject.optString("clockOutTime");
                            Bundle bundle = new Bundle();
                            bundle.putString("currentDate",currentDate);
                            bundle.putString("currentWeek",workDay);
                            if(("null").equals(clockInTime)){
                                notificationUtils.addNotficationBundle("缺卡信息","你上午有一条缺卡记录，点击查看详情并记得补卡！", NO_7 , new KaoqingDetailActivity(),bundle);
                            }

                            /**
                             * 为了修改当天没到下班时间，用户没有打卡 会显示缺卡情况，不符合逻辑
                             */
                            //如果日期不是当天
                            if(!date.equals(currentDate)){
                                if(("null").equals(clockOutTime)){
                                    notificationUtils.addNotficationBundle("缺卡信息", "你下午有一条缺卡记录，点击查看详情并记得补卡！", NO_8 , new KaoqingDetailActivity(),bundle);
                                }
                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    /**
     * 比较是否等于08:50 等于返回1  不等0
     */
    public int equalsString850(String currentdate){
        //定义一个标准时间
        int[] arr = {8,50,0};
        String[] strings = currentdate.split(":");
        int[] temp = new int[strings.length];
        //将字符数据转为int数组
        for (int i = 0; i < strings.length; i++) {
            temp[i]=Integer.parseInt(strings[i]);
        }
        if (temp[0]==arr[0]) {
            if(temp[1]==arr[1]){
                return 1;
            }
        }
        return 0;
    }

    /**
     * 比较是否超过了17:00  超过返回true
     */
    public boolean equalsString17(String currentdate){
        //定义一个标准时间
        int[] arr = {18,0,0};
        String[] strings = currentdate.split(":");
        int[] temp = new int[strings.length];
        //将字符数据转为int数组
        for (int i = 0; i < strings.length; i++) {
            temp[i]=Integer.parseInt(strings[i]);
        }
        if (temp[0]>arr[0]) {
            return true;
        }
        return false;
    }

}
