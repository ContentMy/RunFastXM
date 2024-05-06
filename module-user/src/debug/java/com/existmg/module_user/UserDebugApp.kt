package com.existmg.module_user

import com.existmg.library_base.application.BaseApplication

/**
 * @Author ContentMy
 * @Date 2024/4/15 1:31 AM
 * @Description
 */
class UserDebugApp: BaseApplication() {
    override fun onCreate() {
        super.onCreate()
        /*==============数据库初始化==================*/
//        val database by lazy { TargetDatabase.getDatabase(this) }
//        val repository by lazy { TargetRepository(database.targetDao()) }
//
//        //暂时使用接口回调将repository传回module
//        TargetModuleRoomAccessor.onGetTargetRepositoryCallback = object :
//            TargetModuleRoomAccessor.OnGetTargetRepositoryCallback {
//            override fun onGetTargetRepository(): TargetRepository {
//                return repository
//            }
//        }
    }
}