package com.like.common.base.presenter;

import android.content.Context;
import android.support.annotation.NonNull;

import com.like.common.base.entity.Host;
import com.like.rxbus.RxBus;

/**
 * Presenter的基类。持有Host和Context的引用。包含RxBus注册和取消注册。
 */
public class BasePresenter {
    protected Host host;
    protected Context mContext;

    public BasePresenter(@NonNull Host host) {
        this.host = host;
        mContext = host.getActivity();
        RxBus.register(this);
    }

    public Host getHost() {
        return host;
    }

    /**
     * 取消注册过的所有的事件，建议放在Activity的onDestroy()方法中。
     */
    public void unregister() {
        RxBus.unregister(this);
    }
}
