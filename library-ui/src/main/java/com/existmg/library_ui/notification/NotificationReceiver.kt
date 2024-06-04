package com.existmg.library_ui.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

/**
 * @Author ContentMy
 * @Date 2024/4/13 12:59 PM
 * @Description 这里是ui模块的广播接收者，在用户点击通知action后，会发送广播到这里，集中进行处理，将信息分发给各个模块去处理
 */
class NotificationReceiver:BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
//        println("接收到了广播，想办法启动activity")
        val broadcastIntent = Intent("com.existmg.notification.ACTION_START_ACTIVITY")
        broadcastIntent.setPackage(context?.packageName) // 确保只有当前应用的组件可以接收广播
        //获取传过来的id
        val dataId = intent?.getIntExtra("dataId",0)
//        println("在UI模块中的传递id为：$dataId")
        // 添加id到 Intent 中
        broadcastIntent.putExtra("dataId", dataId)
        context?.sendBroadcast(broadcastIntent)
    }
}