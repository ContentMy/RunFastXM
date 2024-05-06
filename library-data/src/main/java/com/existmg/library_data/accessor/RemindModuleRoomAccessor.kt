package com.existmg.library_data.accessor

import com.existmg.library_data.repository.RemindRepository


/**
 * @Author ContentMy
 * @Date 2024/4/11 10:05 PM
 * @Description 这里是提醒模块的数据库访问层
 */
object RemindModuleRoomAccessor {
    var onGetRemindRepositoryCallback: OnGetRemindRepositoryCallback? = null

    fun getRemindRepository(): RemindRepository {

        if (onGetRemindRepositoryCallback == null) {

            throw IllegalArgumentException("onGetDaoCallback must not be null!!")

        }

        return onGetRemindRepositoryCallback!!.onGetRemindRepository()

    }

    interface OnGetRemindRepositoryCallback {

        fun onGetRemindRepository(): RemindRepository

    }

}
