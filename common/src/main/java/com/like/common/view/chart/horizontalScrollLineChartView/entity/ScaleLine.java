package com.like.common.view.chart.horizontalScrollLineChartView.entity;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;

import com.like.common.util.DimensionUtils;

import java.util.ArrayList;

/**
 * 需要绘制的刻度线
 */
public class ScaleLine {
    private static final float DEFAULT_LONG_SCALE_LINE_LENGTH_DP = 10;
    private static final float DEFAULT_SHORT_SCALE_LINE_LENGTH_DP = 4;
    private static final float DEFAULT_SCALE_LINE_WIDTH_DP = 1;
    private static final int DEFAULT_SCALE_LINE_COLOR = Color.parseColor("#7ccce7");
    /**
     * 刻度坐标点，LineChatUtils工具类中有计算方法
     */
    public ArrayList<PointF> coordinatePointList;
    /**
     * 长刻度线的长度
     */
    public float longScaleLineLength;
    /**
     * 短刻度线的长度
     */
    public float shortScaleLineLength;
    /**
     * 长刻度线出现的周期，注意：第一点必须显示长刻度（比如在5，10，15显示，则周期为5），设置为-1表示没有长刻度
     */
    public int longScaleLinePeriod;
    /**
     * 坐标刻度线画笔
     */
    public Paint scaleLinePaint;
    /**
     * 所有刻度线的坐标点集合
     */
    public ArrayList<ArrayList<PointF>> scaleLinePointList;
    public Context context;

    public ScaleLine(Context context, ArrayList<PointF> coordinatePointList, int longScaleLinePeriod) {
        this.context = context;
        this.coordinatePointList = coordinatePointList;
        this.longScaleLinePeriod = longScaleLinePeriod;
        init();
    }

    private void init() {
        this.longScaleLineLength = DimensionUtils.dp2px(context, DEFAULT_LONG_SCALE_LINE_LENGTH_DP);
        this.shortScaleLineLength = DimensionUtils.dp2px(context, DEFAULT_SHORT_SCALE_LINE_LENGTH_DP);

        scaleLinePaint = new Paint();
        scaleLinePaint.setAntiAlias(true);
        scaleLinePaint.setStyle(Paint.Style.STROKE);
        scaleLinePaint.setStrokeWidth(DimensionUtils.dp2px(context, DEFAULT_SCALE_LINE_WIDTH_DP));
        scaleLinePaint.setColor(DEFAULT_SCALE_LINE_COLOR);
    }

    /**
     * 设置长刻度线的长度，单位px
     *
     * @param longScaleLineLength
     */
    public void setLongScaleLineLength(float longScaleLineLength) {
        this.longScaleLineLength = longScaleLineLength;
    }

    /**
     * 设置短刻度线的长度，单位px
     *
     * @param shortScaleLineLength
     */
    public void setShortScaleLineLength(float shortScaleLineLength) {
        this.shortScaleLineLength = shortScaleLineLength;
    }

    /**
     * 单位px
     *
     * @param width
     */
    public void setScaleLineWidth(int width) {
        scaleLinePaint.setStrokeWidth(width);
    }

    public void setScaleLineColor(int color) {
        scaleLinePaint.setColor(color);
    }

    @Override
    public String toString() {
        return "ScaleLine{" +
                "coordinatePointList=" + coordinatePointList +
                ", longScaleLineLength=" + longScaleLineLength +
                ", shortScaleLineLength=" + shortScaleLineLength +
                ", longScaleLinePeriod=" + longScaleLinePeriod +
                ", scaleLinePointList=" + scaleLinePointList +
                '}';
    }
}
