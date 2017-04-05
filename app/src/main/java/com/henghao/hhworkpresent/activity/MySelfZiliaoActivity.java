package com.henghao.hhworkpresent.activity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.henghao.hhworkpresent.ActivityFragmentSupport;
import com.henghao.hhworkpresent.ProtocolUrl;
import com.henghao.hhworkpresent.R;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by bryanrady on 2017/4/5.
 */

public class MySelfZiliaoActivity extends ActivityFragmentSupport {

    @ViewInject(R.id.myself_tv_username)
    private TextView tv_username;

    @ViewInject(R.id.myself_tv_sex)
    private TextView tv_sex;

    @ViewInject(R.id.myself_tv_bumen)
    private TextView tv_bumen;

    @ViewInject(R.id.myself_tv_zhengzhimianmao)
    private TextView tv_zhenzhimiaomao;

    @ViewInject(R.id.myself_tv_zhiwei)
    private TextView tv_zhiwei;

    @ViewInject(R.id.myself_tv_moblenumber)
    private TextView tv_moblenumber;

    @ViewInject(R.id.myself_tv_birthday)
    private TextView tv_birthday;

    @ViewInject(R.id.myself_tv_joinwork)
    private TextView tv_joinwork;

    @ViewInject(R.id.myself_tv_fixednumber)
    private TextView tv_fixednumber;

    @ViewInject(R.id.myself_tv_renzhidate)
    private TextView tv_renzhidate;

    private Handler mHandler = new Handler(){};

    private String username;
    private String sex;
    private String bumen;
    private String zhenzhimiaomao;
    private String zhiwei;
    private String mobilephone;
    private String birthday;
    private String joinwork;
    private String fixednumber;
    private String renzhidate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.mActivityFragmentView.viewMain(R.layout.activity_selfziliao);
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
        initWithCenterBar();
        mCenterTextView.setText("详细资料");
    }

    @Override
    public void initData() {
        super.initData();
        httpRequestMySelfZiliao();
        httpRequestBumenAndZhiwei();
    }

    /**
     * 查询个人详细资料
     */
    private void httpRequestMySelfZiliao() {
        OkHttpClient okHttpClient = new OkHttpClient();
        Request.Builder builder = new Request.Builder();
        FormEncodingBuilder requestBodyBuilder = new FormEncodingBuilder();
        String uid = "1";
        requestBodyBuilder.add("uid", uid);
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
                            }
                        });
                    }
                    JSONObject dataObject = jsonObject.getJSONObject("data");
                    username = dataObject.getString("username");
                    sex = dataObject.getString("sex");
                    zhenzhimiaomao = dataObject.getString("political");
                    mobilephone = dataObject.getString("mobilePhone");
                    birthday = dataObject.getString("birth");
                    joinwork = dataObject.getString("joinDate");
                    fixednumber = dataObject.getString("fixedPhone");
                    renzhidate = dataObject.getString("beInOfficeDate");

                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            tv_username.setText(username);
                            tv_sex.setText(sex);
                            tv_zhenzhimiaomao.setText(zhenzhimiaomao);
                            tv_moblenumber.setText(mobilephone);
                            tv_birthday.setText(birthday);
                            tv_joinwork.setText(joinwork);
                            tv_fixednumber.setText(fixednumber);
                            tv_renzhidate.setText(renzhidate);
                            mActivityFragmentView.viewLoading(View.GONE);
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void httpRequestBumenAndZhiwei() {
        OkHttpClient okHttpClient = new OkHttpClient();
        Request.Builder builder = new Request.Builder();
        FormEncodingBuilder requestBodyBuilder = new FormEncodingBuilder();
        String uid = "1";
        requestBodyBuilder.add("uid", uid);
        RequestBody requestBody = requestBodyBuilder.build();
        String request_url = ProtocolUrl.ROOT_URL + "/"+ ProtocolUrl.APP_QUERY_TONGXUNLU;
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
                            }
                        });
                    }
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    for(int i=0;i<jsonArray.length();i++){
                        JSONObject dataObject = jsonArray.getJSONObject(i);
                        bumen = dataObject.getString("orgname");
                        zhiwei = dataObject.getString("sysname");
                    }
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            tv_bumen.setText(bumen);
                            tv_zhiwei.setText(zhiwei);
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
