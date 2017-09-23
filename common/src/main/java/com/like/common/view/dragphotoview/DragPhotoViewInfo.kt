package com.like.common.view.dragphotoview

import java.io.Serializable

class DragPhotoViewInfo(val originLeft: Int, val originTop: Int, val originWidth: Int, val originHeight: Int) : Serializable {
    // 下面是根据原始尺寸计算出来的辅助尺寸
    var originCenterX: Int = 0
    var originCenterY: Int = 0

    init {
        originCenterX = originLeft + originWidth / 2
        originCenterY = originTop + originHeight / 2
    }
}
