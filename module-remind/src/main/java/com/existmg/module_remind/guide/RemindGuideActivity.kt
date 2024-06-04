package com.existmg.module_remind.guide

import android.app.Application
import android.graphics.Rect
import android.view.View
import android.view.ViewTreeObserver
import androidx.lifecycle.ViewModelProvider
import com.existmg.library_common.activity.BaseMVVMActivity
import com.existmg.library_common.managers.viewModelFactoryWithParams
import com.existmg.library_common.utils.ToastUtil
import com.existmg.module_remind.R
import com.existmg.module_remind.databinding.RemindActivityGuideBinding

/**
 * @Author ContentMy
 * @Date 2024/5/27 3:51 PM
 * @Description 提醒的引导功能有两步：
 * 第一步是处理添加提醒相关的ui操作
 *      添加按钮显示，完成列表按钮隐藏
 *          文字（用于介绍添加按钮的作用）
 *          箭头（指向添加按钮）
 *          按钮（点击进入下一步逻辑））
 * 第二步是处理完成提醒列表button的引导相关ui操作
 *      添加按钮隐藏，完成列表按钮显示
 *          文字（用于介绍完成列表按钮的作用）
 *          箭头（指向完成按钮）
 *          按钮（完成引导）
 *
 */
class RemindGuideActivity: BaseMVVMActivity<RemindGuideViewModel, RemindActivityGuideBinding>() {
    override fun getViewModelClass(): Class<RemindGuideViewModel> {
        return RemindGuideViewModel::class.java
    }

    override fun bindViewModel() {
       mBinding.viewModel = mViewModel
    }

    override fun provideViewModelFactory(): ViewModelProvider.Factory {
        return viewModelFactoryWithParams(application){
            RemindGuideViewModel(it[0] as Application)
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.remind_activity_guide
    }

    override fun initView() {
        showStepOne()
    }

    override fun initListener() {
        mBinding.remindGuideBtnStepOne.setOnClickListener{
            showStepTwo()
        }

        mBinding.remindGuideBtnStepTwo.setOnClickListener{
            mViewModel.closeRemindGuideWithSP("RemindGuideActivity")
            finish()
        }

        mBinding.remindGuideBtnClose.setOnClickListener{
            mViewModel.closeRemindGuideWithSP("RemindGuideActivity")
            finish()
        }

        mBinding.remindGuideFab.setOnClickListener{
            ToastUtil.showShort(this, "结束引导后可以创建喔~")
        }

        mBinding.remindGuideTitleToolbar.uiTitleToolbarIvRight.setOnClickListener{
            ToastUtil.showShort(this, "结束引导后可以查看喔~")
        }
    }

    override fun initObserver() {
        // 等待布局完成后获取 ImageView 的位置和尺寸
        mBinding.remindGuideFab.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                // 获取 ImageView 的位置和尺寸
                val rect = Rect()
                mBinding.remindGuideFab.getGlobalVisibleRect(rect)
                // 计算放大后的高亮区域尺寸和位置
                val scaleFactor = 1.2f
                val width = rect.width() * scaleFactor
                val height = rect.height() * scaleFactor
                val centerX = rect.exactCenterX()
                val centerY = rect.exactCenterY()
                val left = (centerX - width / 2).toInt()
                val top = (centerY - height / 2).toInt()
                // 设置高亮区域
                mBinding.remindGuideFabView.apply {
                    val layoutParams = this.layoutParams
                    layoutParams.width = width.toInt()
                    layoutParams.height = height.toInt()
                    this.layoutParams = layoutParams
                    this.x = left.toFloat()
                    this.y = top.toFloat()
                }

                // 只需要获取一次位置和尺寸，移除监听器
                mBinding.remindGuideFab.viewTreeObserver.removeOnGlobalLayoutListener(this)
            }
        })
    }
    private fun showStepOne(){
        mBinding.remindGuideFab.visibility = View.VISIBLE
        mBinding.remindGuideLlStepOne.visibility = View.VISIBLE
        mBinding.remindGuideFabView.visibility = View.VISIBLE
    }

    private fun showStepTwo(){
        mBinding.remindGuideFab.visibility = View.GONE
        mBinding.remindGuideLlStepOne.visibility = View.GONE
        mBinding.remindGuideFabView.visibility = View.GONE
        mBinding.remindGuideTitleToolbar.uiTitleToolbarIvRight.visibility = View.VISIBLE
        mBinding.remindGuideTitleToolbar.uiTitleToolbarViewRight.visibility = View.VISIBLE
        mBinding.remindGuideTitleToolbar.uiTitleToolbarIvRight.setImageResource(R.drawable.ui_more_menu)
        mBinding.remindGuideLlStepTwo.visibility = View.VISIBLE
    }
}