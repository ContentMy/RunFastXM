package com.existmg.library_ui.notification

import android.content.Context
import androidx.work.WorkManager


/**
 * @Author ContentMy
 * @Date 2024/4/13 12:59 PM
 * @Description
 */
class NotificationRepository {
    //使用WorkManager进行定时通知的发送
    fun postNotification(context: Context,dataId:Int,iconResId:Int,title:String,content:String,duration:Long){
        val workRequest = NotificationWork.buildWorkRequest(dataId,iconResId, title, content,duration)
        WorkManager.getInstance(context).enqueue(workRequest)

    }
}