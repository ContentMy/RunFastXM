package com.existmg.runfastxm

import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.alibaba.android.arouter.launcher.ARouter
import com.existmg.library_base.application.BaseApplication
import com.existmg.library_data.accessor.MemorandumModuleRoomAccessor
import com.existmg.library_data.accessor.RemindModuleRoomAccessor
import com.existmg.library_data.repository.RemindRepository
import com.existmg.library_data.accessor.TargetModuleRoomAccessor
import com.existmg.library_data.db.database.AppDatabase
import com.existmg.library_data.repository.MemorandumRepository
import com.existmg.library_data.repository.TargetRepository
import com.existmg.module_remind.works.RemindInitStatusWork
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch


/**
 * @Author ContentMy
 * @Date 2024/3/23 4:41 下午
 * @Description
 */
class App: BaseApplication() {
    companion object{
        private val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    }
    override fun onCreate() {
        super.onCreate()
        // 初始化需要初始化的组件
//        ModuleLifecycleConfig.getInstance().initModuleAhead(this)
        ARouter.init(this)
        /*==============数据库初始化==================*/ // TODO: 考虑将数据库初始化放在子线程处理或者放到各个模块中各自初始化，目前统一初始化在app壳会导致冷启动时间过长
        initDatabase()
    }

    private fun initDatabase(){
        /**
         * 这里使用了增量初始化的方案，数据库的初始化以及提醒模块的初始化因为是需要首个页面就会展示用到对应的数据，如果统一放到协程中初始化，可能会导致在获取数据时出现问题
         * 后续看使用情况看是否优化至协程内
         */
        val database by lazy { AppDatabase.getDatabase(this) }
        val remindRepository by lazy { RemindRepository(database.remindDao()) }
        //暂时使用接口回调将repository传回module
        RemindModuleRoomAccessor.onGetRemindRepositoryCallback = object :
            RemindModuleRoomAccessor.OnGetRemindRepositoryCallback {
            override fun onGetRemindRepository(): RemindRepository {
                return  remindRepository
            }
        }
        //TODO：遍历提醒列表修改提醒状态的任务，后续考虑通过反射或者其他手段在remind模块去初始化启动这个入口代码,模块单独执行时这里会报错
        val workRequest = OneTimeWorkRequest.Builder(RemindInitStatusWork::class.java).build()
        WorkManager.getInstance(this).enqueue(workRequest)

        applicationScope.launch{
            /**
             * 非首页的数据就可以放到协程中初始化，以减少application的初始化负担
             */
            val targetRepository by lazy { TargetRepository(database.targetDao(),database.targetCheckInDao()) }
            val memorandumRepository by lazy { MemorandumRepository(database. memorandumDao(),database.memorandumImgDao()) }

            //暂时使用接口回调将repository传回module
            TargetModuleRoomAccessor.onGetTargetRepositoryCallback = object :
                TargetModuleRoomAccessor.OnGetTargetRepositoryCallback {
                override fun onGetTargetRepository(): TargetRepository {
                    return targetRepository
                }
            }

            //暂时使用接口回调将repository传回module
            MemorandumModuleRoomAccessor.onGetMemorandumRepositoryCallback = object :
                MemorandumModuleRoomAccessor.OnGetMemorandumRepositoryCallback {
                override fun onGetMemorandumRepository(): MemorandumRepository {
                    return  memorandumRepository
                }
            }
        }
    }
}