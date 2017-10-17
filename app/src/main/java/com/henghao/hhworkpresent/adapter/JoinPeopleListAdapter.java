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
 * 参加会议人员
 * Created by bryanrady on 2017/10/17.
 */

public class JoinPeopleListAdapter extends BaseAdapter {

    private Context mContext;
    private List<String> mList;
    // 用来控制CheckBox的选中状况
    private static HashMap<Integer, Boolean> isSelected;

    public JoinPeopleListAdapter(Context context, List<String> list) {
        this.mContext = context;
        this.mList = list;
        isSelected = new HashMap<Integer, Boolean>();
        initData();
    }

    private void initData() {
        for (int i = 0; i < mList.size(); i++) {
            getIsSelected().put(i, false);
        }
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        HodlerView mHodlerView = null;
        if (convertView == null) {
            mHodlerView = new HodlerView();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.listview_meeting_personal_item, null);
            mHodlerView.tv_personal_name = (TextView) convertView.findViewById(R.id.tv_personal_name);
            mHodlerView.personal_checkbox = (CheckBox) convertView.findViewById(R.id.personal_checkbox);
            convertView.setTag(mHodlerView);
        } else {
            mHodlerView = (HodlerView) convertView.getTag();
        }
        mHodlerView.tv_personal_name.setText(mList.get(position));
        // 根据isSelected来设置checkbox的选中状况
        mHodlerView.personal_checkbox.setChecked(getIsSelected().get(position));
        return convertView;
    }

    public static class HodlerView {

        public TextView tv_personal_name;

        public CheckBox personal_checkbox;
    }

    public static HashMap<Integer, Boolean> getIsSelected() {
        return isSelected;
    }

    public static void setIsSelected(HashMap<Integer, Boolean> isSelected) {
        isSelected = isSelected;
    }
}
