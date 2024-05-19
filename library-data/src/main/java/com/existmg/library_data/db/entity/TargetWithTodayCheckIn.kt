package com.existmg.library_data.db.entity

import androidx.room.Embedded
import androidx.room.Relation

/**
 * @Author ContentMy
 * @Date 2024/5/17 1:18 AM
 * @Description
 */
data class TargetWithTodayCheckIn(
    @Embedded
    val targetEntity: TargetEntity? = null,
    @Relation(
        parentColumn = "id",
        entityColumn = "targetId"
    )
    val targetCheckInEntity: TargetCheckInEntity? = null // 关联的打卡记录列表
)
