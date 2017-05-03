package com.henghao.hhworkpresent.activity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.widget.ScrollView;
import android.widget.TextView;

import com.henghao.hhworkpresent.ActivityFragmentSupport;
import com.henghao.hhworkpresent.ProtocolUrl;
import com.henghao.hhworkpresent.R;
import com.henghao.hhworkpresent.views.DatabaseHelper;
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

    @ViewInject(R.id.my_ziliao_layout)
    private ScrollView my_ziliao_layout;

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

    private Handler mHandler = new Handler(){};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.mActivityFragmentView.viewMain(R.layout.activity_selfziliao);
        this.mActivityFragmentView.viewEmpty(R.layout.activity_empty);
        this.mActivityFragmentView.viewEmptyGone();
        this.mActivityFragmentView.viewLoading(View.GONE);
        this.mActivityFragmentView.viewLoadingError(View.GONE);
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
        initWithCenterBar();
        mCenterTextView.setText("详细资料");

        initLoadingError();
        tv_viewLoadingError.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActivityFragmentView.viewLoadingError(View.GONE);
                httpRequestMySelfZiliao();
            }
        });
    }

    @Override
    public void initData() {
        super.initData();
        httpRequestMySelfZiliao();
    }

    /**
     * 从本地数据库读取登录用户Id 用来作为数据请求id
     * @return
     */
    public String getLoginUid(){
        DatabaseHelper dbHelper = new DatabaseHelper(this,"user_login.db");
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.query("user",new String[]{"uid"},null,null,null,null,null);
        String uid = null;
        while (cursor.moveToNext()){
            uid = cursor.getString((cursor.getColumnIndex("uid")));
        }
        return uid;
    }

    /**
     * 查询个人详细资料
     */
    private void httpRequestMySelfZiliao() {
        OkHttpClient okHttpClient = new OkHttpClient();
        Request.Builder builder = new Request.Builder();
        FormEncodingBuilder requestBodyBuilder = new FormEncodingBuilder();
        requestBodyBuilder.add("uid", getLoginUid());
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
                        my_ziliao_layout.setVisibility(View.GONE);
                        mActivityFragmentView.viewLoadingError(View.VISIBLE);
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
                                my_ziliao_layout.setVisibility(View.VISIBLE);
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
                                sex = "男";
                            }else if("1".equals(sex)){
                                sex = "女";
                            }
                            if("null".equals(position)||"".equals(position)){
                                position = "";
                            }
                            if("null".equals(name)||name==null){
                                name="";
                            }
                            if("null".equals(emp_NUM)||emp_NUM==null){
                                emp_NUM="";
                            }
                            if("null".equals(birth_DATE)||birth_DATE==null){
                                birth_DATE="";
                            }
                            if("null".equals(telephone)||telephone==null){
                                telephone="";
                            }
                            if("null".equals(address)||address==null){
                                address="";
                            }
                            if("null".equals(cellphone)||cellphone==null){
                                cellphone="";
                            }
                            if("null".equals(work_DESC)||work_DESC==null){
                                work_DESC="";
                            }
                            if("null".equals(dept_NAME)||dept_NAME==null){
                                dept_NAME="";
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
                            mActivityFragmentView.viewLoading(View.GONE);
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

}
