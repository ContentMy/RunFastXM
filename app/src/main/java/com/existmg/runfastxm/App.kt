package com.existmg.runfastxm

import com.existmg.library_base.application.BaseApplication
import com.existmg.library_data.accessor.MemorandumModuleRoomAccessor
import com.existmg.library_data.accessor.RemindModuleRoomAccessor
import com.existmg.library_data.repository.RemindRepository
import com.existmg.library_data.accessor.TargetModuleRoomAccessor
import com.existmg.library_data.db.database.AppDatabase
import com.existmg.library_data.repository.MemorandumRepository
import com.existmg.library_data.repository.TargetRepository


/**
 * @Author ContentMy
 * @Date 2024/3/23 4:41 下午
 * @Description
 */
class App: BaseApplication() {
    override fun onCreate() {
        super.onCreate()
        // 初始化需要初始化的组件
//        ModuleLifecycleConfig.getInstance().initModuleAhead(this)

        /*==============数据库初始化==================*/ // TODO: 考虑将数据库初始化放在子线程处理或者放到各个模块中各自初始化，目前统一初始化在app壳会导致冷启动时间过长
        val database by lazy { AppDatabase.getDatabase(this) }
        val remindRepository by lazy { RemindRepository(database.remindDao()) }
        val targetRepository by lazy { TargetRepository(database.targetDao()) }
        val memorandumRepository by lazy { MemorandumRepository(database. memorandumDao()) }

        //暂时使用接口回调将repository传回module
        RemindModuleRoomAccessor.onGetRemindRepositoryCallback = object :
            RemindModuleRoomAccessor.OnGetRemindRepositoryCallback {
            override fun onGetRemindRepository(): RemindRepository {
                return  remindRepository
            }
        }
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
//        //初始化查看数据库的依赖库 //chrome://inspect/#devices
//        Stetho.initializeWithDefaults(this)
    }
}