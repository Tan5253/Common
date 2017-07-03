package com.like.common.view.chart.horizontalScrollLineChartView.core;

import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.PointF;

import com.like.common.util.MoneyFormatUtils;
import com.like.common.view.chart.horizontalScrollLineChartView.entity.Axis;
import com.like.common.view.chart.horizontalScrollLineChartView.entity.DataLine;
import com.like.common.view.chart.horizontalScrollLineChartView.entity.OtherLine;
import com.like.common.view.chart.horizontalScrollLineChartView.entity.OtherText;

/**
 * 画图工具类
 */
public class DrawUtils {

    public static void drawOtherTexts(OtherText[] otherTexts, Canvas canvas) {
        if (otherTexts == null || otherTexts.length <= 0) {
            return;
        }
        for (int i = 0; i < otherTexts.length; i++) {
            canvas.drawText(otherTexts[i].text,
                    otherTexts[i].baseCoordinatePoint.x,
                    otherTexts[i].baseCoordinatePoint.y,
                    otherTexts[i].textPaint);
        }
    }

    public static void drawOtherLines(OtherLine[] otherLines, Canvas canvas) {
        if (otherLines == null || otherLines.length <= 0) {
            return;
        }
        for (int i = 0; i < otherLines.length; i++) {
            if (otherLines[i].lineStyle == OtherLine.DASH_LINE) {// 画虚线
                Path path = new Path();
                path.moveTo(otherLines[i].startCoordinatePoint.x, otherLines[i].startCoordinatePoint.y);
                path.lineTo(otherLines[i].endCoordinatePoint.x, otherLines[i].endCoordinatePoint.y);
                PathEffect effects = new DashPathEffect(new float[]{5, 5, 5, 5}, 1);
                otherLines[i].linePaint.setPathEffect(effects);
                canvas.drawPath(path, otherLines[i].linePaint);
            } else if (otherLines[i].lineStyle == OtherLine.FULL_LINE) {// 画实线
                canvas.drawLine(otherLines[i].startCoordinatePoint.x,
                        otherLines[i].startCoordinatePoint.y,
                        otherLines[i].endCoordinatePoint.x,
                        otherLines[i].endCoordinatePoint.y,
                        otherLines[i].linePaint);
            }
        }
    }

    /**
     * 画x轴
     */
    public static void drawXAxis(Axis xAxis, Canvas canvas) {
        if (xAxis == null) {
            return;
        }
        // 绘制X轴
        canvas.drawLine(xAxis.startCoordinatePoint.x,
                xAxis.startCoordinatePoint.y,
                xAxis.endCoordinatePoint.x,
                xAxis.endCoordinatePoint.y,
                xAxis.linePaint);
        // 绘制X轴标签
        for (int i = 0; i < xAxis.labels.length; i++) {
            if (xAxis.scaleLine.longScaleLinePeriod == -1) {// -1表示没有长刻度
                // 画出所有标签
                canvas.drawText(xAxis.labels[i],
                        xAxis.scaleLine.coordinatePointList.get(i).x - xAxis.textPaint.measureText(xAxis.labels[i]) / 2,
                        xAxis.scaleLine.coordinatePointList.get(i).y + xAxis.textPaint.getTextSize(),
                        xAxis.textPaint);
            } else if (i + 1 == 1 || (i + 1) % (xAxis.scaleLine.longScaleLinePeriod) == 0) {
                // 有长刻度就只画长刻度标签
                canvas.drawText(xAxis.labels[i],
                        xAxis.scaleLine.coordinatePointList.get(i).x - xAxis.textPaint.measureText(xAxis.labels[i]) / 2,
                        xAxis.scaleLine.coordinatePointList.get(i).y + xAxis.textPaint.getTextSize(),
                        xAxis.textPaint);
            }
        }
        // 画刻度线
        for (int i = 1; i <= xAxis.scaleLine.coordinatePointList.size(); i++) {
            PointF point = xAxis.scaleLine.coordinatePointList.get(i - 1);
            float addLength = xAxis.scaleLine.shortScaleLineLength;// 默认只画短刻度线
            if (xAxis.scaleLine.longScaleLinePeriod != -1) {// -1表示没有长刻度
                if (i == 1 || i % (xAxis.scaleLine.longScaleLinePeriod) == 0) {
                    // 画长刻度
                    addLength = xAxis.scaleLine.longScaleLineLength;
                }
            }
            canvas.drawLine(point.x,
                    point.y,
                    point.x,
                    point.y - addLength,
                    xAxis.scaleLine.scaleLinePaint);
        }
    }

