package com.like.common.view.chart.horizontalScrollLineChartView.core;

import android.graphics.PointF;
import android.util.Log;

import java.util.ArrayList;

/**
 * 折线图配置，必须调用init()方法
 */
public class LineChartConfig {
    /**
     * y轴数据数组
     */
    public float[] datas;
    /**
     * x轴标签数组
     */
    public String[] xLabels;
    /**
     * 总的数据数量
     */
    public int totalNumber;
    /**
     * 一屏显示的数据数量
     */
    public int showNumber;
    /**
     * 最大的数值
     */
    public float maxData;
    /**
     * 最小的数值
     */
    public float minData;
    /**
     * 坐标系距离顶部的距离
     */
    public float coordinateMarginTop;
    /**
     * 坐标系距离左边的距离
     */
    public float coordinateMarginLeft;
    /**
     * 坐标系距离右边的距离
     */
    public float coordinateMarginRight;
    /**
     * 坐标系距离下方的距离
     */
    public float coordinateMarginBottom;

    // --------------------需要调用init()方法计算的字段--------------------
    // ---基础字段---
    /**
     * 图表宽度（包括左边和右边的留空） --需要调用init()方法计算--
     */
    public float mTotalWidth;
    /**
     * 图表高度（包括上方和下方的留空） --需要调用init()方法计算--
     */
    public float mTotalHeight;
    /**
     * X轴数据的长度（只是数据的宽度，不包括左边和右边的留空） --需要调用init()方法计算--
     */
    public float mTotalDataWidth;
    /**
     * Y轴数据的长度（只是数据的高度，不包括上方和下方的留空） --需要调用init()方法计算--
     */
    public float mTotalDataHeight;
    /**
     * x坐标的单元间距 --需要调用init()方法计算--
     */
    public float spacingOfX;
    /**
     * y坐标的单元间距 --需要调用init()方法计算--
     */
    public float spacingOfY;
    /**
     * y轴标签集合 --需要调用init()方法计算--
     */
    public String[] yLabels;
    // ---坐标相关的字段---
    /**
     * 最大值的坐标 --需要调用init()方法计算--
     */
    public float maxDataYCoordinate;
    /**
     * 最小值的坐标 --需要调用init()方法计算--
     */
    public float minDataYCoordinate;
    /**
     * datas对应的数据坐标集合 --需要调用init()方法计算--
     */
    public ArrayList<PointF> dataCoordinatePointList;

    public LineChartConfig(float[] datas,
                           String[] xLabels,
                           float coordinateMarginLeft,
                           float coordinateMarginRight,
                           float coordinateMarginTop,
                           float coordinateMarginBottom) {
        if (datas == null || datas.length <= 0) {
            Log.e("TAG", "LineChartConfig 构造函数 datas参数错误");
            return;
        }
        if (xLabels == null || xLabels.length <= 0) {
            Log.e("TAG", "LineChartConfig 构造函数 xLabels参数错误");
            return;
        }
        if (datas.length != xLabels.length) {
            Log.e("TAG", "LineChartConfig 构造函数 datas和xLabels长度必须相等");
            return;
        }

//        Log.i("TAG", "LineChartConfig 构造函数");

        this.datas = datas;
        this.xLabels = xLabels;
        this.coordinateMarginLeft = coordinateMarginLeft;
        this.coordinateMarginRight = coordinateMarginRight;
        this.coordinateMarginTop = coordinateMarginTop;
        this.coordinateMarginBottom = coordinateMarginBottom;

        totalNumber = datas.length;

        calcMaxAndMinData(datas);
    }

