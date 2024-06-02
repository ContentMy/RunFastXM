package com.existmg.library_base.viewmodel

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import com.existmg.library_base.viewmodel.lifecycle.BaseViewModelLifecycleObserver


/**
 * @Author ContentMy
 * @Date 2024/4/13 1:35 PM
 * @Description 这是一个普通的viewmodel基类
 */
abstract class BaseViewModel: ViewModel(), LifecycleEventObserver {
    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        when (event) {
            Lifecycle.Event.ON_CREATE -> onCreate()
            Lifecycle.Event.ON_START -> onStart()
            Lifecycle.Event.ON_RESUME -> onResume()
            Lifecycle.Event.ON_PAUSE -> onPause()
            Lifecycle.Event.ON_STOP -> onStop()
            Lifecycle.Event.ON_DESTROY -> onDestroy()
            else -> {}
        }
    }
    /*======================Activity & Fragment共有的生命周期，并且无需额外配置========================*/
    /**
     * @Author: ContentMy
     * @Description: Activity&Fragment生命周期，onStateChanged提供，可以直接在ViewModel中使用
     */
    open fun onCreate(){}
    open fun onStart(){}
    open fun onResume(){}
    open fun onPause(){}
    open fun onStop(){}
    open fun onDestroy(){}

    /*======================Activity & Fragment共有的生命周期，需要单独在Base基类中对应的生命周期进行调用========================*/
    open fun onSaveInstanceState(){}//未调用，遇到具体场景后再进行优化

    /*======================Activity特有的生命周期，需要单独在Base基类中对应的生命周期进行调用========================*/
    open fun onRestart(){}//未调用，遇到具体场景后再进行优化
    open fun onNewIntent(){}//未调用，遇到具体场景后再进行优化

    /*======================Fragment特有的生命周期，需要单独在Base基类中对应的生命周期进行调用========================*/
    /**
     * @Author: ContentMy
     * @Description: Fragment生命周期，onStateChanged不提供，手动封装并且在BaseFragment中进行绑定后，在ViewModel进行对应的回调使用
     */
    open fun onAttach(){}//未调用，遇到具体场景后再进行优化
    open fun onCreateView(){}//未调用，遇到具体场景后再进行优化
    open fun onViewCreated(){}//未调用，遇到具体场景后再进行优化
    open fun onActivityCreated(){}//未调用，遇到具体场景后再进行优化
    open fun onDestroyView(){}//未调用，遇到具体场景后再进行优化
    open fun onDetach(){}//未调用，遇到具体场景后再进行优化

}