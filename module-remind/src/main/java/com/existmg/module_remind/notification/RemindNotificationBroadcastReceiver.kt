package com.existmg.module_remind.notification

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.existmg.module_remind.ui.RemindDetailActivity

/**
 * @Author ContentMy
 * @Date 2024/5/8 12:41 AM
 * @Description 这个广播是为了跨模块接收通知点击跳转的activity
 */
class RemindNotificationBroadcastReceiver:BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        //点击通知时关闭通知
        cancelNotification(context,intent)
        //点击通知时跳转到对应的activity
        startToActivity(context,intent)
    }

    private fun cancelNotification(context: Context?, intent: Intent?){
        val notificationId = intent?.getIntExtra("notificationId",0)
        val notificationManager = context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancel(notificationId!!)
    }
    private fun startToActivity(context: Context?, intent: Intent?) {
        val dataId = intent?.getIntExtra("dataId",0)
        // 创建一个 Intent，指定要启动的 Activity
        val activityIntent = Intent(context, RemindDetailActivity::class.java)
        activityIntent.putExtra("dataId",dataId)
//        activityIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK//独栈并且清除其他所有的activity
        activityIntent.flags =  Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP//栈顶

        // 启动指定的 Activity
        context?.startActivity(activityIntent)
    }


}