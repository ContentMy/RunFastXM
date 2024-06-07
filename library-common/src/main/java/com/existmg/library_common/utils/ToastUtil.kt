package com.existmg.library_common.utils

import android.content.Context
import android.widget.Toast

/**
 * @Author ContentMy
 * @Date 2024/6/2 12:20 AM
 * @Description
 */
object ToastUtil {
    private var toast: Toast? = null

    /**
     * @Author: ContentMy
     * @param: context 上下文
     * @param: message toast的文本内容
     * @Description: 短时间内展示一条Toast
     */
    fun showShort(context: Context, message: CharSequence) {
        showToast(context.applicationContext, message, Toast.LENGTH_SHORT)
    }

    /**
     * @Author: ContentMy
     * @param: context 上下文
     * @param: message toast的文本Id
     * @Description: 短时间内展示一条Toast
     */
    fun showShort(context: Context, messageResId: Int) {
        showToast(context.applicationContext, context.getString(messageResId), Toast.LENGTH_SHORT)
    }

    /**
     * @Author: ContentMy
     * @param: context 上下文
     * @param: message toast的文本内容
     * @Description: 较长时间内展示一条Toast
     */
    fun showLong(context: Context, message: CharSequence) {
        showToast(context.applicationContext, message, Toast.LENGTH_LONG)
    }

    /**
     * @Author: ContentMy
     * @param: context 上下文
     * @param: message toast的文本Id
     * @Description: 较长时间内展示一条Toast
     */
    fun showLong(context: Context, messageResId: Int) {
        showToast(context.applicationContext, context.getString(messageResId), Toast.LENGTH_LONG)
    }

    /**
     * @Author: ContentMy
     * @Description: 取消Toast
     */
    fun cancel() {
        toast?.cancel()
    }

    private fun showToast(context: Context, message: CharSequence, duration: Int) {
        cancel() //调用时取消上一条toast
        toast = Toast.makeText(context, null, duration)//这里设置null后续再设置文本是为了兼容小米手机携带appName的问题
        toast?.setText(message)
        toast?.show()
    }
}