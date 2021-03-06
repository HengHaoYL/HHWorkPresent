package com.henghao.hhworkpresent.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.benefit.buy.library.views.xlistview.XListView;
import com.henghao.hhworkpresent.ActivityFragmentSupport;
import com.henghao.hhworkpresent.ProtocolUrl;
import com.henghao.hhworkpresent.R;
import com.henghao.hhworkpresent.adapter.SortAdapter;
import com.henghao.hhworkpresent.entity.ContactSortModel;
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
 * Created by bryanrady on 2017/4/1.
 * 通讯录界面
 */

public class MyTongxunluActivity extends ActivityFragmentSupport {

    @ViewInject(R.id.showname_layout)
    private RelativeLayout showname_layout;

    @ViewInject(R.id.tongxunlu_layout)
    private LinearLayout tongxunlu_layout;

    private XListView sortListView;
    private SideBar sideBar;
    private TextView dialog, mTvTitle;
    private SortAdapter adapter;
    private EditTextWithDel mEtSearchName;
    private List<ContactSortModel> SourceDateList = new ArrayList<>();

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
        this.mActivityFragmentView.viewMain(R.layout.activity_tongxunlu);
        this.mActivityFragmentView.viewEmpty(R.layout.activity_empty);
        this.mActivityFragmentView.viewEmptyGone();
        this.mActivityFragmentView.viewLoading(View.GONE);
        this.mActivityFragmentView.viewLoadingError(View.GONE);
        this.mActivityFragmentView.clipToPadding(true);
        ViewUtils.inject(this, this.mActivityFragmentView);
        showname_layout.setVisibility(View.GONE);
        setContentView(this.mActivityFragmentView);
        initWidget();
        initData();
    }

    @Override
    public void initWidget() {
        super.initWidget();
        initWithBar();
        mLeftTextView.setText(R.string.tv_address_book);
        mLeftTextView.setVisibility(View.VISIBLE);

        initLoadingError();
        tv_viewLoadingError.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActivityFragmentView.viewLoadingError(View.GONE);
                httpRequestMyTongxunlu();
            }
        });
    }

    @Override
    public void initData() {
        super.initData();
        initViews();
    }

    private void initViews() {
        mEtSearchName = (EditTextWithDel) findViewById(R.id.et_search);
        sideBar = (SideBar) findViewById(R.id.sidrbar);
        dialog = (TextView) findViewById(R.id.dialog);
        mTvTitle = (TextView) findViewById(R.id.tv_title);
        sortListView = (XListView) findViewById(R.id.lv_contact);
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
                mTvTitle.setText(((ContactSortModel) adapter.getItem(position1 - 1)).getName());
                name = ((ContactSortModel) adapter.getItem(position1 -1 )).getName();
                emp_NUM = ((ContactSortModel) adapter.getItem(position1 - 1)).getEmp_NUM();
                birth_DATE = ((ContactSortModel) adapter.getItem(position1 - 1)).getBirth_DATE();
                telephone = ((ContactSortModel) adapter.getItem(position1 - 1)).getTelephone();
                sex = ((ContactSortModel) adapter.getItem(position1 - 1)).getSex();
                cellphone = ((ContactSortModel) adapter.getItem(position1 - 1)).getCellphone();
                address = ((ContactSortModel) adapter.getItem(position1 - 1)).getAddress();
                position = ((ContactSortModel) adapter.getItem(position1 - 1)).getPosition();
                work_DESC = ((ContactSortModel) adapter.getItem(position1 - 1)).getWork_DESC();
                dept_NAME = ((ContactSortModel) adapter.getItem(position1 - 1)).getDept_NAME();

                Intent intent = new Intent();
                intent.setClass(getContext(),MyTongxunluDetailActivity.class);
                intent.putExtra("name",name);
                intent.putExtra("emp_NUM",emp_NUM);
                intent.putExtra("birth_Date",birth_DATE);
                intent.putExtra("telephone",telephone);
                intent.putExtra("sex",sex);
                intent.putExtra("cellphone",cellphone);
                intent.putExtra("address",address);
                intent.putExtra("position",position);
                intent.putExtra("work_DESC",work_DESC);
                intent.putExtra("dept_NAME",dept_NAME);
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

                        if("null".equals(position)||"".equals(position)){
                            position = "";
                        }
                        contactSortModel.setName(name);
                        contactSortModel.setEmp_NUM(emp_NUM);
                        contactSortModel.setBirth_DATE(birth_DATE);
                        contactSortModel.setTelephone(telephone);
                        contactSortModel.setSex(sex);
                        contactSortModel.setPosition(position);
                        contactSortModel.setAddress(address);
                        contactSortModel.setCellphone(cellphone);
                        contactSortModel.setWork_DESC(work_DESC);
                        contactSortModel.setDept_NAME(dept_NAME);

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
                            adapter = new SortAdapter(MyTongxunluActivity.this, SourceDateList);
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
