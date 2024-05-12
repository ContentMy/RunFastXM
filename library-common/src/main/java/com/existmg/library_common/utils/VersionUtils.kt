package com.existmg.library_common.utils

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build

/**
 * @Author ContentMy
 * @Date 2024/5/11 12:51 AM
 * @Description 关于应用版本的顶层方法的封装，后期如果版本和其他数据需要联动的情况下，可能会改成一个单例的封装类
 */

fun getAppVersionName(context: Context): String {
    return try {
        val pInfo = context.packageManager.getPackageInfo(context.packageName, 0)
        pInfo.versionName
    } catch (e: PackageManager.NameNotFoundException) {
        e.printStackTrace()
        ""
    }
}

fun getAppVersionCode(context: Context): Int {
    return try {
        val pInfo = context.packageManager.getPackageInfo(context.packageName, 0)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            pInfo.longVersionCode.toInt()
        } else {
            pInfo.versionCode
        }
    } catch (e: PackageManager.NameNotFoundException) {
        e.printStackTrace()
        -1
    }
}