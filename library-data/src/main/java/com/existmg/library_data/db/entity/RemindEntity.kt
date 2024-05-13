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
    @ColumnInfo(name = "remindImg", defaultValue = "")
    val remindImg:String? = null,//提醒展示列表的icon，目前是写死的铃铛icon，后续如果动态添加可以使用此字段
    @ColumnInfo(name = "remindTitle", defaultValue = "")
    val remindTitle:String? = null,//提醒标题字段
    @ColumnInfo(name = "remindContent", defaultValue = "")
    val remindContent:String? = null,//提醒内容字段（目前的展示没有使用，后续可能会增加到详情页和通知内容中）Beta2新增字段
    @ColumnInfo(name = "remindTime")
    val remindTime:Long = 0L,//提醒时间字段，存储的是间隔时间长度，比如10分钟就是10*60*1000
    @ColumnInfo(name = "remindStartTime")
    val remindStartTime:Long= 0L,//提醒开始时间字段，存储的是提醒创建的时间戳
    @ColumnInfo(name = "remindEndTime")
    val remindEndTime:Long = 0L,//提醒结束时间字段，存储的是提醒创建的时间戳+remindTime计算出的结束时间的时间戳
    @ColumnInfo(name = "remindCompleteStatus")//如果更新后，所有存在的提醒会默认值为已提醒，因为从已有的逻辑中无法在数据库升级过程中判断是否已提醒
    val remindCompleteStatus:Boolean = false //提醒的状态（未提醒和已提醒，用来查询时区分展示不同的列表内容）Beta2新增字段
    ):Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readLong(),
        parcel.readLong(),
        parcel.readLong(),
        parcel.readByte() != 0.toByte()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(id)
        parcel.writeString(remindImg)
        parcel.writeString(remindTitle)
        parcel.writeString(remindContent)
        parcel.writeLong(remindTime)
        parcel.writeLong(remindStartTime)
        parcel.writeLong(remindEndTime)
        parcel.writeByte(if (remindCompleteStatus) 1 else 0)
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