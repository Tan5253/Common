package com.like.common.view.dragphotoview

import android.os.Parcel
import android.os.Parcelable

/**
 * @param originLeft    原始imageview的left
 * @param originTop     原始imageview的top
 * @param originWidth   原始imageview的width
 * @param originHeight  原始imageview的height
 * @param imageUrl      原始图片的url
 * @param imageResId    原始图片的资源id
 * @param isClicked     是否当前点击的那张图片
 */
class DragPhotoViewInfo(val originLeft: Int, val originTop: Int, val originWidth: Int, val originHeight: Int, val imageUrl: String = "", val imageResId: Int = 0, val isClicked: Boolean = false) : Parcelable {
    // 下面是根据原始尺寸计算出来的辅助尺寸
    var originCenterX: Int = originLeft + originWidth / 2
    var originCenterY: Int = originTop + originHeight / 2
    var index: Int = 0// 以第一次进入DragPhotoViewActivity时那个视图为索引0，前后的视图依此类推。

    constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readInt(),
            parcel.readInt(),
            parcel.readInt(),
            parcel.readString(),
            parcel.readInt(),
            parcel.readByte() != 0.toByte()) {
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
        parcel.writeByte(if (isClicked) 1 else 0)
        parcel.writeInt(originCenterX)
        parcel.writeInt(originCenterY)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<DragPhotoViewInfo> {
        override fun createFromParcel(parcel: Parcel): DragPhotoViewInfo {
            return DragPhotoViewInfo(parcel)
        }

        override fun newArray(size: Int): Array<DragPhotoViewInfo?> {
            return arrayOfNulls(size)
        }
    }


}
