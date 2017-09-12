package com.henghao.hhworkpresent.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.henghao.hhworkpresent.R;
import com.henghao.hhworkpresent.activity.JianchaTaskActivity;
import com.henghao.hhworkpresent.entity.CompanyInfoEntity;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * 企业信息列表适配器
 * Created by ASUS on 2017/9/5.
 */
public class CompanyInfoXListAdapter extends BaseAdapter {

    public Context mContext;
    public List<CompanyInfoEntity.DataBean> mList;

    public CompanyInfoXListAdapter(Context context,List<CompanyInfoEntity.DataBean> list){
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
            convertView= LayoutInflater.from(mContext).inflate(R.layout.listview_item_company, null);
            viewHolder = new ViewHolder();
            viewHolder.tv_company_name=(TextView) convertView.findViewById(R.id.tv_company_name);
            viewHolder.tv_check_plan=(TextView) convertView.findViewById(R.id.tv_check_plan);
            viewHolder.tv_check_time=(TextView) convertView.findViewById(R.id.tv_check_time);
        //    viewHolder.tv_add=(TextView) convertView.findViewById(R.id.tv_add);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tv_company_name.setText(mList.get(arg0).getEntname());
        viewHolder.tv_check_plan.setText(mList.get(arg0).getIsplan());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        viewHolder.tv_check_time.setText(simpleDateFormat.format(mList.get(arg0).getUpdatetime()));
        /*viewHolder.tv_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(mContext,JianchaTaskActivity.class);
                intent.putExtra("companyname",mList.get(arg0).getEntname());
                mContext.startActivity(intent);
            }
        });*/
        return convertView;
    }

    class ViewHolder{
        TextView tv_company_name;
        TextView tv_check_plan;
        TextView tv_check_time;
    //   TextView tv_add;
    }

}
