package com.henghao.hhworkpresent.fragment.user;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.benefit.buy.library.http.query.callback.AjaxStatus;
import com.benefit.buy.library.utils.tools.ToolsKit;
import com.benefit.buy.library.utils.tools.ToolsRegex;
import com.henghao.hhworkpresent.FragmentSupport;
import com.henghao.hhworkpresent.ProtocolUrl;
import com.henghao.hhworkpresent.R;
import com.henghao.hhworkpresent.entity.BaseEntity;
import com.henghao.hhworkpresent.protocol.LoginProtocol;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import org.json.JSONException;

/**
 * 我的注册〈一句话功能简述〉 〈功能详细描述〉
 *
 * @author zhangxianwen
 * @version HDMNV100R001, 2016年8月15日
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class RegisterFragment extends FragmentSupport {


    /**
     * 用户名
     */
    @ViewInject(R.id.login_user)
    private EditText login_user;
    /**
     * 电话
     */
    @ViewInject(R.id.login_phone)
    private EditText login_phone;
    /**
     * 密码
     */
    @ViewInject(R.id.login_pass)
    private EditText login_pass;
    /**
     * 密码
     */
    @ViewInject(R.id.login_passagain)
    private EditText login_passagain;

    public static FragmentSupport newInstance(Object obj) {
        RegisterFragment fragment = new RegisterFragment();
        if (fragment.object == null) {
            fragment.object = obj;
        }
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        this.mActivityFragmentView.viewMain(R.layout.activity_reg);
        this.mActivityFragmentView.viewEmpty(R.layout.activity_empty);
        this.mActivityFragmentView.viewEmptyGone();
        this.mActivityFragmentView.viewLoading(View.GONE);
        mActivityFragmentView.getNavitionBarView().setVisibility(View.GONE);
        ViewUtils.inject(this, this.mActivityFragmentView);
        initWidget();
        initData();
        return this.mActivityFragmentView;
    }

    private void initData() {
        mLeftImageView = (ImageView) getActivity().findViewById(R.id.bar_left_img);
        mLeftTextView = (TextView) getActivity().findViewById(R.id.bar_left_title);
        mLeftTextView.setText("注册");
        mLeftImageView.setImageDrawable(getResources().getDrawable(R.drawable.btn_blackback));
    }


    public void initWidget() {
    }


    @OnClick({R.id.btn_layout})
    public void viewOnClick(View v) {
        switch (v.getId()) {
            case R.id.btn_layout:
                //注册
                LoginProtocol mLoginProtocol = new LoginProtocol(mActivity);
                mLoginProtocol.addResponseListener(this);
                if (checkData()) {
                    String userName = login_user.getText().toString().trim();
                    String phone = login_phone.getText().toString().trim();
                    String pwd = login_pass.getText().toString().trim();
                    //注册
                    mLoginProtocol.reg_user(userName, pwd, phone);
                    mActivityFragmentView.viewLoading(View.VISIBLE);
                }
                break;
        }
    }

    private boolean checkData() {
        if (ToolsKit.isEmpty(login_user.getText().toString().trim())) {
            mActivity.msg("用户名不能为空");
            return false;
        } else if (ToolsKit.isEmpty(login_phone.getText().toString().trim())) {
            mActivity.msg("电话号码不能为空");
            return false;
        } else if (!ToolsRegex.isMobileNumber(login_phone.getText().toString().trim())) {
            mActivity.msg("电话号码格式不正确");
            return false;
        } else if (ToolsKit.isEmpty(login_pass.getText().toString().trim())) {
            mActivity.msg("密码不能为空");
            return false;
        } else if (ToolsKit.isEmpty(login_passagain.getText().toString().trim())) {
            mActivity.msg("密码确认不能为空");
            return false;
        } else if (!login_passagain.getText().toString().trim().equals(login_pass.getText().toString().trim())) {
            mActivity.msg("两次输入密码不相同");
            return false;
        }
        return true;
    }

    @Override
    public void OnMessageResponse(String url, Object jo, AjaxStatus status) throws JSONException {
        super.OnMessageResponse(url, jo, status);
        if (url.endsWith(ProtocolUrl.APP_REG)) {
            BaseEntity mBaseEntity = (BaseEntity) jo;
            mActivity.msg(mBaseEntity.getMsg());
        }
    }
}
