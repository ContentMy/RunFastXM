package com.existmg.library_ui.dialog.viewmodel

import android.database.Observable
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import java.util.Objects

/**
 * @Author ContentMy
 * @Date 2024/4/25 10:42 AM
 * @Description
 */
internal class DialogMemorandumViewModel:ViewModel(){
    val editTextContent = MutableLiveData<String>().apply {
        ""
    }
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

    private val _processData = MutableLiveData<Map<String,Any>>()
    val processData: LiveData<Map<String,Any>>
        get() = _processData

    fun setTime(time:String){
        mTime.set(time)
    }
    fun setProcessData(){
        if (hasSendData.get()){//如果是能发送的情况，发送到
            val currentMap = HashMap<String,Any>()
            currentMap["title"] = editTextContent.value!!
            currentMap["time"] = mTime.get()!!
            _processData.value = currentMap// 通知LiveData更新
        }
    }
}