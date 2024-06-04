package com.existmg.library_common.managers

import android.app.Activity

/**
 * @Author ContentMy
 * @Date 2024/6/4 4:12 PM
 * @Description 这是一个activity的堆栈管理类
 */
object ActivityStackManager {
    private val activityStack: ArrayDeque<Activity> = ArrayDeque()


    /**
     * @Author: ContentMy
     * @params: activity 入栈的activity
     * @Description: 在基类的onCreate方法中调用，统一处理activity的入栈管理
     */
    fun addActivity(activity: Activity) {
        activityStack.addLast(activity)
    }

    /**
     * @Author: ContentMy
     * @params: activity 出栈的activity
     * @Description: 在基类的onDestroy方法中调用，统一处理activity的出栈管理
     */
    fun removeActivity(activity: Activity) {
        activityStack.remove(activity)
    }

    /**
     * @Author: ContentMy
     * @Description: 退出所有activity，清空栈
     */
    fun finishAllActivities() {
        for (activity in activityStack) {
            activity.finish()
        }
        activityStack.clear()
    }

    /**
     * @Author: ContentMy
     * @return: 栈中activity的个数
     * @Description: 用于获取栈中activity的个数
     */
    fun activityStackSize():Int{
        return activityStack.size
    }
    
    /**
     * @Author: ContentMy
     * @Description: 结束当前activity
     */
    fun finishCurrentActivity(){
        val activity = activityStack.last()
        finishActivity(activity)
    }
    
    /**
     * @Author: ContentMy
     * @params: activity 指定activity
     * @Description: 结束指定activity
     */
    fun finishActivity(activity: Activity?){
        if (activity != null){
            if (!activity.isFinishing){
                activity.finish()
            }
        }
    }

    /**
     * @Author: ContentMy
     * @params: cls 指定类名的activity的class
     * @Description: 结束指定类名的activity
     */
    fun finishActivity(cls: Class<*>) {
        for (activity in activityStack) {
            if (activity.javaClass == cls) {
                finishActivity(activity)
                break
            }
        }
    }
    
    /**
     * @Author: ContentMy
     * @params: cls 指定类名的activity的class
     * @return: 返回对应的activity
     * @Description: 获取指定的activity
     */
    fun getActivity(cls: Class<*>):Activity?{
        for (activity in activityStack) {
            if (activity.javaClass == cls) {
                return activity
            }
        }
        return null
    }
}