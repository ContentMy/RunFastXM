package com.existmg.module_user.ui

import android.content.Intent
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.alibaba.android.arouter.facade.annotation.Route
import com.existmg.library_common.fragment.BaseMvvmFragment
import com.existmg.library_common.managers.viewModelFactory
import com.existmg.library_common.router.RouterFragmentPath
import com.existmg.module_user.R
import com.existmg.module_user.databinding.UserLayoutFragmentBinding
import com.existmg.module_user.ui.activity.UserAboutAppActivity
import com.existmg.module_user.ui.activity.UserInstructionActivity
import com.existmg.module_user.ui.activity.UserPrivacyActivity
import com.existmg.module_user.ui.activity.UserRemindOptimizationActivity
import com.existmg.module_user.viewmodel.UserViewModel

/**
 * @Author ContentMy
 * @Date 2024/4/7 1:33 上午
 * @Description 这里是个人设置模块的入口Fragment
 */
@Route(path = RouterFragmentPath.User.PAGER_USER)
class UserFragment: BaseMvvmFragment<UserViewModel, UserLayoutFragmentBinding>(),
    View.OnClickListener {
    override fun getViewModelClass(): Class<UserViewModel> {
        return UserViewModel::class.java
    }

    override fun bindViewModel() {
        mBinding.viewModel = mViewModel
    }

    override fun provideViewModelFactory(): ViewModelProvider.Factory {
        return viewModelFactory{
            UserViewModel()
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.user_layout_fragment
    }

    override fun initView() {

    }

    override fun initData() {
        mBinding.userIncludeTitleToolbar.uiTitleToolbarTvTitle.text = "个人设置"
    }

    override fun initListener() {
        mBinding.userLlAbout.setOnClickListener(this)
        mBinding.userLlPrivacy.setOnClickListener(this)
        mBinding.userLlInstructions.setOnClickListener(this)
        mBinding.userLlOptimization.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v){
            mBinding.userLlAbout->{
                //跳转到应用信息页面
                startActivity(Intent(requireContext(),UserAboutAppActivity::class.java))
            }
            mBinding.userLlPrivacy->{
                //跳转到隐私页面
                startActivity(Intent(requireContext(),UserPrivacyActivity::class.java))
            }
            mBinding.userLlInstructions->{
                //跳转到说明页面
                startActivity(Intent(requireContext(), UserInstructionActivity::class.java))
            }
            mBinding.userLlOptimization->{
                //跳转到提醒优化页面
                startActivity(Intent(requireContext(), UserRemindOptimizationActivity::class.java))
            }
        }
    }


}