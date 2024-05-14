package com.existmg.module_remind.works

import android.content.Context
import androidx.work.Data
import androidx.work.OneTimeWorkRequest
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.existmg.library_data.accessor.RemindModuleRoomAccessor
import com.existmg.library_data.db.entity.RemindEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

/**
 * @Author:ContentMy
 * @Date: 2024/5/14 7:03 PM
 * @Description:
 */
class RemindStatusWork(
    context: Context,
    workerParams: WorkerParameters
    ) : Worker(context, workerParams) {
    companion object {
        fun buildWorkRequest(dataId:Int,duration:Long,endTime:Long): OneTimeWorkRequest {
            val inputData = Data.Builder()
                .putInt("dataId", dataId)
                .putLong("endTime", endTime)
                .build()

            return OneTimeWorkRequest.Builder(RemindStatusWork::class.java)
                .setInputData(inputData)
                .setInitialDelay(duration, TimeUnit.MILLISECONDS)
                .build()
        }
    }

    override fun doWork(): Result {
        val dataId = inputData.getInt("dataId",0)
        val endTime = inputData.getLong("endTime",0)
        println("执行到dowork ${System.currentTimeMillis() - endTime}  \n $dataId \n $endTime")
        if (System.currentTimeMillis() - endTime>= 0){
            updateRemind(dataId)
        }

        return Result.success()
    }

    private fun updateRemind(dataId: Int) {
        println("执行替换数据")
        val repository = RemindModuleRoomAccessor.getRemindRepository()
        CoroutineScope(Dispatchers.IO).launch {
            val entity = repository.getRemindById(dataId)
            val remindEntity = RemindEntity(
                id = entity.id,
                remindImg = entity.remindImg,
                remindTitle = entity.remindTitle,
                remindContent = entity.remindContent,
                remindTime = entity.remindTime,
                remindStartTime = entity.remindStartTime,
                remindEndTime = entity.remindEndTime,
                remindCompleteStatus = true
            )
            repository.updateRemind(remindEntity)
        }
    }
}