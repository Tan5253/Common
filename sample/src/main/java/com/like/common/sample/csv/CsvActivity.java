package com.like.common.sample.csv;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import com.like.base.context.BaseActivity;
import com.like.base.viewmodel.BaseViewModel;
import com.like.common.sample.R;
import com.like.common.sample.databinding.ActivityCsvBinding;
import com.like.common.util.CsvUtils;
import com.like.common.view.toolbar.ToolbarUtils;
import com.like.toast.ToastUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by like on 2016/12/20.
 */

public class CsvActivity extends BaseActivity {
    private ActivityCsvBinding mBinding;

    @Override
    protected BaseViewModel getViewModel() {
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_csv);
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initToolBar();
    }

    private void initToolBar() {
        new ToolbarUtils(this, mBinding.flToolbarContainer)
                .showTitle("csv读写测试", R.color.common_text_white_0)
                .showBackButton();
    }

    public void write(View view) {
        List<String[]> list = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            String[] s = new String[]{"标题" + i, "内容" + i};
            list.add(s);
        }
        CsvUtils.getInstance(this).write(aBoolean -> {
            if (aBoolean)
                ToastUtils.showShortCenter(this, "写入成功");
        }, "test", list);
//        CsvUtils.getInstance(this).write(null, "test", "内容1内容1内容1~~内容1内容1@#@%$!@", "lsadkfl322l3rk2");
    }

    public void read(View view) {
        StringBuilder sb = new StringBuilder();
        CsvUtils.getInstance(this).read(strings -> {
            for (int i = 0; i < strings.size(); i++) {
                for (int j = 0; j < strings.get(i).length; j++) {
                    sb.append(strings.get(i)[j]).append(",");
                }
                sb.append("\n");
            }
            mBinding.tv.setText(sb.toString());
        }, "test");
    }

}
