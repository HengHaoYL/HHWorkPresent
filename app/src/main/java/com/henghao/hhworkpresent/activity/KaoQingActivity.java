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
import com.henghao.hhworkpresent.fragment.DakaFragment;
import com.henghao.hhworkpresent.fragment.KaoqingFragment;
import com.henghao.hhworkpresent.fragment.QingjiaChuchaiFragment;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bryanrady on 2017/3/1.
 *
 * 考勤界面
 */

public class KaoQingActivity extends ActivityFragmentSupport {

    @ViewInject(R.id.kaoqing_radioGroup)
    private RadioGroup radioGroup;

    public List<FragmentSupport> fragments = new ArrayList<FragmentSupport>();

    private List<HCMenuEntity> menuLists;

    private FragmentTabAdapter tabAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme);
        if (this.tabAdapter != null) {
            this.tabAdapter.remove();
        }
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.mActivityFragmentView.viewMain(R.layout.activity_kaoqing);
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
            // TODO Auto-generated catch block
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
        HCMenuEntity mMenuMsg = new HCMenuEntity(1, "打卡",
                R.drawable.selector_qiandao, DakaFragment.class.getName(), -1);// 打卡
        this.menuLists.add(mMenuMsg);
        HCMenuEntity mMenuApp = new HCMenuEntity(2, "考勤",
                R.drawable.selector_kaoqing, KaoqingFragment.class.getName(), -1);// 考勤
        this.menuLists.add(mMenuApp);
        HCMenuEntity mMenuQingjia = new HCMenuEntity(3, "请假",
                R.drawable.selector_qingjia, QingjiaChuchaiFragment.class.getName(), -1);// 请假、出差、补卡申请
        this.menuLists.add(mMenuQingjia);

    }
}
