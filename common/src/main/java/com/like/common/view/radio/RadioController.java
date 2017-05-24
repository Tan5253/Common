package com.like.common.view.radio;

import android.databinding.ObservableField;
import android.text.TextUtils;

/**
 * 单选控制器，默认不选择，用唯一的id来控制每一项，id>=0
 *
 * @author like
 * @version 1.0
 *          created on 2016/11/29 19:37
 */
public class RadioController {
    private ObservableField<String> mPreSelectId = new ObservableField<>();
    private ObservableField<String> mCurSelectId = new ObservableField<>();

    /**
     * @param id 唯一标记
     */
    public void select(String id) {
        mPreSelectId.set(mCurSelectId.get());
        mCurSelectId.set(id);
    }

    public void clear() {
        mPreSelectId.set(null);
        mCurSelectId.set(null);
    }

    public void delete(String id) {
        if (mPreSelectId.get() != null && mPreSelectId.get().equals(id)) {
            mPreSelectId.set(null);
        }
        if (mCurSelectId.get() != null && mCurSelectId.get().equals(id)) {
            mCurSelectId.set(null);
        }
    }

    /**
     * 是否有选中的
     *
     * @return
     */
    public boolean hasSeleted() {
        return !TextUtils.isEmpty(mCurSelectId.get());
    }

    /**
     * 获取当前选中项的索引
     *
     * @return
     */
    public ObservableField<String> getCurSelectedId() {
        return mCurSelectId;
    }

    /**
     * 用于异步请求失败时还原到上次的选中状态
     */
    public void restore() {
        select(mPreSelectId.get());
    }

    public String getPreSelectId() {
        return mPreSelectId.get();
    }
}
