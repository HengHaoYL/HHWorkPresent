package com.henghao.hhworkpresent.fragment;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.henghao.hhworkpresent.FragmentSupport;
import com.henghao.hhworkpresent.R;
import com.henghao.hhworkpresent.adapter.WorkGridAdapter;
import com.henghao.hhworkpresent.entity.AppGridEntity;
import com.henghao.hhworkpresent.utils.SqliteDBUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by bryanrady on 2017/2/28.
 */

public class WorkFragment extends FragmentSupport {

    @ViewInject(R.id.gridview)
    private GridView gridview;

    private SqliteDBUtils sqliteDBUtils;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        this.mActivityFragmentView.viewMain(R.layout.fragment_work);
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
        sqliteDBUtils = new SqliteDBUtils(mActivity);
        //每天有人打卡就给每个领导人默认签到人员：'曾宇','兰天','胡正康','邓发权','安尤光','魏燕飞','胡美琪','杨亚琦',"陆建"
        String[] leader={   "HZ9080955acfcfff015acfe883040409","HZ9080955acfcfff015acfe9cb0f044d","HZ9080955acfcfff015acfea2edb0456",
                            "HZ9080955acfcfff015acfea808e045d","HZ9080955acfcfff015acfef3bb30555","HZ9080955acfcfff015ad00f25bd08bd",
                            "HZ9080955acfcfff015acff073f9056f","HZ9080955acfcfff015acfeff1750561","HZ8bb0c95ce22e77015ce25ddd100319"};


        List<AppGridEntity> mList = new ArrayList<AppGridEntity>();
        //第一个
        AppGridEntity mEntity = new AppGridEntity();
        mEntity.setImageId(R.drawable.item_waiqingqiandao);
        mEntity.setName("外勤签到");
        mList.add(mEntity);
        //第二个
        AppGridEntity mEntity2 = new AppGridEntity();
        mEntity2.setImageId(R.drawable.item_kaoqing);
        mEntity2.setName("考勤");
        mList.add(mEntity2);
        //第三个
        AppGridEntity mEntity3 = new AppGridEntity();
        mEntity3.setImageId(R.drawable.item_email);
        mEntity3.setName("邮箱");
        mList.add(mEntity3);
        //第四个
        AppGridEntity mEntity4 = new AppGridEntity();
        mEntity4.setImageId(R.drawable.icon_xingzhenzhifa);
        mEntity4.setName("行政执法");
        mList.add(mEntity4);
        //第五个
        AppGridEntity mEntity5 = new AppGridEntity();
        mEntity5.setImageId(R.drawable.item_gongzuogong1ji);
        mEntity5.setName("工作轨迹");
        mList.add(mEntity5);
        //第六个
        AppGridEntity mEntity6 = new AppGridEntity();
        mEntity6.setImageId(R.drawable.icon_listprocess);
        mEntity6.setName("清单流程");
        mList.add(mEntity6);
        //第七个
        AppGridEntity mEntity7 = new AppGridEntity();
        mEntity7.setImageId(R.drawable.icon_listprocess);
        mEntity7.setName("会议管理");
        mList.add(mEntity7);
        //第八个
        AppGridEntity mEntity8 = new AppGridEntity();
        mEntity8.setImageId(R.drawable.icon_xunchajiancha);
        mEntity8.setName("巡查检查");
        mList.add(mEntity8);
        //第九个
        AppGridEntity mEntity9 = new AppGridEntity();
        mEntity9.setImageId(R.drawable.item_renyuankaoqing);
        mEntity9.setName("人员考勤");
        for(String uid : leader){
             if(uid.equals(sqliteDBUtils.getLoginUid())){
                 mList.add(mEntity9);
             }
        }
        WorkGridAdapter adapter = new WorkGridAdapter(this.mActivity,mList);
        gridview.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }
    private void initwithContent() {
        initWithCenterBar();
        this.mCenterTextView.setVisibility(View.VISIBLE);
        this.mCenterTextView.setText("工作");
    }
}