    /**
     * 画y轴
     */
    public static void drawYAxis(Axis yAxis, Canvas canvas) {

    }

    /**
     * 画数据线
     */
    public static void drawDataLines(DataLine[] dataLines, Canvas canvas) {
        if (dataLines == null && dataLines.length <= 0) {
            return;
        }
        for (int i = 0; i < dataLines.length; i++) {
            DataLine dataLine = dataLines[i];
            // 画线
            drawLine(dataLine, canvas, dataLine.linePaint);
            // 画节点
            int halfPointImageWidth = dataLine.pointImage.getWidth() / 2;
            int halfPointImageHeight = dataLine.pointImage.getHeight() / 2;
            switch (dataLine.showPointType) {
                case DataLine.SHOW_NONE_POINT:
                    break;
                case DataLine.SHOW_FIRST_AND_LAST_POINT:
                    PointF startPoint = dataLine.dataCoordinatePointList.get(0);
                    PointF endPoint = dataLine.dataCoordinatePointList.get(dataLine.dataCoordinatePointList.size() - 1);
                    // 绘制起点和终点的节点小icon
                    canvas.drawBitmap(dataLine.pointImage,
                            startPoint.x - halfPointImageWidth,
                            startPoint.y - halfPointImageHeight,
                            dataLine.linePaint);

                    canvas.drawBitmap(dataLine.pointImage,
                            endPoint.x - halfPointImageWidth,
                            endPoint.y - halfPointImageHeight,
                            dataLine.linePaint);

                    String startText = MoneyFormatUtils.formatTwoDecimals(dataLine.datas[0], MoneyFormatUtils.DECIMAL_TYPE_2_2);
                    canvas.drawText(startText,
                            startPoint.x - dataLine.textPaint.measureText(startText) / 2,
                            startPoint.y - dataLine.textPaint.getTextSize(),
                            dataLine.textPaint);

                    String endText = MoneyFormatUtils.formatTwoDecimals(dataLine.datas[dataLine.datas.length - 1], MoneyFormatUtils.DECIMAL_TYPE_2_2);
                    canvas.drawText(endText,
                            endPoint.x - dataLine.textPaint.measureText(endText) / 2,
                            endPoint.y - dataLine.textPaint.getTextSize(),
                            dataLine.textPaint);
                    break;
                case DataLine.SHOW_ALL_POINT:
                    for (int j = 0; j < dataLine.dataCoordinatePointList.size(); j++) {
                        canvas.drawBitmap(dataLine.pointImage,
                                dataLine.dataCoordinatePointList.get(j).x - halfPointImageWidth,
                                dataLine.dataCoordinatePointList.get(j).y - halfPointImageHeight,
                                dataLine.linePaint);

                        String text = MoneyFormatUtils.formatTwoDecimals(dataLine.datas[j], MoneyFormatUtils.DECIMAL_TYPE_2_2);
                        canvas.drawText(text,
                                dataLine.dataCoordinatePointList.get(j).x - dataLine.textPaint.measureText(text) / 2,
                                dataLine.dataCoordinatePointList.get(j).y - dataLine.textPaint.getTextSize(),
                                dataLine.textPaint);
                    }
                    break;
            }
        }
    }

    /**
     * 画折线
     */
    private static void drawLine(DataLine dataLine, Canvas canvas, Paint paint) {
        for (int i = 0; i < dataLine.dataCoordinatePointList.size() - 1; i++) {
            float startX = dataLine.dataCoordinatePointList.get(i).x;
            float startY = dataLine.dataCoordinatePointList.get(i).y;
            float endX = dataLine.dataCoordinatePointList.get(i + 1).x;
            float endY = dataLine.dataCoordinatePointList.get(i + 1).y;
            canvas.drawLine(startX, startY, endX, endY, paint);
        }
    }

    //    /**
//     * 画三次贝塞尔曲线
//     */
//    private static void drawCurve(float[] xCoordinates, float[] yCoordinates, Canvas canvas, Paint paint) {
//        PointF startp;
//        PointF endp;
//        for (int i = 0; i < totalDataNumber - 1; i++) {
//            startp = pointList.get(i);
//            endp = pointList.get(i + 1);
//            float wt = (startp.x + endp.x) / 2;
//            PointF p3 = new PointF();
//            PointF p4 = new PointF();
//            p3.y = startp.y;
//            p3.x = wt;
//            p4.y = endp.y;
//            p4.x = wt;
//
//            Path path = new Path();
//            path.moveTo(startp.x, startp.y);
//            path.cubicTo(p3.x, p3.y, p4.x, p4.y, endp.x, endp.y);
//            canvas.drawPath(path, paint);
//        }
//    }
}
