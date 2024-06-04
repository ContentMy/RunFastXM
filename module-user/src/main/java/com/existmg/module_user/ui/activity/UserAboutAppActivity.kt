package com.existmg.module_user.ui.activity

import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.existmg.library_common.activity.BaseMVVMActivity
import com.existmg.library_common.managers.viewModelFactory
import com.existmg.library_common.utils.getAppVersionName
import com.existmg.module_user.R
import com.existmg.module_user.databinding.UserLayoutAboutAppActivityBinding
import com.existmg.module_user.viewmodel.UserAboutAppViewModel

/**
 * @Author ContentMy
 * @Date 2024/5/11 12:30 AM
 * @Description
 */
class UserAboutAppActivity:
    BaseMVVMActivity<UserAboutAppViewModel, UserLayoutAboutAppActivityBinding>(),
    View.OnClickListener {
    override fun getViewModelClass(): Class<UserAboutAppViewModel> {
        return UserAboutAppViewModel::class.java
    }

    override fun bindViewModel() {
        mBinding.viewModel = mViewModel
    }

    override fun provideViewModelFactory(): ViewModelProvider.Factory {
        return viewModelFactory {
            UserAboutAppViewModel()
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.user_layout_about_app_activity
    }

    override fun isLightStatusBar(): Boolean {
        return false
    }
    override fun initView() {
        mBinding.userAboutAppToolbar.uiToolbarIvBack.setImageResource(R.drawable.ui_top_back_white)
        mBinding.userAboutAppToolbar.uiToolbarTvTitle.text = resources.getText(R.string.user_setting_string_about_us)
        mBinding.userAboutAppToolbar.uiToolbarTvTitle.setTextColor(resources.getColor(R.color.ui_white))
        mBinding.userAboutAppVersion.text = "测试版 ${getAppVersionName(this)}"
    }

    override fun initListener() {
        mBinding.userAboutAppToolbar.uiToolbarIvBack.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v){
            mBinding.userAboutAppToolbar.uiToolbarIvBack->{
                finish()
            }
        }
    }

}