package com.henghao.hhworkpresent.utils;

import android.annotation.SuppressLint;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class DateTimeUtils {

    public static String[] weekName = { "周日", "周一", "周二", "周三", "周四", "周五","周六" };

    public static int getMonthDays(int year, int month) {
        if (month > 12) {
            month = 1;
            year += 1;
        } else if (month < 1) {
            month = 12;
            year -= 1;
        }
        int[] arr = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };
        int days = 0;

        if ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0) {
            arr[1] = 29; // 闰年2月29天
        }

        try {
            days = arr[month - 1];
        } catch (Exception e) {
            e.getStackTrace();
        }

        return days;
    }

    public static int getYear() {
        return Calendar.getInstance().get(Calendar.YEAR);
    }

    public static int getMonth() {
        return Calendar.getInstance().get(Calendar.MONTH) + 1;
    }

    public static int getCurrentMonthDay() {
        return Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
    }

    public static int getWeekDay() {
        return Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
    }

    public static int getHour() {
        return Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
    }
    public static int getMinute() {
        return Calendar.getInstance().get(Calendar.MINUTE);
    }
    public static CustomDate getNextSunday() {

        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, 7 - getWeekDay()+1);
        CustomDate date = new CustomDate(c.get(Calendar.YEAR),
                c.get(Calendar.MONTH)+1, c.get(Calendar.DAY_OF_MONTH));
        return date;
    }

    public static int[] getWeekSunday(int year, int month, int day, int pervious) {
        int[] time = new int[3];
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, day);
        c.add(Calendar.DAY_OF_MONTH, pervious);
        time[0] = c.get(Calendar.YEAR);
        time[1] = c.get(Calendar.MONTH )+1;
        time[2] = c.get(Calendar.DAY_OF_MONTH);
        return time;

    }

    public static int getWeekDayFromDate(int year, int month) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(getDateFromString(year, month));
        int week_index = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (week_index < 0) {
            week_index = 0;
        }
        return week_index;
    }

    @SuppressLint("SimpleDateFormat")
    public static Date getDateFromString(int year, int month) {
        String dateString = year + "-" + (month > 9 ? month : ("0" + month))
                + "-01";
        Date date = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            date = sdf.parse(dateString);
        } catch (ParseException e) {
            System.out.println(e.getMessage());
        }
        return date;
    }
    public static boolean isToday(CustomDate date){
        return(date.year == DateTimeUtils.getYear() &&
                date.month == DateTimeUtils.getMonth()
                && date.day == DateTimeUtils.getCurrentMonthDay());
    }

    public static boolean isCurrentMonth(CustomDate date){
        return(date.year == DateTimeUtils.getYear() &&
                date.month == DateTimeUtils.getMonth());
    }

    /**
     * 格式化日期  将.换为-
     */
    public static String changeDate(String date){
        String newDate = null;
        String[] arr = date.split("\\.");
        int year = Integer.parseInt(arr[0]);
        int month = Integer.parseInt(arr[1]);
        int dayOfMonth = Integer.parseInt(arr[2]);
        String month1 = null;
        String dayOfMonth1 = null;

        newDate = year + "-" + month + "-" + dayOfMonth;
        if(month<10 && dayOfMonth<10){
            month1 = "0"+ month;
            dayOfMonth1 = "0"+ dayOfMonth;
            newDate = year + "-" + month1 + "-" + dayOfMonth1;
        }
        if(month<10 && dayOfMonth>=10){
            month1 = "0"+ month;
            dayOfMonth1 = dayOfMonth+"";
            newDate = year + "-" + month1 + "-" + dayOfMonth1;
        }
        if(month>=10 && dayOfMonth<10){
            month1 = month+"";
            dayOfMonth1 = "0"+ dayOfMonth;
            newDate = year + "-" + month1 + "-" + dayOfMonth1;
        }
        return newDate;
    }

    //日期比较测试       返回值：大于当前日期：1，等于当前日期：0，小于当前日期：-1
    public static int equalsDate(String date){
        //定义一个系统当前日期
        Date date1 = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String currentTime = format.format(date1);
        //传进来的日期数组
        int[] dataArr = StringToIntArr(date);
        //当前日期数组
        int[] current = StringToIntArr(currentTime);
        //进行比较
        if (dataArr[0]>current[0]) {
            System.out.println("大于当前日期");
            return 1;
        }
        if (dataArr[0]==current[0]) {
            //年份相等，判断月份
            if (dataArr[1]>current[1]) {
                System.out.println("大于当前日期");
                return 1;
            }else if(dataArr[1]==current[1]){
                //月份相等，判断天
                if (dataArr[2]>current[2]) {
                    System.out.println("大于当前日期");
                    return 1;
                }else if(dataArr[2]==current[2]){
                    System.out.println("等于当前日期");
                    return 0;
                }
                System.out.println("小于当前日期");
                return -1;
            }
        }
        //年份小于
        System.out.println("小于当前日期");
        return -1;
    }

    //传入String类型日期，返回int 数组
    public static int[] StringToIntArr(String date){
        String[] strings = date.split("-");
        int[] arr = new int[strings.length];
        for (int i = 0; i < strings.length; i++) {
            arr[i] = Integer.parseInt(strings[i]);
        }
        return arr;
    }

    /**
     * 比较是否超过了12:00  超过返回false
     */
    public static boolean equalsString12(String currentdate){
        int[] arr = {12,0,0};
        String[] strings = currentdate.split(":");
        int[] temp = new int[strings.length];
        for (int i = 0; i < strings.length; i++) {
            temp[i]=Integer.parseInt(strings[i]);
        }
        if (temp[0]<arr[0]) {
            return true;
        }
        return false;
    }

    /**
     * 比较是否超过了中间时间  超过返回false
     */
    public static boolean equalsStringMiddle(String currentdate,String middleTime){
        String[] strings = currentdate.split(":");
        String[] middleArr = middleTime.split(":");
        int[] temp = new int[strings.length];
        int[] middle = new int[middleArr.length];
        for (int i = 0; i < strings.length; i++) {
            temp[i]=Integer.parseInt(strings[i]);
        }
        for (int i = 0; i < middle.length; i++) {
            middle[i]=Integer.parseInt(middleArr[i]);
        }
        if (temp[0]<middle[0]) {
            return true;
        }
        return false;
    }

    /**
     * 进行时间转换
     */
    public static String transferDateTime(String date){
        String newDate = date.replace("年","-");
        newDate = newDate.replace("月","");
        return newDate;
    }

    /**
     * 进行时间转换
     */
    public static String transferDateTime2(String date){
        String newDate = date.replace("年","-");
        newDate = newDate.replace("月","-");
        newDate = newDate.replace("日","");
        return newDate;
    }

    /**
     * 比较下班时间  早退返回true
     */
    public static boolean equalsStringXiaban(String clockOutTime,String shouldXBTime){
        String[] strings = clockOutTime.split(":");
        int[] temp = new int[strings.length];
        int[] xiaTime = {16,50,0};
        for (int i = 0; i < strings.length; i++) {
            temp[i]=Integer.parseInt(strings[i]);
        }
        if (temp[0]<xiaTime[0]) {
            return true;
        }
        return false;
    }

    /**
     * 比较上班时间  迟到返回true
     */
    public static boolean equalsStringShangban(String clockInTime,String shouldSBTime){
        String[] strings = clockInTime.split(":");
        int[] temp = new int[strings.length];
        int[] shangTime ={8,50,0};
        for (int i = 0; i < strings.length; i++) {
            temp[i]=Integer.parseInt(strings[i]);
        }
        if (temp[0]>shangTime[0]) {
            return true;
        }
        if(temp[0]==shangTime[0]){
            if (temp[1]>shangTime[1]) {
                return true;
            }
            if (temp[1]==shangTime[1]) {
                if (temp[2]>shangTime[2]) {
                    return true;
                }
            }

        }
        return false;
    }

    /**
     * 产生一个动态数组，从当前月份到之前月份的动态更新数组
     */
    public static String[] getDynamicArray() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int mouth = calendar.get(Calendar.MONTH) + 1;
        String[] datas = new String[10];
        int j = 12;
        for (int i = 0; i < 10; i++) {
            if (mouth > 0) {
                if(mouth<10){
                    datas[i] = year + "年0" + mouth + "月";
                }else{
                    datas[i] = year + "年" + mouth + "月";
                }
                mouth--;
            } else {
                if(j<10){
                    datas[i] = year - 1 + "年0"+j+"月";
                }else{
                    datas[i] = year - 1 + "年" + j + "月";
                }
                j--;
            }
        }
        return datas;
    }


}
