package com.henghao.hhworkpresent.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.benefit.buy.library.views.xlistview.XListView;
import com.henghao.hhworkpresent.FragmentSupport;
import com.henghao.hhworkpresent.ProtocolUrl;
import com.henghao.hhworkpresent.R;
import com.henghao.hhworkpresent.activity.CalendarActivity;
import com.henghao.hhworkpresent.activity.KaoqingChidaoDetailActivity;
import com.henghao.hhworkpresent.activity.KaoqingKuanggongDetailActivity;
import com.henghao.hhworkpresent.activity.KaoqingQuekaDetailActivity;
import com.henghao.hhworkpresent.activity.KaoqingZaotuiDetailActivity;
import com.henghao.hhworkpresent.activity.MainActivity;
import com.henghao.hhworkpresent.adapter.ChidaoListAdapter;
import com.henghao.hhworkpresent.adapter.KuanggongListAdapter;
import com.henghao.hhworkpresent.adapter.QuekaListAdapter;
import com.henghao.hhworkpresent.adapter.ZaotuiListAdapter;
import com.henghao.hhworkpresent.entity.KaoqingEntity;
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
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by bryanrady on 2017/3/10.
 * 考勤界面的考勤记录模块
 */

public class KaoqingFragment extends FragmentSupport {

    @ViewInject(R.id.tv_datepicker)
    private TextView tv_datepicker;

    @ViewInject(R.id.kaoqing_lookyueli)
    private Button lookBtn;

    private String textString;
    private String[] datas;
    private RadioOnClick listener = new RadioOnClick(0);

    private Handler mHandler = new Handler(){};

    @ViewInject(R.id.listview_kaoqing_chidao)
    private XListView chidaoListview;

    @ViewInject(R.id.listview_kaoqing_zaotui)
    private XListView zaotuiListview;

    @ViewInject(R.id.listview_kaoqing_queka)
    private XListView quekaListview;

    @ViewInject(R.id.listview_kaoqing_kuanggong)
    private XListView kuanggongListview;

    @ViewInject(R.id.tv_attendanceDays)
    private TextView tv_attendanceDays;

    @ViewInject(R.id.tv_normalPunchTimes)
    private TextView tv_normalPunchTimes;

    @ViewInject(R.id.tv_waiqingPunchTimes)
    private TextView tv_waiqingPunchTimes;

    @ViewInject(R.id.tv_leaveEarlyDay)
    private TextView tv_leaveEarlyDay;

    @ViewInject(R.id.tv_missingCardTimes)
    private TextView tv_missingCardTimes;

    @ViewInject(R.id.tv_leaveDays)
    private TextView tv_leaveDays;

    @ViewInject(R.id.tv_kuanggongDays)
    private TextView tv_kuanggongDays;

    @ViewInject(R.id.tv_businessTravelDays)
    private TextView tv_businessTravelDays;

    @ViewInject(R.id.tv_lateTimes)
    private TextView tv_lateTimes;


    private ChidaoListAdapter mChidaoAdapter;
    private ZaotuiListAdapter mZaotuiAdapter;
    private QuekaListAdapter mQuekaAdapter;
    private KuanggongListAdapter mKuanggongAdapter;

