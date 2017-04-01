package com.henghao.hhworkpresent.activity.user;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.Window;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.henghao.hhworkpresent.ActivityFragmentSupport;
import com.henghao.hhworkpresent.R;
import com.henghao.hhworkpresent.fragment.user.LoginFragment;
import com.henghao.hhworkpresent.fragment.user.RegisterFragment;
import com.lidroid.xutils.view.annotation.ViewInject;



public class LoginAndRegActivity extends ActivityFragmentSupport implements OnCheckedChangeListener {

    @ViewInject(R.id.tabs_rg)
    private RadioGroup mTabs;

    private LoginFragment loginFragment;

    private RegisterFragment registerFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        mActivityFragmentView.viewMain(R.layout.activity_loginandreg);
        mActivityFragmentView.viewEmpty(R.layout.activity_empty);
        mActivityFragmentView.viewEmptyGone();
        mActivityFragmentView.viewLoading(View.GONE);
        mActivityFragmentView.clipToPadding(true);
        setContentView(mActivityFragmentView);
        com.lidroid.xutils.ViewUtils.inject(this);
        initWidget();
        initData();
    }

    @Override
    public void initWidget() {
        // TODO Auto-generated method stub
        initWithContent();
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        loginFragment = (LoginFragment) LoginFragment.newInstance(0);
        registerFragment = (RegisterFragment) RegisterFragment.newInstance(1);
        ft.replace(R.id.fragment_content, loginFragment).commit();
    }

    private void initWithContent() {
        // TODO Auto-generated method stub
        mActivityFragmentView.getNavitionBarView().setBackgroundColor(getResources().getColor(R.color.white));
        /** 导航栏 */
        initWithBar();
        mLeftTextView.setVisibility(View.VISIBLE);
        mLeftImageView.setVisibility(View.VISIBLE);
        mLeftImageView.setImageResource(R.drawable.btn_blackback);
        mLeftTextView.setText("注册");
        mLeftTextView.setTextColor(getResources().getColor(R.color.text_color_a));
    }

    @Override
    public void initData() {
        // TODO Auto-generated method stub
        mTabs.setOnCheckedChangeListener(this);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        // TODO Auto-generated method stub
        // mRadioGroup.check(checkedId);
        // llHeader.check(checkedId);
        for (int i = 0; i < group.getChildCount(); i++) {
            RadioButton radioButton = (RadioButton) group.getChildAt(i);
            if (radioButton.isChecked()) {
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                switch (i) {
                    case 0:
                        loginFragment = (LoginFragment) LoginFragment.newInstance(0);
                        ft.replace(R.id.fragment_content, loginFragment).commit();
                        break;
                    case 1:
                        registerFragment = (RegisterFragment) RegisterFragment.newInstance(1);
                        ft.replace(R.id.fragment_content, registerFragment).commit();
                        break;
                    default:
                        break;
                }
            }
        }
    }
}
