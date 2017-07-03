package com.like.common.view.chart.horizontalScrollLineChartView.core;

import com.like.common.view.chart.horizontalScrollLineChartView.entity.Axis;
import com.like.common.view.chart.horizontalScrollLineChartView.entity.DataLine;
import com.like.common.view.chart.horizontalScrollLineChartView.entity.OtherLine;
import com.like.common.view.chart.horizontalScrollLineChartView.entity.OtherText;

/**
 * 通过实现此接口来快速构建图表
 */
public interface LineChartHelper {
    LineChartConfig getLineChartConfig();

    Axis getxAxis();

    Axis getyAxis();

    DataLine[] getDataLines();

    OtherLine[] getOtherLines();

    OtherText[] getOtherTexts();
}
