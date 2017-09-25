package com.like.common.view.dragphotoview

import android.os.Parcel
import android.os.Parcelable

class DragPhotoViewInfo(val originLeft: Int, val originTop: Int, val originWidth: Int, val originHeight: Int, val imageUrl: String = "", val imageResId: Int = 0, val isClicked: Boolean = false) : Parcelable {
    // 下面是根据原始尺寸计算出来的辅助尺寸
    var originCenterX: Int = 0
    var originCenterY: Int = 0


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

    init {
        originCenterX = originLeft + originWidth / 2
        originCenterY = originTop + originHeight / 2
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

    override fun toString(): String {
        return "DragPhotoViewInfo(originLeft=$originLeft, originTop=$originTop, originWidth=$originWidth, originHeight=$originHeight, imageUrl='$imageUrl', imageResId=$imageResId, isClicked=$isClicked, originCenterX=$originCenterX, originCenterY=$originCenterY)"
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
