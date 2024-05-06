package com.existmg.library_data.repository

import com.existmg.library_data.db.dao.TargetDao
import com.existmg.library_data.db.entity.TargetEntity
import kotlinx.coroutines.flow.Flow

/**
 * @Author ContentMy
 * @Date 2024/4/11 12:17 AM
 * @Description 这里是目标模块的数据库操作的封装类
 */
//@AndroidEntryPoint
class TargetRepository(private val targetDao: TargetDao) {
    suspend fun insertTarget(target: TargetEntity){
        targetDao.insertTarget(target)
    }

    suspend fun insertTargets(targets: List<TargetEntity>){
        targetDao.insertTargets(targets)
    }

//    suspend fun getTargetById(id: Int): TargetEntity{
//       return targetDao.getTargetById(id)
//    }

    fun getAllTargets(): Flow<List<TargetEntity>>{
        return targetDao.getAllTargets()
    }

    suspend fun updateTarget(target: TargetEntity){
        targetDao.updateTarget(target)
    }

    suspend fun deleteTarget(target: TargetEntity){
        targetDao.deleteTarget(target)
    }

    suspend fun deleteAllTargets(){
        targetDao.deleteAllTargets()
    }
}