package com.henghao.hhworkpresent.activity;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.GridView;

import com.henghao.hhworkpresent.ActivityFragmentSupport;
import com.henghao.hhworkpresent.R;
import com.henghao.hhworkpresent.adapter.XingzhenZFFirstAdapter;
import com.henghao.hhworkpresent.adapter.XingzhenZFSecAdapter;
import com.henghao.hhworkpresent.entity.AppGridEntity;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bryanrady on 2017/3/1.
 */

public class XingZhenZFActivity extends ActivityFragmentSupport {

    @ViewInject(R.id.gridview1)
    private GridView firstGridView;

    @ViewInject(R.id.gridview2)
    private GridView secGridView;

    private XingzhenZFFirstAdapter firstAdapter;
    private XingzhenZFSecAdapter secAdapter;

    private List<AppGridEntity> mList2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.mActivityFragmentView.viewMain(R.layout.activity_cheliangshenpi);
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
        mLeftTextView.setText("行政执法");
        mLeftTextView.setVisibility(View.VISIBLE);
        mLeftImageView.setVisibility(View.VISIBLE);
        mLeftImageView.setImageResource(R.drawable.item_point_left);
        initWithRightBar();
        mRightTextView.setVisibility(View.VISIBLE);
        mRightTextView.setText("帮助");
    }

    @Override
    public void initData() {
        super.initData();
        initFirstGrid();
        initSecondGrid();

    }

    public void initFirstGrid(){
        mList2 = new ArrayList<AppGridEntity>();
        //第一个
        AppGridEntity mEntity = new AppGridEntity();
        mEntity.setImageId(R.drawable.item_daiwoshenpi);
        mEntity.setName("待我审批");
        mList2.add(mEntity);
        //第二个
        AppGridEntity mEntity2 = new AppGridEntity();
        mEntity2.setImageId(R.drawable.item_wofaqide);
        mEntity2.setName("我发起的");
        mList2.add(mEntity2);
        //第三个
        AppGridEntity mEntity3 = new AppGridEntity();
        mEntity3.setImageId(R.drawable.item_chaosongwode);
        mEntity3.setName("抄送我的");
        mList2.add(mEntity3);
        firstAdapter = new XingzhenZFFirstAdapter(this, mList2);
        this.firstGridView.setAdapter(firstAdapter);
        firstAdapter.notifyDataSetChanged();
    }

    public void initSecondGrid(){
        mList2 = new ArrayList<AppGridEntity>();
        //第一个
        AppGridEntity mEntity = new AppGridEntity();
        mEntity.setImageId(R.drawable.item_zhifajiankong);
        mEntity.setName("执法监控");
        mList2.add(mEntity);
        //第二个
        AppGridEntity mEntity2 = new AppGridEntity();
        mEntity2.setImageId(R.drawable.item_zhifazhibiao);
        mEntity2.setName("执法指标");
        mList2.add(mEntity2);
        //第三个
        AppGridEntity mEntity3 = new AppGridEntity();
        mEntity3.setImageId(R.drawable.item_yuangongfenxi);
        mEntity3.setName("员工分析");
        mList2.add(mEntity3);
        //第四个
        AppGridEntity mEntity4 = new AppGridEntity();
        mEntity4.setImageId(R.drawable.item_zonghechaxun);
        mEntity4.setName("综合查询");
        mList2.add(mEntity4);
        //第五个
        AppGridEntity mEntity5 = new AppGridEntity();
        mEntity5.setImageId(R.drawable.item_tongjifenxi);
        mEntity5.setName("统计分析");
        mList2.add(mEntity5);
        //第六个
        AppGridEntity mEntity6 = new AppGridEntity();
        mEntity6.setImageId(R.drawable.item_toushufankui);
        mEntity6.setName("投诉反馈");
        mList2.add(mEntity6);
        //第七个
        AppGridEntity mEntity7 = new AppGridEntity();
        mEntity7.setImageId(R.drawable.item_add);
        mEntity7.setName("添加模块");
        mList2.add(mEntity7);
        secAdapter = new XingzhenZFSecAdapter(this, mList2);
        this.secGridView.setAdapter(secAdapter);
        secAdapter.notifyDataSetChanged();
    }
}
