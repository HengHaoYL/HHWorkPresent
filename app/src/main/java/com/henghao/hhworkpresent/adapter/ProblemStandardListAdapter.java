package com.henghao.hhworkpresent.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.henghao.hhworkpresent.R;
import com.henghao.hhworkpresent.activity.AddYinhuanActivity;
import com.henghao.hhworkpresent.entity.SaveCheckTaskEntity;

import java.util.List;

/**
 * 检查执法标准问题列表适配器
 * Created by ASUS on 2017/9/12.
 */

public class ProblemStandardListAdapter extends BaseAdapter {

    private Context mContext;
    private List<SaveCheckTaskEntity.JianchaMaterialEntityListBean> mList;

    public ProblemStandardListAdapter(Context context,List<SaveCheckTaskEntity.JianchaMaterialEntityListBean> list) {
        this.mContext = context;
        this.mList = list;
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        HodlerView mHodlerView = null;
        if (convertView == null) {
            mHodlerView = new HodlerView();
            convertView= LayoutInflater.from(mContext).inflate(R.layout.listview_standard_item, null);
            mHodlerView.tv_standard_title = (TextView) convertView.findViewById(R.id.tv_standard_title);
            mHodlerView.tv_standard_description = (TextView) convertView.findViewById(R.id.tv_standard_description);
            mHodlerView.standard_radioGroup = (RadioGroup) convertView.findViewById(R.id.standard_radioGroup);
            mHodlerView.radio_conform = (RadioButton) convertView.findViewById(R.id.radio_conform);
            mHodlerView.radio_not_conform = (RadioButton) convertView.findViewById(R.id.radio_not_conform);
            mHodlerView.tv_add_yinhuan = (TextView) convertView.findViewById(R.id.tv_add_yinhuan);
            convertView.setTag(mHodlerView);
        } else {
            mHodlerView = (HodlerView) convertView.getTag();
        }
        mHodlerView.tv_standard_title.setText(mList.get(position).getTitle());
        mHodlerView.tv_standard_description.setText(mList.get(position).getDescript());
        mHodlerView.standard_radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == HodlerView.radio_conform.getId()){

                }else if(checkedId == HodlerView.radio_not_conform.getId()){
                    Intent intent = new Intent();
                    intent.setClass(mContext,AddYinhuanActivity.class);
                    intent.putExtra("JianchaMaterialEntityListBean",mList.get(position));
                    mContext.startActivity(intent);
                }
            }
        });
        return convertView;
    }

    public static class HodlerView {

        public TextView tv_standard_title;

        public TextView tv_standard_description;

        public static RadioGroup standard_radioGroup;

        public static RadioButton radio_conform;

        public static RadioButton radio_not_conform;

        public static TextView tv_add_yinhuan;

    }

}
