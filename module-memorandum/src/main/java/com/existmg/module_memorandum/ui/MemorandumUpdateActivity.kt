package com.existmg.module_memorandum.ui

import android.app.Application
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.existmg.library_base.activity.BaseMVVMActivity
import com.existmg.library_base.manager.viewModelFactoryWithParams
import com.existmg.library_data.accessor.MemorandumModuleRoomAccessor
import com.existmg.library_data.db.entity.MemorandumEntity
import com.existmg.library_data.repository.MemorandumRepository
import com.existmg.module_memorandum.R
import com.existmg.module_memorandum.databinding.MemorandumLayoutActivityMemorandumUpdateBinding
import com.existmg.module_memorandum.viewmodel.MemorandumUpdateViewModel

/**
 * @Author ContentMy
 * @Date 2024/4/29 12:23 AM
 * @Description 这里是更新心情的页面，为了区分dialog页面和正常页面，所以使用了两个activity来做新建和更新
 */
class MemorandumUpdateActivity : BaseMVVMActivity<MemorandumUpdateViewModel, MemorandumLayoutActivityMemorandumUpdateBinding>(),
    View.OnClickListener {
    override fun getViewModelClass(): Class<MemorandumUpdateViewModel> {
        return MemorandumUpdateViewModel::class.java
    }

    override fun bindViewModel() {
        mBinding.viewModel = mViewModel
    }

    override fun provideViewModelFactory(): ViewModelProvider.Factory {
        return viewModelFactoryWithParams(MemorandumModuleRoomAccessor.getMemorandumRepository(),application){
            MemorandumUpdateViewModel(it[0] as MemorandumRepository,it[1] as Application)
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.memorandum_layout_activity_memorandum_update
    }

    override fun initView() {
        mBinding.memorandumUpdateToolbar.uiToolbarTvTitle.text = "记录详情"
        mBinding.memorandumUpdateToolbar.uiToolbarTvRight.text = "修改"
    }

    override fun initData() {
        val memorandumEntity = intent.getParcelableExtra<MemorandumEntity>("memorandumEntity")
        if (memorandumEntity != null){
            mViewModel.initMemorandumData(memorandumEntity)
        }
    }

    override fun initListener() {
        mBinding.memorandumUpdateToolbar.uiToolbarTvRight.setOnClickListener(this)
        mBinding.memorandumUpdateToolbar.uiToolbarIvBack.setOnClickListener(this)
        mBinding.memorandumUpdateBtnSave.setOnClickListener(this)
    }

    override fun initObserver() {
        mViewModel.memorandumTimeString.observe(this){
            if (it.isNullOrEmpty()){
                mBinding.memorandumUpdateTvTime.visibility = View.GONE
            }else{
                mBinding.memorandumUpdateTvTime.visibility = View.VISIBLE
            }
        }

        mViewModel.memorandumContentString.observe(this){
            if (it.isNullOrEmpty()){
                mBinding.memorandumUpdateTvContent.text = "这里还没有写内容哦！"
            }
        }
    }

    override fun onClick(v: View?) {
        when(v){
            mBinding.memorandumUpdateToolbar.uiToolbarTvRight->{
                if (mBinding.memorandumUpdateToolbar.uiToolbarTvRight.text.equals("修改")){
                    updateUIChanged()
                }else if (mBinding.memorandumUpdateToolbar.uiToolbarTvRight.text.equals("保存")){
                    saveUIChanged()
                }
            }

            mBinding.memorandumUpdateToolbar.uiToolbarIvBack->{
                finish()
            }

            mBinding.memorandumUpdateBtnSave->{
                saveUIChanged()
            }
        }
    }

    private fun saveUIChanged(){
        mBinding.memorandumUpdateTvTitle.visibility = View.VISIBLE
        mBinding.memorandumUpdateTvContent.visibility = View.VISIBLE
        mBinding.memorandumUpdateTvTime.visibility = View.VISIBLE
        mBinding.memorandumUpdateEtTitle.visibility = View.GONE
        mBinding.memorandumUpdateCl.visibility = View.GONE
        mBinding.memorandumUpdateToolbar.uiToolbarTvTitle.text = "记录详情"
        mBinding.memorandumUpdateToolbar.uiToolbarTvRight.text = "修改"
        mViewModel.updateMemorandum()
    }

    private fun updateUIChanged(){
        mBinding.memorandumUpdateTvTitle.visibility = View.GONE
        mBinding.memorandumUpdateTvContent.visibility = View.GONE
        mBinding.memorandumUpdateTvTime.visibility = View.GONE
        mBinding.memorandumUpdateEtTitle.visibility = View.VISIBLE
        mBinding.memorandumUpdateCl.visibility = View.VISIBLE
        mBinding.memorandumUpdateToolbar.uiToolbarTvTitle.text = "修改记录"
        mBinding.memorandumUpdateToolbar.uiToolbarTvRight.text = "保存"
    }
}