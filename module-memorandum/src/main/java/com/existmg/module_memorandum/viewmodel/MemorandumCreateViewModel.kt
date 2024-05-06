package com.existmg.module_memorandum.viewmodel

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.existmg.library_base.viewmodel.BaseApplicationViewModel
import com.existmg.library_data.db.entity.MemorandumEntity
import com.existmg.library_data.repository.MemorandumRepository
import com.existmg.module_memorandum.R
import kotlinx.coroutines.launch

/**
 * @Author ContentMy
 * @Date 2024/4/28 11:18 PM
 * @Description 新建心情页面对应的viewmodel，做了数据入库的一些操作
 */
class MemorandumCreateViewModel(
    private var repository:MemorandumRepository,
    application: Application):BaseApplicationViewModel(application) {

    var memorandumTitleString = MutableLiveData<String>()

    var memorandumContentString = MutableLiveData<String>()

    private var _finishActivity = MutableLiveData<Boolean>()
    val finishActivity:LiveData<Boolean> get() = _finishActivity
    fun insertMemorandum(){
        if (memorandumTitleString.value.isNullOrEmpty()){
            Toast.makeText(getApplication(), "记录下心情才能保存哦", Toast.LENGTH_SHORT).show()
            return
        }
        viewModelScope.launch {
            try {
                val memorandumEntity = MemorandumEntity(
                    memorandumTitle = memorandumTitleString.value!!,
                    memorandumContent = if(memorandumContentString.value == null) "" else memorandumContentString.value!!,
                    memorandumCreateTime = System.currentTimeMillis()
                )
                repository.insertMemorandum(memorandumEntity)
                _finishActivity.value = true
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}