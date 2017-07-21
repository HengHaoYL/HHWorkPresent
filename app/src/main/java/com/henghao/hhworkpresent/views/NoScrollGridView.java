package com.henghao.hhworkpresent.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * 如果不适用这个，当我们在Listview显示gridview时候会出现gridview只会显示一行的情况
 * 即两个Scrollview控件不能相互嵌套，必须经过处理
 * Created by bryanrady on 2017/7/21.
 */

public class NoScrollGridView extends GridView {

    public NoScrollGridView(Context context) {
        super(context);

    }
    public NoScrollGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
