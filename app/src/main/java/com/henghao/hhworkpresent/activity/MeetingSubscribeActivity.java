package com.henghao.hhworkpresent.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.henghao.hhworkpresent.ActivityFragmentSupport;
import com.henghao.hhworkpresent.ProtocolUrl;
import com.henghao.hhworkpresent.R;
import com.henghao.hhworkpresent.adapter.CommonListStringAdapter;
import com.henghao.hhworkpresent.adapter.PersonnelListAdapter;
import com.henghao.hhworkpresent.entity.DeptEntity;
import com.henghao.hhworkpresent.entity.MeetingEntity;
import com.henghao.hhworkpresent.utils.PopupWindowHelper;
import com.henghao.hhworkpresent.utils.SqliteDBUtils;
import com.henghao.hhworkpresent.views.CustomDialog;
import com.henghao.hhworkpresent.views.MyDateChooseWheelViewDialog;
import com.henghao.hhworkpresent.views.XCDropDownDeptListView;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * 预约会议界面
 * Created by ASUS on 2017/9/26.
 */

public class MeetingSubscribeActivity extends ActivityFragmentSupport {

    @ViewInject(R.id.tv_meeting_people_num)
    private TextView tv_meeting_people_num;

    @ViewInject(R.id.linear_choose_meet_people)
    private LinearLayout linear_choose_meet_people;

    @ViewInject(R.id.et_meeting_theme)
    private EditText et_meeting_theme;

    @ViewInject(R.id.et_meeting_place)
    private EditText et_meeting_place;

    @ViewInject(R.id.et_meeting_wifi_ssid)
    private EditText et_meeting_wifi_ssid;

    @ViewInject(R.id.tv_meeting_start_time)
    private TextView tv_meeting_start_time;

    @ViewInject(R.id.linear_meeting_duration)
    private LinearLayout linear_meeting_duration;

    @ViewInject(R.id.tv_meeting_ok)
    private TextView tv_meeting_ok;

    @ViewInject(R.id.tv_join_meeting_people)
    private TextView tv_join_meeting_people;

    @ViewInject(R.id.linear_meeting_type)
    private LinearLayout linear_meeting_type;

    @ViewInject(R.id.tv_meeting_type)
    private TextView tv_meeting_type;

    @ViewInject(R.id.tv_meeting_duration)
    private TextView tv_meeting_duration;

    private String[] datas;
    private RadioOnClick listener = new RadioOnClick(0);

    private XCDropDownDeptListView xcDropDownDeptListView;
    private ListView personal_listview;
    private ArrayList<DeptEntity> mDeptList;

    private List<MeetingEntity.PersonnelEntity> personnelEntityList;      //查出来的人员列表

    private PersonnelListAdapter personnelListAdapter;

    private List<MeetingEntity.PersonnelEntity> mSelectPersonnelList;     //被选中的参会人员列表

    /**记录选中的条数*/
    private int checkNum;

    private View popView;
    private PopupWindowHelper popupWindowHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.mActivityFragmentView.viewMain(R.layout.activity_meeting_subscribe);
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
        mLeftTextView.setVisibility(View.VISIBLE);
        initWithCenterBar();
        mCenterTextView.setText("会议预约");
        mCenterTextView.setVisibility(View.VISIBLE);

