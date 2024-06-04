package com.existmg.module_main

import com.alibaba.android.arouter.launcher.ARouter
import com.existmg.library_common.application.BaseApplication

/**
 * @Author ContentMy
 * @Date 2024/4/18 3:44 PM
 * @Description
 */
class MainDebugApp: BaseApplication() {
    override fun onCreate() {
        super.onCreate()
        ARouter.init(this)
    }
}