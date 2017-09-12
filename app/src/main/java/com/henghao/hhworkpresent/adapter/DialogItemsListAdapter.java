package com.henghao.hhworkpresent.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.henghao.hhworkpresent.R;
import com.henghao.hhworkpresent.entity.JianchaMaterialEntity;

import java.util.HashMap;
import java.util.List;

/**
 * 对话框列表显示适配器
 * Created by ASUS on 2017/9/8.
 */

public class DialogItemsListAdapter extends BaseAdapter{

    private Context mContext;
    private List<JianchaMaterialEntity> mList;
    // 用来控制CheckBox的选中状况
    private static HashMap<Integer, Boolean> isSelected;

    public DialogItemsListAdapter(Context context,List<JianchaMaterialEntity> list) {
        this.mContext = context;
        this.mList = list;
        isSelected = new HashMap<Integer, Boolean>();
        // 初始化数据
        initData();
    }

    // 初始化isSelected的数据
    private void initData() {
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
            convertView= LayoutInflater.from(mContext).inflate(R.layout.listview_dialog_item, null);
            mHodlerView.tv_descript_title = (TextView) convertView.findViewById(R.id.tv_descript_title);
            mHodlerView.tv_descript = (TextView) convertView.findViewById(R.id.tv_descript);
            mHodlerView.checkBox = (CheckBox) convertView.findViewById(R.id.checkBox);
            convertView.setTag(mHodlerView);
        } else {
            mHodlerView = (HodlerView) convertView.getTag();
        }
        mHodlerView.tv_descript_title.setText(mList.get(position).getTitle());
        mHodlerView.tv_descript.setText(mList.get(position).getDescript());
        // 根据isSelected来设置checkbox的选中状况
        mHodlerView.checkBox.setChecked(getIsSelected().get(position));
        return convertView;
    }

    public static class HodlerView {

        public TextView tv_descript_title;

        public TextView tv_descript;

        public CheckBox checkBox;
    }

    public static HashMap<Integer, Boolean> getIsSelected() {
        return isSelected;
    }

    public static void setIsSelected(HashMap<Integer, Boolean> isSelected) {
        isSelected = isSelected;
    }
}
