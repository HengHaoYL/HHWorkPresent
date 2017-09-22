package com.henghao.hhworkpresent.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.henghao.hhworkpresent.ActivityFragmentSupport;
import com.henghao.hhworkpresent.R;
import com.henghao.hhworkpresent.adapter.JianchaYinhuanNotEditListAdpter;
import com.henghao.hhworkpresent.entity.CkInspectrecord;
import com.henghao.hhworkpresent.entity.SaveCheckTaskEntity;
import com.henghao.hhworkpresent.utils.SqliteDBUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

/**
 * 我要检查界面  不可编辑状态
 * Created by ASUS on 2017/9/20.
 */

public class WoyaoJianchaNotEditActivity extends ActivityFragmentSupport {

    @ViewInject(R.id.tv_company_name)
    private TextView tv_company_name;

    @ViewInject(R.id.tv_company_type)
    private TextView tv_company_type;

    @ViewInject(R.id.tv_check_time)
    private TextView tv_check_time;

    @ViewInject(R.id.tv_check_people)
    private TextView tv_check_people;

    @ViewInject(R.id.tv_check_scene)
    private TextView tv_check_scene;

    @ViewInject(R.id.tv_check_response)
    private TextView tv_check_response;

    @ViewInject(R.id.tv_personal_login)
    private TextView tv_personal_login;

    @ViewInject(R.id.tv_start_check)
    private TextView tv_start_check;

    @ViewInject(R.id.tv_company_name_detail)
    private TextView tv_company_name_detail;

    @ViewInject(R.id.image_proplem_list_up)
    private ImageView image_proplem_list_up;

    @ViewInject(R.id.imageview_woyaojiancha)
    private ImageView imageview_woyaojiancha;

    @ViewInject(R.id.tv_woyao_checked_save)
    private TextView tv_woyao_checked_save;

    @ViewInject(R.id.check_yinhuan_listview)
    private ListView check_yinhuan_listview;

    @ViewInject(R.id.layout_problem_list)
    private LinearLayout layout_problem_list;

    private boolean isProblemListOpen = true;

    private SaveCheckTaskEntity saveCheckTaskEntity;
    private List<SaveCheckTaskEntity.JianchaMaterialEntityListBean> mJianchaMaterialEntityList;

    private JianchaYinhuanNotEditListAdpter jianchaYinhuanNotEditListAdpter;

    private ImageLoader imageLoader;

    private DisplayImageOptions options;

    public android.os.Handler mHandler = new android.os.Handler(){};

    public static int Pid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.mActivityFragmentView.viewMain(R.layout.activity_woyaojiancha_not_edit);
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
        mCenterTextView.setText("我要检查");
        mCenterTextView.setVisibility(View.VISIBLE);

        image_proplem_list_up.setImageResource(R.drawable.icon_down);

