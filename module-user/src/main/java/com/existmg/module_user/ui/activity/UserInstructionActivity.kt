package com.existmg.module_user.ui.activity

import android.content.Intent
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.existmg.library_base.activity.BaseMVVMActivity
import com.existmg.library_base.manager.viewModelFactory
import com.existmg.module_user.R
import com.existmg.module_user.databinding.UserLayoutInstructionActivityBinding
import com.existmg.module_user.viewmodel.UserInstructionViewModel

/**
 * @Author ContentMy
 * @Date 2024/6/3 12:58 AM
 * @Description
 */
class UserInstructionActivity:BaseMVVMActivity<UserInstructionViewModel,UserLayoutInstructionActivityBinding>(),
    View.OnClickListener {
    override fun getViewModelClass(): Class<UserInstructionViewModel> {
        return UserInstructionViewModel::class.java
    }

    override fun bindViewModel() {
        mBinding.viewModel = mViewModel
    }

    override fun provideViewModelFactory(): ViewModelProvider.Factory {
        return viewModelFactory {
            UserInstructionViewModel()
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.user_layout_instruction_activity
    }

    override fun initData() {
        mBinding.userInstructionToolbar.uiToolbarTvTitle.text = resources.getText(R.string.user_setting_string_instructions)
    }

    override fun initListener() {
        mBinding.userInstructionBtnRemind.setOnClickListener(this)
        mBinding.userInstructionBtnTarget.setOnClickListener(this)
        mBinding.userInstructionBtnMemorandum.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v){
            mBinding.userInstructionBtnRemind->{
                val intent = Intent(this, UserInstructionDetailActivity::class.java)
                intent.putExtra("instructionType","remind")
                startActivity(intent)
            }

            mBinding.userInstructionBtnTarget->{
                val intent = Intent(this, UserInstructionDetailActivity::class.java)
                intent.putExtra("instructionType","target")
                startActivity(intent)
            }

            mBinding.userInstructionBtnMemorandum->{
                val intent = Intent(this, UserInstructionDetailActivity::class.java)
                intent.putExtra("instructionType","memorandum")
                startActivity(intent)
            }
        }
    }
}