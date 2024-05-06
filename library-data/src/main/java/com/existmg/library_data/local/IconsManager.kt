package com.existmg.library_data.local

import android.graphics.drawable.Icon
import com.existmg.library_data.db.entity.IconEntity

/**
 * @Author ContentMy
 * @Date 2024/4/23 5:30 PM
 * @Description 这里直接读取了本地drawable的icon列表
 */
class IconsManager() {
    // 延迟初始化的iconsList，第一次访问时会执行loadIconList()方法初始化
    private val iconsList by lazy { loadIconList() }

    private fun loadIconList():MutableList<IconEntity>{
        return mutableListOf(
            IconEntity("ui_icon_sleep"),
            IconEntity("ui_icon_sun"),
            IconEntity("ui_icon_breakfast"),
            IconEntity("ui_icon_lunch"),
            IconEntity("ui_icon_dinner"),
            IconEntity("ui_icon_study"),
            IconEntity("ui_icon_write"),
            IconEntity("ui_icon_work"),
            IconEntity("ui_icon_running"),
            IconEntity("ui_icon_ride"),
            IconEntity("ui_icon_swimming"),
            IconEntity("ui_icon_soccer"),
        )
    }

    fun getIconList():MutableList<IconEntity>{
        return iconsList
    }

    fun getIconPosition(iconName:String):Int{
        return iconsList.indexOf(IconEntity(iconName))
    }
}