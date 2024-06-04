package com.existmg.module_memorandum.ui.adapter

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.existmg.module_memorandum.R
import com.existmg.module_memorandum.databinding.MemorandumLayoutItemCreateImgBinding
import com.existmg.module_memorandum.model.MemorandumImageItem


/**
 * @Author ContentMy
 * @Date 2024/4/28 2:00 PM
 * @Description
 */
class MemorandumCreateImgRecycleViewAdapter(
    private val context: Context,
    private val isNeedPlaceholder: Boolean = true,
    private val onAddImageClick: () -> Unit,
    private val onDeleteImageClick: (Int) -> Unit
    ): RecyclerView.Adapter<MemorandumCreateImgRecycleViewAdapter.MemorandumDataBindingViewHolder>(){
    private val imageItems = mutableListOf<MemorandumImageItem>()
    init {
        // 添加默认的占位图
        if (isNeedPlaceholder){
            imageItems.add(MemorandumImageItem(isPlaceholder = true))
        }
    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MemorandumDataBindingViewHolder {
        val view = DataBindingUtil.inflate<MemorandumLayoutItemCreateImgBinding>(
            LayoutInflater.from(parent.context),
            R.layout.memorandum_layout_item_create_img,parent,false);
        return MemorandumDataBindingViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: MemorandumDataBindingViewHolder,
        position: Int
    ) {
        val item = imageItems[position]

        if (item.isPlaceholder) {
            Glide.with(context)
                .load(R.drawable.ui_add_img)
                .apply(RequestOptions().centerCrop()) // 使用centerCrop()使图片居中裁剪
                .into(holder.dataBinding.memorandumItemCreateImgIvImg)
        } else {
            Glide.with(context)
                .load(item.uri)
                .apply(RequestOptions().centerCrop()) // 使用centerCrop()使图片居中裁剪
                .into(holder.dataBinding.memorandumItemCreateImgIvImg)
        }
        holder.dataBinding.memorandumItemCreateImgIvImg.setOnClickListener {
            if (item.isPlaceholder){
                onAddImageClick()
            }
        }
    }


    override fun getItemCount(): Int = imageItems.size

    fun addImage(uri: Uri) {
        if (imageItems.size <= 9) {
            // 移除占位图
            if (imageItems.size > 0 && isNeedPlaceholder){
                imageItems.removeAt(imageItems.size - 1)
            }
            imageItems.add(MemorandumImageItem(uri = uri))

            // 如果少于9张图片，重新添加占位图
            if (imageItems.size < 9 && isNeedPlaceholder) {
                imageItems.add(MemorandumImageItem(isPlaceholder = true))
            }

            notifyDataSetChanged()
        }
    }

    fun deleteImage(position: Int) {
        imageItems.removeAt(position)

        // 如果少于9张图片，重新添加占位图
        if (imageItems.size < 9 && !imageItems.last().isPlaceholder) {
            imageItems.add(MemorandumImageItem(isPlaceholder = true))
        }

        notifyDataSetChanged()
    }

    fun getImgList():List<MemorandumImageItem> {
        return imageItems.filter { !it.isPlaceholder }
    }


    //创建使用DataBinding的Holder
    class MemorandumDataBindingViewHolder(itemDataBinding:MemorandumLayoutItemCreateImgBinding):RecyclerView.ViewHolder(itemDataBinding.root){
        var dataBinding:MemorandumLayoutItemCreateImgBinding = itemDataBinding

    }
}