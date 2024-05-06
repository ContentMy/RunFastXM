package com.existmg.library_base.application

import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import android.os.Bundle
import android.os.Process
import androidx.multidex.MultiDex
import androidx.multidex.MultiDexApplication
import com.existmg.library_base.manager.AppManager

/**
 * @Author ContentMy
 * @Date 2024/3/30 6:32 下午
 * @Description 这里是Application的基类封装
 */
open class BaseApplication:MultiDexApplication(){
    private var sInstance: BaseApplication? = null

    private var sDebug = false

    override fun onCreate() {
        super.onCreate()
        MultiDex.install(this) //方法数超过65535的优化方案，继承MultiDexApplication，然后初始化方法，让项目在运行时加载多个dex文件
        setApplication(this)
    }

    /** 当宿主没有继承自该Application时,可以使用set方法进行初始化baseApplication  */
    private fun setApplication(application: BaseApplication) {
        sInstance = application
        application
            .registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks {
                override fun onActivityCreated(
                    activity: Activity,
                    savedInstanceState: Bundle?
                ) {
                    AppManager.getInstance().addActivity(activity)
                }

                override fun onActivityStarted(activity: Activity) {}
                override fun onActivityResumed(activity: Activity) {}
                override fun onActivityPaused(activity: Activity) {}
                override fun onActivityStopped(activity: Activity) {}
                override fun onActivitySaveInstanceState(
                    activity: Activity, outState: Bundle
                ) {
                }

                override fun onActivityDestroyed(activity: Activity) {
                    AppManager.getInstance().removeActivity(activity)
                }
            })
    }

    /**
     * 获得当前app运行的Application
     */
    fun getInstance(): BaseApplication? {
        if (sInstance == null) {
            throw NullPointerException(
                "please inherit BaseApplication or call setApplication."
            )
        }
        return sInstance
    }

    fun setDebug(isDebug: Boolean) {
        sDebug = isDebug
    }

    fun isDebug(): Boolean {
        return sDebug
    }

    /**
     * 获取进程名
     *
     * @param context
     * @return
     */
    fun getCurrentProcessName(context: Context): String? {
        val am = context.getSystemService(ACTIVITY_SERVICE) as ActivityManager
        val runningApps = am.runningAppProcesses ?: return null
        for (proInfo in runningApps) {
            if (proInfo.pid == Process.myPid()) {
                if (proInfo.processName != null) {
                    return proInfo.processName
                }
            }
        }
        return null
    }
}