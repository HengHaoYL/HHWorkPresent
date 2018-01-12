package com.henghao.hhworkpresent.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.henghao.hhworkpresent.ActivityFragmentSupport;
import com.henghao.hhworkpresent.R;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * Created by bryanrady on 2017/4/1.
 */

public class MyTongxunluDetailActivity extends ActivityFragmentSupport {

    @ViewInject(R.id.tongxunlu_tv_name)
    private TextView tv_name;

    @ViewInject(R.id.tongxunlu_tv_sex)
    private TextView tv_sex;

    @ViewInject(R.id.tongxunlu_tv_birth_DATE)
    private TextView tv_birth_Date;

    @ViewInject(R.id.tongxunlu_tv_emp_NUM)
    private TextView tv_emp_Num;

    @ViewInject(R.id.tongxunlu_tv_telephone)
    private TextView tv_telePhone;

    @ViewInject(R.id.tongxunlu_tv_cellphone)
    private TextView tv_cellPhone;

    @ViewInject(R.id.tongxunlu_tv_position)
    private TextView tv_position;

    @ViewInject(R.id.tongxunlu_tv_address)
    private TextView tv_address;

    @ViewInject(R.id.tongxunlu_tv_dept_NAME)
    private TextView tv_dept_Name;

    @ViewInject(R.id.tongxunlu_tv_work_DESC)
    private TextView tv_work_DESC;

    private String name;
    private String emp_NUM;
    private String birth_DATE;
    private String telephone;
    private String sex;
    private String position;
    private String address;
    private String cellphone;
    private String work_DESC;
    private String dept_NAME;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.mActivityFragmentView.viewMain(R.layout.activity_tongxunlu_detail);
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
        mLeftTextView.setText(R.string.tv_address_book_detail);
        mLeftTextView.setVisibility(View.VISIBLE);
    }

    @Override
    public void initData() {
        super.initData();
        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        emp_NUM = intent.getStringExtra("emp_NUM");
        telephone = intent.getStringExtra("telephone");
        birth_DATE = intent.getStringExtra("birth_Date");
        sex = intent.getStringExtra("sex");
        position = intent.getStringExtra("position");
        address = intent.getStringExtra("address");
        cellphone = intent.getStringExtra("cellphone");
        work_DESC = intent.getStringExtra("work_DESC");
        dept_NAME = intent.getStringExtra("dept_NAME");

        if("0".equals(sex)){
            sex = getString(R.string.male);
        }else if("1".equals(sex)){
            sex = getString(R.string.female);
        }

        if("null".equals(position)||position==null){
            position = getString(R.string.tv_null);
        }
        if("null".equals(name)||name==null){
            name = getString(R.string.tv_null);
        }
        if("null".equals(emp_NUM)||emp_NUM==null){
            emp_NUM = getString(R.string.tv_null);
        }
        if("null".equals(birth_DATE)||birth_DATE==null){
            birth_DATE = getString(R.string.tv_null);
        }
        if("null".equals(telephone)||telephone==null){
            telephone = getString(R.string.tv_null);
        }
        if("null".equals(address)||address==null){
            address = getString(R.string.tv_null);
        }
        if("null".equals(cellphone)||cellphone==null){
            cellphone = getString(R.string.tv_null);
        }
        if("null".equals(work_DESC)||work_DESC==null){
            work_DESC = getString(R.string.tv_null);
        }
        if("null".equals(dept_NAME)||dept_NAME==null){
            dept_NAME = getString(R.string.tv_null);
        }

        tv_name.setText(name);
        tv_sex.setText(sex);
        tv_birth_Date.setText(birth_DATE);
        tv_emp_Num.setText(emp_NUM);
        tv_telePhone.setText(telephone);
        tv_cellPhone.setText(cellphone);
        tv_position.setText(position);
        tv_address.setText(address);
        tv_work_DESC.setText(work_DESC);
        tv_dept_Name.setText(dept_NAME);
    }
}
