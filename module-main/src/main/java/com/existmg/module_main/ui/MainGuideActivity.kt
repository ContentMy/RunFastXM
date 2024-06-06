package com.existmg.module_main.ui

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.content.Intent
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.existmg.library_common.activity.BaseMVVMActivity
import com.existmg.library_common.managers.viewModelFactory
import com.existmg.library_common.utils.setOnSingleClickListener
import com.existmg.module_main.R
import com.existmg.module_main.databinding.MainLayoutActivityGuideMainBinding
import com.existmg.module_main.viewmodel.MainGuideViewModel

/**
 * @Author ContentMy
 * @Date 2024/5/28 8:43 PM
 * @Description
 */
class MainGuideActivity: BaseMVVMActivity<MainGuideViewModel, MainLayoutActivityGuideMainBinding>() {
    override fun getViewModelClass(): Class<MainGuideViewModel> {
        return MainGuideViewModel::class.java
    }

    override fun bindViewModel() {
        mBinding.viewModel = mViewModel
    }

    override fun provideViewModelFactory(): ViewModelProvider.Factory {
        return viewModelFactory {
            MainGuideViewModel()
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.main_layout_activity_guide_main
    }

    override fun initListener() {
        mBinding.mainGuideIvStepOneNext.setOnSingleClickListener{
            showStepTwo()
        }

        mBinding.mainGuideIvStepTwoNext.setOnSingleClickListener {
            showStepThree()
        }

        mBinding.mainGuideBtnStepThreeStart.setOnSingleClickListener {
            // TODO: 封装到基类中
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
    private fun showStepTwo(){
        val fadeOut = ObjectAnimator.ofFloat(mBinding.mainGuideClStepOne, "alpha", 1f, 0f)
        fadeOut.duration = 200 // 设置动画持续时间
        fadeOut.start()

        fadeOut.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                mBinding.mainGuideClStepOne.visibility = View.GONE
                mBinding.mainGuideClStepTwo.visibility = View.VISIBLE

                val fadeIn = ObjectAnimator.ofFloat(mBinding.mainGuideClStepTwo, "alpha", 0f, 1f)
                fadeIn.duration = 200 // 设置动画持续时间
                fadeIn.start()
            }
        })
    }

    private fun showStepThree(){
        val fadeOut = ObjectAnimator.ofFloat(mBinding.mainGuideClStepTwo, "alpha", 1f, 0f)
        fadeOut.duration = 200 // 设置动画持续时间
        fadeOut.start()

        fadeOut.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                mBinding.mainGuideClStepTwo.visibility = View.GONE
                mBinding.mainGuideClStepThree.visibility = View.VISIBLE

                val fadeIn = ObjectAnimator.ofFloat(mBinding.mainGuideClStepThree, "alpha", 0f, 1f)
                fadeIn.duration = 200 // 设置动画持续时间
                fadeIn.start()
            }
        })
    }

}