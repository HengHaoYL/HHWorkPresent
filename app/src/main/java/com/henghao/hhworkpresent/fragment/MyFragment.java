package com.henghao.hhworkpresent.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.benefit.buy.library.phoneview.MultiImageSelectorActivity;
import com.benefit.buy.library.utils.ToastUtils;
import com.benefit.buy.library.utils.tools.ToolsKit;
import com.henghao.hhworkpresent.Constant;
import com.henghao.hhworkpresent.FragmentSupport;
import com.henghao.hhworkpresent.ProtocolUrl;
import com.henghao.hhworkpresent.R;
import com.henghao.hhworkpresent.activity.AboutAppActivity;
import com.henghao.hhworkpresent.activity.AboutOurActivity;
import com.henghao.hhworkpresent.activity.LoginActivity;
import com.henghao.hhworkpresent.activity.MySelfZiliaoActivity;
import com.henghao.hhworkpresent.activity.MyTongxunluActivity;
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
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.henghao.hhworkpresent.ProtocolUrl.APP_LODAING_HEAD_IMAGE_URI;

/**
 * Created by bryanrady on 2017/2/28.
 */

public class MyFragment extends FragmentSupport {

    private ImageLoader imageLoader;

    private DisplayImageOptions options;

    /**
     * 圆形图片头像制作
     */
    @ViewInject(R.id.fragment_my_circleImageview)
    private CircleImageView circleImageView;

    @ViewInject(R.id.fragment_my_login_username)
    private TextView tv_loginUsername;

    @ViewInject(R.id.fragment_my_selfziliao)
    private TextView tv_selfziliao;

    @ViewInject(R.id.fragment_my_tongxunlu)
    private TextView tv_tongxunlu;

    @ViewInject(R.id.fragment_my_aboutus)
    private TextView tv_about_us;

    @ViewInject(R.id.fragment_my_aboutapp)
    private TextView tv_about_app;

    @ViewInject(R.id.tv_exitlogin)
    private TextView tv_exitlogin;

    private SqliteDBUtils sqliteDBUtils;

    private ArrayList<String> mSelectPath;

    private static final int REQUEST_IMAGE = 0x00;

    private ArrayList<String> mImageList = new ArrayList<>();

    private ArrayList<File> mFileList = new ArrayList<>();//被选中的用户头像文件

