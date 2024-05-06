package com.existmg.library_common.utils

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager


/**
 * @Author ContentMy
 * @Date 2024/4/22 10:59 PM
 * @Description 软键盘相关的顶层方法封装类
 */

/**
 * @Author: ContentMy
 * @params:
 *      context 上下文
 *      view    对应焦点的view
 * @Description: 隐藏软键盘
 */
fun hideKeyboardViewCommon(context: Context, view: View) {
    val manager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    manager.hideSoftInputFromWindow(view.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
}