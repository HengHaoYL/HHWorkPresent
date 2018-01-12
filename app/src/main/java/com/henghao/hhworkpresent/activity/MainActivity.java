package com.henghao.hhworkpresent.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.benefit.buy.library.utils.ToastUtils;
import com.benefit.buy.library.views.ToastView;
import com.henghao.hhworkpresent.ActivityFragmentSupport;
import com.henghao.hhworkpresent.FragmentSupport;
import com.henghao.hhworkpresent.R;
import com.henghao.hhworkpresent.adapter.FragmentTabAdapter;
import com.henghao.hhworkpresent.entity.HCMenuEntity;
import com.henghao.hhworkpresent.fragment.AppFragment;
import com.henghao.hhworkpresent.fragment.MsgFragment;
import com.henghao.hhworkpresent.fragment.MyFragment;
import com.henghao.hhworkpresent.fragment.WorkFragment;
import com.henghao.hhworkpresent.utils.CheckVersionTask;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

import kr.co.namee.permissiongen.PermissionFail;
import kr.co.namee.permissiongen.PermissionGen;
import kr.co.namee.permissiongen.PermissionSuccess;


/**
 *  主页
 * @author wangqingbin
 */
@SuppressLint("NewApi")
public class MainActivity extends ActivityFragmentSupport {

    @ViewInject(R.id.main_radioGroup)
    private RadioGroup radioGroup;

    public List<FragmentSupport> fragments = new ArrayList<FragmentSupport>();

    private List<HCMenuEntity> menuLists;

    private FragmentTabAdapter tabAdapter;

    private boolean isExit = false;

    private ToastView mToastView;

