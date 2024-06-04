package com.existmg.library_data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.existmg.library_data.db.entity.TargetCheckInEntity
import com.existmg.library_data.db.entity.TargetWithTodayCheckIn
import kotlinx.coroutines.flow.Flow

/**
 * @Author ContentMy
 * @Date 2024/5/17 4:38 PM
 * @Description
 */
@Dao
interface TargetCheckInDao {
    // 插入打卡记录
    @Insert
    suspend fun insertTargetCheckInEntity(targetCheckInEntity: TargetCheckInEntity)

    // 更新打卡记录
    @Update
    suspend fun updateTargetCheckInEntity(targetCheckInEntity: TargetCheckInEntity)

    // 删除打卡记录
    @Delete
    suspend fun deleteTargetCheckInEntity(targetCheckInEntity: TargetCheckInEntity)

    @Query("DELETE FROM target_check_in_table WHERE targetId = :targetId")
    suspend fun deleteTargetCheckInWithId(targetId: Int)
    /**
     * @Author: ContentMy
     * @params: targetId 目标id
     * @return: 返回查询目标的所有打卡记录
     * @Description: 目标所有打卡记录的数据库查询操作，根据目标id查询他的所有打卡记录并返回
     */
    @Query("SELECT * FROM target_check_in_table WHERE targetId = :targetId")
    fun getTargetCheckInEntityListByTargetId(targetId: Int): Flow<List<TargetCheckInEntity>>

    @Deprecated("这个是为了测试添加的相关代码")
    @Query("SELECT * FROM target_check_in_table")
    suspend fun getAllCheckIns(): List<TargetCheckInEntity>

    @Deprecated("这个是为了测试添加的相关代码")
    @Query("SELECT * FROM target_check_in_table WHERE targetCheckInTime >= :startOfDay AND targetCheckInTime < :endOfDay")
    suspend fun getCheckInsForToday(startOfDay: Long, endOfDay: Long): List<TargetCheckInEntity>

}