    /**
     * 必须初始化才能使用
     *
     * @param showNumber 一屏显示的数据数量
     * @param showWidth  图表的显示宽度，因为可能在HorizontalScrollView中，不能onMeasure出自己的宽度
     * @param showHeight
     */
    public void init(int showNumber, float showWidth, float showHeight) {
//        Log.i("TAG", "LineChartConfig init()");
        this.showNumber = showNumber;
        if (showNumber == 1 && totalNumber == 1) {// 当只有一个数据的时候
            spacingOfX = showWidth - coordinateMarginLeft - coordinateMarginRight;
            mTotalDataWidth = spacingOfX;
        } else if (showNumber < totalNumber) {// 没有显示所有数据时
            spacingOfX = (showWidth - coordinateMarginLeft) / (showNumber - 1);
            mTotalDataWidth = (totalNumber - 1) * spacingOfX;
        } else {// 显示所有数据
            spacingOfX = (showWidth - coordinateMarginLeft - coordinateMarginRight) / (showNumber - 1);
            mTotalDataWidth = (totalNumber - 1) * spacingOfX;
        }
        mTotalWidth = mTotalDataWidth + coordinateMarginLeft + coordinateMarginRight;
        mTotalHeight = showHeight;
        mTotalDataHeight = mTotalHeight - coordinateMarginTop - coordinateMarginBottom;
        spacingOfY = mTotalDataHeight / maxData;
        yLabels = getLabels(datas);

        calcCoordinate();
    }

    /**
     * 计算画图需要的坐标
     */
    private void calcCoordinate() {
        maxDataYCoordinate = data2Coordinate(maxData);
        minDataYCoordinate = data2Coordinate(minData);
        dataCoordinatePointList = datas2Coordinates(xLabels, datas);
    }

    /**
     * 计算数据集合中的最大最小值
     *
     * @param datas
     * @return
     */
    private void calcMaxAndMinData(float[] datas) {
        minData = Float.MAX_VALUE;
        maxData = Float.MIN_VALUE;
        for (int i = 0; i < datas.length; i++) {
            if (datas[i] < minData) {
                minData = datas[i];
            }
            if (datas[i] > maxData) {
                maxData = datas[i];
            }
        }
    }

    /**
     * 获取y轴标签
     *
     * @param datas
     * @return
     */
    private String[] getLabels(float[] datas) {
        String[] yLabels = new String[datas.length];


        return yLabels;
    }

    /**
     * datas对应的数据转换成坐标集合
     *
     * @param xLabels
     * @return
     */
    private ArrayList<PointF> datas2Coordinates(String[] xLabels, float[] datas) {
        ArrayList<PointF> list = new ArrayList<>();
        if (xLabels.length == 1) {// 如果只有一条数据，就显示在图表中间
            PointF p = new PointF();
            p.x = spacingOfX / 2 + coordinateMarginLeft;
            p.y = data2Coordinate(datas[0]);
            list.add(p);
        } else {
            for (int i = 0; i < xLabels.length; i++) {
                PointF p = new PointF();
                p.x = i * spacingOfX + coordinateMarginLeft;
                p.y = data2Coordinate(datas[i]);
                list.add(p);
            }
        }
        return list;
    }

    /**
     * y轴数值转换成y坐标值
     *
     * @param data
     * @return
     */
    private float data2Coordinate(float data) {
        return mTotalDataHeight + coordinateMarginTop - data * spacingOfY;
    }

    /**
     * 获取x轴线刻度的坐标集合
     */
    public ArrayList<PointF> getXCoordinates() {
        ArrayList<PointF> list = new ArrayList<>();
        float yCoordinate = coordinateMarginTop + mTotalDataHeight;
        for (int i = 0; i < xLabels.length; i++) {
            PointF p = new PointF();
            p.x = dataCoordinatePointList.get(i).x;
            p.y = yCoordinate;
            list.add(p);
        }
        return list;
    }

    /**
     * 获取y轴线刻度的坐标集合
     */
    public ArrayList<PointF> getYCoordinates() {
        ArrayList<PointF> list = new ArrayList<>();
        float xCoordinate = coordinateMarginLeft;
        for (int i = 0; i < datas.length; i++) {
            PointF p = new PointF();
            p.x = xCoordinate;
            p.y = dataCoordinatePointList.get(i).y;
            list.add(p);
        }
        return list;
    }

}
