package com.henghao.hhworkpresent.fragment;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.benefit.buy.library.phoneview.MultiImageSelectorActivity;
import com.benefit.buy.library.utils.tools.ToolsKit;
import com.henghao.hhworkpresent.FragmentSupport;
import com.henghao.hhworkpresent.ProtocolUrl;
import com.henghao.hhworkpresent.R;
import com.henghao.hhworkpresent.activity.LoginActivity;
import com.henghao.hhworkpresent.activity.MySelfZiliaoActivity;
import com.henghao.hhworkpresent.activity.MyTongxunluActivity;
import com.henghao.hhworkpresent.views.CircleImageView;
import com.henghao.hhworkpresent.views.DatabaseHelper;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
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
import java.util.ArrayList;
import java.util.List;

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

    @ViewInject(R.id.tv_exitlogin)
    private TextView tv_exitlogin;

    private ArrayList<String> mSelectPath;

    private static final int REQUEST_IMAGE = 0x00;

    private ArrayList<String> mImageList = new ArrayList<>();

    private DatabaseHelper dbHelper;
    private SQLiteDatabase db;

    private ArrayList<File> mFileList = new ArrayList<>();//被选中的用户头像文件

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
        initwithContent();
    }

    public void initData(){
        tv_loginUsername.setText("["+getLoginUsername()+"]");

        // 使用DisplayImageOptions.Builder()创建DisplayImageOptions
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.icon_logo) // 设置图片下载期间显示的图片
                .showImageForEmptyUri(R.drawable.icon_logo) // 设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(R.drawable.icon_logo) // 设置图片加载或解码过程中发生错误显示的图片
                .cacheInMemory(true) // 设置下载的图片是否缓存在内存中
                .cacheOnDisk(true) // 设置下载的图片是否缓存在SD卡中
                .displayer(new RoundedBitmapDisplayer(20)) // 设置成圆角图片
                .build(); // 构建完成

        imageLoader = ImageLoader.getInstance();

        //      imageLoader.init(ImageLoaderConfiguration.createDefault(mActivityFragmentSupport));

        String imageUri = ProtocolUrl.ROOT_URL + ProtocolUrl.APP_QUERY_GONGGAO_IMAGE + "图片url";
        imageLoader.displayImage(imageUri, circleImageView, options);

    }

    private void initwithContent() {
        initWithCenterBar();
        this.mCenterTextView.setVisibility(View.VISIBLE);
        this.mCenterTextView.setText("个人中心");
    }

    public void choosePicture(){
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
                           /* Bitmap bm = BitmapFactory.decodeFile(filePath);
                            //设置图片
                            circleImageView.setImageBitmap(bm);*/
                        }
                    }
                    /*if (!ToolsKit.isEmpty(this.mSelectPath)) {
                        for (String imagePath : mSelectPath) {
                            Bitmap bm = BitmapFactory.decodeFile(imagePath);
                            //设置图片
                            circleImageView.setImageBitmap(bm);
                            httpRequestHeadImage();
                        }
                    }*/

                }
            }
        }
    }

    /**
     * 从本地数据库读取登录用户名
     * @return
     */
    public String getLoginUsername(){
        DatabaseHelper dbHelper = new DatabaseHelper(this.mActivity,"user_login.db");
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.query("user",new String[]{"username"},null,null,null,null,null);
        String uid = null;
        String username = null;
        while (cursor.moveToNext()){
            username = cursor.getString((cursor.getColumnIndex("username")));
        }
        return username;
    }

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

    private Handler mHandler = new Handler(){};

    /**
     * 头像上传
     */
    public void httpRequestHeadImage(){
        String headImageUrl = mSelectPath.get(0);
        OkHttpClient okHttpClient = new OkHttpClient();
        Request.Builder builder = new Request.Builder();
        MultipartBuilder multipartBuilder = new MultipartBuilder();
        multipartBuilder.type(MultipartBuilder.FORM)
                .addFormDataPart("uid", getLoginUid());//用户ID
        for (File file : mFileList) {
            multipartBuilder.addFormDataPart("headImage", file.getName(), RequestBody.create(MediaType.parse("multipart/form-data"), file));
        }
        RequestBody requestBody = multipartBuilder.build();
        Log.d("wangqingbin","requesturl=="+ProtocolUrl.ROOT_URL + "/" + ProtocolUrl.APP_REQUEST_HEAD_IMAGE);
        Request request = builder.post(requestBody).url(ProtocolUrl.ROOT_URL + "/" + ProtocolUrl.APP_REQUEST_HEAD_IMAGE).build();
        mActivityFragmentView.viewLoading(View.VISIBLE);
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                Log.d("wangqingbin","上传失败");
                e.printStackTrace();
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mActivityFragmentView.viewLoading(View.GONE);
                        mActivity.msg("网络请求错误！");
                    }
                });
            }

            @Override
            public void onResponse(Response response) throws IOException {
                Log.d("wangqingbin","上传成功");
                String content = response.body().string();
                try {
                    JSONObject jsonObject = new JSONObject(content);
                    final String msg = jsonObject.getString("msg");
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            mActivityFragmentView.viewLoading(View.GONE);
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


    @OnClick({R.id.fragment_my_circleImageview,R.id.fragment_my_selfziliao,R.id.fragment_my_tongxunlu,R.id.tv_exitlogin,R.id.fragment_my_login_username})
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

            case R.id.tv_exitlogin:        //退出登录
                dbHelper = new DatabaseHelper(this.mActivity,"user_login.db");
                db = dbHelper.getReadableDatabase();
                Cursor cursor = db.query("user",new String[]{"uid"},null,null,null,null,null);
                String uid = null;
                while (cursor.moveToNext()){
                   uid = cursor.getString((cursor.getColumnIndex("uid")));
                }
                //删除用户信息
                db.delete("user","id=?",new String[]{uid});
                intent.setClass(mActivity,LoginActivity.class);
                startActivity(intent);
                break;

        }
    }
}
