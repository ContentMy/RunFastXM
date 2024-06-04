package com.existmg.library_ui.views

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.LinearLayout

/**
 * @Author ContentMy
 * @Date 2024/4/30 6:04 PM
 * @Description 这里是针对点击处理的LinearLayout类，主要为了处理dialog样式的activity中的点击不同位置下的对应逻辑操作时间点
 */
class CustomLinearLayout(context: Context, attrs: AttributeSet? = null) : LinearLayout(context, attrs) {

    private var onOutsideClickListener: OnOutsideClickListener? = null

    fun setOnOutsideClickListener(listener: OnOutsideClickListener) {
        this.onOutsideClickListener = listener
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        // 不拦截触摸事件，让子控件自己处理触摸事件
        return false
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
//        println("测试回调${event.action}")
        when(event.action){
            MotionEvent.ACTION_DOWN->{
                return true
            }
            MotionEvent.ACTION_UP->{
//                println("测试up")
                // 获取点击位置的坐标
                val x = event.x.toInt()
                val y = event.y.toInt()

                // 判断点击位置是否在 CustomLinearLayout 内部
                if (x >= 0 && x <= width && y >= 0 && y <= height) {
//                    println("测试up条件1：$x,$y")
                    // 获取点击位置处的子视图
                    val targetView = getChildAt(getChildIndexAtPosition(x, y))
//                    println("测试up条件2：$x,$y")
                    // 判断点击的子视图是否是 CustomLinearLayout 本身
                    return if (targetView == null) {
//                        println("测试up条件3：$x,$y,这里是null")
                        // 如果点击了 CustomLinearLayout 内部，并且点击的是 CustomLinearLayout 本身
                        // 执行回调，关闭 Activity
                        onOutsideClickListener?.onOutsideClick(this)
                        true // 返回 true，表示已处理触摸事件
                    }else{
//                        println("测试up条件4：$x,$y,${targetView.javaClass}")
                        false
                    }
                }
            }
        }
        return super.onTouchEvent(event)
    }

    private fun getChildIndexAtPosition(x: Int, y: Int): Int {
        // 获取点击位置处的子视图索引
        for (i in 0 until childCount) {
            val child = getChildAt(i)
            if (x >= child.left && x <= child.right && y >= child.top && y <= child.bottom) {
                return i
            }
        }
        return -1
    }

    interface OnOutsideClickListener {
        fun onOutsideClick(view: View)
    }
}
