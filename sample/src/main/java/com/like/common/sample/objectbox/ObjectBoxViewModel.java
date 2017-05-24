package com.like.common.sample.objectbox;

import android.support.annotation.NonNull;
import android.view.View;

import com.like.base.entity.Host;
import com.like.base.viewmodel.BaseViewModel;
import com.like.logger.Logger;

import java.util.List;

/**
 * Created by like on 2016/11/30.
 */

public class ObjectBoxViewModel extends BaseViewModel {
    private List<ObjectBoxInfo> list;
    private ObjectBoxPresenter mPresenter;

    public ObjectBoxViewModel(@NonNull Host host) {
        super(new ObjectBoxPresenter(host));
        mPresenter = (ObjectBoxPresenter) super.mPresenter;
    }

    public void addOneData(View view) {
        mPresenter.addOneData();
    }

    public void addSomeData(View view) {
        mPresenter.addSomeData();
    }

    public void deleteAllData(View view) {
        mPresenter.deleteAllData();
    }

    public void getAllData(View view) {
        list = mPresenter.getAllData();
        Logger.printCollection(list);
    }

    public void getData(View view) {
        list = mPresenter.getData(1);
        Logger.printCollection(list);
    }

}
