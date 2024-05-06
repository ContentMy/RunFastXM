package com.existmg.library_data.accessor

import com.existmg.library_data.repository.MemorandumRepository


/**
 * @Author ContentMy
 * @Date 2024/4/11 10:05 PM
 * @Description 这是记录生活模块的数据库访问层
 */
object MemorandumModuleRoomAccessor {
    var onGetMemorandumRepositoryCallback: OnGetMemorandumRepositoryCallback? = null

    fun getMemorandumRepository(): MemorandumRepository {

        if (onGetMemorandumRepositoryCallback == null) {

            throw IllegalArgumentException("onGetDaoCallback must not be null!!")

        }

        return onGetMemorandumRepositoryCallback!!.onGetMemorandumRepository()

    }

    interface OnGetMemorandumRepositoryCallback {

        fun onGetMemorandumRepository(): MemorandumRepository

    }

}
