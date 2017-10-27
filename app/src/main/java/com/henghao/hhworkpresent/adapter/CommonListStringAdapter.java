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
 * 通用ListView〈一句话功能简述〉 〈功能详细描述〉
 * @author zhangxianwen
 * @version HDMNV100R001, 2015年12月21日
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class CommonListStringAdapter extends ArrayAdapter<String> {

    private final LayoutInflater inflater;

    private final ActivityFragmentSupport mActivityFragmentSupport;

    public CommonListStringAdapter(ActivityFragmentSupport activityFragment, List<String> mList) {
        super(activityFragment, R.layout.common_textview, mList);
        this.mActivityFragmentSupport = activityFragment;
        this.inflater = LayoutInflater.from(activityFragment);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        HodlerView mHodlerView = null;
        if (convertView == null) {
            mHodlerView = new HodlerView();
            convertView = this.inflater.inflate(R.layout.common_textview, null);
            mHodlerView.tv_title = (TextView) convertView.findViewById(R.id.tv_common);
            convertView.setTag(mHodlerView);
        }
        else {
            mHodlerView = (HodlerView) convertView.getTag();
        }
        mHodlerView.tv_title.setText(getItem(position));
        return convertView;
    }

    private class HodlerView {

        TextView tv_title;
    }
}
