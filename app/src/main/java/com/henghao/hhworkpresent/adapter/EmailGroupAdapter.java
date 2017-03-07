package com.henghao.hhworkpresent.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.henghao.hhworkpresent.R;

/**
 * Created by bryanrady on 2017/3/3.
 * 邮箱分组适配器
 */

public class EmailGroupAdapter extends BaseExpandableListAdapter {

    private String[] group;
    private String[][] buddy;
    private Context context;
    LayoutInflater inflater;

    public EmailGroupAdapter(Context context,String[] group,String[][] buddy){
        this.context=context;
        inflater = LayoutInflater.from(context);
        this.group=group;
        this.buddy=buddy;
    }
    public Object getChild(int groupPosition, int childPosition) {
        return buddy[groupPosition][childPosition];
    }

    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    public View getChildView(int groupPosition, int childPosition, boolean arg2, View convertView,
            ViewGroup arg4) {
        convertView = inflater.inflate(R.layout.listview_child_item, null);
        TextView nickTextView=(TextView) convertView.findViewById(R.id.tv_title);
        nickTextView.setText(getChild(groupPosition, childPosition).toString());
        return convertView;
    }

    public int getChildrenCount(int groupPosition) {
        return buddy[groupPosition].length;
    }

    public Object getGroup(int groupPosition) {
        return group[groupPosition];
    }

    public int getGroupCount() {
        return group.length;
    }

    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup arg3) {
        convertView = inflater.inflate(R.layout.listview_group_item, null);
        TextView groupNameTextView=(TextView) convertView.findViewById(R.id.buddy_listview_group_name);
        groupNameTextView.setText(getGroup(groupPosition).toString());
        ImageView image = (ImageView) convertView.findViewById(R.id.buddy_listview_image);
/*        image.setImageResource(R.drawable.item_point_right);
        //更换展开分组图片
        if(!isExpanded){
            image.setImageResource(R.drawable.item_point_down);
        }*/
        return convertView;
    }

    public boolean hasStableIds() {
        return true;
    }
    // 子选项是否可以选择
    public boolean isChildSelectable(int arg0, int arg1) {
        // TODO Auto-generated method stub
        return true;
    }

}
