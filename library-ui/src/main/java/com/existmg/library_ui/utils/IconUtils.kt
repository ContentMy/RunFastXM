package com.existmg.library_ui.utils

import android.content.Context
import android.graphics.drawable.GradientDrawable
import androidx.core.content.ContextCompat
import com.existmg.library_ui.R

/**
 * @Author: ContentMy
 * @params: context 上下文 colorId 圆环的背景颜色
 * @Description: 设置icon的圆环
 */
fun setIconOval(context: Context,colorId:Int):GradientDrawable{
    val drawable = GradientDrawable()
    drawable.shape = GradientDrawable.OVAL
    drawable.setStroke(5, ContextCompat.getColor(context, colorId))
    return drawable
}

/**
 * @Author: ContentMy
 * @params: context 上下文 drawableName drawable的名称
 * @Description:
 */
fun getImageId(context: Context, drawableName:String):Int{
    return context.resources.getIdentifier(drawableName, "drawable", context.packageName)
}

/**
 * @Author: ContentMy
 * @params: drawableName drawable的名称
 * @Description:
 */
fun getImageResourceId(drawableName: String): Int {
    return when (drawableName) {
        "ui_icon_sleep" -> R.drawable.ui_icon_sleep
        "ui_icon_sun" -> R.drawable.ui_icon_sun
        "ui_icon_breakfast" -> R.drawable.ui_icon_breakfast
        "ui_icon_lunch" -> R.drawable.ui_icon_lunch
        "ui_icon_dinner" -> R.drawable.ui_icon_dinner
        "ui_icon_running" -> R.drawable.ui_icon_running
        "ui_icon_ride" -> R.drawable.ui_icon_ride
        "ui_icon_soccer" -> R.drawable.ui_icon_soccer
        "ui_icon_swimming" -> R.drawable.ui_icon_swimming
        "ui_icon_study" -> R.drawable.ui_icon_study
        "ui_icon_write" -> R.drawable.ui_icon_write
        "ui_icon_work" -> R.drawable.ui_icon_work
        else -> R.drawable.ui_icon_sleep // 如果找不到对应的图像名称，返回默认图片的资源ID
    }
}