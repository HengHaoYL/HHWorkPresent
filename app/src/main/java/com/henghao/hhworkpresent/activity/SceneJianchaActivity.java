package com.henghao.hhworkpresent.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.benefit.buy.library.phoneview.utils.FileUtils;
import com.henghao.hhworkpresent.ActivityFragmentSupport;
import com.henghao.hhworkpresent.R;
import com.henghao.hhworkpresent.entity.SceneJianchaEntity;
import com.henghao.hhworkpresent.views.CustomDialog;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Rectangle;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;


/**
 * 现场检查界面 也就是展示word页面
 * Created by wangqingbin on 2017/9/14.
 */

public class SceneJianchaActivity extends ActivityFragmentSupport {

    @ViewInject(R.id.scene_jiancha_webview)
    private WebView webView;

    @ViewInject(R.id.tv_scene_jiancha_save)
    private TextView tv_scene_jiancha_save;

    @ViewInject(R.id.tv_scene_jiancha_print)
    private TextView tv_scene_jiancha_print;

    public static Rectangle A4 = PageSize.A4;

    private SceneJianchaEntity sceneJianchaEntity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.mActivityFragmentView.viewMain(R.layout.activity_scene_jiancha);
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
        mLeftImageView.setVisibility(View.VISIBLE);
        initWithCenterBar();
        mCenterTextView.setText("现场检查");
        mCenterTextView.setVisibility(View.VISIBLE);

    }

    // //sdk17版本以上加上注解
    @SuppressLint({ "JavascriptInterface", "SetJavaScriptEnabled" })
    @Override
    public void initData() {
        super.initData();
        sceneJianchaEntity = (SceneJianchaEntity) getIntent().getSerializableExtra("sceneJianchaEntity");

        webView.loadUrl("file:///android_asset/scene.html");    //加载本地的html布局文件
        webView.setDrawingCacheEnabled(true);

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);   //启用javascript支持

        webView.addJavascriptInterface(new SceneJianchaService(sceneJianchaEntity), "scene");//new类名，交互访问时使用的别名
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        // 设置可以支持缩放
        webSettings.setSupportZoom(true);
        // 设置出现缩放工具
        webSettings.setBuiltInZoomControls(true);
        //扩大比例的缩放
        webSettings.setUseWideViewPort(true);
        //自适应屏幕
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webSettings.setLoadWithOverviewMode(true);

        webView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) { // 重写此方法表明点击网页里面的链接还是在当前的webview里跳转，不跳到浏览器那边
                view.loadUrl(url);
                return true;
            }
        });
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onJsAlert(WebView view, String url, String message,
                                     final JsResult result) {
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                result.confirm();
                return true;
            }
        });
    }

    /**
     * webview与html交互的实现类
     */
    public class SceneJianchaService{

        private Object printModel;

        public SceneJianchaService(Object printModel){
            this.printModel = printModel;
        }

        @JavascriptInterface
        public String jsontohtml(){
            //使用fastjson转json
            return JSONObject.toJSONString(sceneJianchaEntity);
        }
    }


    @OnClick({R.id.tv_scene_jiancha_save,R.id.tv_scene_jiancha_print})
    private void viewOnClick(View v) {
        switch (v.getId()) {
            case R.id.tv_scene_jiancha_save:        //保存
                showDialog();
                break;
            case R.id.tv_scene_jiancha_print:       //打印
                if(appIsInstalled(this,"")){

                }else{

                }
                break;
        }
    }

    /**
     * 点击保存弹出列表样式对话框
     */
    public void showDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("选择处理方式");
        //定义列表中的选项
        final String[] items = new String[]{
                "无隐患，归档",
                "存在隐患，责令更改",
                "存在违法，走一般程序处罚",
                "存在违法，走简易程序处罚",
        };
        //设置列表选项
        builder.setItems(items, new DialogInterface.OnClickListener() {
            //点击任何一个列表选项都会触发这个方法
            //which：点击的是哪一个选项
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case 0: //无隐患，归档
                        showWuyinghuanDialog();
                        break;
                    case 1: //存在隐患，责令更改
                        break;
                    case 2: //存在违法，走一般程序处罚
                        break;
                    case 3: //存在违法，走简易程序处罚
                        break;
                }
            }
        });
        // 取消选择
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.show();
    }

    /**
     * 展示无隐患归档的确认对话框
     */
    public void showWuyinghuanDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //设置对话框图标，可以使用自己的图片，Android本身也提供了一些图标供我们使用
        builder.setIcon(android.R.drawable.ic_dialog_alert);
        //设置对话框标题
        builder.setTitle("您选择的处理方式");
        //设置对话框内的文本
        builder.setMessage("无隐患，归档");
        //设置确定按钮，并给按钮设置一个点击侦听，注意这个OnClickListener使用的是DialogInterface类里的一个内部接口
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 执行点击确定按钮的业务逻辑
            }
        });
        //设置取消按钮
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 执行点击取消按钮的业务逻辑
            }
        });
        //使用builder创建出对话框对象
        AlertDialog dialog = builder.create();
        //显示对话框
        dialog.show();
    }


    /**
     * 安装apk文件
     */
    public void installApkFile(){
        Intent intent = new Intent(Intent.ACTION_VIEW);
        File file = getAssetFileToCacheDir(this,"xxx.apk");
        intent.setDataAndType(Uri.fromFile(file),"application/vnd.android.package-archive");
        startActivity(intent);
    }


    /**
     * 调用printershare打印pdf
     */
    public void printPdfFile(File pdf){
        Intent intent = new Intent();
        ComponentName comp = new ComponentName("com.dynamixsoftware.printershare","com.dynamixsoftware.printershare.ActivityPrintPDF");
        intent = new Intent();
        intent.setComponent(comp);
        intent.setAction("android.intent.action.VIEW");
        intent.setType("application/pdf");
        intent.setData(Uri.fromFile(pdf));
        startActivity(intent);
    }


    /**
     * 把Asset下的apk拷贝到sdcard下 /Android/data/你的包名/cache 目录下
     * @param context
     * @param fileName
     * @return
     */
    public static File getAssetFileToCacheDir(Context context, String fileName) {
        try {
            File cacheDir = getCacheDir(context);
            final String cachePath = cacheDir.getAbsolutePath()+ File.separator + fileName;
            InputStream is = context.getAssets().open(fileName);
            File file = new File(cachePath);
            file.createNewFile();
            FileOutputStream fos = new FileOutputStream(file);
            byte[] temp = new byte[1024];

            int i = 0;
            while ((i = is.read(temp)) > 0) {
                fos.write(temp, 0, i);
            }
            fos.close();
            is.close();
            return file;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取sd卡缓存目录
     * @param context
     * @return
     */
    public static File getCacheDir(Context context) {
        String APP_DIR_NAME = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Android/data/";
        File dir = new File(APP_DIR_NAME + context.getPackageName() + "/cache/");
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return dir;
    }

    /**
     * 判断apk是否安装
     * @param context
     * @param pageName
     * @return
     */
    public static boolean appIsInstalled(Context context, String pageName) {
        try {
            context.getPackageManager().getPackageInfo(pageName, 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }



}
