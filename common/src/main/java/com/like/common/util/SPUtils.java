package com.like.common.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import java.lang.reflect.Method;
import java.util.Map;


/**
 * SharedPreferences存储工具类 存储路径：/data/data/packagename/shared_prefs/sp.xml
 * <p>
 * (Manifest.permission.WRITE_EXTERNAL_STORAGE)
 */
public class SPUtils {
    private static final String KEY_IP = "key_ip";
    private static final String KEY_PORT = "key_port";
    private static final String KEY_SESSIONID = "key_sessionId";
    private static String fileName;// 保存为sp.xml
    private static SharedPreferences sp;

    private Context mContext;
    private volatile static SPUtils sInstance = null;

    public static SPUtils getInstance(Context context) {
        if (sInstance == null) {
            synchronized (SPUtils.class) {
                if (sInstance == null) {
                    sInstance = new SPUtils(context);
                }
            }
        }
        return sInstance;
    }

    private SPUtils(Context context) {
        mContext = context.getApplicationContext();
        fileName = context.getPackageName() + ".sharedpreferences";
    }

    /**
     * 保存sessionId到本地
     *
     * @param sessionId
     */
    public void putSessionId(String sessionId) {
        put(KEY_SESSIONID, TextUtils.isEmpty(sessionId) ? "" : sessionId);
    }

    /**
     * 获取sessionId
     *
     * @return
     */
    public String getSessionId() {
        return get(KEY_SESSIONID, "");
    }

    /**
     * 保存ip到本地
     *
     * @param ip
     */
    public void putIp(String ip) {
        put(KEY_IP, TextUtils.isEmpty(ip) ? "" : ip);
    }

    /**
     * 获取ip
     *
     * @return
     */
    public String getIp() {
        return get(KEY_IP, "");
    }

    /**
     * 保存port到本地
     *
     * @param port
     */
    public void putPort(int port) {
        put(KEY_PORT, port);
    }

    /**
     * 获取port
     *
     * @return
     */
    public int getPort() {
        return get(KEY_PORT, 0);
    }

    /**
     * 存入键值对数据
     *
     * @param key
     * @param value
     */
    public void put(String key, Object value) {
        if (sp == null) {// 单例，因为操作的是同一个xml文件，所以只需要一个sp对象即可。
            sp = mContext.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        }
        SharedPreferences.Editor editor = sp.edit();
        if (value.getClass() == String.class)
            editor.putString(key, (String) value);
        else if (value.getClass() == Boolean.class)
            editor.putBoolean(key, (Boolean) value);
        else if (value.getClass() == Float.class)
            editor.putFloat(key, (Float) value);
        else if (value.getClass() == Integer.class)
            editor.putInt(key, (Integer) value);
        else if (value.getClass() == Long.class)
            editor.putLong(key, (Long) value);
        else
            editor.putString(key, value.toString());
        SPCompatible.apply(editor);
    }

    /**
     * 根据key获取跟defaultValue数据类型相同的value，如果获取数据失败，则返回指定defaultValue
     *
     * @param key
     * @param defaultValue 如果获取数据失败，返回此默认值
     * @return
     */
    @SuppressWarnings("unchecked")
    public <T> T get(String key, Object defaultValue) {
        if (sp == null) {
            sp = mContext.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        }
        if (!sp.contains(key)) {
            return (T) defaultValue;
        }
        // 如果默认值为null，说明肯定是String类型
        if (defaultValue == null) {// 这里必须单独判断，否则后面就不能使用.getClass()方法
            defaultValue = sp.getString(key, null);
        } else {
            // 这里如果传入的defaultValue是基本数据类型，也可以使用，因为自动装箱了。
            if (defaultValue.getClass() == String.class)
                defaultValue = sp.getString(key, (String) defaultValue);
            else if (defaultValue.getClass() == Boolean.class)
                defaultValue = sp.getBoolean(key, (Boolean) defaultValue);
            else if (defaultValue.getClass() == Integer.class)
                defaultValue = sp.getInt(key, (Integer) defaultValue);
            else if (defaultValue.getClass() == Float.class)
                defaultValue = sp.getFloat(key, (Float) defaultValue);
            else if (defaultValue.getClass() == Long.class)
                defaultValue = sp.getLong(key, (Long) defaultValue);
        }
        return (T) defaultValue;
    }

    /**
     * 移除某个key对应的那一条数据
     *
     * @param key
     */
    public void remove(String key) {
        if (sp == null) {
            sp = mContext.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        }
        SharedPreferences.Editor editor = sp.edit();
        editor.remove(key);
        SPCompatible.apply(editor);
    }

    /**
     * 清除所有数据
     */
    public void clear() {
        if (sp == null) {
            sp = mContext.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        }
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        SPCompatible.apply(editor);
    }

    /**
     * 查询某个key是否已经存在
     *
     * @param key
     * @return
     */
    public boolean contains(String key) {
        if (sp == null) {
            sp = mContext.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        }
        return sp.contains(key);
    }

    /**
     * 返回所有的键值对数据
     *
     * @return
     */
    public Map<String, ?> getAll() {
        if (sp == null) {
            sp = mContext.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        }
        return sp.getAll();
    }

    /**
     * 创建一个解决SPCompatible.commit()方法的一个兼容类<br/>
     * 注意：<br/>
     * 1、本工具类里面所有的commit()操作都使用了SharedPreferencesCompat.apply()进行了替代。<br/>
     * 2、原因是因为commit方法是同步的，并且我们很多时候的commit操作都是UI线程中，毕竟是IO操作，尽可能采用异步apply()方法。<br/>
     * 3、但是apply()相当于commit()来说是new API呢，为了更好的兼容，我们做了适配，即尽量用异步方法，没有才选择同步方法。
     */
    private static class SPCompatible {
        private static final Method applyMethod = findApplyMethod();

        /**
         * 反射查找apply的方法
         *
         * @return
         */
        @SuppressWarnings({"unchecked", "rawtypes"})
        private static Method findApplyMethod() {
            try {
                Class clz = SharedPreferences.Editor.class;
                return clz.getMethod("apply");
            } catch (NoSuchMethodException e) {
            }
            return null;
        }

        /**
         * 如果找到则使用apply()执行，否则使用commit()
         *
         * @param editor
         */
        public static void apply(SharedPreferences.Editor editor) {
            try {
                if (applyMethod != null) {
                    applyMethod.invoke(editor);
                    return;
                }
            } catch (Exception e) {
            }
            editor.commit();
        }
    }
}
