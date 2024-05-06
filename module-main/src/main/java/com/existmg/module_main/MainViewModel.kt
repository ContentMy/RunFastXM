package com.existmg.module_main

import android.view.MenuItem
import androidx.lifecycle.MutableLiveData
import com.existmg.library_base.viewmodel.BaseViewModel

/**
 * @Author ContentMy
 * @Date 2024/4/6 11:59 下午
 * @Description
 */
class MainViewModel: BaseViewModel() {
    //BottomNavigationView的相关使用逻辑
    val items = MutableLiveData<List<MainDataModel>>(listOf(
        MainDataModel("标题1", "描述1"),
        MainDataModel("标题2", "描述2"),
        MainDataModel("标题1", "描述1"),
        MainDataModel("标题2", "描述2"),
        // 其他数据项
    ))//数据源

    var selectedItem = MutableLiveData<Int>(0)//初始化选择button的下标
    fun onNavigationItemSelected(item: MenuItem): Boolean{
        when (item.itemId) {
            R.id.navigation_remind -> {
                // 处理倒计时页面的选择
                selectedItem.value = 0
                return true
            }
            R.id.navigation_target -> {
                // 处理日程的选择
                selectedItem.value = 1
                return true
            }
            R.id.navigation_memorandum -> {
                // 处理备忘录的选择
                selectedItem.value = 2
                return true
            }
            R.id.navigation_user -> {
                // 处理个人设置的选择
                selectedItem.value = 3
                return true
            }
        }
        return false
    }
    //ViewPager的相关使用逻辑
    fun onPagerChange(){

    }
}