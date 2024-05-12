package com.existmg.module_memorandum.ui

import android.content.Intent
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.android.arouter.facade.annotation.Route
import com.existmg.library_base.fragment.BaseMvvmFragment
import com.existmg.library_base.manager.viewModelFactoryWithParams
import com.existmg.library_common.interfaces.OnItemClickListener
import com.existmg.library_common.interfaces.OnItemLongClickListener
import com.existmg.library_common.router.RouterFragmentPath
import com.existmg.library_data.accessor.MemorandumModuleRoomAccessor
import com.existmg.library_data.db.entity.MemorandumEntity
import com.existmg.library_data.repository.MemorandumRepository
import com.existmg.library_ui.views.DiaryDividerItemDecoration
import com.existmg.module_memorandum.R
import com.existmg.module_memorandum.databinding.MemorandumLayoutFragmentBinding
import com.existmg.module_memorandum.interfaces.MemorandumItemDeleteCallback
import com.existmg.module_memorandum.ui.adapter.MemorandumRecycleViewAdapter
import com.existmg.module_memorandum.viewmodel.MemorandumViewModel
import kotlin.math.abs

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
        mAdapter.setOnItemClickListener(object:OnItemClickListener{
            override fun onItemClick(view: View, position: Int) {
                val entity = mList[position]
                val intent = Intent(context, MemorandumUpdateActivity::class.java)
                intent.putExtra("memorandumEntity", entity)
                startActivity(intent)//点击item时，把数据传递给activity
            }
        })
        
        mAdapter.setOnItemLongClickListener(object:OnItemLongClickListener{
            override fun onItemLongClick(view: View, position: Int) {
//                mAdapter.setShaking(true)
            }
        })

        mAdapter.setMemorandumItemDeleteCallback(object : MemorandumItemDeleteCallback{
            override fun itemDelete(entity: MemorandumEntity, position: Int) {
                mViewModel.deleteMemorandum(entity)
                mList.remove(entity)
            }

        })

        mBinding.memorandumRv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                // 如果RecyclerView正在滚动，隐藏所有删除按钮
                if (abs(dy) > 10) {//暂时避免轻微滑动就执行隐藏代码
                    mAdapter.hideDeleteButton()
                }
            }
        })
    }

    override fun initObserver() {
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

        
        mViewModel.memorandumData.observe(viewLifecycleOwner){
            mBinding.memorandumTvDefaultContent.visibility = if (it.isEmpty()) View.VISIBLE else View.GONE
            mList.clear()
            it.forEach { entity ->
                mList.add(entity)
            }
            mAdapter.setListDeleteStatus(it.size)
        }
    }

    override fun onResume() {
        super.onResume()
        mViewModel.refreshData()
    }

    override fun onPause() {
        super.onPause()
        mAdapter.hideDeleteButton()
    }
}