package com.existmg.library_ui.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat

/**
 * @Author ContentMy
 * @Date 2024/4/13 12:59 PM
 * @Description
 */
class NotificationReceiver:BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        println("接收到了广播")
        // 创建通知
        val notificationManager = context!!.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channelId = "default_channel_id"
        val channelName = "Default Channel"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(channel)
        }

        val notificationBuilder = NotificationCompat.Builder(context, channelId)
            .setContentTitle("提醒")
            .setContentText("您有一个新提醒")
//            .setSmallIcon(R.drawable.abc_vector_test)
            .setAutoCancel(true)

        notificationManager.notify(0, notificationBuilder.build())
    }
}