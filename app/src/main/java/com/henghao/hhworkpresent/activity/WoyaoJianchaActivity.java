package com.henghao.hhworkpresent.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.benefit.buy.library.phoneview.MultiImageSelectorActivity;
import com.benefit.buy.library.utils.tools.ToolsKit;
import com.henghao.hhworkpresent.ActivityFragmentSupport;
import com.henghao.hhworkpresent.Constant;
import com.henghao.hhworkpresent.R;
import com.henghao.hhworkpresent.adapter.JianchaYinhuanListAdpter;
import com.henghao.hhworkpresent.entity.CompanyInfoEntity;
import com.henghao.hhworkpresent.entity.JianchaMaterialEntity;
import com.henghao.hhworkpresent.entity.JianchaPersonalEntity;
import com.henghao.hhworkpresent.entity.SaveCheckTaskEntity;
import com.henghao.hhworkpresent.entity.SceneJianchaEntity;
import com.henghao.hhworkpresent.utils.SqliteDBUtils;
import com.henghao.hhworkpresent.views.CustomDialog;
import com.henghao.hhworkpresent.views.YinhuanDatabaseHelper;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 我要检查界面
 * Created by ASUS on 2017/9/4.
 */

public class WoyaoJianchaActivity extends ActivityFragmentSupport {

    @ViewInject(R.id.tv_company_name)
    private TextView tv_company_name;

    @ViewInject(R.id.tv_company_type)
    private TextView tv_company_type;

    @ViewInject(R.id.tv_check_time)
    private TextView tv_check_time;

    @ViewInject(R.id.tv_check_people)
    private TextView tv_check_people;

    @ViewInject(R.id.et_check_scene)
    private EditText et_check_scene;

    @ViewInject(R.id.et_check_person)
    private EditText et_check_person;

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

    @ViewInject(R.id.tv_woyao_checked_cancel)
    private TextView tv_woyao_checked_cancel;

    @ViewInject(R.id.check_yinhuan_listview)
    private ListView check_yinhuan_listview;

    @ViewInject(R.id.layout_problem_list)
    private LinearLayout layout_problem_list;

    private boolean isProblemListOpen = true;

    private CompanyInfoEntity.DataBean dataBean;
    private JianchaPersonalEntity jianchaPersonalEntity;
    private List<JianchaMaterialEntity> mJianchaMaterialEntityList;

    private JianchaYinhuanListAdpter jianchaYinhuanListAdpter;

    private ArrayList<String> mSelectPath;

    private static final int REQUEST_IMAGE = 0x00;

    private static final int REQUEST_CONTACTS = 0x01;

    private ArrayList<String> mImageList=new ArrayList<>();

    private ArrayList<File> mFileList = new ArrayList<>();//被选中的图片文件

