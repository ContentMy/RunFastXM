package com.existmg.library_data.enums

/**
 * @Author ContentMy
 * @Date 2024/4/23 12:20 AM
 * @Description 这是一个一天不同状态的枚举类，用来配合TargetEntity的targetStatus以及衍生的逻辑处理，value是对应的position，displayName是对应的String内容
 */
enum class DayStatus(val value: Int, val displayName: String) {
    UNKNOWN(0,"暂不设置"),
    MORNING(1, "早晨"),
    FORENOON(2, "上午"),
    NOON(3, "中午"),
    AFTERNOON(4, "下午"),
    EVENING(5, "晚上"),
    NIGHT(6, "深夜");

    companion object {
        fun getDisplayNameFromValue(value: Int): String? {
            return values().find { it.value == value }?.displayName
        }

        fun getValueFromDisplayName(displayName: String):Int?{
            return values().find { it.displayName == displayName }?.value
        }
    }
}