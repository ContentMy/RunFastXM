package com.existmg.library_data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.existmg.library_data.db.entity.TargetEntity
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

//    // 根据ID查询目标
//    @Query("SELECT * FROM target_table WHERE id = :id")
//    suspend fun getTargetById(id: Int): TargetEntity

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
}