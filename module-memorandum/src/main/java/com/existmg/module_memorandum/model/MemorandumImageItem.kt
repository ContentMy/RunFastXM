package com.existmg.module_memorandum.model

import android.net.Uri

/**
 * @Author ContentMy
 * @Date 2024/5/21 12:55 AM
 * @Description
 */
data class MemorandumImageItem(
    val uri: Uri? = null,
    val isPlaceholder: Boolean = false
)
