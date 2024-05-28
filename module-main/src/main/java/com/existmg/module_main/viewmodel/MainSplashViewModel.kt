package com.existmg.module_main.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.existmg.library_base.viewmodel.BaseApplicationViewModel
import com.existmg.library_base.viewmodel.BaseViewModel

/**
 * @Author ContentMy
 * @Date 2024/5/28 5:19 PM
 * @Description
 */
class MainSplashViewModel(application: Application):BaseApplicationViewModel(application) {
    private val sharedPreferences = application.getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)

    private val _showGuide = MutableLiveData<Boolean>()
    val showGuide: LiveData<Boolean> get() = _showGuide

    fun checkIfFirstTime(activityName: String) {
        val isFirstTime = sharedPreferences.getBoolean(activityName, true)
        if (isFirstTime) {
            sharedPreferences.edit().putBoolean(activityName, false).apply()
        }
        _showGuide.value = isFirstTime
    }
}