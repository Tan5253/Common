package com.like.common.view.check;

import android.databinding.ObservableBoolean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 * 复选控制器，默认不选择，用唯一的id来控制每一项，id>=0
 *
 * @author like
 * @version 1.0
 *          created on 2016/11/29 19:37
 */
public class CheckController {
    private HashMap<String, ObservableBoolean> all;
    private HashSet<String> isSelectSet;
    private HashSet<String> isDeSelectSet;

    public CheckController() {
        all = new HashMap<>();
        isSelectSet = new HashSet<>();
        isDeSelectSet = new HashSet<>();
    }

    public void add(String id) {
        isDeSelectSet.add(id);
        all.put(id, new ObservableBoolean());
    }

    public void clear() {
        all.clear();
        isSelectSet.clear();
        isDeSelectSet.clear();
    }

    public void delete(String id) {
        if (all.containsKey(id)) {
            all.remove(id);
        }
        if (isSelectSet.contains(id)) {
            isSelectSet.remove(id);
        }
        if (isDeSelectSet.contains(id)) {
            isDeSelectSet.remove(id);
        }
    }

    /**
     * 选中或者取消选中。没有选中就选中，选中了就取消选中
     */
    public void select(String id) {
        if (isDeSelectSet.contains(id)) {// 没有选中就选中
            isDeSelectSet.remove(id);
            isSelectSet.add(id);
            all.get(id).set(true);
        } else if (isSelectSet.contains(id)) {// 选中了就取消选中
            isSelectSet.remove(id);
            isDeSelectSet.add(id);
            all.get(id).set(false);
        }
    }

    /**
     * 全选
     */
    public void selectAll() {
        for (String id : isDeSelectSet) {
            all.get(id).set(true);
        }
        isSelectSet.addAll(isDeSelectSet);
        isDeSelectSet.clear();
    }

    /**
     * 取消已经选中的
     */
    public void deselectAll() {
        for (String id : isSelectSet) {
            all.get(id).set(false);
        }
        isDeSelectSet.addAll(isSelectSet);
        isSelectSet.clear();
    }

    /**
     * 反选
     */
    public void invertSelection() {
        for (String id : isSelectSet) {
            all.get(id).set(false);
        }
        for (String id : isDeSelectSet) {
            all.get(id).set(true);
        }

        ArrayList<String> temp = new ArrayList<>();
        temp.addAll(isSelectSet);
        isSelectSet.clear();
        isSelectSet.addAll(isDeSelectSet);
        isDeSelectSet.clear();
        isDeSelectSet.addAll(temp);
    }

    public ObservableBoolean get(String id) {
        return all.get(id);
    }

    public HashSet<String> getSelectSet() {
        return isSelectSet;
    }

    public HashSet<String> getDeSelectSet() {
        return isDeSelectSet;
    }
}
