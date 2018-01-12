package com.henghao.hhworkpresent.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.Resources;
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

import com.benefit.buy.library.utils.ToastUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.henghao.hhworkpresent.ActivityFragmentSupport;
import com.henghao.hhworkpresent.Constant;
import com.henghao.hhworkpresent.ProtocolUrl;
import com.henghao.hhworkpresent.R;
import com.henghao.hhworkpresent.adapter.ListStringAdapter;
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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    @ViewInject(R.id.linear_meeting_shenpiren)
    private LinearLayout linear_meeting_shenpiren;

/*    @ViewInject(R.id.tv_meeting_ok)
    private TextView tv_meeting_ok;*/

    @ViewInject(R.id.tv_join_meeting_people)
    private TextView tv_join_meeting_people;

    @ViewInject(R.id.linear_meeting_type)
    private LinearLayout linear_meeting_type;

    @ViewInject(R.id.tv_meeting_type)
    private TextView tv_meeting_type;

    @ViewInject(R.id.tv_meeting_duration)
    private TextView tv_meeting_duration;

    @ViewInject(R.id.tv_meeting_shenpiren)
    private TextView tv_meeting_shenpiren;

    @ViewInject(R.id.tv_meeting_clear)
    private TextView tv_meeting_clear;

    private String[] datas;
    private RadioOnClick listener = new RadioOnClick(0);

    private XCDropDownDeptListView xcDropDownDeptListView;
    private ListView personal_listview;
    private ArrayList<DeptEntity> mDeptList;

    private List<MeetingEntity.PersonnelEntity> personnelEntityList;      //查出来的人员列表

    private PersonnelListAdapter personnelListAdapter;

    private List<MeetingEntity.PersonnelEntity> mSelectPersonnelList;

    /**记录选中的条数*/
    private int checkNum;

    private View popView;
    private PopupWindowHelper popupWindowHelper;

    private String meetingTheme;
    private String meetingPlace;
    private String meetingWifiSSID;
    private String meetingStartTime;
    private String meetingType;
    private String meetingDuration;
    private String leadId;

    private Resources res;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme);
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
        res = this.getResources();

        initWithBar();
        mLeftTextView.setVisibility(View.VISIBLE);
        initWithCenterBar();
        mCenterTextView.setText(R.string.meeting_subscirbe);
        mCenterTextView.setVisibility(View.VISIBLE);
        initWithRightBar();
        mRightTextView.setText(getString(R.string.submit));
        mRightTextView.setVisibility(View.VISIBLE);
        mRightTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                httpSaveMeetingToService();
            }
        });

        this.popView = LayoutInflater.from(this).inflate(R.layout.common_android_listview, null);
        ListView mListView = (ListView) this.popView.findViewById(R.id.mlistview);
        String[] meetingTypeArr = res.getStringArray(R.array.meeting_type);
        final List<String> mList = new ArrayList<String>();
        for(int i=0;i<meetingTypeArr.length;i++){
            mList.add(meetingTypeArr[i]);
        }
        ListStringAdapter mListStringAdapter = new ListStringAdapter(this, mList);
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

        SimpleDateFormat format = new SimpleDateFormat(Constant.DATE_TIME_MM_FORMAT);
        tv_meeting_start_time.setText(format.format(new Date()));

        //查询部门集合
        httpRequestDeptList();
    }

    @OnClick({R.id.linear_choose_meet_people,R.id.tv_meeting_start_time,R.id.linear_meeting_duration,R.id.linear_meeting_type,R.id.linear_meeting_shenpiren,R.id.tv_meeting_clear})
    private void viewOnClick(View v) {
        switch (v.getId()){
            case R.id.linear_choose_meet_people:
                chooseJoinMeetingPeople();
                break;
            case R.id.tv_meeting_start_time:
                getDialogTime(getString(R.string.tv_choose_date));
                break;
            case R.id.linear_meeting_duration:
                showSingleChoiceButton();
                break;
            case R.id.linear_meeting_type:
                popupWindowHelper.showFromTop(v);
                break;
            case R.id.linear_meeting_shenpiren: //选择审批人
                chooseMeetingShenpiren();
                break;
            case R.id.tv_meeting_clear:
                mSelectPersonnelList.clear();
                tv_meeting_clear.setVisibility(View.GONE);
                tv_meeting_people_num.setText(R.string.meeting_zero_people);
                tv_join_meeting_people.setText(R.string.tv_null);
                break;
        }
    }

    /**
     * 选择会议审批人
     */
    private void chooseMeetingShenpiren(){
        View customView = View.inflate(this,R.layout.layout_list_dialog,null);
        xcDropDownDeptListView = (XCDropDownDeptListView) customView.findViewById(R.id.xCDropDownListView);
        TextView tv_zhifaduiwu = (TextView) customView.findViewById(R.id.tv_zhifaduiwu);
        tv_zhifaduiwu.setText(R.string.dept);
        personal_listview = (ListView) customView.findViewById(R.id.personal_listview);
        xcDropDownDeptListView.setItemsData(mDeptList);

        //传空id代表查询全部人员
        httpRequestJianchaPersonalInfo(getString(R.string.tv_null));

        mSelectPersonnelList.clear();
        personal_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PersonnelListAdapter.HodlerView holder = (PersonnelListAdapter.HodlerView) view.getTag();
                personnelListAdapter.getIsSelected().put(position,true);
                holder.personal_checkbox.toggle();
                personnelListAdapter.getIsSelected().put(position, holder.personal_checkbox.isChecked());

                //使用checkbox实现单选功能
                for (int i = 0; i < personnelEntityList.size(); i++) {
                    personnelListAdapter.getIsSelected().put(i, false);
                    personnelListAdapter.notifyDataSetChanged();
                }
                personnelListAdapter.getIsSelected().put(position,true);
                mSelectPersonnelList.add((MeetingEntity.PersonnelEntity) personnelListAdapter.getItem(position));
                personnelListAdapter.notifyDataSetChanged();
            }
        });
        CustomDialog.Builder dialog=new CustomDialog.Builder(this);
        dialog.setTitle(R.string.choose_meeting_approver)
                .setContentView(customView)//设置自定义customView
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        for(MeetingEntity.PersonnelEntity personnelEntity : mSelectPersonnelList){
                            //得到被选中的人的id
                            leadId = personnelEntity.getId();
                            tv_meeting_shenpiren.setText(personnelEntity.getName());
                        }
                    }
                }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
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
     * 上传会议到服务器
     */
    private void httpSaveMeetingToService(){
        meetingTheme = et_meeting_theme.getText().toString();
        if(meetingTheme.equals("")){
            ToastUtils.show(this,R.string.meetingTheme_mustbe_selected);
            return;
        }

        meetingPlace = et_meeting_place.getText().toString();
        if(meetingPlace.equals("")){
            ToastUtils.show(this,R.string.meetingPlace_mustbe_selected);
            return;
        }
        meetingWifiSSID = et_meeting_wifi_ssid.getText().toString();
        if(meetingWifiSSID.equals("")){
            ToastUtils.show(this,R.string.meetingWifi_mustbe_selected);
            return;
        }

        meetingType = tv_meeting_type.getText().toString();
        if(meetingType.equals("")){
            ToastUtils.show(this,R.string.meetingType_mustbe_selected);
            return;
        }

        if(leadId.equals("")){
            ToastUtils.show(this,R.string.meetingapprover_mustbe_selected);
            return;
        }

        meetingStartTime = tv_meeting_start_time.getText().toString();
        meetingDuration = tv_meeting_duration.getText().toString();
        if(mSelectPersonnelList == null){
            ToastUtils.show(this,R.string.choose_meeting_people);
            return;
        }
        if(mSelectPersonnelList.size()<3){
            ToastUtils.show(this,R.string.choose_three_people);
            return;
        }

        MeetingEntity meetingEntity = new MeetingEntity();
        meetingEntity.setUid(new SqliteDBUtils(this).getLoginUid());
        meetingEntity.setLeadId(leadId);
        meetingEntity.setMeetingTheme(meetingTheme);
        meetingEntity.setMeetingPlace(meetingPlace);
        meetingEntity.setWifiSSID(meetingWifiSSID);
        meetingEntity.setMeetingStartTime(meetingStartTime);
        meetingEntity.setMeetingDuration(meetingDuration);
        meetingEntity.setMeetingType(meetingType);
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
        mActivityFragmentView.viewLoading(View.VISIBLE);
        mRightTextView.setEnabled(false);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e){
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mActivityFragmentView.viewLoading(View.GONE);
                        ToastUtils.show(getContext(),R.string.app_network_failure);
                    }
                });
            }

            @Override
            public void onResponse(Response response) throws IOException {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mActivityFragmentView.viewLoading(View.GONE);
                        msg(getString(R.string.app_upload_succeed));
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
    private void httpRequestDeptList(){
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
                        ToastUtils.show(getContext(),R.string.app_network_failure);
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
                        ToastUtils.show(getContext(),R.string.app_network_failure);
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
    private void chooseJoinMeetingPeople(){
        View customView = View.inflate(this,R.layout.layout_list_dialog,null);
        xcDropDownDeptListView = (XCDropDownDeptListView) customView.findViewById(R.id.xCDropDownListView);
        TextView tv_zhifaduiwu = (TextView) customView.findViewById(R.id.tv_zhifaduiwu);
        tv_zhifaduiwu.setText(R.string.dept);
        personal_listview = (ListView) customView.findViewById(R.id.personal_listview);
        xcDropDownDeptListView.setItemsData(mDeptList);

        //传空id代表查询全部人员
        httpRequestJianchaPersonalInfo(getString(R.string.tv_null));

        CustomDialog.Builder dialog=new CustomDialog.Builder(this);
        dialog.setTitle(R.string.choose_meeting_people)
                .setContentView(customView)
                .setPositiveButton( R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        HashMap<Integer, Boolean> isSelected = PersonnelListAdapter.getIsSelected();
                        for(int j=0;j<personnelEntityList.size();j++) {
                            if(isSelected.get(j)) {      //如果被选中
                                mSelectPersonnelList.add(personnelEntityList.get(j));
                            }
                        }

                        //去重复
                        Set<MeetingEntity.PersonnelEntity> set = new HashSet<>();
                        set.addAll(mSelectPersonnelList);

                        tv_meeting_people_num.setText(set.size() + getString(R.string.people));
                        StringBuilder stringBuilder = new StringBuilder();
                        for(MeetingEntity.PersonnelEntity personnelEntity : set){
                            stringBuilder.append(personnelEntity.getName()+ getString(R.string.semicolon));
                            tv_join_meeting_people.setText(stringBuilder);
                        }

                        if(set.size()>0){
                            tv_meeting_clear.setVisibility(View.VISIBLE);
                        }
                    }
                }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        }).create().show();

    //    mSelectPersonnelList.clear();
        personal_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PersonnelListAdapter.HodlerView holder = (PersonnelListAdapter.HodlerView) view.getTag();
                personnelListAdapter.getIsSelected().put(position,true);
                holder.personal_checkbox.toggle();
                personnelListAdapter.getIsSelected().put(position, holder.personal_checkbox.isChecked());
            }
        });

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
    private void showSingleChoiceButton() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        datas = res.getStringArray(R.array.meeting_duration);
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
