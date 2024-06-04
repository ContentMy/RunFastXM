package com.existmg.library_common.log

import java.text.DateFormat
import java.util.Date
import java.util.Locale

/**
 * @Author ContentMy
 * @Date 2024/6/1 4:55 PM
 * @Description
 */
class LogUtil (private val tag: String) {

    companion object {
        private const val DEBUG = "DEBUG"
        private const val INFO = "INFO"
        private const val WARN = "WARN"
        private const val ERROR = "ERROR"
        inline fun <reified T> getLogger(): LogUtil = LogUtil(T::class.java.simpleName)
    }

    private fun log(level: String, message: String, throwable: Throwable? = null) {
        val dateFormat: DateFormat = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT, Locale.getDefault())
        val timestamp = dateFormat.format(Date())
        val logMessage = "$timestamp [$level] [$tag] $message"
        println(logMessage)
        throwable?.printStackTrace()
    }

    fun debug(message: String) {
        log(DEBUG, message)
    }

    fun info(message: String) {
        log(INFO, message)
    }

    fun warn(message: String) {
        log(WARN, message)
    }

    fun error(message: String) {
        log(ERROR, message)
    }

    fun error(message: String, throwable: Throwable) {
        log(ERROR, message, throwable)
    }
}