package com.like.common.view.banner

import android.content.Context
import android.os.Handler
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewTreeObserver
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import com.like.common.R
import com.like.common.util.ResourceUtils

abstract class BaseBannerView<in T>(context: Context, attrs: AttributeSet?) : RelativeLayout(context, attrs) {
    private var vp: BannerViewPager? = null
    private var indicatorContainer: LinearLayout? = null
    /**
     * 循环的时间间隔
     */
    private var mCycleInterval: Long = 0L
    /**
     * 指示器控制器
     */
    private var mIndicatorViewControl: IndicatorViewControl? = null
    /**
     * 真实的图片数量
     */
    private var mRealImageCount = -1
    /**
     * ViewPager的当前位置
     */
    private var mCurPosition = 100000
    /**
     * 是否正在自动循环播放
     */
    private var isAutoPlaying: Boolean = false

    private var mCycleHandler: Handler? = null

    private var mList: List<T>? = null

    init {
        val viewLayoutId = getViewLayoutId()
        if (viewLayoutId < 0) {
            throw IllegalArgumentException("getViewLayoutId()获取的布局id有误")
        }
        val view = LayoutInflater.from(context).inflate(viewLayoutId, this, true)
        vp = view.findViewById(ResourceUtils.getViewIdByName(context, "vp"))
        indicatorContainer = view.findViewById(ResourceUtils.getViewIdByName(context, "indicatorContainer"))
        if (view == null) {
            throw IllegalArgumentException("getViewLayoutId()获取的布局id有误")
        }
        if (vp == null) {
            throw IllegalArgumentException("沒有找到id为‘vp’的BannerViewPager")
        }
        if (indicatorContainer == null) {
            throw IllegalArgumentException("沒有找到id为‘indicatorContainer’的LinearLayout指示器容器")
        }
        mCycleHandler = Handler {
            if (isAutoPlaying) {
                // Logger.d("handleMessage mCurPosition=" + mCurPosition);
                mCurPosition++
                vp?.setCurrentItem(mCurPosition, true)
                mCycleHandler?.sendEmptyMessageDelayed(0, mCycleInterval)
            }
            true
        }
    }


    /**
     * 初始化banner并播放
     *
     * @param heightWidthRatio          banner图的高宽比
     * @param cycleInterval             循环时间间隔，毫秒。如果<=0，表示不循环播放
     * @param list                      数据，如果个数为1，则不会滚动
     * @param normalIndicatorResId      正常状态的指示器图片id
     * @param selectedIndicatorResIds   选中状态的指示器图片id
     * @param indicatorPadding          指示器之间的间隔，默认10dp
     */
    fun initParamsAndStartPlay(heightWidthRatio: Float, cycleInterval: Long, list: List<T>, normalIndicatorResId: Int, selectedIndicatorResIds: List<Int>, indicatorPadding: Int) {
        if (list.isEmpty()) {
            throw IllegalArgumentException("list必须大于0")
        }
        mRealImageCount = list.size
        mList = list

        initViewPager(heightWidthRatio)

        when (list.size) {
            1 -> {
                vp?.setScrollable(false)
                pausePlay()
            }
            else -> {
                mCycleInterval = cycleInterval
                // 初始化指示器视图
                mIndicatorViewControl = IndicatorViewControl(context, indicatorContainer, mRealImageCount, normalIndicatorResId, selectedIndicatorResIds, indicatorPadding)
                vp?.setScrollable(true)
                if (cycleInterval > 0) {
                    // 开始轮播
                    startPlay()
                }
            }
        }
    }

    private fun initViewPager(heightWidthRatio: Float) {
        vp ?: return
        vp!!.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                vp!!.viewTreeObserver.removeOnGlobalLayoutListener(this)

                if (mList != null && mList!!.isNotEmpty()) {
                    vp!!.layoutParams.height = (vp!!.width * heightWidthRatio).toInt()
                    vp!!.adapter = getAdapter(vp!!, mList!!)
                    vp!!.offscreenPageLimit = 3
                    // 取余处理，避免默认值不能被整除
                    mCurPosition -= mCurPosition % mRealImageCount
                    vp!!.currentItem = mCurPosition
                }
            }
        })
        vp!!.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            // position当前选择的是哪个页面
            override fun onPageSelected(position: Int) {
                mCurPosition = position
                // 设置指示器
                selectDot()
            }

            // position表示目标位置，positionOffset表示偏移的百分比，positionOffsetPixels表示偏移的像素
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

            override fun onPageScrollStateChanged(state: Int) {
                when (state) {
                    1// 开始滑动
                    -> if (isAutoPlaying)
                        pausePlay()
                    2// 手指松开了页面自动滑动
                    -> {
                    }
                    0// 停止在某页
                    -> if (!isAutoPlaying)
                        continuePlay()
                }
            }
        })
    }

    private fun selectDot() {
        mIndicatorViewControl?.select(mCurPosition % mRealImageCount)
    }

    private fun startPlay() {
        if (mRealImageCount <= 0) {
            return
        }
        isAutoPlaying = true
        mCycleHandler?.removeCallbacksAndMessages(null)
        mCycleHandler?.sendEmptyMessageDelayed(0, mCycleInterval)
    }

    private fun pausePlay() {
        isAutoPlaying = false
        mCycleHandler?.removeCallbacksAndMessages(null)
    }

    private fun continuePlay() {
        isAutoPlaying = true
        mCycleHandler?.removeCallbacksAndMessages(null)
        mCycleHandler?.sendEmptyMessageDelayed(0, mCycleInterval)
    }

    fun destroy() {
        mCycleHandler?.removeCallbacksAndMessages(null)
        destoryBitmaps()
        mIndicatorViewControl?.destory()
    }

    /**
     * 销毁所有图片资源
     */
    private fun destoryBitmaps() {
        vp ?: return
        val childCount = vp!!.childCount
        (0 until childCount)
                .map { vp!!.getChildAt(it) as ImageView }
                .mapNotNull { it.drawable }
                .forEach {
                    it.callback = null// 解除drawable对view的引用
                }
    }

    abstract fun getAdapter(vp: BannerViewPager, list: List<T>): BaseBannerPagerAdapter

    /**
     * view的id，其中包括id为"vp"的BannerViewPager、id为"indicatorContainer"的LinearLayout指示器容器
     */
    abstract fun getViewLayoutId(): Int

}
