package com.henghao.hhworkpresent.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.henghao.hhworkpresent.ActivityFragmentSupport;
import com.henghao.hhworkpresent.ProtocolUrl;
import com.henghao.hhworkpresent.R;
import com.henghao.hhworkpresent.adapter.WorkTrajectoryListAdapter;
import com.henghao.hhworkpresent.entity.TrajectoryEntity;
import com.henghao.hhworkpresent.listener.OnDateChooseDialogListener;
import com.henghao.hhworkpresent.utils.SqliteDBUtils;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * 工作轨迹界面
 * Created by bryanrady on 2017/7/17.
 */

public class WorkTrajectoryActivity extends ActivityFragmentSupport {

    @ViewInject(R.id.work_trajectory_listview)
    private ListView work_trajectory_listview;

    @ViewInject(R.id.work_trajectory_layout)
    private LinearLayout work_trajectory_layout;

    @ViewInject(R.id.null_trajectory_layout)
    private RelativeLayout null_trajectory_layout;

    @ViewInject(R.id.tv_shangbanDaka)
    private TextView tv_shangbanDaka;

    @ViewInject(R.id.tv_xiabanDaka)
    private TextView tv_xiabanDaka;

    @ViewInject(R.id.shangban_layout)
    private LinearLayout shangban_layout;

    @ViewInject(R.id.xiaban_layout)
    private LinearLayout xiaban_layout;

    @ViewInject(R.id.tv_trajectory_datepicker)
    private TextView datepickerTV;

    private String textString;
    private SqliteDBUtils sqliteDBUtils;

    private WorkTrajectoryListAdapter mAdapter;
    private List<TrajectoryEntity> worktrajectoryList;

    public final static int HOLIDAY_TRUE = 2;       //是节假日
    public final static int HOLIDAY_FALSE = 1;      //不是节假日 并且是当前日期
    public final static int HOLIDAY_DIALOG_FALSE = 3;       //不是节假日  不是当前日期
    public final static int HOLIDAY_DIALOG_TRUE = 4;

