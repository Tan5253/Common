package com.like.common.sample.chenjin;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import com.like.base.entity.Host;
import com.like.base.viewmodel.BaseViewModel;
import com.like.common.sample.databinding.ActivityChenjinBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by like on 2016/7/7.
 */

public class ChenJinViewModel extends BaseViewModel {
    private ActivityChenjinBinding mBinding;
    private List<Fragment> fragmentList;

    public ChenJinViewModel(@NonNull Host host, ActivityChenjinBinding binding) {
        super(host);
        mBinding = binding;
        initBindingVariable();
    }


    /**
     * 初始化绑定需要的变量(这些变量不需要从网络获取)
     */
    private void initBindingVariable() {
        fragmentList = new ArrayList<>();
        fragmentList.add(new TabFragment0());
        fragmentList.add(new TabFragment1());
        fragmentList.add(new TabFragment2());

        mBinding.setAdapter(new TabFragmentAdapter(host.getFragmentManager(), fragmentList));
    }

    public Fragment getFragment(int position) {
        return fragmentList.get(position);
    }

    public List<Fragment> getAllFragment() {
        return fragmentList;
    }

}
