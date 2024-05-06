package com.existmg.library_common.utils

import android.view.View
import android.view.Window

/**
 * @Author ContentMy
 * @Date 2024/4/20 10:21 PM
 * @Description 这里是一个和Activity有关的顶层函数类
 */

fun setLightStatusBar(window:Window){
    val flags = window.decorView.systemUiVisibility
    window.decorView.systemUiVisibility = flags or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
}

fun setDarkStatusBar(window:Window) {
    val flags = window.decorView.systemUiVisibility or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
    window.decorView.systemUiVisibility = flags xor View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
}