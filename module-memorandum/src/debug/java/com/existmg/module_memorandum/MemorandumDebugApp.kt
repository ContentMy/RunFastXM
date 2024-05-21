package com.existmg.module_memorandum

import com.existmg.library_base.application.BaseApplication
import com.existmg.library_data.accessor.MemorandumModuleRoomAccessor
import com.existmg.library_data.db.database.AppDatabase
import com.existmg.library_data.repository.MemorandumRepository

/**
 * @Author ContentMy
 * @Date 2024/4/15 1:31 AM
 * @Description
 */
class MemorandumDebugApp: BaseApplication() {
    override fun onCreate() {
        super.onCreate()
        /*==============数据库初始化==================*/
        val database by lazy { AppDatabase.getDatabase(this) }
        val repository by lazy { MemorandumRepository(database.memorandumDao(),database.memorandumImgDao()) }

        //暂时使用接口回调将repository传回module
        MemorandumModuleRoomAccessor.onGetMemorandumRepositoryCallback = object :
            MemorandumModuleRoomAccessor.OnGetMemorandumRepositoryCallback {
            override fun onGetMemorandumRepository(): MemorandumRepository {
                return repository
            }
        }
    }
}