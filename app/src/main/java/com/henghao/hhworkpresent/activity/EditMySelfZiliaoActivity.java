package com.henghao.hhworkpresent.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.henghao.hhworkpresent.ActivityFragmentSupport;
import com.henghao.hhworkpresent.ProtocolUrl;
import com.henghao.hhworkpresent.R;
import com.henghao.hhworkpresent.adapter.CommonListStringAdapter;
import com.henghao.hhworkpresent.utils.PopupWindowHelper;
import com.henghao.hhworkpresent.utils.SqliteDBUtils;
import com.henghao.hhworkpresent.views.DateChooseWheelViewDialog;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by bryanrady on 2017/5/19.
 * 个人资料编辑页面
 */

public class EditMySelfZiliaoActivity extends ActivityFragmentSupport {

    @ViewInject(R.id.tongxunlu_et_name)
    private TextView et_name;

    @ViewInject(R.id.tongxunlu_et_sex)
    private TextView et_sex;

    @ViewInject(R.id.tongxunlu_et_birth_DATE)
    private TextView et_birth_Date;

    @ViewInject(R.id.tongxunlu_et_emp_NUM)
    private EditText et_emp_Num;

    @ViewInject(R.id.tongxunlu_et_telephone)
    private EditText et_telePhone;

    @ViewInject(R.id.tongxunlu_et_cellphone)
    private EditText et_cellPhone;

    @ViewInject(R.id.tongxunlu_et_position)
    private EditText et_position;

    @ViewInject(R.id.tongxunlu_et_address)
    private EditText et_address;

    @ViewInject(R.id.tongxunlu_et_dept_NAME)
    private TextView et_dept_Name;

    @ViewInject(R.id.tongxunlu_et_work_DESC)
    private EditText et_work_DESC;

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

    private View popView;
    private View popView1;

