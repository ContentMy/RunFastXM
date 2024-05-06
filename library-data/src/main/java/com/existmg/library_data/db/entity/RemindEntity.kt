package com.existmg.library_data.db.entity

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * @Author ContentMy
 * @Date 2024/4/15 11:32 PM
 * @Description 这里是提醒模块的实体类
 */
@Entity(tableName = "remind_table")//将Remind标记为实体类，并且映射到数据中名为remind_table的表中
data class RemindEntity(
    @PrimaryKey(autoGenerate = true)
    val id:Int? = null,//用于标记实体类中的一个字段作为主键。主键是数据库表中用于唯一标识每一行记录的字段
    @ColumnInfo(name = "remindImg")
    val remindImg:String = "",
    @ColumnInfo(name = "remindTitle")
    val remindTitle:String = "",
    @ColumnInfo(name = "remindTime")
    val remindTime:Long = 0L):Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readLong()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(id)
        parcel.writeString(remindImg)
        parcel.writeString(remindTitle)
        parcel.writeLong(remindTime)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<RemindEntity> {
        override fun createFromParcel(parcel: Parcel): RemindEntity {
            return RemindEntity(parcel)
        }

        override fun newArray(size: Int): Array<RemindEntity?> {
            return arrayOfNulls(size)
        }
    }
}