package com.henghao.hhworkpresent.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import static com.henghao.hhworkpresent.fragment.KaoqingTongjiFragment.mColor;
import static com.henghao.hhworkpresent.fragment.KaoqingTongjiFragment.str;
import static com.henghao.hhworkpresent.fragment.KaoqingTongjiFragment.strPercent;

/**
 * 圆环统计自定义View
 */
public class MyChatView extends View {

    //-------------必须给的数据相关-------------
   // private String[] str = new String[]{"未打卡", "迟到", "请假", "出差"};
    //分配比例大小，总比例大小为100,由于经过运算后最后会是99.55左右的数值，导致圆不能够重合，会留出点空白，所以这里的总比例大小我们用101
  //  private int[] strPercent = new int[]{10, 25, 18, 41, 2, 5};
    //圆的直径
    private float mRadius = 260;
    //圆的粗细
    private float mStrokeWidth = 40;
    //文字大小
    private int textSize = 40;
    //-------------画笔相关-------------
    //圆环的画笔
    private Paint cyclePaint;
    //文字的画笔
    private Paint textPaint;
    //标注的画笔
    private Paint labelPaint;
    //-------------颜色相关-------------
    //边框颜色和标注颜色
 //   private int[] mColor = new int[]{0xFFF06292, 0xFF9575CD, 0xFFE57373, 0xFF4FC3F7/*, 0xFFFFF176, 0xFF81C784*/};
    //文字颜色
    private int textColor = 0xFF000000;
    //-------------View相关-------------
    //View自身的宽和高
    private int mHeight;
    private int mWidth;

    public MyChatView(Context context) {
        super(context);
    }

    public MyChatView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyChatView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //移动画布到圆环的左上角
        canvas.translate(mWidth / 2 - mRadius / 2, mHeight / 2 - mRadius / 2);
        //初始化画笔
        initPaint();
        //画圆环
        drawCycle(canvas);
        //画文字和标注
        drawTextAndLabel(canvas);

    }

    /**
     * 初始化画笔
     */
    private void initPaint() {
        //边框画笔
        cyclePaint = new Paint();
        cyclePaint.setAntiAlias(true);
        cyclePaint.setStyle(Paint.Style.STROKE);
        cyclePaint.setStrokeWidth(mStrokeWidth);
        //文字画笔
        textPaint = new Paint();
        textPaint.setAntiAlias(true);
        textPaint.setColor(textColor);
        textPaint.setStyle(Paint.Style.STROKE);
        textPaint.setStrokeWidth(1);
        textPaint.setTextSize(textSize);
        //标注画笔
        labelPaint = new Paint();
        labelPaint.setAntiAlias(true);
        labelPaint.setStyle(Paint.Style.FILL);
        labelPaint.setStrokeWidth(2);
    }

    /**
     * 画圆环
     * @param canvas
     */
    private void drawCycle(Canvas canvas) {
        float startPercent = 0;
        float sweepPercent = 0;
        int total = 0;
        for (int j = 0; j < strPercent.size(); j++) {
            total += strPercent.get(j);
        }
        for (int i = 0; i < strPercent.size(); i++) {
            cyclePaint.setColor(mColor[i]);
            startPercent = sweepPercent + startPercent;
            //这里采用比例占总数total的百分比乘于360的来计算出占用的角度，使用先乘再除可以算出值
            sweepPercent = strPercent.get(i) * 360 / total;
            canvas.drawArc(new RectF(0, 0, mRadius, mRadius), startPercent, sweepPercent, false, cyclePaint);
        }
    }

    /**
     * 画文字和标注
     * @param canvas
     */
    private void drawTextAndLabel(Canvas canvas) {
        for (int i = 0; i < strPercent.size(); i++) {
            //文字离右边环边距为80，文字与文字之间的距离为80
            canvas.drawText(str[i], mRadius + 120, i * 80, textPaint);
            //画标注,标注离右边环边距为80,y轴则要减去半径（20）的一半才能对齐文字
            labelPaint.setColor(mColor[i]);
            canvas.drawCircle(mRadius + 80, i * 80 - 10, 20, labelPaint);
        }
    }

}