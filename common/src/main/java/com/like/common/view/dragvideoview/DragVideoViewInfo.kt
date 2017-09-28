package com.like.common.view.dragvideoview

import android.os.Parcel
import android.os.Parcelable

/**
 * @param originLeft    原始imageview的left
 * @param originTop     原始imageview的top
 * @param originWidth   原始imageview的width
 * @param originHeight  原始imageview的height
 * @param imageUrl      原始图片的url
 * @param imageResId    原始图片的资源id
 * @param videoUrl      视频url
 */
class DragVideoViewInfo(val originLeft: Int, val originTop: Int, val originWidth: Int, val originHeight: Int, val imageUrl: String = "", val imageResId: Int = 0, val videoUrl: String = "") : Parcelable {
    // 下面是根据原始尺寸计算出来的辅助尺寸
    var originCenterX: Int = originLeft + originWidth / 2
    var originCenterY: Int = originTop + originHeight / 2

    constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readInt(),
            parcel.readInt(),
            parcel.readInt(),
            parcel.readString(),
            parcel.readInt(),
            parcel.readString()) {
        originCenterX = parcel.readInt()
        originCenterY = parcel.readInt()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(originLeft)
        parcel.writeInt(originTop)
        parcel.writeInt(originWidth)
        parcel.writeInt(originHeight)
        parcel.writeString(imageUrl)
        parcel.writeInt(imageResId)
        parcel.writeString(videoUrl)
        parcel.writeInt(originCenterX)
        parcel.writeInt(originCenterY)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<DragVideoViewInfo> {
        override fun createFromParcel(parcel: Parcel): DragVideoViewInfo {
            return DragVideoViewInfo(parcel)
        }

        override fun newArray(size: Int): Array<DragVideoViewInfo?> {
            return arrayOfNulls(size)
        }
    }


}