package com.like.common.sample

import android.databinding.DataBindingUtil
import com.like.base.context.BaseActivity
import com.like.base.viewmodel.BaseViewModel
import com.like.common.sample.databinding.ActivityNavigationBinding
import com.like.common.view.bottomnavigationbar.BottomNavigationBarsHelper

class NavigationActivity : BaseActivity() {
    private val mBinding: ActivityNavigationBinding by lazy {
        DataBindingUtil.setContentView<ActivityNavigationBinding>(this, R.layout.activity_navigation)
    }

    override fun getViewModel(): BaseViewModel? {
        mBinding
        initNavigationBars()
        return null
    }

    /**
     * 添加底部tab导航栏视图
     */
    private fun initNavigationBars() {
        val helper = BottomNavigationBarsHelper(this, mBinding.flBottomTabContainer)
        // 原始的
        val names = arrayOf("电费缴纳", "服务公告", "个人中心")
        val originNormalImageResIds = intArrayOf(R.drawable.main_bottom_tab0_normal, R.drawable.main_bottom_tab1_normal, R.drawable.main_bottom_tab2_normal)
        val originPressImageResIds = intArrayOf(R.drawable.main_bottom_tab0_press, R.drawable.main_bottom_tab1_press, R.drawable.main_bottom_tab2_press)
        val normalTextColor = R.color.common_text_black_1
        val pressTextColor = R.color.common_text_blue_1
        helper.setBottomBgColor(R.color.common_text_white_0)
//        .initOriginView(names, originNormalImageResIds, originPressImageResIds, normalTextColor, pressTextColor)
                .initOriginView(originNormalImageResIds, originPressImageResIds)
                .setNew(1, R.drawable.main_bottom_tab1_new)// 添加活动的
                .setMessageCount(0, 99)// 设置消息数
    }
}
