package com.henghao.hhworkpresent.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.henghao.hhworkpresent.ActivityFragmentSupport;
import com.henghao.hhworkpresent.ProtocolUrl;
import com.henghao.hhworkpresent.R;
import com.henghao.hhworkpresent.utils.DateTimeUtils;
import com.henghao.hhworkpresent.utils.SqliteDBUtils;
import com.henghao.hhworkpresent.views.CircleImageView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
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

/**
 * Created by bryanrady on 2017/7/21.
 */

public class RenyuanKaoqingInfoActivity extends ActivityFragmentSupport {

    private ImageLoader imageLoader;

    private DisplayImageOptions options;

    @ViewInject(R.id.kaoqingdetail_circleImageview)
    private CircleImageView headImage;

    private String uid;
    private String name;

    @ViewInject(R.id.daka_calendar_btn)
    private Button daka_calendar_btn;

    @ViewInject(R.id.kaoqing_chidao_username)
    private TextView tv_userName;

    @ViewInject(R.id.kaoqing_chidao_currentdate)
    private TextView tv_currentDate;

    @ViewInject(R.id.kaoqing_chidao_currentweek)
    private TextView tv_currentWeek;

    @ViewInject(R.id.kaoqing_chidao_shangbantime)
    private TextView tv_shangbanTime;

    @ViewInject(R.id.kaoqing_chidao_shangbanDakaState)
    private TextView tv_shangbanState;

    @ViewInject(R.id.kaoqing_chidao_xiabantime)
    private TextView tv_xiabanTime;

    @ViewInject(R.id.kaoqing_chidao_xiabanDakaState)
    private TextView tv_xiabanState;

    @ViewInject(R.id.kaoqing_chidao_xiabanBuka)
    private Button btn_shangbanBuka;

    @ViewInject(R.id.kaoqing_chidao_xiabanBuka)
    private Button btn_xiabanBuka;

    @ViewInject(R.id.kaoqing_chidao_xiabanLinear)
    private RelativeLayout xiabanLayout;

