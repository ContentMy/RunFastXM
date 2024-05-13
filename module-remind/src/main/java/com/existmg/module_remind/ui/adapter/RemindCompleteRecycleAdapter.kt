package com.existmg.module_remind.ui.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder
import com.existmg.module_remind.R
import com.existmg.library_data.db.entity.RemindEntity
import com.existmg.module_remind.databinding.RemindCompletedRecycleItemViewBinding
import com.existmg.module_remind.databinding.RemindRecycleItemViewBinding

/**
 * @Author ContentMy
 * @Date 2024/4/7 6:21 下午
 * @Description 提醒列表对应的adapter
 */
class RemindCompleteRecycleAdapter(list:MutableList<RemindEntity>?):BaseQuickAdapter<RemindEntity,BaseDataBindingHolder<RemindCompletedRecycleItemViewBinding>>(
    R.layout.remind_completed_recycle_item_view,list){
    override fun convert(holder: BaseDataBindingHolder<RemindCompletedRecycleItemViewBinding>, item: RemindEntity) {
        holder.dataBinding?.item = item
        holder.dataBinding?.targetCompletedRecycleItemIvIcon?.setImageResource(R.drawable.ui_remind_completed)
        holder.dataBinding?.targetCompletedRecycleItemTvDelete?.setOnClickListener{
            mCallback?.itemDeleteClick(item)
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
            item: RemindEntity
        )
    }
}