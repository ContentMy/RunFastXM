package com.existmg.module_memorandum.viewmodel

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.existmg.library_base.viewmodel.BaseApplicationViewModel
import com.existmg.library_common.utils.ToastUtil
import com.existmg.library_data.db.entity.MemorandumEntity
import com.existmg.library_data.db.entity.MemorandumImgEntity
import com.existmg.library_data.repository.MemorandumRepository
import com.existmg.module_memorandum.model.MemorandumImageItem
import com.existmg.module_memorandum.utils.logs.MemorandumLoggerManager
import kotlinx.coroutines.launch

/**
 * @Author ContentMy
 * @Date 2024/4/28 11:18 PM
 * @Description 新建心情页面对应的viewmodel，做了数据入库的一些操作
 */
class MemorandumCreateViewModel(
    private var repository:MemorandumRepository,
    application: Application):BaseApplicationViewModel(application) {
    private val mLog = MemorandumLoggerManager.getLogger<MemorandumCreateViewModel>()
    var memorandumTitleString = MutableLiveData<String>()

    var memorandumContentString = MutableLiveData<String>()

    private var _finishActivity = MutableLiveData<Boolean>()
    val finishActivity:LiveData<Boolean> get() = _finishActivity
    fun insertMemorandum(imgList: List<MemorandumImageItem>) {
        if (memorandumTitleString.value.isNullOrEmpty()){
            ToastUtil.showShort(getApplication(), "记录下心情才能保存哦")
            return
        }
        viewModelScope.launch {
            try {
                val memorandumEntity = MemorandumEntity(
                    memorandumTitle = memorandumTitleString.value!!,
                    memorandumContent = memorandumContentString.value ?: "",
                    memorandumCreateTime = System.currentTimeMillis()
                )
                val imgEntities = imgList.map {
                    MemorandumImgEntity(
                        memorandumImgFilePath = it.uri.toString()
                    )
                }
                repository.insertMemorandumWithImg(memorandumEntity,imgEntities)
                _finishActivity.value = true
            } catch (e: Exception) {
                mLog.error("数据库插入单个日记数据时发生异常：",e)
                e.printStackTrace()
            }
        }
    }
}