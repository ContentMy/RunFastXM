package com.existmg.library_data.db.entity

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Relation

/**
 * @Author ContentMy
 * @Date 2024/4/8 12:22 AM
 * @Description 这里是记录生活模块的实体类
 */
data class MemorandumWithImagesEntity(
    @Embedded
    val memorandumEntity: MemorandumEntity? = null,
    @Relation(
        parentColumn = "id",
        entityColumn = "memorandumId"
    )
    val memorandumImgEntityList: List<MemorandumImgEntity>? = null // 关联的图片列表
):Parcelable{
    constructor(parcel: Parcel) : this(
        parcel.readParcelable(MemorandumEntity::class.java.classLoader),
        parcel.createTypedArrayList(MemorandumImgEntity)
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeParcelable(memorandumEntity, flags)
        parcel.writeTypedList(memorandumImgEntityList)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<MemorandumWithImagesEntity> {
        override fun createFromParcel(parcel: Parcel): MemorandumWithImagesEntity {
            return MemorandumWithImagesEntity(parcel)
        }

        override fun newArray(size: Int): Array<MemorandumWithImagesEntity?> {
            return arrayOfNulls(size)
        }
    }

}