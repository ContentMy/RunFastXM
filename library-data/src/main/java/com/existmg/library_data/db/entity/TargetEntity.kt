package com.existmg.library_data.db.entity

import android.os.CountDownTimer
import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * @Author ContentMy
 * @Date 2024/4/8 12:22 AM
 * @Description 这里是目标模块的实体类
 */
@Entity(tableName = "target_table")//将Target标记为实体类，并且映射到数据中名为Target的表中
data class TargetEntity(
    @PrimaryKey(autoGenerate = true)
    val id:Int? = null,//用于标记实体类中的一个字段作为主键。主键是数据库表中用于唯一标识每一行记录的字段
    @ColumnInfo(name = "targetImg")
    val targetImg:String? = null,//用于目标icon的路径记录，这里是本地drawable的图片资源
    @ColumnInfo(name = "targetTitle")
    val targetTitle:String? = null,//目标的标题
    @ColumnInfo(name = "targetStartTime")
    val targetStartTime:Long = 0L,//目标的开始时间
    @ColumnInfo(name = "targetEndTime")
    val targetEndTime:Long = 0L,//目标的结束时间
    @ColumnInfo(name = "targetStatus")
    val targetStatus:Int = 0,//目标的状态（一天之内的状态：早晨、上午、中午、下午、晚上、深夜）
    @ColumnInfo(name = "targetContent")
    val targetContent:String? = null,//目标的内容/备注
    @ColumnInfo(name = "targetRemind")
    val targetRemind:Boolean = false//目标是否提醒，想要与Remind模块进行联动，期望结果为：如果选择了提醒，会在remind数据库增加一组对应的数据
    ): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readString(),
        parcel.readLong(),
        parcel.readLong(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readByte() != 0.toByte()
    ) {
    }

    override fun toString(): String {
        return "TargetEntity(id=$id, targetImg='$targetImg', targetTitle='$targetTitle', " +
                "targetStartTime=$targetStartTime, targetEndTime=$targetEndTime, " +
                "targetStatus=$targetStatus, targetContent='$targetContent', " +
                "targetRemind=$targetRemind)"
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(id)
        parcel.writeString(targetImg)
        parcel.writeString(targetTitle)
        parcel.writeLong(targetStartTime)
        parcel.writeLong(targetEndTime)
        parcel.writeInt(targetStatus)
        parcel.writeString(targetContent)
        parcel.writeByte(if (targetRemind) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<TargetEntity> {
        override fun createFromParcel(parcel: Parcel): TargetEntity {
            return TargetEntity(parcel)
        }

        override fun newArray(size: Int): Array<TargetEntity?> {
            return arrayOfNulls(size)
        }
    }
}