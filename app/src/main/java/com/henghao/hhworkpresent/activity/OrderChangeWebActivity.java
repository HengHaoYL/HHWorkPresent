package com.henghao.hhworkpresent.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.henghao.hhworkpresent.ActivityFragmentSupport;
import com.henghao.hhworkpresent.R;
import com.henghao.hhworkpresent.entity.CkInspectrecord;
import com.henghao.hhworkpresent.entity.OrderChangeEntity;
import com.henghao.hhworkpresent.entity.SaveCheckTaskEntity;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 责令更改指令文书
 * Created by ASUS on 2017/9/21.
 */

public class OrderChangeWebActivity extends ActivityFragmentSupport {

    @ViewInject(R.id.scene_jiancha_webview)
    private WebView webView;

    @ViewInject(R.id.tv_scene_jiancha_save)
    private TextView tv_scene_jiancha_save;

    @ViewInject(R.id.tv_scene_jiancha_print)
    private TextView tv_scene_jiancha_print;

    private CkInspectrecord ckInspectrecord;

    private OrderChangeEntity orderChangeEntity;

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
        ckInspectrecord = (CkInspectrecord) getIntent().getSerializableExtra("ckInspectrecord");

        orderChangeEntity = new OrderChangeEntity();
        orderChangeEntity.setCheckUnit(ckInspectrecord.getCheckUnit());
        List<SaveCheckTaskEntity.JianchaMaterialEntityListBean> list = ckInspectrecord.getCheckYinhuanList();
        ArrayList<String> mDescriptList = new ArrayList<>();
        for(int i=0;i<list.size();i++){
            mDescriptList.add("("+(i+1)+")"+ list.get(i).getCheckDescript());
        }
        orderChangeEntity.setCheckYinhuanList(listToString(mDescriptList,'。'));
        orderChangeEntity.setCheckNum("");      //第几项
        orderChangeEntity.setCheckTime(ckInspectrecord.getCheckTime1());
        orderChangeEntity.setGovernmentName1("");
        orderChangeEntity.setMechanism("");
        orderChangeEntity.setCourtName("");
        orderChangeEntity.setCheckPeople1(ckInspectrecord.getCheckPeople1());
        orderChangeEntity.setDocumentsId1(ckInspectrecord.getDocumentsId1());
        orderChangeEntity.setCheckPeople2(ckInspectrecord.getCheckPeople2());
        orderChangeEntity.setDocumentsId2(ckInspectrecord.getDocumentsId2());
        orderChangeEntity.setBeCheckedPeople(ckInspectrecord.getBeCheckedPeople());
        orderChangeEntity.setRecordingTime(ckInspectrecord.getRecordingTime());

        webView.loadUrl("file:///android_asset/order_change.html");    //加载本地的html布局文件
        webView.setDrawingCacheEnabled(true);

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);   //启用javascript支持

        webView.addJavascriptInterface(new SceneJianchaService(orderChangeEntity), "order");//new类名，交互访问时使用的别名
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

        //http://www.jianshu.com/p/47ea6970d85d
        //Android WebView 调用JS方法获取返回值
        /**
         * 但该JS方法没有添加回调原生的函数时，我们是获取不到该方法的返回值
         * 那么我们就得换一种方式，直接调用JS的alert方法将JS方法的返回值提示给我们，alert出来的message就是我们要获取的返回值。
         * webView.loadUrl("javascript:alert(getTextData())");
         * 调用方法添加alert
         */
        webView.setWebChromeClient(new WebChromeClient() {

            @Override
            public boolean onJsAlert(WebView view, String url, String message,
                                     final JsResult result) {
                result.confirm();
                //将从html页面返回的json数据message解析成对象
                Gson gson = new Gson();
                OrderChangeEntity orderChangeEntity = gson.fromJson(message,OrderChangeEntity.class);
                saveOrderChangeDataToService(orderChangeEntity);
                return true;
            }
        });

    }

    /**
     * 上传文书数据到服务器
     * @param orderChangeEntity
     */
    public void saveOrderChangeDataToService(OrderChangeEntity orderChangeEntity){
        OkHttpClient okHttpClient = new OkHttpClient();
        Request.Builder builder = new Request.Builder();
        MultipartBuilder multipartBuilder = new MultipartBuilder();
        multipartBuilder.type(MultipartBuilder.FORM)
                .addFormDataPart("json", com.alibaba.fastjson.JSONObject.toJSONString(orderChangeEntity))//json数据
                .addFormDataPart("pid",SceneJianchaActivity.Pid)        //计划检查表的计划id
                .addFormDataPart("htmlUrl","order_change.html");        //文书web页面地址

        RequestBody requestBody = multipartBuilder.build();
        Request request = builder.post(requestBody).url("http://172.16.0.81:8080/istration/writData/addOrderChangeEntity").build();
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
                        msg("数据保存成功！");
                    }
                });
                Intent intent = new Intent();
                intent.setClass(OrderChangeWebActivity.this,XunchaJianchaActivity.class);
                startActivity(intent);
                finish();
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
            return JSONObject.toJSONString(orderChangeEntity);
        }
    }


    @OnClick({R.id.tv_scene_jiancha_save,R.id.tv_scene_jiancha_print})
    private void viewOnClick(View v) {
        switch (v.getId()) {
            case R.id.tv_scene_jiancha_save:        //保存
                webView.loadUrl("javascript:alert(getTextData())");
                //保存成功返回巡查检查界面 数据添加到我要复查列表
                break;
            case R.id.tv_scene_jiancha_print:       //打印
                break;
        }
    }

    /**
     * list转String字符串
     * @param list
     * @param separator
     * @return
     */
    public String listToString(ArrayList<String> list, char separator) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            sb.append(list.get(i)).append(separator);
        }
        return sb.toString().substring(0,sb.toString().length()-1);}
}
