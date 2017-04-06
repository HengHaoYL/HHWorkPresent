package com.henghao.hhworkpresent.activity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.benefit.buy.library.utils.tools.ToolsKit;
import com.henghao.hhworkpresent.ActivityFragmentSupport;
import com.henghao.hhworkpresent.ProtocolUrl;
import com.henghao.hhworkpresent.R;
import com.henghao.hhworkpresent.views.DatabaseHelper;
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

    private DatabaseHelper dbHelper;
    private SQLiteDatabase db;
    private Handler mHandler = new Handler(){};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        mLeftTextView.setText("登录");
    }

    @Override
    public void initData() {
        super.initData();
    }

    @OnClick({R.id.tv_login})
    public void viewClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.tv_login:
                //登录
                if (checkData()) {
                    httpRequestLogin();
                    mActivityFragmentView.viewLoading(View.VISIBLE);
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
                        Toast.makeText(getContext(), "网络访问错误！", Toast.LENGTH_SHORT).show();
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
                                msg("登录失败，请重新登录！");
                            }
                        });
                    }
                    if(status == 1){
                        JSONObject dataObject = jsonObject.getJSONObject("data");
                        String uid = dataObject.optString("id");
                        dbHelper = new DatabaseHelper(LoginActivity.this,"user_login.db");
                        db = dbHelper.getWritableDatabase();
                        ContentValues contentValues = new ContentValues();
                        contentValues.put("uid",uid);
                        contentValues.put("username",login_user.getText().toString().trim());
                        contentValues.put("password",login_pass.getText().toString().trim());
                        db.insert("user", null, contentValues);

                        Intent intent = new Intent();
                        intent.setClass(LoginActivity.this,MainActivity.class);
                        startActivity(intent);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private boolean checkData() {
        if (ToolsKit.isEmpty(login_user.getText().toString().trim())) {
            this.msg("用户名不能为空");
            return false;
        }
        if (ToolsKit.isEmpty(login_pass.getText().toString().trim())) {
            this.msg("密码不能为空");
            return false;
        }
        return true;
    }

}
