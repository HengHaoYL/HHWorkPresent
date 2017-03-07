package com.henghao.hhworkpresent.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.henghao.hhworkpresent.R;

/**
 * Created by bryanrady on 2017/3/1.'
 *
 *  图片文字组合控件
 */

public class ImageTextButton extends LinearLayout {

    private TextView itemText;

    private ImageView itemImage;

    public ImageTextButton(Context context){
        super(context,null);
    }

    public ImageTextButton(Context context, AttributeSet attrs){
        super(context,attrs);
        LayoutInflater.from(context).inflate(R.layout.layout_imagetext_button, this, true);
        itemText = (TextView) findViewById(R.id.item_textview);
        itemImage = (ImageView) findViewById(R.id.item_imageview);

    }

    public void setItemTextResource(String text){
        itemText.setText(text);
    }

    public void setItemImageResource(int resId){
        itemImage.setImageResource(resId);
    }

}
