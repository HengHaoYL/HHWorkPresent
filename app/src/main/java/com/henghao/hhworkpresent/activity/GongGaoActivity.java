package com.henghao.hhworkpresent.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.TabHost;

import com.benefit.buy.library.http.query.callback.AjaxStatus;
import com.benefit.buy.library.views.xlistview.XListView;
import com.henghao.hhworkpresent.ActivityFragmentSupport;
import com.henghao.hhworkpresent.ProtocolUrl;
import com.henghao.hhworkpresent.R;
import com.henghao.hhworkpresent.adapter.NotificatReadGonggaoAdapter;
import com.henghao.hhworkpresent.adapter.NotificatUnReadGonggaoAdapter;
import com.henghao.hhworkpresent.entity.BaseEntity;
import com.henghao.hhworkpresent.entity.GonggaoEntity;
import com.henghao.hhworkpresent.protocol.GonggaoProtocol;
import com.henghao.hhworkpresent.utils.SqliteDBUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;


/**
 *
 * 通知公告界面
 * Created by bryanrady on 2017/3/1.
 */

public class GongGaoActivity extends ActivityFragmentSupport {

    private TabHost tabHost;

    @ViewInject(R.id.gonggao_read_lisview)
    private XListView read_listView;

    @ViewInject(R.id.gonggao_unread_listview)
    private XListView unread_listView;

    private NotificatReadGonggaoAdapter mReadAdapter;

    private NotificatUnReadGonggaoAdapter mUnReadAdaper;

    private List<GonggaoEntity> readData;

    private List<GonggaoEntity> unReadData;

    private GonggaoProtocol gonggaoProtocol = new GonggaoProtocol(this);

    private SqliteDBUtils sqliteDBUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.mActivityFragmentView.viewMain(R.layout.activity_gonggao);
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
        mLeftTextView.setText("公告");
        mLeftTextView.setVisibility(View.VISIBLE);

        initWithRightBar();
        mRightTextView.setText("全部已读");
        mRightTextView.setVisibility(View.VISIBLE);
        /*mRightTextView.setVisibility(View.GONE);
        mRightTextView.setText("发公告");
        mRightLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(GongGaoActivity.this, SendGonggaoActivity.class);
                startActivity(intent);
            }
        });*/


        //得到TabHost对象实例
        tabHost =(TabHost) findViewById(R.id.tabhost);
        //调用 TabHost.setup()
        tabHost.setup();
        //创建Tab标签
        tabHost.addTab(tabHost.newTabSpec("one").setIndicator("未读").setContent(R.id.frame_unread));
        tabHost.addTab(tabHost.newTabSpec("two").setIndicator("已读").setContent(R.id.frame_read));


    }

    @Override
    public void onResume() {
        super.onResume();
        queryUnReadGonggao();
    }

    public void queryUnReadGonggao(){
        gonggaoProtocol.addResponseListener(this);
        gonggaoProtocol.queryUnreadGongao(sqliteDBUtils.getLoginUid());
        mActivityFragmentView.viewLoading(View.VISIBLE);
    }

    public void queryReadGonggao(){
        gonggaoProtocol.addResponseListener(this);
        gonggaoProtocol.queryReadGongao(sqliteDBUtils.getLoginUid());
        mActivityFragmentView.viewLoading(View.VISIBLE);
    }

    /**
     * 将全部未读变为已读
     */
    public void addAllReadGonggao(){
        gonggaoProtocol.addResponseListener(this);
        gonggaoProtocol.addAllReadGonggao(sqliteDBUtils.getLoginUid());
        mActivityFragmentView.viewLoading(View.VISIBLE);
    }


    @Override
    public void initData() {
        super.initData();
        sqliteDBUtils = new SqliteDBUtils(this);
        readData = new ArrayList<>();
        unReadData = new ArrayList<>();
        mReadAdapter = new NotificatReadGonggaoAdapter(this, readData);
        mUnReadAdaper = new NotificatUnReadGonggaoAdapter(this,unReadData);
        read_listView.setAdapter(mReadAdapter);
        unread_listView.setAdapter(mUnReadAdaper);
        mReadAdapter.notifyDataSetChanged();
        mUnReadAdaper.notifyDataSetChanged();

        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                if (tabId.equals("one")) {
                    queryUnReadGonggao();
                    mRightTextView.setVisibility(View.VISIBLE);
                    mRightTextView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            addAllReadGonggao();
                            queryUnReadGonggao();
                        }
                    });
                } else if (tabId.equals("two")) {
                    mRightTextView.setVisibility(View.GONE);
                    queryReadGonggao();
                }
            }
        });

        unread_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String author = mUnReadAdaper.getItem(position-1).getGonggao_author();
                String title = mUnReadAdaper.getItem(position-1).getGonggao_titile();
                String date = mUnReadAdaper.getItem(position-1).getGonggao_sendDate();
                String content = mUnReadAdaper.getItem(position-1).getGonggao_content();

                Intent intent = new Intent();
                intent.setClass(GongGaoActivity.this,GonggaoDetailActivity.class);
                intent.putExtra("gongao_title",title);
                intent.putExtra("gongao_author",author);
                intent.putExtra("gongao_date",date);
                intent.putExtra("gongao_content",content);
                startActivity(intent);

                /**
                 * 将一条未读公告变为已读
                 */
                gonggaoProtocol.addResponseListener(GongGaoActivity.this);
                gonggaoProtocol.addReadGonggao(mUnReadAdaper.getItem(position-1).getGid(),sqliteDBUtils.getLoginUid());
                mActivityFragmentView.viewLoading(View.VISIBLE);
            }
        });

        read_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String author = mReadAdapter.getItem(position-1).getGonggao_author();
                String title = mReadAdapter.getItem(position-1).getGonggao_titile();
                String date = mReadAdapter.getItem(position-1).getGonggao_sendDate();
                String content = mReadAdapter.getItem(position-1).getGonggao_content();

                Intent intent = new Intent();
                intent.setClass(GongGaoActivity.this,GonggaoDetailActivity.class);
                intent.putExtra("gongao_title",title);
                intent.putExtra("gongao_author",author);
                intent.putExtra("gongao_date",date);
                intent.putExtra("gongao_content",content);
                startActivity(intent);
            }
        });
    }

    @Override
    public void OnMessageResponse(String url, Object jo, AjaxStatus status) throws JSONException {
        super.OnMessageResponse(url, jo, status);
        if (url.endsWith(ProtocolUrl.APP_QUERY_UNREAD_GONGGAO)) {
            if (jo instanceof BaseEntity) {
          //      BaseEntity mData = (BaseEntity) jo;
                return;
            }
            List<GonggaoEntity> homedata = (List<GonggaoEntity>) jo;
            unReadData.clear();
            unReadData.addAll(homedata);
            Log.d("wangqingbin","homedata.size=="+homedata.size());
            mUnReadAdaper.notifyDataSetChanged();
        } else if(url.endsWith(ProtocolUrl.APP_QUERY_READ_GONGGAO)){
            if (jo instanceof BaseEntity) {
                //      BaseEntity mData = (BaseEntity) jo;
                return;
            }
            List<GonggaoEntity> homedata = (List<GonggaoEntity>) jo;
            readData.clear();
            readData.addAll(homedata);
            mReadAdapter.notifyDataSetChanged();

        }
    }
}