    private SqliteDBUtils sqliteDBUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.mActivityFragmentView.viewMain(R.layout.activity_kaoqing_info);
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
        mLeftTextView.setText("人员考勤信息");
        mLeftTextView.setVisibility(View.VISIBLE);
    }

    @Override
    public void initData() {
        super.initData();
        Intent intent = getIntent();
        uid = intent.getStringExtra("uid");
        name = intent.getStringExtra("name");

        httpLoadingHeadImage();
        tv_userName.setText(name);
        tv_currentDate.setText(new SimpleDateFormat("yyyy年MM月dd日").format(new Date()));
        tv_currentWeek.setText(new SimpleDateFormat("EEEE").format(new Date()));
        httpRequestKaoqingofCurrentDay();
    }

    @OnClick({R.id.daka_calendar_btn})
    private void viewOnClick(View v) {
        switch (v.getId()){
            case R.id.daka_calendar_btn:
                Intent intent = new Intent();
                intent.setClass(this, RenyuanCalendarActivity.class);
                intent.putExtra("uid",uid);
                intent.putExtra("name",name);
                startActivity(intent);
                break;
        }
    }

    public Handler mHandler = new Handler(){};

    /**
     * 查询当天签到信息
     */
    private void httpRequestKaoqingofCurrentDay() {
        OkHttpClient okHttpClient = new OkHttpClient();
        Request.Builder builder = new Request.Builder();
        FormEncodingBuilder requestBodyBuilder = new FormEncodingBuilder();
        requestBodyBuilder.add("userId", uid);
        requestBodyBuilder.add("date", new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
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
                    Date date = new Date();
                    final SimpleDateFormat format = new SimpleDateFormat("HH:mm");
                    final String currentTime = format.format(date);

                    JSONObject jsonObject = new JSONObject(result_str);
                    //开始用String 来接收 放回 data出现Null的情况 ,导致布局无法显示
                    String data = jsonObject.getString("data");
                    JSONObject jsonObject1 = new JSONObject(data);
                    String checkInfo = jsonObject1.getString("ck");
                    final String shouldSBTime = jsonObject1.getString("ClockIn");
                    final String shouldXBTime = jsonObject1.getString("ClockOut");
                    final String middleTime = jsonObject1.getString("MiddleTime");

                    if (("null").equals(checkInfo)) {
                        //如果没超过中间时间, 表示上午
                        if (DateTimeUtils.equalsStringMiddle(currentTime,middleTime)) {
                            mHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    mActivityFragmentView.viewLoading(View.GONE);
                                    tv_shangbanTime.setText("无");
                                }
                            });
                        } else {
                            mHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    //下午
                                    mActivityFragmentView.viewLoading(View.GONE);
                                    tv_xiabanTime.setText("无");
                                }
                            });
                        }
                    } else {
                        JSONObject dataObject = jsonObject1.getJSONObject("ck");
                        String morningCount = dataObject.optString("morningCount");
                        String afterCount = dataObject.optString("afterCount");
                        String checkType = dataObject.optString("checkType");

                        //如果checkType 是这几个值i 直接显示下午布局
                        if("请假".equals(checkType)){
                            mHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    mActivityFragmentView.viewLoading(View.GONE);
                                    tv_shangbanTime.setText("无");
                                    tv_shangbanState.setText("请假");
                                    tv_xiabanTime.setText("无");
                                    tv_xiabanState.setText("请假");
                                }
                            });
                            return;
                        }else if("补签".equals(checkType)){
                            mHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    mActivityFragmentView.viewLoading(View.GONE);
                                    tv_shangbanTime.setText("无");
                                    tv_shangbanState.setText("补签");
                                    tv_xiabanTime.setText("无");
                                    tv_xiabanState.setText("补签");
                                }
                            });
                            return;
                        }else if("出差".equals(checkType)){
                            mHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    mActivityFragmentView.viewLoading(View.GONE);
                                    tv_shangbanTime.setText("无");
                                    tv_shangbanState.setText("出差");
                                    tv_xiabanTime.setText("无");
                                    tv_xiabanState.setText("出差");
                                }
                            });
                            return;
                        }else{
                            //代表上午还没有签到
                            if ("0".equals(morningCount)) {
                                //如果没超过12.00 表示上午
                                if (DateTimeUtils.equalsStringMiddle(currentTime,middleTime)) {
                                    mHandler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            mActivityFragmentView.viewLoading(View.GONE);
                                            tv_shangbanTime.setText("无");
                                            tv_xiabanTime.setText("无");
                                        }
                                    });
                                    return;
                                } else {
                                    mHandler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            //下午
                                            mActivityFragmentView.viewLoading(View.GONE);
                                            tv_shangbanTime.setText("无");
                                            tv_xiabanTime.setText("无");
                                        }
                                    });
                                }
                            } else {
                                mHandler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        mActivityFragmentView.viewLoading(View.GONE);
                                        //如果不是0 则显示下午签到布局
                                        httpRequestKaoqingofCurrentDateShangwu();
                                    }
                                });
                            }

                            //代表下午还没有签到
                            if ("0".equals(afterCount)) {
                                mHandler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        mActivityFragmentView.viewLoading(View.GONE);
                                        httpRequestKaoqingofCurrentDateShangwu();
                                    }
                                });
                            } else {
                                mHandler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        mActivityFragmentView.viewLoading(View.GONE);
                                        httpRequestKaoqingofPastDate();
                                    }
                                });
                            }
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
        requestBodyBuilder.add("userId", uid);
        requestBodyBuilder.add("date", new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
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
                    final String shouldSBTime = jsonObject1.getString("ClockIn");
                    final String shouldXBTime = jsonObject1.getString("ClockOut");
                    final String middleTime = jsonObject1.getString("MiddleTime");
                    if (("null").equals(checkInfo)) {
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                mActivityFragmentView.viewLoading(View.GONE);
                                tv_shangbanTime.setText("无");
                                tv_xiabanTime.setText("无");
                            }
                        });
                    }else{
                        int status = jsonObject.getInt("status");
                        if (status == 0) {
                            mHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    mActivityFragmentView.viewLoading(View.GONE);
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
                                tv_shangbanTime.setText(clockInTime);
                                tv_xiabanTime.setText(clockOutTime);
                            }
                        });

                        String checkType = dataObject.optString("checkType");

                        //如果checkType 是这几个值i 直接显示下午布局
                        if("请假".equals(checkType)){
                            mHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    mActivityFragmentView.viewLoading(View.GONE);
                                    tv_shangbanTime.setText("无");
                                    tv_shangbanState.setText("请假");
                                    tv_xiabanTime.setText("无");
                                    tv_xiabanState.setText("请假");
                                }
                            });
                            return;
                        }else if("补签".equals(checkType)){
                            mHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    mActivityFragmentView.viewLoading(View.GONE);
                                    tv_shangbanTime.setText(shouldSBTime);
                                    tv_shangbanState.setText("补签");
                                    tv_xiabanTime.setText(shouldXBTime);
                                    tv_xiabanState.setText("补签");
                                }
                            });
                            return;
                        }else if("出差".equals(checkType)) {
                            mHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    mActivityFragmentView.viewLoading(View.GONE);
                                    tv_shangbanTime.setText(shouldSBTime);
                                    tv_shangbanState.setText("出差");
                                    tv_xiabanTime.setText(shouldXBTime);
                                    tv_xiabanState.setText("出差");
                                }
                            });
                            return;
                        }

                        //缺卡情况
                        if(("null").equals(clockInTime)){
                            mHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    tv_shangbanState.setText("缺卡");
                                    tv_shangbanTime.setText("无");
                                    //           pastdate_shangbanbuka.setVisibility(View.VISIBLE);
                                    mActivityFragmentView.viewLoading(View.GONE);
                                }
                            });
                        }

                        if(("null").equals(clockOutTime)){
                            mHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    tv_xiabanState.setText("缺卡");
                                    tv_xiabanTime.setText("无");
                                    //          pastdate_xiabanbuka.setVisibility(View.VISIBLE);
                                    mActivityFragmentView.viewLoading(View.GONE);
                                }
                            });
                        }

                        //上班迟到情况
                        if(!("null").equals(clockInTime)){
                            if(DateTimeUtils.equalsStringShangban(clockInTime,shouldSBTime)){
                                mHandler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        tv_shangbanState.setText("迟到");
                                    }
                                });

                            }
                        }

                        //下班早退情况
                        if(!("null").equals(clockOutTime)){
                            if(DateTimeUtils.equalsStringXiaban(clockOutTime,shouldXBTime)){
                                mHandler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        tv_xiabanState.setText("早退");
                                    }
                                });
                            }
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void httpRequestKaoqingofCurrentDateShangwu() {
        OkHttpClient okHttpClient = new OkHttpClient();
        Request.Builder builder = new Request.Builder();
        FormEncodingBuilder requestBodyBuilder = new FormEncodingBuilder();
        requestBodyBuilder.add("userId", uid);
        requestBodyBuilder.add("date", new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
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
                    //开始用String 来接收 放回 data出现Null的情况 ,导致布局无法显示
                    String data = jsonObject.getString("data");
                    JSONObject jsonObject1 = new JSONObject(data);
                    String checkInfo = jsonObject1.getString("ck");
                    final String shouldSBTime = jsonObject1.getString("ClockIn");
                    final String shouldXBTime = jsonObject1.getString("ClockOut");
                    final String middleTime = jsonObject1.getString("MiddleTime");
                    if (("null").equals(checkInfo)) {
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                tv_shangbanTime.setText("无");
                                tv_xiabanTime.setText("无");
                                tv_shangbanState.setText("缺卡");
                                tv_xiabanState.setText("缺卡");
                                mActivityFragmentView.viewLoading(View.GONE);
                            }
                        });
                    }else{
                        final JSONObject dataObject = jsonObject1.getJSONObject("ck");
                        final String clockInTime = dataObject.optString("clockInTime");
                        //这时的clockInTime是一个null字符串 ，不是null
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                tv_shangbanTime.setText(clockInTime);
                            }
                        });

                        //缺卡情况
                        if(("null").equals(clockInTime)||clockInTime==null){
                            mHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    tv_shangbanState.setText("缺卡");
                                    tv_shangbanTime.setText("无");
                                    //       xiaban_shangbanbuka.setVisibility(View.VISIBLE);
                                    mActivityFragmentView.viewLoading(View.GONE);
                                }
                            });
                        }

                        //上班迟到情况
                        if(!("null").equals(clockInTime)){
                            if(DateTimeUtils.equalsStringShangban(clockInTime,shouldSBTime)){
                                mHandler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        tv_shangbanTime.setText(clockInTime);
                                        tv_shangbanState.setText("迟到");
                                    }
                                });

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
     * 下载头像
     */
    public void httpLoadingHeadImage(){
        OkHttpClient okHttpClient = new OkHttpClient();
        Request.Builder builder = new Request.Builder();
        FormEncodingBuilder requestBodyBuilder = new FormEncodingBuilder();
        requestBodyBuilder.add("uid",uid);
        RequestBody requestBody = requestBodyBuilder.build();
        String request_url = ProtocolUrl.ROOT_URL + "/"+ ProtocolUrl.APP_LODAING_HEAD_IMAGE;
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
                    if (status == 1) {
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                mActivityFragmentView.viewLoading(View.GONE);
                                msg("下载错误");
                            }
                        });
                    }
                    if(status == 0){
                        final String imageName = jsonObject.optString("data");
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                // 使用DisplayImageOptions.Builder()创建DisplayImageOptions
                                options = new DisplayImageOptions.Builder()
                                        .showImageOnLoading(R.drawable.icon_logo) // 设置图片下载期间显示的图片
                                        .showImageForEmptyUri(R.drawable.icon_logo) // 设置图片Uri为空或是错误的时候显示的图片
                                        .showImageOnFail(R.drawable.icon_logo) // 设置图片加载或解码过程中发生错误显示的图片
                                        .cacheInMemory(true) // 设置下载的图片是否缓存在内存中
                                        .cacheOnDisk(true) // 设置下载的图片是否缓存在SD卡中
                                        //              .displayer(new RoundedBitmapDisplayer(20)) // 设置成圆角图片  如果使用这句代码，图片直接显示不出来。
                                        .build(); // 构建完成

                                imageLoader = ImageLoader.getInstance();
                                //imageLoader.init(ImageLoaderConfiguration.createDefault(mActivity));
                                String imageUri = ProtocolUrl.ROOT_URL + ProtocolUrl.APP_LODAING_HEAD_IMAGE_URI + imageName;
                                imageLoader.displayImage(imageUri, headImage, options);
                                mActivityFragmentView.viewLoading(View.GONE);
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
