package com.existmg.module_target.ui.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder
import com.existmg.library_data.db.entity.IconEntity
import com.existmg.library_ui.utils.setDrawableImageByName
import com.existmg.module_target.R
import com.existmg.module_target.databinding.TargetIconsRecycleItemViewBinding
import com.existmg.module_target.ui.TargetCreateActivity

/**
 * @Author ContentMy
 * @Date 2024/4/23 1:04 AM
 * @Description 这里是目标图标选择的列表adapter
 */
class TargetIconsRecycleViewAdapter(list:MutableList<IconEntity>?):
    BaseQuickAdapter<IconEntity, BaseDataBindingHolder<TargetIconsRecycleItemViewBinding>>(
    R.layout.target_icons_recycle_item_view,list){

    override fun convert(holder: BaseDataBindingHolder<TargetIconsRecycleItemViewBinding>, item: IconEntity) {
//        holder.dataBinding?.targetStatusRecycleTvName?.text = item.name
        holder.dataBinding?.targetIconsRecycleIvIcon?.setDrawableImageByName(item.name)
        mCallback?.iconItemSelected(holder, holder.layoutPosition,item.name)
    }

    private var mCallback:OnIconsItemSelectedCallback? = null

    fun setOnIconsItemSelectedCallback(callback: OnIconsItemSelectedCallback){
        mCallback = callback
    }

    interface OnIconsItemSelectedCallback{
        fun iconItemSelected(
            holder: BaseDataBindingHolder<TargetIconsRecycleItemViewBinding>,
            position: Int,
            displayName: String
        )
    }
}