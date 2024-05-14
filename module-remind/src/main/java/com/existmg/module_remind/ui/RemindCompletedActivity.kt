package com.existmg.module_remind.ui

import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder
import com.existmg.library_base.activity.BaseMVVMActivity
import com.existmg.library_base.manager.viewModelFactoryWithParams
import com.existmg.library_data.accessor.RemindModuleRoomAccessor
import com.existmg.library_data.db.entity.RemindEntity
import com.existmg.library_data.repository.RemindRepository
import com.existmg.module_remind.R
import com.existmg.module_remind.databinding.RemindActivityRemindCompletedBinding
import com.existmg.module_remind.databinding.RemindCompletedRecycleItemViewBinding
import com.existmg.module_remind.ui.adapter.RemindCompleteRecycleAdapter
import com.existmg.module_remind.viewmodel.RemindCompletedViewmodel

/**
 * @Author ContentMy
 * @Date 2024/5/14 12:46 AM
 * @Description
 */
class RemindCompletedActivity : BaseMVVMActivity<RemindCompletedViewmodel,RemindActivityRemindCompletedBinding>() {
    private lateinit var mAdapter:RemindCompleteRecycleAdapter
    private val mList = mutableListOf<RemindEntity>()
    override fun getViewModelClass(): Class<RemindCompletedViewmodel> {
        return RemindCompletedViewmodel::class.java
    }

    override fun bindViewModel() {
        mBinding.viewModel = mViewModel
    }

    override fun provideViewModelFactory(): ViewModelProvider.Factory {
        return viewModelFactoryWithParams(RemindModuleRoomAccessor.getRemindRepository()){
            RemindCompletedViewmodel(it[0] as RemindRepository)
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.remind_activity_remind_completed
    }


    override fun initView() {
        mAdapter = RemindCompleteRecycleAdapter(mList)
        val layoutManager = LinearLayoutManager(this)//需设置布局方向，否则不会展示数据
        mBinding.remindCompletedRv.layoutManager = layoutManager
        mBinding.remindCompletedRv.adapter = mAdapter
    }
    override fun initData() {
        mViewModel.refreshData()
        mBinding.remindCompletedToolbar.uiToolbarTvRight.text = "清空"
        mBinding.remindCompletedToolbar.uiToolbarTvTitle.text = "已完成提醒"
    }

    override fun initListener() {
        mBinding.remindCompletedToolbar.uiToolbarIvBack.setOnClickListener {
            finish()

        }
        mBinding.remindCompletedToolbar.uiToolbarTvRight.setOnClickListener {
            mViewModel.deleteAllCompletedRemind()
        }

        mAdapter.setOnItemDeleteClickCallback(object : RemindCompleteRecycleAdapter.OnItemDeleteClickCallback{
            override fun itemDeleteClick(
                item: RemindEntity
            ) {
                mViewModel.deleteRemind(item)
            }

        })
    }

    override fun initObserver() {
        mViewModel.remindCompleteData.observe(this){
            mBinding.remindCompletedTvDefaultContent.visibility = if (it.isEmpty()) View.VISIBLE else View.GONE
            mAdapter.setList(it)
        }
    }
}