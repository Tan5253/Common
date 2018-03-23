package com.like.common.view.bottomnavigationbar

interface OnTabSelectedListener {
    /**
     * 当tab被选中(包括代码选中和手指点击)时回调
     *
     * @param selectedIndex
     */
    fun onSelected(selectedIndex: Int)

    /**
     * 当tab被手指点击时回调
     *
     * @param selectedIndex
     */
    fun onClick(selectedIndex: Int) {}
}
