package com.henghao.hhworkpresent.adapter;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by bryanrady on 2017/3/20.
 *
 * 我们有一个需求需要左右切换，我们选用最熟悉的ViewPager，但这里有个问题，怎么实现无限循环呢，这里我们传入一个日历卡数组，
 * 让ViewPager循环复用这几个日历卡，避免消耗内存
 */

public class CalendarViewAdapter<V extends View> extends PagerAdapter {
    public static final String TAG = "CalendarViewAdapter";
    private V[] views;


    public CalendarViewAdapter(V[] views) {
        super();
        this.views = views;
    }



    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        if (((ViewPager) container).getChildCount() == views.length) {
            ((ViewPager) container).removeView(views[position % views.length]);
        }

        ((ViewPager) container).addView(views[position % views.length], 0);
        return views[position % views.length];
    }


    @Override
    public int getCount() {
        return Integer.MAX_VALUE;
    }


    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((View) object);
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((View) container);
    }

    public V[] getAllItems() {
        return views;
    }


}
