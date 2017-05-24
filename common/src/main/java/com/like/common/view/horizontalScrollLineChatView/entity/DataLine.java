package com.like.common.view.horizontalScrollLineChatView.entity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;

import com.like.common.R;
import com.like.common.util.DimensionUtils;

import java.util.ArrayList;

/**
 * 需要绘制的数据线
 */
public class DataLine {
    /**
     * 不显示折点
     */
    public static final int SHOW_NONE_POINT = 0;
    /**
     * 显示第一个和最后一个折点的图片及数据
     */
    public static final int SHOW_FIRST_AND_LAST_POINT = 1;
    /**
     * 显示所有折点的图片及数据
     */
    public static final int SHOW_ALL_POINT = 2;

    private static final float DEFAULT_LINE_WIDTH_DP = 2.5f;
    private static final int DEFAULT_LINE_COLOR = Color.parseColor("#46d3ff");
    private static float DEFAULT_TEXT_SIZE_SP = 12;
    private static final int DEFAULT_TEXT_COLOR = Color.parseColor("#ffffff");
    private static final int DEFAULT_POINT_IMAGE_RES_ID = R.drawable.linechatview_node_icon;

    public int showPointType = SHOW_NONE_POINT;
    public ArrayList<PointF> dataCoordinatePointList;
    public float[] datas;
    public Paint linePaint;
    public Paint textPaint;
    /**
     * 交点小图标
     */
    public Bitmap pointImage;
    public Context context;

    public DataLine(Context context, ArrayList<PointF> dataCoordinatePointList, float[] datas) {
        this.context = context;
        this.dataCoordinatePointList = dataCoordinatePointList;
        this.datas = datas;
        init();
    }

    private void init() {
        linePaint = new Paint();
        linePaint.setAntiAlias(true);
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setStrokeWidth(DimensionUtils.dp2px(context, DEFAULT_LINE_WIDTH_DP));
        linePaint.setColor(DEFAULT_LINE_COLOR);
        linePaint.setStrokeJoin(Paint.Join.ROUND);// 拟合处变成了更加平滑

        textPaint = new Paint();
        textPaint.setAntiAlias(true);
        textPaint.setTextSize(DimensionUtils.sp2px(context, DEFAULT_TEXT_SIZE_SP));
        textPaint.setColor(DEFAULT_TEXT_COLOR);

        pointImage = BitmapFactory.decodeResource(context.getResources(), DEFAULT_POINT_IMAGE_RES_ID);
    }

    public void setPointImage(Bitmap pointImage) {
        this.pointImage = pointImage;
    }

    public void setPointImage(int resId) {
        this.pointImage = BitmapFactory.decodeResource(context.getResources(), resId);
    }

    public void setShowPointType(int showPointType) {
        this.showPointType = showPointType;
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
        return "DataLine{" +
                "showPointType=" + showPointType +
                ", dataCoordinatePointList=" + dataCoordinatePointList +
                '}';
    }
}
