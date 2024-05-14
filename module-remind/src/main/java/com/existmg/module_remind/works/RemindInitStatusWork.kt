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
class RemindInitStatusWork(
    context: Context,
    workerParams: WorkerParameters
    ) : Worker(context, workerParams) {

    override fun doWork(): Result {
        updateRemind()
        return Result.success()
    }

    private fun updateRemind() {
        val repository = RemindModuleRoomAccessor.getRemindRepository()
        CoroutineScope(Dispatchers.IO).launch {
            repository.getAllInProgressReminds().collect{ list ->
                list.forEach {
                    val endTime = it.remindEndTime
                    val isCompleted = if (endTime == 0L){//这里是防止旧数据和异常数据，如果有此类数据，在处理时被标记为true
                        true
                    }else if(endTime <= System.currentTimeMillis()){//这里是正常逻辑判断，如果提醒的结束时间已经满足条件，那么就认为是true
                        true
                    }else{
                        false
                    }

                    val remindEntity = RemindEntity(
                        id = it.id,
                        remindImg = it.remindImg,
                        remindTitle = it.remindTitle,
                        remindContent = it.remindContent,
                        remindTime = it.remindTime,
                        remindStartTime = it.remindStartTime,
                        remindEndTime = endTime,
                        remindCompleteStatus = isCompleted
                    )
                    repository.updateRemind(remindEntity)
                }
            }
        }
    }
}