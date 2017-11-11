package com.like.common.sample;

import android.Manifest;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Message;
import android.view.View;

import com.like.base.context.BasePermissionActivity;
import com.like.base.viewmodel.BaseViewModel;
import com.like.common.sample.chenjin.ChenJinActivity;
import com.like.common.sample.csv.CsvActivity;
import com.like.common.sample.customRadioAndCheck.CustomRadioAndCheckActivity;
import com.like.common.sample.databinding.ActivityMainBinding;
import com.like.common.sample.objectbox.ObjectBoxActivity;
import com.like.common.util.ClickUtils;
import com.like.common.util.GlideUtils;
import com.like.common.util.ObjectSerializeUtils;
import com.like.common.util.RxJavaUtils;
import com.like.common.util.Verify;
import com.like.common.view.toolbar.ToolbarUtils;
import com.like.logger.Logger;
import com.like.toast.ToastUtilsKt;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class MainActivity extends BasePermissionActivity {
    private ActivityMainBinding mBinding;
    private android.os.Handler mHandler = new android.os.Handler(new android.os.Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {

            return false;
        }
    });

    @Override
    protected BaseViewModel getViewModel() {
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        initToolBar();
        ClickUtils.addOnClickListener(5, mBinding.btnClicktimes, v -> {

        });
        RxJavaUtils.addTextChangedListener(mBinding.etSearch, new RxJavaUtils.OnSubscribe<String>() {
            @Override
            protected Observable<String> onSubscribeCall1(CharSequence searchStr) {
                Logger.e("onSubscribeCall1----" + searchStr + "-----" + Thread.currentThread().getName());
                return Observable.just("dddd");
            }

            @Override
            protected void onNextCall(String s) {
                super.onNextCall(s);
                Logger.e("onNextCall----------" + s + "-----" + Thread.currentThread().getName());
            }

            @Override
            protected void onErrorCall(Throwable throwable) {
                super.onErrorCall(throwable);
                Logger.e("onErrorCall---------" + Thread.currentThread().getName());
            }
        });

        List<String> urlList = new ArrayList<>();
        urlList.add("http://www.114la.com/static/upd/201708/1515155610347f55.jpg");
        urlList.add("https://www.114la.com/static/upd/201710/32f350101b780039d292c4a4f25f82f0.jpg");
        new GlideUtils(this).downloadImages(urlList).subscribe(stringBitmapPair -> {
            if (stringBitmapPair.first.equals("http://www.114la.com/static/upd/201708/1515155610347f55.jpg"))
                mBinding.iv.setImageBitmap(stringBitmapPair.second);
            else
                mBinding.iv1.setImageBitmap(stringBitmapPair.second);
        });
        return null;
    }

    @Override
    protected String[] getPermissions() {
        return new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.CAMERA
        };
    }

    @Override
    protected String getRationale() {
        return "必需权限";
    }

    @Override
    protected void hasPermissions() {
        // 序列化测试，看是否在杀死进程后，序列化的对象是否也被清除了。
        Logger.e(ObjectSerializeUtils.getObject(this, "111"));
        ObjectSerializeUtils.saveObject(this, "111", new SerializeInfo("like"));
    }

    private void initToolBar() {
        new ToolbarUtils(this, mBinding.flToolbarContainer)
                .showTitle("sample", R.color.common_text_white_0)
                .showCustomNavigationView(R.drawable.icon_take_photo, "拍照", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ToastUtilsKt.shortToastCenter(MainActivity.this, "拍照啦！");
                    }
                })
                .setCustomNavigationViewTextColor(R.color.common_text_white_0)
                .setCustomNavigationViewMessageCount("88")
                .setDividerHeight(30)
                .setToolbarHeight(300)
                .setRightMenu(R.menu.toolbar_right_menu_main, item -> true)
                .setRightMenuMargin(R.id.action_right_message, 10, 0)
                .setRightMenuMessageMargin(R.id.action_right_message, 0, 5, 5, 0)
                .replaceMenuWithCustomView(R.id.action_right_message, R.drawable.icon_0, "消息", v -> {
                })
                .setRightMenuTextColor(R.id.action_right_message, getResources().getColor(R.color.common_text_white_0))
                .setRightMenuMessageCount(R.id.action_right_message, "1").setDividerColor(Color.RED)

                .replaceMenuWithCustomView(R.id.action_right_hahaha, R.drawable.icon_0, "消息", v -> {
                })
                .setRightMenuMessageCount(R.id.action_right_hahaha, "1").setDividerColor(Color.RED);

    }

    @Override
    protected boolean isSupportDoublePressBackToExit() {
        return true;
    }

    public void clickChart0(View view) {
        startActivity(new Intent(this, PieChartActivity.class));
    }

    public void clickChart1(View view) {
        startActivity(new Intent(this, LineFillChartActivity.class));
    }

    public void clickChart2(View view) {
        startActivity(new Intent(this, TwoLineChartActivity.class));
    }

    public void clickChart3(View view) {
        startActivity(new Intent(this, BarChartActivity.class));
    }

    public void clickCustomRadioAndCheck(View view) {
        startActivity(new Intent(this, CustomRadioAndCheckActivity.class));
    }

    public void clickCsv(View view) {
        startActivity(new Intent(this, CsvActivity.class));
    }

    public void clickChenJin(View view) {
        startActivity(new Intent(this, ChenJinActivity.class));
    }

    public void clickReg(View view) {
        Logger.w(Verify.verifyPwd(this, "1!.等等1a@4"));
    }

    public void clickObjectBox(View view) {
        startActivity(new Intent(this, ObjectBoxActivity.class));
    }

    public void clickTakePhoto(View view) {
        startActivity(new Intent(this, TakePhotoActivity.class));
    }

    public void clickDialogFragment(View view) {
        startActivity(new Intent(this, DialogFragmentActivity.class));
    }

    public void clickDragPhoto(View view) {
        startActivity(new Intent(this, DragViewTestActivity.class));
    }

    public void clickNavigation(View view) {
        startActivity(new Intent(this, NavigationActivity.class));
    }

    private Disposable subscribe;

    public void clickRxJavaUtils(View view) {
        Observable.interval(1, TimeUnit.SECONDS).takeUntil(Observable.timer(5, TimeUnit.SECONDS)).subscribe(new Observer<Long>() {
            @Override
            public void onSubscribe(Disposable d) {
                subscribe = d;
                Logger.e("onSubscribe " + subscribe.isDisposed());
            }

            @Override
            public void onNext(Long aLong) {
                Logger.e("onNext " + subscribe.isDisposed());
            }

            @Override
            public void onError(Throwable e) {
                Logger.e("onError " + subscribe.isDisposed());
            }

            @Override
            public void onComplete() {
                Logger.e("onComplete " + subscribe.isDisposed());
            }
        });
    }

}
