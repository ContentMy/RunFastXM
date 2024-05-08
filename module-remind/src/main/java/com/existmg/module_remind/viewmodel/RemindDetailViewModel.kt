package com.existmg.module_remind.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.existmg.library_base.viewmodel.BaseViewModel
import com.existmg.library_data.db.entity.RemindEntity
import com.existmg.library_data.repository.RemindRepository
import kotlinx.coroutines.launch

/**
 * @Author ContentMy
 * @Date 2024/4/27 6:38 PM
 * @Description 这里是提醒倒计时详情页面的对应viewmodel
 */
class RemindDetailViewModel(private val repository: RemindRepository): BaseViewModel() {
    private var _remindById = MutableLiveData<RemindEntity>()
    val remindById:LiveData<RemindEntity> get() = _remindById
    fun queryRemindEntity(dataId:Int){
        viewModelScope.launch {
            try {
                val remindEntity = repository.getRemindById(dataId)
                _remindById.value = remindEntity
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}