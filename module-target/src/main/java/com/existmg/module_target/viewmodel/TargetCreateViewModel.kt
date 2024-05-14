package com.existmg.module_target.viewmodel

import android.app.Application
import android.widget.Toast
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.existmg.library_base.viewmodel.BaseApplicationViewModel
import com.existmg.library_data.local.IconsManager
import com.existmg.library_ui.notification.NotificationRepository
import com.existmg.library_data.enums.DayStatus
import com.existmg.module_target.model.CalendarHelperEvent
import com.existmg.library_data.db.entity.TargetEntity
import com.existmg.library_data.repository.TargetRepository
import com.existmg.library_common.utils.calenderToDateString
import com.existmg.library_common.utils.dateStringToLong
import com.existmg.library_ui.utils.getImageResourceId
import kotlinx.coroutines.launch
import java.util.Calendar

/**
 * @Author ContentMy
 * @Date 2024/4/8 6:50 PM
 * @Description 这里是新建目标页面的对应viewmodel，主要是做了数据插入的对应操作
 */
class TargetCreateViewModel(
    private val targetRepository: TargetRepository,
    private val notificationRepository: NotificationRepository,
    application: Application
    ): BaseApplicationViewModel(application) {
    // 可变的 MutableLiveData 用于存储icon资源名称
    var targetImageString = MutableLiveData<String>()
    // 可变的 MutableLiveData 用于存储目标名称以及和布局中控件的双向绑定 TODO:要增加对于title的判断，如果为空的话，就不能进行保存。这里应该使用Toast实现，看是否要自定义实现或者直接使用系统的
    var targetTitleString = MutableLiveData<String>()
    // 可变的 MutableLiveData 用于存储目标备注内容以及和布局中控件的双向绑定 TODO:要增加对于content内容的字符串默认值随机给出，每次进入新建页面时，都随机一段话
    var targetContentString = MutableLiveData<String>()
    // 可变的 MutableLiveData 用于存储目标开始时间以及和布局中控件的双向绑定 TODO:要增加对于StartTime的时间戳和对应String格式的转化，时间戳用于写入数据库中，String格式的时间用于在ui展示(done)
    var targetStartTimeString = ObservableField<String>()
    // 可变的 MutableLiveData 用于存储目标结束时间以及和布局中控件的双向绑定 TODO:复用开始时间的格式转化，同时考虑结束时间永久的情况，以及持续时间的计算（用于倒计时、后续时间维度等，考虑可能要增加字段在实体类中）
    var targetEndTimeString = ObservableField<String>()
    // 可变的 MutableLiveData 用于目标状态和布局双向绑定 TODO:考虑不在新建目标时作为一个事项去让用户操作，或者转化为其他维度去记录，比如一天的早上，中午，下午，晚上等，去将目标细分到一天的时间段内
    var targetStatusString = ObservableField<String>()
    // 可变的 MutableLiveData 用于开关状态和布局双向绑定 TODO:是用来开启是否提醒，结合保存的逻辑处去判断开启通知的条件，是直接复用这个参数还是参数只是一个媒介，实际要去考虑入库时的target中的实时状态
    var targetIsRemind = MutableLiveData<Boolean>()

    private var startTimeLong = 0L//用于存放开始结束时间的时间戳，用来做入库判断
    private var endTimeLong = 0L
    private var statusInt = 0//单设置一个int值存放用于入库时赋值，在入库时去直接转化数据会获取到0，目前可能是ObservableField或者是枚举遍历的问题，所以提前进行逻辑处理
    private lateinit var updateTarget: TargetEntity
    init {
//        val calendar = Calendar.getInstance()
//        startTimeLong = calendar.timeInMillis//给定一个初始化的开始时间戳，避免入库时默认值为0
//        targetStartTimeString.set(calenderToDateString(calendar))//初始化开始时间为当前的时间
//        targetEndTimeString.set("永不结束")
    }

    /*================初始化数据操作 - 开始===================*/
    fun initNewTarget(){
        val calendar = Calendar.getInstance()
        targetImageString.value = IconsManager().getIconList().first().name
        startTimeLong = calendar.timeInMillis//给定一个初始化的开始时间戳，避免入库时默认值为0
        targetStartTimeString.set(calenderToDateString(calendar))//初始化开始时间为当前的时间
        targetEndTimeString.set("永不结束")
        targetStatusString.set(DayStatus.UNKNOWN.displayName)
        targetIsRemind.value = false
    }

    fun initExistTarget(target: TargetEntity) {//TODO:这里的初始化逻辑还有一个方案是直接传递实体类的id，根据id在这个页面再开始去数据库中查询。可以作为逻辑优化方案之一
        updateTarget = target//这里传进来后续是有可能要更新数据库的，所以赋值一个成员变量给数据库操作时使用
        val calendar = Calendar.getInstance()
        targetImageString.value = target.targetImg//TODO:有维护一个图标列表，获取这里暂时没有用到，但是图标的逻辑没有处理，所以这里也暂时注释掉
        targetTitleString.value = target.targetTitle
        targetContentString.value = target.targetContent
        calendar.timeInMillis = target.targetStartTime
        targetStartTimeString.set(calenderToDateString(calendar))
        if (target.targetEndTime == 0L){
            targetEndTimeString.set("永不结束")
        }else{
            calendar.timeInMillis = target.targetEndTime
            targetEndTimeString.set(calenderToDateString(calendar))
        }
        val statusValue = DayStatus.getDisplayNameFromValue(target.targetStatus)
        targetStatusString.set(statusValue) //TODO：定义枚举，枚举值为数据库中存储的值，返回的名称为String
        targetIsRemind.value = target.targetRemind
    }
    /*================初始化数据操作 - 结束===================*/


    /*================icon操作 - 开始===================*/
    fun chooseIcon(iconName:String){
        targetImageString.value = iconName
    }
    /*================icon操作 - 结束===================*/



    /*================选择开始时间操作 - 开始===================*/
    private val _showDialog = MutableLiveData<CalendarHelperEvent>()
    val showDialog:LiveData<CalendarHelperEvent> get() = _showDialog//这种是计算属性，不去存储_showDialog的值，每次获取通过get计算返回值
    fun chooseStartTime(){
        val calendarHelperEvent = CalendarHelperEvent("startTime",
            dateStringToLong(targetStartTimeString.get()!!)
        )
        _showDialog.value = calendarHelperEvent
    }
    /*================选择开始时间操作 - 结束===================*/


    /*================选择结束时间操作 - 开始===================*/
    fun chooseEndTime(){
        val calendarHelperEvent = CalendarHelperEvent("endTime",
            dateStringToLong(targetEndTimeString.get()!!)
        )
        _showDialog.value = calendarHelperEvent
    }
    /*================选择结束时间操作 - 结束===================*/


    /*================选择目标状态操作 - 开始===================*/
    fun chooseStatus(displayName: String) {
        targetStatusString.set(displayName)
        statusInt = DayStatus.getValueFromDisplayName(displayName)!!
    }
    /*================选择目标状态操作 - 结束===================*/

    /*================是否提醒操作 - 开始===================*/

    /*================是否提醒操作 - 结束===================*/


    /*================数据入库操作 - 开始===================*/
    private val _target = MutableLiveData<TargetEntity>()
    var target:LiveData<TargetEntity>  = _target

    /**
     * @Author: ContentMy
     * @Description: 点击保存的回调函数进行数据入库。根据boolean值判断插入新数据还是更新已有的数据
     */
    fun saveTarget(isNewTarget:Boolean = true){
        if (targetTitleString.value.isNullOrEmpty()){
            //TODO: 后续封装log库，这里需要去给出log，目标名称不能为空，并且也要封装一个Toast去替换系统Toast，给出对应的提示给用户。并且现在小米会弹出携带应用名称的Toast，需要封装处理
            Toast.makeText(getApplication(),"目标名称不能为空",Toast.LENGTH_SHORT).show()
            return
        }

        if (endTimeLong != 0L && endTimeLong < startTimeLong){//0L代表永不结束
            Toast.makeText(getApplication(),"目标结束时间不能比目标开始时间早哦",Toast.LENGTH_SHORT).show()
            return
        }
        viewModelScope.launch {
            val target = TargetEntity(
                // 不设置 id，因为它是自动生成的
                id = if(isNewTarget) null else updateTarget.id,
                targetImg = targetImageString.value!!,
                targetTitle = targetTitleString.value!!,
                targetStartTime = startTimeLong,//这里没有从targetStartTimeString中实时获取，是因为本身存储了一个临时的startTimeLong用于计算，而且在目前的逻辑中，这个临时数据也满足了入库的要求
                targetEndTime = endTimeLong,
                targetStatus = statusInt,
                targetContent = if(targetContentString.value == null) "" else targetContentString.value!!,
                targetRemind = targetIsRemind.value!!
            )
            try {
                if (isNewTarget){
                    println("插入数据${target.toString()}")
                    targetRepository.insertTarget(target)
                }else{
                    println("更新数据${target.toString()}")
                    targetRepository.updateTarget(target)
                }
                _target.value = target//通知UI进行操作
            } catch (t:Throwable){
                t.printStackTrace()
            }
        }
    }

    /*================数据入库操作 - 结束===================*/


    /*================通知操作 - 开始===================*/

    fun startNotification(targetEntity: TargetEntity, application: Application){
//        notificationRepository.startNotification(1, application)
        val dataId = targetEntity.id
        val iconResId = getImageResourceId(targetEntity.targetImg)
        val title = targetEntity.targetTitle
        val content = targetEntity.targetContent
        val duration = 5*1000L //TODO:这里的写死数据是为了方便测试
        notificationRepository.postNotification(application,dataId!!,iconResId,title,content,duration)
    }

    fun cancelNotification(application:Application){
        notificationRepository.cancelNotification(application)
    }

    /*================通知操作 - 结束===================*/

    fun getCalendar(source: String, calendar: Calendar) {
        when (source) {
            "startTime" -> {
                startTimeLong = calendar.timeInMillis
                targetStartTimeString.set(calenderToDateString(calendar))
            }
            "endTime" -> {
                endTimeLong = calendar.timeInMillis
                targetEndTimeString.set(calenderToDateString(calendar))
            }
        }
    }
}

class TargetCreateViewModelFactory(
    private val repository: TargetRepository,
    private val notificationRepository: NotificationRepository,
    private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TargetCreateViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TargetCreateViewModel(repository,notificationRepository,application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}