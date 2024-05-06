package com.existmg.library_common.utils

import java.io.PrintWriter

import java.io.StringWriter

import java.net.UnknownHostException


/**
 * @Author ContentMy
 * @Date 2024/5/2 12:12 AM
 * @Description 字符串相关的顶层方法封装类
 */

fun isEmpty(s: String?): Boolean {
    return s == null || s.trim { it <= ' ' }.isEmpty()
}

fun getStackTraceString(tr: Throwable?): String {
    if (tr == null) {
        return ""
    }

    // This is to reduce the amount of log spew that apps do in the non-error
    // condition of the network being unavailable.
    var t = tr
    while (t != null) {
        if (t is UnknownHostException) {
            return ""
        }
        t = t.cause
    }
    val sw = StringWriter()
    val pw = PrintWriter(sw)
    tr.printStackTrace(pw)
    pw.flush()
    return sw.toString()
}