    private Handler mHandler = new Handler(){};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        this.mActivityFragmentView.viewMain(R.layout.fragment_myself);
        this.mActivityFragmentView.viewEmpty(R.layout.activity_empty);
        this.mActivityFragmentView.viewEmptyGone();
        this.mActivityFragmentView.viewLoading(View.GONE);
        ViewUtils.inject(this, this.mActivityFragmentView);
        initWidget();
        initData();
        return this.mActivityFragmentView;
    }

    public void initWidget(){
        initWithCenterBar();
        this.mCenterTextView.setVisibility(View.VISIBLE);
        this.mCenterTextView.setText(getString(R.string.hc_myself));
    }

    public void initData(){
        sqliteDBUtils = new SqliteDBUtils(mActivity);
        httpLoadingHeadImage();
        tv_loginUsername.setText(getString(R.string.left_brackets) + sqliteDBUtils.getLoginFirstName()
                + sqliteDBUtils.getLoginGiveName() + getString(R.string.right_brackets));
    }

    private void httpLoadingHeadImage(){
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
                                mActivity.msg(getString(R.string.download_error));
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
                                imageLoader.displayImage(imageUri, circleImageView, options);
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

    private void choosePicture(){
        // 查看session是否过期
        // int selectedMode = MultiImageSelectorActivity.MODE_SINGLE;
        //设置单选模式
        int selectedMode = MultiImageSelectorActivity.MODE_SINGLE;
        int maxNum = 9;
        Intent picIntent = new Intent(this.mActivity, MultiImageSelectorActivity.class);
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

    private String getImageName(String url) {
        return url.substring(url.lastIndexOf("/") + 1);
    }

    /**
     * 头像上传
     */
    private void httpRequestHeadImage(){
        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.setConnectTimeout(300, TimeUnit.SECONDS);
        okHttpClient.setWriteTimeout(300,TimeUnit.SECONDS);
        Request.Builder builder = new Request.Builder();
        MultipartBuilder multipartBuilder = new MultipartBuilder();
        multipartBuilder.type(MultipartBuilder.FORM)
                .addFormDataPart("uid", sqliteDBUtils.getLoginUid());//用户ID
        for (File file : mFileList) {
            for(String filePath : mSelectPath) {
                Bitmap bm = BitmapFactory.decodeFile(filePath);
                File outputFile=new File(filePath);
                try {
                    if (!outputFile.exists()) {
                        outputFile.getParentFile().mkdirs();
                    }else{
                        outputFile.delete();
                    }
                    FileOutputStream out = new FileOutputStream(outputFile);
                    bm.compress(Bitmap.CompressFormat.JPEG, 50, out);
                }catch (Exception e){
                    e.printStackTrace();
                }
                multipartBuilder.addFormDataPart("headImage", file.getName(), RequestBody.create(MediaType.parse("multipart/form-data"), outputFile));
            }
        }
        RequestBody requestBody = multipartBuilder.build();
        Request request = builder.post(requestBody).url(ProtocolUrl.ROOT_URL + "/" + ProtocolUrl.APP_REQUEST_HEAD_IMAGE).build();
        mActivityFragmentView.viewLoading(View.VISIBLE);
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                e.printStackTrace();
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mActivityFragmentView.viewLoading(View.GONE);
                        ToastUtils.show(mActivity,R.string.app_network_failure);
                    }
                });
            }

            @Override
            public void onResponse(Response response) throws IOException {
                String content = response.body().string();
                try {
                    JSONObject jsonObject = new JSONObject(content);
                    final String msg = jsonObject.getString("msg");
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            mActivityFragmentView.viewLoading(View.GONE);
                            if((getString(R.string.picture_upload_failred)).equals(msg)){
                                mActivity.msg(msg);
                                return;
                            }
                            for(String filePath : mSelectPath){
                                Bitmap bm = BitmapFactory.decodeFile(filePath);
                                //设置图片
                                circleImageView.setImageBitmap(bm);
                            }
                            mActivity.msg(msg);
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            if (requestCode == REQUEST_IMAGE) {
                if ((resultCode == Activity.RESULT_OK) || (resultCode == Activity.RESULT_CANCELED)) {
                    this.mSelectPath = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
                    if (!ToolsKit.isEmpty(this.mSelectPath)) {
                        List<String> fileNames = new ArrayList<>();
                        mImageList.clear();
                        mFileList.clear();
                        for (String filePath : mSelectPath) {
                            String imageName = getImageName(filePath);
                            fileNames.add(imageName);
                            File file = new File(filePath);
                            mFileList.add(file);
                            httpRequestHeadImage();
                        }
                    }
                }
            }
        }
    }

    @OnClick({R.id.fragment_my_circleImageview,R.id.fragment_my_selfziliao,R.id.fragment_my_tongxunlu,
            R.id.fragment_my_aboutus,R.id.fragment_my_aboutapp,R.id.tv_exitlogin,R.id.fragment_my_login_username})
    private void viewOnClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.fragment_my_circleImageview:
                choosePicture();
                break;

            case R.id.fragment_my_login_username:
                intent.setClass(mActivity, MySelfZiliaoActivity.class);
                startActivity(intent);
                break;

            case R.id.fragment_my_selfziliao:    //个人资料
                intent.setClass(mActivity, MySelfZiliaoActivity.class);
                startActivity(intent);
                break;

            case R.id.fragment_my_tongxunlu:        //通讯录
                intent.setClass(mActivity,MyTongxunluActivity.class);
                startActivity(intent);
                break;

            case R.id.fragment_my_aboutus:   //关于我们
                intent.setClass(mActivity,AboutOurActivity.class);
                startActivity(intent);
                break;

            case R.id.fragment_my_aboutapp:   //关于应用
                intent.setClass(mActivity,AboutAppActivity.class);
                startActivity(intent);
                break;

            case R.id.tv_exitlogin:        //退出登录
                //删除数据库
                mActivity.deleteDatabase(Constant.USER_LOGIN_DATABASE);

                //发送停止服务的广播
                intent.setAction(Constant.STOP_REALTIMESERVICE);
                mActivity.sendBroadcast(intent);

                intent.setClass(mActivity,LoginActivity.class);
                startActivity(intent);
                mActivity.finish();
                break;

        }
    }
}