    private MyReceiver myReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.mActivityFragmentView.viewMain(R.layout.activity_woyaojiancha);
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
                intent.setClass(WoyaoJianchaActivity.this,QueryYinhuanInfoActivity.class);
                intent.putExtra("JianchaMaterialEntity",mJianchaMaterialEntityList.get(position));
                startActivity(intent);
            }
        });
    }

    @Override
    public void initData() {
        super.initData();
        Intent data = getIntent();
        dataBean = (CompanyInfoEntity.DataBean)data.getSerializableExtra("dataBean");
        mJianchaMaterialEntityList = (List<JianchaMaterialEntity>)data.getSerializableExtra("mSelectDescriptData");
        jianchaPersonalEntity = (JianchaPersonalEntity) data.getSerializableExtra("checkpeople");
        String checktime = data.getStringExtra("checktime");
        tv_company_name.setText(dataBean.getEntname());
        tv_company_type.setText(dataBean.getIndustry1());
        tv_check_time.setText(checktime);

        myReceiver = new MyReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Constant.PRESS_SAVE_BUTTON);
        registerReceiver(myReceiver,filter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(myReceiver);
    }

    @OnClick({R.id.tv_personal_login,R.id.tv_start_check,R.id.image_proplem_list_up, R.id.tv_company_name_detail,
            R.id.imageview_woyaojiancha,R.id.tv_woyao_checked_save,R.id.tv_woyao_checked_cancel})
    private void viewOnClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.imageview_woyaojiancha:
                choosePicture();
                break;
            case R.id.tv_personal_login:  //陪同检查人员登录
                showLoginDialog();
                break;
            case R.id.tv_start_check:  //打开标准逐项排查
                //删除隐患数据库，把之前的删除
                deleteDatabase("threat_info.db");

                if(et_check_scene.getText().toString()==null){
                    Toast.makeText(WoyaoJianchaActivity.this,"请填写检查现场",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(et_check_person.getText().toString()==null){
                    Toast.makeText(WoyaoJianchaActivity.this,"请填写企业现场负责人",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(tv_check_people.getText().toString()==null){
                    Toast.makeText(WoyaoJianchaActivity.this,"请登录陪同执法人员",Toast.LENGTH_SHORT).show();
                    return;
                }
                intent.putExtra("mSelectDescriptData",(Serializable) mJianchaMaterialEntityList);
                intent.setClass(this,JianchaStandardActivity.class);
                startActivity(intent);
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
            case R.id.tv_company_name_detail:   //点击详细资料
                intent.setClass(this,QueryYinhuanInfoActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_woyao_checked_save:    //保存并打开现场检查文书
                //先进行保存数据到服务器的操作
            //    saveCheckedDataToService();
                //然后再打开文书页面
                bindingDataToSceneJianchaActivity();
                break;
            case R.id.tv_woyao_checked_cancel:  //取消  跳转到添加检查任务界面
                intent.setClass(this,AddJianchaTaskActivity.class);
                startActivity(intent);
                //删除隐患数据库，以防下次重新进入的时候还存在原来的数据
                deleteDatabase("threat_info.db");
                finish();
                break;
        }
    }

    /**
     * 保存数据到服务器的操作
     */
    public void saveCheckedDataToService(){
        /*SaveCheckTaskEntity saveCheckTaskEntity = new SaveCheckTaskEntity();
        saveCheckTaskEntity.setCompany_name(tv_company_name.getText().toString());
        saveCheckTaskEntity.setCheckPeople1("");
        saveCheckTaskEntity.setCheckPeople2(tv_check_people.getText().toString());
        saveCheckTaskEntity.setCheckTime(tv_check_time.getText().toString());
        saveCheckTaskEntity.setJianchaMaterialEntityList(mJianchaMaterialEntityList);
        saveCheckTaskEntity.setCheckSite(et_check_scene.getText().toString());
        saveCheckTaskEntity.setSiteResponse(et_check_person.getText().toString());
        //上传图片路径 并且还要上传图片文件
        saveCheckTaskEntity.setSiteImagePath(mSelectPath.get(0));
        OkHttpClient okHttpClient = new OkHttpClient();
        Request.Builder builder = new Request.Builder();
        //这个要和服务器保持一致 application/json;charset=UTF-8
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json;charset=UTF-8"),
                com.alibaba.fastjson.JSONObject.toJSONString(saveCheckTaskEntity));
        Request request = builder.post(requestBody).url("http://172.16.0.81:8080/istration/enforceapp/savePlan").build();
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
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        msg("数据保存成功！");
                    }
                });
            }
        });*/
    }


    /**
     * 传递数据并打开页面
     */
    public void bindingDataToSceneJianchaActivity(){
        String checkUnit = tv_company_name.getText().toString();    //被检查单位
        String checkAddress = dataBean.getProductaddress();         //单位地址
        String legalRepresentative = dataBean.getLegalpeople();     //法定代表人
        String legalDuty = null;      //法定代表人职务没有找到
        String contactNumber =  dataBean.getLegalmobilephone();     //法定代表人联系电话
        String checkSite =  et_check_scene.getText().toString();    //检查场所
        String checkTime = tv_check_time.getText().toString();      //检查时间
        String cityName = null;   //市的名字 先暂时为Null
        String checkPeople1 = new SqliteDBUtils(this).getLoginFirstName()+ new SqliteDBUtils(this).getLoginGiveName();  //检查人员1 也就是系统登录人员
        String checkPeople2 = jianchaPersonalEntity.getName();  //检查人员2 也就是被选中的执法人员
        String documentsId1 = null;  // =  执法人员1 的编号 也就是系统登录人员的员工编号
        String documentsId2 = jianchaPersonalEntity.getEmp_NUM(); //执法人员2 的证件号
        String checkCase = null;  // = 检查情况 没有数据
        List<JianchaMaterialEntity> checkYinhuanList = mJianchaMaterialEntityList;    //被选中的检查问题隐患
        String checkSignature11 = new SqliteDBUtils(this).getLoginFirstName()+ new SqliteDBUtils(this).getLoginGiveName();  //检查人员1的签名
        String checkSignature12 = jianchaPersonalEntity.getName();  //检查人员2的签名
        String beCheckedPeople = et_check_person.getText().toString();  //被检查企业现场负责人
        String recordingTime = tv_check_time.getText().toString();

        SceneJianchaEntity sceneJianchaEntity = new SceneJianchaEntity();
        sceneJianchaEntity.setCheckUnit(checkUnit);
        sceneJianchaEntity.setCheckAddress(checkAddress);
        sceneJianchaEntity.setLegalRepresentative(legalRepresentative);
        sceneJianchaEntity.setLegalDuty(legalDuty);
        sceneJianchaEntity.setContactNumber(contactNumber);
        sceneJianchaEntity.setCheckSite(checkSite);
        sceneJianchaEntity.setCheckTime(checkTime);
        sceneJianchaEntity.setCityName(cityName);
        sceneJianchaEntity.setCheckPeople1(checkPeople1);
        sceneJianchaEntity.setCheckPeople2(checkPeople2);
        sceneJianchaEntity.setDocumentsId1(documentsId1);
        sceneJianchaEntity.setDocumentsId2(documentsId2);
        sceneJianchaEntity.setCheckCase(checkCase);
        sceneJianchaEntity.setCheckYinhuanList(checkYinhuanList);
        sceneJianchaEntity.setCheckSignature11(checkSignature11);
        sceneJianchaEntity.setCheckSignature12(checkSignature12);
        sceneJianchaEntity.setBeCheckedPeople(beCheckedPeople);
        sceneJianchaEntity.setRecordingTime(recordingTime);

        Intent intent = new Intent();
        intent.setClass(this, SceneJianchaActivity.class);
        Bundle mBundle = new Bundle();
        mBundle.putSerializable("sceneJianchaEntity", sceneJianchaEntity);
        intent.putExtras(mBundle);
        startActivity(intent);
    }

    class MyReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            //监听检查标准页面保存键
            if((Constant.PRESS_SAVE_BUTTON).equals(action)){
                mJianchaMaterialEntityList = queryAllToThreat();
                setListViewHeightBasedOnChildren(check_yinhuan_listview);
                jianchaYinhuanListAdpter = new JianchaYinhuanListAdpter(WoyaoJianchaActivity.this,mJianchaMaterialEntityList);
                check_yinhuan_listview.setAdapter(jianchaYinhuanListAdpter);
                jianchaYinhuanListAdpter.notifyDataSetChanged();
            }
        }
    }

    /**
     * 从threat表中查询所有数据
     */
    public List<JianchaMaterialEntity> queryAllToThreat(){
        String sql = "select * from threat";
        YinhuanDatabaseHelper databaseHelper = new YinhuanDatabaseHelper(this,"threat_info.db");
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        List<JianchaMaterialEntity> jianchaMaterialEntityList = new ArrayList<JianchaMaterialEntity>();
        JianchaMaterialEntity jianchaMaterialEntity = null;
        Cursor cursor = db.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            jianchaMaterialEntity = new JianchaMaterialEntity();
            jianchaMaterialEntity.setFindTime(cursor.getString(cursor.getColumnIndex("threat_time")));
            jianchaMaterialEntity.setCheckDegree(cursor.getString(cursor.getColumnIndex("threat_degree")));
            jianchaMaterialEntity.setCheckPosition(cursor.getString(cursor.getColumnIndex("threat_position")));
            jianchaMaterialEntity.setCheckDescript(cursor.getString(cursor.getColumnIndex("threat_description")));
            String threat_imagepath= cursor.getString(cursor.getColumnIndex("threat_imagepath"));
            if(threat_imagepath!=null){
                String[] imagePathArr = threat_imagepath.split(";");
                List<String> mSelectPath = Arrays.asList(imagePathArr);
                jianchaMaterialEntity.setSelectImagePath(mSelectPath);
            }
            jianchaMaterialEntityList.add(jianchaMaterialEntity);
        }
        return jianchaMaterialEntityList;
    }

    /**
     * 展示登录对话框
     */
    public void showLoginDialog(){
        View customView = View.inflate(this,R.layout.layout_person_login_dialog,null);
        final EditText et_person_username = (EditText) customView.findViewById(R.id.et_person_username);
        final EditText et_person_password = (EditText) customView.findViewById(R.id.et_person_password);
        CustomDialog.Builder dialog=new CustomDialog.Builder(this);
        dialog.setTitle("陪同执法人员登录")
                .setContentView(customView)//设置自定义customView
                .setPositiveButton("登录", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        boolean isUsername = et_person_username.getText().toString()!=null && !et_person_username.getText().toString().equals("") && !et_person_username.getText().toString().equals("null");
                        boolean isPassword = et_person_password.getText().toString()!=null && !et_person_password.getText().toString().equals("") && !et_person_password.getText().toString().equals("null");
                        if(isUsername && isPassword){
                            if(jianchaPersonalEntity.getLoginid().equals(et_person_username.getText().toString()) &&
                                    jianchaPersonalEntity.getPassword().equals(et_person_password.getText().toString())){
                                tv_check_people.setText(jianchaPersonalEntity.getName());
                                tv_personal_login.setVisibility(View.GONE);
                            }else{
                                et_person_username.setText("");
                                et_person_password.setText("");
                                Toast.makeText(WoyaoJianchaActivity.this,"用户名或密码错误,请重新输入",Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        }).create().show();
    }

    public void choosePicture(){
        // 查看session是否过期
        // int selectedMode = MultiImageSelectorActivity.MODE_SINGLE;
        //设置单选模式
        int selectedMode = MultiImageSelectorActivity.MODE_SINGLE;
        int maxNum = 9;
        Intent picIntent = new Intent(this, MultiImageSelectorActivity.class);
        // 是否显示拍摄图片
        picIntent.putExtra(MultiImageSelectorActivity.EXTRA_SHOW_CAMERA, true);
        // 最大可选择图片数量
        picIntent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_COUNT, maxNum);
        // 选择模式
        picIntent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_MODE, selectedMode);
        // 默认选择
        if ((this.mSelectPath != null) && (this.mSelectPath.size() > 0)) {
            picIntent.putExtra(MultiImageSelectorActivity.EXTRA_DEFAULT_SELECTED_LIST, this.mSelectPath);
        }
        startActivityForResult(picIntent, REQUEST_IMAGE);
    }

    private String getImageName(String url) {
        return url.substring(url.lastIndexOf("/") + 1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            if (requestCode == REQUEST_IMAGE) {
                if ((resultCode == Activity.RESULT_OK) || (resultCode == Activity.RESULT_CANCELED)) {
                    this.mSelectPath = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
                    if (!ToolsKit.isEmpty(this.mSelectPath)) {
                        List<String> fileNames = new ArrayList<>();
                        mImageList.clear();
                        mFileList.clear();
                        for (String filePath : mSelectPath) {
                            String imageName = getImageName(filePath);
                            fileNames.add(imageName);
                            File file = new File(filePath);
                            mFileList.add(file);
                            Bitmap bm = BitmapFactory.decodeFile(filePath);
                            //设置图片
                            imageview_woyaojiancha.setImageBitmap(bm);
                        }
                    }
                }
            }
        }
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //删除隐患数据库，以防下次重新进入的时候还存在原来的数据
        deleteDatabase("threat_info.db");
    }
}
