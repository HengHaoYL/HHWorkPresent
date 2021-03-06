package com.henghao.hhworkpresent.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.henghao.hhworkpresent.ActivityFragmentSupport;
import com.henghao.hhworkpresent.ProtocolUrl;
import com.henghao.hhworkpresent.R;
import com.henghao.hhworkpresent.utils.SqliteDBUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by bryanrady on 2017/4/5.
 */

public class MySelfZiliaoActivity extends ActivityFragmentSupport {

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

    /*@ViewInject(R.id.my_ziliao_layout)
    private ScrollView my_ziliao_layout;*/

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

    private String deptId;

    private Handler mHandler = new Handler(){};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.mActivityFragmentView.viewMain(R.layout.activity_selfziliao);
        this.mActivityFragmentView.viewEmpty(R.layout.activity_empty);
        this.mActivityFragmentView.viewEmptyGone();
        this.mActivityFragmentView.viewLoading(View.GONE);
        //    this.mActivityFragmentView.viewLoadingError(View.GONE);
        this.mActivityFragmentView.clipToPadding(true);
        ViewUtils.inject(this, this.mActivityFragmentView);
        setContentView(this.mActivityFragmentView);
        initWidget();
        initData();
    }

    @Override
    public void onResume() {
        super.onResume();
        httpRequestMySelfZiliao();
    }

    @Override
    public void initWidget() {
        super.initWidget();
        initWithBar();
        mLeftTextView.setText(R.string.tv_personal_data);
        mLeftTextView.setVisibility(View.VISIBLE);

        initWithRightBar();
        mRightTextView.setText(R.string.tv_edit);
        mRightTextView.setVisibility(View.VISIBLE);
        mRightTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toNextActivity();
            }
        });

    }

    public void toNextActivity(){
        if("0".equals(sex)){
            sex = getString(R.string.male);
        }else if("1".equals(sex)){
            sex = getString(R.string.female);
        }
        if("null".equals(position)||"".equals(position)){
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
        Intent intent = new Intent();
        intent.setClass(MySelfZiliaoActivity.this,EditMySelfZiliaoActivity.class);
        intent.putExtra("name",name);
        intent.putExtra("emp_NUM",emp_NUM);
        intent.putExtra("birth_DATE",birth_DATE);
        intent.putExtra("telephone",telephone);
        intent.putExtra("sex",sex);
        intent.putExtra("position",position);
        intent.putExtra("address",address);
        intent.putExtra("cellphone",cellphone);
        intent.putExtra("work_DESC",work_DESC);
        intent.putExtra("dept_NAME",dept_NAME);
        intent.putExtra("deptId",deptId);
        startActivity(intent);
    }


    @Override
    public void initData() {
        super.initData();
        httpRequestDeptId();
        httpRequestMySelfZiliao();
    }

    private void httpRequestDeptId(){
        SqliteDBUtils sqliteDBUtils = new SqliteDBUtils(this);
        OkHttpClient okHttpClient = new OkHttpClient();
        Request.Builder builder = new Request.Builder();
        FormEncodingBuilder requestBodyBuilder = new FormEncodingBuilder();
        requestBodyBuilder.add("uid", sqliteDBUtils.getLoginUid());
        RequestBody requestBody = requestBodyBuilder.build();
        String request_url = ProtocolUrl.ROOT_URL + ProtocolUrl.APP_QUERY_MYSELF_DEPT_ID;
        Request request = builder.url(request_url).post(requestBody).build();
        Call call = okHttpClient.newCall(request);
        mActivityFragmentView.viewLoading(View.VISIBLE);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mActivityFragmentView.viewLoading(View.GONE);
                    }
                });
            }

            @Override
            public void onResponse(Response response) throws IOException {
                String result_str = response.body().string();
                try {
                    JSONObject jsonObject = new JSONObject(result_str);
                    int status = jsonObject.getInt("status");
                    if (status == 0) {
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                mActivityFragmentView.viewLoading(View.GONE);
                            }
                        });
                    }
                    deptId  = jsonObject.getString("data");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 查询个人详细资料
     */
    private void httpRequestMySelfZiliao() {
        SqliteDBUtils sqliteDBUtils = new SqliteDBUtils(this);
        OkHttpClient okHttpClient = new OkHttpClient();
        Request.Builder builder = new Request.Builder();
        FormEncodingBuilder requestBodyBuilder = new FormEncodingBuilder();
        requestBodyBuilder.add("uid", sqliteDBUtils.getLoginUid());
        RequestBody requestBody = requestBodyBuilder.build();
        String request_url = ProtocolUrl.ROOT_URL + "/"+ ProtocolUrl.APP_QUERY_MYSELF_ZILIAO;
        Request request = builder.url(request_url).post(requestBody).build();
        Call call = okHttpClient.newCall(request);
        mActivityFragmentView.viewLoading(View.VISIBLE);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mActivityFragmentView.viewLoading(View.GONE);
                    }
                });
            }

            @Override
            public void onResponse(Response response) throws IOException {
                String result_str = response.body().string();
                try {
                    JSONObject jsonObject = new JSONObject(result_str);
                    int status = jsonObject.getInt("status");
                    if (status == 0) {
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                           //     my_ziliao_layout.setVisibility(View.VISIBLE);
                                mActivityFragmentView.viewLoading(View.GONE);
                            }
                        });
                    }
                    JSONObject dataObject = jsonObject.getJSONObject("data");
                    name = dataObject.getString("name");
                    emp_NUM = dataObject.getString("emp_NUM");
                    birth_DATE = dataObject.getString("birth_DATE");
                    telephone = dataObject.getString("telephone");
                    sex = dataObject.getString("sex");
                    position = dataObject.getString("position");
                    address = dataObject.getString("address");
                    cellphone = dataObject.getString("cellphone");
                    work_DESC = dataObject.getString("work_DESC");
                    dept_NAME = dataObject.getString("dept_NAME");

                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            if("0".equals(sex)){
                                sex = getString(R.string.male);
                            }else if("1".equals(sex)){
                                sex = getString(R.string.female);
                            }
                            if("null".equals(position)||"".equals(position)){
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
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

}
