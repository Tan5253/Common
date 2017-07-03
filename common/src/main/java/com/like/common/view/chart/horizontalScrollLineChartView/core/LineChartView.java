package com.like.common.view.chart.horizontalScrollLineChartView.core;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.like.common.view.chart.horizontalScrollLineChartView.entity.Axis;
import com.like.common.view.chart.horizontalScrollLineChartView.entity.DataLine;
import com.like.common.view.chart.horizontalScrollLineChartView.entity.OtherLine;
import com.like.common.view.chart.horizontalScrollLineChartView.entity.OtherText;

/**
 * 折线图，必须调用init()方法
 */
public class LineChartView extends View {
    private LineChartConfig lineChartConfig;
    private Axis xAxis;
    private Axis yAxis;
    private DataLine[] dataLines;
    private OtherLine[] otherLines;
    private OtherText[] otherTexts;

    public LineChartView(Context context) {
        super(context);
    }

    public LineChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * 必须初始化才能使用
     *
     * @param lineChartHelper
     * @param showNumber     一屏显示的数据数量
     * @param showWidth      图表视图显示在屏幕上面的宽度
     * @param showHeight     图表视图显示在屏幕上面的高度
     */
    public void init(LineChartHelper lineChartHelper, int showNumber, float showWidth, float showHeight) {
//        Log.e("TAG", "LineChatView init()");
        if (lineChartHelper == null) {
            Log.e("TAG", "LineChatView init() lineChatHelper参数错误");
            return;
        }
        lineChartConfig = lineChartHelper.getLineChartConfig();
        lineChartConfig.init(showNumber, showWidth, showHeight);
        xAxis = lineChartHelper.getxAxis();
        yAxis = lineChartHelper.getyAxis();
        dataLines = lineChartHelper.getDataLines();
        otherLines = lineChartHelper.getOtherLines();
        otherTexts = lineChartHelper.getOtherTexts();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        Log.e("TAG", "LineChatView onMeasure()");
        if (lineChartConfig == null) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        } else {
            setMeasuredDimension((int) (lineChartConfig.mTotalWidth), (int) (lineChartConfig.mTotalHeight));
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
//        Log.e("TAG", "LineChatView onDraw()");
        super.onDraw(canvas);
        DrawUtils.drawXAxis(xAxis, canvas);
        DrawUtils.drawYAxis(yAxis, canvas);
        DrawUtils.drawOtherLines(otherLines, canvas);
        DrawUtils.drawOtherTexts(otherTexts, canvas);
        DrawUtils.drawDataLines(dataLines, canvas);// 注意一定要最后画节点图标，因为它必须在最上面，覆盖节点
    }

}
