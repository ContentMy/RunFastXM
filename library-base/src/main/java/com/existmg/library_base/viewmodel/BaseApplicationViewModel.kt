package com.existmg.library_base.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.existmg.library_base.viewmodel.lifecycle.BaseViewModelLifecycleObserver

/**
 * @Author ContentMy
 * @Date 2024/4/13 1:41 PM
 * @Description 这是基于application生命周期的viewmodel，适用于需要使用application的相关场景
 */
abstract class BaseApplicationViewModel(application: Application):AndroidViewModel(application){
}