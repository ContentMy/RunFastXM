package com.existmg.module_user.ui.activity

import androidx.lifecycle.ViewModelProvider
import com.existmg.library_common.activity.BaseMVVMActivity
import com.existmg.library_common.managers.viewModelFactory
import com.existmg.module_user.R
import com.existmg.module_user.databinding.UserLayoutPrivacyActivityBinding
import com.existmg.module_user.viewmodel.UserPrivacyViewModel

/**
 * @Author ContentMy
 * @Date 2024/6/3 12:36 AM
 * @Description
 */
class UserPrivacyActivity :
    BaseMVVMActivity<UserPrivacyViewModel, UserLayoutPrivacyActivityBinding>() {
    override fun getViewModelClass(): Class<UserPrivacyViewModel> {
        return UserPrivacyViewModel::class.java
    }

    override fun bindViewModel() {
        mBinding.viewModel = mViewModel
    }

    override fun provideViewModelFactory(): ViewModelProvider.Factory {
        return viewModelFactory {
            UserPrivacyViewModel()
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.user_layout_privacy_activity
    }

    override fun initData() {
        mBinding.userPrivacyToolbar.uiToolbarTvTitle.text = resources.getText(R.string.user_setting_string_privacy)
    }

    override fun initListener() {
        mBinding.userPrivacyToolbar.uiToolbarIvBack.setOnClickListener {
            finish()
        }
    }
}