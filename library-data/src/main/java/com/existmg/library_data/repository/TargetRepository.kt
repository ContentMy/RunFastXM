package com.existmg.library_data.repository

import androidx.room.Query
import com.existmg.library_data.db.dao.TargetCheckInDao
import com.existmg.library_data.db.dao.TargetDao
import com.existmg.library_data.db.entity.TargetCheckInEntity
import com.existmg.library_data.db.entity.TargetEntity
import com.existmg.library_data.db.entity.TargetWithCheckInList
import com.existmg.library_data.db.entity.TargetWithTodayCheckIn
import kotlinx.coroutines.flow.Flow

/**
 * @Author ContentMy
 * @Date 2024/4/11 12:17 AM
 * @param
 *          targetDao       目标的数据库访问对象
 *          targetCheckDao  目标打卡的数据库访问对象
 * @Description 这里是目标模块的数据库操作的封装类
 */
//@AndroidEntryPoint
class TargetRepository(
    private val targetDao: TargetDao,
    private val targetCheckInDao: TargetCheckInDao) {


    /*========================目标数据的相关数据库操作=============================*/
    /**
     * @Author: ContentMy
     * @params: target 目标的实体类
     * @Description: 单个目标的数据库插入操作，传入了封装完成数据的目标的实体类
     */
    suspend fun insertTarget(target: TargetEntity){
        targetDao.insertTarget(target)
    }

    /**
     * @Author: ContentMy
     * @params: targets 目标实体类集合
     * @Description: 多个目标的数据库插入操作，传入封装完成数据的目标实体类集合
     */
    suspend fun insertTargets(targets: List<TargetEntity>){
        targetDao.insertTargets(targets)
    }

    /**
     * @Author: ContentMy
     * @params: id 想要查询的目标的id
     * @return: 返回对应的目标实体类
     * @Description: 单个目标的数据库查询操作，根据提供的数据实体类id查询对应的数据
     */
    suspend fun getTargetById(id: Int): TargetEntity{
       return targetDao.getTargetById(id)
    }

    /**
     * @Author: ContentMy
     * @return: 返回对应的目标实体类集合
     * @Description: 所有目标的数据库查询操作，查询对应的数据集合
     */
    fun getAllTargets(): Flow<List<TargetEntity>>{
        return targetDao.getAllTargets()
    }

    /**
     * @Author: ContentMy
     * @params: target 想要更新的目标的实体类
     * @Description: 更新目标的数据库更新操作，根据提供的目标实体类更新对应的数据
     */
    suspend fun updateTarget(target: TargetEntity){
        targetDao.updateTarget(target)
    }

    /**
     * @Author: ContentMy
     * @params: target 想要删除的目标实体类
     * @Description: 单个目标删除的数据库删除操作，根据提供的目标实体类删除对应的数据
     */
    suspend fun deleteTarget(target: TargetEntity){
        targetDao.deleteTarget(target)
    }

    /**
     * @Author: ContentMy
     * @Description: 所有目标的数据库删除操作
     */
    suspend fun deleteAllTargets(){
        targetDao.deleteAllTargets()
    }

    /*========================目标打卡记录的相关数据库操作=============================*/

    /**
     * @Author: ContentMy
     * @params: targetCheckInEntity 打卡记录的实体类
     * @Description: 目标打卡记录的数据库插入操作，根据提供的实体类插入对应的数据
     */
    suspend fun insertTargetCheckInEntity(targetCheckInEntity: TargetCheckInEntity) {
        targetCheckInDao.insertTargetCheckInEntity(targetCheckInEntity)
    }

    /**
     * @Author: ContentMy
     * @params: targetCheckInEntity 打卡记录的实体类
     * @Description: 目标打卡记录的数据库更新操作，根据提供的实体类更新对应的数据
     */
    suspend fun updateTargetCheckInEntity(targetCheckInEntity: TargetCheckInEntity) {
        targetCheckInDao.updateTargetCheckInEntity(targetCheckInEntity)
    }

    /**
     * @Author: ContentMy
     * @params: targetCheckInEntity 打卡记录的实体类
     * @Description: 目标打卡记录的数据库删除操作，根据提供的实体类删除对应的数据
     */
    suspend fun deleteTargetCheckInEntity(targetCheckInEntity: TargetCheckInEntity) {
        targetCheckInDao.deleteTargetCheckInEntity(targetCheckInEntity)
    }


    suspend fun deleteTargetCheckInWithTargetId(id: Int) {//TODO:id如果变为long，这里也要进行更新
        targetCheckInDao.deleteTargetCheckInWithId(id)
    }

    /**
     * @Author: ContentMy
     * @params: targetId 目标id
     * @return: 返回查询目标的所有打卡记录
     * @Description: 目标所有打卡记录的数据库查询操作，根据目标id查询他的所有打卡记录并返回
     */
    fun getTargetCheckInEntityListByTargetId(targetId: Int): Flow<List<TargetCheckInEntity>> {
        return targetCheckInDao.getTargetCheckInEntityListByTargetId(targetId)
    }

    /**
     * @Author: ContentMy
     * @return: 返回所有打卡记录
     * @Description: 目标打卡记录的数据库查询操作
     */
    @Deprecated("这个是为了测试添加的相关代码")
    suspend fun getAllCheckIns(): List<TargetCheckInEntity>{
        return targetCheckInDao.getAllCheckIns()
    }

    /**
     * @Author: ContentMy
     * @params:
     *       startOfDay  当天开始的时间
     *       endOfDay    当天结束的时间
     * @return:
     *       返回当天所有的打卡情况的集合
     * @Description:
     */
    @Deprecated("这个是为了测试添加的相关代码")
    suspend fun getCheckInsForToday(startOfDay: Long, endOfDay: Long): List<TargetCheckInEntity>{
        return targetCheckInDao.getCheckInsForToday(startOfDay, endOfDay)
    }

    /*========================关联表的相关数据库操作=============================*/
    /**
     * @Author: ContentMy
     * @params:
     *       startOfDay  当天开始的时间
     *       endOfDay    当天结束的时间
     * @return:
     *       返回当天所有目标以及他们的打卡情况的集合
     * @Description: 这是关联表查询，根据当天的时间查询返回当天的目标以及打卡情况
     */
    fun getAllTargetsWithTodayCheckIn(startOfDay: Long, endOfDay: Long): Flow<List<TargetWithTodayCheckIn>> {
        //TODO：bug.并联表查询当天目标的打卡记录时，会在未打卡的时间里显示已打卡，具体如下，
        // 创建新目标->进行打卡（显示已打卡）-> 杀死应用，更改系统时间一天后 -> 重新进入应用，查询目标显示已打卡（正常应该是未打卡）
        // 增加日志想排查问题，结果加入以下堆栈数据时，数据正常展示了显示未打卡。目前怀疑是room的缓存问题，后续需求完成后，需要进行复现以及查找问题
        return targetDao.getAllTargetsWithTodayCheckIn(startOfDay, endOfDay)
    }

}