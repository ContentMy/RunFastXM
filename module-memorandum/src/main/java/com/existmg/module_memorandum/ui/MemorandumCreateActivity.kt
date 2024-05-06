package com.existmg.module_memorandum.ui

import android.app.Application
import android.content.Context
import android.os.Handler
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.lifecycle.ViewModelProvider
import com.existmg.library_base.activity.BaseMVVMActivity
import com.existmg.library_base.manager.viewModelFactoryWithParams
import com.existmg.library_data.accessor.MemorandumModuleRoomAccessor
import com.existmg.library_data.repository.MemorandumRepository
import com.existmg.library_ui.view.CustomLinearLayout
import com.existmg.module_memorandum.R
import com.existmg.module_memorandum.databinding.MemorandumLayoutActivityMemorandumCreateBinding
import com.existmg.module_memorandum.viewmodel.MemorandumCreateViewModel
import com.existmg.module_memorandum.viewmodel.MemorandumViewModel


/**
 * @Author ContentMy
 * @Date 2024/4/28 1:49 PM
 * @Description 记录生活的新建记录内容页面
 * TODO:方案一：待解决，activity作为dialog去使用时，不会占满屏幕的问题。
 * 方案二：目前是完全按照activity的方案做的，只不过设置了透明模糊以及根布局点击finish来达到效果
 */
class MemorandumCreateActivity : BaseMVVMActivity<MemorandumCreateViewModel,MemorandumLayoutActivityMemorandumCreateBinding>(){
    private val inputMethodManager:InputMethodManager? by lazy{
        getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    }

    override fun getLayoutId(): Int {
        return R.layout.memorandum_layout_activity_memorandum_create
    }

    override fun getViewModelClass(): Class<MemorandumCreateViewModel> {
        return MemorandumCreateViewModel::class.java
    }

    override fun bindViewModel() {
        mBinding.viewModel = mViewModel
    }

    override fun provideViewModelFactory(): ViewModelProvider.Factory {
        return viewModelFactoryWithParams(MemorandumModuleRoomAccessor.getMemorandumRepository(),application){
            MemorandumCreateViewModel(it[0] as MemorandumRepository,it[1] as Application)
        }
    }


    override fun initView() {

    }

    override fun initData() {

    }

    override fun initListener() {
        mBinding.memorandumCreateLl.setOnOutsideClickListener(object : CustomLinearLayout.OnOutsideClickListener{
            override fun onOutsideClick(view: View) {
                    finish()
            }
        })

        mBinding.memorandumCreateCl.setOnClickListener{
            hideSoftInput(it)
            mBinding.memorandumCreateEtTitle.clearFocus()
            mBinding.memorandumCreateEtContent.clearFocus()
        }

        mBinding.memorandumCreateBtnSave.setOnClickListener {
            try {
                mViewModel.insertMemorandum()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun initObserver() {
        mViewModel.finishActivity.observe(this){
            finish()
        }
    }

    override fun onResume() {
        super.onResume()
        window.setWindowAnimations(R.style.ui_ActivityDialogAlphaAnimation)
        inputMethodManager?.let {
            mBinding.memorandumCreateEtTitle.requestFocus()//不起作用，需要handle延时提醒，因为dialog和软键盘会冲突
            it.showSoftInput(mBinding.memorandumCreateEtTitle, InputMethodManager.SHOW_IMPLICIT)
        }
    }

    override fun finish() {
        super.finish()
        hideSoftInput(mBinding.memorandumCreateEtTitle)
        overridePendingTransition(R.anim.ui_alpha_show,R.anim.ui_alpha_hide)//解决退出activity时黑屏的问题
    }

    private fun hideSoftInput(v:View){//TODO:后续增加判断，如果软键盘显示的情况下再调用
        val manager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        manager.hideSoftInputFromWindow(v.windowToken, 0)
    }
}