package com.henghao.hhworkpresent.activity;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.RadioGroup;

import com.henghao.hhworkpresent.ActivityFragmentSupport;
import com.henghao.hhworkpresent.FragmentSupport;
import com.henghao.hhworkpresent.R;
import com.henghao.hhworkpresent.adapter.FragmentTabAdapter;
import com.henghao.hhworkpresent.entity.HCMenuEntity;
import com.henghao.hhworkpresent.fragment.KaoqingTongjiFragment;
import com.henghao.hhworkpresent.fragment.RenyuanKaoqingFragment;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * 人员考勤界面
 * Created by bryanrady on 2017/7/17.
 */

public class RenyuanKaoqingActivity extends ActivityFragmentSupport {

    @ViewInject(R.id.kaoqing_radioGroup)
    private RadioGroup radioGroup;

    public List<FragmentSupport> fragments = new ArrayList<FragmentSupport>();

    private List<HCMenuEntity> menuLists;

    private FragmentTabAdapter tabAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (this.tabAdapter != null) {
            this.tabAdapter.remove();
        }
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.mActivityFragmentView.viewMain(R.layout.activity_renyuankaoqing);
        this.mActivityFragmentView.viewEmpty(R.layout.activity_empty);
        this.mActivityFragmentView.viewEmptyGone();
        this.mActivityFragmentView.viewLoading(View.GONE);
        this.mActivityFragmentView.clipToPadding(true);
        ViewUtils.inject(this, this.mActivityFragmentView);
        setContentView(this.mActivityFragmentView);
        initWidget();
        initData();
        menuList();

        try {
            // 动态加载tab
            // 动态设置tab item
            for (int i = 0; i < this.menuLists.size(); i++) {
                HCMenuEntity menu = this.menuLists.get(i);
                if (menu.getStatus() == -1) {
                    @SuppressWarnings("unchecked")
                    Class<FragmentSupport> clazz = (Class<FragmentSupport>) Class.forName(menu.getClazz());
                    FragmentSupport fragmentSuper = clazz.newInstance();
                    fragmentSuper.fragmentId = menu.getmId();
                    this.fragments.add(fragmentSuper);
                }
            }
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        this.tabAdapter = new FragmentTabAdapter(this, this.fragments, R.id.tab_content, this.radioGroup);
        this.tabAdapter.setOnRgsExtraCheckedChangedListener(new FragmentTabAdapter.OnRgsExtraCheckedChangedListener() {

            @Override
            public void OnRgsExtraCheckedChanged(RadioGroup radioGroup, int checkedId, int index) {
                System.out.println("Extra---- " + index + " checked!!! ");
            }
        });
    }

    @Override
    public void initWidget() {
        super.initWidget();
        initWithRightBar();
        mRightImageView.setVisibility(View.VISIBLE);
        mRightImageView.setImageResource(R.drawable.item_wenhao);
    }

    @Override
    public void initData() {
        super.initData();
        mActivityFragmentView.getNavitionBarView().setVisibility(View.GONE);
    }

    /**
     * 牵扯到的tab items
     */
    public void menuList() {
        this.menuLists = new ArrayList<HCMenuEntity>();
        HCMenuEntity mMenuMsg = new HCMenuEntity(1, "人员考勤",
                R.drawable.selector_qiandao, RenyuanKaoqingFragment.class.getName(), -1);// 打卡
        this.menuLists.add(mMenuMsg);
        HCMenuEntity mMenuApp = new HCMenuEntity(2, "统计",
                R.drawable.selector_kaoqing, KaoqingTongjiFragment.class.getName(), -1);// 考勤
        this.menuLists.add(mMenuApp);

    }
}
