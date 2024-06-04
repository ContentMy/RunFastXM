package com.existmg.library_ui.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.work.Data
import androidx.work.OneTimeWorkRequest
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.existmg.library_ui.R
import java.util.concurrent.TimeUnit

/**
 * @Author ContentMy
 * @Date 2024/4/13 5:57 PM
 * @Description
 */
class NotificationWork(
    val context: Context,
    workerParams: WorkerParameters
    ) : Worker(context, workerParams) {
    companion object {
        private const val NOTIFICATION_ID = 0
        private const val CHANNEL_ID = "default_channel_id"
        private const val CHANNEL_NAME = "Default Channel"
        fun buildWorkRequest(dataId:Int,iconResId: Int, title: String, content: String,duration: Long): OneTimeWorkRequest {
            val inputData = Data.Builder()
                .putInt("dataId", dataId)
                .putInt("iconResId", iconResId)
                .putString("title", title)
                .putString("content", content)
                .build()

            return OneTimeWorkRequest.Builder(NotificationWork::class.java)
                .setInputData(inputData)
                .setInitialDelay(duration, TimeUnit.MILLISECONDS)//这里使用时间戳的格式也就是毫秒来统一时间单位
                .build()
        }
    }

    override fun doWork(): Result {
        // 获取通知所需的参数
        val dataId = inputData.getInt("dataId",0)
        val iconResId = inputData.getInt("iconResId", R.mipmap.ic_launcher)
        val title = inputData.getString("title")
        val content = inputData.getString("content")

        // 调用通知方法，传递参数
        postNotification(context,dataId, iconResId, if (title.isNullOrEmpty()) "" else title, if (content.isNullOrEmpty()) "" else content)

        return Result.success()
    }

    private fun postNotification(context: Context, dataId: Int,iconResId: Int, title: String, content: String) {
        // 创建通知
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(channel)
        }

        // 创建广播接收器的意图并添加操作到通知中
        val actionIntent = Intent(context, NotificationReceiver::class.java)
        println("在worker中拿到的id为：$dataId")
        actionIntent.putExtra("dataId",dataId)
        val actionPendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            PendingIntent.getBroadcast(context, dataId, actionIntent, PendingIntent.FLAG_IMMUTABLE)
        } else {
            PendingIntent.getBroadcast(context, dataId, actionIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        }

        val notificationBuilder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setContentTitle(title)
            .setContentText(content)
            .setSmallIcon(iconResId)
            .setAutoCancel(true)
            .setContentIntent(actionPendingIntent)

        notificationManager.notify(dataId, notificationBuilder.build())//把提醒的数据表id作为通知的id，保证了唯一
    }
}