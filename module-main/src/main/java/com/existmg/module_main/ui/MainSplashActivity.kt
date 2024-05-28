package com.existmg.module_main.ui

import android.app.Application
import android.content.Intent
import android.os.Handler
import androidx.lifecycle.ViewModelProvider
import com.existmg.library_base.activity.BaseMVVMActivity
import com.existmg.library_base.manager.viewModelFactoryWithParams
import com.existmg.module_main.R
import com.existmg.module_main.databinding.MainLayoutActivitySplashMainBinding
import com.existmg.module_main.viewmodel.MainSplashViewModel

/**
 * @Author ContentMy
 * @Date 2024/5/28 5:18 PM
 * @Description
 * 这里是Splash页面，目前还是使用了布局的形式来完成启动页的ui展示
 * 后续解决方向：
 *          1.合成icon+文字的图片，用来设置在theme避免启动白屏
 *          2.继续调研SplashScreen的可能性，目前根据api设置较为死板，有没有其他ui图片提供，所以暂不使用SplashScreen
 */
class MainSplashActivity:BaseMVVMActivity<MainSplashViewModel,MainLayoutActivitySplashMainBinding>() {
    override fun getViewModelClass(): Class<MainSplashViewModel> {
        return MainSplashViewModel::class.java
    }

    override fun bindViewModel() {
        mBinding.viewModel = mViewModel
    }

    override fun provideViewModelFactory(): ViewModelProvider.Factory {
        return viewModelFactoryWithParams(application) {
            MainSplashViewModel(it[0] as Application)
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.main_layout_activity_splash_main
    }

    override fun initView() {
        Handler().postDelayed({
            mViewModel.checkIfFirstTime("MainSplashActivity")
        },1000)
    }

    override fun initObserver() {
        mViewModel.showGuide.observe(this){
            if(it){
                val intent = Intent(this, MainGuideActivity::class.java)
                startActivity(intent)
                finish()
            }else{
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }
}