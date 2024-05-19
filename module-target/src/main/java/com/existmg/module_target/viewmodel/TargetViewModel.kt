package com.existmg.module_target.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.existmg.library_base.viewmodel.BaseViewModel
import com.existmg.library_common.utils.getEndTimeOfDay
import com.existmg.library_common.utils.getStartTimeOfDay
import com.existmg.library_data.db.entity.TargetCheckInEntity
import com.existmg.library_data.db.entity.TargetEntity
import com.existmg.library_data.db.entity.TargetWithCheckInList
import com.existmg.library_data.db.entity.TargetWithTodayCheckIn
import com.existmg.library_data.repository.TargetRepository
import kotlinx.coroutines.launch

/**
 * @Author ContentMy
 * @Date 2024/4/7 6:18 下午
 * @Description 这里是目标列表页面的对应viewmodel，主要是做了数据展示以及跳转的逻辑处理
 */
class TargetViewModel(private val repository: TargetRepository): BaseViewModel() {
    /*===================页面跳转的处理-开始=======================*/
    //核心思想通过标志位来完成是否跳转的响应处理
    private val _navigateToCreateTargetActivity = MutableLiveData<Boolean>()
    val navigateToCreateTargetActivity : LiveData<Boolean> get() = _navigateToCreateTargetActivity
    fun onFabClick(){
        _navigateToCreateTargetActivity.value = true//将标志位置为true，观察到值变化，页面会进行跳转处理
    }

    fun restoreFAB() {
        _navigateToCreateTargetActivity.value = false//重置标志位
    }
    /*===================页面跳转的处理-结束=======================*/

    /*===================数据库的处理-开始=======================*/
    // 使用MutableLiveData
    /*
    带有下划线的变量名是一种约定，表示这是一个私有的、受保护的属性，不应该直接从类的外部访问
    这是一个MutableLiveData的实例，它允许你在其内部修改数据（即调用setValue或postValue方法）。
    由于它是MutableLiveData，所以可以在其所在的类（通常是ViewModel）内部进行值的更新。
    通常在ViewModel中，你会看到这种命名约定，用以区分对外暴露的只读LiveData和对内可写的MutableLiveData
    */
    private val _targetData = MutableLiveData<List<TargetWithTodayCheckIn>>()
    /*
    这是一个只读的LiveData引用，它公开了_targetData中的数据，但是不允许直接修改。
    这个属性通常会被标记为public或open，意味着它可以从类的外部访问，例如在Activity或Fragment中观察数据变化。
    由于它是LiveData，因此只能被观察（使用observe方法），而不能被修改。这确保了数据的单向流动：从ViewModel到UI
    */
    val targetData: LiveData<List<TargetWithTodayCheckIn>> = _targetData
    /*
    声明两个对象的原因
    第一次操作是在ViewModel内部，当你需要更新数据时。你会使用_targetData（MutableLiveData）来调用setValue或postValue方法，这样任何观察targetData的UI组件都会收到更新。
    第二次操作是在Activity或Fragment中，当你需要观察数据变化以更新UI时。你会使用targetData（LiveData）来调用observe方法，并在lambda表达式中处理数据更新。
    这种双层结构有助于保持数据的封装性和一致性，同时确保只有ViewModel可以修改数据，而UI组件只能观察数据变化。这是MVVM架构的一个核心原则，有助于实现清晰的数据流和易于测试的代码。
    */
    /**
     * @Author: ContentMy
     * @Description: 这里是目标列表页面的获取数据的封装方法。根据调用时机（onResume），开启协程去数据库中查询
     * 现有的逻辑是展示当天的目标以及打卡情况，所以会传入封装好的分别获取当天开始时间以及结束时间的顶层方法，
     * 然后根据返回的数据更新到livedata中，交由观察数据view去展示
     */
    fun refreshData(){
        println("try to query data")
        viewModelScope.launch {
            try {
                println("读取数据库时的开始时间${getStartTimeOfDay()}\n结束时间${getEndTimeOfDay()}")
                repository.getAllTargetsWithTodayCheckIn(getStartTimeOfDay(), getEndTimeOfDay()).collect{
                    it.forEach {
                        println("这里是每日数据的记录$it")
                    }
                    _targetData.value = it
                }
            }catch (e:Throwable){
                println("从数据库中获取数据列表时发生了异常" + e.message)
                e.printStackTrace()
            }
        }
    }

