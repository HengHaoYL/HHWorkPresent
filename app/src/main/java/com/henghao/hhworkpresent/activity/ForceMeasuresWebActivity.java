package com.henghao.hhworkpresent.activity;

import android.annotation.SuppressLint;
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
import com.henghao.hhworkpresent.entity.ForceMeasuresEntity;
import com.henghao.hhworkpresent.entity.OrderChangeEntity;
import com.henghao.hhworkpresent.entity.SaveCheckTaskEntity;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.util.ArrayList;
import java.util.List;

/**
 * 强制措施决定书文书页面
 * Created by ASUS on 2017/9/25.
 */

public class ForceMeasuresWebActivity extends ActivityFragmentSupport {

    @ViewInject(R.id.scene_jiancha_webview)
    private WebView webView;

    @ViewInject(R.id.tv_scene_jiancha_save)
    private TextView tv_scene_jiancha_save;

    @ViewInject(R.id.tv_scene_jiancha_print)
    private TextView tv_scene_jiancha_print;

    private CkInspectrecord ckInspectrecord;

    private ForceMeasuresEntity forceMeasuresEntity;

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
        mCenterTextView.setText("责令整改");
        mCenterTextView.setVisibility(View.VISIBLE);

    }

    // //sdk17版本以上加上注解
    @SuppressLint({ "JavascriptInterface", "SetJavaScriptEnabled" })
    @Override
    public void initData() {
        super.initData();
        ckInspectrecord = (CkInspectrecord) getIntent().getSerializableExtra("ckInspectrecord");

        forceMeasuresEntity = new ForceMeasuresEntity();
        forceMeasuresEntity.setCheckUnit(ckInspectrecord.getCheckUnit());
        List<SaveCheckTaskEntity.JianchaMaterialEntityListBean> list = ckInspectrecord.getCheckYinhuanList();
        ArrayList<String> mDescriptList = new ArrayList<>();
        for(int i=0;i<list.size();i++){
            mDescriptList.add("("+(i+1)+")"+ list.get(i).getCheckDescript());
        }
        forceMeasuresEntity.setCheckYinhuanList(listToString(mDescriptList,'；'));
        forceMeasuresEntity.setRecordingTime(ckInspectrecord.getRecordingTime());

        webView.loadUrl("file:///android_asset/order_change.html");    //加载本地的html布局文件
        webView.setDrawingCacheEnabled(true);

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);   //启用javascript支持

        webView.addJavascriptInterface(new SceneJianchaService(forceMeasuresEntity), "force_measures");//new类名，交互访问时使用的别名
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
                ForceMeasuresEntity forceMeasuresEntity = gson.fromJson(message,ForceMeasuresEntity.class);
        //        saveSceneJianchaDataToService(ckInspectrecord);
        //        saveOrderChangeDataToService(orderChangeEntity);
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
            return JSONObject.toJSONString(forceMeasuresEntity);
        }
    }

    @OnClick({R.id.tv_scene_jiancha_save,R.id.tv_scene_jiancha_print})
    private void viewOnClick(View v) {
        switch (v.getId()) {
            case R.id.tv_scene_jiancha_save:        //保存
                webView.loadUrl("javascript:alert(getTextData())");
                //还要把现场检查文书也保存
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
