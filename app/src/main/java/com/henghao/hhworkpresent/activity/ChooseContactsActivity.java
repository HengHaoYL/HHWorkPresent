package com.henghao.hhworkpresent.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;

import com.benefit.buy.library.views.xlistview.XListView;
import com.henghao.hhworkpresent.ActivityFragmentSupport;
import com.henghao.hhworkpresent.R;
import com.henghao.hhworkpresent.adapter.ContactsListAdapter;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.util.ArrayList;

/**
 * Created by bryanrady on 2017/3/14.
 *
 * 选择联系人界面
 */

public class ChooseContactsActivity  extends ActivityFragmentSupport {

    @ViewInject(R.id.listview_choose_contacts)
    private XListView contactsListview;

    @ViewInject(R.id.contacts_currentCompangName)
    private TextView currentCompanyName;

    @ViewInject(R.id.contacts_allSelctBtn)
    private Button allSelectBtn;

    @ViewInject(R.id.contacts_cancelBtn)
    private Button cacelBtn;

    @ViewInject(R.id.contacts_alreadyChooseTv)
    private TextView alreadyChooseTV;

    private ContactsListAdapter adapter;

    /**记录选中的条数*/
    private int checkNum;

    private ArrayList<String> mList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.mActivityFragmentView.viewMain(R.layout.activity_choosecontacts);
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
        mLeftTextView.setText("选择联系人");
        mLeftTextView.setVisibility(View.VISIBLE);
        mLeftImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createDialog();
            }
        });
        initWithRightBar();
    }

    @Override
    public void initData() {
        super.initData();
        mList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            mList.add("测试"+i);
        }
        adapter = new ContactsListAdapter(mList,this);
        contactsListview.setAdapter(adapter);

        contactsListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // 取得ViewHolder对象，这样就省去了通过层层的findViewById去实例化我们需要的cb实例的步骤
                ContactsListAdapter.ViewHolder holder = (ContactsListAdapter.ViewHolder) view.getTag();
                // 改变CheckBox的状态
                holder.checkBox.toggle();
                // 将CheckBox的选中状况记录下来
                adapter.getIsSelected().put(position, holder.checkBox.isChecked());
                // 调整选定条目
                if (holder.checkBox.isChecked() == true) {
                    checkNum++;
                } else {
                    checkNum--;
                }
                alreadyChooseTV.setText("已选择" + checkNum+"/"+mList.size());
                initContetnWithBar();
            }
        });
    }

    public void initContetnWithBar(){
        mRightTextView.setText("确定");
        if(checkNum>0){
            mRightTextView.setVisibility(View.VISIBLE);
            mRightTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.putExtra("key", checkNum+"");//key和value分别是要传递数据的名称和数值
                    setResult(RESULT_OK, intent);
                    finish();
                }
            });
        }else {
            mRightTextView.setVisibility(View.GONE);
        }
    }

    @OnClick({R.id.contacts_allSelctBtn,R.id.contacts_cancelBtn})
    private void viewOnClick(View v){
        Intent intent = new Intent();
        switch (v.getId()){
            case R.id.contacts_allSelctBtn:
                // 遍历list的长度，将Adapter中的map值全部设为true
                for (int i = 0; i < mList.size(); i++) {
                adapter.getIsSelected().put(i, true);
                }
                // 数量设为list的长度
                checkNum = mList.size();
                // 刷新listview和TextView的显示
                initContetnWithBar();
                dataChanged();
                break;

            case R.id.contacts_cancelBtn:
                // 遍历list的长度，将已选的按钮设为未选
                 for (int i = 0; i < mList.size(); i++) {
                      if (adapter.getIsSelected().get(i)) {
                          adapter.getIsSelected().put(i, false);
                          checkNum--;// 数量减1
                    }
                 }
                 // 刷新listview和TextView的显示
                initContetnWithBar();
                dataChanged();
                break;
        }
    }

    // 刷新listview和TextView的显示
    private void dataChanged() {
        // 通知listView刷新
        adapter.notifyDataSetChanged();
        // TextView显示最新的选中数目
        alreadyChooseTV.setText("已选择" + checkNum+"/"+mList.size());

    }

    /**
     * 创建对话框
     */
    public void createDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //设置对话框内的文本
        builder.setMessage("确认退出此操作吗？");
        //设置确定按钮，并给按钮设置一个点击侦听，注意这个OnClickListener使用的是DialogInterface类里的一个内部接口
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

            @Override

            public void onClick(DialogInterface dialog, int which) {
                // 执行点击确定按钮的业务逻辑
                dialog.dismiss();
                finish();
            }
        });
        //设置取消按钮
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

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
