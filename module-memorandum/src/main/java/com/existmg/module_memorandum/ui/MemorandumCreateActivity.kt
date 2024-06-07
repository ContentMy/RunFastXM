package com.existmg.module_memorandum.ui

import android.app.Application
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.existmg.library_common.activity.BaseMVVMActivity
import com.existmg.library_common.managers.viewModelFactoryWithParams
import com.existmg.library_data.accessor.MemorandumModuleRoomAccessor
import com.existmg.library_data.repository.MemorandumRepository
import com.existmg.library_ui.views.CustomLinearLayout
import com.existmg.module_memorandum.R
import com.existmg.module_memorandum.databinding.MemorandumLayoutActivityMemorandumCreateBinding
import com.existmg.module_memorandum.ui.adapter.MemorandumCreateImgRecycleViewAdapter
import com.existmg.module_memorandum.viewmodel.MemorandumCreateViewModel


/**
 * @Author ContentMy
 * @Date 2024/4/28 1:49 PM
 * @Description 记录生活的新建记录内容页面
 * TODO:方案一：待解决，activity作为dialog去使用时，不会占满屏幕的问题。
 * 方案二：目前是完全按照activity的方案做的，只不过设置了透明模糊以及根布局点击finish来达到效果
 * 最新：更改页面样式，不再使用仿dialog的样式来做，因为增加了图片选择与展示，页面需要滑动，而这种ui在dialog这种看着滑动比较突兀，先改为正常的activity展示
 */
class MemorandumCreateActivity : BaseMVVMActivity<MemorandumCreateViewModel, MemorandumLayoutActivityMemorandumCreateBinding>(){
    private lateinit var mAdapter:MemorandumCreateImgRecycleViewAdapter
    private val pickImageLauncher =
        registerForActivityResult(ActivityResultContracts.OpenDocument()) { uri: Uri? ->
            uri?.let {
                val takeFlags = Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                contentResolver.takePersistableUriPermission(it, takeFlags)
                mAdapter.addImage(it)
            }
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
        mAdapter = MemorandumCreateImgRecycleViewAdapter(
            this,
            onAddImageClick = {
                pickImageLauncher.launch(arrayOf("image/*"))
            },
            onDeleteImageClick = { position ->
                mAdapter.deleteImage(position)
            }
        )
        val linearLayoutManager = GridLayoutManager(this,3,GridLayoutManager.VERTICAL,false)
        mBinding.memorandumCreateRv.layoutManager = linearLayoutManager
        mBinding.memorandumCreateRv.adapter = mAdapter
        mBinding.memorandumCreateToolbar.uiToolbarBtnSave.visibility = View.VISIBLE
    }

    override fun initData() {
        mBinding.memorandumCreateToolbar.uiToolbarTvTitle.text = resources.getString(R.string.memorandum_create_title_string)

    }

    override fun initListener() {
        mBinding.memorandumCreateLl.setOnOutsideClickListener(object : CustomLinearLayout.OnOutsideClickListener{
            override fun onOutsideClick(view: View) {
//                    finish()
                hideSoftInput(mBinding.memorandumCreateEtTitle)
                overridePendingTransition(R.anim.ui_alpha_show,R.anim.ui_alpha_hide)//解决退出activity时黑屏的问题
            }
        })

        mBinding.memorandumCreateCl.setOnClickListener{
            hideSoftInput(it)
            mBinding.memorandumCreateEtTitle.clearFocus()
            mBinding.memorandumCreateEtContent.clearFocus()
        }

        mBinding.memorandumCreateToolbar.uiToolbarBtnSave.setOnClickListener {
            try {
                mViewModel.insertMemorandum(mAdapter.getImgList())
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        mBinding.memorandumCreateToolbar.uiToolbarIvBack.setOnClickListener {
            finish()
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
        showSoftInput(mBinding.memorandumCreateEtTitle)
    }

    override fun finish() {
        super.finish()
        hideSoftInput(mBinding.memorandumCreateEtTitle)
        overridePendingTransition(R.anim.ui_alpha_show,R.anim.ui_alpha_hide)//解决退出activity时黑屏的问题
    }

}