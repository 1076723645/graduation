package com.example.finaldesign.presenter;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.example.finaldesign.R;

/**
 * Created by Administrator on 2017/11/7.
 */

public class CircleBar extends View {

    private static final String TAG = "CircleBar";

    private RectF mColorWheelRectangle = new RectF();//圆圈的矩形范围
    private Paint mDefaultWheelPaint;////绘制底部灰色圆圈的画笔
    private Paint mColorWheelPaint;//绘制蓝色扇形的画笔
    private Paint textPaint;//中间数值文字的画笔
    private Paint textDesPaint;//描述文字的画笔
    private Paint textshowPaint;
    private float mColorWheelRadius;
    private float circleStrokeWidth;//圆圈的线条粗细
    private float pressExtraStrokeWidth;
    private int mTextColor = getResources().getColor(R.color.limegreen);//默认数字颜色
    private int mWheelColor = getResources().getColor(R.color.limegreen);//默认圆环颜色

    private String mText;
    private String mTextDes;//文字的描述
    private String mTextshow;
    private int mTextDesSize;//描述文字的大小
    private float mSweepAngle;//扇形弧度
    private int mTextSize;//文字大小
    private int mDistance;// 上下文字的距离
    private int mTextshowSize;

    public CircleBar(Context context) {
        super(context);
        init(null, 0);
    }

    public CircleBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public CircleBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }


    private void init(AttributeSet attrs, int defStyle) {
        //初始化一些值
        circleStrokeWidth = dip2px(getContext(), 2);
        pressExtraStrokeWidth = dip2px(getContext(), 2);
        mTextSize = dip2px(getContext(), 10);
        mTextDesSize = dip2px(getContext(), 30);
        mTextshowSize = dip2px(getContext(), 12);
        mDistance = dip2px(getContext(), 12);//文字距离
        //外圆环的画笔
        mColorWheelPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mColorWheelPaint.setColor(mWheelColor);
        mColorWheelPaint.setStyle(Paint.Style.STROKE);
        mColorWheelPaint.setStrokeWidth(circleStrokeWidth);//圆圈的线条粗细
        mColorWheelPaint.setStrokeCap(Paint.Cap.ROUND);//开启显示边缘为圆形
        //默认圆的画笔
        mDefaultWheelPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mDefaultWheelPaint.setColor(getResources().getColor(R.color.lightgrey));
        mDefaultWheelPaint.setStyle(Paint.Style.STROKE);
        mDefaultWheelPaint.setStrokeWidth(circleStrokeWidth);//圆圈的线条粗细
        mDefaultWheelPaint.setStrokeCap(Paint.Cap.ROUND);//开启显示边缘为圆形
        //描述文字的画笔
        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.LINEAR_TEXT_FLAG);
        textPaint.setColor(getResources().getColor(R.color.grey));
        textPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        textPaint.setTextAlign(Paint.Align.LEFT);
        textPaint.setTextSize(mTextSize);
        //数值的画笔
        textDesPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.LINEAR_TEXT_FLAG);
        textDesPaint.setColor(mTextColor);
        textDesPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        textDesPaint.setTextSize(mTextDesSize);
        textDesPaint.setTextAlign(Paint.Align.LEFT);
        //
        textshowPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.LINEAR_TEXT_FLAG);
        textshowPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        textshowPaint.setColor(getResources().getColor(R.color.grey));
        textshowPaint.setTextSize(mTextshowSize);
        textshowPaint.setTextAlign(Paint.Align.LEFT);

        mTextshow = "空气质量指数";
        mText = "AQI";
        mTextDes = "78";
        mSweepAngle = 300;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawArc(mColorWheelRectangle, -225, 270, false, mDefaultWheelPaint);//画外接的圆环
        canvas.drawArc(mColorWheelRectangle, -225, Integer.parseInt(mTextDes)/mSweepAngle*270, false, mColorWheelPaint);//画圆环
        Rect bounds = new Rect();

        textPaint.getTextBounds(mText, 0, mText.length(), bounds);
        textDesPaint.getTextBounds(mTextDes, 0, mTextDes.length(), bounds);

        // drawText各个属性的意思(文字,x坐标,y坐标,画笔)
        canvas.drawText(mText,
                (mColorWheelRectangle.centerX())
                        - (textPaint.measureText(mText) / 2),
                mColorWheelRectangle.centerY() + bounds.height() / 4 + mDistance,
                textPaint);
        canvas.drawText(mTextDes,
                (mColorWheelRectangle.centerX())
                        - (textDesPaint.measureText(mTextDes) / 2),
                mColorWheelRectangle.centerY() + bounds.height() / 4,
                textDesPaint);
        canvas.drawText(mTextshow,
                (mColorWheelRectangle.centerX())
                        - (textshowPaint.measureText(mTextshow) / 2),
                mColorWheelRectangle.centerY() + mColorWheelRadius / 2,
                textshowPaint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int height = getDefaultSize(getSuggestedMinimumHeight(),
                heightMeasureSpec);
        int width = getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec);
        int min = Math.min(width, height);
        setMeasuredDimension(min, min);
        mColorWheelRadius = min - circleStrokeWidth - pressExtraStrokeWidth;

        mColorWheelRectangle.set(circleStrokeWidth + pressExtraStrokeWidth, circleStrokeWidth + pressExtraStrokeWidth,
                mColorWheelRadius, mColorWheelRadius);
    }

    public void setText(String text) {
        mText = text;
        postInvalidate();

    }

    public void setDesText(String text) {
        mTextDes = text;
        postInvalidate();
    }

    public void setShowText(String text) {
        mTextshow = text;
    }

    public void setTextColor(int color) {
        mTextColor = color;
        textDesPaint.setColor(mTextColor);
        postInvalidate();
    }

    public void setSweepAngle(float sweepAngle) {
        mSweepAngle = sweepAngle;
    }

    public void setWheelColor(int color) {
        this.mColorWheelPaint.setColor(color);
    }

    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }
}