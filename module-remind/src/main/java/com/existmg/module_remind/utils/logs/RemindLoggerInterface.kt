package com.existmg.module_remind.utils.logs

/**
 * @Author ContentMy
 * @Date 2024/6/1 5:11 PM
 * @Description 这里是日志类的接口定义
 */
interface RemindLoggerInterface {
    fun debug(message: String)
    fun info(message: String)
    fun warn(message: String)
    fun error(message: String)
    fun error(message: String, throwable: Throwable)
}