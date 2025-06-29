package com.existmg.module_remind.ui

import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.existmg.library_common.activity.BaseMVVMActivity
import com.existmg.library_common.managers.viewModelFactoryWithParams
import com.existmg.library_data.accessor.RemindModuleRoomAccessor
import com.existmg.library_data.db.entity.RemindEntity
import com.existmg.library_data.repository.RemindRepository
import com.existmg.library_ui.notification.NotificationRepository
import com.existmg.module_remind.R
import com.existmg.module_remind.databinding.RemindActivityRemindCompletedBinding
import com.existmg.module_remind.ui.adapter.RemindCompleteRecycleAdapter
import com.existmg.module_remind.utils.logs.RemindLoggerManager
import com.existmg.module_remind.viewmodel.RemindCompletedViewmodel

/**
 * @Author ContentMy
 * @Date 2024/5/14 12:46 AM
 * @Description
 */
class RemindCompletedActivity : BaseMVVMActivity<RemindCompletedViewmodel, RemindActivityRemindCompletedBinding>() {
    private val mLog = RemindLoggerManager.getLogger<RemindCompletedActivity>()
    private lateinit var mAdapter:RemindCompleteRecycleAdapter
    private val mList = mutableListOf<RemindEntity>()
    override fun getViewModelClass(): Class<RemindCompletedViewmodel> {
        return RemindCompletedViewmodel::class.java
    }

    override fun bindViewModel() {
        mBinding.viewModel = mViewModel
    }

    override fun provideViewModelFactory(): ViewModelProvider.Factory {
        return viewModelFactoryWithParams(RemindModuleRoomAccessor.getRemindRepository(),NotificationRepository()){
            RemindCompletedViewmodel(it[0] as RemindRepository,it[1] as NotificationRepository)
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
        mBinding.remindCompletedToolbar.uiToolbarTvRight.text = resources.getString(R.string.remind_string_clear)
        mBinding.remindCompletedToolbar.uiToolbarTvTitle.text = resources.getString(R.string.remind_string_completed_remind)
    }

    override fun initListener() {
        mBinding.remindCompletedToolbar.uiToolbarIvBack.setOnClickListener {
            finish()

        }
        mBinding.remindCompletedToolbar.uiToolbarTvRight.setOnClickListener {
            mViewModel.deleteAllCompletedRemind()
        }

        //TODO：目前使用了BaseQuickAdapter,但自定义实现的RecycleView的左滑效果必须基于这个adapter的点击事件监听后，事件才能被处理，后续优化考虑统一使用memorandum模块的封装adapter
        mAdapter.setOnItemClickListener { adapter, view, position ->
            mLog.debug("点击了item哦")
        }
        mAdapter.setOnItemDeleteClickCallback(object : RemindCompleteRecycleAdapter.OnItemDeleteClickCallback{
            override fun itemDeleteClick(
                item: RemindEntity
            ) {
                mViewModel.deleteRemind(item)
            }

            override fun itemResetClick(item: RemindEntity) {
                mViewModel.resetRemind(item)//TODO:优化方向：点击重新提醒时，item的消失动画以及item下面的item列表上移较为生硬，需要优化
            }

        })
    }

    override fun initObserver() {
        mViewModel.remindCompleteData.observe(this){
            mBinding.remindCompletedTvDefaultContent.visibility = if (it.isEmpty()) View.VISIBLE else View.GONE
            mAdapter.setList(it)
        }

        mViewModel.remindResetData.observe(this){
            mViewModel.startNotification(it,application)
            mViewModel.startRemindStatusWork(it,application)
        }
    }
}