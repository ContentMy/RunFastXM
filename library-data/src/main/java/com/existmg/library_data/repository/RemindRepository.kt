package com.existmg.library_data.repository

import com.existmg.library_data.db.dao.RemindDao
import com.existmg.library_data.db.entity.RemindEntity
import kotlinx.coroutines.flow.Flow

/**
 * @Author ContentMy
 * @Date 2024/4/11 12:17 AM
 * @Description 这里是提醒模块的数据库操作的封装类
 */
//@AndroidEntryPoint
class RemindRepository(private val remindDao: RemindDao) {
    suspend fun insertRemind(remind: RemindEntity):Long{
        return remindDao.insertRemind(remind)
    }

    suspend fun insertReminds(reminds: List<RemindEntity>){
        remindDao.insertReminds(reminds)
    }

    suspend fun getRemindById(id: Int): RemindEntity{
       return remindDao.getRemindById(id)
    }

    fun getAllReminds(): Flow<List<RemindEntity>>{
        return remindDao.getAllReminds()
    }

    fun getAllInProgressReminds(): Flow<List<RemindEntity>>{
        return remindDao.getAllInProgressReminds()
    }
    fun getAllCompletedReminds(): Flow<List<RemindEntity>>{
        return remindDao.getAllCompletedReminds()
    }

    suspend fun updateRemind(remind: RemindEntity){
        remindDao.updateRemind(remind)
    }

    suspend fun deleteRemind(remind: RemindEntity){
        remindDao.deleteRemind(remind)
    }

    suspend fun deleteAllReminds(){
        remindDao.deleteAllReminds()
    }

    suspend fun deleteAllCompletedReminds(){
        remindDao.deleteAllReminds()
    }
}