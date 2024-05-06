package com.existmg.library_base.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

/**
 * @Author:ContentMy
 * @Date: 2024/4/6 10:39 AM
 * @Description: 这里是基类Activity，用于做通用的封装操作
 */
abstract class BaseActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutId())
        beforeInit()
        initView()
        initData()
        initListener()
    }

    /**
     * @Author: ContentMy
     * @return: 返回布局ID e.g:R.layout.activity_main
     * @Description: 子类实现抽象方法，返回布局id，用于在基类中统一设置布局
     */
    abstract fun getLayoutId(): Int

    /**
     * @Author: ContentMy
     * @Description: 初始化view相关的操作，使用open而不是静态函数是为了让子类实现有更多的操作空间
     */
    open fun initView(){

    }

    /**
     * @Author: ContentMy
     * @Description: 初始化数据相关的操作
     */
    open fun initData(){

    }

    /**
     * @Author: ContentMy
     * @Description: 初始化监听相关的操作
     */
    open fun initListener(){

    }

    /**
     * @Author: ContentMy
     * @Description: 提供一个可以在初始化一系列操作之前进行调用的开放方法给子类使用
     */
    open fun beforeInit(){

    }
}