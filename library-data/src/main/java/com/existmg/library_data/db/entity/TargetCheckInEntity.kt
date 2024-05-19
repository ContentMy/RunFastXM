package com.existmg.library_data.db.entity

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * @Author ContentMy
 * @Date 2024/5/16 1:14 AM
 * @Description 这里是目标打卡的实体类，与目标实体类是属于一对多的关系，一个目标可以有多次打卡
 */
@Entity(tableName = "target_check_in_table")//将Target标记为实体类，并且映射到数据中名为Target的表中
data class TargetCheckInEntity(
    @PrimaryKey(autoGenerate = true)
    val id:Int? = null,//用于标记实体类中的一个字段作为主键。主键是数据库表中用于唯一标识每一行记录的字段
    @ColumnInfo(name = "targetId")
    val targetId:Int = 0,//targetEntity的id,用于关联表的绑定
    @ColumnInfo(name = "targetCheckIn")
    val targetCheckIn:Boolean = false,//目标打卡状态
    @ColumnInfo(name = "targetCheckInTime")
    val targetCheckInTime:Long = 0L,//目标的打卡时间
): Parcelable{
    constructor(parcel: Parcel) : this(
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readInt(),
        parcel.readByte() != 0.toByte(),
        parcel.readLong()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(id)
        parcel.writeInt(targetId)
        parcel.writeByte(if (targetCheckIn) 1 else 0)
        parcel.writeLong(targetCheckInTime)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<TargetCheckInEntity> {
        override fun createFromParcel(parcel: Parcel): TargetCheckInEntity {
            return TargetCheckInEntity(parcel)
        }

        override fun newArray(size: Int): Array<TargetCheckInEntity?> {
            return arrayOfNulls(size)
        }
    }

}
