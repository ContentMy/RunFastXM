package com.existmg.library_common.viewmodel

import android.app.Application

/**
 * @Author ContentMy
 * @Date 2024/4/13 1:41 PM
 * @Description 这是基于application生命周期的viewmodel，适用于需要使用application的相关场景
 */
open class BaseApplicationViewModel(private val application: Application): BaseViewModel(){
    /**
     * Return the application.
     */
    @Suppress("UNCHECKED_CAST")
    open fun <T : Application> getApplication(): T {
        return application as T
    }
}