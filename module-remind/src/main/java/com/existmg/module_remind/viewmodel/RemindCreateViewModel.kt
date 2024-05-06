package com.existmg.module_remind.viewmodel

import android.app.Application
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.existmg.library_base.viewmodel.BaseApplicationViewModel
import com.existmg.library_data.db.entity.RemindEntity
import com.existmg.library_data.repository.RemindRepository
import kotlinx.coroutines.launch

/**
 * @Author ContentMy
 * @Date 2024/4/27 6:38 PM
 * @Description 这里是创建提醒页面对应的viewmodel，主要是处理数据插入相关的操作
 */
class RemindCreateViewModel(private var remindRepository: RemindRepository, application: Application) : BaseApplicationViewModel(application) {
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

    fun insertRemind(){
        if (hasSendData.get()){//如果是能发送的情况，发送到
            viewModelScope.launch {
                try {
                    val remindEntity = RemindEntity(
                        remindTitle =  editTextContent.value!!,
                        remindTime = getTimeFromString(mTime.get()!!)
                    )
                    remindRepository.insertRemind(remindEntity)
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
}