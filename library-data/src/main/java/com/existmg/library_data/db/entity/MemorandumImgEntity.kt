package com.existmg.library_data.db.entity

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * @Author ContentMy
 * @Date 2024/4/8 12:22 AM
 * @Description 这里是记录生活模块的实体类
 */
@Entity(
    tableName = "memorandum_img_table",
    foreignKeys = [ForeignKey(
        entity = MemorandumEntity::class,
        parentColumns = ["id"],
        childColumns = ["memorandumId"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index(value = ["memorandumId"])]
)//将Memorandum标记为实体类，并且映射到数据中名为Memorandum的表中
data class MemorandumImgEntity(
    @PrimaryKey(autoGenerate = true)
    val id:Int? = null,//用于标记实体类中的一个字段作为主键。主键是数据库表中用于唯一标识每一行记录的字段
    @ColumnInfo(name = "memorandumId")
    val memorandumId:Int = 0,//memorandumEntity的id，用于关联表
    @ColumnInfo(name = "memorandumImgFilePath")
    val memorandumImgFilePath:String? = null//图片的地址
    ): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readInt(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(id)
        parcel.writeInt(memorandumId)
        parcel.writeString(memorandumImgFilePath)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<MemorandumImgEntity> {
        override fun createFromParcel(parcel: Parcel): MemorandumImgEntity {
            return MemorandumImgEntity(parcel)
        }

        override fun newArray(size: Int): Array<MemorandumImgEntity?> {
            return arrayOfNulls(size)
        }
    }
}