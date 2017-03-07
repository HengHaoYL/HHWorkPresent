package com.henghao.hhworkpresent.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.RadioGroup;

import com.benefit.buy.library.utils.NSLog;
import com.benefit.buy.library.views.ToastView;
import com.henghao.hhworkpresent.ActivityFragmentSupport;
import com.henghao.hhworkpresent.Constant;
import com.henghao.hhworkpresent.FragmentSupport;
import com.henghao.hhworkpresent.R;
import com.henghao.hhworkpresent.adapter.FragmentTabAdapter;
import com.henghao.hhworkpresent.entity.HCMenuEntity;
import com.henghao.hhworkpresent.fragment.AppFragment;
import com.henghao.hhworkpresent.fragment.MsgFragment;
import com.henghao.hhworkpresent.fragment.MyFragment;
import com.henghao.hhworkpresent.fragment.WorkFragment;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;




/**
 *  主页
 * @author wangqingbin
 */
@SuppressLint("NewApi")
public class MainActivity extends ActivityFragmentSupport {

    @ViewInject(R.id.main_radioGroup)
    private RadioGroup radioGroup;

    @ViewInject(R.id.tab_top)
    public View mTabLinearLayout;

    public List<FragmentSupport> fragments = new ArrayList<FragmentSupport>();

    private List<HCMenuEntity> menuLists;

    private FragmentTabAdapter tabAdapter;

    private boolean isExit = false;

    private ToastView mToastView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (this.tabAdapter != null) {
            this.tabAdapter.remove();
        }
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.mActivityFragmentView.viewMain(R.layout.activity_main);
        this.mActivityFragmentView.viewEmpty(R.layout.activity_empty);
        this.mActivityFragmentView.viewEmptyGone();
        this.mActivityFragmentView.viewLoading(View.GONE);
        this.mActivityFragmentView.clipToPadding(true);
        setContentView(this.mActivityFragmentView);
        com.lidroid.xutils.ViewUtils.inject(this);
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
        }
        catch (Exception e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        // Intent intent = getIntent();
        // page = intent.getIntExtra("page", 0);
        // if (page == 3) {
        // hcShopcar.setChecked(true);
        // }
        this.tabAdapter = new FragmentTabAdapter(this, this.fragments, R.id.tab_content, this.radioGroup);
        this.tabAdapter.setOnRgsExtraCheckedChangedListener(new FragmentTabAdapter.OnRgsExtraCheckedChangedListener() {

            @Override
            public void OnRgsExtraCheckedChanged(RadioGroup radioGroup, int checkedId, int index) {
                System.out.println("Extra---- " + index + " checked!!! ");
            }
        });
        initData();
    }

    @Override
    public void initData() {
        // TODO Auto-generated method stub
        mActivityFragmentView.getNavitionBarView().setVisibility(View.GONE);
    }

    /**
     * 牵扯到的tab items
     */
    public void menuList() {
        this.menuLists = new ArrayList<HCMenuEntity>();
        HCMenuEntity mMenuMsg = new HCMenuEntity(1, getResources().getString(R.string.hc_home),
                R.drawable.selector_msg, MsgFragment.class.getName(), -1);// 消息
        this.menuLists.add(mMenuMsg);
        HCMenuEntity mMenuApp = new HCMenuEntity(2, getResources().getString(R.string.hc_app),
                R.drawable.selector_app, AppFragment.class.getName(), -1);// 应用
        this.menuLists.add(mMenuApp);
        HCMenuEntity mMenuWork = new HCMenuEntity(3, getResources().getString(R.string.hc_work),
                R.drawable.selector_work, WorkFragment.class.getName(), -1);// 工作
        this.menuLists.add(mMenuWork);
        HCMenuEntity mMenuMyself = new HCMenuEntity(4, getResources().getString(R.string.hc_myself),
                R.drawable.selector_my, MyFragment.class.getName(), -1);// 个人中心
        this.menuLists.add(mMenuMyself);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if ((event.getKeyCode() == KeyEvent.KEYCODE_BACK) && (event.getAction() != KeyEvent.ACTION_UP)) {
            if (!this.isExit) {
                this.isExit = true;
                this.mToastView = ToastView.makeText(this, getResources().getString(R.string.home_exit)).setGravity(
                        Gravity.CENTER, 0, 0);
                this.mToastView.show();
                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        MainActivity.this.isExit = false;
                    }
                }, 3000);
                return true;
            }
            else {
                this.mToastView.cancel();
                this.mApplication.exit();
                return false;
            }
        }
        return super.dispatchKeyEvent(event);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case Constant.SCANNIN_GREQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    Bundle bundle = data.getExtras();
                    // 显示扫描到的内容
                    String content = bundle.getString("result");
                    // 显示
                    Bitmap bitmap = data.getParcelableExtra("bitmap");
                    NSLog.e(this, "content:" + content);
                }
                break;
        }
    }

}
