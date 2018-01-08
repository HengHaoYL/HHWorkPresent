package com.henghao.hhworkpresent.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.SDKInitializer;
import com.benefit.buy.library.phoneview.MultiImageSelectorActivity;
import com.benefit.buy.library.utils.tools.ToolsKit;
import com.google.gson.Gson;
import com.henghao.hhworkpresent.ActivityFragmentSupport;
import com.henghao.hhworkpresent.ProtocolUrl;
import com.henghao.hhworkpresent.R;
import com.henghao.hhworkpresent.entity.MeetingEntity;
import com.henghao.hhworkpresent.entity.MeetingTrajectoryEntity;
import com.henghao.hhworkpresent.utils.LocationUtils;
import com.henghao.hhworkpresent.utils.SqliteDBUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;

/**
 * 会议现场签到界面
 * Created by bryanrady on 2017/10/23.
 */

public class MeetingQiandaoActivity extends ActivityFragmentSupport {

    @ViewInject(R.id.tv_meeting_qiandao_theme)
    private TextView tv_meeting_qiandao_theme;

    @ViewInject(R.id.tv_meeting_qiandao_place)
    private TextView tv_meeting_qiandao_place;

    @ViewInject(R.id.tv_meeting_qiandao_start_time)
    private TextView tv_meeting_qiandao_start_time;

    @ViewInject(R.id.tv_meeting_qiandao_duration)
    private TextView tv_meeting_qiandao_duration;

    @ViewInject(R.id.tv_meeting_qiandao_people_num)
    private TextView tv_meeting_qiandao_people_num;

    @ViewInject(R.id.image_meeting_qiandao_start)
    private ImageView image_meeting_qiandao_start;

/*    @ViewInject(R.id.image_meeting_qiandao_end)
    private ImageView image_meeting_qiandao_end;*/

    @ViewInject(R.id.tv_qiandao_start_text)
    private TextView tv_qiandao_start_text;

    @ViewInject(R.id.tv_qiandao_start_time)
    private TextView tv_qiandao_start_time;

/*    @ViewInject(R.id.tv_qiandao_end_text)
    private TextView tv_qiandao_end_text;

    @ViewInject(R.id.tv_qiandao_end_time)
    private TextView tv_qiandao_end_time;*/

    @ViewInject(R.id.meeting_qiandao_linear1)
    private LinearLayout meeting_qiandao_linear1;

    @ViewInject(R.id.meeting_qiandao_linearlayout)
    private LinearLayout meeting_qiandao_linearlayout;

    @ViewInject(R.id.meeting_qiandao_linear2)
    private LinearLayout meeting_qiandao_linear2;

    private MeetingEntity meetingEntity;

    private MeetingTrajectoryEntity meetingTrajectoryEntity;

    private SqliteDBUtils sqliteDBUtils;

    private int mid;

    private long msg_id;

    private int isEnd;

    @ViewInject(R.id.et_meeting_upload_summary)
    private EditText et_meeting_upload_summary;

    @ViewInject(R.id.tv_meeting_upload_save)
    private TextView tv_meeting_upload_save;

    @ViewInject(R.id.tv_meeting_upload_cancel)
    private TextView tv_meeting_upload_cancel;

    @ViewInject(R.id.upload_meeting_picture_gridview)
    private GridView upload_meeting_picture_gridview;

    private GridAdapter adapter;

    private ArrayList<String> mSelectPath;

    private static final int REQUEST_IMAGE = 0x00;

    private ArrayList<String> mImageList=new ArrayList<>();

    public static ArrayList<File> mMeetingFileList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        SDKInitializer.initialize(getApplicationContext());
        LocationUtils.Location(getApplicationContext());
        this.mActivityFragmentView.viewMain(R.layout.activity_meeting_qiandao);
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
        mLeftTextView.setVisibility(View.VISIBLE);
        initWithCenterBar();
        mCenterTextView.setText("会议签到");
        mCenterTextView.setVisibility(View.VISIBLE);

        initWithRightBar();
        sqliteDBUtils = new SqliteDBUtils(this);
        Intent intent = getIntent();
        msg_id = intent.getLongExtra("msg_id",0);
        httpRequestMeetingContent();

        mRightTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                httpChangeIsEndStatus();
            }
        });
    }

    @Override
    public void initData() {
        super.initData();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
        String currentDate = simpleDateFormat.format(new Date());
        tv_qiandao_start_time.setText(currentDate);

        adapter = new GridAdapter();
        upload_meeting_picture_gridview.setAdapter(adapter);

        meetingEntity = (MeetingEntity) getIntent().getSerializableExtra("meetingEntity");
        tv_meeting_qiandao_theme.setText(meetingEntity.getMeetingTheme());
        tv_meeting_qiandao_place.setText(meetingEntity.getMeetingPlace());
        tv_meeting_qiandao_start_time.setText(meetingEntity.getMeetingStartTime());
        tv_meeting_qiandao_duration.setText(meetingEntity.getMeetingDuration());
        tv_meeting_qiandao_people_num.setText(meetingEntity.getUserIds());

        meetingTrajectoryEntity = (MeetingTrajectoryEntity) getIntent().getSerializableExtra("meetingTrajectoryEntity");
        if(meetingTrajectoryEntity.getStartSignInTime()==null && meetingTrajectoryEntity.getEndSignInTime()==null){   //两个都是null,表示还没有进场签到
            tv_qiandao_start_text.setText("进场签到");
        }
        if(meetingTrajectoryEntity.getStartSignInTime()!=null && meetingTrajectoryEntity.getEndSignInTime()==null){
            tv_qiandao_start_text.setText("退场签到");
        }
        if(meetingTrajectoryEntity.getStartSignInTime()!=null && meetingTrajectoryEntity.getEndSignInTime()!=null
                && meetingTrajectoryEntity.getMeetingSummary()==null){
            meeting_qiandao_linear1.setVisibility(View.GONE);
            meeting_qiandao_linearlayout.setVisibility(View.GONE);
            meeting_qiandao_linear2.setVisibility(View.VISIBLE);
        }
    }

    @OnClick({R.id.image_meeting_qiandao_start,R.id.image_meeting_qiandao_end,R.id.tv_meeting_upload_save,R.id.tv_meeting_upload_cancel})
    private void viewOnClick(View v) {
        switch (v.getId()){
            case R.id.image_meeting_qiandao_start:  //进场签到
                onClickQianDao();
                break;
            case R.id.image_meeting_qiandao_end:    //退场签到
                break;
            case R.id.tv_meeting_upload_save:
                if(et_meeting_upload_summary.getText().toString().equals("")){
                    Toast.makeText(this,"必须填写会议纪要",Toast.LENGTH_SHORT).show();
                    return;
                }
                httpUploadSummaryAndSiteFile(meetingEntity.getMid(),sqliteDBUtils.getLoginUid(),et_meeting_upload_summary.getText().toString());
                break;
            case R.id.tv_meeting_upload_cancel:
                finish();
                break;
        }
    }

    //检查连接的是什么网络
    public  Integer checkWifi(Context context) {
        ConnectivityManager ConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mNetworkInfo =  ConnectivityManager.getActiveNetworkInfo();
        if (mNetworkInfo.getState() == NetworkInfo.State.CONNECTED) {
            if (mNetworkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {     //返回1，连接的是移动网络
                return 1;
            } else if (mNetworkInfo.getType() == ConnectivityManager.TYPE_WIFI) {  //返回2，连接的是wifi
                return 2;
            }
        } else {     //返回3，没有连接。
            return 3;
        }
        return 3;
    }

    //获取IP
    public String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (SocketException ex) {
            Log.e("IP 地址为：", ex.toString());
        }
        return null;
    }

    //获取Wifi的SSID
    public String getWifiSSID() {
        WifiManager wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifi.getConnectionInfo();
        return info.getSSID();
    }

    public void onClickQianDao(){
        if(checkWifi(this)== 1){
            Toast.makeText(this,"你当前连接的移动网络，不能签到。",Toast.LENGTH_SHORT).show();
        } else if(checkWifi(this)== 3){
            Toast.makeText(this, "你当前没有连接wifi网络，签退失败！", Toast.LENGTH_LONG).show();
        } else if(checkWifi(this)== 2){
            String ssid = "\"" + meetingEntity.getWifiSSID() + "\"";
            if(getWifiSSID().equals(ssid)){
                if(tv_qiandao_start_text.getText().toString().equals("进场签到")){
                    if(isEnd == 1){
                        Toast.makeText(this, "会议已经被结束", Toast.LENGTH_LONG).show();
                        return;
                    }
                    String lat = String.valueOf(LocationUtils.getLat());
                    String lng = String.valueOf(LocationUtils.getLng());
                    if(lat==null && lng==null){
                        Toast.makeText(this,"当前没有获取到你的位置信息，请重新点击!",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    httpOnClickStartQiandaoUploadData(meetingEntity.getMid(),sqliteDBUtils.getLoginUid(),LocationUtils.getLat()+","+LocationUtils.getLng());
                }else if (tv_qiandao_start_text.getText().toString().equals("退场签到")){
                    if(isEnd == 1){
                        Toast.makeText(this, "会议已经被结束", Toast.LENGTH_LONG).show();
                        return;
                    }
                    String lat = String.valueOf(LocationUtils.getLat());
                    String lng = String.valueOf(LocationUtils.getLng());
                    if(lat==null && lng==null){
                        Toast.makeText(this,"当前没有获取到你的位置信息，请重新点击!",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    httpOnClickEndQiandaoUploadData(meetingEntity.getMid(),sqliteDBUtils.getLoginUid(),LocationUtils.getLat()+","+LocationUtils.getLng());
                }
            }
        }
    }

    /**
     * 进场签到更新数据
     */
    public void httpOnClickStartQiandaoUploadData(int mid, String userId, String startSignInCoordinates){
        OkHttpClient okHttpClient = new OkHttpClient();
        Request.Builder builder = new Request.Builder();
        MultipartBuilder multipartBuilder = new MultipartBuilder();
        multipartBuilder.type(MultipartBuilder.FORM)
                .addFormDataPart("mid", String.valueOf(mid))
                .addFormDataPart("userId", userId)
                .addFormDataPart("startSignInCoordinates", startSignInCoordinates);
        RequestBody requestBody = multipartBuilder.build();
        String requestUrl = ProtocolUrl.ROOT_URL + ProtocolUrl.APP_UPDATE_MEETING_ONLICK_START;
        Request request = builder.post(requestBody).url(requestUrl).build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e){
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        msg("网络请求错误！");
                    }
                });
            }

            @Override
            public void onResponse(Response response) throws IOException {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        msg("签到成功");
                        tv_qiandao_start_text.setText("退场签到");
                    }
                });
            }
        });
    }

    /**
     * 退场签到更新数据
     */
    public void httpOnClickEndQiandaoUploadData(int mid, String userId, String endSignInCoordinates){
        OkHttpClient okHttpClient = new OkHttpClient();
        Request.Builder builder = new Request.Builder();
        MultipartBuilder multipartBuilder = new MultipartBuilder();
        multipartBuilder.type(MultipartBuilder.FORM)
                .addFormDataPart("mid", String.valueOf(mid))
                .addFormDataPart("userId", userId)
                .addFormDataPart("endSignInCoordinates", endSignInCoordinates);
        RequestBody requestBody = multipartBuilder.build();
        String requestUrl = ProtocolUrl.ROOT_URL + ProtocolUrl.APP_UPDATE_MEETING_ONLICK_END;
        Request request = builder.post(requestBody).url(requestUrl).build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e){
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        msg("网络请求错误！");
                    }
                });
            }

            @Override
            public void onResponse(Response response) throws IOException {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        msg("签退成功");
                        tv_qiandao_start_text.setText("退场签到");
                        meeting_qiandao_linear1.setVisibility(View.GONE);
                        meeting_qiandao_linearlayout.setVisibility(View.GONE);
                        meeting_qiandao_linear2.setVisibility(View.VISIBLE);
                    }
                });
            }
        });
    }

    /**
     * 上传工作纪要和现场图片
     */
    public void httpUploadSummaryAndSiteFile(int mid, String userId, String meetingSummary){
        OkHttpClient okHttpClient = new OkHttpClient();
        Request.Builder builder = new Request.Builder();
        MultipartBuilder multipartBuilder = new MultipartBuilder();
        multipartBuilder.type(MultipartBuilder.FORM)
                .addFormDataPart("mid", String.valueOf(mid))
                .addFormDataPart("userId", userId)
                .addFormDataPart("meetingSummary", meetingSummary);
        if(mMeetingFileList.size()==0){
            Toast.makeText(this,"必须上传会议图片",Toast.LENGTH_SHORT).show();
            return;
        }
        for (File file : mMeetingFileList) {
            //上传现场图片
            multipartBuilder.addFormDataPart("files", file.getName(), RequestBody.create(MediaType.parse("multipart/form-data"),file));
        }
        RequestBody requestBody = multipartBuilder.build();
        String requestUrl = ProtocolUrl.ROOT_URL + ProtocolUrl.APP_UPDATE_MEETING_SUMMARY_FILE;
        Request request = builder.post(requestBody).url(requestUrl).build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e){
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        msg("网络请求错误！");
                    }
                });
            }

            @Override
            public void onResponse(Response response) throws IOException {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        msg("数据上传成功！");
                        finish();
                    }
                });
            }
        });
    }

    /**
     * 查询指定的消息
     */
    public void httpRequestMeetingContent(){
        OkHttpClient okHttpClient = new OkHttpClient();
        Request.Builder builder = new Request.Builder();
        MultipartBuilder multipartBuilder = new MultipartBuilder();
        multipartBuilder.type(MultipartBuilder.FORM)
                .addFormDataPart("msg_id", String.valueOf(msg_id))
                .addFormDataPart("uid", sqliteDBUtils.getLoginUid());
        RequestBody requestBody = multipartBuilder.build();
        String request_url = ProtocolUrl.ROOT_URL + ProtocolUrl.APP_QUERY_TUI_SONG_MESSAGE;
        Request request = builder.post(requestBody).url(request_url).build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                runOnUiThread(new Runnable() {
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
                    if(status==0) {
                        result_str = jsonObject.getString("data");
                        Gson gson = new Gson();
                        meetingEntity = gson.fromJson(result_str,MeetingEntity.class);
                        mid = meetingEntity.getMid();
                        meetingTrajectoryEntity = meetingEntity.getMeetingTrajectoryEntity();
                        final String faqirenId = meetingEntity.getUid();
                        isEnd = meetingEntity.getIsEnd();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if(isEnd == 0 && faqirenId.equals(sqliteDBUtils.getLoginUid())){
                                    mRightTextView.setText("结束会议");
                                    mRightTextView.setVisibility(View.VISIBLE);
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
     * 改变会议状态 是否结束
     */
    private void httpChangeIsEndStatus(){
        OkHttpClient okHttpClient = new OkHttpClient();
        Request.Builder builder = new Request.Builder();
        MultipartBuilder multipartBuilder = new MultipartBuilder();
        multipartBuilder.type(MultipartBuilder.FORM)
                .addFormDataPart("mid", String.valueOf(mid));
        RequestBody requestBody = multipartBuilder.build();
        String requestUrl = ProtocolUrl.ROOT_URL + ProtocolUrl.APP_UPLOAD_MEETING_ISEND;
        Request request = builder.post(requestBody).url(requestUrl).build();
        Call call = okHttpClient.newCall(request);
        mActivityFragmentView.viewLoading(View.VISIBLE);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e){
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mActivityFragmentView.viewLoading(View.GONE);
                        msg("网络请求错误！");
                    }
                });
            }

            @Override
            public void onResponse(Response response) throws IOException {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        msg("会议结束！");
                        mActivityFragmentView.viewLoading(View.GONE);
                        mRightTextView.setVisibility(View.GONE);
                        finish();
                    }
                });
            }
        });
    }


    public void choosePicture(){
        int selectedMode = MultiImageSelectorActivity.MODE_MULTI;
        int maxNum = 6;
        Intent picIntent = new Intent(this, MultiImageSelectorActivity.class);
        // 是否显示拍摄图片
        picIntent.putExtra(MultiImageSelectorActivity.EXTRA_SHOW_CAMERA, true);
        // 最大可选择图片数量
        picIntent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_COUNT, maxNum);
        // 选择模式
        picIntent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_MODE, selectedMode);
        // 默认选择
        if ((this.mSelectPath != null) && (this.mSelectPath.size() > 0)) {
            picIntent.putExtra(MultiImageSelectorActivity.EXTRA_DEFAULT_SELECTED_LIST, this.mSelectPath);
        }
        startActivityForResult(picIntent, REQUEST_IMAGE);
    }

    @SuppressWarnings("static-access")
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            if (requestCode == REQUEST_IMAGE) {
                if ((resultCode == Activity.RESULT_OK) || (resultCode == Activity.RESULT_CANCELED)) {
                    this.mSelectPath = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
                    if (!ToolsKit.isEmpty(this.mSelectPath)) {
                        mImageList.clear();
                        mMeetingFileList.clear();
                        for (String filePath : mSelectPath) {  //mSelectPath是被选中的所有图片路径集合
                            File file = new File(filePath);
                            mMeetingFileList.add(file);  //mFileList用来存储被选中的图片文件，不是路径
                            if(!mImageList.contains(filePath)){
                                mImageList.add(filePath);
                                upload_meeting_picture_gridview.setAdapter(adapter);
                            }
                        }
                    }
                }
            }
        }
    }

    class GridAdapter extends BaseAdapter {

        public LayoutInflater layoutInflater = LayoutInflater.from(context);
        @Override
        public int getCount() {
            return mImageList.size()+ 1;
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null ;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = layoutInflater.inflate(R.layout.gridview_picture_item, null);
                holder.image = (ImageView) convertView.findViewById(R.id.imageView);
                holder.btn = (Button) convertView.findViewById(R.id.delete);

                holder.image.setScaleType(ImageView.ScaleType.CENTER_CROP);

                convertView.setTag(holder);
            }
            else{
                holder = (ViewHolder) convertView.getTag();
            }
            if (position == mImageList.size()) {

                holder.image.setImageBitmap(BitmapFactory.decodeResource(
                        context.getResources(), R.drawable.item_add_picture));
                holder.btn.setVisibility(View.GONE);

                holder.image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        choosePicture();
                    }
                });
            } else {
                //做压缩处理后就不会爆出OOM异常
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                Bitmap bm = BitmapFactory.decodeFile(mSelectPath.get(position),options);
                // 获取到这个图片的原始宽度和高度  
                int picWidth = options.outWidth;
                int picHeight = options.outHeight;
                // 获取屏的宽度和高度  
                WindowManager windowManager = getWindowManager();
                Display display = windowManager.getDefaultDisplay();
                int screenWidth = display.getWidth();
                int screenHeight = display.getHeight();
                // isSampleSize是表示对图片的缩放程度，比如值为2图片的宽度和高度都变为以前的1/2  
                options.inSampleSize = 2;
                // 根据屏的大小和图片大小计算出缩放比例  
                if(picWidth > picHeight){
                    if(picWidth > screenWidth)
                        options.inSampleSize = picWidth/screenWidth;
                }else{
                    if (picHeight > screenHeight)
                        options.inSampleSize = picHeight/screenHeight;
                }
                // 这次再真正地生成一个有像素的，经过缩放了的bitmap  
                options.inJustDecodeBounds = false;
                bm = BitmapFactory.decodeFile(mSelectPath.get(position),options);
                //将图片显示到ImageView中
                holder.image.setImageBitmap(bm);
                holder.btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //点击后移除图片
                        mImageList.remove(position);
                        mSelectPath.remove(position);

                        //更新UI
                        upload_meeting_picture_gridview.setAdapter(adapter);
                    }
                });

            }
            return convertView;
        }
    }

    public class ViewHolder {
        public ImageView image;
        public Button btn ;
    }

}
