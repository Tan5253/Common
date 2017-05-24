package com.like.common.view.check;

import android.databinding.ObservableBoolean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

/**
 * 复选控制器，默认不选择，用实体来控制唯一，所以实体必须实现equals()和hashCode()方法
 *
 * @author like
 * @version 1.0
 * @created at 2017/4/23 6:20
 */
public class ObjectCheckController<T> {
    private HashMap<T, ObservableBoolean> all;
    private HashSet<T> isSelectSet;
    private HashSet<T> isDeSelectSet;

    public ObjectCheckController() {
        all = new HashMap<>();
        isSelectSet = new HashSet<>();
        isDeSelectSet = new HashSet<>();
    }

    public void add(T entity) {
        isDeSelectSet.add(entity);
        all.put(entity, new ObservableBoolean());
    }

    public void addAll(List<T> list) {
        isDeSelectSet.addAll(list);
        for (T entity : list) {
            all.put(entity, new ObservableBoolean());
        }
    }

    public void clear() {
        all.clear();
        isSelectSet.clear();
        isDeSelectSet.clear();
    }

    public void delete(T entity) {
        all.remove(entity);
        isSelectSet.remove(entity);
        isDeSelectSet.remove(entity);
    }

    public void deleteAll(List<T> list) {
        for (T entity : list) {
            all.remove(entity);
        }
        isSelectSet.removeAll(list);
        isDeSelectSet.removeAll(list);
    }

    /**
     * 选中或者取消选中。没有选中就选中，选中了就取消选中
     */
    public void select(T entity) {
        if (isDeSelectSet.contains(entity)) {// 没有选中就选中
            isDeSelectSet.remove(entity);
            isSelectSet.add(entity);
            all.get(entity).set(true);
        } else if (isSelectSet.contains(entity)) {// 选中了就取消选中
            isSelectSet.remove(entity);
            isDeSelectSet.add(entity);
            all.get(entity).set(false);
        }
    }

    /**
     * 全选
     */
    public void selectAll() {
        for (T entity : isDeSelectSet) {
            all.get(entity).set(true);
        }
        isSelectSet.addAll(isDeSelectSet);
        isDeSelectSet.clear();
    }

    /**
     * 取消已经选中的
     */
    public void deselectAll() {
        for (T entity : isSelectSet) {
            all.get(entity).set(false);
        }
        isDeSelectSet.addAll(isSelectSet);
        isSelectSet.clear();
    }

    /**
     * 反选
     */
    public void invertSelection() {
        for (T entity : isSelectSet) {
            all.get(entity).set(false);
        }
        for (T entity : isDeSelectSet) {
            all.get(entity).set(true);
        }

        ArrayList<T> temp = new ArrayList<>();
        temp.addAll(isSelectSet);
        isSelectSet.clear();
        isSelectSet.addAll(isDeSelectSet);
        isDeSelectSet.clear();
        isDeSelectSet.addAll(temp);
    }

    /**
     * 用于绑定选中、未选中的checkbox状态
     */
    public ObservableBoolean get(T entity) {
        return all.get(entity);
    }

    public List<T> getSelectSet() {
        return new ArrayList<>(isSelectSet);
    }

    public List<T> getDeSelectSet() {
        return new ArrayList<>(isDeSelectSet);
    }
}