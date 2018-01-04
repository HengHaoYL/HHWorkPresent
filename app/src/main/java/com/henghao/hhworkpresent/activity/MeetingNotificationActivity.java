package com.henghao.hhworkpresent.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.henghao.hhworkpresent.ActivityFragmentSupport;
import com.henghao.hhworkpresent.ProtocolUrl;
import com.henghao.hhworkpresent.R;
import com.henghao.hhworkpresent.adapter.PersonnelListAdapter;
import com.henghao.hhworkpresent.entity.DeptEntity;
import com.henghao.hhworkpresent.entity.JPushToUser;
import com.henghao.hhworkpresent.entity.MeetingEntity;
import com.henghao.hhworkpresent.entity.MeetingTrajectoryEntity;
import com.henghao.hhworkpresent.utils.SqliteDBUtils;
import com.henghao.hhworkpresent.views.CustomDialog;
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
import java.util.ArrayList;
import java.util.List;

/**
 * 通知开会的界面
 * Created by ASUS on 2017/9/27.
 */

public class MeetingNotificationActivity extends ActivityFragmentSupport {

    @ViewInject(R.id.tv_meeting_notification)
    private TextView tv_meeting_notification;

    @ViewInject(R.id.tv_notification_time)
    private TextView tv_notification_time;

    @ViewInject(R.id.tv_meeting_faqiren)
    private TextView tv_meeting_faqiren;

    @ViewInject(R.id.btn_notification_join)
    private Button btn_notification_join;

    @ViewInject(R.id.btn_notification_not_join)
    private Button btn_notification_not_join;

    @ViewInject(R.id.relativelayout_btn)
    private RelativeLayout relativelayout_btn;

    private XCDropDownDeptListView xcDropDownDeptListView;
    private ListView personal_listview;
    private ArrayList<DeptEntity> mDeptList;

    private List<MeetingEntity.PersonnelEntity> personnelEntityList;      //查出来的人员列表
    private PersonnelListAdapter personnelListAdapter;
    private List<MeetingEntity.PersonnelEntity> mSelectPersonnelList;     //被选中的参会人员列表

