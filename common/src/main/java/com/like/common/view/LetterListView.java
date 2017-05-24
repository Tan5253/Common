package com.like.common.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

public class LetterListView extends View {

    OnTouchingLetterChangedListener onTouchingLetterChangedListener;
    String[] letters = {"#", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L"
            , "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
    int choose = -1;
    Paint paint = new Paint();
    boolean showBkg = false;
    private int mScreenWidth;
    private Context context;

    @SuppressWarnings("deprecation")
    public LetterListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    @SuppressWarnings("deprecation")
    public LetterListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    @SuppressWarnings("deprecation")
    public LetterListView(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        if (isInEditMode()) {
            return;
        }
        this.context = context;
        Activity activity = (Activity) context;
        WindowManager manage = activity.getWindowManager();
        Display display = manage.getDefaultDisplay();
        mScreenWidth = display.getWidth();
    }

    /**
     * 字体之间的间隙
     */
    private float jianxi = -1.0f;
    /**
     * 字体
     */
    private int ziti = -1;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (showBkg) {
            canvas.drawColor(Color.parseColor("#40000000"));
        }

        int height = getHeight() - 6;// drawText()方法有点偏差，需要手动修正下。
        int width = getWidth();

        int baseZiTi = 20;
        if (ziti == -1) {
            // 分辨率是240x320
            if (mScreenWidth <= 240) {
                ziti = baseZiTi;
                // 分辨率是320x480
            } else if (mScreenWidth <= 320) {
                ziti = baseZiTi + 4;
                // 分辨率是480x800
            } else if (mScreenWidth <= 480) {
                ziti = baseZiTi + 8;
            } else if (mScreenWidth <= 720) {
                ziti = baseZiTi + 12;
            } else if (mScreenWidth <= 1080) {
                ziti = baseZiTi + 16;
            } else {
                ziti = baseZiTi + 20;
            }
        }
        if (jianxi == -1.0f) {
            jianxi = (float) (height - letters.length * ziti) / (letters.length + 1);

        }

        for (int i = 0; i < letters.length; i++) {

            if (showBkg) {
                //在有背景情况下，字体为白色
                paint.setColor(Color.WHITE);
            } else {
                //在没有背景情况下，字体为灰色
                paint.setColor(Color.parseColor("#939393"));
            }

            paint.setTypeface(Typeface.DEFAULT);
            paint.setAntiAlias(true);


            paint.setTextSize(ziti);
            if (i == choose) {
                paint.setColor(Color.parseColor("#3399ff"));
                paint.setFakeBoldText(true);
            }
            float xPos = width / 2 - paint.measureText(letters[i]) / 2;
//	       float yPos = singleHeight * i + singleHeight;
            float yPos = (jianxi + ziti) * (i + 1);
            canvas.drawText(letters[i], xPos, yPos, paint);
            paint.reset();
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        final int action = event.getAction();
        final float y = event.getY();
        final int oldChoose = choose;
        final OnTouchingLetterChangedListener listener = onTouchingLetterChangedListener;
        final int c = (int) (y / getHeight() * letters.length);

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                showBkg = true;
                if (oldChoose != c && listener != null) {
                    if (c >= 0 && c < letters.length) {
                        listener.onTouchingLetterChanged(letters[c]);
                        choose = c;
                        invalidate();
                    }
                }

                break;
            case MotionEvent.ACTION_MOVE:
                if (oldChoose != c && listener != null) {
                    if (c > 0 && c < letters.length) {
                        listener.onTouchingLetterChanged(letters[c]);
                        choose = c;
                        invalidate();
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                showBkg = false;
                choose = -1;
                invalidate();
                break;
        }
        return true;
    }

    public void setOnTouchingLetterChangedListener(
            OnTouchingLetterChangedListener onTouchingLetterChangedListener) {
        this.onTouchingLetterChangedListener = onTouchingLetterChangedListener;
    }

    public interface OnTouchingLetterChangedListener {
        public void onTouchingLetterChanged(String s);
    }

}