        this.popView = LayoutInflater.from(this).inflate(R.layout.common_android_listview, null);
        ListView mListView = (ListView) this.popView.findViewById(R.id.mlistview);
        final List<String> mList = new ArrayList<String>();
        mList.add("小型会议");
        mList.add("中型会议");
        mList.add("大型会议");
        mList.add("其他类型");
        CommonListStringAdapter mListStringAdapter = new CommonListStringAdapter(this, mList);
        mListView.setAdapter(mListStringAdapter);
        mListStringAdapter.notifyDataSetChanged();
        this.popupWindowHelper = new PopupWindowHelper(this.popView);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                String whatSelect = mList.get(arg2);
                tv_meeting_type.setText(whatSelect);
                popupWindowHelper.dismiss();
            }
        });

    }

    @Override
    public void initData() {
        super.initData();
        mDeptList = new ArrayList<>();
        mSelectPersonnelList = new ArrayList<>();

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        tv_meeting_start_time.setText(format.format(new Date()));

        //查询部门集合
        httpRequestDeptList();
    }

    @OnClick({R.id.linear_choose_meet_people,R.id.tv_meeting_start_time,R.id.linear_meeting_duration,R.id.linear_meeting_type, R.id.tv_meeting_ok})
    private void viewOnClick(View v) {
        switch (v.getId()){
            case R.id.linear_choose_meet_people:
                chooseJoinMeetingPeople();
                break;
            case R.id.tv_meeting_start_time:
                getDialogTime("请选择日期");
                break;
            case R.id.linear_meeting_duration:
                showSingleChoiceButton();
                break;
            case R.id.linear_meeting_type:
                popupWindowHelper.showFromTop(v);
                break;
            case R.id.tv_meeting_ok:
                httpSaveMeetingToService();
                break;
        }
    }

    /**
     * 上传会议到服务器
     */
    public void httpSaveMeetingToService(){
        String meetingTheme = et_meeting_theme.getText().toString();
        if(meetingTheme.equals("")){
            Toast.makeText(this,"必须填写会议主题",Toast.LENGTH_SHORT).show();
            return;
        }
        String meetingPlace = et_meeting_place.getText().toString();
        if(meetingPlace.equals("")){
            Toast.makeText(this,"必须填写会议地点",Toast.LENGTH_SHORT).show();
            return;
        }
        String meetingWifiSSID = et_meeting_wifi_ssid.getText().toString();
        if(meetingWifiSSID.equals("")){
            Toast.makeText(this,"必须填写会议地点指定使用的wifi名称，否则无法实现会议签到！",Toast.LENGTH_SHORT).show();
            return;
        }
        String meetingStartTime = tv_meeting_start_time.getText().toString();
        String meetingDuration = tv_meeting_duration.getText().toString();
        if(mSelectPersonnelList == null){
            Toast.makeText(this,"请选择参会人员",Toast.LENGTH_SHORT).show();
            return;
        }
        if(mSelectPersonnelList.size()<3){
            Toast.makeText(this,"请选择至少3位以上参会人员",Toast.LENGTH_SHORT).show();
            return;
        }
        MeetingEntity meetingEntity = new MeetingEntity();
        meetingEntity.setUid(new SqliteDBUtils(this).getLoginUid());
        meetingEntity.setMeetingTheme(meetingTheme);
        meetingEntity.setMeetingPlace(meetingPlace);
        meetingEntity.setWifiSSID(meetingWifiSSID);
        meetingEntity.setMeetingStartTime(meetingStartTime);
        meetingEntity.setMeetingDuration(meetingDuration);
        meetingEntity.setMeetingType(tv_meeting_type.getText().toString());
        meetingEntity.setMeetingPeople(mSelectPersonnelList);

        OkHttpClient okHttpClient = new OkHttpClient();
        Request.Builder builder = new Request.Builder();
        //这个要和服务器保持一致 application/json;charset=UTF-8
        MultipartBuilder multipartBuilder = new MultipartBuilder();
        multipartBuilder.type(MultipartBuilder.FORM)
                .addFormDataPart("json", com.alibaba.fastjson.JSONObject.toJSONString(meetingEntity));
        RequestBody requestBody = multipartBuilder.build();
        String request_url = ProtocolUrl.ROOT_URL + ProtocolUrl.APP_ADD_MEETING_CONTENT;
        Request request = builder.post(requestBody).url(request_url).build();
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
                        finish();
                    }
                });
            }
        });

    }


    /**
     * 弹出时间选择器
     * @param title
     * @return
     */
    private MyDateChooseWheelViewDialog getDialogTime(String title) {
        MyDateChooseWheelViewDialog startDateChooseDialog = new MyDateChooseWheelViewDialog(this, new MyDateChooseWheelViewDialog.DateChooseInterface() {
            @Override
            public void getDateTime(String time, boolean longTimeChecked) {
                tv_meeting_start_time.setText(time);
            }
        });
        startDateChooseDialog.setDateDialogTitle(title);
        startDateChooseDialog.showDateChooseDialog();
        startDateChooseDialog.setCanceledOnTouchOutside(true);
        return startDateChooseDialog;
    }

    /**
     * 查询部门集合
     */
    public void httpRequestDeptList(){
        OkHttpClient okHttpClient = new OkHttpClient();
        Request.Builder builder = new Request.Builder();
        String request_url = ProtocolUrl.ROOT_URL + ProtocolUrl.APP_QUERY_DEPT_LIST;
        Request request = builder.url(request_url).build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
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
                    Type type = new TypeToken<ArrayList<DeptEntity>>() {}.getType();
                    mDeptList = gson.fromJson(result_str,type);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 根据部门Id请求相应队伍的部门人员列表
     */
    private void httpRequestJianchaPersonalInfo(String deptId){
        OkHttpClient okHttpClient = new OkHttpClient();
        Request.Builder builder = new Request.Builder();
        MultipartBuilder multipartBuilder = new MultipartBuilder();
        multipartBuilder.type(MultipartBuilder.FORM)
                .addFormDataPart("id", deptId);
        RequestBody requestBody = multipartBuilder.build();
        String request_url = ProtocolUrl.ROOT_URL + ProtocolUrl.APP_QUERY_PERSONAL_LIST;
        Request request = builder.post(requestBody).url(request_url).build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
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
                    Type type = new TypeToken<ArrayList<MeetingEntity.PersonnelEntity>>() {}.getType();
                    personnelEntityList = gson.fromJson(result_str,type);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            personnelListAdapter = new PersonnelListAdapter(MeetingSubscribeActivity.this,personnelEntityList);
                            personal_listview.setAdapter(personnelListAdapter);
                            personnelListAdapter.notifyDataSetChanged();
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    /**
     * 选择参会人员
     */
    public void chooseJoinMeetingPeople(){
        View customView = View.inflate(this,R.layout.layout_list_dialog,null);
        xcDropDownDeptListView = (XCDropDownDeptListView) customView.findViewById(R.id.xCDropDownListView);
        TextView tv_zhifaduiwu = (TextView) customView.findViewById(R.id.tv_zhifaduiwu);
        tv_zhifaduiwu.setText("部门");
        personal_listview = (ListView) customView.findViewById(R.id.personal_listview);
        xcDropDownDeptListView.setItemsData(mDeptList);

        //传空id代表查询全部人员
        httpRequestJianchaPersonalInfo("");

        mSelectPersonnelList.clear();
        personal_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PersonnelListAdapter.HodlerView holder = (PersonnelListAdapter.HodlerView) view.getTag();
                personnelListAdapter.getIsSelected().put(position,true);
                holder.personal_checkbox.toggle();
                personnelListAdapter.getIsSelected().put(position, holder.personal_checkbox.isChecked());
            }
        });
        CustomDialog.Builder dialog=new CustomDialog.Builder(this);
        dialog.setTitle("选择参会人员")
                .setContentView(customView)//设置自定义customView
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        //得到参选人的集合
                        HashMap<Integer, Boolean> isSelected = PersonnelListAdapter.getIsSelected();
                        for(int j=0;j<personnelEntityList.size();j++){
                            if(isSelected.get(j)){      //如果被选中
                                mSelectPersonnelList.add(personnelEntityList.get(j));
                            }
                        }
                        tv_meeting_people_num.setText(mSelectPersonnelList.size()+"人");
                        StringBuilder stringBuilder = new StringBuilder();
                        for(MeetingEntity.PersonnelEntity personnelEntity : mSelectPersonnelList){
                            stringBuilder.append(personnelEntity.getName()+";");
                            tv_join_meeting_people.setText(stringBuilder);
                        }
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        }).create().show();

        xcDropDownDeptListView.setOnItemClickXCDropDownListViewListener(new XCDropDownDeptListView.XCDropDownListViewListener() {
            @Override
            public void getItemData(DeptEntity deptEntity) {
                if(personnelEntityList!=null){
                    personnelEntityList.clear();
                }
                httpRequestJianchaPersonalInfo(deptEntity.getId());
            }
        });
    }

    /**
     * 展示单选对话框
     */
    public void showSingleChoiceButton() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        datas = new String[]{"30分钟","1个小时","1个半小时","2个小时"};
        builder.setSingleChoiceItems(datas, listener.getIndex(), listener);
        builder.show();
    }

    class RadioOnClick implements DialogInterface.OnClickListener {

        private int index;

        public RadioOnClick(int index) {
            this.index = index;
        }

        public void setIndex(int index) {
            this.index = index;
        }

        public int getIndex() {
            return index;
        }

        @Override
        public void onClick(DialogInterface dialog, int whichButton){
            setIndex(whichButton);
            tv_meeting_duration.setText(datas[index]);
            dialog.dismiss();
        }
    }
}
