package com.henghao.hhworkpresent.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.mapapi.SDKInitializer;
import com.benefit.buy.library.views.xlistview.XListView;
import com.henghao.hhworkpresent.FragmentSupport;
import com.henghao.hhworkpresent.ProtocolUrl;
import com.henghao.hhworkpresent.R;
import com.henghao.hhworkpresent.activity.RenyuanKaoqingInfoActivity;
import com.henghao.hhworkpresent.adapter.SortAdapter;
import com.henghao.hhworkpresent.entity.ContactSortModel;
import com.henghao.hhworkpresent.utils.LocationUtils;
import com.henghao.hhworkpresent.utils.PinyinComparator;
import com.henghao.hhworkpresent.utils.PinyinUtils;
import com.henghao.hhworkpresent.views.EditTextWithDel;
import com.henghao.hhworkpresent.views.SideBar;
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by bryanrady on 2017/7/19.
 */

public class RenyuanKaoqingFragment extends FragmentSupport {

    @ViewInject(R.id.showname_layout)
    private RelativeLayout showname_layout;

    @ViewInject(R.id.tongxunlu_layout)
    private LinearLayout tongxunlu_layout;

    @ViewInject(R.id.lv_contact)
    private XListView sortListView;

    @ViewInject(R.id.sidrbar)
    private SideBar sideBar;
    @ViewInject(R.id.dialog)
    private TextView dialog;
    @ViewInject(R.id.tv_title)
    public TextView mTvTitle;
    private SortAdapter adapter;
    @ViewInject(R.id.et_search)
    private EditTextWithDel mEtSearchName;
    private List<ContactSortModel> SourceDateList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        SDKInitializer.initialize(getActivity().getApplication().getApplicationContext());
        LocationUtils.Location(getActivity().getApplication().getApplicationContext());
        this.mActivityFragmentView.viewMain(R.layout.activity_tongxunlu);
        this.mActivityFragmentView.viewEmpty(R.layout.activity_empty);
        this.mActivityFragmentView.viewEmptyGone();
        this.mActivityFragmentView.viewLoading(View.GONE);
        this.mActivityFragmentView.viewLoadingError(View.GONE);
        ViewUtils.inject(this, this.mActivityFragmentView);
        //注册定位监听  必须用全局 context  不能用 this.mActivity
        initWidget();
        initData();
        return this.mActivityFragmentView;
    }

    public void initWidget() {
        initWithBar();
        mLeftTextView.setText("人员考勤");
        mLeftTextView.setVisibility(View.VISIBLE);
        mLeftTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActivity.finish();
            }
        });
        mLeftImageView.setImageResource(R.drawable.item_point_left);
        mLeftImageView.setVisibility(View.VISIBLE);
        mLeftImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActivity.finish();
            }
        });

        initLoadingError();
        tv_viewLoadingError.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActivityFragmentView.viewLoadingError(View.GONE);
                httpRequestMyTongxunlu();
            }
        });
    }

    public void initData() {
        initViews();
    }

    private void initViews() {
        /*mEtSearchName = (EditTextWithDel) findViewById(R.id.et_search);
        sideBar = (SideBar) findViewById(R.id.sidrbar);
        dialog = (TextView) findViewById(R.id.dialog);
        mTvTitle = (TextView) findViewById(R.id.tv_title);
        sortListView = (XListView) findViewById(R.id.lv_contact);*/
        initDatas();
        initEvents();
        httpRequestMyTongxunlu();
    }

    private void initEvents() {
        //设置右侧触摸监听
        sideBar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {
            @Override
            public void onTouchingLetterChanged(String s) {
                //该字母首次出现的位置
                if(adapter!=null){
                    int position = adapter.getPositionForSection(s.charAt(0));
                    if (position != -1) {
                        sortListView.setSelection(position + 1);
                    }
                }
            }
        });

        //ListView的点击事件
        sortListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position1, long id) {
                showname_layout.setVisibility(View.VISIBLE);
                Intent intent = new Intent();
                intent.setClass(mActivity, RenyuanKaoqingInfoActivity.class);
                intent.putExtra("uid",((ContactSortModel) adapter.getItem(position1 -1 )).getUid());
                intent.putExtra("name",((ContactSortModel) adapter.getItem(position1 -1 )).getName());
                startActivity(intent);
            }
        });

        //根据输入框输入值的改变来过滤搜索
        mEtSearchName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //当输入框里面的值为空，更新为原来的列表，否则为过滤数据列表
                filterData(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void initDatas() {
        sideBar.setTextView(dialog);
    }

    /**
     * 根据输入框中的值来过滤数据并更新ListView
     *
     * @param filterStr
     */
    private void filterData(String filterStr) {
        List<ContactSortModel> mSortList = new ArrayList<>();
        if (TextUtils.isEmpty(filterStr)) {
            mSortList = SourceDateList;
        } else {
            mSortList.clear();
            for (ContactSortModel sortModel : SourceDateList) {
                String name = sortModel.getName();
                if (name.toUpperCase().indexOf(filterStr.toString().toUpperCase()) != -1 || PinyinUtils.getPingYin(name).toUpperCase().startsWith(filterStr.toString().toUpperCase())) {
                    mSortList.add(sortModel);
                }
            }
        }
        // 根据a-z进行排序
        Collections.sort(mSortList, new PinyinComparator());
        adapter.updateListView(mSortList);
        adapter.notifyDataSetChanged();
    }

    private Handler mHandler = new Handler(){};

    private ArrayList<String> indexString;

    private void httpRequestMyTongxunlu() {
        OkHttpClient okHttpClient = new OkHttpClient();
        Request.Builder builder = new Request.Builder();
        FormEncodingBuilder requestBodyBuilder = new FormEncodingBuilder();
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
                        tongxunlu_layout.setVisibility(View.GONE);
                        mActivityFragmentView.viewLoadingError(View.VISIBLE);
                    }
                });
            }

            @Override
            public void onResponse(Response response) throws IOException {
                String result_str = response.body().string();
                try {
                    JSONObject jsonObject = new JSONObject(result_str);
                    Log.d("wangqingbin","jsonObject=="+jsonObject);
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
                    indexString = new ArrayList<>();
                    for(int i=0;i<jsonArray.length();i++){
                        ContactSortModel contactSortModel = new ContactSortModel();
                        JSONObject dataObject = jsonArray.getJSONObject(i);
                        String name = dataObject.getString("name");
                        String uid = dataObject.getString("id");
                        contactSortModel.setName(name);
                        contactSortModel.setUid(uid);

                        //将名字转化为拼音
                        String pinyin = PinyinUtils.getPingYin(name);
                        String sortString = pinyin.substring(0, 1).toUpperCase();
                        if (sortString.matches("[A-Z]")) {
                            contactSortModel.setSortLetters(sortString.toUpperCase());
                            if (!indexString.contains(sortString)) {
                                indexString.add(sortString);
                            }
                        }
                        SourceDateList.add(contactSortModel);
                    }

                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            tongxunlu_layout.setVisibility(View.VISIBLE);
                            Collections.sort(indexString);
                            sideBar.setIndexText(indexString);
                            Collections.sort(SourceDateList, new PinyinComparator());
                            adapter = new SortAdapter(mActivity, SourceDateList);
                            sortListView.setAdapter(adapter);
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
