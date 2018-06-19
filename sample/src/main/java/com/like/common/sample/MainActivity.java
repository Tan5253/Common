package com.like.common.sample;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.like.common.base.context.BaseActivity;
import com.like.common.base.viewmodel.BaseViewModel;
import com.like.common.sample.banner.BannerInfo;
import com.like.common.sample.chenjin.ChenJinActivity;
import com.like.common.sample.csv.CsvActivity;
import com.like.common.sample.customRadioAndCheck.CustomRadioAndCheckActivity;
import com.like.common.sample.databinding.ActivityMainBinding;
import com.like.common.sample.objectbox.ObjectBoxActivity;
import com.like.common.util.AppUtils;
import com.like.common.util.ClickUtils;
import com.like.common.util.DimensionUtils;
import com.like.common.util.GlideUtils;
import com.like.common.util.PhoneUtils;
import com.like.common.util.RxJavaUtils;
import com.like.common.util.StatusBarUtils;
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

public class MainActivity extends BaseActivity {
    private ActivityMainBinding mBinding;
    private android.os.Handler mHandler = new android.os.Handler(new android.os.Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {

            return false;
        }
    });
    private Disposable mDisposable = null;

    @Override
    protected BaseViewModel getViewModel() {
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        initToolBar();

        List<BannerInfo> bannerInfoList = new ArrayList<>();
        BannerInfo bannerInfo = new BannerInfo();
        bannerInfo.imageUrl = "https://www.114la.com/static/upd/201710/311040041f91ccea.jpg";
        bannerInfoList.add(bannerInfo);
        BannerInfo bannerInfo1 = new BannerInfo();
        bannerInfo1.imageUrl = "https://www.114la.com/static/upd/201710/31105918a5901eac.jpg";
        bannerInfoList.add(bannerInfo1);
        BannerInfo bannerInfo2 = new BannerInfo();
        bannerInfo2.imageUrl = "https://123p0.sogoucdn.com/imgu/2018/01/20180122151534_866.jpg";
        bannerInfoList.add(bannerInfo2);

        List<Integer> list = new ArrayList<>();
        list.add(R.drawable.store_point2);
        mBinding.bannerView.initParamsAndStartPlay(0.4f, 2000L, bannerInfoList, R.drawable.store_point1, list, 10);

        PhoneUtils.getInstance(this).getUuid();
        AppUtils.getInstance(this);

        mDisposable = RxJavaUtils.polling(() -> Logger.e("asdf"), 1000, 4000);

        ClickUtils.addOnClickListener(5, mBinding.btnClicktimes, v -> {

        });
//        RxJavaUtils.addTextChangedListener(mBinding.etSearch, new RxJavaUtils.OnSubscribe<String>() {
//            @Override
//            protected Observable<String> onSubscribeCall1(CharSequence searchStr) {
//                Logger.e("onSubscribeCall1----" + searchStr + "-----" + Thread.currentThread().getName());
//                return Observable.just("dddd");
//            }
//
//            @Override
//            protected void onNextCall(String s) {
//                super.onNextCall(s);
//                Logger.e("onNextCall----------" + s + "-----" + Thread.currentThread().getName());
//            }
//
//            @Override
//            protected void onErrorCall(Throwable throwable) {
//                super.onErrorCall(throwable);
//                Logger.e("onErrorCall---------" + Thread.currentThread().getName());
//            }
//        });

//        List<String> urlList = new ArrayList<>();
//        urlList.add("http://www.114la.com/static/upd/201708/1515155610347f55.jpg");
//        urlList.add("https://www.114la.com/static/upd/201710/32f350101b780039d292c4a4f25f82f0.jpg");
//        new GlideUtils(this).downloadImages(urlList).subscribe(stringBitmapPair -> {
//            if (stringBitmapPair.first.equals("http://www.114la.com/static/upd/201708/1515155610347f55.jpg"))
//                mBinding.iv.setImageBitmap(stringBitmapPair.second);
//            else
//                mBinding.iv1.setImageBitmap(stringBitmapPair.second);
//        });

//        new GlideUtils(this).getBitmapSize("http://www.114la.com/static/upd/201708/1515155610347f55.jpg", new GlideUtils.onGetSizeListener() {
//            @Override
//            public void getSize(@Nullable Bitmap bitmap, int width, int height) {
//                Logger.e("width = " + width + " , height = " + height);
//            }
//        });
        new GlideUtils(this).displayCircleNoCache("", mBinding.iv, -1, R.drawable.image_0);
        return null;
    }

//    @Override
//    protected String[] getPermissions() {
//        return new String[]{
//                Manifest.permission.ACCESS_FINE_LOCATION,
//                Manifest.permission.READ_PHONE_STATE
//        };
//    }
//
//    @Override
//    protected String getRationale() {
//        return "必需权限";
//    }
//
//    @Override
//    protected void hasPermissions() {
//        // 序列化测试，看是否在杀死进程后，序列化的对象是否也被清除了。
//        Logger.e(ObjectSerializeUtils.getObject(this, "111"));
//        ObjectSerializeUtils.saveObject(this, "111", new SerializeInfo("like"));
//    }

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
                .setCustomNavigationViewMessageCount("99+")
                .setCustomNavigationViewMessageBackgroundColor(ContextCompat.getColor(this, R.color.common_divider_gray))
                .setCustomNavigationViewMessageTextSize(10)
                .setDividerHeight(30)
                .setToolbarHeight(DimensionUtils.dp2px(this, 52f))
                .setRightMenu(R.menu.toolbar_right_menu_main, item -> true)
                .setRightMenuMargin(R.id.action_right_message, 10, 0)
                .replaceMenuWithCustomView(R.id.action_right_message, R.drawable.icon_0, "消息", v -> {
                })
                .setRightMenuTextColor(R.id.action_right_message, getResources().getColor(R.color.common_text_white_0))
                .setRightMenuMessageCount(R.id.action_right_message, "1").setDividerColor(Color.RED)

                .replaceMenuWithCustomView(R.id.action_right_hahaha, R.drawable.icon_0, "消息", v -> {
                })
                .setRightMenuMessageCount(R.id.action_right_hahaha, "2").setDividerColor(Color.RED);
        Logger.e(DimensionUtils.dp2px(this, 52f) + " , " + StatusBarUtils.getStatusBarHeight(this));
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mDisposable != null && !mDisposable.isDisposed()) {
            mDisposable.dispose();
        }
    }
}
