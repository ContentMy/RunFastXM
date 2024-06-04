package com.existmg.library_common.application

import androidx.multidex.MultiDex
import androidx.multidex.MultiDexApplication

/**
 * @Author ContentMy
 * @Date 2024/3/30 6:32 下午
 * @Description 这里是Application的基类封装
 */
open class BaseApplication:MultiDexApplication(){
    override fun onCreate() {
        super.onCreate()
        MultiDex.install(this) //方法数超过65535的优化方案，继承MultiDexApplication，然后初始化方法，让项目在运行时加载多个dex文件
    }
}