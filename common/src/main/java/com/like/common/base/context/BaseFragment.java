package com.like.common.base.context;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.like.common.base.entity.Host;
import com.like.common.base.viewmodel.BaseViewModel;
import com.like.logger.Logger;
import com.trello.rxlifecycle2.components.support.RxFragment;
import com.umeng.analytics.MobclickAgent;

/**
 * <pre>
 * 若把初始化内容放到initData实现</br>
 * 就是采用Lazy方式加载的Fragment</br>
 * 若不需要Lazy加载则initData方法内留空,初始化内容放到initViews即可</br>
 *
 * 注1:</br>
 * 如果是与ViewPager一起使用，调用的是setUserVisibleHint。</br>
 *
 * 注2:</br>
 * 如果是通过FragmentTransaction的show和hide的方法来控制显示，调用的是onHiddenChanged.</br>
 * 针对初始就show的Fragment 为了触发onHiddenChanged事件 达到lazy效果 需要先hide再show</br>
 * eg:</br>
 * transaction.hide(aFragment);</br>
 * transaction.show(aFragment);</br>
 */
public abstract class BaseFragment extends RxFragment {
    protected Activity mActivity;
    /**
     * 是否可见状态
     */
    protected boolean isVisible;
    /**
     * 标志位，View已经初始化完成。
     */
    private boolean isPrepared;
    /**
     * 是否第一次加载
     */
    private boolean isFirstLoad = true;
    public View mContentView;

    protected BaseViewModel mViewModel;
    protected Host host;

    @SuppressWarnings("deprecation")
    @Override
    public void onAttach(Activity activity) {
        Logger.e("BaseFragment onAttach");
        super.onAttach(activity);
        mActivity = activity;
        host = new Host(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Logger.e("BaseFragment onCreateView");
        isFirstLoad = true;
        mContentView = initViews(inflater, container, savedInstanceState);
        initViewModel();
        isPrepared = true;
        lazyLoad();
        return mContentView;
    }

    private void initViewModel() {
        mViewModel = getViewModel();
    }

    @Override
    public void onDestroy() {
        Logger.e("BaseFragment onDestroy");
        super.onDestroy();
        if (mViewModel != null)
            mViewModel.unregister();
    }

    @Override
    public void onResume() {
        Logger.e("BaseFragment onResume");
        super.onResume();
        if (BaseApplication.openUMeng) {
            MobclickAgent.onPageStart(this.getClass().getName()); // 友盟统计：统计页面(仅有Activity的应用中SDK自动调用，不需要单独写。参数为页面名称，可自定义)
        }
        if (isVisible) {
            if (host != null)
                host.setForeground(true);
        }
        if (host != null)
            setChildrenFragmentForeground(host.isForeground());
    }

    @Override
    public void onPause() {
        Logger.e("BaseFragment onPause");
        super.onPause();
        if (BaseApplication.openUMeng) {
            MobclickAgent.onPageEnd(this.getClass().getName()); // 友盟统计：（仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息。参数为页面名称，可自定义
        }
        if (host != null) {
            host.setForeground(false);
            setChildrenFragmentForeground(host.isForeground());
        }
    }

    /**
     * 如果是与ViewPager一起使用，调用的是setUserVisibleHint
     *
     * @param isVisibleToUser 是否显示出来了
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        Logger.e("BaseFragment setUserVisibleHint");
        super.setUserVisibleHint(isVisibleToUser);
        if (host != null)
            host.setForeground(isVisibleToUser);
        if (isVisibleToUser) {
            isVisible = true;
            onVisible();
        } else {
            isVisible = false;
            onInvisible();
        }
    }

    /**
     * 如果是通过FragmentTransaction的show和hide的方法来控制显示，调用的是onHiddenChanged.
     * 若是初始就show的Fragment 为了触发该事件 需要先hide再show
     *
     * @param hidden
     */
    @Override
    public void onHiddenChanged(boolean hidden) {
        Logger.e("BaseFragment onHiddenChanged");
        super.onHiddenChanged(hidden);
        if (host != null)
            host.setForeground(!hidden);
        if (!hidden) {
            isVisible = true;
            onVisible();
        } else {
            isVisible = false;
            onInvisible();
        }
    }

    public void onVisible() {
        lazyLoad();
        if (host != null)
            setChildrenFragmentForeground(host.isForeground());
    }

    public void onInvisible() {
        if (host != null)
            setChildrenFragmentForeground(host.isForeground());
    }

    /**
     * 要实现延迟加载Fragment内容,需要在 onCreateView isPrepared = true;
     */
    private void lazyLoad() {
//        Logger.i(this + "isPrepared=" + isPrepared + ",isVisible=" + isVisible + ",isFirstLoad=" + isFirstLoad);
        if (!isPrepared || !isVisible || !isFirstLoad) {
            return;
        }
        isFirstLoad = false;
        lazyLoadData();
    }

    /**
     * 当设置isFirstLoad为true，下次显示该Fragment时，还会触发lazyLoadData()方法
     *
     * @param isFirstLoad
     */
    public void setFirstLoad(boolean isFirstLoad) {
        this.isFirstLoad = isFirstLoad;
    }

    public boolean isFirstLoad() {
        return isFirstLoad;
    }

    /**
     * 需要延迟加载的数据放到这里
     */
    protected void lazyLoadData() {
    }

    /**
     * 初始化Fragment的视图
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    protected abstract View initViews(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);


    protected abstract BaseViewModel getViewModel();

    /**
     * 是否显示在前面
     *
     * @return
     */
    public boolean isVisible1() {
        return isVisible;
    }

    /**
     * 如果父Fragment中包含有子Fragment，那么可以重写此方法来设置子Fragment的Host的isForeground参数
     *
     * @param isForeground 父Fragment的参数值，一般把子Fragment的参数值设置和这个一样
     */
    protected void setChildrenFragmentForeground(boolean isForeground) {

    }

}
