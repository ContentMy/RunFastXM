package com.existmg.library_data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.existmg.library_data.db.entity.TargetEntity
import com.existmg.library_data.db.entity.TargetWithCheckInList
import com.existmg.library_data.db.entity.TargetWithTodayCheckIn
import kotlinx.coroutines.flow.Flow

//import kotlinx.coroutines.flow.Flow

/**
 * @Author ContentMy
 * @Date 2024/4/9 5:07 PM
 * @Description 这里是目标模块对应的dao
 */
@Dao
interface TargetDao {
    // 插入目标
    @Insert
    suspend fun insertTarget(target: TargetEntity)

    // 插入多个目标
    @Insert
    suspend fun insertTargets(targets: List<TargetEntity>)

    // 根据ID查询目标
    @Query("SELECT * FROM target_table WHERE id = :id")
    suspend fun getTargetById(id: Int): TargetEntity

    // 查询所有目标
    @Query("SELECT * FROM target_table")
    fun getAllTargets(): Flow<List<TargetEntity>>
    // 更新目标
    @Update
    suspend fun updateTarget(target: TargetEntity)

    // 删除目标
    @Delete
    suspend fun deleteTarget(target: TargetEntity)

    // 删除所有目标
    @Query("DELETE FROM target_table")
    suspend fun deleteAllTargets()


    /**
     * @Author: ContentMy
     * @params: targetId 目标id
     * @return: 返回查询目标的所有打卡记录
     * @Description: 这是一个关联表查询，根据目标id查询他的所有打卡记录并返回
     *
     */
    @Deprecated(
        message = "这个是在TargetDao中声明的，另外还有在打卡类对应的dao中也有一份查询操作，那边是单表就可以查询完成，所以弃用这个方法",
        replaceWith = ReplaceWith("targetCheckInDao.getTargetCheckInEntityListByTargetId()")
        )
    @Transaction
    @Query("SELECT * FROM target_table WHERE id = :targetId")
    fun getTargetWithCheckInList(targetId: Int): Flow<TargetWithCheckInList>

   /**
    * @Author: ContentMy
    * @params:
    *       startOfDay  当天开始的时间
    *       endOfDay    当天结束的时间
    * @return:
    *       返回当天所有目标以及他们的打卡情况的集合
    * @Description: 这是关联表查询，根据当天的时间查询返回当天的目标以及打卡情况
    */
    @Transaction//这是关联表查询。为了确保操作在一个事务中执行
    //"""用于多行文本，可以直接写出多行内容而不需要转义字符。对复杂查询非常有帮助，因为它避免了每行都需要使用 \n 来表示换行。
    @Query("""
        SELECT t.*, c.id AS targetId, c.targetCheckIn, c.targetCheckInTime 
        FROM target_table AS t 
        LEFT JOIN target_check_in_table AS c 
        ON t.id = c.targetId AND c.targetCheckInTime >= :startOfDay AND c.targetCheckInTime < :endOfDay
    """)
    fun getAllTargetsWithTodayCheckIn(startOfDay: Long, endOfDay: Long): Flow<List<TargetWithTodayCheckIn>>

}