    private PopupWindowHelper popupWindowHelper;
    private PopupWindowHelper popupWindowHelper1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.mActivityFragmentView.viewMain(R.layout.activity_selfziliao_edit);
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
    public void initWidget() {
        super.initWidget();
        initWithBar();
        mLeftTextView.setText("编辑个人资料");
        mLeftTextView.setVisibility(View.VISIBLE);

        this.popView = LayoutInflater.from(this).inflate(R.layout.common_android_listview, null);
        ListView mListView = (ListView) this.popView.findViewById(R.id.mlistview);
        final List<String> mList = new ArrayList<String>();
        mList.add("局办公室");
        mList.add("局领导");
        mList.add("监察室");
        mList.add("法规处");
        mList.add("职安处");
        mList.add("煤监处");
        mList.add("协调办");
        mList.add("综合科");
        mList.add("安监一处");
        mList.add("安监二处");
        mList.add("安监三处");
        mList.add("安监四处");
        CommonListStringAdapter mListStringAdapter = new CommonListStringAdapter(EditMySelfZiliaoActivity.this, mList);
        mListView.setAdapter(mListStringAdapter);
        mListStringAdapter.notifyDataSetChanged();
        this.popupWindowHelper = new PopupWindowHelper(this.popView);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                String whatSelect = mList.get(arg2);
                et_dept_Name.setText(whatSelect);
                popupWindowHelper.dismiss();
            }
        });

        this.popView1 = LayoutInflater.from(this).inflate(R.layout.common_android_listview, null);
        ListView mListView1 = (ListView) this.popView1.findViewById(R.id.mlistview);
        final List<String> mList1 = new ArrayList<String>();
        mList1.add("男");
        mList1.add("女");
        CommonListStringAdapter mListStringAdapter1 = new CommonListStringAdapter(EditMySelfZiliaoActivity.this, mList1);
        mListView1.setAdapter(mListStringAdapter1);
        mListStringAdapter1.notifyDataSetChanged();
        this.popupWindowHelper1 = new PopupWindowHelper(this.popView1);
        mListView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                String whatSelect = mList1.get(arg2);
                et_sex.setText(whatSelect);
                popupWindowHelper1.dismiss();
            }
        });

        initWithRightBar();
        mRightTextView.setText("完成");
        mRightTextView.setVisibility(View.VISIBLE);
        mRightTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //上传到服务器
                uploadPersonalInformation();
            }
        });
    }

    @OnClick({R.id.tongxunlu_et_sex,R.id.tongxunlu_et_birth_DATE,R.id.tongxunlu_et_dept_NAME})
    private void viewOnClick(View v) {
        switch (v.getId()) {
            //让部门不可编辑
            /*case R.id.tongxunlu_et_sex:
                popupWindowHelper1.showFromBottom(v);
                break;*/
            case R.id.tongxunlu_et_dept_NAME:
                popupWindowHelper.showFromBottom(v);
                break;
            case R.id.tongxunlu_et_birth_DATE:
                getDialogTime("请选择日期");
                break;
        }
    }

    private DateChooseWheelViewDialog getDialogTime(String title) {
        DateChooseWheelViewDialog startDateChooseDialog = new DateChooseWheelViewDialog(this, new DateChooseWheelViewDialog.DateChooseInterface() {
            @Override
            public void getDateTime(String time, boolean longTimeChecked) {
                et_birth_Date.setText(time);
                birth_DATE = time;
            }
        });
        startDateChooseDialog.setDateDialogTitle(title);
        startDateChooseDialog.showDateChooseDialog();
        startDateChooseDialog.setCanceledOnTouchOutside(true);
        return startDateChooseDialog;
    }

    @Override
    public void initData() {
        super.initData();
        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        emp_NUM = intent.getStringExtra("emp_NUM");
        telephone = intent.getStringExtra("telephone");
        birth_DATE = intent.getStringExtra("birth_DATE");
        sex = intent.getStringExtra("sex");
        position = intent.getStringExtra("position");
        address = intent.getStringExtra("address");
        cellphone = intent.getStringExtra("cellphone");
        work_DESC = intent.getStringExtra("work_DESC");
        dept_NAME = intent.getStringExtra("dept_NAME");

        if("0".equals(sex)){
            sex = "男";
        }else if("1".equals(sex)){
            sex = "女";
        }

        if("null".equals(position)||position==null){
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

        et_name.setText(name);
        et_sex.setText(sex);
        et_birth_Date.setText(birth_DATE);
        et_emp_Num.setText(emp_NUM);
        et_telePhone.setText(telephone);
        et_cellPhone.setText(cellphone);
        et_position.setText(position);
        et_address.setText(address);
        et_work_DESC.setText(work_DESC);
        et_dept_Name.setText(dept_NAME);
    }

    /**
     * 上传编辑后的用户个人资料
     */
    public void uploadPersonalInformation(){
        name = et_name.getText().toString().trim();
        sex = et_sex.getText().toString().trim();
        birth_DATE = et_birth_Date.getText().toString().trim();
        emp_NUM = et_emp_Num.getText().toString().trim();
        telephone = et_telePhone.getText().toString().trim();
        cellphone = et_cellPhone.getText().toString().trim();
        position = et_position.getText().toString().trim();
        address = et_address.getText().toString().trim();
        work_DESC = et_work_DESC.getText().toString().trim();
        dept_NAME = et_dept_Name.getText().toString().trim();
        if("男".equals(et_sex.getText().toString().trim())){
            sex = "0";
        }else if("女".equals(et_sex.getText().toString().trim())){
            sex = "1";
        }
        SqliteDBUtils sqliteDBUtils = new SqliteDBUtils(this);
        OkHttpClient okHttpClient = new OkHttpClient();
        Request.Builder builder = new Request.Builder();
        MultipartBuilder multipartBuilder = new MultipartBuilder();
        multipartBuilder.type(MultipartBuilder.FORM)//
                .addFormDataPart("ID", sqliteDBUtils.getLoginUid())
                .addFormDataPart("NAME",name)
                .addFormDataPart("SEX", sex)
                .addFormDataPart("BIRTH_DATE", birth_DATE)
                .addFormDataPart("EMP_NUM", emp_NUM)
                .addFormDataPart("TELEPHONE", telephone)
                .addFormDataPart("CELLPHONE", cellphone)
                .addFormDataPart("POSITION", position)
                .addFormDataPart("ADDRESS", address)
                .addFormDataPart("WORK_DESC", work_DESC)
                .addFormDataPart("DEPT_NAME", dept_NAME);

        RequestBody requestBody = multipartBuilder.build();
        Request request = builder.post(requestBody).url(ProtocolUrl.ROOT_URL + "/" + ProtocolUrl.APP_UPLOAD_MYSELF_ZILIAO).build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e){
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        msg("网络请求错误！");
                    }
                });
            }

            @Override
            public void onResponse(Response response) throws IOException {
                finish();
            }
        });
    }

}
