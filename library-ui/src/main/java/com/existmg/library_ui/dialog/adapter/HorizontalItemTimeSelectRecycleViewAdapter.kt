package com.existmg.library_ui.dialog.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder
import com.existmg.library_ui.R
import com.existmg.library_ui.databinding.UiLayoutHorizontalTimeSelectItemBinding

/**
 * @Author ContentMy
 * @Date 2024/4/27 12:10 AM
 * @Description 这里是横向选择String条目的RecyclerAdapter e.g 新建提醒页面的时间选择列表
 */
class HorizontalItemTimeSelectRecycleViewAdapter(list:MutableList<String>?):
    BaseQuickAdapter<String, BaseDataBindingHolder<UiLayoutHorizontalTimeSelectItemBinding>>(
        R.layout.ui_layout_horizontal_time_select_item,list){

    override fun convert(holder: BaseDataBindingHolder<UiLayoutHorizontalTimeSelectItemBinding>, item: String) {
        holder.dataBinding?.uiItemHorizontalTimeSelectTvTime?.text = item
        mCallback?.horizontalItemSelected(holder, holder.layoutPosition,item)
    }

    private var mCallback:OnHorizontalItemSelectedCallback? = null

    fun setOnHorizontalItemSelectedCallback(callback: OnHorizontalItemSelectedCallback){
        mCallback = callback
    }

    interface OnHorizontalItemSelectedCallback{
        fun horizontalItemSelected(
            holder: BaseDataBindingHolder<UiLayoutHorizontalTimeSelectItemBinding>,
            position: Int,
            displayName: String
        )
    }
}