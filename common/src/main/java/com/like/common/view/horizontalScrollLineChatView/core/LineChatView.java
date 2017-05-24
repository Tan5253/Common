package com.like.common.view.horizontalScrollLineChatView.core;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.like.common.view.horizontalScrollLineChatView.entity.Axis;
import com.like.common.view.horizontalScrollLineChatView.entity.DataLine;
import com.like.common.view.horizontalScrollLineChatView.entity.OtherLine;
import com.like.common.view.horizontalScrollLineChatView.entity.OtherText;

/**
 * 折线图，必须调用init()方法
 */
public class LineChatView extends View {
    private LineChartConfig lineChartConfig;
    private Axis xAxis;
    private Axis yAxis;
    private DataLine[] dataLines;
    private OtherLine[] otherLines;
    private OtherText[] otherTexts;

    public LineChatView(Context context) {
        super(context);
    }

    public LineChatView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * 必须初始化才能使用
     *
     * @param lineChatHelper
     * @param showNumber     一屏显示的数据数量
     * @param showWidth      图表视图显示在屏幕上面的宽度
     * @param showHeight     图表视图显示在屏幕上面的高度
     */
    public void init(LineChatHelper lineChatHelper, int showNumber, float showWidth, float showHeight) {
//        Log.e("TAG", "LineChatView init()");
        if (lineChatHelper == null) {
            Log.e("TAG", "LineChatView init() lineChatHelper参数错误");
            return;
        }
        lineChartConfig = lineChatHelper.getLineChartConfig();
        lineChartConfig.init(showNumber, showWidth, showHeight);
        xAxis = lineChatHelper.getxAxis();
        yAxis = lineChatHelper.getyAxis();
        dataLines = lineChatHelper.getDataLines();
        otherLines = lineChatHelper.getOtherLines();
        otherTexts = lineChatHelper.getOtherTexts();
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
