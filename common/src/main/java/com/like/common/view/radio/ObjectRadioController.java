package com.like.common.view.radio;

import android.databinding.ObservableField;

/**
 * 单选控制器，默认不选择，用实体来控制唯一，所以实体必须实现equals()和hashCode()方法
 *
 * @author like
 * @version 1.0
 *          created on 2016/11/29 19:37
 */
public class ObjectRadioController<T> {
    private ObservableField<T> mPreSelect = new ObservableField<>();
    private ObservableField<T> mCurSelect = new ObservableField<>();

    public void select(T t) {
        mPreSelect.set(mCurSelect.get());
        mCurSelect.set(t);
    }

    public void clear() {
        mPreSelect.set(null);
        mCurSelect.set(null);
    }

    public void delete(T t) {
        if (mPreSelect.get() != null && mPreSelect.get().equals(t)) {
            mPreSelect.set(null);
        }
        if (mCurSelect.get() != null && mCurSelect.get().equals(t)) {
            mCurSelect.set(null);
        }
    }

    /**
     * 是否有选中的
     *
     * @return
     */
    public boolean hasSeleted() {
        return mCurSelect.get() == null;
    }

    /**
     * 获取当前选中项的索引
     *
     * @return
     */
    public ObservableField<T> getCurSelected() {
        return mCurSelect;
    }

    /**
     * 用于异步请求失败时还原到上次的选中状态
     */
    public void restore() {
        select(mPreSelect.get());
    }

    public T getPreSelect() {
        return mPreSelect.get();
    }
}
