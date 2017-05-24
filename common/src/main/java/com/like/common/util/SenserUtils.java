package com.like.common.util;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Build;

/**
 * 传感器工具类
 *
 * @author like
 * @version 1.0
 * @created at 2017/2/2 14:10
 */
public class SenserUtils {
    /**
     * 检测计步传感器是否可以使用
     *
     * @param context 上下文
     * @return 是否可用计步传感器
     */
    public static boolean hasStepSensor(Context context) {
        if (context == null) {
            return false;
        }

        Context appContext = context.getApplicationContext();
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            return false;
        } else {
            boolean hasSensor = false;
            Sensor sensor = null;
            try {
                hasSensor = appContext.getPackageManager().hasSystemFeature("android.hardware.sensor.stepcounter");
                SensorManager sm = (SensorManager) appContext.getSystemService(Context.SENSOR_SERVICE);
                sensor = sm.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return hasSensor && sensor != null;
        }
    }
}
