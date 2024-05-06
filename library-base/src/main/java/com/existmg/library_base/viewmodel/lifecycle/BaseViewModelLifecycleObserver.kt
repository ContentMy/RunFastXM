package com.existmg.library_base.viewmodel.lifecycle

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent

/**
 * @Author ContentMy
 * @Date 2024/4/8 4:43 PM
 * @Description 这是基于MVVM的一个LifecycleObserver的接口声明，可以直接在viewmodel中实现，以达到在viewmodel中处理生命周期相关的操作的目的
 */
interface BaseViewModelLifecycleObserver:LifecycleObserver{
    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreate()
    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onStart()
    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResume()
    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun onPause()
    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onStop()
    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy()
    @OnLifecycleEvent(Lifecycle.Event.ON_ANY)
    fun onAny()
}