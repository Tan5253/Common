package com.like.common.util;

import android.util.SparseArray;
import android.view.View;

/**
 * ViewHolder类，在adapter适配器中使用
 */
public class ViewHolder {
    // 不允许直接构造此类，也不允许反射构造此类
    private ViewHolder() {
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    /**
     * 根据父容器和id查找视图。
     *
     * @param convertView
     * @param id
     * @return View
     */
    @SuppressWarnings("unchecked")
    public static <T extends View> T get(View convertView, int id) {
        // 一个viewHolder代表一个item，它里面包含了item中的所有视图
        SparseArray<View> viewHolder = (SparseArray<View>) convertView.getTag();
        if (viewHolder == null) {
            viewHolder = new SparseArray<View>();
            convertView.setTag(viewHolder);
        }
        View childView = viewHolder.get(id);// 从viewHolder集合中取数据
        if (childView == null) {// 说明此子视图还没有加入viewHolder中
            // 使用viewHolder减少了查找xml文件的次数。
            childView = convertView.findViewById(id);// 从父容器中查找
            viewHolder.put(id, childView);
        }
        return (T) childView;
    }
}