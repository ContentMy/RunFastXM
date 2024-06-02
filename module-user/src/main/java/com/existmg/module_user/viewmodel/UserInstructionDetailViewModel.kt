package com.existmg.module_user.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.existmg.library_base.viewmodel.BaseViewModel

/**
 * @Author ContentMy
 * @Date 2024/6/3 12:38 AM
 * @Description
 */
class UserInstructionDetailViewModel:BaseViewModel() {
    private val _instructionType = MutableLiveData<String>()
    val instructionType: LiveData<String> = _instructionType

    fun setInstructionType(type: String) {
        _instructionType.value = type
    }
}