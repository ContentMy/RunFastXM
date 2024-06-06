package com.existmg.library_common.utils

import android.view.View

/**
 * @Author ContentMy
 * @Date 2024/6/6 4:35 PM
 * @Description View相关的顶层方法工具类
 */

/**
 * @Author: ContentMy
 * @params: interval 双击检测的间隔时间
 * @Description: 优化双击导致的多次点击的问题，短时间内双击会认为是单次点击
 */
private var lastClickTime = 0L
fun View.setOnSingleClickListener(interval: Long = 1000L, onClick: (View) -> Unit) {
    this.setOnClickListener {
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastClickTime >= interval) {
            lastClickTime = currentTime
            onClick(it)
        }
    }
}