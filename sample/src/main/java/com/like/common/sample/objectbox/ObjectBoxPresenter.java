package com.like.common.sample.objectbox;

import android.support.annotation.NonNull;

import com.like.base.entity.Host;
import com.like.base.presenter.BasePresenter;

import java.util.ArrayList;
import java.util.List;

import io.objectbox.Box;
import io.objectbox.BoxStore;
import io.objectbox.query.Query;

public class ObjectBoxPresenter extends BasePresenter {
    private Box<ObjectBoxInfo> objectBoxInfoBox;

    public ObjectBoxPresenter(@NonNull Host host) {
        super(host);
        BoxStore boxStore = MyObjectBox.builder().androidContext(mContext.getApplicationContext()).build();
        objectBoxInfoBox = boxStore.boxFor(ObjectBoxInfo.class);
    }

    public void addOneData() {
        ObjectBoxInfo info = new ObjectBoxInfo();
        info.setName("一条数据" + 11111);
        objectBoxInfoBox.put(info);
    }

    public void addSomeData() {
        List<ObjectBoxInfo> list = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            ObjectBoxInfo info = new ObjectBoxInfo();
            info.setName("多条数据" + i);
            list.add(info);
        }
        objectBoxInfoBox.put(list);
    }

    public void deleteAllData() {
        objectBoxInfoBox.removeAll();
    }

    public List<ObjectBoxInfo> getAllData() {
        Query<ObjectBoxInfo> objectBoxInfoQuery = objectBoxInfoBox.query().order(ObjectBoxInfo_.name).build();
        return objectBoxInfoQuery.find();
    }

    public List<ObjectBoxInfo> getData(long id) {
        Query<ObjectBoxInfo> objectBoxInfoQuery = objectBoxInfoBox.query().order(ObjectBoxInfo_.name).equal(ObjectBoxInfo_.id, id).build();
        return objectBoxInfoQuery.find();
    }

}
