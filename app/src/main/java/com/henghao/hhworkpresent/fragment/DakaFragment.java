package com.henghao.hhworkpresent.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.henghao.hhworkpresent.FragmentSupport;
import com.henghao.hhworkpresent.ProtocolUrl;
import com.henghao.hhworkpresent.R;
import com.henghao.hhworkpresent.activity.MainActivity;
import com.henghao.hhworkpresent.activity.QiandaoShangbanSubmitActivity;
import com.henghao.hhworkpresent.activity.QiandaoXiabanSubmitActivity;
import com.henghao.hhworkpresent.listener.OnDateChooseDialogListener;
import com.henghao.hhworkpresent.utils.LocationUtils;
import com.henghao.hhworkpresent.views.DatabaseHelper;
import com.henghao.hhworkpresent.views.DateChooseDialog;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.app.Activity.RESULT_OK;

/**
 * Created by bryanrady on 2017/3/10.
 * 打卡界面
 */

public class DakaFragment extends FragmentSupport {

    @ViewInject(R.id.tv_datepicker)
    private TextView datepickerTV;
    private String textString;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        this.mActivityFragmentView.viewMain(R.layout.fragment_currentdate_shangbandaka);
        this.mActivityFragmentView.viewEmpty(R.layout.activity_empty);
        this.mActivityFragmentView.viewEmptyGone();
        this.mActivityFragmentView.viewLoading(View.GONE);
        ViewUtils.inject(this, this.mActivityFragmentView);

        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        String currentTime = format.format(date);
        //如果没超过12.00 表示上午
        if(equalsString12(currentTime)){
            pastdate_layout.setVisibility(View.GONE);
            shangban_layout.setVisibility(View.VISIBLE);
            xiaban_layout.setVisibility(View.GONE);
            Date date1 = new Date();
            SimpleDateFormat format1 = new SimpleDateFormat("HH:mm");
            String currentTime1 = format.format(date1);
            shangban_qiandao_date.setText(currentTime1);
        }else {
            //下午
            pastdate_layout.setVisibility(View.GONE);
            shangban_layout.setVisibility(View.GONE);
            xiaban_layout.setVisibility(View.VISIBLE);
            httpRequestKaoqingofCurrentDateShangwu();
            Date date1 = new Date();
            SimpleDateFormat format1 = new SimpleDateFormat("HH:mm");
            String currentTime1 = format.format(date1);
            xiaban_qiandao_date.setText(currentTime1);
        }
        //注册定位监听
        LocationUtils.Location(this.mActivity);
        initWidget();
        initData();
        return this.mActivityFragmentView;
    }

    public void initWidget(){
        initWithBar();
        mLeftTextView.setText("打卡");
        mLeftTextView.setVisibility(View.VISIBLE);
        mLeftTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toMainActivity();
            }
        });
        mLeftImageView.setImageResource(R.drawable.item_point_left);
        mLeftImageView.setVisibility(View.VISIBLE);
        mLeftImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toMainActivity();
            }
        });
    }

    public void toMainActivity(){
        Intent intent = new Intent();
        intent.setClass(getContext(),MainActivity.class);
        startActivity(intent);
    }

    public void initData(){
        SimpleDateFormat f=new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        textString = f.format(date);
        datepickerTV.setText(textString);
    }

   /*************** 三大布局 *********************/
    @ViewInject(R.id.fragment_pastdate_daka_layout)
    private RelativeLayout pastdate_layout;

    @ViewInject(R.id.fragment_shangban_daka_layout)
    private RelativeLayout shangban_layout;

    @ViewInject(R.id.fragment_xiaban_daka_layout)
    private RelativeLayout xiaban_layout;
    /************************************/

    /*************** 上班打卡布局 *********************/
    //上班签到图片
    @ViewInject(R.id.daka_shangban_qiandao_image)
    private ImageView shangban_qiandao_image;

    @ViewInject(R.id.daka_shangban_qiandao_date)
    private TextView shangban_qiandao_date;

    @ViewInject(R.id.daka_shangban_qiandao_state)
    private TextView shangban_qiandao_location;
    /************************************/

    /*************** 下班打卡布局 *********************/
    //下班布局的上班时间
    @ViewInject(R.id.daka_xiaban_shangbantime)
    private TextView xiaban_shangbantime;

    //下班布局的上班状态
    @ViewInject(R.id.daka_xiaban_shangbanstate)
    private TextView xiaban_shangbanstate;

    //下班布局的上班补卡申请
    @ViewInject(R.id.daka_xiaban_shangbanbuka)
    private Button xiaban_shangbanbuka;

    //下班签到图片
    @ViewInject(R.id.daka_xiaban_qiandao_image)
    private ImageView xiaban_qiandao_image;

    //下班打卡时间
    @ViewInject(R.id.daka_xiaban_qiandao_datetime)
    private TextView xiaban_qiandao_date;

    @ViewInject(R.id.daka_xiaban_qiandao_state)
    private TextView xiaban_qiandao_location;

    /************************************/


    /*************** 过去日期布局获取控件 *********************/
    @ViewInject(R.id.daka_pastdate_shangbantime)
    private TextView pastdate_shangbantime;

    @ViewInject(R.id.daka_pastdate_shangbanstate)
    private TextView pastdate_shangbanstate;

    @ViewInject(R.id.daka_pastdate_shangbanbuka)
    private Button pastdate_shangbanbuka;

    @ViewInject(R.id.daka_pastdate_xiabantime)
    private TextView pastdate_xiabantime;

    @ViewInject(R.id.daka_pastdate_xiabanstate)
    private TextView pastdate_xiabanstate;

    @ViewInject(R.id.daka_pastdate_xiabanbuka)
    private Button pastdate_xiabanbuka;

    /************************************/

    @OnClick({R.id.tv_datepicker,R.id.daka_shangban_qiandao_image,R.id.daka_xiaban_qiandao_image})
    private void viewOnClick(View v) {
        switch (v.getId()) {
            case R.id.tv_datepicker:
                onClickDatePicker();
                break;

            case R.id.daka_shangban_qiandao_image:
                onClickShangbanDaka();
                break;

            case R.id.daka_xiaban_qiandao_image:
                onClickXiabanDaka();
                break;
        }
    }

    private static int SHANGBAN_QIANDAO_REQUEST = 1001;
    private static int XIABAN_QIANDAO_REQUEST = 1002;

    private String shangbanDakaAdrress;
    private String xiabanDakaAdrress;

    @Override
    public void onResume() {
        super.onResume();
        LocationUtils.Location(this.mActivity);
        shangbanDakaAdrress = LocationUtils.getAddress();
        if(("").equals(shangbanDakaAdrress)||("null").equals(shangbanDakaAdrress)){
            shangban_qiandao_location.setText("当前没有定位信息!");
        }else{
            shangban_qiandao_location.setText(shangbanDakaAdrress);
        }

        xiabanDakaAdrress = LocationUtils.getAddress();
        if(("").equals(xiabanDakaAdrress)||("null").equals(xiabanDakaAdrress)){
            xiaban_qiandao_location.setText("当前没有定位信息!");
        }else{
            xiaban_qiandao_location.setText(xiabanDakaAdrress);
        }
    }

    public void onClickShangbanDaka(){
        Intent intent = new Intent(mActivity, QiandaoShangbanSubmitActivity.class);
        String time = shangban_qiandao_date.getText().toString();// 签到时间
        String address = shangban_qiandao_location.getText().toString(); // 签到地址
        double longitude = LocationUtils.getLng();
        double latitude = LocationUtils.getLat();
        if (address.equals("当前没有定位信息!")) {
            Toast.makeText(mActivity, "当前没有定位，请定位后再签到！", Toast.LENGTH_SHORT).show();
            return;
        }
        intent.putExtra("time", time);
        intent.putExtra("latitude", latitude);
        intent.putExtra("longitude", longitude);
        intent.putExtra("address", address);
        startActivityForResult(intent,SHANGBAN_QIANDAO_REQUEST);
    }

    public void onClickXiabanDaka(){
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        String currentTime = format.format(date);
        //如果没超过12.00 表示上午
        if(equalsString12(currentTime)){
            Toast.makeText(this.mActivity,"时间还没到下午，不能打下班卡!",Toast.LENGTH_SHORT).show();
            return;
        }else {
            //下午
            pastdate_layout.setVisibility(View.GONE);
            shangban_layout.setVisibility(View.GONE);
            xiaban_layout.setVisibility(View.VISIBLE);
            httpRequestKaoqingofCurrentDateShangwu();
            Date date1 = new Date();
            SimpleDateFormat format1 = new SimpleDateFormat("HH:mm");
            String currentTime1 = format.format(date1);
            xiaban_qiandao_date.setText(currentTime1);
        }
        Intent intent = new Intent(mActivity, QiandaoXiabanSubmitActivity.class);
        String time = xiaban_qiandao_date.getText().toString();// 签到时间
        String address = xiaban_qiandao_location.getText().toString(); // 签到地址
        double longitude = LocationUtils.getLng();
        double latitude = LocationUtils.getLat();
        Log.d("wangqingbin","address=="+address);
        Log.d("wangqingbin","longitude=="+longitude);
        Log.d("wangqingbin","latitude=="+latitude);
        if (address.equals("当前没有定位信息!")) {
            Toast.makeText(mActivity, "当前没有定位，请定位后再签到！", Toast.LENGTH_SHORT).show();
            return;
        }
        intent.putExtra("time", time);
        intent.putExtra("latitude", latitude);
        intent.putExtra("longitude", longitude);
        intent.putExtra("address", address);
        startActivityForResult(intent,XIABAN_QIANDAO_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //上班打卡成功
        if (requestCode == SHANGBAN_QIANDAO_REQUEST && resultCode == RESULT_OK) {
            //下午
            pastdate_layout.setVisibility(View.GONE);
            shangban_layout.setVisibility(View.GONE);
            xiaban_layout.setVisibility(View.VISIBLE);
            httpRequestKaoqingofCurrentDateShangwu();
            Date date = new Date();
            SimpleDateFormat format = new SimpleDateFormat("HH:mm");
            String currentTime = format.format(date);
            xiaban_qiandao_date.setText(currentTime);

        } else if(requestCode == XIABAN_QIANDAO_REQUEST && resultCode == RESULT_OK){
            //下班打卡成功
            pastdate_layout.setVisibility(View.VISIBLE);
            shangban_layout.setVisibility(View.GONE);
            xiaban_layout.setVisibility(View.GONE);
            httpRequestKaoqingofPastDate();
        }
    }

    public void onClickDatePicker(){
        final DateChooseDialog.Builder builder = new DateChooseDialog.Builder(this.mActivity);
        builder.setTitle("选择日期");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                //设置你的操作事项
                dialog.dismiss();
            }
        });

        //监听对话框确定键，得到返回的数据
        builder.setOnDateChooseDialogListener(new OnDateChooseDialogListener() {
            @Override
            public void onDateSetting(String textDate) {
                datepickerTV.setText(textDate);
                int type = equalsDate(datepickerTV.getText().toString());
                //大于当前日期：1，等于当前日期：0，小于当前日期：-1
                if(type==0){
                    Date date = new Date();
                    SimpleDateFormat format = new SimpleDateFormat("HH:mm");
                    String currentTime = format.format(date);
                    //如果没超过12.00 表示上午
                    if(equalsString12(currentTime)){
                        pastdate_layout.setVisibility(View.GONE);
                        shangban_layout.setVisibility(View.VISIBLE);
                        xiaban_layout.setVisibility(View.GONE);
                        Date date1 = new Date();
                        SimpleDateFormat format1 = new SimpleDateFormat("HH:mm");
                        String currentTime1 = format.format(date1);
                        shangban_qiandao_date.setText(currentTime1);
                    }else{
                        //下午
                        pastdate_layout.setVisibility(View.GONE);
                        shangban_layout.setVisibility(View.GONE);
                        xiaban_layout.setVisibility(View.VISIBLE);
                        httpRequestKaoqingofCurrentDateShangwu();
                        Date date1 = new Date();
                        SimpleDateFormat format1 = new SimpleDateFormat("HH:mm");
                        String currentTime1 = format.format(date1);
                        xiaban_qiandao_date.setText(currentTime1);
                    }

                } else if(type==1){

                } else if(type==-1){
                    pastdate_layout.setVisibility(View.VISIBLE);
                    shangban_layout.setVisibility(View.GONE);
                    xiaban_layout.setVisibility(View.GONE);
                    httpRequestKaoqingofPastDate();
                }
            }
        });

        builder.setNegativeButton("取消", new android.content.DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }


    private Handler mHandler = new Handler(){};

    /**
     * 从本地数据库读取登录用户Id 用来作为数据请求id
     * @return
     */
    public String getLoginUid(){
        DatabaseHelper dbHelper = new DatabaseHelper(this.mActivity,"user_login.db");
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.query("user",new String[]{"uid"},null,null,null,null,null);
        String uid = null;
        while (cursor.moveToNext()){
            uid = cursor.getString((cursor.getColumnIndex("uid")));
        }
        return uid;
    }

    private void httpRequestKaoqingofCurrentDateShangwu() {
        OkHttpClient okHttpClient = new OkHttpClient();
        Request.Builder builder = new Request.Builder();
        FormEncodingBuilder requestBodyBuilder = new FormEncodingBuilder();
        String date ="2017-03-31";
        requestBodyBuilder.add("uid", getLoginUid());
        requestBodyBuilder.add("date", date);
        RequestBody requestBody = requestBodyBuilder.build();
        String request_url = ProtocolUrl.ROOT_URL + "/"+ ProtocolUrl.APP_QUERY_DAY_OF_KAOQING;
        Request request = builder.url(request_url).post(requestBody).build();
        Call call = okHttpClient.newCall(request);
        mActivityFragmentView.viewLoading(View.VISIBLE);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mActivityFragmentView.viewLoading(View.GONE);
                        Toast.makeText(getContext(), "网络访问错误！", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Response response) throws IOException {
                String result_str = response.body().string();
                try {
                    JSONObject jsonObject = new JSONObject(result_str);
                    int status = jsonObject.getInt("status");
                    if (status == 0) {
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                mActivityFragmentView.viewLoading(View.GONE);
                            }
                        });
                    }
                    final JSONObject dataObject = jsonObject.getJSONObject("data");
                    final String clockInTime = dataObject.optString("clockInTime");
                    final String clockOutTime = dataObject.optString("clockOutTime");
                    //这时的clockInTime是一个null字符串 ，不是null
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            xiaban_shangbantime.setText(clockInTime);
                        }
                    });

                    //缺卡情况
                    if(("null").equals(clockInTime)){
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                xiaban_shangbanstate.setText("缺卡");
                                xiaban_shangbantime.setText("无");
                                xiaban_shangbanbuka.setVisibility(View.VISIBLE);
                                mActivityFragmentView.viewLoading(View.GONE);
                            }
                        });
                    }

                    //上班迟到情况
                    if(!("null").equals(clockInTime)){
                        if(equalsStringShangban(clockInTime)){
                            mHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    xiaban_shangbanstate.setText("迟到");
                                    xiaban_shangbanbuka.setVisibility(View.GONE);
                                }
                            });

                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void httpRequestKaoqingofPastDate() {
        OkHttpClient okHttpClient = new OkHttpClient();
        Request.Builder builder = new Request.Builder();
        FormEncodingBuilder requestBodyBuilder = new FormEncodingBuilder();
        String date ="2017-03-22";
        requestBodyBuilder.add("uid", getLoginUid());
        requestBodyBuilder.add("date", date);
        RequestBody requestBody = requestBodyBuilder.build();
        String request_url = ProtocolUrl.ROOT_URL + "/"+ ProtocolUrl.APP_QUERY_DAY_OF_KAOQING;
        Request request = builder.url(request_url).post(requestBody).build();
        Call call = okHttpClient.newCall(request);
        mActivityFragmentView.viewLoading(View.VISIBLE);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mActivityFragmentView.viewLoading(View.GONE);
                        Toast.makeText(getContext(), "网络访问错误！", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Response response) throws IOException {
                String result_str = response.body().string();
                try {
                    JSONObject jsonObject = new JSONObject(result_str);
                    int status = jsonObject.getInt("status");
                    if (status == 0) {
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                mActivityFragmentView.viewLoading(View.GONE);
                            }
                        });
                    }
                    final JSONObject dataObject = jsonObject.getJSONObject("data");
                    final String clockInTime = dataObject.optString("clockInTime");
                    final String clockOutTime = dataObject.optString("clockOutTime");
                    //这时的clockInTime是一个null字符串 ，不是null
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            pastdate_shangbantime.setText(clockInTime);
                            pastdate_xiabantime.setText(clockOutTime);
                        }
                    });

                    //缺卡情况
                    if(("null").equals(clockInTime)){
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                pastdate_shangbanstate.setText("缺卡");
                                pastdate_shangbantime.setText("无");
                                pastdate_shangbanbuka.setVisibility(View.VISIBLE);
                                mActivityFragmentView.viewLoading(View.GONE);
                            }
                        });
                    }

                    if(("null").equals(clockOutTime)){
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                pastdate_xiabanstate.setText("缺卡");
                                pastdate_xiabantime.setText("无");
                                pastdate_xiabanbuka.setVisibility(View.VISIBLE);
                                mActivityFragmentView.viewLoading(View.GONE);
                            }
                        });
                    }

                    //上班迟到情况
                    if(!("null").equals(clockInTime)){
                        if(equalsStringShangban(clockInTime)){
                            mHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    pastdate_shangbanstate.setText("迟到");
                                }
                            });

                        }
                    }

                    //下班早退情况
                    if(!("null").equals(clockOutTime)){
                        if(equalsStringXiaban(clockOutTime)){
                            mHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    pastdate_xiabanstate.setText("早退");
                                }
                            });
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 比较上班时间  迟到返回true
     */
    public boolean equalsStringShangban(String clockInTime){
        //定义一个标准时间 09:00
        int[] arr = {9,0,0};
        String[] strings = clockInTime.split(":");
        int[] temp = new int[strings.length];
        //将字符数据转为int数组
        for (int i = 0; i < strings.length; i++) {
            temp[i]=Integer.parseInt(strings[i]);
        }
        //比较小时
        if (temp[0]>arr[0]) {
            return true;
        }
        if(temp[0]==arr[0]){
            //比较分钟
            if (temp[1]>arr[1]) {
                return true;
            }
            //如果分钟相等	9.0.0 , 9.0.0
            if (temp[1]==arr[1]) {
                //比较秒的用意，是为了对刚好在时间点打卡（如：9:00:00）的判断
                if (temp[2]>arr[2]) {
                    return true;
                }
            }

        }
        return false;
    }

    /**
     * 比较下班时间  早退返回true
     */
    public boolean equalsStringXiaban(String clockOutTime){
        //定义一个标准时间
        int[] arr = {18,0,0};
        String[] strings = clockOutTime.split(":");
        int[] temp = new int[strings.length];
        //将字符数据转为int数组
        for (int i = 0; i < strings.length; i++) {
            temp[i]=Integer.parseInt(strings[i]);
        }
        //只要是在18点之前，都属于早退，在18点之后，都属于正常下班
        if (temp[0]<arr[0]) {
            return true;
        }
        return false;
    }

    /**
     * 比较是否超过了12:00  超过返回false
     */
    public boolean equalsString12(String currentdate){
        //定义一个标准时间
        int[] arr = {12,0,0};
        String[] strings = currentdate.split(":");
        int[] temp = new int[strings.length];
        //将字符数据转为int数组
        for (int i = 0; i < strings.length; i++) {
            temp[i]=Integer.parseInt(strings[i]);
        }
        //只要是在12点之前，都属于上午，在12点之后，都属于下午
        if (temp[0]<arr[0]) {
            return true;
        }
        return false;
    }

    //日期比较测试       返回值：大于当前日期：1，等于当前日期：0，小于当前日期：-1
    public static int equalsDate(String date){
        //定义一个系统当前日期
        Date date1 = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String currentTime = format.format(date1);
        //传进来的日期数组
        int[] dataArr = StringToIntArr(date);
        //当前日期数组
        int[] current = StringToIntArr(currentTime);
        //进行比较
        if (dataArr[0]>current[0]) {
            System.out.println("大于当前日期");
            return 1;
        }
        if (dataArr[0]==current[0]) {
            //年份相等，判断月份
            if (dataArr[1]>current[1]) {
                System.out.println("大于当前日期");
                return 1;
            }else if(dataArr[1]==current[1]){
                //月份相等，判断天
                if (dataArr[2]>current[2]) {
                    System.out.println("大于当前日期");
                    return 1;
                }else if(dataArr[2]==current[2]){
                    System.out.println("等于当前日期");
                    return 0;
                }
                System.out.println("小于当前日期");
                return -1;
            }
        }
        //年份小于
        System.out.println("小于当前日期");
        return -1;
    }

    //传入String类型日期，返回int 数组
    public static int[] StringToIntArr(String date){
        String[] strings = date.split("-");
        int[] arr = new int[strings.length];
        for (int i = 0; i < strings.length; i++) {
            arr[i] = Integer.parseInt(strings[i]);
        }
        return arr;
    }
}
