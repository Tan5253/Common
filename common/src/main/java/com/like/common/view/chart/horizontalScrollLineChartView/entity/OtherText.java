package com.like.common.view.chart.horizontalScrollLineChartView.entity;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;

import com.like.common.util.DimensionUtils;

/**
 * 需要绘制的其它文本
 */
public class OtherText {
    private static final float DEFAULT_TEXT_SIZE_SP = 12;
    private static final int DEFAULT_TEXT_COLOR = Color.parseColor("#ffffff");
    public Context context;
    public String text;
    public PointF baseCoordinatePoint;
    public Paint textPaint;

    public OtherText(Context context, String text, PointF baseCoordinatePoint) {
        this.context = context;
        this.text = text;
        init();
        // 修正位置，使文本居中显示
        baseCoordinatePoint.x -= textPaint.measureText(text) / 2;
        this.baseCoordinatePoint = baseCoordinatePoint;
    }

    private void init() {
        textPaint = new Paint();
        textPaint.setAntiAlias(true);
        textPaint.setTextSize(DimensionUtils.sp2px(context, DEFAULT_TEXT_SIZE_SP));
        textPaint.setColor(DEFAULT_TEXT_COLOR);
    }

    /**
     * 单位px
     *
     * @param textSize
     */
    public void setTextSize(int textSize) {
        textPaint.setTextSize(textSize);
    }

    public void setTextColor(int color) {
        textPaint.setColor(color);
    }

    public void setText(String text) {
        this.text = text;
    }
}
