package com.existmg.library_ui.dialog.view

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.content.Context
import android.widget.DatePicker
import com.existmg.library_ui.interfaces.DataPickerGetCalendarListener
import java.util.Calendar

/**
 * @Author ContentMy
 * @Date 2024/4/21 7:00 PM
 * @Description 这里是时间选择的dialog封装类
 */
class MyDataPickerDialog private constructor(builder: Builder): OnDateSetListener{
    private val datePickerDialog: DatePickerDialog
    private val calendar = Calendar.getInstance()
    private var calendarListener: DataPickerGetCalendarListener? = null
    init {
        val context = builder.context
        val year = builder.getYear()
        val month = builder.getMonth()
        val dayOfMonth = builder.getDayOfMonth()
        calendarListener = builder.getListener()

        datePickerDialog = DatePickerDialog(
            context,
            this,
            year,
            month,
            dayOfMonth
        )
    }

    fun show(timestamp: Long) {
        calendar.timeInMillis = if (timestamp == 0L){
            System.currentTimeMillis()
        }else{
            // 根据传递的时间戳重置 calendar 对象
            timestamp
        }
        // 更新 DatePickerDialog 中显示的日期
        datePickerDialog.updateDate(
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.show()
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        // 设置日期
        calendar.set(year, month, dayOfMonth)
        calendarListener?.getCalendar(calendar)
    }

    class Builder(val context: Context){
        private var calendarYear: Int = Calendar.getInstance().get(Calendar.YEAR)
        private var calendarMonth: Int = Calendar.getInstance().get(Calendar.MONTH)
        private var calendarDayOfMonth: Int = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        private var calendarListener: DataPickerGetCalendarListener? = null

        fun setCalendar(year: Int, month: Int, dayOfMonth: Int): Builder {
            calendarYear = year
            calendarMonth = month
            calendarDayOfMonth = dayOfMonth
            return this
        }

        fun setListener(listener: DataPickerGetCalendarListener): Builder {
            calendarListener = listener
            return this
        }

        fun resetCalendar(){
            //TODO:这里本想去开放一个重置时间的api，用来防止多次不同的事件来源调用这个dialog时，时间显示为上次事件选择完的时间。但目前想到去动态传递一个时间戳到show方法中，去更新calendar
        }
        fun getYear():Int{
            return calendarYear
        }

        fun getMonth():Int{
            return calendarMonth
        }

        fun getDayOfMonth():Int{
            return calendarDayOfMonth
        }

        fun getListener(): DataPickerGetCalendarListener? {
            return calendarListener
        }
        fun build(): MyDataPickerDialog {
            return MyDataPickerDialog(this)
        }
    }
}