package com.like.common.view.horizontalScrollLineChatView.entity;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;

import com.like.common.util.DimensionUtils;

import java.util.Arrays;

/**
 * 需要绘制的坐标轴
 */
public class Axis {
    private static final float DEFAULT_LINE_WIDTH_DP = 1;
    private static final int DEFAULT_LINE_COLOR = Color.parseColor("#82b8da");
    private static final float DEFAULT_TEXT_SIZE_SP = 10;
    private static final int DEFAULT_TEXT_COLOR = Color.parseColor("#ffffff");
    public String[] labels;
    public ScaleLine scaleLine;
    /**
     * 轴线的起点坐标
     */
    public PointF startCoordinatePoint;
    /**
     * 轴线的终点坐标
     */
    public PointF endCoordinatePoint;
    /**
     * 文本画笔
     */
    public Paint textPaint;
    /**
     * 坐标轴画笔
     */
    public Paint linePaint;

    public Context context;

    public Axis(Context context, String[] labels, PointF startCoordinatePoint, PointF endCoordinatePoint, ScaleLine scaleLine) {
        this.context = context;
        this.labels = labels;
        this.startCoordinatePoint = startCoordinatePoint;
        this.endCoordinatePoint = endCoordinatePoint;
        this.scaleLine = scaleLine;
        init();
    }

    private void init() {
        linePaint = new Paint();
        linePaint.setAntiAlias(true);
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setStrokeWidth(DimensionUtils.dp2px(context, DEFAULT_LINE_WIDTH_DP));
        linePaint.setColor(DEFAULT_LINE_COLOR);

        textPaint = new Paint();
        textPaint.setAntiAlias(true);
        textPaint.setTextSize(DimensionUtils.sp2px(context, DEFAULT_TEXT_SIZE_SP));
        textPaint.setColor(DEFAULT_TEXT_COLOR);
    }

    /**
     * 单位px
     *
     * @param width
     */
    public void setLineWidth(int width) {
        linePaint.setStrokeWidth(width);
    }

    public void setLineColor(int color) {
        linePaint.setColor(color);
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

    @Override
    public String toString() {
        return "Axis{" +
                "labels=" + Arrays.toString(labels) +
                ", scaleLine=" + scaleLine +
                ", startCoordinatePoint=" + startCoordinatePoint +
                ", endCoordinatePoint=" + endCoordinatePoint +
                '}';
    }
}
