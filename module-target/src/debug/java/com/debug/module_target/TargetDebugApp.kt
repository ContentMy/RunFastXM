package com.debug.module_target

import com.existmg.library_base.application.BaseApplication
import com.existmg.library_data.accessor.TargetModuleRoomAccessor
import com.existmg.library_data.db.database.AppDatabase
import com.existmg.library_data.repository.TargetRepository

/**
 * @Author ContentMy
 * @Date 2024/4/15 1:31 AM
 * @Description
 */
class TargetDebugApp: BaseApplication() {
    override fun onCreate() {
        super.onCreate()
        /*==============数据库初始化==================*/
        val database by lazy { AppDatabase.getDatabase(this) }
        val repository by lazy { TargetRepository(database.targetDao(),database.targetCheckInDao()) }

        //暂时使用接口回调将repository传回module
        TargetModuleRoomAccessor.onGetTargetRepositoryCallback = object :
            TargetModuleRoomAccessor.OnGetTargetRepositoryCallback {
            override fun onGetTargetRepository(): TargetRepository {
                return repository
            }
        }
    }
}