package com.existmg.module_main.viewmodel

import android.os.Parcel
import android.os.Parcelable

/**
 * @Author ContentMy
 * @Date 2024/3/22 11:09 上午
 * @Description
 */
class MainDataModel(private val title: String, private val description: String):Parcelable{
    // 私有构造函数，用于从Parcel中反序列化对象
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: ""
//        parcel.readInt()
    ) {
    }
    // 实现writeToParcel方法，将对象写入Parcel
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(title)
        parcel.writeString(description)
    }
    // 描述内容的大小
    override fun describeContents(): Int {
        return 0
    }
    // 创建CREATOR实例，用于从Parcel中反序列化对象
    companion object CREATOR : Parcelable.Creator<MainDataModel> {
        override fun createFromParcel(parcel: Parcel): MainDataModel {
            return MainDataModel(parcel)
        }

        override fun newArray(size: Int): Array<MainDataModel?> {
            return arrayOfNulls(size)
        }
    }

}