package com.existmg.module_memorandum.interfaces

import com.existmg.library_data.db.entity.MemorandumWithImagesEntity

/**
 * @Author ContentMy
 * @Date 2024/5/11 10:45 PM
 * @Description
 */
interface MemorandumItemDeleteCallback {
    fun itemDelete(entity: MemorandumWithImagesEntity, position: Int)
}