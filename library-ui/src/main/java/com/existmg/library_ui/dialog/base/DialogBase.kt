package com.existmg.library_ui.dialog.base

import android.app.Dialog
import android.content.Context
import android.view.View
import android.view.ViewGroup

/**
 * @Author ContentMy
 * @Date 2024/4/14 12:06 AM
 * @Description
 * 适用场景：适合用于定制化程度较高的对话框，例如需要完全自定义对话框样式、交互逻辑等。自定义 Dialog 可以根据需求创建任意样式的对话框，并且可以灵活处理对话框内部的逻辑。适用于对话框内容非常定制化、需要特殊样式或交互逻辑的情况。
 * 优点：定制灵活，可以根据需求创建任意样式的对话框。如果屏幕旋转重建，可以基于FragmentManager自动重建
 * 缺点：需要自行处理对话框的显示、隐藏、交互等逻辑，相对复杂一些
 */
class DialogBase(context: Context,themeResId:Int) : Dialog(context,themeResId){

    override fun setContentView(layoutResID: Int) {
        super.setContentView(layoutResID)
    }

    override fun setContentView(view: View) {
        super.setContentView(view)
    }

    override fun setContentView(view: View, params: ViewGroup.LayoutParams?) {
        super.setContentView(view, params)
    }

}