package com.henghao.hhworkpresent.activity;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import com.henghao.hhworkpresent.ActivityFragmentSupport;
import com.henghao.hhworkpresent.R;
import com.henghao.hhworkpresent.adapter.EmailGroupAdapter;
import com.lidroid.xutils.ViewUtils;

/**
 * Created by bryanrady on 2017/3/1.
 * 邮箱界面
 */

public class EmailActivity extends ActivityFragmentSupport {

/*    @ViewInject(R.id.email_listview)
    private XListView mXlistView;*/
    //日期
    private String[] group = new String[] { "2月18日", "2月13日", "2月09日"};
    //消息标题
    private String[][] buddy = new String[][] {
            { "智联招聘", "QQ邮箱管理员", "QQ会员"},
            {"拉勾消息","QQ会员" },
            { "拉勾消息" }};

    /*带分组的listview*/
    private ExpandableListView expandablelistview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.mActivityFragmentView.viewMain(R.layout.activity_email);
        this.mActivityFragmentView.viewEmpty(R.layout.activity_empty);
        this.mActivityFragmentView.viewEmptyGone();
        this.mActivityFragmentView.viewLoading(View.GONE);
        this.mActivityFragmentView.clipToPadding(true);
        ViewUtils.inject(this, this.mActivityFragmentView);
        setContentView(this.mActivityFragmentView);
        initData();
        initWidget();
    }

    @Override
    public void initData() {
        super.initData();
        /*List<String> mList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            mList.add("测试");
        }
        EmailAdapter mAdapter = new EmailAdapter(this, mList);
        mXlistView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();*/
        expandablelistview= (ExpandableListView) findViewById(R.id.buddy_expandablelistview);
        ExpandableListAdapter adapter=new EmailGroupAdapter(this,group,buddy);
        expandablelistview.setAdapter(adapter);

        //分组展开
        expandablelistview.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener(){
            public void onGroupExpand(int groupPosition) {
            }
        });
        //分组关闭
        expandablelistview.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener(){
            public void onGroupCollapse(int groupPosition) {
            }
        });
        //子项单击
        expandablelistview.setOnChildClickListener(new ExpandableListView.OnChildClickListener(){
            public boolean onChildClick(ExpandableListView arg0, View arg1,
                    int groupPosition, int childPosition, long arg4) {
                return false;
            }
        });

    }

    @Override
    public void initWidget() {
        super.initWidget();
        initWithBar();
        mLeftTextView.setVisibility(View.VISIBLE);
        mLeftTextView.setText("收件箱");
        mLeftImageView.setImageResource(R.drawable.item_point_left);
        initWithRightBar();
        mRightImageView.setVisibility(View.VISIBLE);
        mRightImageView.setImageResource(R.drawable.item_menu);

    }

}
