package com.henghao.hhworkpresent.activity;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TabHost;
import android.widget.TextView;

import com.benefit.buy.library.views.xlistview.XListView;
import com.henghao.hhworkpresent.ActivityFragmentSupport;
import com.henghao.hhworkpresent.R;
import com.henghao.hhworkpresent.entity.WoyaoJianchaEntity;
import com.henghao.hhworkpresent.utils.AnimationUtil;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * 巡查检查模块
 * Created by ASUS on 2017/9/4.
 */

public class XunchaJianchaActivity extends ActivityFragmentSupport {

    @ViewInject(R.id.gray_layout)
    private View mGrayLayout;

    private PopupWindow mPopupWindow;
    private boolean isPopWindowShowing = false;
    private int fromYDelta;

    private TabHost mTabHost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.mActivityFragmentView.viewMain(R.layout.activity_xunchajiancha);
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
        mLeftTextView.setText("我的任务");
        mLeftTextView.setVisibility(View.VISIBLE);
        initWithRightBar();
        mRightImageView.setImageResource(R.drawable.item_add);
        mRightImageView.setVisibility(View.VISIBLE);
        mRightImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isPopWindowShowing){
                    mPopupWindow.getContentView().startAnimation(AnimationUtil.createOutAnimation(XunchaJianchaActivity.this, fromYDelta));
                    mPopupWindow.getContentView().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mPopupWindow.dismiss();
                        }
                    },AnimationUtil.ANIMATION_OUT_TIME);
                }else{
                    showPopupWindow();
                }
            }
        });

        mTabHost =(TabHost) findViewById(android.R.id.tabhost);
        mTabHost.setup();

        //创建选项卡
        TabHost.TabSpec tab1 = mTabHost.newTabSpec("tab1")
                //创建标题卡片，参数一：指定标题卡片的文本内容，参数二：指定标题卡片的背景图片
        //        .setIndicator("我要检查", getResources().getDrawable(R.drawable.tab1))
                .setIndicator("我要检查")
                //将setContent（int）参数指定的组件（即面板）和上面的卡片标题进行绑定
                .setContent(R.id.fragment_woyao_check);
        //将上面创建好的一个选项卡（包括面板和卡片标题）添加到tabHost容器中
        mTabHost.addTab(tab1);


        //按照上面的方法创建剩余的三个选项卡，并进行添加
        TabHost.TabSpec tab2 = mTabHost.newTabSpec("tab2")
                .setIndicator("我要复查")
                .setContent(R.id.fragment_woyao_fucha);
        mTabHost.addTab(tab2);

        TabHost.TabSpec tab3 = mTabHost.newTabSpec("tab3")
                .setIndicator("调查取证")
                .setContent(R.id.fragment_obtain_statement);
        mTabHost.addTab(tab3);

        TabHost.TabSpec tab4 = mTabHost.newTabSpec("tab4")
                .setIndicator("历史记录")
                .setContent(R.id.fragment_history_record);
        mTabHost.addTab(tab4);

        //对黑色半透明背景做监听，点击时开始退出动画并将popupwindow dismiss掉
        mGrayLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isPopWindowShowing){
                    mPopupWindow.getContentView().startAnimation(AnimationUtil.createOutAnimation(XunchaJianchaActivity.this, fromYDelta));
                    mPopupWindow.getContentView().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mPopupWindow.dismiss();
                        }
                    },AnimationUtil.ANIMATION_OUT_TIME);
                }
            }
        });
    }

    @Override
    public void initData() {
        super.initData();
    }

    /**
     * http://blog.csdn.net/lnn368/article/details/51185732
     * 展示PopupWindow
     */
    private void showPopupWindow(){
        final View contentView= LayoutInflater.from(this).inflate(R.layout.selectlist,null);
        TextView t1= (TextView) contentView.findViewById(R.id.text1);
        mPopupWindow=new PopupWindow(contentView, LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
        //将这两个属性设置为false，使点击popupwindow外面其他地方不会消失
        mPopupWindow.setOutsideTouchable(false);
        mPopupWindow.setFocusable(false);
        mGrayLayout.setVisibility(View.VISIBLE);
        //获取popupwindow高度确定动画开始位置
        int contentHeight= com.henghao.hhworkpresent.utils.ViewUtils.getViewMeasuredHeight(contentView);
        mPopupWindow.showAsDropDown(mRightImageView);
        fromYDelta=-contentHeight - 50;
        mPopupWindow.getContentView().startAnimation(AnimationUtil.createInAnimation(this, fromYDelta));

        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                isPopWindowShowing=false;
                mGrayLayout.setVisibility(View.GONE);
            }
        });

        t1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(XunchaJianchaActivity.this,AddJianchaTaskActivity.class);
                startActivity(intent);
                mPopupWindow.dismiss();
            }
        });
        isPopWindowShowing=true;
    }
}
