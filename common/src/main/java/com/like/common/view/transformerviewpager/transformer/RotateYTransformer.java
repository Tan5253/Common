package com.like.common.view.transformerviewpager.transformer;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.like.common.util.PhoneUtils;

public class RotateYTransformer implements ViewPager.PageTransformer {
    private static final float MIN_ALPHA = 0.5f;

    @Override
    public void transformPage(View view, float position) {
        float rotate = getRotate(view.getContext());
        view.setPivotY(view.getHeight() / 2);

        if (position < -1) { // [-Infinity,-1)
            // This page is way off-screen to the left.
            view.setRotationY(1 * rotate);
            view.setPivotX(0);
        } else if (position <= 1) { // [-1,1]
            // Modify the default slide transition to shrink the page as well
            view.setRotationY(-position * rotate);

            if (position < 0)// [0,-1]
            {
                view.setPivotX(0);

                float factor = MIN_ALPHA + (1 - MIN_ALPHA) * (1 + position);
                view.setAlpha(factor);
            } else// [1,0]
            {
                view.setPivotX(view.getWidth());

                float factor = MIN_ALPHA + (1 - MIN_ALPHA) * (1 - position);
                view.setAlpha(factor);
            }

            // Scale the page down (between MIN_SCALE and 1)
        } else { // (1,+Infinity]
            // This page is way off-screen to the right.
            view.setRotationY(-1 * rotate);
            view.setPivotX(view.getWidth());
        }
    }

    /**
     * 获取倾斜度
     *
     * @param context
     * @return
     */
    protected float getRotate(Context context) {
        float rotate = 1f;
        int densityDpi = PhoneUtils.getInstance(context).mPhoneStatus.densityDpi;
        if (densityDpi <= 240) {
            rotate = 5f;
        } else if (densityDpi <= 320) {
            rotate = 3f;
        }
        return rotate;
    }

}
