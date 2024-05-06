package com.existmg.library_ui.dialog.base

import android.app.Activity
import android.os.Bundle
import android.view.Window

/**
 * @Author ContentMy
 * @Date 2024/4/13 11:33 PM
 * @Description 基础的一个DialogActivity,可以直接使用默认布局，也可以覆盖自己的布局进行处理
 * 适用场景：适合用于封装简单的对话框，例如登录界面、设置界面、提示框等。DialogActivity 将对话框相关的逻辑和界面封装到一个独立的 Activity 中，便于模块化、复用和维护。适用于对话框内容较简单，与主界面的交互不复杂的情况。
 * 优点：模块化、易于维护、易于复用。
 * 缺点：可能会造成 Activity 过多，影响应用性能；对话框内容过于复杂时不易于管理。
 */
abstract class DialogBaseActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 请求无标题特性
        requestWindowFeature(Window.FEATURE_NO_TITLE)

        // 设置窗口背景透明
        window.setBackgroundDrawableResource(android.R.color.transparent)

        // 获取内容视图，子类可以覆盖
        setContentView(getLayoutId())
        initView()
        initData()
        initListener()
    }

    abstract fun getLayoutId(): Int

    abstract fun initView()

    abstract fun initData()

    abstract fun initListener()

    open fun beforeInit(){

    }
}