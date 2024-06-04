package com.existmg.library_data.repository

import androidx.room.Transaction
import com.existmg.library_data.db.dao.MemorandumDao
import com.existmg.library_data.db.dao.MemorandumImgDao
import com.existmg.library_data.db.entity.MemorandumEntity
import com.existmg.library_data.db.entity.MemorandumImgEntity
import com.existmg.library_data.db.entity.MemorandumWithImagesEntity
import kotlinx.coroutines.flow.Flow

/**
 * @Author ContentMy
 * @Date 2024/4/11 12:17 AM
 * @Description 这里是记录生活模块的数据库操作的封装类
 */
//@AndroidEntryPoint
class MemorandumRepository(
    private val  memorandumDao: MemorandumDao,
    private val  memorandumImgDao: MemorandumImgDao
) {
    suspend fun insertMemorandum( memorandum: MemorandumEntity):Long{
         return memorandumDao.insertMemorandum( memorandum)
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


    suspend fun insertMemorandumImg(memorandumImgEntity: MemorandumImgEntity){
        memorandumImgDao.insertMemorandumImg(memorandumImgEntity)
    }



    /*============================关联表处理===============================*/
    @Transaction
    suspend fun insertMemorandumWithImg(memorandumEntity: MemorandumEntity,imgList: List<MemorandumImgEntity>){
        val memorandumId = memorandumDao.insertMemorandum(memorandumEntity).toInt()
        imgList.forEach {
            val memorandumImgEntity = MemorandumImgEntity(
                memorandumId = memorandumId,
                memorandumImgFilePath = it.memorandumImgFilePath
            )
            insertMemorandumImg(memorandumImgEntity)
        }
    }

    fun getAllMemorandumWithImg():Flow<List<MemorandumWithImagesEntity>>{
        return memorandumDao.getAllMemorandumWithImg()
    }
}