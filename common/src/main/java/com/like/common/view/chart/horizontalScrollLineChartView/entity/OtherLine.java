package com.like.common.view.chart.horizontalScrollLineChartView.entity;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;

import com.like.common.util.DimensionUtils;

/**
 * 需要绘制的其它辅助线
 */
public class OtherLine {
    /**
     * 实线
     */
    public static final int FULL_LINE = 0;
    /**
     * 虚线
     */
    public static final int DASH_LINE = 1;

    private static final float DEFAULT_LINE_WIDTH_DP = 1;
    private static final int DEFAULT_LINE_COLOR = Color.parseColor("#82b8da");

    public Context context;
    public PointF startCoordinatePoint;
    public PointF endCoordinatePoint;
    public Paint linePaint;
    /**
     * 线条类型，虚线或者实线
     */
    public int lineStyle = DASH_LINE;

    public OtherLine(Context context, PointF startCoordinatePoint, PointF endCoordinatePoint) {
        this.context = context;
        this.startCoordinatePoint = startCoordinatePoint;
        this.endCoordinatePoint = endCoordinatePoint;
        init();
    }

    private void init() {
        linePaint = new Paint();
        linePaint.setAntiAlias(true);
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setStrokeWidth(DimensionUtils.dp2px(context, DEFAULT_LINE_WIDTH_DP));
        linePaint.setColor(DEFAULT_LINE_COLOR);
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
     * 默认虚线
     *
     * @param lineStyle
     */
    public void setLineStyle(int lineStyle) {
        this.lineStyle = lineStyle;
    }

    @Override
    public String toString() {
        return "OtherLine{" +
                "startCoordinatePoint=" + startCoordinatePoint +
                ", endCoordinatePoint=" + endCoordinatePoint +
                '}';
    }
}
