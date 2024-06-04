package com.existmg.library_data.db.entity

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * @Author ContentMy
 * @Date 2024/4/8 12:22 AM
 * @Description 这里是记录生活模块的实体类
 */
@Entity(tableName = "memorandum_table")//将Memorandum标记为实体类，并且映射到数据中名为Memorandum的表中
data class MemorandumEntity(
    @PrimaryKey(autoGenerate = true)
    val id:Int? = null,//用于标记实体类中的一个字段作为主键。主键是数据库表中用于唯一标识每一行记录的字段
    @ColumnInfo(name = "memorandumIcon")
    val memorandumIcon:String? = null,//用于存放图标的drawable名称，获取时转成id，去drawable获取展示
    @ColumnInfo(name = "memorandumTitle")
    val memorandumTitle:String? = null,//使用var方便在xml中直接使用双向绑定来完成EditText的设置和获取值，如果是val则无法使用，需要通过另外的手段来实现参数的传递从而实现双向绑定
    @ColumnInfo(name = "memorandumContent")
    val memorandumContent:String? = null,//删除了内容的图片字段，单独使用另一个表来管理图片内容
    @ColumnInfo(name = "memorandumCreateTime")
    val memorandumCreateTime:Long = 0L,
    @ColumnInfo(name = "memorandumUpdateTime")
    val memorandumUpdateTime:Long = 0L,
    ): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readLong(),
        parcel.readLong()
    ) {
    }

    override fun toString(): String {
        return "memorandumEntity(id=$id, memorandumIcon='$memorandumIcon', memorandumTitle='$memorandumTitle'," +
                "memorandumContent='$memorandumContent'," +
                "memorandumCreateTime=$memorandumCreateTime,memorandumUpdateTime=$memorandumUpdateTime)"
    }
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(id)
        parcel.writeString(memorandumIcon)
        parcel.writeString(memorandumTitle)
        parcel.writeString(memorandumContent)
        parcel.writeLong(memorandumCreateTime)
        parcel.writeLong(memorandumUpdateTime)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<MemorandumEntity> {
        override fun createFromParcel(parcel: Parcel): MemorandumEntity {
            return MemorandumEntity(parcel)
        }

        override fun newArray(size: Int): Array<MemorandumEntity?> {
            return arrayOfNulls(size)
        }
    }
}