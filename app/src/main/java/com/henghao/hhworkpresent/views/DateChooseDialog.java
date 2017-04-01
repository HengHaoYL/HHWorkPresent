package com.henghao.hhworkpresent.views;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.henghao.hhworkpresent.R;
import com.henghao.hhworkpresent.listener.OnDateChooseDialogListener;
import com.henghao.hhworkpresent.utils.Utility;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 通过自定义Dialog实现日期选择控件，这里也可以控制日期的可选范围
 * Created by bryanrady on 2017/3/20.
 */

public class DateChooseDialog extends Dialog {

    public DateChooseDialog(Context context) {
        super(context);
    }

    public DateChooseDialog(Context context, int theme) {
        super(context, theme);
    }

    @SuppressLint("NewApi")
    public static class Builder{
        private Context context;
        private String title;
        private int yearsSeletion;
        private int monthSeletion;
        private int daySeletion;
        private List<String> yearsItems;
        private List<String> monthItems;
        private List<String> dayItems;
        private TextView textView;
        private TextView tvTime;
        private WheelView wheelView1, wheelView2, wheelView3;
        private Button positiveButton, negativeButton;
        private DialogInterface.OnClickListener positiveButtonClickListener;
        private DialogInterface.OnClickListener negativeButtonClickListener;
        private String years;
        private String month;
        private String day;

        public Builder(Context context) {
            this.context = context;
            yearsItems = new ArrayList<String>();
            monthItems = new ArrayList<String>();
            dayItems = new ArrayList<String>();

            String date = Utility.dateFormatDay(new Date());
            years = date.split("-")[0];
            month = date.split("-")[1];
            day = date.split("-")[2];

            for(int i = 2000; i < 2030; i++){
                yearsItems.add(i + "");
            }
            for(int i = 0; i < 12; i++){
                if(1 + i < 10){
                    monthItems.add("0" + (1 + i));
                }else{
                    monthItems.add(1 + i + "");
                }
            }
            for(int i = 0; i < Integer.valueOf(Utility.getMonthMaxDay(Utility.dateFormat(new Date()))); i++){
                if(1 + i < 10){
                    dayItems.add("0" + (1 + i));
                }else{
                    dayItems.add(1 + i + "");
                }
            }
            yearsSeletion = Integer.valueOf(date.split("-")[0]) - 2000;
            monthSeletion = Integer.valueOf(date.split("-")[1]) - 1;
            daySeletion = Integer.valueOf(date.split("-")[2]) - 1;
        }

        /**
         * Set the Dialog title from resource
         *
         * @param title
         * @return
         */
         public Builder setTitle(int title) {
            this.title = (String) context.getText(title);
            return this;
         }

        /**
         * Set the Dialog title from String
         *
         * @param title
         * @return
         */

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setYearsItems(List<String> list){
            this.yearsItems = list;
            return this;
        }

        public Builder setMonthItems(List<String> list){
            this.monthItems = list;
            return this;
        }

        public Builder setTextView(TextView tv) {
            this.textView = tv;
            return this;
        }

        /**
         * Set the positive button resource and it's listener
         *
         * @param positiveButtonText
         * @return
         */
        public Builder setPositiveButton(int positiveButtonText, DialogInterface.OnClickListener listener) {
            this.positiveButtonClickListener = listener;
            return this;
        }

        public Builder setPositiveButton(String positiveButtonText, DialogInterface.OnClickListener listener) {
            this.positiveButtonClickListener = listener;
            return this;
        }

        public Builder setNegativeButton(int negativeButtonText, DialogInterface.OnClickListener listener) {
            this.negativeButtonClickListener = listener;
            return this;
        }

        public Builder setNegativeButton(String negativeButtonText, DialogInterface.OnClickListener listener) {
            this.negativeButtonClickListener = listener;
            return this;
        }

        private OnDateChooseDialogListener dateChooseDialogListener;

        public void setOnDateChooseDialogListener(OnDateChooseDialogListener dateChooseDialogListener){
                this.dateChooseDialogListener = dateChooseDialogListener;
        }

        public DateChooseDialog create() {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            // instantiate the dialog with the custom Theme
            final DateChooseDialog dialog = new DateChooseDialog(context, R.style.Dialog);
            View layout = inflater.inflate(R.layout.date_choose_dialog, null);
            tvTime = (TextView) layout.findViewById(R.id.title);
            wheelView1 = (WheelView)layout.findViewById(R.id.wheelview1);
            wheelView2 = (WheelView)layout.findViewById(R.id.wheelview2);
            wheelView3 = (WheelView)layout.findViewById(R.id.wheelview3);
            positiveButton = (Button) layout.findViewById(R.id.positiveButton);
            negativeButton = (Button) layout.findViewById(R.id.negativeButton);

            dialog.addContentView(layout, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            // set the dialog title
            tvTime.setText(title);
            // set the confirm button
            positiveButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    //           textView.setText(years + "-" + month + "-" + day);
                    Log.d("wang","dateChooseDialogListener==" + dateChooseDialogListener);
                    if(dateChooseDialogListener!=null){
                        dateChooseDialogListener.onDateSetting(years + "-" + month + "-" + day);
                    }
                    positiveButtonClickListener.onClick(dialog, DialogInterface.BUTTON_POSITIVE);
                }
            });

            negativeButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    negativeButtonClickListener.onClick(dialog, DialogInterface.BUTTON_NEGATIVE);
                }
            });

            // set the content message
            wheelView1.setOffset(1);
            wheelView1.setItems(yearsItems);
            wheelView1.setSeletion(yearsSeletion);
            wheelView1.setOnWheelViewListener(new WheelView.OnWheelViewListener(){
                public void onSelected(int selectedIndex, String item) {
                    years = item;
                    wheelView2.setItems(monthItems);
                    ArrayList<String> items = new ArrayList<String>();
                    for(int i = 0; i < Integer.valueOf(Utility.getMonthMaxDay(years + "-" + month)); i++){
                        if(1 + i < 10){
                            items.add("0" + (1 + i));
                        }else{
                            items.add(1 + i + "");
                        }
                    }
                    wheelView3.setItems(items);
                }
            });

            wheelView2.setOffset(1);
            wheelView2.setItems(monthItems);
            wheelView2.setSeletion(monthSeletion);
            wheelView2.setOnWheelViewListener(new WheelView.OnWheelViewListener(){
                public void onSelected(int selectedIndex, String item) {
                    month = item;
                    int maxDay =  Integer.valueOf(Utility.getMonthMaxDay(years + "-" + item));
                    ArrayList<String> items = new ArrayList<String>();
                    for(int i = 0; i < maxDay; i++){
                        if(1 + i < 10){
                            items.add("0" + (1 + i));
                        }else{
                            items.add(1 + i + "");
                        }
                    }
                    wheelView3.setItems(items);
                    if(Integer.valueOf(day) > maxDay){
                        day = maxDay + "";
                        wheelView3.setSeletion(maxDay);
                    }
                }
            });

            wheelView3.setOffset(1);
            wheelView3.setItems(dayItems);
            wheelView3.setSeletion(daySeletion);
            wheelView3.setOnWheelViewListener(new WheelView.OnWheelViewListener(){
                public void onSelected(int selectedIndex, String item) {
                    day = item;
                }
            });

            dialog.setContentView(layout);
            return dialog;
         }
    }
}