    private boolean isGpsOpen = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme);
        if (this.tabAdapter != null) {
            this.tabAdapter.remove();
        }
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.mActivityFragmentView.viewMain(R.layout.activity_main);
        this.mActivityFragmentView.viewEmpty(R.layout.activity_empty);
        this.mActivityFragmentView.viewEmptyGone();
        this.mActivityFragmentView.viewLoading(View.GONE);
        this.mActivityFragmentView.clipToPadding(true);

        requestPermission();

        /**
         * 开启检测版本任务
         */
        CheckVersionTask checkVersionTask = new CheckVersionTask(this);
        checkVersionTask.start();

        setContentView(this.mActivityFragmentView);
        com.lidroid.xutils.ViewUtils.inject(this);
        menuList();
        try {
            // 动态加载tab
            // 动态设置tab item

            for (int i = 0; i < this.menuLists.size(); i++) {
                HCMenuEntity menu = this.menuLists.get(i);
                if (menu.getStatus() == -1) {
                    @SuppressWarnings("unchecked")
                    Class<FragmentSupport> clazz = (Class<FragmentSupport>) Class.forName(menu.getClazz());
                    FragmentSupport fragmentSuper = clazz.newInstance();
                    fragmentSuper.fragmentId = menu.getmId();
                    this.fragments.add(fragmentSuper);
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        this.tabAdapter = new FragmentTabAdapter(this, this.fragments, R.id.tab_content, this.radioGroup);
        this.tabAdapter.setOnRgsExtraCheckedChangedListener(new FragmentTabAdapter.OnRgsExtraCheckedChangedListener() {

            @Override
            public void OnRgsExtraCheckedChanged(RadioGroup radioGroup, int checkedId, int index) {
                System.out.println("Extra---- " + index + " checked!!! ");
            }
        });
        initData();
    }

    public void requestPermission(){
        PermissionGen.with(MainActivity.this)
                .addRequestCode(100)
                .permissions(
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.CHANGE_WIFI_STATE,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .request();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                                     int[] grantResults) {
        PermissionGen.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }

    @PermissionSuccess(requestCode = 100)
    public void doSomething(){
        ToastUtils.show(this,R.string.grant_permission);
    }

    @PermissionFail(requestCode = 100)
    public void doFailSomething(){
        ToastUtils.show(this,R.string.refuse_permission);
    }

    @Override
    public void initData() {
        mActivityFragmentView.getNavitionBarView().setVisibility(View.GONE);
    }

    /**
     * 牵扯到的tab items
     */
    public void menuList() {
        this.menuLists = new ArrayList<HCMenuEntity>();
        HCMenuEntity mMenuMsg = new HCMenuEntity(1, getResources().getString(R.string.hc_home),
                R.drawable.selector_msg, MsgFragment.class.getName(), -1);// 消息
        this.menuLists.add(mMenuMsg);
        HCMenuEntity mMenuApp = new HCMenuEntity(2, getResources().getString(R.string.hc_app),
                R.drawable.selector_app, AppFragment.class.getName(), -1);// 应用
        this.menuLists.add(mMenuApp);
        HCMenuEntity mMenuWork = new HCMenuEntity(3, getResources().getString(R.string.hc_work),
                R.drawable.selector_work, WorkFragment.class.getName(), -1);// 工作
        this.menuLists.add(mMenuWork);
        HCMenuEntity mMenuMyself = new HCMenuEntity(4, getResources().getString(R.string.hc_myself),
                R.drawable.selector_my, MyFragment.class.getName(), -1);// 个人中心
        this.menuLists.add(mMenuMyself);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if ((event.getKeyCode() == KeyEvent.KEYCODE_BACK) && (event.getAction() != KeyEvent.ACTION_UP)) {
            if (!this.isExit) {
                this.isExit = true;
                this.mToastView = ToastView.makeText(this, getResources().getString(R.string.home_exit)).setGravity(
                        Gravity.CENTER, 0, 0);
                this.mToastView.show();
                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        MainActivity.this.isExit = false;
                    }
                }, 3000);
                return true;
            }
            else {
                this.mToastView.cancel();
                this.mApplication.exit();
                return false;
            }
        }
        return super.dispatchKeyEvent(event);
    }

    @Override
    public void onResume() {
        super.onResume();
        checkGpsService();

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while(!isGpsOpen){
                    LocationManager locationManager = (LocationManager)getContext().getSystemService(Context.LOCATION_SERVICE);
                    boolean gpsIsStart = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
                    if(!gpsIsStart){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                createDialog();
                            }
                        });
                        isGpsOpen = false;
                    }else {
                        isGpsOpen = true;
                    }
                    try{
                        Thread.sleep(10000);
                    }catch (InterruptedException e){
                        e.printStackTrace();
                    }
                }
            }
        });
        thread.start();
    }

    /**
     * 检测系统GPS定位服务是否开启
     */
    public void checkGpsService(){
        LocationManager locationManager = (LocationManager)getContext().getSystemService(Context.LOCATION_SERVICE);
        boolean gpsIsStart = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if(!gpsIsStart){
            createDialog();
            isGpsOpen = false;
        }else {
            isGpsOpen = true;
        }
    }

   public void createDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //设置对话框图标，可以使用自己的图片，Android本身也提供了一些图标供我们使用
        // builder.setIcon(android.R.drawable.ic_dialog_alert);
        //设置对话框标题
        builder.setTitle(getString(R.string.gps_location));
        //设置对话框内的文本
        builder.setMessage(getString(R.string.start_gps_service));
        //设置确定按钮，并给按钮设置一个点击侦听，注意这个OnClickListener使用的是DialogInterface类里的一个内部接口
        builder.setPositiveButton(getString(R.string.goto_setting), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 执行点击确定按钮的业务逻辑
                Intent intent=new Intent();
                intent.setAction(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
                dialog.dismiss();
            }
        });
        //设置取消按钮
        builder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 执行点击取消按钮的业务逻辑
                dialog.dismiss();
            }
        });
        //使用builder创建出对话框对象
        AlertDialog dialog = builder.create();
        //显示对话框
        dialog.show();
    }

}
