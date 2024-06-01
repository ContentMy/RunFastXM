package com.existmg.module_remind.works

import android.content.Context
import androidx.work.Data
import androidx.work.OneTimeWorkRequest
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.existmg.library_data.accessor.RemindModuleRoomAccessor
import com.existmg.library_data.db.entity.RemindEntity
import com.existmg.module_remind.utils.logs.RemindLoggerManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.job
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
    private val mLog = RemindLoggerManager.getLogger<RemindInitStatusWork>()
    override fun doWork(): Result {
        updateRemind()
        return Result.success()
    }

    private fun updateRemind() {
        val repository = RemindModuleRoomAccessor.getRemindRepository()
        //TODO：bug 协程里的getAllInProgressReminds在数据库插入数据后，反复执行collect回调，导致数据库数据出现波动从而影响了列表的展示和交互。已解决：后续统一优化时记录并删除
        CoroutineScope(Dispatchers.IO).launch {
            repository.getAllInProgressReminds().collect{ list ->
                mLog.debug("初始化启动的work在搞事情")
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
                coroutineContext.job.cancel()//遍历完成，取消协程任务以避免数据库使用的问题
            }
        }
    }
}