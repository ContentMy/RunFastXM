package com.existmg.module_remind

import com.existmg.library_base.application.BaseApplication
import com.existmg.library_data.accessor.RemindModuleRoomAccessor
import com.existmg.library_data.db.database.AppDatabase
import com.existmg.library_data.repository.RemindRepository

/**
 * @Author ContentMy
 * @Date 2024/4/15 1:31 AM
 * @Description
 */
class RemindDebugApp: BaseApplication() {
    override fun onCreate() {
        super.onCreate()
        /*==============数据库初始化==================*/
        val database by lazy { AppDatabase.getDatabase(this) }
        val repository by lazy { RemindRepository(database.remindDao()) }

        //暂时使用接口回调将repository传回module
        RemindModuleRoomAccessor.onGetRemindRepositoryCallback = object :
            RemindModuleRoomAccessor.OnGetRemindRepositoryCallback {
            override fun onGetRemindRepository(): RemindRepository {
                return repository
            }
        }
    }
}