package com.like.common.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.widget.GridView;

/**
 * 配合布局来自动调整间距的gridview，并绘制垂直分割线
 */
public class MyVerticalDividerGridView extends GridView {

    public MyVerticalDividerGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyVerticalDividerGridView(Context context) {
        super(context);
    }

    public MyVerticalDividerGridView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        int columns = getNumColumns();
        int childCount = getChildCount();
        Paint localPaint = new Paint();
        localPaint.setStyle(Paint.Style.STROKE);
        localPaint.setColor(Color.parseColor("#44000000"));
        for (int i = 0; i < childCount; i++) {
            View cellView = getChildAt(i);
            if ((i + 1) % columns != 0) {// 最后一列不要分割线
                canvas.drawLine(cellView.getRight(), cellView.getTop(), cellView.getRight(), cellView.getBottom(), localPaint);
            }
        }
    }
}
