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
    int fromYDelta;

    private TabHost mTabHost;

    @ViewInject(R.id.listview_woyaojiancha)
    private XListView woyaojiancha_listview;

    @ViewInject(R.id.listview_woyaofucha)
    private XListView woyaofucha_listview;

    @ViewInject(R.id.listview_diaochaquzheng)
    private XListView diaochaquzheng_listview;

    @ViewInject(R.id.listview_lishijilu)
    private XListView lishijilu_listview;

    private List<WoyaoJianchaEntity> mWoyaoJianchaData;

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

        //得到TabHost对象实例
        mTabHost =(TabHost) findViewById(R.id.xuncha_tabhost);
        //调用 TabHost.setup()
        mTabHost.setup();
        //创建Tab标签
        mTabHost.addTab(mTabHost.newTabSpec("one").setIndicator("我要检查").setContent(R.id.frame_woyaojiancha));
        mTabHost.addTab(mTabHost.newTabSpec("two").setIndicator("我要复查").setContent(R.id.frame_woyaofucha));
        mTabHost.addTab(mTabHost.newTabSpec("three").setIndicator("调查取证").setContent(R.id.frame_diaochaquzheng));
        mTabHost.addTab(mTabHost.newTabSpec("four").setIndicator("历史记录").setContent(R.id.frame_lishijilu));

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
        mWoyaoJianchaData = new ArrayList<WoyaoJianchaEntity>();
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
            }
        });
        isPopWindowShowing=true;
    }
}
