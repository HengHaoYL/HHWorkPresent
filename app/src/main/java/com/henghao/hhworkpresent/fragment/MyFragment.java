package com.henghao.hhworkpresent.fragment;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.benefit.buy.library.phoneview.MultiImageSelectorActivity;
import com.benefit.buy.library.utils.tools.ToolsKit;
import com.henghao.hhworkpresent.FragmentSupport;
import com.henghao.hhworkpresent.R;
import com.henghao.hhworkpresent.activity.LoginActivity;
import com.henghao.hhworkpresent.activity.MySelfZiliaoActivity;
import com.henghao.hhworkpresent.activity.MyTongxunluActivity;
import com.henghao.hhworkpresent.views.CircleImageView;
import com.henghao.hhworkpresent.views.DatabaseHelper;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.util.ArrayList;

/**
 * Created by bryanrady on 2017/2/28.
 */

public class MyFragment extends FragmentSupport {

    /**
     * 圆形图片头像制作
     */
    @ViewInject(R.id.fragment_my_circleImageview)
    private CircleImageView circleImageView;

    @ViewInject(R.id.fragment_my_changeImageTV)
    private TextView tvChangeImage;

    @ViewInject(R.id.fragment_my_selfziliao)
    private TextView tv_selfziliao;

    @ViewInject(R.id.fragment_my_tongxunlu)
    private TextView tv_tongxunlu;

    @ViewInject(R.id.tv_exitlogin)
    private TextView tv_exitlogin;

    private ArrayList<String> mSelectPath;

    private static final int REQUEST_IMAGE = 0x00;

    private ArrayList<String> mImageList = new ArrayList<>();

    private DatabaseHelper dbHelper;
    private SQLiteDatabase db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        this.mActivityFragmentView.viewMain(R.layout.fragment_myself);
        this.mActivityFragmentView.viewEmpty(R.layout.activity_empty);
        this.mActivityFragmentView.viewEmptyGone();
        this.mActivityFragmentView.viewLoading(View.GONE);
        ViewUtils.inject(this, this.mActivityFragmentView);
        initWidget();
        initData();
        return this.mActivityFragmentView;
    }

    public void initWidget(){
        initwithContent();
    }

    public void initData(){
    }

    private void initwithContent() {
        initWithCenterBar();
        this.mCenterTextView.setVisibility(View.VISIBLE);
        this.mCenterTextView.setText("个人中心");
    }

    public void choosePicture(){
        // 查看session是否过期
        // int selectedMode = MultiImageSelectorActivity.MODE_SINGLE;
        //设置单选模式
        int selectedMode = MultiImageSelectorActivity.MODE_SINGLE;
        int maxNum = 9;
        Intent picIntent = new Intent(this.mActivity, MultiImageSelectorActivity.class);
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            if (requestCode == REQUEST_IMAGE) {
                if ((resultCode == Activity.RESULT_OK) || (resultCode == Activity.RESULT_CANCELED)) {
                    this.mSelectPath = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
                    if (!ToolsKit.isEmpty(this.mSelectPath)) {
                        for (String imagePath : mSelectPath) {
                            Bitmap bm = BitmapFactory.decodeFile(imagePath);
                            //设置图片
                            circleImageView.setImageBitmap(bm);
                        }
                    }

                }
            }
        }
    }

    @OnClick({R.id.fragment_my_changeImageTV,R.id.fragment_my_selfziliao,R.id.fragment_my_tongxunlu,R.id.tv_exitlogin})
    private void viewOnClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.fragment_my_changeImageTV:
                choosePicture();
                break;

            case R.id.fragment_my_selfziliao:    //个人资料
                intent.setClass(mActivity, MySelfZiliaoActivity.class);
                startActivity(intent);
                break;

            case R.id.fragment_my_tongxunlu:        //通讯录
                intent.setClass(mActivity,MyTongxunluActivity.class);
                startActivity(intent);
                break;

            case R.id.tv_exitlogin:        //退出登录
                dbHelper = new DatabaseHelper(this.mActivity,"user_login.db");
                db = dbHelper.getReadableDatabase();
                Cursor cursor = db.query("user",new String[]{"uid"},null,null,null,null,null);
                String uid = null;
                while (cursor.moveToNext()){
                   uid = cursor.getString((cursor.getColumnIndex("uid")));
                }
                //删除用户信息
                db.delete("user","id=?",new String[]{uid});
                intent.setClass(mActivity,LoginActivity.class);
                startActivity(intent);
                break;

        }
    }
}
