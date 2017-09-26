package com.henghao.hhworkpresent.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.henghao.hhworkpresent.R;
import com.henghao.hhworkpresent.entity.SaveCheckTaskEntity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 我要复查列表适配器
 * Created by ASUS on 2017/9/25.
 */

public class WoyaoFuchaListAdapter extends BaseAdapter {

    public Context mContext;
    public List<SaveCheckTaskEntity> mList;

    public WoyaoFuchaListAdapter(Context context,List<SaveCheckTaskEntity> list){
        super();
        this.mContext = context;
        this.mList = list;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int arg0) {
        return mList.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        return arg0;
    }

    @Override
    public View getView(final int arg0, View convertView, ViewGroup arg2) {
        ViewHolder viewHolder;
        if(convertView==null){
            convertView= LayoutInflater.from(mContext).inflate(R.layout.listview_woyao_check_item, null);
            viewHolder = new ViewHolder();
            viewHolder.tv_woyaojiancha_company_name=(TextView) convertView.findViewById(R.id.tv_woyaojiancha_company_name);
            viewHolder.tv_woyaoojiancha_xianshi=(TextView) convertView.findViewById(R.id.tv_woyaoojiancha_xianshi);
            viewHolder.tv_woyaojiancha_check_people=(TextView) convertView.findViewById(R.id.tv_woyaojiancha_check_people);
            //    viewHolder.tv_add=(TextView) convertView.findViewById(R.id.tv_add);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tv_woyaojiancha_company_name.setText(mList.get(arg0).getEnterprise().getEntname());
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        viewHolder.tv_woyaoojiancha_xianshi.setText(format.format(new Date(Long.parseLong(mList.get(arg0).getCreatePlanTime()))));
        viewHolder.tv_woyaojiancha_check_people.setText(mList.get(arg0).getCheckPeople1() +" "+ mList.get(arg0).getTroopemp().getName());
        return convertView;
    }

    class ViewHolder{
        TextView tv_woyaojiancha_company_name;
        TextView tv_woyaoojiancha_xianshi;
        TextView tv_woyaojiancha_check_people;
        //   TextView tv_add;
    }
}
