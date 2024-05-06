package com.existmg.library_ui.dialog.base

import android.app.Activity
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner

/**
 * @Author ContentMy
 * @Date 2024/4/13 11:33 PM
 * @Description 基础的一个DialogActivity,可以直接使用默认布局，也可以覆盖自己的布局进行处理
 * 适用场景：适合用于封装简单的对话框，例如登录界面、设置界面、提示框等。DialogActivity 将对话框相关的逻辑和界面封装到一个独立的 Activity 中，便于模块化、复用和维护。适用于对话框内容较简单，与主界面的交互不复杂的情况。
 * 优点：模块化、易于维护、易于复用。
 * 缺点：可能会造成 Activity 过多，影响应用性能；对话框内容过于复杂时不易于管理。
 */
abstract class DialogBaseMvvmActivity<VM : ViewModel, DB : ViewDataBinding> : DialogBaseActivity(),ViewModelStoreOwner {
    protected var mViewModel: VM? = null
    protected var mBinding: DB? = null
    override val viewModelStore = ViewModelStore()

    abstract fun getViewModelClass(): Class<VM>

    abstract fun bindViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, getLayoutId())
//        mBinding.lifecycleOwner = this
        mViewModel = ViewModelProvider(this)[getViewModelClass()]
        bindViewModel()
    }

    override fun onDestroy() {
        super.onDestroy()
        mViewModel = null
        mBinding = null
    }
}