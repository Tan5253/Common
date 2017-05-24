package com.like.common.util;

import com.like.logger.Logger;

import java.lang.reflect.Method;

/**
 * 反射相关的工具类
 *
 * @author like
 * @version 1.0
 *          created on 2017/3/16 15:29
 */
public class ReflectUtils {

    /**
     * 通过反射执行方法
     *
     * @param clazz
     * @param methodName
     * @param args       实参
     */
    public static Object invokeMethod(Class<?> clazz, Object receiver, String methodName, Object... args) throws Exception {
        if (clazz == null || methodName == null || methodName.isEmpty() || hasNullArgs(args)) {
            return null;
        }
        Method[] methods = clazz.getDeclaredMethods();// 获取全部方法
        if (methods != null && methods.length > 0) {
            for (Method method : methods) {
                // 判断方法名是否匹配
                if (methodName.equals(method.getName())) {
                    // 判断参数列表是否匹配
                    Class[] argsTypes = method.getParameterTypes();
                    if (argsTypes.length != args.length) {// 形参和实参的个数是否匹配
                        break;
                    }
                    boolean isAllParamsMatch = true;// 是否所有参数类型都匹配
                    for (int j = 0; j < argsTypes.length; j++) {
                        // 校验一个类是否实现指定的父类。parent.isAssignableFrom(child)
                        if (!argsTypes[j].isAssignableFrom(args[j].getClass())) {
                            isAllParamsMatch = false;
                            break;
                        }
                    }
                    if (isAllParamsMatch) {
                        method.setAccessible(true);
                        return method.invoke(receiver, args);
                    }
                }
            }
        }
        Logger.e("ReflectUtils invokeMethod 没有找到对应的方法");
        return null;
    }

    /**
     * 检查参数是否为null，或者其中是否包含null值
     */
    private static boolean hasNullArgs(Object... args) {
        if (args == null) {
            Logger.e("ReflectUtils invokeMethod args 不能为null");
            return true;
        } else if (args.length > 0) {
            int nullArgCount = 0;
            for (Object obj : args) {
                if (obj == null) {
                    nullArgCount++;
                }
            }
            if (nullArgCount > 0) {
                Logger.e("ReflectUtils invokeMethod args 中不能包含null值");
                return true;
            }
        }
        return false;
    }

}
