package com.existmg.library_ui.notification

import android.app.AlarmManager
import android.app.Application
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.SystemClock
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit


/**
 * @Author ContentMy
 * @Date 2024/4/13 12:59 PM
 * @Description
 */
class NotificationRepository {
    //使用AlarmManager进行定时通知的发送
    fun startNotification(frequencyMinutes: Int,application: Application) {
        println("发送了广播，时间为${frequencyMinutes}分钟")
        val alarmManager = application.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(application, NotificationReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(application, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        val triggerTime = SystemClock.elapsedRealtime() + frequencyMinutes * 60 * 1000 // 转换为毫秒
        alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerTime,
            (frequencyMinutes * 60 * 1000).toLong(), pendingIntent)
    }

    //取消AlarmManager定时通知
    fun cancelNotification(application: Application) {
        val alarmManager =application.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(application, NotificationReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(application, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        alarmManager.cancel(pendingIntent)
    }

    //使用WorkManager进行定时通知的发送
    fun postNotification(context: Context){
        // 设置任务延迟执行，例如1分钟后执行
        val workRequest = OneTimeWorkRequest.Builder(NotificationWork::class.java)
            .setInitialDelay(1, TimeUnit.MINUTES)
            .build()

        WorkManager.getInstance(context).enqueue(workRequest)

    }
}