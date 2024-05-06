package com.existmg.library_ui.utils

import android.content.Context
import android.widget.ImageView

/**
 * @Author ContentMy
 * @Date 2024/4/23 7:48 PM
 * @Description
 */
fun ImageView.setDrawableImageByName(imageName: String) {
    val resourceId = getImageResourceId(imageName)
    this.setImageResource(resourceId)
}

fun ImageView.setDrawableImageCircleById(context: Context,colorId: Int) {
    this.background = setIconOval(context,colorId)
}