package com.existmg.module_target.ui.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder
import com.existmg.module_target.R
import com.existmg.module_target.databinding.TargetStatusRecycleItemViewBinding
import com.existmg.library_data.enums.DayStatus

/**
 * @Author ContentMy
 * @Date 2024/4/23 1:04 AM
 * @Description 这里是目标状态选择列表的adapter TODO:后续考虑优化掉，统一在UI模块处理
 */
class TargetStatusRecycleViewAdapter(list:MutableList<DayStatus>?):
    BaseQuickAdapter<DayStatus, BaseDataBindingHolder<TargetStatusRecycleItemViewBinding>>(
    R.layout.target_status_recycle_item_view,list){

    override fun convert(holder: BaseDataBindingHolder<TargetStatusRecycleItemViewBinding>, item: DayStatus) {
        holder.dataBinding?.targetStatusRecycleTvName?.text = item.displayName
        mCallback?.statusIconSelected(holder, holder.layoutPosition,item.displayName)
    }

    private var mCallback:OnItemSelectedCallback? = null

    fun setOnItemSelectedCallback(callback: OnItemSelectedCallback){
        mCallback = callback
    }

    interface OnItemSelectedCallback{
        fun statusIconSelected(
            holder: BaseDataBindingHolder<TargetStatusRecycleItemViewBinding>,
            position: Int,
            displayName: String
        )
    }
}