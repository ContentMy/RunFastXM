package com.existmg.library_data.repository

import com.existmg.library_data.db.dao.MemorandumDao
import com.existmg.library_data.db.entity.MemorandumEntity
import kotlinx.coroutines.flow.Flow

/**
 * @Author ContentMy
 * @Date 2024/4/11 12:17 AM
 * @Description 这里是记录生活模块的数据库操作的封装类
 */
//@AndroidEntryPoint
class MemorandumRepository(private val  memorandumDao: MemorandumDao) {
    suspend fun insertMemorandum( memorandum: MemorandumEntity){
         memorandumDao.insertMemorandum( memorandum)
    }

    suspend fun insertMemorandums( memorandums: List<MemorandumEntity>){
         memorandumDao.insertMemorandums( memorandums)
    }

//    suspend fun getMemorandumById(id: Int): MemorandumEntity{
//       return  memorandumDao.getMemorandumById(id)
//    }

    fun getAllMemorandums(): Flow<List<MemorandumEntity>>{
        return  memorandumDao.getAllMemorandums()
    }

    suspend fun updateMemorandum( memorandum: MemorandumEntity){
         memorandumDao.updateMemorandum( memorandum)
    }

    suspend fun deleteMemorandum( memorandum: MemorandumEntity){
         memorandumDao.deleteMemorandum( memorandum)
    }

    suspend fun deleteAllMemorandums(){
         memorandumDao.deleteAllMemorandums()
    }
}