    /**
     * @Author: ContentMy
     * @params: targetEntity 传入的需要删除的目标实体类
     * @Description:
     * 这里是目标列表页面左滑中的删除按钮的点击事件删除目标的对应封装方法，传入的是对应的item的实体类
     * 根据传入的实体类调用数据库的删除方法进行删除
     */
    fun deleteTarget(targetEntity: TargetEntity) {
        viewModelScope.launch {
            try {
                repository.deleteTarget(targetEntity)
            } catch (e: Exception) {
                println("从数据库中删除数据时发生了异常" + e.message)
                e.printStackTrace()
            }
        }
    }

    /**
     * @Author: ContentMy
     * @params: targetId 需要插入打卡数据对应目标id
     * @Description:
     * 这里是目标列表页面的打卡按钮的点击事件插入打卡数据的对应封装方法，传入的是点击的对应目标id。
     * 根据点击时的id，组合封装一个目标打卡实体类，用来插入到目标打卡的数据表中
     */
    fun checkInTarget(targetId:Int){//TODO:id字段在release上线前需要确认是否修改为long
        println("插入打卡数据时的id为：$targetId")
        viewModelScope.launch {
            try {
                val targetCheckInEntity = TargetCheckInEntity(
                    targetId = targetId,
                    targetCheckIn = true,
                    targetCheckInTime = System.currentTimeMillis()
                )
                repository.insertTargetCheckInEntity(targetCheckInEntity)
            } catch (e: Exception) {
                println("从数据库中添加打卡数据时发生了异常" + e.message)
                e.printStackTrace()
            }
        }
    }

    /**
     * @Author: ContentMy
     * @params: targetCheckInEntity 需要删除的目标打卡实体类
     * @Description:
     * 这里是目标列表页面的取消打卡的删除数据的封装方法，传入需要取消打卡的实体类，
     * 然后开启子线程(默认)的协程中，完成数据库的删除操作。
     * 目前取消打卡暂时使用了删除的这个封装方法，后续可能会更改为更新数据的封装方法
     */
    fun deleteCheckInTarget(targetCheckInEntity: TargetCheckInEntity){
        viewModelScope.launch {
            try {
                repository.deleteTargetCheckInEntity(targetCheckInEntity)
            } catch (e: Exception) {
                println("从数据库中删除打卡数据时发生了异常" + e.message)
                e.printStackTrace()
            }
        }
    }

    /**
     * @Author: ContentMy
     * @params: targetCheckInEntity 需要更新的目标打卡实体类
     * @Description:
     * 这里是目标列表页面的取消打卡的更新数据的封装方法，传入需要取消打卡的实体类，
     * 然后开启子线程(默认)的协程中，完成数据库的更新操作，将打卡状态置为false,打卡时间置为0
     */
    fun cancelCheckInTarget(targetCheckInEntity: TargetCheckInEntity){
        viewModelScope.launch {
            try {
                val targetCancelCheckInEntity = TargetCheckInEntity(
                    id = targetCheckInEntity.id,
                    targetId = targetCheckInEntity.targetId,
                    targetCheckIn = false,
                    targetCheckInTime = 0L
                )
                repository.updateTargetCheckInEntity(targetCancelCheckInEntity)
            } catch (e: Exception) {
                println("从数据库中删除打卡数据时发生了异常" + e.message)
                e.printStackTrace()
            }
        }
    }
    /*===================数据库的处理-结束=======================*/

}