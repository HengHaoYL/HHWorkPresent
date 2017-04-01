package com.henghao.hhworkpresent.utils;

/**
 * Created by bryanrady on 2017/3/20.
 */

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@SuppressLint("SimpleDateFormat")
public class Utility {

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
     public static int dip2px(Context context, float dpValue) {
         final float scale = context.getResources().getDisplayMetrics().density;
         return (int) (dpValue * scale + 0.5f);
     }

    /**
     * 获取控件的高度，如果获取的高度为0，则重新计算尺寸后再返回高度
     *
     * @param view
     * @return
     */
     public static int getViewMeasuredHeight(View view) {
         calcViewMeasure(view);
         return view.getMeasuredHeight();
     }

     /**
      * 测量控件的尺寸
      *
      * @param view
      */
      public static void calcViewMeasure(View view) {
          int width = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
          int expandSpec = View.MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, View.MeasureSpec.AT_MOST);
          view.measure(width, expandSpec);
      }

      public static String dateFormat(Date date) {
          SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
          return sdf.format(date);
      }

      public static String dateFormatDay(Date date) {
          SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
          return sdf.format(date);
      }

     /**
      * 获取月末最后一天
      *
      * @param sDate
      *            2014-11-24
      * @return 30
      */
      public static String getMonthMaxDay(String sDate) {
          SimpleDateFormat sdf_full = new SimpleDateFormat("yyyy-MM-dd");
          Calendar cal = Calendar.getInstance();
          Date date = null;
          try {
              date = sdf_full.parse(sDate + "-01");
          } catch (ParseException e) {
              e.printStackTrace();
          }
          cal.setTime(date);
          int last = cal.getActualMaximum(Calendar.DATE);
          return String.valueOf(last);
      }
}
