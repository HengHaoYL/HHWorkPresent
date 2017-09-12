package com.henghao.hhworkpresent.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.benefit.buy.library.utils.DigestUtils;
import com.henghao.hhworkpresent.ActivityFragmentSupport;
import com.henghao.hhworkpresent.R;
import com.henghao.hhworkpresent.entity.CompanyInfoEntity;
import com.henghao.hhworkpresent.entity.JianchaMaterialEntity;
import com.henghao.hhworkpresent.entity.JianchaPersonalEntity;
import com.henghao.hhworkpresent.views.CustomDialog;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.io.Serializable;
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

    @ViewInject(R.id.image_proplem_list_up)
    private ImageView image_proplem_list_up;

    @ViewInject(R.id.layout_problem_list)
    private LinearLayout layout_problem_list;

    private boolean isProblemListOpen = true;

    private CompanyInfoEntity.DataBean dataBean;
    private JianchaPersonalEntity jianchaPersonalEntity;
    private List<JianchaMaterialEntity> mJianchaMaterialEntityList;

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
    }

    @OnClick({R.id.tv_personal_login,R.id.tv_start_check,R.id.image_proplem_list_up})
    private void viewOnClick(View v) {
        switch (v.getId()) {
            case R.id.tv_personal_login:  //陪同检查人员登录
                showLoginDialog();
                break;
            case R.id.tv_start_check:  //打开标准逐项排查
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
                Intent intent = new Intent();
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
        }
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
                            //md5加密
                            if(jianchaPersonalEntity.getLoginid().equals(et_person_username.getText().toString()) &&
                                    jianchaPersonalEntity.getPassword().equals(DigestUtils.md5(et_person_password.getText().toString()))){
                                tv_check_people.setText(jianchaPersonalEntity.getName());
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

}
