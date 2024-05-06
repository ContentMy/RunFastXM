package com.existmg.module_memorandum.ui.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder
import com.existmg.library_common.utils.timeLongToString
import com.existmg.library_data.db.entity.MemorandumEntity
import com.existmg.module_memorandum.R
import com.existmg.module_memorandum.databinding.MemorandumLayoutItemMemorandumBinding

/**
 * @Author ContentMy
 * @Date 2024/4/28 2:00 PM
 * @Description
 */
class MemorandumRecycleViewAdapter(list:MutableList<MemorandumEntity>?):
    BaseQuickAdapter<MemorandumEntity, BaseDataBindingHolder<MemorandumLayoutItemMemorandumBinding>>(
        R.layout.memorandum_layout_item_memorandum,list) {
    override fun convert(
        holder: BaseDataBindingHolder<MemorandumLayoutItemMemorandumBinding>,
        item: MemorandumEntity
    ) {
        holder.dataBinding?.memorandumItemTvDate?.text = timeLongToString(item.memorandumCreateTime)
        holder.dataBinding?.memorandumItemTvTitle?.text = item.memorandumTitle
        holder.dataBinding?.memorandumItemTvContent?.text = item.memorandumContent
    }

}