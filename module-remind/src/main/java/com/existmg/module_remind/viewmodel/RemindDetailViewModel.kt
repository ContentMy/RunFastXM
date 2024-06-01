package com.existmg.module_remind.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.existmg.library_base.viewmodel.BaseViewModel
import com.existmg.library_data.db.entity.RemindEntity
import com.existmg.library_data.repository.RemindRepository
import com.existmg.module_remind.utils.logs.RemindLoggerManager
import kotlinx.coroutines.launch

/**
 * @Author ContentMy
 * @Date 2024/4/27 6:38 PM
 * @Description 这里是提醒倒计时详情页面的对应viewmodel
 */
class RemindDetailViewModel(private val repository: RemindRepository): BaseViewModel() {
    private val mLog = RemindLoggerManager.getLogger<RemindDetailViewModel>()
    private var _remindById = MutableLiveData<RemindEntity>()
    val remindById:LiveData<RemindEntity> get() = _remindById
    fun queryRemindEntity(dataId:Int){
        viewModelScope.launch {
            try {
                val remindEntity = repository.getRemindById(dataId)
                initData(remindEntity)//去调用Time相关的逻辑，判断并设置对应的ui
                _remindById.value = remindEntity
            } catch (e: Exception) {
                mLog.error("数据库查询单个提醒时出现异常：",e)
                e.printStackTrace()
            }
        }
    }

    /**
     * @Author: ContentMy
     * @params: remindData 传入的提醒实体类
     * @Description:
     * 这个方法主要是对初始化时传入实体类的数据进行逻辑判断，根据对应的数据进行展示
     * 从这个方法传入的数据中的结束时间是一定要比当前时间晚的，因为是从列表中点击对应item进入的页面
     * 如果提醒时间到达，那么会认为提醒已经完成，不会在当前页面列表中展示
     */
    private var _remindShowTime = MutableLiveData<Long>()
    val remindShowTime:LiveData<Long> get() = _remindShowTime
    fun initData(remindData: RemindEntity) {
        val endTime = remindData.remindEndTime
        val showTime = endTime - System.currentTimeMillis()
        _remindShowTime.value = showTime
    }
}