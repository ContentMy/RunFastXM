package com.existmg.library_data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.existmg.library_data.db.entity.RemindEntity
import kotlinx.coroutines.flow.Flow

/**
 * @Author ContentMy
 * @Date 2024/4/15 11:55 PM
 * @Description 这里是提供模块对应的dao
 */
@Dao
interface RemindDao {
    // 插入目标
    @Insert(onConflict = OnConflictStrategy.REPLACE)//定义插入数据库时冲突的策略，如果插入的数据与数据库中已存在的数据发生冲突，则用新数据替换数据库中的已存在数据
    suspend fun insertRemind(remind: RemindEntity):Long//返回RemindEntity，在插入成功后需要使用其中的自增长主键

    // 插入多个目标
    @Insert
    suspend fun insertReminds(reminds: List<RemindEntity>)

    // 根据ID查询目标
    @Query("SELECT * FROM remind_table WHERE id = :id")
    suspend fun getRemindById(id: Int): RemindEntity

    // 查询所有目标
    @Query("SELECT * FROM remind_table")
    fun getAllReminds(): Flow<List<RemindEntity>>
    // 更新目标
    @Update
    suspend fun updateRemind(remind: RemindEntity)

    // 删除目标
    @Delete
    suspend fun deleteRemind(remind: RemindEntity)

    // 删除所有目标
    @Query("DELETE FROM remind_table")
    suspend fun deleteAllReminds()
}