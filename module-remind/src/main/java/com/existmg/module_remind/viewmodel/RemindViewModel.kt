package com.existmg.module_remind.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.existmg.library_common.viewmodel.BaseApplicationViewModel
import com.existmg.library_data.db.entity.RemindEntity
import com.existmg.library_data.repository.RemindRepository
import com.existmg.module_remind.utils.logs.RemindLoggerManager
import kotlinx.coroutines.launch

/**
 * @Author ContentMy
 * @Date 2024/4/15 11:25 PM
 * @Description 这里是提醒列表页面对应的viewmodel，主要是用于数据展示以及跳转的相关处理
 */
class RemindViewModel(private var repository: RemindRepository,application: Application) : BaseApplicationViewModel(application){
    private val mLog = RemindLoggerManager.getLogger<RemindViewModel>()
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
    private val _remindData = MutableLiveData<List<RemindEntity>>()
    /*
    这是一个只读的LiveData引用，它公开了_remindData中的数据，但是不允许直接修改。
    这个属性通常会被标记为public或open，意味着它可以从类的外部访问，例如在Activity或Fragment中观察数据变化。
    由于它是LiveData，因此只能被观察（使用observe方法），而不能被修改。这确保了数据的单向流动：从ViewModel到UI
    */
    val remindData: LiveData<List<RemindEntity>> = _remindData
    /*
    声明两个对象的原因
    第一次操作是在ViewModel内部，当你需要更新数据时。你会使用_remindData（MutableLiveData）来调用setValue或postValue方法，这样任何观察remindData的UI组件都会收到更新。
    第二次操作是在Activity或Fragment中，当你需要观察数据变化以更新UI时。你会使用remindData（LiveData）来调用observe方法，并在lambda表达式中处理数据更新。
    这种双层结构有助于保持数据的封装性和一致性，同时确保只有ViewModel可以修改数据，而UI组件只能观察数据变化。这是MVVM架构的一个核心原则，有助于实现清晰的数据流和易于测试的代码。
    */

    fun refreshData(){
        viewModelScope.launch {
            try {
                repository.getAllInProgressReminds().collect{
                    _remindData.value = it
                }
            } catch (e: Throwable) {
                mLog.error("数据库查询所有未完成提醒列表时出现异常：",e)
                e.printStackTrace()
            }
        }
    }

    fun deleteRemind(remindEntity: RemindEntity){
        viewModelScope.launch {
            try {
                repository.deleteRemind(remindEntity)
            } catch (e: Throwable) {
                mLog.error("数据库删除单个提醒时出现异常：",e)
                e.printStackTrace()
            }
        }
    }

    /*===================数据库的处理-结束=======================*/

    /*===================引导功能处理-开始=======================*/
    private val sharedPreferences = application.getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)

    private val _showGuide = MutableLiveData<Boolean>()
    val showGuide: LiveData<Boolean> get() = _showGuide

    fun checkIfFirstTime(activityName: String) {
        val isFirstTime = sharedPreferences.getBoolean(activityName, true)
//        if (isFirstTime) {
//            sharedPreferences.edit().putBoolean(activityName, false).apply()
//        }
        _showGuide.value = isFirstTime
    }
    /*===================引导功能处理-开始=======================*/


    /*===================优化引导的处理-开始=======================*/
    private val _optimizationShow = MutableLiveData<Boolean>()
    val optimizationShow : LiveData<Boolean> get() = _optimizationShow
    fun onOptimizationShow(){
        val isFirstTime = sharedPreferences.getBoolean("remindOptimization", true)
        _optimizationShow.value = isFirstTime
    }

    private val _optimizationClose = MutableLiveData<Boolean>()
    val optimizationClose : LiveData<Boolean> get() = _optimizationClose
    fun onOptimizationClose(){
        sharedPreferences.edit().putBoolean("remindOptimization", false).apply()
        _optimizationClose.value = true
    }

    private val _navigateToOptimizationActivity = MutableLiveData<Boolean>()
    val navigateToOptimizationActivity : LiveData<Boolean> get() = _navigateToOptimizationActivity

    fun onOptimizationJump(){
        _navigateToOptimizationActivity.value = true
    }
    /*===================优化引导的处理-结束=======================*/
}