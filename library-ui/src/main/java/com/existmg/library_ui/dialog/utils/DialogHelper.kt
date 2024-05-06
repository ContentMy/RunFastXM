package com.existmg.library_ui.dialog.utils

import android.os.Handler
import android.os.Looper
import androidx.fragment.app.FragmentManager
import com.existmg.library_ui.dialog.fragment.DialogBottomFragment
import com.existmg.library_ui.dialog.fragment.DialogMemorandumFragment
import com.existmg.library_ui.dialog.interfaces.DialogDatabaseDelegate


/**
 * @Author ContentMy
 * @Date 2024/4/25 2:18 PM
 * @Description
 *  在Fragment中调用此方法，fragmentManager获取使用childFragmentManager
 *  在Activity中调用此方法，fragmentManager获取使用parentFragmentManager
 *  getFragmentManger已经被标记为过时
 */
private fun displayWithDelegate(fragmentManager: FragmentManager, delegateFunction: (DialogDatabaseDelegate) -> Unit) {
    val dialogFragment = DialogBottomFragment()
    val delegate = object : DialogDatabaseDelegate {
        override fun processBusinessLogic(data: Map<String, Any>) {
            delegateFunction(this)
        }
    }
    dialogFragment.setDialogDatabaseDelegate(delegate)
    dialogFragment.show(fragmentManager, DialogBottomFragment::class.java.simpleName)
}

fun displayDialogBottomFragment(fragmentManager: FragmentManager, delegateFunction: (DialogDatabaseDelegate) -> Unit) {
    val runnable = Runnable {
        displayWithDelegate(fragmentManager, delegateFunction)
    }

    // 判断当前线程是否为主线程
    if (Thread.currentThread() == Looper.getMainLooper().thread) {
        // 如果当前线程为主线程，直接执行
        runnable.run()
    } else {
        // 如果当前线程不是主线程，使用 Handler 将操作 post 到主线程执行
        Handler(Looper.getMainLooper()).post(runnable)
    }
}


private fun displayWithDelegate(fragmentManager: FragmentManager, delegateFunction:DialogDatabaseDelegate) {
    val dialogFragment = DialogBottomFragment()
    dialogFragment.setDialogDatabaseDelegate(delegateFunction)
    dialogFragment.show(fragmentManager, DialogBottomFragment::class.java.simpleName)
}

fun displayDialogBottomFragment(fragmentManager: FragmentManager, delegateFunction: DialogDatabaseDelegate) {
    val runnable = Runnable {
        displayWithDelegate(fragmentManager, delegateFunction)
    }

    // 判断当前线程是否为主线程
    if (Thread.currentThread() == Looper.getMainLooper().thread) {
        // 如果当前线程为主线程，直接执行
        runnable.run()
    } else {
        // 如果当前线程不是主线程，使用 Handler 将操作 post 到主线程执行
        Handler(Looper.getMainLooper()).post(runnable)
    }
}

private fun displayWithDelegateMemorandum(fragmentManager: FragmentManager, delegateFunction:DialogDatabaseDelegate) {
    val dialogFragment = DialogMemorandumFragment()
    dialogFragment.setDialogDatabaseDelegate(delegateFunction)
    dialogFragment.show(fragmentManager, DialogMemorandumFragment::class.java.simpleName)
}

fun displayDialogMemorandumFragment(fragmentManager: FragmentManager, delegateFunction: DialogDatabaseDelegate) {
    val runnable = Runnable {
        displayWithDelegateMemorandum(fragmentManager, delegateFunction)
    }

    // 判断当前线程是否为主线程
    if (Thread.currentThread() == Looper.getMainLooper().thread) {
        // 如果当前线程为主线程，直接执行
        runnable.run()
    } else {
        // 如果当前线程不是主线程，使用 Handler 将操作 post 到主线程执行
        Handler(Looper.getMainLooper()).post(runnable)
    }
}