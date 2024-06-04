package com.existmg.module_memorandum.ui.adapter

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.existmg.library_data.db.entity.MemorandumImgEntity
import com.existmg.module_memorandum.R
import com.existmg.module_memorandum.databinding.MemorandumLayoutItemItemImgBinding


/**
 * @Author ContentMy
 * @Date 2024/4/28 2:00 PM
 * @Description
 */
class MemorandumItemImgRecycleViewAdapter(private val context: Context): RecyclerView.Adapter<MemorandumItemImgRecycleViewAdapter.MemorandumDataBindingViewHolder>(){
    private val imageItems = mutableListOf<MemorandumImgEntity>()
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MemorandumDataBindingViewHolder {
        val view = DataBindingUtil.inflate<MemorandumLayoutItemItemImgBinding>(
            LayoutInflater.from(parent.context),
            R.layout.memorandum_layout_item_item_img,parent,false);
        return MemorandumDataBindingViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: MemorandumDataBindingViewHolder,
        position: Int
    ) {
        val item = imageItems[position]
        Glide.with(context)
            .load(Uri.parse(item.memorandumImgFilePath))
            .apply(RequestOptions().centerCrop()) // 使用centerCrop()使图片居中裁剪
            .into(holder.dataBinding.memorandumItemImgIvImg)
    }


    override fun getItemCount(): Int = imageItems.size
    fun setImages(images:List<MemorandumImgEntity>) {
        imageItems.clear()
        images.forEach {
            imageItems.add(it)
        }
        notifyDataSetChanged()
    }

    //创建使用DataBinding的Holder
    class MemorandumDataBindingViewHolder(itemDataBinding:MemorandumLayoutItemItemImgBinding):RecyclerView.ViewHolder(itemDataBinding.root){
        var dataBinding:MemorandumLayoutItemItemImgBinding = itemDataBinding

    }
}