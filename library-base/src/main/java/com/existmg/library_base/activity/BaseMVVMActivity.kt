package com.existmg.library_base.activity

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import com.existmg.library_base.viewmodel.BaseViewModel

/**
 * @Author:ContentMy
 * @Date: 2024/4/6 10:46 AM
 * @Description: 这里是基于MVVM的基类Activity，继承了最基本的封装的基类，然后加入了mvvm相关的封装内容
 */
abstract class BaseMVVMActivity<VM : ViewModel, VB : ViewDataBinding> : BaseActivity() {
    protected lateinit var mViewModel: VM
    protected lateinit var mBinding: VB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initObserver()
    }

    /**
     * @Author: ContentMy
     * @Description: 实现基类的beforeInit用来做MVVM的相关初始化操作
     */
    override fun beforeInit() {
        super.beforeInit()
        mBinding = DataBindingUtil.setContentView(this, getLayoutId())
        mBinding.lifecycleOwner = this
        mViewModel = createViewModel()
        bindViewModel()
    }

    /**
     * @Author: ContentMy
     * @return: 返回viewmodel的class对象
     * @Description: 子类实现抽象方法,返回viewmodel的class对象用于在基类中初始化viewmodel
     */
    abstract fun getViewModelClass(): Class<VM>

    /**
     * @Author: ContentMy
     * @Description: 子类实现抽象方法,需要手动去将binding和viewmodel绑定
     */
    abstract fun bindViewModel()

    /**
     * @Author: ContentMy
     * @return: viewmodel工厂类，调用对应的扩展函数对viewmodel进行参数传值
     * @Description: 是一个子类需要实现的抽象方法，会返回一个viewmodel的工厂类对象，主要是为了处理携带参数的viewmodel的初始化问题
     */
    abstract fun provideViewModelFactory(): ViewModelProvider.Factory

    /**
     * @Author: ContentMy
     * @Description: 初始化observe的相关操作
     */
    open fun initObserver(){

    }

    /**
     * @Author: ContentMy
     * @return: viewmodel
     * @Description: viewmodel的初始化操作，使用了子类返回的ViewModelFactory和ViewModelClass，得到最终的viewmodel
     */
    private fun createViewModel(): VM {
        return ViewModelProvider(this, provideViewModelFactory())[getViewModelClass()]
    }

}