        check_yinhuan_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.setClass(WoyaoJianchaNotEditActivity.this,QueryYinhuanInfoActivity.class);
                intent.putExtra("JianchaMaterialEntity",mJianchaMaterialEntityList.get(position));
                startActivity(intent);
            }
        });
    }

    @Override
    public void initData() {
        super.initData();
        Intent data = getIntent();
        Pid = data.getIntExtra("Pid",0);

        httpRequestCheckTask();
    }
    @OnClick({R.id.tv_woyao_checked_save,R.id.image_proplem_list_up})
    private void viewOnClick(View v) {
        switch (v.getId()) {
            case R.id.tv_woyao_checked_save:
                //然后再打开文书页面
                bindingDataToSceneJianchaActivity();
                break;

            case R.id.image_proplem_list_up:  //问题列表开关
                if(isProblemListOpen){
                    image_proplem_list_up.setImageResource(R.drawable.icon_down);
                    layout_problem_list.setVisibility(View.VISIBLE);
                    isProblemListOpen = false;
                }else{
                    image_proplem_list_up.setImageResource(R.drawable.icon_up);
                    layout_problem_list.setVisibility(View.GONE);
                    isProblemListOpen = true;
                }
                break;
        }
    }

    /**
     * 根据pid查询检查任务  并把数据添加到我要检查页面
     */
    public void httpRequestCheckTask(){
        OkHttpClient okHttpClient = new OkHttpClient();
        Request.Builder builder = new Request.Builder();
        final String request_url = "http://172.16.0.81:8080/istration/enforceapp/queryplanbyid?id="+Pid;
        Request request = builder.url(request_url).build();
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
                    result_str = jsonObject.getString("data");
                    Gson gson = new Gson();
                    saveCheckTaskEntity = gson.fromJson(result_str,SaveCheckTaskEntity.class);
                    mJianchaMaterialEntityList = saveCheckTaskEntity.getJianchaMaterialEntityList();

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mActivityFragmentView.viewLoading(View.GONE);
                            tv_company_type.setText(saveCheckTaskEntity.getEnterprise().getIndustry1());
                            tv_company_name.setText(saveCheckTaskEntity.getEnterprise().getEntname());
                            tv_check_time.setText(saveCheckTaskEntity.getCheckTime());
                            tv_check_scene.setText(saveCheckTaskEntity.getCheckSite());
                            tv_check_response.setText(saveCheckTaskEntity.getSiteResponse());
                            tv_check_people.setText(saveCheckTaskEntity.getTroopemp().getName());

                            jianchaYinhuanNotEditListAdpter = new JianchaYinhuanNotEditListAdpter(WoyaoJianchaNotEditActivity.this, mJianchaMaterialEntityList);
                            setListViewHeightBasedOnChildren(check_yinhuan_listview);
                            check_yinhuan_listview.setAdapter(jianchaYinhuanNotEditListAdpter);
                            jianchaYinhuanNotEditListAdpter.notifyDataSetChanged();

                            /**
                             * 下载现场图片
                             */
                            if(saveCheckTaskEntity.getSiteImagePath()!=null){
                                options = new DisplayImageOptions.Builder()
                                        .showImageOnLoading(R.drawable.icon_logo) // 设置图片下载期间显示的图片
                                        .showImageForEmptyUri(R.drawable.icon_logo) // 设置图片Uri为空或是错误的时候显示的图片
                                        .showImageOnFail(R.drawable.icon_logo) // 设置图片加载或解码过程中发生错误显示的图片
                                        .cacheInMemory(true) // 设置下载的图片是否缓存在内存中
                                        .cacheOnDisk(true) // 设置下载的图片是否缓存在SD卡中
                                        //              .displayer(new RoundedBitmapDisplayer(20)) // 设置成圆角图片  如果使用这句代码，图片直接显示不出来。
                                        .build(); // 构建完成

                                imageLoader = ImageLoader.getInstance();
                                String imageUri = "http://172.16.0.81:8080/image/" + saveCheckTaskEntity.getSiteImagePath();
                                imageLoader.displayImage(imageUri, imageview_woyaojiancha, options);
                                mActivityFragmentView.viewLoading(View.GONE);
                            }
                        }
                    });

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        });
    }

    /**
     * 传递数据并打开页面
     */
    public void bindingDataToSceneJianchaActivity(){
        String checkUnit = tv_company_name.getText().toString();    //被检查单位
        String checkAddress = saveCheckTaskEntity.getEnterprise().getProductaddress();         //单位地址
        String legalRepresentative = saveCheckTaskEntity.getEnterprise().getLegalpeople();     //法定代表人
        String legalDuty = "";      //法定代表人职务没有找到
        String contactNumber =  saveCheckTaskEntity.getEnterprise().getLegalmobilephone();     //法定代表人联系电话
        String checkSite =  tv_check_scene.getText().toString();    //检查场所
        String checkTime = tv_check_time.getText().toString();      //检查时间
        String cityName = "";   //市的名字 先暂时为Null
        String checkPeople1 = new SqliteDBUtils(this).getLoginFirstName()+ new SqliteDBUtils(this).getLoginGiveName();  //检查人员1 也就是系统登录人员
        String checkPeople2 = saveCheckTaskEntity.getTroopemp().getName();  //检查人员2 也就是被选中的执法人员
        String documentsId1 = "";  // =  执法人员1 的编号 也就是系统登录人员的员工编号
        String documentsId2 = saveCheckTaskEntity.getTroopemp().getEmp_NUM(); //执法人员2 的证件号
        String checkCase = "";  // = 检查情况 没有数据
        List<SaveCheckTaskEntity.JianchaMaterialEntityListBean> checkYinhuanList = mJianchaMaterialEntityList;    //被选中的检查问题隐患
        String checkSignature11 = new SqliteDBUtils(this).getLoginFirstName()+ new SqliteDBUtils(this).getLoginGiveName();  //检查人员1的签名
        String checkSignature12 = saveCheckTaskEntity.getTroopemp().getName();  //检查人员2的签名
        String beCheckedPeople = tv_check_response.getText().toString();  //被检查企业现场负责人
        String recordingTime = tv_check_time.getText().toString();

        CkInspectrecord ckInspectrecord = new CkInspectrecord();
        ckInspectrecord.setCheckUnit(checkUnit);
        ckInspectrecord.setCheckAddress(checkAddress);
        ckInspectrecord.setLegalRepresentative(legalRepresentative);
        ckInspectrecord.setLegalDuty(legalDuty);
        ckInspectrecord.setContactNumber(contactNumber);
        ckInspectrecord.setCheckSite(checkSite);
        ckInspectrecord.setCheckTime1(checkTime);
        ckInspectrecord.setCheckTime2(checkTime);
        ckInspectrecord.setCityName(cityName);
        ckInspectrecord.setCheckPeople1(checkPeople1);
        ckInspectrecord.setCheckPeople2(checkPeople2);
        ckInspectrecord.setDocumentsId1(documentsId1);
        ckInspectrecord.setDocumentsId2(documentsId2);
        ckInspectrecord.setCheckCase(checkCase);
        ckInspectrecord.setCheckYinhuanList(checkYinhuanList);
        ckInspectrecord.setCheckSignature11(checkSignature11);
        ckInspectrecord.setCheckSignature12(checkSignature12);
        ckInspectrecord.setBeCheckedPeople(beCheckedPeople);
        ckInspectrecord.setRecordingTime(recordingTime);

        Intent intent = new Intent();
        intent.setClass(this, SceneJianchaActivity.class);
        Bundle mBundle = new Bundle();
        mBundle.putSerializable("ckInspectrecord", ckInspectrecord);
        mBundle.putString("Pid",String.valueOf(Pid));
        intent.putExtras(mBundle);
        startActivity(intent);
    }

    /**
     * 根据Item数设定ListView高度
     * @param listView
     */
    public void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * listAdapter.getCount());
        listView.setLayoutParams(params);
    }
}
