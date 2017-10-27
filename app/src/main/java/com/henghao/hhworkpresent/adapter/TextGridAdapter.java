package com.henghao.hhworkpresent.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.henghao.hhworkpresent.ActivityFragmentSupport;
import com.henghao.hhworkpresent.R;

import java.util.List;

/**
 * 检查文书的九宫格适配器
 * Created by ASUS on 2017/9/20.
 */

public class TextGridAdapter extends ArrayAdapter<String> {

    private final LayoutInflater inflater;

    private final ActivityFragmentSupport mActivityFragmentSupport;

    private final List<String> mList;

    public TextGridAdapter(ActivityFragmentSupport activityFragment, List<String> mList2){
        super(activityFragment, R.layout.layout_gridview_text_item, mList2);
        this.mActivityFragmentSupport = activityFragment;
        this.mList = mList2;
        this.inflater = LayoutInflater.from(activityFragment);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        HodlerView mHodlerView = null;
        if (convertView == null) {
            mHodlerView = new HodlerView();
            convertView = this.inflater.inflate(R.layout.layout_gridview_text_item, null);
            mHodlerView.tv_text = (TextView) convertView.findViewById(R.id.tv_text);
            convertView.setTag(mHodlerView);
        } else {
            mHodlerView = (HodlerView) convertView.getTag();
        }
        mHodlerView.tv_text.setVisibility(View.VISIBLE);
        mHodlerView.tv_text.setText(mList.get(position).toString());
        viewOnClick(mHodlerView, convertView, position);
        return convertView;
    }

    class HodlerView {
        TextView tv_text;
    }

    private void viewOnClick(HodlerView mHodlerView, View convertView, final int position) {
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (position){
                    case 0:
                        break;
                }
            }

        });
    }
}
