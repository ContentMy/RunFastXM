package com.existmg.module_target.ui

import android.content.Intent
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.android.arouter.facade.annotation.Route
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder
import com.existmg.library_base.fragment.BaseMvvmFragment
import com.existmg.library_base.manager.viewModelFactoryWithParams
import com.existmg.library_common.router.RouterFragmentPath
import com.existmg.module_target.R
import com.existmg.library_data.accessor.TargetModuleRoomAccessor
import com.existmg.module_target.viewmodel.TargetViewModel
import com.existmg.module_target.ui.adapter.TargetRecycleViewAdapter
import com.existmg.library_data.db.entity.TargetEntity
import com.existmg.library_data.repository.TargetRepository
import com.existmg.module_target.databinding.TargetLayoutFragmentBinding
import com.existmg.module_target.databinding.TargetRecycleItemViewBinding

/**
 * @Author ContentMy
 * @Date 2024/4/7 1:31 上午
 * @Description 这里是目标模块的入口Fragment
 */
@Route(path = RouterFragmentPath.Target.PAGER_TARGET)
class TargetFragment:BaseMvvmFragment<TargetViewModel,TargetLayoutFragmentBinding>(),TargetRecycleViewAdapter.OnItemDeleteClickCallback{

    private lateinit var adapter: TargetRecycleViewAdapter
    private var list: MutableList<TargetEntity> = ArrayList()

    override fun getViewModelClass(): Class<TargetViewModel> {
        return TargetViewModel::class.java
    }

    override fun bindViewModel() {
        mBinding.viewModel = mViewModel
    }

    override fun provideViewModelFactory(): ViewModelProvider.Factory {
        return viewModelFactoryWithParams(TargetModuleRoomAccessor.getTargetRepository()){
            TargetViewModel(it[0] as TargetRepository)
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.target_layout_fragment
    }

    override fun initView() {
        adapter = TargetRecycleViewAdapter(list)
        val layoutManager = LinearLayoutManager(context)//需设置布局方向，否则不会展示数据
        mBinding.targetRv.layoutManager = layoutManager
        mBinding.targetRv.adapter = adapter
    }

    override fun initData() {
        mViewModel.refreshData()
        mBinding.targetIncludeTitleToolbar.uiTitleToolbarTvTitle.text = "目标列表"
    }

    override fun initListener() {
        adapter.setOnItemClickListener { adapter, view, position ->
            val entity = adapter.data[position] as TargetEntity
            val intent = Intent(context, TargetCreateActivity::class.java)
            intent.putExtra("isNewTarget",false)
            intent.putExtra("targetEntity", entity)
            startActivity(intent)//点击item时，把数据传递给activity
        }
        adapter.setOnItemDeleteClickCallback(this)
    }

    override fun initObserver() {
        mViewModel.targetData.observe(viewLifecycleOwner){
            mBinding.targetTvDefaultContent.visibility = if (it.isEmpty()) View.VISIBLE else View.GONE
            adapter.setList(it)
        }

        mViewModel.navigateToCreateTargetActivity.observe(viewLifecycleOwner, Observer {
            if (it){
                // 执行跳转
                val intent = Intent(context, TargetCreateActivity::class.java)
                startActivity(intent)
            }
        })
    }

    override fun itemDeleteClick(
        holder: BaseDataBindingHolder<TargetRecycleItemViewBinding>,
        position: Int,
        item: TargetEntity
    ) {
        mViewModel.deleteTarget(item)
    }
}