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
 * @Date 2024/5/14 12:52 AM
 * @Description
 */
class RemindCompletedViewmodel(private var repository: RemindRepository) : BaseViewModel(){
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
                e.printStackTrace()
            }
        }
    }

    fun deleteRemind(remindEntity: RemindEntity){
        viewModelScope.launch {
            try {
                repository.deleteRemind(remindEntity)
            } catch (e: Throwable) {
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
                e.printStackTrace()
            }
        }
    }
}