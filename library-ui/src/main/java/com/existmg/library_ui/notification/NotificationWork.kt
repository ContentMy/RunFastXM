package com.existmg.library_ui.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.existmg.library_ui.R

/**
 * @Author ContentMy
 * @Date 2024/4/13 5:57 PM
 * @Description
 */
class NotificationWork(
    val context: Context,
    workerParams: WorkerParameters
    ) : Worker(context, workerParams) {
    override fun doWork(): Result {
        postNotification(context)
        return Result.success()
    }

    private fun postNotification(context:Context){
        // 创建通知
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channelId = "default_channel_id"
        val channelName = "Default Channel"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(channel)
        }

        val notificationBuilder = NotificationCompat.Builder(context, channelId)
            .setContentTitle("提醒")
            .setContentText("您有一个新提醒")
            .setSmallIcon(R.drawable.abc_vector_test)
            .setAutoCancel(true)

        notificationManager.notify(0, notificationBuilder.build())
    }
}