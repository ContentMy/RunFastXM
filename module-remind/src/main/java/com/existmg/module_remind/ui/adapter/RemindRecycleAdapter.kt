package com.existmg.module_remind.ui.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder
import com.existmg.module_remind.R
import com.existmg.library_data.db.entity.RemindEntity
import com.existmg.module_remind.databinding.RemindRecycleItemViewBinding

/**
 * @Author ContentMy
 * @Date 2024/4/7 6:21 下午
 * @Description 提醒列表对应的adapter
 */
class RemindRecycleAdapter(list:MutableList<RemindEntity>?):BaseQuickAdapter<RemindEntity,BaseDataBindingHolder<RemindRecycleItemViewBinding>>(
    R.layout.remind_recycle_item_view,list){
    override fun convert(holder: BaseDataBindingHolder<RemindRecycleItemViewBinding>, item: RemindEntity) {
        holder.dataBinding?.item = item
        holder.dataBinding?.targetRecycleItemIvIcon?.setImageResource(R.drawable.ui_remind)
    }
}