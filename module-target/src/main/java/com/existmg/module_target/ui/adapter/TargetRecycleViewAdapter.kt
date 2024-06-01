package com.existmg.module_target.ui.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder
import com.existmg.module_target.R
import com.existmg.library_data.db.entity.TargetEntity
import com.existmg.library_data.db.entity.TargetWithCheckInList
import com.existmg.library_data.db.entity.TargetWithTodayCheckIn
import com.existmg.module_target.databinding.TargetRecycleItemViewBinding
import com.existmg.module_target.utils.logs.TargetLoggerManager

/**
 * @Author ContentMy
 * @Date 2024/4/7 6:21 下午
 * @Description 这里是目标列表的adapter
 */
class TargetRecycleViewAdapter(list:MutableList<TargetWithTodayCheckIn>?):
    BaseQuickAdapter<TargetWithTodayCheckIn,BaseDataBindingHolder<TargetRecycleItemViewBinding>>(R.layout.target_recycle_item_view,list) {
    private val mLog = TargetLoggerManager.getLogger<TargetRecycleViewAdapter>()
    override fun convert(holder: BaseDataBindingHolder<TargetRecycleItemViewBinding>, item: TargetWithTodayCheckIn) {
        val targetEntity = item.targetEntity
        val targetCheckInEntity = item.targetCheckInEntity
        holder.dataBinding?.item = targetEntity
        holder.dataBinding?.targetRecycleItemIvIcon?.setImageResource(context.resources.getIdentifier(targetEntity?.targetImg, "drawable", context.packageName))
        if (targetCheckInEntity!=null){
            mLog.debug("在adapter中展示时，打卡的状态${targetCheckInEntity.targetCheckIn}")
            holder.dataBinding?.targetRecycleItemTvCheckIn?.text = if (targetCheckInEntity.targetCheckIn)
                context.resources.getString(R.string.target_item_check_in_completed_string) else context.resources.getString(R.string.target_item_check_in_string)
        }else{
            mLog.debug("在adapter中展示时,没有targetCheckInEntity")
            holder.dataBinding?.targetRecycleItemTvCheckIn?.text = context.resources.getString(R.string.target_item_check_in_string)
        }
        holder.dataBinding?.targetRecycleItemTvDelete?.setOnClickListener {
            mItemDeleteClickCallback?.itemDeleteClick(holder,holder.layoutPosition,targetEntity!!)
            remove(item)
            notifyItemChanged(holder.layoutPosition)
        }
        holder.dataBinding?.targetRecycleItemTvCheckIn?.setOnClickListener {
            mCheckInCallback?.itemCheckIn(item, targetCheckInEntity?.targetCheckIn ?: false)
            notifyItemChanged(holder.layoutPosition)
        }
    }
    private var mItemDeleteClickCallback:OnItemDeleteClickCallback? = null
    private var mCheckInCallback:OnItemCheckInCallback? = null
    fun setOnItemDeleteClickCallback(callback: OnItemDeleteClickCallback){
        mItemDeleteClickCallback = callback
    }
    fun setOnItemCheckInCallback(callback: OnItemCheckInCallback){
        mCheckInCallback = callback
    }

    interface OnItemDeleteClickCallback{
        fun itemDeleteClick(
            holder: BaseDataBindingHolder<TargetRecycleItemViewBinding>,
            position: Int,
            item: TargetEntity
        )
    }
    interface OnItemCheckInCallback{
        fun itemCheckIn(item: TargetWithTodayCheckIn, targetCheckIn: Boolean)
    }
}