package com.existmg.library_data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.existmg.library_data.db.entity.MemorandumEntity
import com.existmg.library_data.db.entity.MemorandumWithImagesEntity
import kotlinx.coroutines.flow.Flow

//import kotlinx.coroutines.flow.Flow

/**
 * @Author ContentMy
 * @Date 2024/4/9 5:07 PM
 * @Description 这里是记录生活模块对应的dao
 */
@Dao
interface MemorandumDao {
    // 插入目标
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMemorandum(memorandum: MemorandumEntity):Long

    // 插入多个目标
    @Insert
    suspend fun insertMemorandums(memorandums: List<MemorandumEntity>)

//    // 根据ID查询目标
//    @Query("SELECT * FROM memorandum_table WHERE id = :id")
//    suspend fun getMemorandumById(id: Int): MemorandumEntity

    // 查询所有目标
    @Query("SELECT * FROM memorandum_table")
    fun getAllMemorandums(): Flow<List<MemorandumEntity>>
    // 更新目标
    @Update
    suspend fun updateMemorandum(memorandum: MemorandumEntity)

    // 删除目标
    @Delete
    suspend fun deleteMemorandum(memorandum: MemorandumEntity)

    // 删除所有目标
    @Query("DELETE FROM memorandum_table")
    suspend fun deleteAllMemorandums()

    /*========================关联表的相关数据库操作=============================*/
    @Transaction
    @Query("SELECT * FROM memorandum_table")
    fun getAllMemorandumWithImg():Flow<List<MemorandumWithImagesEntity>>
}