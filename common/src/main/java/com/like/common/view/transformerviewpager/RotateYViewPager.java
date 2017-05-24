package com.like.common.view.transformerviewpager;


import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;

import com.like.common.util.DimensionUtils;
import com.like.common.util.PhoneUtils;

public class RotateYViewPager extends ViewPager {
    private int mRealWidth;
    private int mRealHeight;

    public RotateYViewPager(Context context) {
        this(context, null);
    }

    public RotateYViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        // 设置缓存，这样左右拖动即可看见后面的Fragment。
        this.setOffscreenPageLimit(3);

        mRealWidth = PhoneUtils.getInstance(context).mPhoneStatus.screenWidth - DimensionUtils.dp2px(context, 70);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (mRealHeight == 0) {
            // 根据内容视图自动调整高度
            int height = 0;
            float heightWidthRatio = 0f;
            // 下面遍历所有child的高度
            for (int i = 0; i < getChildCount(); i++) {
                View child = getChildAt(i);
                child.measure(0, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
                int h = child.getMeasuredHeight();
                if (h > height) // 采用最大的view的高度。
                    height = h;
                int w = child.getMeasuredWidth();
                if (w > 0) {
                    float hwRatio = ((float) h) / w;
                    if (hwRatio > heightWidthRatio) // 采用最大的高宽比率。
                        heightWidthRatio = hwRatio;
                }
            }
            mRealHeight = (int) (mRealWidth * heightWidthRatio);
        }
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(mRealHeight, MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

}
