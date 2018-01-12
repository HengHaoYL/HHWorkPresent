package com.henghao.hhworkpresent.activity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.benefit.buy.library.utils.ToastUtils;
import com.benefit.buy.library.utils.tools.ToolsKit;
import com.google.gson.Gson;
import com.henghao.hhworkpresent.ActivityFragmentSupport;
import com.henghao.hhworkpresent.Constant;
import com.henghao.hhworkpresent.ProtocolUrl;
import com.henghao.hhworkpresent.R;
import com.henghao.hhworkpresent.entity.UserInfoEntity;
import com.henghao.hhworkpresent.service.KaoqingService;
import com.henghao.hhworkpresent.service.NotificationService;
import com.henghao.hhworkpresent.service.RealTimeService;
import com.henghao.hhworkpresent.views.DatabaseHelper;
import com.henghao.hhworkpresent.views.RemenberDatabaseHelper;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
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
 * 用户登录界面
 * Created by bryanrady on 2017/4/5.
 */

public class LoginActivity extends ActivityFragmentSupport {

    @ViewInject(R.id.login_user)
    private EditText login_user;

    @ViewInject(R.id.login_pass)
    private EditText login_pass;

    @ViewInject(R.id.tv_login)
    private TextView tv_logo;

    @ViewInject(R.id.remenber_password)
    private CheckBox checkbox_remenber_password;

    private RemenberDatabaseHelper remDBHelper;

    private DatabaseHelper dbHelper;
    private SQLiteDatabase db;
    private Handler mHandler = new Handler(){};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.mActivityFragmentView.viewMain(R.layout.activity_login);
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
        mLeftImageView.setVisibility(View.GONE);
        initWithCenterBar();
        mCenterTextView.setText(R.string.login);
        mCenterTextView.setVisibility(View.VISIBLE);
    }

    @Override
    public void initData() {
        super.initData();
        String isRemenbered = isRemenbered();
        if("0".equals(isRemenbered)){  //说明选中了
            checkbox_remenber_password.setChecked(true);
            login_user.setText(getRemUsername());
            login_pass.setText(getRemPassword());
        }else{
            login_user.setText(getRemUsername());
            login_pass.setText(R.string.tv_null);
        }
    }

    @OnClick({R.id.tv_login})
    public void viewClick(View v) {
        switch (v.getId()) {
            case R.id.tv_login:
                //登录
                if (checkData()) {
                    httpRequestLogin();
                }
                break;
        }
    }

    public void httpRequestLogin(){
        OkHttpClient okHttpClient = new OkHttpClient();
        Request.Builder builder = new Request.Builder();
        FormEncodingBuilder requestBodyBuilder = new FormEncodingBuilder();
        requestBodyBuilder.add("username", login_user.getText().toString().trim());
        requestBodyBuilder.add("password", login_pass.getText().toString().trim());
        RequestBody requestBody = requestBodyBuilder.build();
        String request_url = ProtocolUrl.ROOT_URL + "/"+ ProtocolUrl.APP_USER_LOGIN;
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
                        ToastUtils.show(getContext(), R.string.app_network_failure);
                    }
                });
            }

            @Override
            public void onResponse(Response response) throws IOException {
                String result_str = response.body().string();
                try {
                    UserInfoEntity userInfoEntity = new Gson().fromJson(result_str, UserInfoEntity.class);
                    JSONObject jsonObject = new JSONObject(result_str);
                    int status = jsonObject.getInt("status");
                    if (status == 1) {
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                mActivityFragmentView.viewLoading(View.GONE);
                                ToastUtils.show(getContext(), R.string.login_failure);
                            }
                        });
                    }
                    if(status == 0){
                        UserInfoEntity.UserInfo userInfo = userInfoEntity.getData();
                        String uid = userInfo.getId();
                        String firstName = userInfo.getFirstname();
                        String giveName = userInfo.getGivenname();

                        dbHelper = new DatabaseHelper(LoginActivity.this, Constant.USER_LOGIN_DATABASE);
                        db = dbHelper.getWritableDatabase();
                        ContentValues contentValues = new ContentValues();
                        contentValues.put("uid",uid);
                        contentValues.put("username",login_user.getText().toString().trim());
                        contentValues.put("password",login_pass.getText().toString().trim());
                        contentValues.put("firstName",firstName);
                        contentValues.put("giveName",giveName);
                        db.insert("user", null, contentValues);

                        remDBHelper = new RemenberDatabaseHelper(LoginActivity.this,Constant.USER_LOGIN_REMEMBER_DATABASE);
                        db = remDBHelper.getWritableDatabase();
                        contentValues = new ContentValues();
                        contentValues.put("uid",uid);
                        contentValues.put("username",login_user.getText().toString().trim());
                        contentValues.put("password",login_pass.getText().toString().trim());
                        String isChecked = null;
                        if(checkbox_remenber_password.isChecked()) {
                            isChecked = "0";
                        }else{
                            isChecked = "1";
                        }
                        contentValues.put("isChecked",isChecked);
                        db.insert("user_remenber", null, contentValues);

                        Intent intent1= new Intent();
                        intent1.setClass(LoginActivity.this,MainActivity.class);
                        startActivity(intent1);
                        finish();

                        /**
                         * 开启实时定位服务
                         */
                        Intent intent = new Intent(LoginActivity.this, RealTimeService.class);
                        startService(intent);

                        /**
                         * 开启服务
                         */
                        Intent intent2 = new Intent(LoginActivity.this, NotificationService.class);
                        startService(intent2);

                        /**
                         * 开启服务
                         */
                        Intent intent3 = new Intent(LoginActivity.this, KaoqingService.class);
                        startService(intent3);

                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                mActivityFragmentView.viewLoading(View.GONE);
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    /**
     * 查看记住密码选项选中状态
     * @return
     */
    public String isRemenbered(){
        remDBHelper = new RemenberDatabaseHelper(this,"user_login_remenber.db");
        db = remDBHelper.getReadableDatabase();
        Cursor cursor = db.query("user_remenber",new String[]{"isChecked"},null,null,null,null,null);
        String isChecked = null;
        while (cursor.moveToNext()){
            isChecked = cursor.getString((cursor.getColumnIndex("isChecked")));
        }
        return isChecked;
    }

    /**
     * 查看记住密码
     * @return
     */
    public String getRemPassword(){
        remDBHelper = new RemenberDatabaseHelper(this,"user_login_remenber.db");
        db = remDBHelper.getWritableDatabase();
        Cursor cursor = db.query("user_remenber",new String[]{"password"},null,null,null,null,null);
        String password = null;
        while (cursor.moveToNext()){
            password = cursor.getString((cursor.getColumnIndex("password")));
        }
        return password;
    }

    /**
     * 查看记住的用户名
     * @return
     */
    public String getRemUsername(){
        remDBHelper = new RemenberDatabaseHelper(this,"user_login_remenber.db");
        db = remDBHelper.getWritableDatabase();
        Cursor cursor = db.query("user_remenber",new String[]{"username"},null,null,null,null,null);
        String username = null;
        while (cursor.moveToNext()){
            username = cursor.getString((cursor.getColumnIndex("username")));
        }
        return username;
    }


    private boolean checkData() {
        if (ToolsKit.isEmpty(login_user.getText().toString().trim())) {
            Toast.makeText(getContext(), R.string.username_not_null, Toast.LENGTH_SHORT).show();
            return false;
        }
        if (ToolsKit.isEmpty(login_pass.getText().toString().trim())) {
            Toast.makeText(getContext(), R.string.password_not_null, Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

}
