package com.henghao.hhworkpresent.activity;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.henghao.hhworkpresent.ActivityFragmentSupport;
import com.henghao.hhworkpresent.ProtocolUrl;
import com.henghao.hhworkpresent.R;
import com.henghao.hhworkpresent.entity.UpdateInfo;
import com.henghao.hhworkpresent.utils.CheckVersionTask;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import static com.henghao.hhworkpresent.utils.CheckVersionTask.getUpdataInfo;

/**
 * 关于应用
 * Created by bryanrady on 2017/5/4.
 */

public class AboutAppActivity extends ActivityFragmentSupport {

    @ViewInject(R.id.tv_device_info)
    private TextView tv_device_info;

    @ViewInject(R.id.tv_softversion_info)
    private TextView tv_softverion_info;

    @ViewInject(R.id.tv_system_version_info)
    private TextView tv_system_version_info;

    @ViewInject(R.id.tv_check_update)
    private TextView tv_check_update;

    private UpdateInfo info;
    private String XML_PATH = ProtocolUrl.ROOT_URL + ProtocolUrl.APP_UPDATE_XML_PASER_ADDRESS;//xml解析地址
    public Handler mHandler = new Handler(){};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.mActivityFragmentView.viewMain(R.layout.activity_about_app);
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
        mLeftTextView.setText("关于应用");
        mLeftTextView.setVisibility(View.VISIBLE);
    }

    @Override
    public void initData() {
        super.initData();
        tv_device_info.setText(android.os.Build.MODEL);
        tv_system_version_info.setText("Android  "+ android.os.Build.VERSION.RELEASE);
        tv_softverion_info.setText(getVersion(this));

    }

    /**
     * 获取版本号
     * @return 当前应用的版本号
     *
     */
    public String getVersion(Context context) {
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            return info.versionName;
        } catch (Exception e) {
            e.printStackTrace();
            return "获取版本失败";
        }
    }

    @OnClick({R.id.tv_check_update})
    private void viewOnClick(View v) {
        switch (v.getId()){
            case R.id.tv_check_update:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            URL url = new URL(XML_PATH);
                            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                            conn.setConnectTimeout(5000);
                            conn.connect();
                            InputStream is = conn.getInputStream();
                            info = getUpdataInfo(is);
                            if (info.getVersion().equals(getVersion(getApplication()))) {
                                mHandler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getApplication(), "当前已经是最新版本!", Toast.LENGTH_LONG).show();
                                    }
                                });
                            }else{
                                mHandler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        startVersionTask();
                                    }
                                });
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }).start();
                break;
        }
    }

    public void startVersionTask(){
        /**
         * 开启检测版本任务
         */
        CheckVersionTask checkVersionTask = new CheckVersionTask(this);
        checkVersionTask.start();
    }

}
