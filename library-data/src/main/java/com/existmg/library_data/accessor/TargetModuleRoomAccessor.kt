package com.existmg.library_data.accessor

import com.existmg.library_data.repository.TargetRepository

/**
 * @Author ContentMy
 * @Date 2024/4/11 10:05 PM
 * @Description 这里是目标模块的数据库访问层
 */
object TargetModuleRoomAccessor {
    var onGetTargetRepositoryCallback: OnGetTargetRepositoryCallback? = null

    fun getTargetRepository(): TargetRepository {

        if (onGetTargetRepositoryCallback == null) {

            throw IllegalArgumentException("onGetDaoCallback must not be null!!")

        }

        return onGetTargetRepositoryCallback!!.onGetTargetRepository()

    }

    interface OnGetTargetRepositoryCallback {

        fun onGetTargetRepository(): TargetRepository

    }

}
