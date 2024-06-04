package com.existmg.library_data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.existmg.library_data.db.entity.MemorandumImgEntity

/**
 * @Author ContentMy
 * @Date 2024/5/20 4:15 PM
 * @Description
 */
@Dao
interface MemorandumImgDao {
    // 插入图片
    @Insert
    suspend fun insertMemorandumImg(memorandumImg: MemorandumImgEntity)

    // 插入多个图片
    @Insert
    suspend fun insertMemorandumImgList(memorandumImgList: List<MemorandumImgEntity>)

    // 查询所有图片
    @Query("SELECT * FROM memorandum_img_table WHERE memorandumId = :memorandumId")
    suspend fun getImagesForMemorandum(memorandumId: Int): List<MemorandumImgEntity>
    // 更新图片
    @Update
    suspend fun updateMemorandumImg(memorandumImg: MemorandumImgEntity)

    // 删除图片
    @Delete
    suspend fun deleteMemorandumImg(memorandumImg: MemorandumImgEntity)

    // 删除所有图片
    @Query("DELETE FROM memorandum_img_table WHERE memorandumId = :memorandumId")
    suspend fun deleteAllMemorandums(memorandumId: Int)
}