package com.existmg.library_common.utils

import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale

/**
 * @Author ContentMy
 * @Date 2024/4/22 12:35 AM
 * @Description 这里是时间转换相关的顶层方法封装类
 */

/**
 * @Author: ContentMy
 * @params: dataString 输入的时间String e.g 2024-4-22
 * @return: Long 返回对应的时间戳
 * @Description: 通过calendar将String格式的时间转换为对应的时间戳
 */
fun dateStringToLong(dateString:String):Long{
    return if (dateString == "永不结束"){
        0L
    }else{
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        calendar.time = dateFormat.parse(dateString)!!
        calendar.timeInMillis
    }
}

/**
 * @Author: ContentMy
 * @params: calendar 外部提供的calendar对象
 * @return String 返回对应yyyy-MM-dd格式的String时间
 * @Description: calendar转换为String格式的时间
 */
fun calenderToDateString(calendar: Calendar):String{
    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    return dateFormat.format(calendar.time)
}

/**
 * @Author: ContentMy
 * @params: time 时间戳
 * @return: String 返回对应格式的String时间
 * @Description: 将时间戳转化为yyyy-MM-dd格式的String时间
 */
fun timeLongToString(time:Long):String{
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = time
    return calenderToDateString(calendar)
}

/**
 * @Author: ContentMy
 * @params: time 时间戳
 * @return String 返回对应格式的String时间
 * @Description: 将时间戳转化为yyyy-MM-dd HH:mm:ss格式的String时间
 */
fun timeLongToStringWithHourMinSec(time:Long):String{
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = time
    val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
    return dateFormat.format(calendar.time)
}