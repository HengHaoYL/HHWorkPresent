package com.henghao.hhworkpresent.activity;

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
import com.henghao.hhworkpresent.utils.SqliteDBUtils;
import com.henghao.hhworkpresent.views.CircleImageView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
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

import static com.henghao.hhworkpresent.ProtocolUrl.APP_LODAING_HEAD_IMAGE_URI;

/**
 * Created by bryanrady on 2017/3/17.
 *
 * 考勤详情界面
 */

public class KaoqingDetailActivity extends ActivityFragmentSupport {

    private ImageLoader imageLoader;

    private DisplayImageOptions options;

    @ViewInject(R.id.kaoqingdetail_circleImageview)
    private CircleImageView headImage;

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
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.mActivityFragmentView.viewMain(R.layout.activity_kaoqingchidaodetail);
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
        mLeftTextView.setText("考勤详情");
        mLeftTextView.setVisibility(View.VISIBLE);
    }

    @Override
    public void initData() {
        super.initData();
        sqliteDBUtils = new SqliteDBUtils(this);

        httpLoadingHeadImage();
        tv_userName.setText(sqliteDBUtils.getLoginFirstName() + sqliteDBUtils.getLoginGiveName());

        /*Intent intent = getIntent();
        String currentDate = intent.getStringExtra("currentDate");
        String currentWeek = intent.getStringExtra("currentWeek");
        tv_currentDate.setText(currentDate);
        tv_currentWeek.setText(currentWeek);*/

        /**
         * 用这个接收数据是为了发送通知时好解析数据
         */
        Bundle bundle = getIntent().getExtras();
        String currentDate = bundle.getString("currentDate");
        String currentWeek = bundle.getString("currentWeek");
        tv_currentDate.setText(currentDate);
        tv_currentWeek.setText(currentWeek);

    }

    public void httpLoadingHeadImage(){
        OkHttpClient okHttpClient = new OkHttpClient();
        Request.Builder builder = new Request.Builder();
        FormEncodingBuilder requestBodyBuilder = new FormEncodingBuilder();
        requestBodyBuilder.add("uid",sqliteDBUtils.getLoginUid());
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
                                String imageUri = ProtocolUrl.ROOT_URL + APP_LODAING_HEAD_IMAGE_URI + imageName;
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

    @Override
    public void onResume() {
        super.onResume();
        httpRequestKaoqingDetailOfDate();
    }

    private Handler mHandler = new Handler(){};

    private void httpRequestKaoqingDetailOfDate() {
        OkHttpClient okHttpClient = new OkHttpClient();
        Request.Builder builder = new Request.Builder();
        FormEncodingBuilder requestBodyBuilder = new FormEncodingBuilder();
        requestBodyBuilder.add("userId", sqliteDBUtils.getLoginUid());
        requestBodyBuilder.add("date",tv_currentDate.getText().toString());
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
                    String data = jsonObject.getString("data");
                    JSONObject jsonObject1 = new JSONObject(data);
                    final String shouldSBTime = jsonObject1.getString("ClockIn");
                    final String shouldXBTime = jsonObject1.getString("ClockOut");
                    final String middleTime = jsonObject1.getString("MiddleTime");

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

                    //缺卡情况
                    if(("null").equals(clockInTime)){
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                tv_shangbanState.setText("缺卡");
                                tv_shangbanTime.setText("无");
                         //       btn_shangbanBuka.setVisibility(View.VISIBLE);
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
                         //       btn_xiabanBuka.setVisibility(View.VISIBLE);
                                mActivityFragmentView.viewLoading(View.GONE);
                            }
                        });
                    }

                    //上班迟到情况
                    if(!("null").equals(clockInTime)){
                        if(equalsStringShangban(clockInTime,shouldSBTime)){
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
                        if(equalsStringXiaban(clockOutTime,shouldXBTime)){
                            mHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    tv_xiabanState.setText("早退");
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
    public boolean equalsStringShangban(String clockInTime,String shouldSBTime){
        //定义一个标准时间 08:00
        //    int[] arr = {9,0,0};
        String[] strings = clockInTime.split(":");
        //    String[] shangTimes = shouldSBTime.split(":");
        int[] temp = new int[strings.length];
        //     int[] shangTime = new int[shangTimes.length];
        int[] shangTime ={9,10,0};
        //将字符数据转为int数组
        for (int i = 0; i < strings.length; i++) {
            temp[i]=Integer.parseInt(strings[i]);
        }
        /*for (int i = 0; i < shangTime.length; i++) {
            shangTime[i]=Integer.parseInt(shangTimes[i]);
        }*/
        //比较小时
        if (temp[0]>shangTime[0]) {
            return true;
        }
        if(temp[0]==shangTime[0]){
            //比较分钟
            if (temp[1]>shangTime[1]) {
                return true;
            }
            //如果分钟相等	9.0.0 , 9.0.0
            if (temp[1]==shangTime[1]) {
                //比较秒的用意，是为了对刚好在时间点打卡（如：9:00:00）的判断
                if (temp[2]>shangTime[2]) {
                    return true;
                }
            }

        }
        return false;
    }

    /**
     * 比较下班时间  早退返回true
     */
    public boolean equalsStringXiaban(String clockOutTime,String shouldXBTime){
        //定义一个标准时间
        //    int[] arr = {17,0,0};
        String[] strings = clockOutTime.split(":");
        //    String[] xiaTimes = shouldXBTime.split(":");
        int[] temp = new int[strings.length];
        //    int[] xiaTime = new int[xiaTimes.length];
        int[] xiaTime = {16,50,0};
        //将字符数据转为int数组
        for (int i = 0; i < strings.length; i++) {
            temp[i]=Integer.parseInt(strings[i]);
        }
        /*//将字符数据转为int数组
        for (int i = 0; i < xiaTime.length; i++) {
            xiaTime[i]=Integer.parseInt(xiaTimes[i]);
        }*/
        //只要是在18点之前，都属于早退，在18点之后，都属于正常下班
        if (temp[0]<xiaTime[0]) {
            return true;
        }
        return false;
    }
}
