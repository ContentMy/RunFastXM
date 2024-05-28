package com.existmg.module_main.ui.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.alibaba.android.arouter.launcher.ARouter
import com.existmg.library_common.router.RouterFragmentPath
import com.existmg.module_main.viewmodel.MainViewModel

/**
 * @Author ContentMy
 * @Date 2024/4/7 12:08 上午
 * @Description
 */
class ViewPagerAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int {
        // 返回你的数据模型列表的大小
        return MainViewModel().items.value?.size ?: 0
    }


    override fun createFragment(position: Int): Fragment {
        //如果需要传值
//        val item = ViewPagerViewModel().items.value?.get(position) ?: return ViewPagerOneFragment()
//        return ViewPagerOneFragment().apply {
//            arguments = Bundle().apply {
//                putParcelable("item", item)
//            }
//        }
        return when(position){
//            try{
                0 -> ARouter.getInstance().build(RouterFragmentPath.Remind.PAGER_REMIND).navigation() as Fragment
                1 -> ARouter.getInstance().build(RouterFragmentPath.Target.PAGER_TARGET).navigation() as Fragment
                2 -> ARouter.getInstance().build(RouterFragmentPath.Memorandum.PAGER_MEMORANDUM).navigation() as Fragment
                3 -> ARouter.getInstance().build(RouterFragmentPath.User.PAGER_USER).navigation() as Fragment
                else -> throw IllegalArgumentException("Invalid position: $position")
//            }catch (t:Throwable){
//                t.printStackTrace()
//            }
        }
    }
}