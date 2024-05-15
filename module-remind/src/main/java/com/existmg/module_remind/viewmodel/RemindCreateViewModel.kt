package com.existmg.module_remind.viewmodel

import android.app.Application
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.work.WorkManager
import com.existmg.library_base.viewmodel.BaseApplicationViewModel
import com.existmg.library_data.db.entity.RemindEntity
import com.existmg.library_data.repository.RemindRepository
import com.existmg.library_ui.notification.NotificationRepository
import com.existmg.module_remind.R
import com.existmg.module_remind.works.RemindStatusWork
import kotlinx.coroutines.launch

/**
 * @Author ContentMy
 * @Date 2024/4/27 6:38 PM
 * @Description 这里是创建提醒页面对应的viewmodel，主要是处理数据插入相关的操作
 */
class RemindCreateViewModel(
    private var remindRepository: RemindRepository,
    private var notificationRepository: NotificationRepository,
    application: Application) : BaseApplicationViewModel(application) {
    val editTextContent = MutableLiveData<String>()
    var mTime = ObservableField<String>()

    val hasSendData = ObservableBoolean().apply {
        this.set(false)
    }
    private val _editTextFocus = MutableLiveData<Boolean>()
    val editTextFocus: LiveData<Boolean>
        get() = _editTextFocus

    fun setEditTextFocus(hasFocus: Boolean){
        _editTextFocus.value = hasFocus
    }

    fun setTime(time:String){
        mTime.set(time)
    }

    private val _remind = MutableLiveData<RemindEntity>()
    var remind:LiveData<RemindEntity>  = _remind
    fun insertRemind(){
        if (hasSendData.get()){//如果是能发送的情况，发送到
            viewModelScope.launch {
                try {
                    val remindTime = getTimeFromString(mTime.get()!!)
//                    val remindTime = 30 * 1000L //TODO:这里写死是为了测试
                    val remindStartTime = System.currentTimeMillis()
                    val remindEntity = RemindEntity(
                        remindTitle =  editTextContent.value!!,
                        remindTime = remindTime,
                        remindStartTime =  System.currentTimeMillis(),
                        remindEndTime = remindStartTime + remindTime
                    )
                    println("入库前：${remindEntity.remindCompleteStatus}")
                    //这里得到的id是long，原因是因为insert注解返回long类型是id，int类型是表的行号，
                    // 并且自增长的主键定义是int型的，所以在查询是将long转为int也不会丢失精度导致数据错误
                    val remindLongId = remindRepository.insertRemind(remindEntity)//已解决
                    val remindUpdateEntity = remindRepository.getRemindById(remindLongId.toInt())
                    println("${remindUpdateEntity.remindCompleteStatus}")
                    _remind.value = remindUpdateEntity //TODO:这里是问题所在，id在传递的一开始就为null，方案是去dao中返回id，但是是异步的，需要考虑怎么进行优化。已解决，后续Beta版本结束统一收集问题时清除
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun getTimeFromString(timeString:String):Long{
        return when(timeString){
            "5分钟"-> 5 * 60 * 1000
            "10分钟"-> 10 * 60 * 1000
            "15分钟"-> 15 * 60 * 1000
            "20分钟"-> 20 * 60 * 1000
            "25分钟"-> 25 * 60 * 1000
            "30分钟"-> 30 * 60 * 1000
            "45分钟"-> 45 * 60 * 1000
            "1小时"-> 60 * 60 * 1000
            else -> 0
        }
    }

    /*================通知操作 - 开始===================*/

    fun startNotification(remindEntity: RemindEntity, application: Application){
        val dataId = if (remindEntity.id == null) 0 else remindEntity.id
        val iconResId = R.drawable.ui_remind //TODO：目前提醒模块的icon统一是使用remind图标，后续有改动的话，这里再进行动态获取
        val title = remindEntity.remindTitle!!
        val content = "提醒时间到了哦！"
        val duration = remindEntity.remindTime
        println("在创建提醒时，id为：$dataId")
        notificationRepository.postNotification(application, dataId!!,iconResId, title, content, duration)
    }

    fun cancelNotification(application:Application){
        notificationRepository.cancelNotification(application)
    }

    /*================通知操作 - 结束===================*/

    /*================提醒状态的处理 - 开始===================*/

    fun startRemindStatusWork(remindEntity: RemindEntity, application: Application){
        val id = if (remindEntity.id == null) 0 else remindEntity.id
        val time = remindEntity.remindTime
        val endTime = remindEntity.remindEndTime
        println("处理状态时的布尔值：${remindEntity.remindCompleteStatus}")
        //后启动后台定时任务去计算时间，在满足逻辑的情况下进行已完成提醒的状态修改
        val workRequest = RemindStatusWork.buildWorkRequest(id!!,time, endTime)
        WorkManager.getInstance(application).enqueue(workRequest)
    }

    /*================提醒状态的处理 - 开始===================*/
}