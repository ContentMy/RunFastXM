package com.existmg.module_target.ui.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder
import com.existmg.module_target.R
import com.existmg.library_data.db.entity.TargetEntity
import com.existmg.module_target.databinding.TargetRecycleItemViewBinding

/**
 * @Author ContentMy
 * @Date 2024/4/7 6:21 下午
 * @Description 这里是目标列表的adapter
 */
class TargetRecycleViewAdapter(list:MutableList<TargetEntity>?):
    BaseQuickAdapter<TargetEntity,BaseDataBindingHolder<TargetRecycleItemViewBinding>>(R.layout.target_recycle_item_view,list) {
    override fun convert(holder: BaseDataBindingHolder<TargetRecycleItemViewBinding>, item: TargetEntity) {
        holder.dataBinding?.item = item
        holder.dataBinding?.targetRecycleItemIvIcon?.setImageResource(context.resources.getIdentifier(item.targetImg, "drawable", context.packageName))
        holder.dataBinding?.targetRecycleItemTvDelete?.setOnClickListener {
            mCallback?.itemDeleteClick(holder,holder.layoutPosition,item)
            remove(item)
            notifyItemChanged(holder.layoutPosition)
        }
    }
    private var mCallback:OnItemDeleteClickCallback? = null
    fun setOnItemDeleteClickCallback(callback: OnItemDeleteClickCallback){
        mCallback = callback
    }
    interface OnItemDeleteClickCallback{
        fun itemDeleteClick(
            holder: BaseDataBindingHolder<TargetRecycleItemViewBinding>,
            position: Int,
            item: TargetEntity
        )
    }
}