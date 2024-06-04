package com.existmg.library_common.managers

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

/**
 * @Author ContentMy
 * @Date 2024/4/29 11:34 AM
 * @Description
 */
inline fun <reified VM : ViewModel> viewModelFactory(crossinline provider: () -> VM): ViewModelProvider.Factory {
    return object : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(VM::class.java)) {
                return provider() as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}

inline fun <reified VM : ViewModel> viewModelFactoryWithParams(vararg params: Any, crossinline provider: (Array<Any>) -> VM): ViewModelProvider.Factory {
    return object : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(VM::class.java)) {
                return provider(params as Array<Any>) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}