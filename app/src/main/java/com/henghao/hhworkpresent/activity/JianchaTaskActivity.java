package com.henghao.hhworkpresent.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.henghao.hhworkpresent.ActivityFragmentSupport;
import com.henghao.hhworkpresent.R;
import com.henghao.hhworkpresent.adapter.BaseManageGridAdapter;
import com.henghao.hhworkpresent.adapter.DescriptListAdapter;
import com.henghao.hhworkpresent.adapter.DialogItemsListAdapter;
import com.henghao.hhworkpresent.adapter.JianchaPersonalListAdapter;
import com.henghao.hhworkpresent.adapter.SiteManageGridAdapter;
import com.henghao.hhworkpresent.entity.CompanyInfoEntity;
import com.henghao.hhworkpresent.entity.JianchaMaterialEntity;
import com.henghao.hhworkpresent.entity.JianchaPersonalEntity;
import com.henghao.hhworkpresent.entity.JianchaTeamEntity;
import com.henghao.hhworkpresent.views.CustomDialog;
import com.henghao.hhworkpresent.views.ListViewForScrollView;
import com.henghao.hhworkpresent.views.XCDropDownListView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * 根据选择的公司添加检查任务界面
 * Created by ASUS on 2017/9/6.
 */

public class JianchaTaskActivity extends ActivityFragmentSupport {

    @ViewInject(R.id.tv_company_name)
    private TextView tv_company_name;

    @ViewInject(R.id.gridview_base)
    private GridView base_gridview;

    @ViewInject(R.id.gridview_site)
    private GridView site_gridview;

    @ViewInject(R.id.tv_descript_save)
    private TextView tv_descript_save;

    @ViewInject(R.id.tv_descript_save_into)
    private TextView tv_descript_save_into;

    @ViewInject(R.id.descript_layout)
    private LinearLayout descript_layout;

    @ViewInject(R.id.image_base_up)
    private ImageView image_base_up;

    @ViewInject(R.id.image_site_up)
    private ImageView image_site_up;

    @ViewInject(R.id.image_descript_up)
    private ImageView image_descript_up;

    private boolean isBaseOpen = true;
    private boolean isSiteOpen = true;
    private boolean isDescriptOpen = true;

    private List<String> mBaseList; //基础管理部分item数据
    private List<String> mSiteList;  //现场管理部分item数据
    private BaseManageGridAdapter mBaseManageGridAdapter;
    private SiteManageGridAdapter mSiteManageGridAdapter;
    public boolean checkBaseState = true; //基础管理部分的 表示全选
    public boolean checkSiteState = true; //现场管理部分的 表示全选

    private List<JianchaMaterialEntity> mSelectBaseData; //基础管理部分 勾选item项产生的数据
    private List<Integer> mPostionBaseList; //基础管理部分 被勾选的item集合

    private List<JianchaMaterialEntity> mSelectSiteData; //基础管理部分 勾选item项产生的数据
    private List<Integer> mPostionSiteList; //基础管理部分 被勾选的item集合
    private List<JianchaMaterialEntity> mSelectTotalData;  //基础和现场管理部分 加起来被选中的数据

    private List<JianchaPersonalEntity> mJianchaPersonalEntityList;  //检查人员集合

    private ListView personal_listview;
    private JianchaPersonalListAdapter jianchaPersonalListAdapter;
    private List<JianchaPersonalEntity> mSelectedPersonalData;  //被选中的个人列表集合

    private DialogItemsListAdapter dialogItemsListAdapter;

    @ViewInject(R.id.selected_descript_listview)
    private ListViewForScrollView selected_descript_listview;
    private List<JianchaMaterialEntity> mSelectDescriptData;  //被勾选的检查材料文书集合
    private DescriptListAdapter descriptListAdapter;

    private CompanyInfoEntity.DataBean dataBean; //公司信息对象

    @ViewInject(R.id.tv_check_table)
    private TextView tv_check_table;

    @ViewInject(R.id.tv_personal)
    private TextView tv_personal;

    @ViewInject(R.id.et_check_time)
    private EditText et_check_time;

    private XCDropDownListView xCDropDownListView;

    private ArrayList<JianchaTeamEntity> jianchaTeamEntityList;  //检查队伍集合

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.mActivityFragmentView.viewMain(R.layout.activity_jiancharenwu);
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
        mCenterTextView.setText("添加检查任务");
        mCenterTextView.setVisibility(View.VISIBLE);

