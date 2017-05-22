package com.henghao.hhworkpresent.utils;

import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.henghao.hhworkpresent.R;


/**
 * Created by Administrator on 2015/8/3.
 */
public class PopupWindowHelper {

    private final View popupView;

    private PopupWindow mPopupWindow;

    private static final int TYPE_WRAP_CONTENT = 0, TYPE_MATCH_PARENT = 1;

    public PopupWindowHelper(View view) {
        this.popupView = view;
    }

    public void showAsDropDown(View anchor) {
        initPopupWindow(TYPE_WRAP_CONTENT);
        this.mPopupWindow.showAsDropDown(anchor);
    }

    public void showAsDropDown(View anchor, int xoff, int yoff) {
        initPopupWindow(TYPE_WRAP_CONTENT);
        this.mPopupWindow.showAsDropDown(anchor, xoff, yoff);
    }

    public void showAtLocation(View parent, int gravity, int x, int y) {
        initPopupWindow(TYPE_WRAP_CONTENT);
        this.mPopupWindow.showAtLocation(parent, gravity, x, y);
    }

    public void dismiss() {
        if (this.mPopupWindow.isShowing()) {
            this.mPopupWindow.dismiss();
        }
    }

    public void showAsPopUp(View anchor) {
        showAsPopUp(anchor, 0, 0);
    }

    public void showAsPopUp(View anchor, int xoff, int yoff) {
        initPopupWindow(TYPE_WRAP_CONTENT);
        this.mPopupWindow.setAnimationStyle(R.style.AnimationUpPopup);
        this.popupView.measure(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        int height = this.popupView.getMeasuredHeight();
        int[] location = new int[2];
        anchor.getLocationInWindow(location);
        this.mPopupWindow.showAtLocation(anchor, Gravity.LEFT | Gravity.TOP, location[0] + xoff, location[1] - height
                + yoff);
    }

    public void showFromBottom(View anchor) {
        initPopupWindow(TYPE_MATCH_PARENT);
        this.mPopupWindow.setAnimationStyle(R.style.AnimationFromButtom);
        this.mPopupWindow.showAtLocation(anchor, Gravity.LEFT | Gravity.BOTTOM, 0, 0);
        setCancelable(true);
    }

    public void showFromTop(View anchor) {
        initPopupWindow(TYPE_MATCH_PARENT);
        this.mPopupWindow.setAnimationStyle(R.style.AnimationFromTop);
        this.mPopupWindow.showAtLocation(anchor, Gravity.LEFT | Gravity.TOP, 0, getStatusBarHeight());
    }

    /**
     * touch outside dismiss the popupwindow, default is ture
     * @param isCancelable
     */
    public void setCancelable(boolean isCancelable) {
        if (isCancelable) {
            this.mPopupWindow.setOutsideTouchable(true);
            this.mPopupWindow.setFocusable(true);
        }
        else {
            this.mPopupWindow.setOutsideTouchable(false);
            this.mPopupWindow.setFocusable(false);
        }
    }

    public void initPopupWindow(int type) {
        if (type == TYPE_WRAP_CONTENT) {
            this.mPopupWindow = new PopupWindow(this.popupView, ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        else if (type == TYPE_MATCH_PARENT) {
            this.mPopupWindow = new PopupWindow(this.popupView, ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        this.mPopupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setCancelable(true);
    }

    private int getStatusBarHeight() {
        return Math.round(25 * Resources.getSystem().getDisplayMetrics().density);
    }
}
