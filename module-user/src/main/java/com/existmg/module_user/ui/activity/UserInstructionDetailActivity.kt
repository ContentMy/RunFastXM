package com.existmg.module_user.ui.activity

import androidx.lifecycle.ViewModelProvider
import com.existmg.library_base.activity.BaseMVVMActivity
import com.existmg.library_base.manager.viewModelFactory
import com.existmg.module_user.R
import com.existmg.module_user.databinding.UserLayoutInstructionDetailActivityBinding
import com.existmg.module_user.viewmodel.UserInstructionDetailViewModel

/**
 * @Author ContentMy
 * @Date 2024/6/3 1:10 AM
 * @Description
 */
class UserInstructionDetailActivity:
    BaseMVVMActivity<UserInstructionDetailViewModel, UserLayoutInstructionDetailActivityBinding>() {
    override fun getViewModelClass(): Class<UserInstructionDetailViewModel> {
        return UserInstructionDetailViewModel::class.java
    }

    override fun bindViewModel() {
        mBinding.viewModel = mViewModel
    }

    override fun provideViewModelFactory(): ViewModelProvider.Factory {
        return viewModelFactory {
            UserInstructionDetailViewModel()
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.user_layout_instruction_detail_activity
    }

    override fun initData() {
        val instructionTypeString = intent.getStringExtra("instructionType")
        if (!instructionTypeString.isNullOrEmpty())mViewModel.setInstructionType(instructionTypeString)
        mBinding.userInstructionDetailToolbar.uiToolbarTvTitle.text = resources.getText(R.string.user_setting_string_instructions)
    }

    override fun initObserver() {
        mViewModel.instructionType.observe(this){type->
            when(type){
                "remind" -> {
                    mBinding.userInstructionDetailToolbar.uiToolbarTvTitle.text = resources.getText(R.string.user_instruction_remind_string)
                    mBinding.userInstructionDetailWv.loadUrl("https://github.com/ContentMy/RunFastXM/blob/master/instructions/remindInstruction.md")
                }
                "target" -> {
                    mBinding.userInstructionDetailToolbar.uiToolbarTvTitle.text = resources.getText(R.string.user_instruction_target_string)
                    mBinding.userInstructionDetailWv.loadUrl("https://github.com/ContentMy/RunFastXM/blob/master/instructions/targetInstruction.md")
                }
                "memorandum" -> {
                    mBinding.userInstructionDetailToolbar.uiToolbarTvTitle.text = resources.getText(R.string.user_instruction_memorandum_string)
                    mBinding.userInstructionDetailWv.loadUrl("https://github.com/ContentMy/RunFastXM/blob/master/instructions/memorandumInstruction.md")
                }
            }
        }
    }
}