    private int mid;
    private long msg_id;
    private Handler mHandler = new Handler(){};
    private MeetingEntity meetingEntity;
    private MeetingTrajectoryEntity meetingTrajectoryEntity;
    private SqliteDBUtils sqliteDBUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.mActivityFragmentView.viewMain(R.layout.activity_meeting_notification);
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
        mCenterTextView.setText("会议召开通知");
        mCenterTextView.setVisibility(View.VISIBLE);
    }

    @Override
    public void initData() {
        super.initData();
        sqliteDBUtils = new SqliteDBUtils(this);
        mDeptList = new ArrayList<>();
        mSelectPersonnelList = new ArrayList<>();
        Intent intent = getIntent();
        msg_id = intent.getLongExtra("msg_id",0);
    }

    @Override
    public void onResume() {
        super.onResume();
        httpRequestMeetingContent();
    }

    @OnClick({R.id.btn_notification_join,R.id.btn_notification_not_join})
    private void viewOnClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()){
            case R.id.btn_notification_join:        //准时参加
                intent.setClass(this,MeetingQiandaoActivity.class);
                intent.putExtra("meetingEntity",meetingEntity);
                intent.putExtra("meetingTrajectoryEntity",meetingTrajectoryEntity);
                intent.putExtra("msg_id",msg_id);
                startActivity(intent);
                break;
            case R.id.btn_notification_not_join:    //找人代替
                chooseJoinMeetingPeople();
                break;
        }
    }

    /**
     * 选择参会人员
     */
    private void chooseJoinMeetingPeople(){
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
        dialog.setTitle("选择替代参会人员")
                .setContentView(customView)//设置自定义customView
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        for(MeetingEntity.PersonnelEntity personnelEntity : mSelectPersonnelList){
                            //得到被选中的人
                            chooseReplacedMeeting(String.valueOf(personnelEntity.getId()));
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
     * 根据执法队伍Id请求相应队伍的执法人员列表  GET
     */
    private void httpRequestJianchaPersonalInfo(String teamId){
        OkHttpClient okHttpClient = new OkHttpClient();
        Request.Builder builder = new Request.Builder();
        MultipartBuilder multipartBuilder = new MultipartBuilder();
        multipartBuilder.type(MultipartBuilder.FORM)
                .addFormDataPart("id", teamId);
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
                    int status = jsonObject.getInt("status");
                    if(status==0) {
                        result_str = jsonObject.getString("data");
                        Gson gson = new Gson();
                        Type type = new TypeToken<ArrayList<MeetingEntity.PersonnelEntity>>() {}.getType();
                        personnelEntityList = gson.fromJson(result_str,type);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                personnelListAdapter = new PersonnelListAdapter(MeetingNotificationActivity.this,personnelEntityList);
                                personal_listview.setAdapter(personnelListAdapter);
                                personnelListAdapter.notifyDataSetChanged();
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
     * 查询指定的消息
     */
    public void httpRequestMeetingContent(){
        OkHttpClient okHttpClient = new OkHttpClient();
        Request.Builder builder = new Request.Builder();
        MultipartBuilder multipartBuilder = new MultipartBuilder();
        multipartBuilder.type(MultipartBuilder.FORM)
                .addFormDataPart("msg_id", String.valueOf(msg_id))
                .addFormDataPart("uid", new SqliteDBUtils(this).getLoginUid());
        RequestBody requestBody = multipartBuilder.build();
        //这个接口用在很多地方 都是显示用的
        String request_url = ProtocolUrl.ROOT_URL + ProtocolUrl.APP_QUERY_TUI_SONG_MESSAGE;
        Request request = builder.post(requestBody).url(request_url).build();
        Call call = okHttpClient.newCall(request);
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
                    if(status==0) {
                        result_str = jsonObject.getString("data");
                        Gson gson = new Gson();
                        meetingEntity = gson.fromJson(result_str,MeetingEntity.class);
                        mid = meetingEntity.getMid();
                        meetingTrajectoryEntity = meetingEntity.getMeetingTrajectoryEntity();
                        final List<JPushToUser> jPushToUserList = meetingEntity.getjPushToUser();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                for(JPushToUser jPushToUser : jPushToUserList){
                                    if(jPushToUser.getMsg_id()==msg_id){
                                        tv_meeting_notification.setText("你好，请于"+meetingEntity.getMeetingStartTime()+"在"+meetingEntity.getMeetingPlace()
                                                +"参加" +jPushToUser.getMessageSendPeople()+"发起的主题为"+meetingEntity.getMeetingTheme()
                                                +"的会议，并在抵达会议地点的时候在会议的时候连接名字为"+meetingEntity.getWifiSSID()+"的wifi进行签到，谢谢！");
                                        tv_meeting_faqiren.setText("会议发起人："+jPushToUser.getMessageSendPeople());
                                        tv_notification_time.setText(jPushToUser.getMessageSendTime());
                                    }
                                }
                                if(meetingTrajectoryEntity==null){  //A选择B代替，B再选择C代替就会出现这种情况，因为后台会把B角色的数据没有，所以会返回Null.
                                    relativelayout_btn.setVisibility(View.GONE);
                                }else if (meetingTrajectoryEntity!=null){
                                    if(meetingTrajectoryEntity.getSubstitute()!=null && !meetingTrajectoryEntity.getSubstitute().equals(sqliteDBUtils.getLoginUid())){      //表示已经找人代替自己去开会或会议纪要已经提交过了
                                        relativelayout_btn.setVisibility(View.GONE);
                                    }
                                    if(meetingTrajectoryEntity.getMeetingSummary()!=null){  //会议纪要已经提交过了
                                        relativelayout_btn.setVisibility(View.GONE);
                                    }
                                }
                                if(meetingTrajectoryEntity.getStartSignInTime()!=null){     //如果签到过了，就只显示会议签到按钮，找人代替按钮隐藏，不能选人代替了
                                    btn_notification_not_join.setVisibility(View.GONE);
                                }
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
     * 选择代替人开会的接口
     */
    public void chooseReplacedMeeting(String fungibleId){
        OkHttpClient okHttpClient = new OkHttpClient();
        Request.Builder builder = new Request.Builder();
        MultipartBuilder multipartBuilder = new MultipartBuilder();
        multipartBuilder.type(MultipartBuilder.FORM)
                .addFormDataPart("mid", String.valueOf(mid))
                .addFormDataPart("supersededId", new SqliteDBUtils(this).getLoginUid())      //本身id
                .addFormDataPart("fungibleId", fungibleId);     //代替开会人的id
        RequestBody requestBody = multipartBuilder.build();
        String request_url = ProtocolUrl.ROOT_URL + ProtocolUrl.APP_CHOOSE_REPLACE_PEOPLE;
        Request request = builder.post(requestBody).url(request_url).build();
        Call call = okHttpClient.newCall(request);
        mActivityFragmentView.viewLoading(View.VISIBLE);
        btn_notification_not_join.setEnabled(false);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mActivityFragmentView.viewLoading(View.GONE);
                        msg("网络请求错误！");
                    }
                });
            }

            @Override
            public void onResponse(Response response) throws IOException {
                String result_str = response.body().string();
                try {
                    JSONObject jsonObject = new JSONObject(result_str);
                    int status = jsonObject.getInt("status");
                    if(status==0) {
                        final String resultStr = jsonObject.getString("data");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if(resultStr.equals("null")){     //代表选择人成功
                                    mActivityFragmentView.viewLoading(View.GONE);
                                    relativelayout_btn.setVisibility(View.GONE);     //选择替代人成功之后把会议签到去掉
                                    msg("选择代替开会人员成功!");
                                }else{
                                    msg(resultStr);
                                }
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        });
    }
}
