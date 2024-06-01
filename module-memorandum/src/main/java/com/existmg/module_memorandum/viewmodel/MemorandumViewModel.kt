package com.existmg.module_memorandum.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.existmg.library_base.viewmodel.BaseViewModel
import com.existmg.library_data.db.entity.MemorandumWithImagesEntity
import com.existmg.library_data.repository.MemorandumRepository
import com.existmg.module_memorandum.utils.logs.MemorandumLoggerManager
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

/**
 * @Author ContentMy
 * @Date 2024/4/28 11:16 PM
 * @Description 记录生活入口页面对应的viewmodel，主要是做了数据展示的相关操作
 */
class MemorandumViewModel(private var repository: MemorandumRepository):BaseViewModel() {
    private val mLog = MemorandumLoggerManager.getLogger<MemorandumViewModel>()
    /*========================页面跳转管理============================*/
    //核心思想通过标志位来完成是否跳转的响应处理
    private val _navigateToCreateMemorandumActivity = MutableLiveData<Boolean>()
    val navigateToCreateMemorandumActivity : LiveData<Boolean> get() = _navigateToCreateMemorandumActivity
    fun onFabClick(){
        _navigateToCreateMemorandumActivity.value = true//将标志位置为true，观察到值变化，页面会进行跳转处理
    }

    fun restoreFAB() {
        _navigateToCreateMemorandumActivity.value = false//重置标志位
    }

    /*========================Dialog管理============================*/
    private val _navigateShowDialog = MutableLiveData<Boolean>()
    val navigateShowDialog : LiveData<Boolean> get() = _navigateShowDialog
    fun showDialog(){
        _navigateShowDialog.value = true
    }

    fun restoreDialog(){
        _navigateShowDialog.value = false
    }
    /*========================数据库管理============================*/

    private val _memorandumData = MutableLiveData<List<MemorandumWithImagesEntity>>()
    val memorandumData: LiveData<List<MemorandumWithImagesEntity>> = _memorandumData
    private var hasCollectedData = false
    fun resetDataCollectionFlag() {
        hasCollectedData = false
    }
    fun refreshData() {
        if (hasCollectedData){
            return
        }
        mLog.debug("try to query data")
        viewModelScope.launch {
            try {
                repository.getAllMemorandumWithImg()
                    .distinctUntilChanged() // 确保仅在数据实际变动时才触发
                    .collect{
                        mLog.debug("获取到了数据${it.size}")
                        it.forEach {
                            mLog.debug(it.toString())
                        }
                        _memorandumData.value = it
                    }
                hasCollectedData = true
            }catch (e:Throwable){
                mLog.error("从数据库中获取数据列表时发生了异常：", e)
                e.printStackTrace()
            }
        }
    }

    fun deleteMemorandum(entity: MemorandumWithImagesEntity) {
        viewModelScope.launch {
            try {
//                repository.deleteMemorandum(entity)
            }catch (e:Throwable){
                mLog.error("从数据库中删除数据时发生了异常：", e)
                e.printStackTrace()
            }
        }
    }
    /*========================生命周期管理============================*/
}