package com.existmg.module_main.ui

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.alibaba.android.arouter.facade.annotation.Route
import com.existmg.library_common.activity.BaseMVVMActivity
import com.existmg.library_common.managers.viewModelFactory
import com.existmg.library_common.router.RouterFragmentPath
import com.existmg.module_main.R
import com.existmg.module_main.databinding.MainActivityMainBinding
import com.existmg.module_main.ui.adapter.ViewPagerAdapter
import com.existmg.module_main.utils.logs.MainLoggerManager
import com.existmg.module_main.viewmodel.MainViewModel
import com.tencent.bugly.crashreport.CrashReport

/**
 * @Author:ContentMy
 * @Date: 2024/4/6 12:22 PM
 * @Description: 这里是项目入口,使用了ViewPager2+BottomNavigationView实现整体的ui架构，点击跳转到对应模块的fragment入口
 */

@Route(path = RouterFragmentPath.Main.PAGE_MAIN)
class MainActivity : BaseMVVMActivity<MainViewModel, MainActivityMainBinding>() {
    private lateinit var adapter: ViewPagerAdapter
//    private lateinit var vibrator: Vibrator
    private val mLog = MainLoggerManager.getLogger<MainActivity>()
    override fun beforeContentView() {
        //TODO:暂时放在MainActivity中初始化bugly
        CrashReport.initCrashReport(applicationContext, "a2cd585ea9", true);//boolean表示是否调试
    }
    override fun getLayoutId(): Int {
        return R.layout.main_activity_main
    }

    override fun getViewModelClass(): Class<MainViewModel> {
        return MainViewModel::class.java
    }

    override fun bindViewModel() {
        mBinding.viewModel = mViewModel
    }

    override fun provideViewModelFactory(): ViewModelProvider.Factory {
        return viewModelFactory {
            MainViewModel()
        }
    }

    override fun initView() {
//        vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator//震动器
        //绑定viewpager适配器
        adapter = ViewPagerAdapter(this)
        mBinding.mainVp2.adapter = adapter
    }

    override fun initData() {
    }

    override fun initListener() {
        // 设置BottomNavigationView的初始选择项
        mBinding.mainBotNav.setOnNavigationItemSelectedListener(mViewModel::onNavigationItemSelected)
        mViewModel.selectedItem.observe(this, Observer { selectedItemId ->
//            // 根据selectedItemId更新UI
//            viewPager.currentItem = selectedItemId
            mBinding.mainVp2.setCurrentItem(selectedItemId, false)//使用smoothScroll来判断是否需要平滑滑动动画
        })

        // 设置ViewPager
        mBinding.mainVp2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
//                vibrator.vibrate(50)//添加点击震动
                mLog.debug("页面选择$position")
                mLog.debug("值为：" + mViewModel.selectedItem.value)
                mViewModel.selectedItem.value = position
                when (position) {
                    0 -> mBinding.mainBotNav.selectedItemId = R.id.navigation_remind
                    1 -> mBinding.mainBotNav.selectedItemId = R.id.navigation_target
                    2 -> mBinding.mainBotNav.selectedItemId = R.id.navigation_memorandum
                    3 -> mBinding.mainBotNav.selectedItemId = R.id.navigation_user
                }
            }
        })
        // 设置ViewPager动画效果
//        viewPager.setPageTransformer(object : ViewPager2.PageTransformer{
//            override fun transformPage(page: View, position: Float) {
//            }
//        })
        //禁止滑动
        mBinding.mainVp2.isUserInputEnabled = false
        //将默认页面值为首页
        mBinding.mainVp2.currentItem = 0
    }
}