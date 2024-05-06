package com.existmg.module_remind

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.existmg.module_remind.ui.RemindFragment


class RemindDebugActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.remind_debug_activity)
        // 获取FragmentManager
        // 获取FragmentManager
        val fragmentManager: FragmentManager = supportFragmentManager

        // 开启一个Fragment事务

        // 开启一个Fragment事务
        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()

        // 创建并添加Fragment到容器

        // 创建并添加Fragment到容器
        val myFragment = RemindFragment()
        fragmentTransaction.add(R.id.remind_debug_main, myFragment)

        // 提交事务

        // 提交事务
        fragmentTransaction.commit()
    }
}