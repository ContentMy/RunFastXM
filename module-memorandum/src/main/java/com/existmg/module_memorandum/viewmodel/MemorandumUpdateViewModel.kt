package com.existmg.module_memorandum.viewmodel

import android.app.Application
import android.widget.Toast
import androidx.databinding.ObservableField
import androidx.databinding.ObservableLong
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.existmg.library_common.viewmodel.BaseApplicationViewModel
import com.existmg.library_common.utils.ToastUtil
import com.existmg.library_common.utils.timeLongToString
import com.existmg.library_common.utils.timeLongToStringWithHourMinSec
import com.existmg.library_data.db.entity.MemorandumEntity
import com.existmg.library_data.db.entity.MemorandumImgEntity
import com.existmg.library_data.db.entity.MemorandumWithImagesEntity
import com.existmg.library_data.repository.MemorandumRepository
import com.existmg.module_memorandum.utils.logs.MemorandumLoggerManager
import kotlinx.coroutines.launch

/**
 * @Author ContentMy
 * @Date 2024/4/28 11:18 PM
 * @Description 更改心情页面的viewmodel，主要是做了更新相关逻辑内容操作
 */
class MemorandumUpdateViewModel(
    private var repository:MemorandumRepository,
    application: Application): BaseApplicationViewModel(application) {
    private val mLog = MemorandumLoggerManager.getLogger<MemorandumUpdateViewModel>()
    var memorandumTitleString = MutableLiveData<String>()

    var memorandumContentString = MutableLiveData<String>()

    var memorandumCreateTime = ObservableField<Long>()

    var memorandumTimeString = MutableLiveData<String>()

    var memorandumUpdateTime = ObservableField<Long>()

    private lateinit var updateMemorandum: MemorandumEntity

    private var _memorandumImgData = MutableLiveData<List<MemorandumImgEntity>>()
    val memorandumImgData:LiveData<List<MemorandumImgEntity>> get() = _memorandumImgData
    fun initMemorandumData(memorandumWithImagesEntity: MemorandumWithImagesEntity) {
        val memorandumEntity = memorandumWithImagesEntity.memorandumEntity!!
        updateMemorandum = memorandumEntity
        memorandumTitleString.value = memorandumEntity.memorandumTitle!!
        memorandumContentString.value = memorandumEntity.memorandumContent!!
        memorandumCreateTime.set(memorandumEntity.memorandumCreateTime)
        memorandumUpdateTime.set(memorandumEntity.memorandumUpdateTime)
        timeShowInString(memorandumEntity.memorandumCreateTime,memorandumEntity.memorandumUpdateTime)
        _memorandumImgData.value = memorandumWithImagesEntity.memorandumImgEntityList!!
    }

    fun updateMemorandum(){
        if (memorandumTitleString.value.isNullOrEmpty()){
            ToastUtil.showShort(getApplication(), "记录下心情才能保存哦")
            return
        }
        viewModelScope.launch {
            try {
                val memorandumEntity = MemorandumEntity(
                    id =  updateMemorandum.id,
                    memorandumTitle = memorandumTitleString.value!!,
                    memorandumContent = if(memorandumContentString.value == null) "" else memorandumContentString.value!!,
                    memorandumCreateTime = memorandumCreateTime.get()!!,
                    memorandumUpdateTime = System.currentTimeMillis()
                )
                repository.updateMemorandum(memorandumEntity)
                timeShowInString(memorandumEntity.memorandumCreateTime,memorandumEntity.memorandumUpdateTime)
            } catch (e: Exception) {
                mLog.error("数据库更新单个日记数据时出现异常：",e)
                e.printStackTrace()
            }
        }
    }

    private fun timeShowInString(createTime:Long?,updateTime:Long?){
        if (createTime != 0L){
            if (updateTime == 0L){
                memorandumTimeString.value = "创建时间:${timeLongToStringWithHourMinSec(createTime!!)}"
            }else{
                memorandumTimeString.value = "创建时间:${timeLongToStringWithHourMinSec(createTime!!)}\n修改时间:${timeLongToStringWithHourMinSec(updateTime!!)}"
            }
        }else{
            memorandumTimeString.value = ""
        }
    }

}