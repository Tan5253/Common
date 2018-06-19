package com.like.common.base.entity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 需要进行数据解析的基类
 */
public abstract class BaseParseInfo<T> {
    /**
     * 从json数组中解析出对象集合
     *
     * @param jsonArray json数组
     * @return 实体类集合
     */
    public List<T> parse(JSONArray jsonArray) {
        if (jsonArray == null) {
            return null;
        }

        List<T> result = new ArrayList<>();

        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                result.add(parse(jsonArray.optJSONObject(i)));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return result;
    }

    /**
     * 从json对象从解析出对象
     *
     * @param jsonObject json对象
     * @return 实体类
     */
    public abstract T parse(JSONObject jsonObject);
}
