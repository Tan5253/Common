package com.like.common.base.viewmodel;

import android.content.Context;
import android.support.annotation.NonNull;

import com.like.common.base.entity.Host;
import com.like.common.base.presenter.BasePresenter;
import com.like.rxbus.RxBus;

/**
 * ViewModel的基类，持有Host、Context、BasePresenter的引用。包含RxBus注册和取消注册。
 */
public class BaseViewModel {
    protected Host host;
    protected Context mContext;
    protected BasePresenter mPresenter;

    public BaseViewModel(@NonNull BasePresenter presenter) {
        host = presenter.getHost();
        mContext = host.getActivity();
        mPresenter = presenter;
        RxBus.register(this);
    }

    public BaseViewModel(@NonNull Host host) {
        this.host = host;
        mContext = host.getActivity();
        RxBus.register(this);
    }

    /**
     * 取消注册过的所有的事件，建议放在Activity的onDestroy()方法中。
     */
    public void unregister() {
        RxBus.unregister(this);
        if (mPresenter != null)
            mPresenter.unregister();
    }
}
