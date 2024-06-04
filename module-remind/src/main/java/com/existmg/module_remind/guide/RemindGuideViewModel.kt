package com.existmg.module_remind.guide

import android.app.Application
import android.content.Context
import com.existmg.library_common.viewmodel.BaseApplicationViewModel
import com.existmg.library_common.viewmodel.BaseViewModel

/**
 * @Author ContentMy
 * @Date 2024/5/27 3:51 PM
 * @Description
 */
class RemindGuideViewModel(application: Application): BaseApplicationViewModel(application) {
    private val sharedPreferences = application.getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
    fun closeRemindGuideWithSP(activityName:String) {
        sharedPreferences.edit().putBoolean(activityName, false).apply()
    }
}