    public Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == HOLIDAY_TRUE){
                work_trajectory_layout.setVisibility(View.GONE);
                null_trajectory_layout.setVisibility(View.VISIBLE);
            }else if(msg.what == HOLIDAY_FALSE){
                httpRequestKaoqingofCurrentDay();
            }else if(msg.what == HOLIDAY_DIALOG_FALSE){
                work_trajectory_layout.setVisibility(View.VISIBLE);
                null_trajectory_layout.setVisibility(View.GONE);
                httpRequestKaoqingofPastDate();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.mActivityFragmentView.viewMain(R.layout.activity_trajectory);
        this.mActivityFragmentView.viewEmpty(R.layout.activity_empty);
        this.mActivityFragmentView.viewEmptyGone();
        this.mActivityFragmentView.viewLoading(View.GONE);
        this.mActivityFragmentView.clipToPadding(true);
        ViewUtils.inject(this, this.mActivityFragmentView);
        setContentView(this.mActivityFragmentView);
        initWidget();
        initData();
    }

    @Override
    public void initWidget() {
        super.initWidget();
        initWithBar();
        mLeftTextView.setText("工作轨迹");
        mLeftTextView.setVisibility(View.VISIBLE);
        initWithRightBar();
        mRightTextView.setText("添加轨迹");
        mRightTextView.setVisibility(View.VISIBLE);
        mRightTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(WorkTrajectoryActivity.this,AddTrajectoryActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void initData() {
        super.initData();
        sqliteDBUtils = new SqliteDBUtils(this);
        SimpleDateFormat f=new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        textString = f.format(date);
        datepickerTV.setText(textString);

        worktrajectoryList = new ArrayList<TrajectoryEntity>();
        mAdapter = new WorkTrajectoryListAdapter(WorkTrajectoryActivity.this,worktrajectoryList);
    }

    @Override
    public void onResume() {
        super.onResume();
        equalsHoliday(datepickerTV.getText().toString());
        downloadWorkTragectory();
    }

    /**
     * 从服务器下载工作轨迹数据
     */
    public void downloadWorkTragectory(){
        OkHttpClient okHttpClient = new OkHttpClient();
        Request.Builder builder = new Request.Builder();
        FormEncodingBuilder requestBodyBuilder = new FormEncodingBuilder();
        requestBodyBuilder.add("userId", sqliteDBUtils.getLoginUid());
        requestBodyBuilder.add("date", datepickerTV.getText().toString());
        RequestBody requestBody = requestBodyBuilder.build();
        String request_url = ProtocolUrl.ROOT_URL + ProtocolUrl.APP_DOWNLOAD_WORK_TRAJECTORY;
        final Request request = builder.url(request_url).post(requestBody).build();
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
                    worktrajectoryList.clear();
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    for(int i=0;i<jsonArray.length();i++){
                        //这两个必须卸用在循环内，否则对象和集合重复
                        TrajectoryEntity trajectoryEntity = new TrajectoryEntity();
                        List<String> eventImageNameList = new ArrayList<String>();
                        JSONObject dataObject = jsonArray.getJSONObject(i);
                        String eventName = dataObject.optString("eventName");
                        String eventAddress = dataObject.optString("eventAddress");
                        String eventTime = dataObject.optString("eventTime");
                        JSONArray jsonArray1 = dataObject.getJSONArray("eventImageNameList");
                        eventImageNameList.clear();
                        for(int j=0;j<jsonArray1.length();j++){
                            String filePath = jsonArray1.optString(j);
                            eventImageNameList.add(filePath);
                        }
                        trajectoryEntity.setEventName(eventName);
                        trajectoryEntity.setEventAddress(eventAddress);
                        trajectoryEntity.setEventTime(eventTime);
                        trajectoryEntity.setEventImageNameList(eventImageNameList);
                        worktrajectoryList.add(trajectoryEntity);
                    }
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            mActivityFragmentView.viewLoading(View.GONE);
                            mAdapter.notifyDataSetChanged();
                            work_trajectory_listview.setAdapter(mAdapter);
                        }
                    });

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    @OnClick({R.id.tv_trajectory_datepicker})
    private void viewOnClick(View v) {
        switch (v.getId()) {
            case R.id.tv_trajectory_datepicker:
                onClickDatePicker();
                break;
        }
    }

    /**
     * 查询当天签到信息
     */
    private void httpRequestKaoqingofCurrentDay() {
        SqliteDBUtils sqliteDBUtils = new SqliteDBUtils(this);
        OkHttpClient okHttpClient = new OkHttpClient();
        Request.Builder builder = new Request.Builder();
        FormEncodingBuilder requestBodyBuilder = new FormEncodingBuilder();
        requestBodyBuilder.add("userId", sqliteDBUtils.getLoginUid());
        requestBodyBuilder.add("date", datepickerTV.getText().toString());
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
                    }
                });
            }

            @Override
            public void onResponse(Response response) throws IOException {
                String result_str = response.body().string();
                try {
                    final JSONObject jsonObject = new JSONObject(result_str);

                    String data = jsonObject.getString("data");
                    JSONObject jsonObject1 = new JSONObject(data);
                    String checkInfo = jsonObject1.getString("ck");
                    final String middleTime = jsonObject1.getString("MiddleTime");

                    if (("null").equals(checkInfo)) {
                        mActivityFragmentView.viewLoading(View.GONE);
                        null_trajectory_layout.setVisibility(View.VISIBLE);
                        work_trajectory_layout.setVisibility(View.GONE);
                    } else {
                        int status = jsonObject.getInt("status");
                        final String msg = jsonObject.getString("msg");
                        if (status == 1) {
                            mHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    mActivityFragmentView.viewLoading(View.GONE);
                                    msg(msg);
                                }
                            });
                        }
                        final JSONObject dataObject = jsonObject1.getJSONObject("ck");
                        final String morningCount = dataObject.optString("morningCount");
                        final String afterCount = dataObject.optString("afterCount");
                        final String clockInTime = dataObject.optString("clockInTime");
                        final String clockOutTime = dataObject.optString("clockOutTime");
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                mActivityFragmentView.viewLoading(View.GONE);
                                //        waiqing_layout.setVisibility(View.VISIBLE);
                                //代表上午还没有签到
                                if("0".equals(morningCount)){
                                    Date date = new Date();
                                    SimpleDateFormat format = new SimpleDateFormat("HH:mm");
                                    String currentTime = format.format(date);
                                    //如果没超过12.00 表示上午
                                    if(equalsStringMiddle(currentTime,middleTime)){
                                        null_trajectory_layout.setVisibility(View.VISIBLE);
                                        work_trajectory_layout.setVisibility(View.GONE);
                                    }else {
                                        null_trajectory_layout.setVisibility(View.GONE);
                                        work_trajectory_layout.setVisibility(View.VISIBLE);
                                        shangban_layout.setVisibility(View.VISIBLE);
                                        tv_shangbanDaka.setText("无");
                                    }
                                    return;
                                }else {
                                    null_trajectory_layout.setVisibility(View.GONE);
                                    work_trajectory_layout.setVisibility(View.VISIBLE);
                                    shangban_layout.setVisibility(View.VISIBLE);
                                    if("null".equals(clockInTime)){
                                        tv_shangbanDaka.setText("无");
                                    }else{
                                        tv_shangbanDaka.setText(clockInTime);
                                    }

                                }

                                //代表下午还没有签到
                                if("0".equals(afterCount)){
                                    xiaban_layout.setVisibility(View.GONE);
                                }else{
                                    xiaban_layout.setVisibility(View.VISIBLE);
                                    if("null".equals(clockOutTime)){
                                        tv_xiabanDaka.setText("无");
                                    }else{
                                        tv_xiabanDaka.setText(clockOutTime);
                                    }

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
     * 查询过去时间的考勤信息
     */
    private void httpRequestKaoqingofPastDate() {
        OkHttpClient okHttpClient = new OkHttpClient();
        Request.Builder builder = new Request.Builder();
        FormEncodingBuilder requestBodyBuilder = new FormEncodingBuilder();
        String date = datepickerTV.getText().toString();
        requestBodyBuilder.add("userId", sqliteDBUtils.getLoginUid());
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
                    String data = jsonObject.getString("data");
                    JSONObject jsonObject1 = new JSONObject(data);
                    String checkInfo = jsonObject1.getString("ck");
                    if (("null").equals(checkInfo)) {
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                mActivityFragmentView.viewLoading(View.GONE);
                            }
                        });
                    }else{
                        int status = jsonObject.getInt("status");
                        if (status == 0) {
                            mHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    mActivityFragmentView.viewLoading(View.GONE);
                                    null_trajectory_layout.setVisibility(View.GONE);
                                }
                            });
                        }
                        final JSONObject dataObject = jsonObject1.getJSONObject("ck");
                        final String clockInTime = dataObject.optString("clockInTime");
                        final String clockOutTime = dataObject.optString("clockOutTime");
                        //这时的clockInTime是一个null字符串 ，不是null
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                shangban_layout.setVisibility(View.VISIBLE);
                                xiaban_layout.setVisibility(View.VISIBLE);
                                if("null".equals(clockInTime)){
                                    tv_shangbanDaka.setText("无");
                                }else{
                                    tv_shangbanDaka.setText(clockInTime);
                                }
                                if("null".equals(clockOutTime)){
                                    tv_xiabanDaka.setText("无");
                                }else{
                                    tv_xiabanDaka.setText(clockOutTime);
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
     * 日期选择控件 点击
     */
    public void onClickDatePicker(){
        final DateChooseDialog.Builder builder = new DateChooseDialog.Builder(this);
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
                //大于当前日期：1，    等于当前日期：0，      小于当前日期：-1
                if(type==0){
                    null_trajectory_layout.setVisibility(View.VISIBLE);
                    equalsHoliday(datepickerTV.getText().toString());
                } else if(type==1){
                    work_trajectory_layout.setVisibility(View.GONE);
                    null_trajectory_layout.setVisibility(View.VISIBLE);
                } else if(type==-1){
                    equalsHoliday(datepickerTV.getText().toString());
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

    /**
     * 判断日期date是不是节假日 isHoliday true代表是节假日和周末不上班的日子  false代表不是节假日要上班
     * date 需要处理
     */
    public Handler mHandler = new Handler(){};
    private void equalsHoliday(String date){
        String textdate = date.replace("-","");
        OkHttpClient okHttpClient = new OkHttpClient();
        Request.Builder builder = new Request.Builder();
        FormEncodingBuilder requestBodyBuilder = new FormEncodingBuilder();
        //这个id是日期在数据库里的id 形式20170530
        requestBodyBuilder.add("id", textdate);
        RequestBody requestBody = requestBodyBuilder.build();
        String request_url = ProtocolUrl.ROOT_URL + "/"+ ProtocolUrl.APP_QUERY_DAY_OF_HOLIDAY;
        Request request = builder.url(request_url).post(requestBody).build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mActivityFragmentView.viewLoading(View.GONE);
                    }
                });
            }

            @Override
            public void onResponse(Response response) throws IOException {
                String result_str = response.body().string();
                //result_str=={"id":20170530,"date":"2017-05-30","status":"法定假日"}
                // {"id":20170528,"date":"2017-05-28","status":"周末"}
                Log.d("wangqingbin","result_str=="+result_str);
                //如果是空，  result_str.length()==0这个判断才有用
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mActivityFragmentView.viewLoading(View.GONE);
                    }
                });
                try{
                    JSONObject jsonObject = new JSONObject(result_str);
                    String status = jsonObject.getString("status");
                    //代表是要上班的日子
                    if("无节假日信息".equals(status)){
                        //如果日期是当天
                        if((datepickerTV.getText().toString()).equals(new SimpleDateFormat("yyyy-MM-dd").format(new Date()))){
                            Message message = Message.obtain();
                            message.what = HOLIDAY_FALSE;
                            handler.sendMessage(message);
                        }else{
                            Message message = Message.obtain();
                            message.what = HOLIDAY_DIALOG_FALSE;
                            handler.sendMessage(message);
                        }
                    }else{
                        Message message = Message.obtain();
                        message.what = HOLIDAY_TRUE;
                        handler.sendMessage(message);
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        });
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

    /**
     * 比较是否超过了中间时间  超过返回false
     */
    public boolean equalsStringMiddle(String currentdate,String middleTime){
        //定义一个标准时间
        String[] strings = currentdate.split(":");
        String[] middleArr = middleTime.split(":");
        int[] temp = new int[strings.length];
        int[] middle = new int[middleArr.length];
        //将字符数据转为int数组
        for (int i = 0; i < strings.length; i++) {
            temp[i]=Integer.parseInt(strings[i]);
        }
        for (int i = 0; i < middle.length; i++) {
            middle[i]=Integer.parseInt(middleArr[i]);
        }
        //只要是在12点之前，都属于上午，在12点之后，都属于下午
        if (temp[0]<middle[0]) {
            return true;
        }
        return false;
    }
}