    private List<KaoqingEntity> mChidaoData,mZaotuiData,mQuekaData,mKuanggongData;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        this.mActivityFragmentView.viewMain(R.layout.fragment_kaoqing);
        this.mActivityFragmentView.viewEmpty(R.layout.activity_empty);
        this.mActivityFragmentView.viewEmptyGone();
        this.mActivityFragmentView.viewLoading(View.GONE);
        ViewUtils.inject(this, this.mActivityFragmentView);
        initWidget();
        initData();
        return this.mActivityFragmentView;
    }

    public void initWidget(){
        initWithBar();
        mLeftTextView.setText("考勤");
        mLeftTextView.setVisibility(View.VISIBLE);
        mLeftTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(getContext(),MainActivity.class);
                startActivity(intent);
            }
        });
        mLeftImageView.setImageResource(R.drawable.item_point_left);
        mLeftImageView.setVisibility(View.VISIBLE);
        mLeftImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(getContext(),MainActivity.class);
                startActivity(intent);
            }
        });
        initWithRightBar();
        mRightImageView.setVisibility(View.VISIBLE);
        mRightImageView.setImageResource(R.drawable.item_wenhao);

        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月");
        tv_datepicker.setText(format.format(date));

    }

    public void initData(){
       /* datas = new String[]{"2017年3月","2017年2月", "2017年1月", "2016年12月", "2016年11月",
                "2016年10月", "2016年9月" , "206年8月", "2016年7月", "2016年6月"};
*/
        DynamicArray();

        mChidaoData = new ArrayList<>();
        mZaotuiData = new ArrayList<>();
        mQuekaData = new ArrayList<>();
        mKuanggongData = new ArrayList<>();

        mChidaoAdapter = new ChidaoListAdapter(mActivity, mChidaoData);
        mZaotuiAdapter = new ZaotuiListAdapter(mActivity,mZaotuiData);
        mQuekaAdapter = new QuekaListAdapter(mActivity, mQuekaData);
        mKuanggongAdapter = new KuanggongListAdapter(mActivity,mKuanggongData);
        chidaoListview.setAdapter(mChidaoAdapter);
        zaotuiListview.setAdapter(mZaotuiAdapter);
        quekaListview.setAdapter(mQuekaAdapter);
        kuanggongListview.setAdapter(mKuanggongAdapter);
        mChidaoAdapter.notifyDataSetChanged();
        mZaotuiAdapter.notifyDataSetChanged();
        mQuekaAdapter.notifyDataSetChanged();
        mKuanggongAdapter.notifyDataSetChanged();

        initEvent();
    }

    @Override
    public void onResume() {
        super.onResume();
        httpRequest();
        httpRequestChidao();
        httpRequestZaotui();
        httpRequestQueka();
        httpRequestKuanggong();
    }


    /**
     * 访问网络
     */
    private void httpRequest() {
        OkHttpClient okHttpClient = new OkHttpClient();
        Request.Builder builder = new Request.Builder();
/*        SharedPreferences preferences = getSharedPreferences(Constant.SHARED_SET, 0);
        String UID = preferences.getString(Constant.USERID, null);*/
        FormEncodingBuilder requestBodyBuilder = new FormEncodingBuilder();
        requestBodyBuilder.add("uid", "1");
        requestBodyBuilder.add("date", transferDateTime(tv_datepicker.getText().toString()));
        RequestBody requestBody = requestBodyBuilder.build();
        String request_url = ProtocolUrl.ROOT_URL + "/"+ ProtocolUrl.APP_QUERY_MOUNTH_KAOQING;
        Log.d("wangqingbin","request_url=="+request_url);
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
                    JSONObject jsonObject1 = jsonObject.getJSONObject("data");

                    final String attendanceDays = jsonObject1.optString("attendanceDays");
                    final String normalPunchTimes = jsonObject1.optString("normalPunchTimes");
                    final String waiqingPunchTimes = jsonObject1.optString("waiqingPunchTimes");
                    final String leaveEarlyDay  = jsonObject1.optString("leaveEarlyDay");
                    final String missingCardTimes = jsonObject1.optString("missingCardTimes");
                    final String lateTimes = jsonObject1.optString("lateTimes");
                    final String kuanggongDays = jsonObject1.optString("kuanggongDays");
                    final String businessTravelDays = jsonObject1.optString("businessTravelDays");
                    final String leaveDays = jsonObject1.optString("leaveDays");
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            tv_attendanceDays.setText(attendanceDays+"天");
                            tv_normalPunchTimes.setText(normalPunchTimes+"天");
                            tv_waiqingPunchTimes.setText(waiqingPunchTimes+"次");
                            tv_leaveEarlyDay.setText(leaveEarlyDay+"次");
                            tv_missingCardTimes.setText(missingCardTimes+"次");
                            tv_lateTimes.setText(lateTimes+"次");
                            tv_kuanggongDays.setText(kuanggongDays+"次");
                            tv_businessTravelDays.setText(businessTravelDays+"天");
                            tv_leaveDays.setText(leaveDays+"天");
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void httpRequestChidao() {
        OkHttpClient okHttpClient = new OkHttpClient();
        Request.Builder builder = new Request.Builder();
/*        SharedPreferences preferences = getSharedPreferences(Constant.SHARED_SET, 0);
        String UID = preferences.getString(Constant.USERID, null);*/
        FormEncodingBuilder requestBodyBuilder = new FormEncodingBuilder();
        requestBodyBuilder.add("uid", "1");
        requestBodyBuilder.add("date", transferDateTime(tv_datepicker.getText().toString()));
        RequestBody requestBody = requestBodyBuilder.build();
        String request_url = ProtocolUrl.ROOT_URL + "/"+ ProtocolUrl.APP_QUERY_MONTH_CHIDAO;
        Log.d("wangqingbin","request_url=="+request_url);
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
                    mChidaoData.clear();
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    for(int i=0;i<jsonArray.length();i++){
                        KaoqingEntity kaoqingEntity = new KaoqingEntity();
                        JSONObject dataObject = jsonArray.getJSONObject(i);
                        String currentDate = dataObject.optString("currentDate");
                        String workDay = dataObject.optString("workDay");
                        String clockInTime = dataObject.optString("clockInTime");
                        kaoqingEntity.setCurrentDate(currentDate);
                        kaoqingEntity.setWorkDay(workDay);
                        kaoqingEntity.setClockInTime(clockInTime);
                        mChidaoData.add(kaoqingEntity);
                    }
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            mChidaoAdapter.notifyDataSetChanged();
                            chidaoListview.setAdapter(mChidaoAdapter);

                            mZaotuiAdapter.notifyDataSetChanged();
                            zaotuiListview.setAdapter(mZaotuiAdapter);

                            mActivityFragmentView.viewLoading(View.GONE);
                        }
                    });

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void httpRequestZaotui() {
        OkHttpClient okHttpClient = new OkHttpClient();
        Request.Builder builder = new Request.Builder();
/*        SharedPreferences preferences = getSharedPreferences(Constant.SHARED_SET, 0);
        String UID = preferences.getString(Constant.USERID, null);*/
        FormEncodingBuilder requestBodyBuilder = new FormEncodingBuilder();
        requestBodyBuilder.add("uid", "1");
        requestBodyBuilder.add("date", transferDateTime(tv_datepicker.getText().toString()));
        RequestBody requestBody = requestBodyBuilder.build();
        String request_url = ProtocolUrl.ROOT_URL + "/"+ ProtocolUrl.APP_QUERY_MONTH_ZAOTUI;
        Log.d("wangqingbin","request_url=="+request_url);
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
                    mZaotuiData.clear();
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    for(int i=0;i<jsonArray.length();i++){
                        KaoqingEntity kaoqingEntity = new KaoqingEntity();
                        JSONObject dataObject = jsonArray.getJSONObject(i);
                        String currentDate = dataObject.optString("currentDate");
                        String workDay = dataObject.optString("workDay");
                        String clockOutTime = dataObject.optString("clockOutTime");
                        kaoqingEntity.setCurrentDate(currentDate);
                        kaoqingEntity.setWorkDay(workDay);
                        kaoqingEntity.setClockOutTime(clockOutTime);
                        mZaotuiData.add(kaoqingEntity);
                    }
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            mZaotuiAdapter.notifyDataSetChanged();
                            zaotuiListview.setAdapter(mZaotuiAdapter);
                            mActivityFragmentView.viewLoading(View.GONE);
                        }
                    });

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void httpRequestQueka() {
        OkHttpClient okHttpClient = new OkHttpClient();
        Request.Builder builder = new Request.Builder();
/*        SharedPreferences preferences = getSharedPreferences(Constant.SHARED_SET, 0);
        String UID = preferences.getString(Constant.USERID, null);*/
        FormEncodingBuilder requestBodyBuilder = new FormEncodingBuilder();
        requestBodyBuilder.add("uid", "1");
        requestBodyBuilder.add("date", transferDateTime(tv_datepicker.getText().toString()));
        RequestBody requestBody = requestBodyBuilder.build();
        String request_url = ProtocolUrl.ROOT_URL + "/"+ ProtocolUrl.APP_QUERY_MONTH_QUEKA;
        Log.d("wangqingbin","request_url=="+request_url);
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
                    mQuekaData.clear();
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    for(int i=0;i<jsonArray.length();i++){
                        KaoqingEntity kaoqingEntity = new KaoqingEntity();
                        JSONObject dataObject = jsonArray.getJSONObject(i);
                        String currentDate = dataObject.optString("currentDate");
                        String workDay = dataObject.optString("workDay");
                        String clockInTime = dataObject.optString("clockInTime");
                        String clockOutTime = dataObject.optString("clockOutTime");
                        //这时的clockInTime是一个null字符串 ，不是null
                        if(("null").equals(clockInTime)){
                            clockInTime = "09:00";
                        }else{
                            clockInTime = "18:00";
                        }
                        kaoqingEntity.setClockInTime(clockInTime);
                        kaoqingEntity.setCurrentDate(currentDate);
                        kaoqingEntity.setWorkDay(workDay);
                        mQuekaData.add(kaoqingEntity);
                    }
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            mQuekaAdapter.notifyDataSetChanged();
                            quekaListview.setAdapter(mQuekaAdapter);
                            mActivityFragmentView.viewLoading(View.GONE);
                        }
                    });

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void httpRequestKuanggong() {
        OkHttpClient okHttpClient = new OkHttpClient();
        Request.Builder builder = new Request.Builder();
/*        SharedPreferences preferences = getSharedPreferences(Constant.SHARED_SET, 0);
        String UID = preferences.getString(Constant.USERID, null);*/
        FormEncodingBuilder requestBodyBuilder = new FormEncodingBuilder();
        requestBodyBuilder.add("uid", "1");
        requestBodyBuilder.add("date", transferDateTime(tv_datepicker.getText().toString()));
        RequestBody requestBody = requestBodyBuilder.build();
        String request_url = ProtocolUrl.ROOT_URL + "/"+ ProtocolUrl.APP_QUERY_MONTH_KUANGGONG;
        Log.d("wangqingbin","request_url=="+request_url);
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
                    Log.d("wangqingbin","jsonObject=="+jsonObject);
                    int status = jsonObject.getInt("status");
                    if (status == 0) {
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                mActivityFragmentView.viewLoading(View.GONE);
                            }
                        });
                    }
                    mKuanggongData.clear();
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    Log.d("wangqingbin","jsonArray=="+jsonArray);
                    for(int i=0;i<jsonArray.length();i++){
                        KaoqingEntity kaoqingEntity = new KaoqingEntity();
                        String kuanggongDateWeek = jsonArray.getString(i);
                        Log.d("wangqingbin","first=="+kuanggongDateWeek);
                        kaoqingEntity.setCurrentDate(kuanggongDateWeek);
                        mKuanggongData.add(kaoqingEntity);
                    }
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            mKuanggongAdapter.notifyDataSetChanged();
                            kuanggongListview.setAdapter(mKuanggongAdapter);
                            mActivityFragmentView.viewLoading(View.GONE);
                        }
                    });

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 进行时间转换
     */
    public String transferDateTime(String date){
        String newDate = date.replace("年","-");
        newDate = newDate.replace("月","");
        return newDate;
    }

    /**
     * 产生一个动态数组，从当前月份到之前月份的动态更新数组
     */
    private void DynamicArray() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int mouth = calendar.get(Calendar.MONTH) + 1;
        datas = new String[10];
        int j = 12;
        for (int i = 0; i < 10; i++) {
            if (mouth > 0) {
                if(mouth<10){
                    datas[i] = year + "年0" + mouth + "月";
                }else{
                    datas[i] = year + "年" + mouth + "月";
                }
                mouth--;
            } else {
                if(j<10){
                    datas[i] = year - 1 + "年0"+j+"月";
                }else{
                    datas[i] = year - 1 + "年" + j + "月";
                }
                j--;
            }
        }
    }


    public void initEvent(){
        chidaoListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("wangqingbin","mChidaoAdapter=="+mChidaoAdapter);
                String currentDate = mChidaoAdapter.getItem(position-1).getCurrentDate();
                Log.d("wangqingbin","currentDate=="+currentDate);
                String currentWeek = mChidaoAdapter.getItem(position-1).getWorkDay();
                Intent intent = new Intent();
                intent.putExtra("currentDate",currentDate);
                intent.putExtra("currentWeek",currentWeek);
                intent.setClass(getContext(), KaoqingChidaoDetailActivity.class);
                startActivity(intent);
            }
        });

        zaotuiListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String currentDate = mZaotuiAdapter.getItem(position-1).getCurrentDate();
                String currentWeek = mZaotuiAdapter.getItem(position-1).getWorkDay();
                Intent intent = new Intent();
                intent.putExtra("currentDate",currentDate);
                intent.putExtra("currentWeek",currentWeek);
                intent.setClass(getContext(), KaoqingZaotuiDetailActivity.class);
                startActivity(intent);
            }
        });

        quekaListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String currentDate = mQuekaAdapter.getItem(position-1).getCurrentDate();
                String currentWeek = mQuekaAdapter.getItem(position-1).getWorkDay();
                Intent intent = new Intent();
                intent.putExtra("currentDate",currentDate);
                intent.putExtra("currentWeek",currentWeek);
                intent.setClass(getContext(), KaoqingQuekaDetailActivity.class);
                startActivity(intent);
            }
        });

        kuanggongListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String currentDate = mKuanggongAdapter.getItem(position-1).getCurrentDate();
                String currentWeek = mKuanggongAdapter.getItem(position-1).getWorkDay();
                Intent intent = new Intent();
                intent.putExtra("currentDate",currentDate);
                intent.putExtra("currentWeek",currentWeek);
                intent.setClass(getContext(), KaoqingKuanggongDetailActivity.class);
                startActivity(intent);
            }
        });
    }

    @OnClick({R.id.tv_datepicker,R.id.kaoqing_lookyueli})
    private void viewOnClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.tv_datepicker:
                showSingleChoiceButton();
                break;

            case R.id.kaoqing_lookyueli:
                intent.setClass(getActivity(), CalendarActivity.class);
                startActivity(intent);
                break;
        }
    }

    /**
     * 展示单选对话框
     */
    public void showSingleChoiceButton() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        builder.setSingleChoiceItems(datas, listener.getIndex(), listener);
        builder.show();
    }


    class RadioOnClick implements DialogInterface.OnClickListener {

        private int index;

        public RadioOnClick(int index) {
            this.index = index;
        }

        public void setIndex(int index) {
            this.index = index;
        }

        public int getIndex() {
            return index;
        }

        @Override
        public void onClick(DialogInterface dialog, int whichButton){
            setIndex(whichButton);
            tv_datepicker.setText(datas[index]);
            dialog.dismiss();
        }

    }

}
