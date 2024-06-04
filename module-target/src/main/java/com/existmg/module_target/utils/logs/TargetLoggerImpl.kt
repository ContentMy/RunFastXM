package com.existmg.module_target.utils.logs

import com.existmg.library_common.log.LogUtil
import com.existmg.module_target.BuildConfig

/**
 * @Author ContentMy
 * @Date 2024/6/1 5:08 PM
 * @Description 这里是log日志类的二次封装，目的是为了与基础模块中的log类做解耦
 */
class TargetLoggerImpl(private val tag:String): TargetLoggerInterface {
    private val logger = LogUtil(tag)

    /**
     * @Author: ContentMy
     * @params: message 日志信息
     * @Description:  debug级别的日志方法，正式环境不会展示
     */
    override fun debug(message: String) {
        if (BuildConfig.LOG_DEBUG) {
            logger.debug(message)
        }
    }

    /**
     * @Author: ContentMy
     * @params: message 日志信息
     * @Description:  info级别的日志方法，正式环境不会展示
     */
    override fun info(message: String) {
        if (BuildConfig.LOG_DEBUG) {
            logger.info(message)
        }
    }

    /**
     * @Author: ContentMy
     * @params: message 日志信息
     * @Description:  warn级别的日志方法，正式环境会有展示
     */
    override fun warn(message: String) {
        logger.warn(message)
    }

    /**
     * @Author: ContentMy
     * @params: message 日志信息
     * @Description:  error级别的日志方法，正式环境会有展示
     */
    override fun error(message: String) {
        logger.error(message)
    }

    /**
     * @Author: ContentMy
     * @params:
     *          message   日志信息
     *          throwable 错误堆栈
     * @Description:  error级别的日志方法，携带错误堆栈，正式环境会有展示
     */
    override fun error(message: String, throwable: Throwable) {
        logger.error(message, throwable)
    }
}