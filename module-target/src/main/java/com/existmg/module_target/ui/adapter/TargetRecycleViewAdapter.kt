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
    BaseQuickAdapter<TargetEntity,BaseDataBindingHolder<TargetRecycleItemViewBinding>>(R.layout.target_recycle_item_view,list)
//    OnItemClickListener,
//    OnItemChildClickListener
    {
    private var mList = list
    override fun convert(holder: BaseDataBindingHolder<TargetRecycleItemViewBinding>, item: TargetEntity) {
        holder.dataBinding?.item = item
        holder.dataBinding?.targetRecycleItemIvIcon?.setImageResource(context.resources.getIdentifier(item.targetImg, "drawable", context.packageName))
    }

//    override fun onItemClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
//        println("这里是adapter的回调 onItemClick ")
//    }
//
//    override fun onItemChildClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
////        Toast.makeText(view.context,"点击了item",Toast.LENGTH_SHORT).show()
//    }

}