package com.existmg.module_remind.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.work.WorkManager
import com.existmg.library_base.viewmodel.BaseViewModel
import com.existmg.library_data.db.entity.RemindEntity
import com.existmg.library_data.repository.RemindRepository
import com.existmg.library_ui.notification.NotificationRepository
import com.existmg.module_remind.R
import com.existmg.module_remind.utils.logs.RemindLoggerManager
import com.existmg.module_remind.works.RemindStatusWork
import kotlinx.coroutines.launch

/**
 * @Author ContentMy
 * @Date 2024/5/14 12:52 AM
 * @Description
 */
class RemindCompletedViewmodel(
    private var repository: RemindRepository,
    private var notificationRepository: NotificationRepository,) : BaseViewModel(){
    private val mLog = RemindLoggerManager.getLogger<RemindCompletedViewmodel>()
    /*===================数据库的处理-开始=======================*/
    private val _remindCompleteData = MutableLiveData<List<RemindEntity>>()
    val remindCompleteData: LiveData<List<RemindEntity>> = _remindCompleteData
    fun refreshData(){
        viewModelScope.launch {
            try {
                repository.getAllCompletedReminds().collect{
                    _remindCompleteData.value = it
                }
            } catch (e: Throwable) {
                mLog.error("数据库获取已完成目标列表出现异常：",e)
                e.printStackTrace()
            }
        }
    }

    fun deleteRemind(remindEntity: RemindEntity){
        viewModelScope.launch {
            try {
                repository.deleteRemind(remindEntity)
            } catch (e: Throwable) {
                mLog.error("数据库删除单个提醒时出现异常：",e)
                e.printStackTrace()
            }
        }
    }

    fun deleteAllCompletedRemind(){
        viewModelScope.launch {
            try {
                repository.deleteAllCompletedReminds()
                _remindCompleteData.value = mutableListOf()
            } catch (e: Throwable) {
                mLog.error("数据库删除所有提醒出现异常：",e)
                e.printStackTrace()
            }
        }
    }

    private val _remindResetData = MutableLiveData<RemindEntity>()
    val remindResetData: LiveData<RemindEntity> = _remindResetData
    /**
     * @Author: ContentMy
     * @params: item 传入的需要更改状态的提醒实体类
     * @Description:
     * 重新提醒的逻辑：
     *  传入的提醒实体类是已完成提醒的实体类，并且id、startTime、time、title、content都是固定的，(还有一个考虑是否是将startTime更新为当前时间，但想到本身的含义，而且数据是属于更新而不是新创建的，所以使用不变的方案)
     *  而需要修改的数据就是endTime和status这两个参数。
     *  组合成新的数据对数据库的数据进行更新
     */
    fun resetRemind(item: RemindEntity) {
        viewModelScope.launch {
            try {
                val time = item.remindTime
                val updateEntity = RemindEntity(
                    id = item.id,
                    remindTitle = item.remindTitle,
                    remindContent = item.remindContent,
                    remindTime = item.remindTime,
                    remindStartTime = item.remindStartTime,
                    remindEndTime = System.currentTimeMillis()+time,
                    remindImg = item.remindImg,
                    remindCompleteStatus = false,

                )
                repository.updateRemind(updateEntity)
                _remindResetData.value = updateEntity//TODO：这里后续优化需要考虑数据库更新成功后再去更新livedata
            } catch (e: Throwable) {
                mLog.error("数据库更新单个提醒时出现异常：",e)
                e.printStackTrace()
            }
        }
    }

    //TODO:与RemindCreate共用到的逻辑，考虑后续抽离到一个模块封装的基类中
    fun startNotification(remindEntity: RemindEntity, application: Application){
        val dataId = if (remindEntity.id == null) 0 else remindEntity.id
        val iconResId = R.drawable.ui_remind //TODO：目前提醒模块的icon统一是使用remind图标，后续有改动的话，这里再进行动态获取
        val title = remindEntity.remindTitle!!
        val content = "提醒时间到了哦！"//TODO:remindContent后续开放功能时，这里需要进行动态获取，目前没有开放，所以给了一个默认的值
        val duration = remindEntity.remindTime
        mLog.debug("在创建提醒时，id为：$dataId")
        notificationRepository.postNotification(application, dataId!!,iconResId, title, content, duration)
    }

    fun startRemindStatusWork(remindEntity: RemindEntity, application: Application){
        val id = if (remindEntity.id == null) 0 else remindEntity.id
        val time = remindEntity.remindTime
        val endTime = remindEntity.remindEndTime
        mLog.debug("处理状态时的布尔值：${remindEntity.remindCompleteStatus}")
        //后启动后台定时任务去计算时间，在满足逻辑的情况下进行已完成提醒的状态修改
        val workRequest = RemindStatusWork.buildWorkRequest(id!!,time, endTime)
        WorkManager.getInstance(application).enqueue(workRequest)
    }
}