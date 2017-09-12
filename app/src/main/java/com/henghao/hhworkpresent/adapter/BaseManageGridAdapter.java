package com.henghao.hhworkpresent.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.henghao.hhworkpresent.R;

import java.util.HashMap;
import java.util.List;

/**
 * 基础管理部分多选框适配器
 * Created by ASUS on 2017/9/6.
 */

public class BaseManageGridAdapter extends BaseAdapter{

    private Context mContext;
    private List<String> mList;
    // 用来控制CheckBox的选中状况
    private static HashMap<Integer, Boolean> isSelected;

    public BaseManageGridAdapter(Context context,List<String> list) {
        this.mContext = context;
        this.mList = list;
        isSelected = new HashMap<Integer, Boolean>();
        // 初始化数据
        initData();
    }

    // 初始化isSelected的数据
    private void initData() {
        //这里不加1 的话 i只到12  原本应该是13
        for (int i = 0; i < mList.size()+1; i++) {
            getIsSelected().put(i, false);
        }
    }

    @Override
    public int getCount() {
        return mList.size(); // 返回Adapter中数据集的条目数
    }
    @Override
    public Object getItem(int position) {
        return mList.get(position); // 获取数据集中与指定索引对应的数据项
    }
    @Override
    public long getItemId(int position) {
        return position; // 取在列表中与指定索引对应的行id
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        HodlerView mHodlerView = null;
        if (convertView == null) {
            mHodlerView = new HodlerView();
            convertView= LayoutInflater.from(mContext).inflate(R.layout.gridview_item_base_manage, null);
            mHodlerView.tv_manage_title = (TextView) convertView.findViewById(R.id.tv_manage_title);
            mHodlerView.checkbox_manage = (CheckBox) convertView.findViewById(R.id.checkbox_manage);
            convertView.setTag(mHodlerView);
        } else {
            mHodlerView = (HodlerView) convertView.getTag();
        }
        // 设置list中TextView的显示
        mHodlerView.tv_manage_title.setText(mList.get(position));
        // 根据isSelected来设置checkbox的选中状况
        mHodlerView.checkbox_manage.setChecked(getIsSelected().get(position));
        return convertView;
    }

    public static class HodlerView {

        public TextView tv_manage_title;

        public CheckBox checkbox_manage;
    }

    public static HashMap<Integer, Boolean> getIsSelected() {
        return isSelected;
    }

    public static void setIsSelected(HashMap<Integer, Boolean> isSelected) {
        isSelected = isSelected;
    }

}
