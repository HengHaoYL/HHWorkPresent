package com.henghao.hhworkpresent.activity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;

import com.henghao.hhworkpresent.ActivityFragmentSupport;
import com.henghao.hhworkpresent.R;
import com.henghao.hhworkpresent.views.DatabaseHelper;
import com.henghao.hhworkpresent.views.ProgressWebView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * 已发事宜
 * Created by bryanrady on 2017/5/3.
 */

public class FaqishiyiActivity extends ActivityFragmentSupport {

    @ViewInject(R.id.carapply_webview)
    private ProgressWebView progressWebView;

    @ViewInject(R.id.webview_layout)
    private RelativeLayout webview_layout;

    private ValueCallback mUploadMessage;
    public static final int FILECHOOSER_RESULTCODE = 10000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.mActivityFragmentView.viewMain(R.layout.activity_carapply);
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
        mLeftTextView.setText("已发事宜");
        mLeftTextView.setVisibility(View.VISIBLE);
        mLeftTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        initLoadingError();
        tv_viewLoadingError.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActivityFragmentView.viewLoadingError(View.GONE);
                webview_layout.setVisibility(View.VISIBLE);
                init();
            }
        });
    }

    @Override
    public void initData() {
        super.initData();
        init();
    }

    public String getUsername(){
        DatabaseHelper dbHelper = new DatabaseHelper(this,"user_login.db");
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String username = null;
        Cursor cursor = db.query("user",new String[]{"username"},null,null,null,null,null);
        // 将光标移动到下一行，从而判断该结果集是否还有下一条数据，如果有则返回true，没有则返回false
        while (cursor.moveToNext()){
            username = cursor.getString((cursor.getColumnIndex("username")));
        }
        return username;
    }

    public void init() {
        WebSettings webSettings = progressWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setDefaultTextEncodingName("utf-8");
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);//关闭WebView中缓存
        webSettings.setDomStorageEnabled(true);  // 开启 DOM storage API 功能
        webSettings.setRenderPriority(WebSettings.RenderPriority.HIGH);
        webSettings.setAllowFileAccess(true);  // 可以读取文件缓存(manifest生效)
        progressWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                System.out.println("Page开始  " + url + "   " + favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                System.out.println("Page结束  " + url);
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                //显示空白页，防止重新加载网页的时候又会显示错误界面再进行加载
                progressWebView.loadUrl("about:blank");
                webview_layout.setVisibility(View.GONE);
                mActivityFragmentView.viewLoadingError(View.VISIBLE);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //return super.shouldOverrideUrlLoading(view, url);
                view.loadUrl(url);
                return true;
            }
        });

        /**
         * 让webview支持文件上传
         */
        progressWebView.setWebChromeClient(new WebChromeClient(){
            public void openFileChooser(ValueCallback<Uri> uploadMsg) {
                Log.d("wangqingbin", "openFileChoose(ValueCallback<Uri> uploadMsg)");
                mUploadMessage = uploadMsg;
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.addCategory(Intent.CATEGORY_OPENABLE);
                i.setType("*/*");
                startActivityForResult(Intent.createChooser(i, "File Chooser"), FILECHOOSER_RESULTCODE);
            }

            public void openFileChooser( ValueCallback uploadMsg, String acceptType ) {
                Log.d("wangqingbin", "openFileChoose( ValueCallback uploadMsg, String acceptType )");
                mUploadMessage = uploadMsg;
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.addCategory(Intent.CATEGORY_OPENABLE);
                i.setType("*/*");
                startActivityForResult(
                        Intent.createChooser(i, "File Browser"),
                        FILECHOOSER_RESULTCODE);
            }
            public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture){
                Log.d("wangqingbin", "openFileChoose(ValueCallback<Uri> uploadMsg, String acceptType, String capture)");
                mUploadMessage = uploadMsg;
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.addCategory(Intent.CATEGORY_OPENABLE);
                i.setType("*/*");
                startActivityForResult( Intent.createChooser( i, "File Browser" ), FILECHOOSER_RESULTCODE );
            }
        });
        progressWebView.loadUrl("http://222.85.156.33:8082/hz7//resource/skins/bootstrap/view/datatables/view.datatables.jsp?" +
                "loginName="+getUsername()+"&viewid=HZ2881f84fafe789014fb016ac360067");
    }
}
