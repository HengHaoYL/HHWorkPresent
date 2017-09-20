package com.henghao.hhworkpresent.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.henghao.hhworkpresent.ActivityFragmentSupport;
import com.henghao.hhworkpresent.R;
import com.henghao.hhworkpresent.adapter.TextGridAdapter;
import com.henghao.hhworkpresent.entity.SaveCheckTaskEntity;
import com.henghao.hhworkpresent.entity.SceneJianchaEntity;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * 添加一起案件界面
 * Created by ASUS on 2017/9/20.
 */

public class AddAnjianActivity extends ActivityFragmentSupport {

    @ViewInject(R.id.tv_anjian_source)
    private TextView tv_anjian_source;

    @ViewInject(R.id.et_anjian_name)
    private EditText et_anjian_name;

    @ViewInject(R.id.et_anjian_reason)
    private EditText et_anjian_reason;

    @ViewInject(R.id.tv_anjian_company)
    private TextView tv_anjian_company;

    @ViewInject(R.id.tv_anjian_save)
    private TextView tv_anjian_save;

    @ViewInject(R.id.tv_anjian_cancel)
    private TextView tv_anjian_cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.mActivityFragmentView.viewMain(R.layout.activity_add_anjian);
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
        mLeftImageView.setVisibility(View.VISIBLE);
        initWithCenterBar();
        mCenterTextView.setText("添加一起案件");
        mCenterTextView.setVisibility(View.VISIBLE);

    }

    @Override
    public void initData() {
        super.initData();
        Intent data = getIntent();
        tv_anjian_source.setText("现场执法检查");
        tv_anjian_company.setText(((SceneJianchaEntity)data.getSerializableExtra("sceneJianchaEntity")).getCheckUnit());
    }

    @OnClick({R.id.tv_anjian_save,R.id.tv_anjian_cancel})
    private void viewOnClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.tv_anjian_save:        //保存
                if(("").equals(et_anjian_name.getText().toString())){
                    Toast.makeText(this,"请填写案件名称",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(("").equals(et_anjian_reason.getText().toString())){
                    Toast.makeText(this,"请填写案件源由",Toast.LENGTH_SHORT).show();
                    return;
                }
                intent.setClass(this,DiaochaQuzhengActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.tv_anjian_cancel:       //取消
                intent.setClass(this,XunchaJianchaActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }
}
