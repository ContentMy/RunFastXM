package com.existmg.library_ui.dialog.interfaces

/**
 * @Author ContentMy
 * @Date 2024/4/26 1:44 PM
 * @Description 这里是用于dialog将业务逻辑委托给外部模块去做的接口
 */
interface DialogDatabaseDelegate {
    /**
     * @Author: ContentMy
     * @params: Map 用于将多个参数统一交给回调的方法然后交由业务侧去处理
     * @Description: 这是一个处理业务逻辑的回调方法
     */
    fun processBusinessLogic(data:Map<String,Any>)
}