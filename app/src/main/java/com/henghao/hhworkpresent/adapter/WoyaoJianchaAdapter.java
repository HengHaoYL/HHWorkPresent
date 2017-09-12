package com.henghao.hhworkpresent.adapter;

import android.view.LayoutInflater;
import android.widget.ArrayAdapter;

import com.henghao.hhworkpresent.ActivityFragmentSupport;
import com.henghao.hhworkpresent.R;
import com.henghao.hhworkpresent.entity.WoyaoJianchaEntity;

import java.util.List;

/**
 * Created by ASUS on 2017/9/4.
 */
public class WoyaoJianchaAdapter extends ArrayAdapter<WoyaoJianchaEntity> {

    private final LayoutInflater inflater;

    private final ActivityFragmentSupport mActivityFragmentSupport;

    public WoyaoJianchaAdapter(ActivityFragmentSupport activityFragment, List<WoyaoJianchaEntity> mList){
        super(activityFragment, R.layout.listview_woyaojiancha_item, mList);
        this.mActivityFragmentSupport = activityFragment;
        this.inflater = LayoutInflater.from(activityFragment);

    }
}
