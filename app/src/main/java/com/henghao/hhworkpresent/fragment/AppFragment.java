package com.henghao.hhworkpresent.fragment;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.henghao.hhworkpresent.FragmentSupport;
import com.henghao.hhworkpresent.ProtocolUrl;
import com.henghao.hhworkpresent.R;
import com.henghao.hhworkpresent.views.DatabaseHelper;
import com.henghao.hhworkpresent.views.ProgressWebView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;


/**
 * Created by bryanrady on 2017/2/28.
 */

public class AppFragment extends FragmentSupport {

    /*@ViewInject(R.id.tv_cheliangSP)
    private TextView cheliangSP;

    @ViewInject(tv_xingzhengSP)
    private TextView xingzhengSP;

    @ViewInject(tv_xingzhengZF)
    private TextView xingzhengZF;

    @ViewInject(tv_bangongRW)
    private TextView bangongRW;

    @ViewInject(R.id.view_pager)
    private AutoScrollViewPager viewPager;


    @ViewInject(R.id.indicator)
    private CirclePageIndicator indicator;

    private Intent intent;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        this.mActivityFragmentView.viewMain(R.layout.fragment_app);
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
        CommonAutoViewpager.viewPageAdapter(this.mActivity, viewPager, indicator);
    }

    private void initwithContent() {
        initWithCenterBar();
        this.mCenterTextView.setVisibility(View.VISIBLE);
        this.mCenterTextView.setText("应用");
    }

    @OnClick({R.id.tv_cheliangSP,tv_xingzhengSP,tv_bangongRW,tv_xingzhengZF})
    private void viewOnClick(View v){
        Intent intent = new Intent();
        switch (v.getId()){
            case R.id.tv_cheliangSP:
                intent.setClass(getActivity(), CheliangSPActivity.class);
                startActivity(intent);
                break;
            case tv_xingzhengSP:
                intent.setClass(getActivity(), XingZhenSPActivity.class);
                startActivity(intent);
                break;
            case tv_xingzhengZF:
                intent.setClass(getActivity(), XingZhenZFActivity.class);
                startActivity(intent);
                break;
            case tv_bangongRW:
                intent.setClass(getActivity(), BangGongRWActivity.class);
                startActivity(intent);
                break;
        }
    }*/

    @ViewInject(R.id.carapply_webview)
    private ProgressWebView progressWebView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        this.mActivityFragmentView.viewMain(R.layout.activity_carapply);
        this.mActivityFragmentView.viewEmpty(R.layout.activity_empty);
        this.mActivityFragmentView.viewEmptyGone();
        this.mActivityFragmentView.viewLoading(View.GONE);
        this.mActivityFragmentView.clipToPadding(true);
        ViewUtils.inject(this, this.mActivityFragmentView);
        initWidget();
        initData();
        return this.mActivityFragmentView;
    }

    public void initWidget(){
        initWithCenterBar();
        this.mCenterTextView.setVisibility(View.VISIBLE);
        this.mCenterTextView.setText("应用");
    }

    public void initData(){
        init();
    }

    public String getUsername(){
        DatabaseHelper dbHelper = new DatabaseHelper(this.mActivity,"user_login.db");
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
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //return super.shouldOverrideUrlLoading(view, url);
                view.loadUrl(url);
                return true;
            }
        });
        progressWebView.loadUrl(ProtocolUrl.ROOT_URL + "/"+ ProtocolUrl.APP_WORKFLOW_NOLOGIN + getUsername());
    }

    @Override
    public void onResume() {
        super.onResume();
        init();
    }
}
