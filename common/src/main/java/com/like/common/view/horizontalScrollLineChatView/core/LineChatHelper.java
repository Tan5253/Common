package com.like.common.view.horizontalScrollLineChatView.core;

import com.like.common.view.horizontalScrollLineChatView.entity.Axis;
import com.like.common.view.horizontalScrollLineChatView.entity.DataLine;
import com.like.common.view.horizontalScrollLineChatView.entity.OtherLine;
import com.like.common.view.horizontalScrollLineChatView.entity.OtherText;

/**
 * 通过实现此接口来快速构建图表
 */
public interface LineChatHelper {
    LineChartConfig getLineChartConfig();

    Axis getxAxis();

    Axis getyAxis();

    DataLine[] getDataLines();

    OtherLine[] getOtherLines();

    OtherText[] getOtherTexts();
}
