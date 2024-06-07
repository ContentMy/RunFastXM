package com.existmg.library_common.activity

import android.content.Context
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.view.inputmethod.InputMethodManager
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowInsetsControllerCompat
import com.existmg.library_common.R
import com.existmg.library_common.managers.ActivityStackManager
import com.gyf.immersionbar.ImmersionBar

/**
 * @Author:ContentMy
 * @Date: 2024/4/6 10:39 AM
 * @Description: 这里是基类Activity，用于做通用的封装操作
 */
abstract class BaseActivity: AppCompatActivity() {
    private val inputMethodManager: InputMethodManager? by lazy{
        getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        /*==============沉浸式逻辑开始================*/
        ImmersionBar.with(this)
            .statusBarDarkFont(true) //状态栏字体是深色，不写默认为亮色
            .init() //必须调用方可应用以上所配置的参数
        /*==============沉浸式逻辑结束================*/
        beforeContentView()
        setContentView(getLayoutId())
        beforeInit()
        initView()
        initData()
        initListener()
        ActivityStackManager.addActivity(this)
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
     * @Description: 提供一个可以在setContentView操作之前进行调用的开放方法给子类使用
     */
    open fun beforeContentView(){

    }
    /**
     * @Author: ContentMy
     * @Description: 提供一个可以在初始化一系列操作之前进行调用的开放方法给子类使用
     */
    open fun beforeInit(){

    }

    /*==============沉浸式逻辑开始================*/
    /**
     * @Author: ContentMy
     * @Description: 子类重写这个方法来决定状态栏文字颜色
     */
    open fun isLightStatusBar(): Boolean {
        return true // 默认为浅色状态栏文字
    }
    /**
     * @Author: ContentMy
     * @Description: 沉浸式状态栏处理（30以上）
     */
    @RequiresApi(Build.VERSION_CODES.R)
    private fun setupTransparentStatusBar() {
        val controller = window.insetsController
        controller?.let {
            // 隐藏状态栏和导航栏
            it.hide(WindowInsets.Type.statusBars() or WindowInsets.Type.navigationBars())
            // 设置系统栏可见性行为
            it.systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
    }

    /**
     * @Author: ContentMy
     * @Description: 沉浸式状态栏处理（30以下）
     */
    private fun setupLegacyTransparentStatusBar() {
        window.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
        window.statusBarColor = Color.TRANSPARENT
    }

    /**
     * @Author: ContentMy
     * @params: isLight 是否是浅色状态栏，由子类实现isLightStatusBar返回对应的boolean
     * @Description: 设置状态栏的文字颜色，默认背景为浅色模式（也就是文字是黑色）
     */
    private fun setStatusBarTextColor(light: Boolean) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val controller = WindowInsetsControllerCompat(window, window.decorView)
            controller.isAppearanceLightStatusBars = light
        } else {
            if (light) {
                window.decorView.systemUiVisibility = window.decorView.systemUiVisibility or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            } else {
                window.decorView.systemUiVisibility = window.decorView.systemUiVisibility and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
            }
        }
    }
    /*==============沉浸式逻辑结束================*/
    override fun onDestroy() {
        super.onDestroy()
        ActivityStackManager.removeActivity(this)
    }


    //解决Android Q（Android 10）引入的android.app.IRequestFinishCallback$Stub类引起的内存泄漏问题
    override fun onBackPressed() {
        // 检查是否是任务根，并且Fragment栈为空
        if (isTaskRoot && supportFragmentManager.backStackEntryCount == 0) {
            // 使用finishAfterTransition()替代super.onBackPressed()
            finishAfterTransition()
        } else {
            super.onBackPressed()
        }
    }

    open fun showSoftInput(v:View){
        inputMethodManager?.let {
            v.requestFocus()
            it.showSoftInput(v, InputMethodManager.SHOW_IMPLICIT)
        }
    }
    open fun hideSoftInput(v: View){//TODO:后续增加判断，如果软键盘显示的情况下再调用
        inputMethodManager?.hideSoftInputFromWindow(v.windowToken, 0)
    }
}