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
import com.henghao.hhworkpresent.utils.SqliteDBUtils;
import com.henghao.hhworkpresent.views.CustomDialog;
import com.henghao.hhworkpresent.views.DatabaseHelper;
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

    @ViewInject(R.id.tv_login_check_person)
    private TextView tv_login_check_person;

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

        SqliteDBUtils sqliteDBUtils = new SqliteDBUtils(this);
        tv_login_check_person.setText(sqliteDBUtils.getLoginFirstName()+ sqliteDBUtils.getLoginGiveName());

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
                    //资质证照
                    JianchaMaterialEntity jianchaMaterialEntity1 = new JianchaMaterialEntity();
                    jianchaMaterialEntity1.setTitle("安全生产相关资质");
                    jianchaMaterialEntity1.setDescript("是否有安全生产相关资质证照，是否超期。如果不需取得相关资质，请注明。");
                    JianchaMaterialEntity jianchaMaterialEntity2 = new JianchaMaterialEntity();
                    jianchaMaterialEntity2.setTitle("营业执照");
                    jianchaMaterialEntity2.setDescript("申请人凭批准书向工商行政管理部门办理登记注册手续。");
                    mSelectBaseData.add(jianchaMaterialEntity1);
                    mSelectBaseData.add(jianchaMaterialEntity2);
                    break;
                case 2:
                    //2. 安全生产管理结构及人员
                    JianchaMaterialEntity jianchaMaterialEntity3 = new JianchaMaterialEntity();
                    jianchaMaterialEntity3.setTitle("安全管理机构或人员");
                    jianchaMaterialEntity3.setDescript("“矿山、金属冶金、建筑施工、道路运输单位和危险物品的生产、经营、储存单位，应当设置安全生产管理机构或者配置专职安全生产管理人员。" +
                            "规定以外的其他生产经营单位，从业人员超过一百人的，应当设置安全生产管理机构或者配备专职安全生产管理人员；从业人员在一百人一下的，应当配备专职或者兼职的安全生产管理人员。”");
                    mSelectBaseData.add(jianchaMaterialEntity3);
                    break;
                case 3:
                    //3. 安全生产责任制
                    JianchaMaterialEntity jianchaMaterialEntity4 = new JianchaMaterialEntity();
                    jianchaMaterialEntity4.setTitle("单位主要负责人");
                    jianchaMaterialEntity4.setDescript("（一）建立、健全本单位安全生产责任制；（二）组织制定本单位安全生产规章制度和操作规程；" +
                            "（三）保证本单位安全生产投入的有效实施；（四）督促、检查本单位的安全生产工作，及时消除安全生产事故隐患；" +
                            "（五）组织制定并实施本单位的生产安全事故应急救援预案；（六）及时、如实报告安全生产事故。");
                    mSelectBaseData.add(jianchaMaterialEntity4);
                    JianchaMaterialEntity jianchaMaterialEntity5 = new JianchaMaterialEntity();
                    jianchaMaterialEntity5.setTitle("各部门、各岗位职责");
                    jianchaMaterialEntity5.setDescript("生产经营单位的安全生产责任制应当明确各岗位的责任人员、责任内容和考核要求，形成包括全体人员和全部生产经营活动的责任体系。");
                    mSelectBaseData.add(jianchaMaterialEntity5);
                    break;
                case 4:
                    //4. 安全生产规章制度
                    JianchaMaterialEntity jianchaMaterialEntity6 = new JianchaMaterialEntity();
                    jianchaMaterialEntity6.setTitle("其他按法律、法规规章要求制定的制度");
                    jianchaMaterialEntity6.setDescript("按要求建立的制度，要合法、合规、真实、有效。");
                    mSelectBaseData.add(jianchaMaterialEntity6);
                    JianchaMaterialEntity jianchaMaterialEntity7 = new JianchaMaterialEntity();
                    jianchaMaterialEntity7.setTitle("各岗位安全操作规程");
                    jianchaMaterialEntity7.setDescript("生产经营单位的主要负责人组织制定本单位安全生产规章制度和操作规程。");
                    mSelectBaseData.add(jianchaMaterialEntity7);
                    JianchaMaterialEntity jianchaMaterialEntity8 = new JianchaMaterialEntity();
                    jianchaMaterialEntity8.setTitle("安全生产教育培训制度");
                    jianchaMaterialEntity8.setDescript("主要内容包括：（一）安全生产教育培训的目的；（二）负责安全生产培训的责任部门和责任人员；（三）培训的周期和时间安排；（四）参加安全生产教育和培训的人员；（五）教育和培训的主要内容；" +
                            "（六）教育和培训的形式（自行培训或委托专业机构培训） （七）教育和培训工作的要求。");
                    mSelectBaseData.add(jianchaMaterialEntity8);
                    JianchaMaterialEntity jianchaMaterialEntity9 = new JianchaMaterialEntity();
                    jianchaMaterialEntity9.setTitle("生产安全事故报告和处理制度");
                    jianchaMaterialEntity9.setDescript("主要内容包括：（一）安全生产事故报告和处理制度制定依据；（二）安全生产事故的概念；（三）安全生产事故的分类；（四）安全生产事故报告程序；（五）安全生产事故现场保护的要求；（六）安全生产事故的调查处理；（七）安全生产事故资料的归档要求；（八）对安全生产事故进行经验教训总结。");
                    mSelectBaseData.add(jianchaMaterialEntity9);
                    break;
                case 5:
                    //5. 安全生产教育培训
                    JianchaMaterialEntity jianchaMaterialEntity10 = new JianchaMaterialEntity();
                    jianchaMaterialEntity10.setTitle("从业人员教育培训");
                    jianchaMaterialEntity10.setDescript("企业对新职工（包括临时、合同工）和实习培训人员必须进行三级安全教育。2、经“三级教育”考试合格的工人才允许上岗见习，并明确所跟师傅。学徒期满考试合格后，有车间签写安全作业证，送安全技术部门备案、认可盖章。取得安全作业证的工人，才具备独立上岗操作资格。3、三级安全教育必须保证教育时间：一级（厂级）安全教育的教育时间不应小于48小时；二级（车间级）安全教育不应小于36小时，班组安全教育不应少于24小时。4、企业必须对各级干部（包括公司、厂、车间、班组）每年进行一次或多次安全培训，累计时间不得少于24小时。主要学习安全生产的方针、政策，以及安全管理、安全技术知识等内容。");
                    mSelectBaseData.add(jianchaMaterialEntity10);
                    JianchaMaterialEntity jianchaMaterialEntity11 = new JianchaMaterialEntity();
                    jianchaMaterialEntity11.setTitle("其他教育培训情况");
                    jianchaMaterialEntity11.setDescript("是否按照要求完成教育培训");
                    mSelectBaseData.add(jianchaMaterialEntity11);
                    break;
                case 6:
                    //6. 安全生产管理基础档案
                    JianchaMaterialEntity jianchaMaterialEntity12 = new JianchaMaterialEntity();
                    jianchaMaterialEntity12.setTitle("与承租单位、承包单位签订安全生产管理协议");
                    jianchaMaterialEntity12.setDescript("生产经营单位将生产经营项目、场所、设备发包或者出租的，应当与承包单位、租赁单位签订专门的安全生产管理协议，或者在承包、租赁合同中约定各自的安全生产管理职责。");
                    mSelectBaseData.add(jianchaMaterialEntity12);
                    JianchaMaterialEntity jianchaMaterialEntity13 = new JianchaMaterialEntity();
                    jianchaMaterialEntity13.setTitle("事故管理记录档案");
                    jianchaMaterialEntity13.setDescript("生产经营单位应当保护事故现场；需要移动现场物品时，应当做出标记和书面记录，妥善保管有关证物。");
                    mSelectBaseData.add(jianchaMaterialEntity13);
                    JianchaMaterialEntity jianchaMaterialEntity14 = new JianchaMaterialEntity();
                    jianchaMaterialEntity14.setTitle("其他安全生产管理档案");
                    jianchaMaterialEntity14.setDescript("其他安全生产管理档案");
                    mSelectBaseData.add(jianchaMaterialEntity14);
                    JianchaMaterialEntity jianchaMaterialEntity15 = new JianchaMaterialEntity();
                    jianchaMaterialEntity15.setTitle("安全检查及事故隐患排查记录");
                    jianchaMaterialEntity15.setDescript("应当对安全生产状况进行经常性检查。检查情况应当记录在案，并按照规定的期限保存。对自查出的隐患要应当采取相应的安全防范措施，及时上报，并按要求进行整改。对发现的重大隐患要及时上报有关安全生产监管部门。");
                    mSelectBaseData.add(jianchaMaterialEntity15);
                    JianchaMaterialEntity jianchaMaterialEntity16 = new JianchaMaterialEntity();
                    jianchaMaterialEntity16.setTitle("工商社会保险、安责险缴费记录");
                    jianchaMaterialEntity16.setDescript("生产经营单位必须依法参加工伤社会保险，为从业人员缴纳保险费，并按照先关规定投保安全生产责任保险。");
                    mSelectBaseData.add(jianchaMaterialEntity16);
                    break;
                case 7:
                    //7. 应急救援
                    JianchaMaterialEntity jianchaMaterialEntity17 = new JianchaMaterialEntity();
                    jianchaMaterialEntity17.setTitle("应急演练记录");
                    jianchaMaterialEntity17.setDescript("“1、应急救援预案应当每年演练2次以上，并有演练记录。2、应急预案演练结束后，应急预案演练组织单位应当对应急预案演练效果进行评估报告，分析存在的问题，并对应急预案提出修订意见。”");
                    mSelectBaseData.add(jianchaMaterialEntity17);
                    JianchaMaterialEntity jianchaMaterialEntity18 = new JianchaMaterialEntity();
                    jianchaMaterialEntity18.setTitle("应急装备物资");
                    jianchaMaterialEntity18.setDescript("生产经营单位应当按照应急预案的要求配备相应的应急物资及装备，建立使用善状况档案，定期检测和维护，使其处于良好的状态。");
                    mSelectBaseData.add(jianchaMaterialEntity18);
                    JianchaMaterialEntity jianchaMaterialEntity19 = new JianchaMaterialEntity();
                    jianchaMaterialEntity19.setTitle("应急队伍");
                    jianchaMaterialEntity19.setDescript("企业应当建立与本单位安全生产特点相适应的专兼职应急救援队伍，或指定专兼职应急救援人员，并组织训练；无需建立应急救援队伍的，可与附近具备专业资质的应急救援队伍签订服务协议。");
                    mSelectBaseData.add(jianchaMaterialEntity19);
                    JianchaMaterialEntity jianchaMaterialEntity20 = new JianchaMaterialEntity();
                    jianchaMaterialEntity20.setTitle("应急预案");
                    jianchaMaterialEntity20.setDescript("“生产经营单位应当根据有关法律、法规和《生产经营单位安全生产事故应急预案编制导则》（AQ/T9002-2006），结合本单位和可能发生的事故特点，制定相应的应急预案。生产经营单位的应急预案按照针对情况的不同，分为综合应急预案、专项应急预案和现场处置方案。”");
                    mSelectBaseData.add(jianchaMaterialEntity20);
                    JianchaMaterialEntity jianchaMaterialEntity21 = new JianchaMaterialEntity();
                    jianchaMaterialEntity21.setTitle("预案备案");
                    jianchaMaterialEntity21.setDescript("企业应急预案应根据有关规定报当地主管部门备案，与当地政府应急预案保持衔接，通报有关应急作协单位，并定期进行演练。");
                    mSelectBaseData.add(jianchaMaterialEntity21);
                    JianchaMaterialEntity jianchaMaterialEntity22 = new JianchaMaterialEntity();
                    jianchaMaterialEntity22.setTitle("预案评审");
                    jianchaMaterialEntity22.setDescript("应急预案应当定期评审，并根据评审结果或实际情况的变化进行修订和完善，至少没三年修订一次，预案修订情况应有记录并归档。");
                    mSelectBaseData.add(jianchaMaterialEntity22);
                    break;
                case 8:
                    //8. 职业卫生
                    JianchaMaterialEntity jianchaMaterialEntity23 = new JianchaMaterialEntity();
                    jianchaMaterialEntity23.setTitle("其他职业卫生基础资料");
                    jianchaMaterialEntity23.setDescript("其他职业卫生基础资料");
                    mSelectBaseData.add(jianchaMaterialEntity23);
                    JianchaMaterialEntity jianchaMaterialEntity24 = new JianchaMaterialEntity();
                    jianchaMaterialEntity24.setTitle("职业卫生应急救援预案");
                    jianchaMaterialEntity24.setDescript("用人单位应建立健全全职业病危害事故应急救援预案");
                    mSelectBaseData.add(jianchaMaterialEntity24);
                    JianchaMaterialEntity jianchaMaterialEntity25 = new JianchaMaterialEntity();
                    jianchaMaterialEntity25.setTitle("职业卫生教育培训");
                    jianchaMaterialEntity25.setDescript("应设置或者指定职业卫生管理机构或者组织，配备专职或者兼职的职业卫生专业人员，负责本单位的职业病防治工作");
                    mSelectBaseData.add(jianchaMaterialEntity25);
                    JianchaMaterialEntity jianchaMaterialEntity26 = new JianchaMaterialEntity();
                    jianchaMaterialEntity26.setTitle("职业卫生结构及人员");
                    jianchaMaterialEntity26.setDescript("应当对劳动者进行上岗前的职业卫生培训和在岗期间的定期职业卫生培训，普及职业卫生知识，督促劳动者遵守职业病防治法律、法规、规章和操作规程，知道劳动者正确使用职业病防护设备和个人使用的职业病防护用品。");
                    mSelectBaseData.add(jianchaMaterialEntity26);
                    break;
                case 9:
                    //9. 安全操作规程
                    break;
                case 10:
                    //10. 安全生产投入
                    break;
                case 11:
                    //11. 特种设备基础管理
                    break;
                case 12:
                    //12. 相关方基础管理
                    break;
                case 13:
                    //13. 其他基础管理
                    JianchaMaterialEntity jianchaMaterialEntity27 = new JianchaMaterialEntity();
                    jianchaMaterialEntity27.setTitle("其他基础资料");
                    jianchaMaterialEntity27.setDescript("其他基础资料");
                    mSelectBaseData.add(jianchaMaterialEntity27);
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
                    //1.生产设备设施
                    break;
                case 2:
                    //2.特种设备现场管理
                    break;
                case 3:
                    //消防与应急安全
                    break;
                case 4:
                    //用电安全
                    break;
                case 5:
                    //职业卫生现场安全
                    JianchaMaterialEntity jianchaMaterialEntity28 = new JianchaMaterialEntity();
                    jianchaMaterialEntity28.setTitle("公告栏");
                    jianchaMaterialEntity28.setDescript("生产职业病危害的用人单位，应在醒目位置设置公告栏，公布有关职业病防治的规章制度、操作规程。");
                    mSelectSiteData.add(jianchaMaterialEntity28);
                    JianchaMaterialEntity jianchaMaterialEntity29 = new JianchaMaterialEntity();
                    jianchaMaterialEntity29.setTitle("其他职业卫生现场安全");
                    jianchaMaterialEntity29.setDescript("其他按照《安全生产法》 《中华人民共和国职业病防治法》职业卫生现场安全情况检查。");
                    mSelectSiteData.add(jianchaMaterialEntity29);
                    JianchaMaterialEntity jianchaMaterialEntity30 = new JianchaMaterialEntity();
                    jianchaMaterialEntity30.setTitle("检、维修要求");
                    jianchaMaterialEntity30.setDescript("对职业病防护设备和个人使用的职业病防护用品，用人单位应当进行经常性的维护、检修、定期检测其性能和效果，确保其处于正常状态。");
                    mSelectSiteData.add(jianchaMaterialEntity30);
                    JianchaMaterialEntity jianchaMaterialEntity31 = new JianchaMaterialEntity();
                    jianchaMaterialEntity31.setTitle("生产布局");
                    jianchaMaterialEntity31.setDescript("作业场所与生活场所分开，作业场所不得住人；有害作业与无害作业分开。");
                    mSelectSiteData.add(jianchaMaterialEntity31);
                    JianchaMaterialEntity jianchaMaterialEntity32 = new JianchaMaterialEntity();
                    jianchaMaterialEntity32.setTitle("禁止超标作业");
                    jianchaMaterialEntity32.setDescript("职业病危害因素不符合国家职业卫生标准和卫生要求，经治理仍不符合卫生要求，应停止存在职业病危害因素的作业。");
                    mSelectSiteData.add(jianchaMaterialEntity32);
                    JianchaMaterialEntity jianchaMaterialEntity33 = new JianchaMaterialEntity();
                    jianchaMaterialEntity33.setTitle("警示标识");
                    jianchaMaterialEntity33.setDescript("对生产严重职业病危害的作业岗位，应当在醒目位置，设置警示标识和中文警示说明。警示说明应当载明产生职业病危害的种类、后果、预防以及应急救治措施等内容。");
                    mSelectSiteData.add(jianchaMaterialEntity33);
                    JianchaMaterialEntity jianchaMaterialEntity34 = new JianchaMaterialEntity();
                    jianchaMaterialEntity34.setTitle("防护设施");
                    jianchaMaterialEntity34.setDescript("严禁擅自拆除、停止使用职业病防护设备");
                    mSelectSiteData.add(jianchaMaterialEntity34);
                    break;
                case 6:
                    //危险化学品
                    break;
                case 7:
                    //场所环境
                    break;
                case 8:
                    //从业人员操作行为
                    break;
                case 9:
                    //有限空间现场管理
                    break;
                case 10:
                    //辅助动力系统
                    break;
                case 11:
                    //相关方现场管理
                    break;
                case 12:
                    //其他现场管理
                    JianchaMaterialEntity jianchaMaterialEntity35 = new JianchaMaterialEntity();
                    jianchaMaterialEntity35.setTitle("作业现场安全生产情况");
                    jianchaMaterialEntity35.setDescript("按照《安全生产法》及相关法律、法规、规章、标准要求进行安全检查。");
                    mSelectSiteData.add(jianchaMaterialEntity35);
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
                finish();
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