        image_base_up.setImageResource(R.drawable.icon_down);
        image_site_up.setImageResource(R.drawable.icon_down);

        descript_layout.setVisibility(View.GONE);  //进来先将材料文书列表描述布局隐藏
    }

    @Override
    public void initData() {
        super.initData();
        Intent data = getIntent();
        dataBean = (CompanyInfoEntity.DataBean)data.getSerializableExtra("dataBean");
        tv_company_name.setText(dataBean.getEntname());

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        et_check_time.setText(format.format(new Date()));

        httpRequestJianchaTeamList();
        getListData();

        mPostionBaseList = new ArrayList<>();
        mSelectBaseData = new ArrayList<>();
        mPostionSiteList = new ArrayList<>();
        mSelectSiteData = new ArrayList<>();
        mSelectTotalData = new ArrayList<>();
        mSelectedPersonalData = new ArrayList<>();
        mJianchaPersonalEntityList = new ArrayList<>();

        mBaseManageGridAdapter = new BaseManageGridAdapter(this,mBaseList);
        mSiteManageGridAdapter = new SiteManageGridAdapter(this,mSiteList);
        base_gridview.setAdapter(mBaseManageGridAdapter);
        site_gridview.setAdapter(mSiteManageGridAdapter);

        getDialogData();

    }

    private void getDialogData(){
        //基础管理部分
        base_gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                BaseManageGridAdapter.HodlerView holder = (BaseManageGridAdapter.HodlerView) view.getTag();
                // 改变CheckBox的状态
                holder.checkbox_manage.toggle();
                // 将CheckBox的选中状况记录下来
                mBaseManageGridAdapter.getIsSelected().put(position, holder.checkbox_manage.isChecked());
                //全选、全不选
                if(position==0){
                    if(checkBaseState){
                        // 遍历list的长度，将Adapter中的map值全部设为true
                        for (int i = 0; i < 14; i++) {
                            mBaseManageGridAdapter.getIsSelected().put(i, true);
                            mBaseManageGridAdapter.notifyDataSetChanged();
                        }
                        checkBaseState = false;
                    }else{
                        // 遍历list的长度，将Adapter中的map值全部设为false
                        for (int i = 0; i < 14; i++) {
                            mBaseManageGridAdapter.getIsSelected().put(i, false);
                            mBaseManageGridAdapter.notifyDataSetChanged();
                        }
                        checkBaseState = true;
                    }
                }
                //表示有选中的item
                if(mBaseManageGridAdapter.getIsSelected().get(position)){
                    mPostionBaseList.add(position);
                    if(position==0){
                        mPostionBaseList.clear();
                        //0代表全选反选按钮、不添加
                        for(int i = 1; i < 14; i++){
                            mPostionBaseList.add(i);
                        }
                    }
                }else{
                    mPostionBaseList.clear();
                    mSelectBaseData.clear();
                }
                getDialogBaseData();

            }
        });

        //现场管理部分
        site_gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SiteManageGridAdapter.HodlerView holder = (SiteManageGridAdapter.HodlerView) view.getTag();
                holder.checkbox_manage.toggle();
                mSiteManageGridAdapter.getIsSelected().put(position, holder.checkbox_manage.isChecked());
                //全选、全不选
                if(position==0){
                    if(checkSiteState){
                        for (int i = 0; i < 13; i++) {
                            mSiteManageGridAdapter.getIsSelected().put(i, true);
                            mSiteManageGridAdapter.notifyDataSetChanged();
                        }
                        checkSiteState = false;
                    }else{
                        for (int i = 0; i < 13; i++) {
                            mSiteManageGridAdapter.getIsSelected().put(i, false);
                            mSiteManageGridAdapter.notifyDataSetChanged();
                        }
                        checkSiteState = true;
                    }
                }
                if(mSiteManageGridAdapter.getIsSelected().get(position)){
                    mPostionSiteList.add(position);
                    if(position==0){
                        mPostionSiteList.clear();
                        for(int i = 1; i < 13; i++){
                            mPostionSiteList.add(i);
                        }
                    }
                }else{
                    mPostionSiteList.clear();
                    mSelectSiteData.clear();
                }
                getDialogSiteData();
            }
        });
    }

    /**
     * 基础管理部分 获取填充对话框的数据 ，根据点击的Item获取数据来源
     */
    public void getDialogBaseData(){
        for(int i = 0; i < mPostionBaseList.size(); i++){
            int itemId = mPostionBaseList.get(i);
            switch (itemId){
                case 1:
                    JianchaMaterialEntity jianchaMaterialEntity1 = new JianchaMaterialEntity();
                    jianchaMaterialEntity1.setTitle("标题1");
                    jianchaMaterialEntity1.setDescript("A");
                    JianchaMaterialEntity jianchaMaterialEntity2 = new JianchaMaterialEntity();
                    jianchaMaterialEntity2.setTitle("标题2");
                    jianchaMaterialEntity2.setDescript("B");
                    mSelectBaseData.add(jianchaMaterialEntity1);
                    mSelectBaseData.add(jianchaMaterialEntity2);
                    break;
                case 2:
                    JianchaMaterialEntity jianchaMaterialEntity3 = new JianchaMaterialEntity();
                    jianchaMaterialEntity3.setTitle("标题3");
                    jianchaMaterialEntity3.setDescript("E");
                    JianchaMaterialEntity jianchaMaterialEntity4 = new JianchaMaterialEntity();
                    jianchaMaterialEntity4.setTitle("标题4");
                    jianchaMaterialEntity4.setDescript("F");
                    mSelectBaseData.add(jianchaMaterialEntity3);
                    mSelectBaseData.add(jianchaMaterialEntity4);
                    break;
                case 3:
                    JianchaMaterialEntity jianchaMaterialEntity5 = new JianchaMaterialEntity();
                    jianchaMaterialEntity5.setTitle("标题5");
                    jianchaMaterialEntity5.setDescript("G");
                    JianchaMaterialEntity jianchaMaterialEntity6 = new JianchaMaterialEntity();
                    jianchaMaterialEntity6.setTitle("标题6");
                    jianchaMaterialEntity6.setDescript("H");
                    mSelectBaseData.add(jianchaMaterialEntity5);
                    mSelectBaseData.add(jianchaMaterialEntity6);
                    break;
                case 4:
                    JianchaMaterialEntity jianchaMaterialEntity7 = new JianchaMaterialEntity();
                    jianchaMaterialEntity7.setTitle("标题75");
                    jianchaMaterialEntity7.setDescript("J");
                    JianchaMaterialEntity jianchaMaterialEntity8 = new JianchaMaterialEntity();
                    jianchaMaterialEntity8.setTitle("标题8");
                    jianchaMaterialEntity8.setDescript("K");
                    mSelectBaseData.add(jianchaMaterialEntity7);
                    mSelectBaseData.add(jianchaMaterialEntity8);
                    break;
                case 5:
                    JianchaMaterialEntity jianchaMaterialEntity9 = new JianchaMaterialEntity();
                    jianchaMaterialEntity9.setTitle("标题9");
                    jianchaMaterialEntity9.setDescript("c");
                    JianchaMaterialEntity jianchaMaterialEntity10 = new JianchaMaterialEntity();
                    jianchaMaterialEntity10.setTitle("标题10");
                    jianchaMaterialEntity10.setDescript("D");
                    mSelectBaseData.add(jianchaMaterialEntity9);
                    mSelectBaseData.add(jianchaMaterialEntity10);
                    break;
                case 6:
                    JianchaMaterialEntity jianchaMaterialEntity11 = new JianchaMaterialEntity();
                    jianchaMaterialEntity11.setTitle("标题11");
                    jianchaMaterialEntity11.setDescript("b");
                    JianchaMaterialEntity jianchaMaterialEntity12 = new JianchaMaterialEntity();
                    jianchaMaterialEntity12.setTitle("标题12");
                    jianchaMaterialEntity12.setDescript("z");
                    mSelectBaseData.add(jianchaMaterialEntity11);
                    mSelectBaseData.add(jianchaMaterialEntity12);
                    break;
                case 7:
                    JianchaMaterialEntity jianchaMaterialEntity13 = new JianchaMaterialEntity();
                    jianchaMaterialEntity13.setTitle("标题13");
                    jianchaMaterialEntity13.setDescript("a");
                    JianchaMaterialEntity jianchaMaterialEntity14 = new JianchaMaterialEntity();
                    jianchaMaterialEntity14.setTitle("标题14");
                    jianchaMaterialEntity14.setDescript("q");
                    mSelectBaseData.add(jianchaMaterialEntity13);
                    mSelectBaseData.add(jianchaMaterialEntity14);
                    break;
                case 8:
                    JianchaMaterialEntity jianchaMaterialEntity15 = new JianchaMaterialEntity();
                    jianchaMaterialEntity15.setTitle("标题15");
                    jianchaMaterialEntity15.setDescript("w");
                    JianchaMaterialEntity jianchaMaterialEntity16 = new JianchaMaterialEntity();
                    jianchaMaterialEntity16.setTitle("标题16");
                    jianchaMaterialEntity16.setDescript("e");
                    mSelectBaseData.add(jianchaMaterialEntity15);
                    mSelectBaseData.add(jianchaMaterialEntity16);
                    break;
                case 9:
                    JianchaMaterialEntity jianchaMaterialEntity17 = new JianchaMaterialEntity();
                    jianchaMaterialEntity17.setTitle("标题17");
                    jianchaMaterialEntity17.setDescript("r");
                    JianchaMaterialEntity jianchaMaterialEntity18 = new JianchaMaterialEntity();
                    jianchaMaterialEntity18.setTitle("标题18");
                    jianchaMaterialEntity18.setDescript("t");
                    mSelectBaseData.add(jianchaMaterialEntity17);
                    mSelectBaseData.add(jianchaMaterialEntity18);
                    break;
                case 10:
                    JianchaMaterialEntity jianchaMaterialEntity19 = new JianchaMaterialEntity();
                    jianchaMaterialEntity19.setTitle("标题19");
                    jianchaMaterialEntity19.setDescript("y");
                    JianchaMaterialEntity jianchaMaterialEntity20 = new JianchaMaterialEntity();
                    jianchaMaterialEntity20.setTitle("标题20");
                    jianchaMaterialEntity20.setDescript("u");
                    mSelectBaseData.add(jianchaMaterialEntity19);
                    mSelectBaseData.add(jianchaMaterialEntity20);
                    break;
                case 11:
                    JianchaMaterialEntity jianchaMaterialEntity21 = new JianchaMaterialEntity();
                    jianchaMaterialEntity21.setTitle("标题21");
                    jianchaMaterialEntity21.setDescript("i");
                    JianchaMaterialEntity jianchaMaterialEntity22 = new JianchaMaterialEntity();
                    jianchaMaterialEntity22.setTitle("标题22");
                    jianchaMaterialEntity22.setDescript("o");
                    mSelectBaseData.add(jianchaMaterialEntity21);
                    mSelectBaseData.add(jianchaMaterialEntity22);
                    break;
                case 12:
                    JianchaMaterialEntity jianchaMaterialEntity23 = new JianchaMaterialEntity();
                    jianchaMaterialEntity23.setTitle("标题23");
                    jianchaMaterialEntity23.setDescript("p");
                    JianchaMaterialEntity jianchaMaterialEntity24 = new JianchaMaterialEntity();
                    jianchaMaterialEntity24.setTitle("标题24");
                    jianchaMaterialEntity24.setDescript("l");
                    mSelectBaseData.add(jianchaMaterialEntity23);
                    mSelectBaseData.add(jianchaMaterialEntity24);
                    break;
                case 13:
                    JianchaMaterialEntity jianchaMaterialEntity25 = new JianchaMaterialEntity();
                    jianchaMaterialEntity25.setTitle("标题25");
                    jianchaMaterialEntity25.setDescript("d");
                    JianchaMaterialEntity jianchaMaterialEntity26 = new JianchaMaterialEntity();
                    jianchaMaterialEntity26.setTitle("标题26");
                    jianchaMaterialEntity26.setDescript("z");
                    mSelectBaseData.add(jianchaMaterialEntity25);
                    mSelectBaseData.add(jianchaMaterialEntity26);
                    break;
                default:
                    break;
            }
        }
        //去除集合重复元素
        Set<JianchaMaterialEntity> set = new LinkedHashSet<>();
        set.addAll(mSelectBaseData);
        mSelectBaseData.clear();
        mSelectBaseData.addAll(set);
    }

    /**
     * 现场管理部分 获取填充对话框的数据 ，根据点击的Item获取数据来源
     */
    public void getDialogSiteData(){
        for(int i = 0; i < mPostionSiteList.size(); i++){
            int itemId = mPostionSiteList.get(i);
            switch (itemId){
                case 1:
                    JianchaMaterialEntity jianchaMaterialEntity27 = new JianchaMaterialEntity();
                    jianchaMaterialEntity27.setTitle("标题27");
                    jianchaMaterialEntity27.setDescript("1");
                    JianchaMaterialEntity jianchaMaterialEntity28 = new JianchaMaterialEntity();
                    jianchaMaterialEntity28.setTitle("标题28");
                    jianchaMaterialEntity28.setDescript("2");
                    mSelectSiteData.add(jianchaMaterialEntity27);
                    mSelectSiteData.add(jianchaMaterialEntity28);
                    break;
                case 2:
                    JianchaMaterialEntity jianchaMaterialEntity29 = new JianchaMaterialEntity();
                    jianchaMaterialEntity29.setTitle("标题29");
                    jianchaMaterialEntity29.setDescript("3");
                    JianchaMaterialEntity jianchaMaterialEntity30 = new JianchaMaterialEntity();
                    jianchaMaterialEntity30.setTitle("标题30");
                    jianchaMaterialEntity30.setDescript("4");
                    mSelectSiteData.add(jianchaMaterialEntity29);
                    mSelectSiteData.add(jianchaMaterialEntity30);
                    break;
                case 3:
                    JianchaMaterialEntity jianchaMaterialEntity31 = new JianchaMaterialEntity();
                    jianchaMaterialEntity31.setTitle("标题31");
                    jianchaMaterialEntity31.setDescript("5");
                    JianchaMaterialEntity jianchaMaterialEntity32 = new JianchaMaterialEntity();
                    jianchaMaterialEntity32.setTitle("标题32");
                    jianchaMaterialEntity32.setDescript("6");
                    mSelectSiteData.add(jianchaMaterialEntity31);
                    mSelectSiteData.add(jianchaMaterialEntity32);
                    break;
                case 4:
                    JianchaMaterialEntity jianchaMaterialEntity33 = new JianchaMaterialEntity();
                    jianchaMaterialEntity33.setTitle("标题33");
                    jianchaMaterialEntity33.setDescript("7");
                    JianchaMaterialEntity jianchaMaterialEntity34 = new JianchaMaterialEntity();
                    jianchaMaterialEntity34.setTitle("标题34");
                    jianchaMaterialEntity34.setDescript("8");
                    mSelectSiteData.add(jianchaMaterialEntity33);
                    mSelectSiteData.add(jianchaMaterialEntity34);
                    break;
                case 5:
                    JianchaMaterialEntity jianchaMaterialEntity35 = new JianchaMaterialEntity();
                    jianchaMaterialEntity35.setTitle("标题35");
                    jianchaMaterialEntity35.setDescript("9");
                    JianchaMaterialEntity jianchaMaterialEntity36 = new JianchaMaterialEntity();
                    jianchaMaterialEntity36.setTitle("标题36");
                    jianchaMaterialEntity36.setDescript("10");
                    mSelectSiteData.add(jianchaMaterialEntity35);
                    mSelectSiteData.add(jianchaMaterialEntity36);
                    break;
                case 6:
                    JianchaMaterialEntity jianchaMaterialEntity37 = new JianchaMaterialEntity();
                    jianchaMaterialEntity37.setTitle("标题37");
                    jianchaMaterialEntity37.setDescript("11");
                    JianchaMaterialEntity jianchaMaterialEntity38 = new JianchaMaterialEntity();
                    jianchaMaterialEntity38.setTitle("标题38");
                    jianchaMaterialEntity38.setDescript("12");
                    mSelectSiteData.add(jianchaMaterialEntity37);
                    mSelectSiteData.add(jianchaMaterialEntity38);
                    break;
                case 7:
                    JianchaMaterialEntity jianchaMaterialEntity39 = new JianchaMaterialEntity();
                    jianchaMaterialEntity39.setTitle("标题39");
                    jianchaMaterialEntity39.setDescript("13");
                    JianchaMaterialEntity jianchaMaterialEntity40 = new JianchaMaterialEntity();
                    jianchaMaterialEntity40.setTitle("标题40");
                    jianchaMaterialEntity40.setDescript("14");
                    mSelectSiteData.add(jianchaMaterialEntity39);
                    mSelectSiteData.add(jianchaMaterialEntity40);
                    break;
                case 8:
                    JianchaMaterialEntity jianchaMaterialEntity41 = new JianchaMaterialEntity();
                    jianchaMaterialEntity41.setTitle("标题41");
                    jianchaMaterialEntity41.setDescript("15");
                    JianchaMaterialEntity jianchaMaterialEntity42 = new JianchaMaterialEntity();
                    jianchaMaterialEntity42.setTitle("标题42");
                    jianchaMaterialEntity42.setDescript("16");
                    mSelectSiteData.add(jianchaMaterialEntity41);
                    mSelectSiteData.add(jianchaMaterialEntity42);
                    break;
                case 9:
                    JianchaMaterialEntity jianchaMaterialEntity43 = new JianchaMaterialEntity();
                    jianchaMaterialEntity43.setTitle("标题43");
                    jianchaMaterialEntity43.setDescript("17");
                    JianchaMaterialEntity jianchaMaterialEntity44 = new JianchaMaterialEntity();
                    jianchaMaterialEntity44.setTitle("标题44");
                    jianchaMaterialEntity44.setDescript("18");
                    mSelectSiteData.add(jianchaMaterialEntity43);
                    mSelectSiteData.add(jianchaMaterialEntity44);
                    break;
                case 10:
                    JianchaMaterialEntity jianchaMaterialEntity45 = new JianchaMaterialEntity();
                    jianchaMaterialEntity45.setTitle("标题45");
                    jianchaMaterialEntity45.setDescript("19");
                    JianchaMaterialEntity jianchaMaterialEntity46 = new JianchaMaterialEntity();
                    jianchaMaterialEntity46.setTitle("标题46");
                    jianchaMaterialEntity46.setDescript("20");
                    mSelectSiteData.add(jianchaMaterialEntity45);
                    mSelectSiteData.add(jianchaMaterialEntity46);
                    break;
                case 11:
                    JianchaMaterialEntity jianchaMaterialEntity47 = new JianchaMaterialEntity();
                    jianchaMaterialEntity47.setTitle("标题47");
                    jianchaMaterialEntity47.setDescript("21");
                    JianchaMaterialEntity jianchaMaterialEntity48 = new JianchaMaterialEntity();
                    jianchaMaterialEntity48.setTitle("标题48");
                    jianchaMaterialEntity48.setDescript("22");
                    mSelectSiteData.add(jianchaMaterialEntity47);
                    mSelectSiteData.add(jianchaMaterialEntity48);
                    break;
                case 12:
                    JianchaMaterialEntity jianchaMaterialEntity49 = new JianchaMaterialEntity();
                    jianchaMaterialEntity49.setTitle("标题49");
                    jianchaMaterialEntity49.setDescript("23");
                    JianchaMaterialEntity jianchaMaterialEntity50 = new JianchaMaterialEntity();
                    jianchaMaterialEntity50.setTitle("标题50");
                    jianchaMaterialEntity50.setDescript("24");
                    mSelectSiteData.add(jianchaMaterialEntity49);
                    mSelectSiteData.add(jianchaMaterialEntity50);
                    break;
                default:
                    break;
            }
        }
        //去除集合重复元素
        Set<JianchaMaterialEntity> set = new LinkedHashSet<>();
        set.addAll(mSelectSiteData);
        mSelectSiteData.clear();
        mSelectSiteData.addAll(set);
    }

    private void getListData(){
        mBaseList = new ArrayList<String>();
        mSiteList = new ArrayList<String>();
        mBaseList.add("全选/全不选");
        mBaseList.add("资质证照");
        mBaseList.add("安全生产管理机构及人员");
        mBaseList.add("安全生产责任制");
        mBaseList.add("安全生产规章制度");
        mBaseList.add("安全生产教育培训");
        mBaseList.add("安全生产管理基础档案");
        mBaseList.add("应急救援");
        mBaseList.add("职业卫生");
        mBaseList.add("安全操作规程");
        mBaseList.add("安全生产投入");
        mBaseList.add("特种设备基础管理");
        mBaseList.add("相关方基础管理");
        mBaseList.add("其他基础管理");
        mSiteList.add("全选/全不选");
        mSiteList.add("生产设备设施");
        mSiteList.add("特种设备现场管理");
        mSiteList.add("消防与应急安全");
        mSiteList.add("用电安全");
        mSiteList.add("职业卫生现场安全");
        mSiteList.add("危险化学品");
        mSiteList.add("场所环境");
        mSiteList.add("从业人员操作行为");
        mSiteList.add("有限空间现场管理");
        mSiteList.add("辅助动力系统");
        mSiteList.add("相关方现场管理");
        mSiteList.add("其他现场管理");
    }

    @OnClick({R.id.tv_check_table,R.id.tv_personal,R.id.image_base_up,R.id.image_site_up,R.id.image_descript_up,R.id.tv_descript_save,R.id.tv_descript_save_into})
    private void viewOnClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.tv_check_table:  //制作检查表
                showSingleChoiceButton();
                break;
            case R.id.tv_personal:  //选择检查人员
                showPersonalListDialog();
                break;
            case R.id.image_base_up:  //基础管理部分
                if(isBaseOpen){
                    image_base_up.setImageResource(R.drawable.icon_down);
                    base_gridview.setVisibility(View.VISIBLE);
                    isBaseOpen = false;
                }else{
                    image_base_up.setImageResource(R.drawable.icon_up);
                    base_gridview.setVisibility(View.GONE);
                    isBaseOpen = true;
                }
                break;
            case R.id.image_site_up:  //现场管理部分
                if(isSiteOpen){
                    image_site_up.setImageResource(R.drawable.icon_down);
                    site_gridview.setVisibility(View.VISIBLE);
                    isSiteOpen = false;
                }else{
                    image_site_up.setImageResource(R.drawable.icon_up);
                    site_gridview.setVisibility(View.GONE);
                    isSiteOpen = true;
                }
                break;
            case R.id.image_descript_up: //被选中的材料文书展示部分
                if(isDescriptOpen){
                    image_descript_up.setImageResource(R.drawable.icon_down);
                    selected_descript_listview.setVisibility(View.VISIBLE);
                    isDescriptOpen = false;
                }else{
                    image_descript_up.setImageResource(R.drawable.icon_up);
                    selected_descript_listview.setVisibility(View.GONE);
                    isDescriptOpen = true;
                }
                break;
            case R.id.tv_descript_save:  //保存
                if(tv_personal.getText().toString().equals("")
                        ||tv_personal.getText().toString()==null
                        ||tv_personal.getText().toString().equals("null")){
                    Toast.makeText(this,"计划检查人员不能为空",Toast.LENGTH_SHORT).show();
                    return;
                }
                //进行保存操作
                break;
            case R.id.tv_descript_save_into:  //保存并进入检查登记
                if(tv_personal.getText().toString().equals("")
                        ||tv_personal.getText().toString()==null
                        ||tv_personal.getText().toString().equals("null")){
                    Toast.makeText(this,"计划检查人员不能为空",Toast.LENGTH_SHORT).show();
                    return;
                }
                //进行保存进入下一步的操作
                Intent mIntent = new Intent();
                mIntent.setClass(JianchaTaskActivity.this,WoyaoJianchaActivity.class);
                JianchaPersonalEntity jianchaPersonalEntity = mSelectedPersonalData.get(0);
                //传递对象
                mIntent.putExtra("dataBean",dataBean);      //传递公司对象
                mIntent.putExtra("checkpeople",jianchaPersonalEntity);  //被选中的检查人员
                mIntent.putExtra("mSelectDescriptData",(Serializable) mSelectDescriptData);
                mIntent.putExtra("checktime",et_check_time.getText().toString());  //检查时间
                startActivity(mIntent);
                break;

        }
    }

    /**
     * 展示单选对话框
     */
    public void showSingleChoiceButton() {
        if(mSelectBaseData.size()==0){
            Toast.makeText(this,"必须选择基础管理部分，否则不能制作检查表",Toast.LENGTH_SHORT).show();
            return;
        }
        //去除集合重复元素
        mSelectTotalData.addAll(mSelectBaseData);
        mSelectTotalData.addAll(mSelectSiteData);
        Set<JianchaMaterialEntity> set = new LinkedHashSet<>();
        set.addAll(mSelectTotalData);
        mSelectTotalData.clear();
        mSelectTotalData.addAll(set);

        mSelectDescriptData = new ArrayList<>();

        ListView listView= new ListView(this);
        //构造listview对象。
        dialogItemsListAdapter = new DialogItemsListAdapter(
                this, mSelectTotalData);
        listView.setAdapter(dialogItemsListAdapter);
        final AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        descript_layout.setVisibility(View.VISIBLE);
                        descriptListAdapter = new DescriptListAdapter(JianchaTaskActivity.this,mSelectDescriptData);
                        selected_descript_listview.setAdapter(descriptListAdapter);
                        descriptListAdapter.notifyDataSetChanged();
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mSelectDescriptData.clear();
                    }
                }).create();
        alertDialog.setCanceledOnTouchOutside(false);//使除了dialog以外的地方不能被点击
        alertDialog.setView(listView);
        alertDialog.show();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DialogItemsListAdapter.HodlerView holder = (DialogItemsListAdapter.HodlerView) view.getTag();
                holder.checkBox.toggle();
                dialogItemsListAdapter.getIsSelected().put(position, holder.checkBox.isChecked());
                //拿到被选中的对象
                mSelectDescriptData.add((JianchaMaterialEntity) dialogItemsListAdapter.getItem(position));
            }
        });
    }

    /**
     * 展示人员列表的对话框
     * http://blog.csdn.net/u013064109/article/details/51990526
     */
    private void showPersonalListDialog(){
        View customView = View.inflate(this,R.layout.layout_list_dialog,null);
        xCDropDownListView = (XCDropDownListView) customView.findViewById(R.id.xCDropDownListView);
        personal_listview = (ListView) customView.findViewById(R.id.personal_listview);
        xCDropDownListView.setItemsData(jianchaTeamEntityList);

        //传递空字符串代表查询全部执法人员
        httpRequestJianchaPersonalInfo("");

        personal_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //使用checkbox实现单选功能
                for (int i = 0; i < mJianchaPersonalEntityList.size(); i++) {
                    jianchaPersonalListAdapter.getIsSelected().put(i, false);
                    jianchaPersonalListAdapter.notifyDataSetChanged();
                }
                jianchaPersonalListAdapter.getIsSelected().put(position,true);
                mSelectedPersonalData.add((JianchaPersonalEntity) jianchaPersonalListAdapter.getItem(position));
                jianchaPersonalListAdapter.notifyDataSetChanged();
            }
        });
        CustomDialog.Builder dialog=new CustomDialog.Builder(this);
        dialog.setTitle("选择检查人员")
                .setContentView(customView)//设置自定义customView
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        for(JianchaPersonalEntity jianchaPersonalEntity : mSelectedPersonalData){
                            tv_personal.setText(jianchaPersonalEntity.getName());

                        }
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        }).create().show();

        xCDropDownListView.setOnItemClickXCDropDownListViewListener(new XCDropDownListView.XCDropDownListViewListener() {
            @Override
            public void getItemData(JianchaTeamEntity jianchaTeamEntity) {
                if(mJianchaPersonalEntityList!=null){
                    mJianchaPersonalEntityList.clear();
                }
                httpRequestJianchaPersonalInfo(jianchaTeamEntity.getId());
            }
        });
    }

    /**
     * 请求执法队伍列表 GET
     */
    private void httpRequestJianchaTeamList(){
        jianchaTeamEntityList = new ArrayList<>();
        OkHttpClient okHttpClient = new OkHttpClient();
        Request.Builder builder = new Request.Builder();
        String request_url = "http://172.16.0.81:8080/tongji/firmdate/querytroop";
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
                    Type type = new TypeToken<ArrayList<JianchaTeamEntity>>() {}.getType();
                    jianchaTeamEntityList = gson.fromJson(result_str,type);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 根据执法队伍Id请求相应队伍的执法人员列表  GET
     */
    private void httpRequestJianchaPersonalInfo(String teamId){
        OkHttpClient okHttpClient = new OkHttpClient();
        Request.Builder builder = new Request.Builder();
        String request_url = "http://172.16.0.81:8080/tongji/firmdate/querytroopemp?id="+teamId;
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
                    Type type = new TypeToken<ArrayList<JianchaPersonalEntity>>() {}.getType();
                    mJianchaPersonalEntityList = gson.fromJson(result_str,type);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            jianchaPersonalListAdapter = new JianchaPersonalListAdapter(JianchaTaskActivity.this,mJianchaPersonalEntityList);
                            personal_listview.setAdapter(jianchaPersonalListAdapter);
                            jianchaPersonalListAdapter.notifyDataSetChanged();
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

}
