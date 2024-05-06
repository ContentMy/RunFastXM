package com.existmg.module_memorandum.ui

import android.content.Intent
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.android.arouter.facade.annotation.Route
import com.existmg.library_base.fragment.BaseMvvmFragment
import com.existmg.library_base.manager.viewModelFactoryWithParams
import com.existmg.library_common.router.RouterFragmentPath
import com.existmg.library_data.accessor.MemorandumModuleRoomAccessor
import com.existmg.library_data.db.entity.MemorandumEntity
import com.existmg.library_data.repository.MemorandumRepository
import com.existmg.library_ui.view.DiaryDividerItemDecoration
import com.existmg.module_memorandum.R
import com.existmg.module_memorandum.databinding.MemorandumLayoutFragmentBinding
import com.existmg.module_memorandum.ui.adapter.MemorandumRecycleViewAdapter
import com.existmg.module_memorandum.viewmodel.MemorandumViewModel

/**
 * @Author ContentMy
 * @Date 2024/4/7 1:24 上午
 * @Description 这里是记录生活模块的页面入口
 */
@Route(path = RouterFragmentPath.Memorandum.PAGER_MEMORANDUM)
class MemorandumFragment : BaseMvvmFragment<MemorandumViewModel,MemorandumLayoutFragmentBinding>(){
    private lateinit var mAdapter:MemorandumRecycleViewAdapter
    private var mList = mutableListOf<MemorandumEntity>()
    override fun getLayoutId(): Int {
        return R.layout.memorandum_layout_fragment
    }

    override fun getViewModelClass(): Class<MemorandumViewModel> {
        return MemorandumViewModel::class.java
    }

    override fun bindViewModel() {
        mBinding.viewModel = mViewModel
    }

    override fun provideViewModelFactory(): ViewModelProvider.Factory {
        return viewModelFactoryWithParams(MemorandumModuleRoomAccessor.getMemorandumRepository()){
            MemorandumViewModel(it[0] as MemorandumRepository)
        }
    }

    override fun initView() {
        mAdapter = MemorandumRecycleViewAdapter(mList)
        val linearLayoutManager = LinearLayoutManager(context)
        mBinding.memorandumRv.layoutManager = linearLayoutManager
        mBinding.memorandumRv.adapter = mAdapter
        val mDividerItemDecoration = DiaryDividerItemDecoration(requireActivity(),R.id.memorandum_item_tv_date)//设置时间轴
        mBinding.memorandumRv.addItemDecoration(mDividerItemDecoration)
    }

    override fun initData() {
        mViewModel.refreshData()
        mBinding.memorandumIncludeTitleToolbar.uiTitleToolbarTvTitle.text = "记录生活"
    }

    override fun initListener() {
        mViewModel.navigateToCreateMemorandumActivity.observe(this){
            if (it){
                val intent = Intent(context,MemorandumCreateActivity::class.java)
                startActivity(intent)
            }
        }

        mViewModel.navigateShowDialog.observe(this){
            if (it){
                val intent = Intent(context,MemorandumCreateActivity::class.java)
                startActivity(intent)
            }
        }

        mAdapter.setOnItemClickListener { adapter, view, position ->
            val entity = adapter.data[position] as MemorandumEntity
            val intent = Intent(context, MemorandumUpdateActivity::class.java)
            intent.putExtra("memorandumEntity", entity)
            startActivity(intent)//点击item时，把数据传递给activity
        }

        mViewModel.memorandumData.observe(viewLifecycleOwner){
            mBinding.memorandumTvDefaultContent.visibility = if (it.isEmpty()) View.VISIBLE else View.GONE
            mAdapter.setList(it)
        }
    }

    override fun onResume() {
        super.onResume()
        mViewModel.refreshData()
    }
}