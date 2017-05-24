package com.like.common.util;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.ColorRes;
import android.support.annotation.DimenRes;
import android.view.View;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Method;

/**
 * 资源获取相关的工具类
 */
public class ResourceUtils {

    /**
     * 根据图片名称获取图片资源id
     *
     * @param context
     * @param resName
     * @return
     */
    public static int getDrawableIdByName(Context context, String resName) {
        Context applicationContext = context.getApplicationContext();
        return applicationContext.getResources().getIdentifier(resName, "drawable", applicationContext.getPackageName());
    }

    /**
     * 根据名称获取资源id
     *
     * @param context
     * @param resName
     * @return
     */
    public static int getViewIdByName(Context context, String resName) {
        Context applicationContext = context.getApplicationContext();
        return applicationContext.getResources().getIdentifier(resName, "id", applicationContext.getPackageName());
    }

    /**
     * 根据名称获取资源文件中的string
     *
     * @param context
     * @param resName
     * @return
     */
    public static String getStringByName(Context context, String resName) {
        Context applicationContext = context.getApplicationContext();
        Resources resources = applicationContext.getResources();
        int id = resources.getIdentifier(resName, "string", applicationContext.getPackageName());
        return resources.getString(id);
    }

    /**
     * 根据名称获取资源文件中的数组
     *
     * @param context
     * @param resName
     * @return
     */
    public static String[] getArrayByName(Context context, String resName) {
        Context applicationContext = context.getApplicationContext();
        Resources resources = applicationContext.getResources();
        int id = resources.getIdentifier(resName, "array", applicationContext.getPackageName());
        return resources.getStringArray(id);
    }

    /**
     * 从Assets目录下的指定文件中读取文本
     *
     * @param context
     * @param fileName
     * @return
     */
    public static String getTextFromAssets(Context context, String fileName) {
        Context applicationContext = context.getApplicationContext();
        StringBuilder sb = null;
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(applicationContext.getResources().getAssets().open(fileName)));
            String line = "";
            sb = new StringBuilder();
            while ((line = br.readLine()) != null)
                sb.append(line);
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return sb != null ? sb.toString() : "";
    }

    /**
     * 获取尺寸px
     *
     * @param context
     * @param dimenRes
     * @return
     */
    public static int getDimensionPixelSize(Context context, @DimenRes int dimenRes) {
        return context.getResources().getDimensionPixelSize(dimenRes);
    }

    public static int getColor(Context context, @ColorRes int colorResId) {
        return getColor(context, colorResId, null);
    }

    public static int getColor(Context context, @ColorRes int colorResId, Resources.Theme theme) {
        Resources resources = context.getResources();
        Class<?> resourcesClass = resources.getClass();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            try {
                Method getColorMethod = resourcesClass.getMethod("getColor", int.class, Resources.Theme.class);
                getColorMethod.setAccessible(true);
                return (Integer) getColorMethod.invoke(resources, colorResId, theme);
            } catch (Throwable e) {
            }
        else
            try {
                Method getColorMethod = resourcesClass.getMethod("getColor", int.class);
                getColorMethod.setAccessible(true);
                return (Integer) getColorMethod.invoke(resources, colorResId);
            } catch (Throwable e) {
            }
        return Color.BLACK;
    }

    public static Drawable getDrawable(Context context, int drawableId) {
        return getDrawable(context, drawableId, null);
    }

    public static Drawable getDrawable(Context context, int drawableId, Resources.Theme theme) {
        Resources resources = context.getResources();
        Class<?> resourcesClass = resources.getClass();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            try {
                Method getDrawableMethod = resourcesClass.getMethod("getDrawable", int.class, Resources.Theme.class);
                getDrawableMethod.setAccessible(true);
                return (Drawable) getDrawableMethod.invoke(resources, drawableId, theme);
            } catch (Throwable e) {
            }
        else
            try {
                Method getDrawableMethod = resourcesClass.getMethod("getDrawable", int.class);
                getDrawableMethod.setAccessible(true);
                return (Drawable) getDrawableMethod.invoke(resources, drawableId);
            } catch (Throwable e) {
            }
        return null;
    }

    public static void setBackground(View view, int drawableId) {
        setBackground(view, getDrawable(view.getContext(), drawableId));
    }

    public static void setBackground(View view, Drawable background) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
            setBackground("setBackground", view, background);
        else
            setBackground("setBackgroundDrawable", view, background);
    }

    public static void setBackground(String method, View view, Drawable background) {
        try {
            Method viewMethod = view.getClass().getMethod(method, Drawable.class);
            viewMethod.setAccessible(true);
            viewMethod.invoke(view, background);
        } catch (Throwable e) {
        }
    }

    public static void setTextAppearance(TextView view, int textAppearance) {
        Class<?> resourcesClass = view.getClass();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            try {
                Method getColorMethod = resourcesClass.getMethod("setTextAppearance", Context.class, int.class);
                getColorMethod.setAccessible(true);
                getColorMethod.invoke(view, view.getContext(), textAppearance);
            } catch (Throwable e) {
            }
        else
            try {
                Method getColorMethod = resourcesClass.getMethod("setTextAppearance", int.class);
                getColorMethod.setAccessible(true);
                getColorMethod.invoke(view, textAppearance);
            } catch (Throwable e) {
            }
    }
}
